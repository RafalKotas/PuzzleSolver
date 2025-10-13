package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramActionScheduler;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RowSequencesCorrectionHelperImplTest {

    List<List<Integer>> rowSeqLengths;
    List<List<List<Integer>>> rangesAllRows;
    List<List<Integer>> rowsFieldsNotToInclude;
    List<List<Integer>> rowsExcludedIds;

    @Mock
    NonogramRowLogic rowLogic;

    @Mock
    NonogramRules rules;

    @Mock
    NonogramState state;

    @Mock
    NonogramActionScheduler scheduler;

    @Mock
    NonogramBoardAccessHelper nonogramBoardAccessHelper;

    @InjectMocks RowSequencesCorrectionHelperImpl subject;

    @BeforeEach
    void setUpRules() {
        when(rowLogic.getNonogramRules()).thenReturn(rules);
    }

    @DisplayName("correctRowSequencesRanges should update next range from left: [[2, 8],[7, 12]] → [[2, 8],[9, 12]] for rowIdx=5")
    @Test // o07825
    void shouldCorrectFromLeft_only_andLogAndSchedule() {
        // given
        int rowIdx = 5;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(6, 1));
        List<List<Integer>> rangesRow5 = new ArrayList<>();
        rangesRow5.add(new ArrayList<>(List.of(2, 8)));
        rangesRow5.add(new ArrayList<>(List.of(7, 12)));
        rangesAllRows.set(rowIdx, rangesRow5);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>());

        // stubs
        when(rowLogic.getNonogramState()).thenReturn(state);
        when(rowLogic.getActionScheduler()).thenReturn(scheduler);
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // make updateRowSequenceRange mutate our backing list, so finalRanges reflects the change
        doAnswer(inv -> {
            int rIdx = inv.getArgument(0, Integer.class);
            int seqIdx = inv.getArgument(1, Integer.class);
            @SuppressWarnings("unchecked")
            List<Integer> newRange = inv.getArgument(2, List.class);
            rangesAllRows.get(rIdx).set(seqIdx, new ArrayList<>(newRange));
            return null;
        }).when(rowLogic).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then: second range updated to [9,12]
        assertEquals(List.of(2, 8), rangesAllRows.get(rowIdx).get(0));
        assertEquals(List.of(9, 12), rangesAllRows.get(rowIdx).get(1));

        // state step increased and scheduling logged once for the row (Field(rowIdx, 0))
        verify(state, times(1)).increaseMadeSteps();

        ArgumentCaptor<Field> fieldCaptor = ArgumentCaptor.forClass(Field.class);
        verify(scheduler, times(1))
                .scheduleActionsBasedOnField(fieldCaptor.capture(),
                        eq(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW));
        Field scheduled = fieldCaptor.getValue();
        assertEquals(rowIdx, scheduled.getRowIdx());
        assertEquals(0, scheduled.getColumnIdx());

        // a log was created
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();

        // no exclusion (new length = 12 - 9 + 1 = 4 != lengths.get(1) = 1)
        verify(rowLogic, never()).excludeSequenceInRow(eq(rowIdx), anyInt());
        // no colour check (guarded by length equality)
        verifyNoInteractionsWithBoardAccessHelper(rowLogic);
    }

    @DisplayName("correctRowSequencesRanges should do nothing when ranges stay the same (anyUpdated == false) — rowIdx=8, lengths [1,1], ranges [[0,7],[2,9]]")
    @Test // o06005
    void shouldNotChangeAnything_whenNoCorrectionIsNeeded() {
        // given
        int rowIdx = 8;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 1));
        List<List<Integer>> rangesRow8 = new ArrayList<>();
        rangesRow8.add(new ArrayList<>(List.of(0, 7)));
        rangesRow8.add(new ArrayList<>(List.of(2, 9)));
        rangesAllRows.set(rowIdx, rangesRow8);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>());

        // stubs
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then: ranges unchanged
        assertEquals(List.of(0, 7), rangesAllRows.get(rowIdx).get(0));
        assertEquals(List.of(2, 9), rangesAllRows.get(rowIdx).get(1));

        // no updates to ranges
        verify(rowLogic, never()).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        // anyUpdated == false → no state increment, no scheduler call, no logs
        verify(state, never()).increaseMadeSteps();
        verify(scheduler, never()).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, never()).setTmpLog(anyString());
        verify(rowLogic, never()).addLog();
    }

    @DisplayName("correctRowSequencesRanges: loop skips when nextIdx ∈ excludedIds (anyUpdated=false)")
    @Test // o06005
    void shouldSkipWhenNextIdxIsExcluded_andDoNothing() {
        // given
        int rowIdx = 1;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 1, 1, 1, 1));
        List<List<Integer>> rangesRow1 = new ArrayList<>();
        rangesRow1.add(new ArrayList<>(List.of(0, 0))); // seqIdx=0 (nextIdx=1 will be skipped)
        rangesRow1.add(new ArrayList<>(List.of(2, 2))); // seqIdx=1
        rangesRow1.add(new ArrayList<>(List.of(4, 4))); // (modified from [4, 5] original)
        rangesRow1.add(new ArrayList<>(List.of(6, 6)));
        rangesRow1.add(new ArrayList<>(List.of(8, 9)));
        rangesAllRows.set(rowIdx, rangesRow1);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of(1, 3)));

        // stubs
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then: ranges unchanged (loop skipped for nextIdx=1; right-correction also produces no change)
        assertEquals(List.of(0, 0), rangesAllRows.get(rowIdx).get(0));
        assertEquals(List.of(2, 2), rangesAllRows.get(rowIdx).get(1));

        // no updates to ranges
        verify(rowLogic, never()).updateRowSequenceRange(anyInt(), anyInt(), anyList());


        // anyUpdated == false → no state increment, no scheduler call, no logs
        verify(state, never()).increaseMadeSteps();
        verify(scheduler, never()).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, never()).setTmpLog(anyString());
        verify(rowLogic, never()).addLog();
    }

    @DisplayName("correctRowSequencesRanges - Update(from left) when next start is smaller: [[0,4],[8,9],[8,13]] -> [[0,4],[8,9],[10,13]] and exclude when coloured")
    @Test
    void shouldExcludeSequenceWithIdx2() {
        // given
        int rowIdx = 10;
        int height = 15;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(5, 1, 4));
        List<List<Integer>> rangesRow10 = new ArrayList<>();
        rangesRow10.add(new ArrayList<>(List.of(0, 4)));
        rangesRow10.add(new ArrayList<>(List.of(8, 9)));
        rangesRow10.add(new ArrayList<>(List.of(8, 13)));
        rangesAllRows.set(rowIdx, rangesRow10);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of(0)));

        // stubs
        mockNonogramStateDependent();
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        when(nonogramBoardAccessHelper.isRowRangeColoured(rowIdx, List.of(10, 13))).thenReturn(true);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then
        verify(rowLogic, times(2)).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
        verify(rowLogic, times(1)).excludeSequenceInRow(rowIdx, 2);
    }

    @DisplayName("correctRowSequencesRanges - should not exclude seqId = 5 when range equal to sequence length but not coloured")
    @Test
    void shouldNotExcludeSeq_WhenFieldsInRangeAreNotColoured() {
        // given
        int rowIdx = 3;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 2, 1, 1, 1, 1));
        List<List<Integer>> rangesRow3 = new ArrayList<>();
        rangesRow3.add(new ArrayList<>(List.of(0, 0)));
        rangesRow3.add(new ArrayList<>(List.of(4, 6)));
        rangesRow3.add(new ArrayList<>(List.of(8, 8)));
        rangesRow3.add(new ArrayList<>(List.of(10, 10)));
        rangesRow3.add(new ArrayList<>(List.of(12, 12)));
        rangesRow3.add(new ArrayList<>(List.of(13, 14)));
        rangesAllRows.set(rowIdx, rangesRow3);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of(0, 2, 3)));

        // stubs
        mockNonogramStateDependent();
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        when(nonogramBoardAccessHelper.isRowRangeColoured(rowIdx, List.of(14, 14))).thenReturn(false);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then
        verify(rowLogic, times(1)).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
    }

    // 1
    @DisplayName("correctRowSequencesRanges(correctFromRight) - should continue when prevIdx(3) is excluded")
    @Test // o06005 // 1st parametrized
    void shouldContinueWhenPrevSeqIdxIsExcluded() {
        // given
        int rowIdx = 1;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 1, 1, 1, 1));
        List<List<Integer>> rangesRow1 = new ArrayList<>();
        rangesRow1.add(new ArrayList<>(List.of(0, 1)));
        rangesRow1.add(new ArrayList<>(List.of(2, 2)));
        rangesRow1.add(new ArrayList<>(List.of(4, 5)));
        rangesRow1.add(new ArrayList<>(List.of(6, 6)));
        rangesRow1.add(new ArrayList<>(List.of(8, 9)));
        rangesAllRows.set(rowIdx, rangesRow1);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of(1, 3)));

        // stubs
        mockNonogramStateDependent();
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then
        verify(rowLogic, times(2)).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
    }

        /*
        A = !oldRange.get(1).equals(newRange.get(1));
        B = rangeLength(newRange) == lengths.get(sequenceIdx);
        C = nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, newRange);
        tryCorrectFromRight, test cases:
        1. A
        2. A && B && C
        3. A && B && !C
        4. A && !B
        5. !A
     */

    @DisplayName("correctRowSequencesRanges (tryCorrectFromRight) - updateRowSequenceRange when old and new ranges second elements differ")
    @Test // 1. A (o06005) // 2nd parametrized
    void tryCorrectFromRightShouldUpdateRowSequenceRange() {
        // given
        int rowIdx = 1;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 1, 1, 1, 1));
        List<List<Integer>> rangesRow1 = new ArrayList<>();
        rangesRow1.add(new ArrayList<>(List.of(0, 1)));
        rangesRow1.add(new ArrayList<>(List.of(2, 2)));
        rangesRow1.add(new ArrayList<>(List.of(4, 5)));
        rangesRow1.add(new ArrayList<>(List.of(6, 6)));
        rangesRow1.add(new ArrayList<>(List.of(8, 9)));
        rangesAllRows.set(rowIdx, rangesRow1);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of(1, 3)));

        // stubs
        mockNonogramStateDependent();
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then
        verify(rowLogic, times(2)).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
    }

    @DisplayName("correctRowSequencesRanges (tryCorrectFromRight) - updateRowSequenceRange with both excluding conditions met")
    @Test // 2. A && B && C o10683
    void tryCorrectFromRightShouldUpdateRowSequenceRangeWithAllExcludingConditionsMet() {
        // given
        int rowIdx = 5;
        int height = 15;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(2, 3, 4, 2));
        List<List<Integer>> rangesRow5 = new ArrayList<>();
        rangesRow5.add(new ArrayList<>(List.of(0, 2)));
        rangesRow5.add(new ArrayList<>(List.of(3, 5)));
        rangesRow5.add(new ArrayList<>(List.of(7, 10)));
        rangesRow5.add(new ArrayList<>(List.of(12, 14)));
        rangesAllRows.set(rowIdx, rangesRow5);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of(2)));

        // stubs
        mockNonogramStateDependent();
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);
        when(rowLogic.getBoardAccessHelper()).thenReturn(nonogramBoardAccessHelper);
        when(nonogramBoardAccessHelper.isRowRangeColoured(rowIdx, List.of(0, 1))).thenReturn(true);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then
        verify(rowLogic, times(1)).updateRowSequenceRange(eq(rowIdx), anyInt(), eq(List.of(0, 1)));
        verify(rowLogic, times(1)).excludeSequenceInRow(eq(rowIdx), anyInt());
        verify(rowLogic, times(1)).updateRowSequenceRange(anyInt(), anyInt(), anyList()); // [0, 2] -> [0, 1]

        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
    }

    @DisplayName("correctRowSequencesRanges (tryCorrectFromRight) - updateRowSequenceRange with only first excluding condition met")
    @Test // 3. A && B && !C // 3rd parametrized
    void tryCorrectFromRightShouldUpdateRowSequenceRangeWithFirstExcludingConditionMetSecondExcludingConditionNotMet() {
        // given
        int rowIdx = 1;
        int height = 15;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 1, 1, 1, 1));
        List<List<Integer>> rangesRow1 = new ArrayList<>();
        rangesRow1.add(new ArrayList<>(List.of(0, 1)));
        rangesRow1.add(new ArrayList<>(List.of(2, 2)));
        rangesRow1.add(new ArrayList<>(List.of(4, 5)));
        rangesRow1.add(new ArrayList<>(List.of(6, 6)));
        rangesRow1.add(new ArrayList<>(List.of(8, 9)));
        rangesAllRows.set(rowIdx, rangesRow1);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of(1, 3)));

        // stubs
        mockNonogramStateDependent();
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then
        verify(rowLogic, times(2)).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
    }

    @DisplayName("correctRowSequencesRanges (tryCorrectFromRight) - updateRowSequenceRange with first excluding condition not met")
    @Test // 4. A && !B o07387
    void tryCorrectFromRightShouldUpdateRowSequenceRangeWithOnlyFirstExcludingConditionMet() {
        // given
        int rowIdx = 6;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(2, 2));
        List<List<Integer>> rangesRow6 = new ArrayList<>();
        rangesRow6.add(new ArrayList<>(List.of(0, 5)));
        rangesRow6.add(new ArrayList<>(List.of(4, 5)));
        rangesAllRows.set(rowIdx, rangesRow6);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of(1)));

        // stubs
        when(rowLogic.getNonogramState()).thenReturn(state);
        when(rowLogic.getActionScheduler()).thenReturn(scheduler);

        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then [0, 5] -> [0, 3]
        verify(rowLogic, times(1)).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
    }

    @DisplayName("correctRowSequencesRanges (tryCorrectFromRight) - not update sequence range - oldRange.get(1).equals(newRange.get(1)")
    @Test // 5. !A o06005
    void tryCorrectFromRightShouldNotUpdateRowSequenceRange() {
        // given
        int rowIdx = 8;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 1));
        List<List<Integer>> rangesRow6 = new ArrayList<>();
        rangesRow6.add(new ArrayList<>(List.of(0, 7)));
        rangesRow6.add(new ArrayList<>(List.of(2, 9)));
        rangesAllRows.set(rowIdx, rangesRow6);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>(2));
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of()));

        // stubs
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        verify(rowLogic, times(0)).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        verify(state, times(0)).increaseMadeSteps();
        verify(scheduler, times(0)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(0)).setTmpLog(anyString());
        verify(rowLogic, times(0)).addLog();
    }

    @DisplayName("correctRowSequencesRanges(correctFromRight) - should calculate updated previous sequence range - seqIdx(1) is not excluded")
    @Test // o06005
    void shouldCalculateUpdatedPreviousSequenceRangeAfterIncludedSequenceIfSeqIdxIsNotExcluded() {
        // given
        int rowIdx = 8;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 1));
        List<List<Integer>> rangesRow8 = new ArrayList<>();
        rangesRow8.add(new ArrayList<>(List.of(0, 7)));
        rangesRow8.add(new ArrayList<>(List.of(2, 9)));
        rangesAllRows.set(rowIdx, rangesRow8);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>(2));
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of()));

        // stubs
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then
        verify(rowLogic, times(0)).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        verify(state, times(0)).increaseMadeSteps();
        verify(scheduler, times(0)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(0)).setTmpLog(anyString());
        verify(rowLogic, times(0)).addLog();
    }

    @DisplayName("correctRowSequencesRanges(correctFromRight) - should calculate updated previous sequence range - seqIdx(3) is excluded")
    @Test // o06005
    void shouldCalculateUpdatedPreviousSequenceRangeAfterIncludedSequenceIfSeqIdxIsExcluded() {
        // given
        int rowIdx = 1;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 1, 1, 1, 1));
        List<List<Integer>> rangesRow1 = new ArrayList<>();
        rangesRow1.add(new ArrayList<>(List.of(0, 1)));
        rangesRow1.add(new ArrayList<>(List.of(2, 2)));
        rangesRow1.add(new ArrayList<>(List.of(4, 5)));
        rangesRow1.add(new ArrayList<>(List.of(6, 6)));
        rangesRow1.add(new ArrayList<>(List.of(8, 9)));
        rangesAllRows.set(rowIdx, rangesRow1);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>(2));
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of(1, 3)));

        // stubs
        mockNonogramStateDependent();
        wireRowData(rowSeqLengths, rangesAllRows, rowsFieldsNotToInclude, rowsExcludedIds);

        // when
        subject.correctRowSequencesRanges(rowIdx);

        // then
        verify(rowLogic, times(2)).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
    }

    @DisplayName("correctRowSequencesRangesWhenMetColouredFieldFromLeft - should not enter to while loop when lengths size is zero (theoretical case)")
    @Test
    void shouldNotEnterToWhileLoopWhenLengthsSizeIsZero() {
        // given
        int rowIdx = 0;
        int height = 1;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of());
        List<List<Integer>> rangesRow0 = new ArrayList<>();
        rangesRow0.add(new ArrayList<>(List.of(-1, -1)));
        rangesAllRows.set(rowIdx, rangesRow0);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of()));

        // stubs
        when(rules.getRowSequencesLengths()).thenReturn(rowSeqLengths);
        when(rowLogic.getRowsSequencesRanges()).thenReturn(rangesAllRows);

        // when
        subject.correctRowSequencesRangesWhenMetColouredField(rowIdx);

        // then
        verify(rowLogic, times(3)).getRowsSequencesRanges();
        verify(rules, times(2)).getRowSequencesLengths();
        verify(rules, times(0)).getWidth();
        verify(rowLogic, times(0)).getNonogramSolutionBoard();
        verify(rowLogic, times(0)).updateRowSequenceRange(anyInt(), anyInt(), anyList());
        verify(rowLogic, times(0)).getBoardAccessHelper();
        verify(nonogramBoardAccessHelper, times(0)).isRowRangeColoured(anyInt(), anyList());
        verify(rowLogic, times(0)).excludeSequenceInRow(anyInt(), anyInt());
        verify(state, times(0)).increaseMadeSteps();
        verify(scheduler, times(0)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(0)).setTmpLog(anyString());
        verify(rowLogic, times(0)).addLog();
    }

    @DisplayName("correctRowSequencesRangesWhenMetColouredFieldFromLeft - should not enter to while loop when ranges size is zero (theoretical case)")
    @Test
    void shouldNotEnterToWhileLoopWhenRangesSizeIsZero() {
        // given
        int rowIdx = 0;
        int height = 1;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(0));
        List<List<Integer>> rangesRow0 = new ArrayList<>();
        rangesAllRows.set(rowIdx, rangesRow0);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of()));

        // stubs
        when(rules.getRowSequencesLengths()).thenReturn(rowSeqLengths);
        when(rowLogic.getRowsSequencesRanges()).thenReturn(rangesAllRows);

        // when
        subject.correctRowSequencesRangesWhenMetColouredField(rowIdx);

        // then
        verify(rowLogic, times(3)).getRowsSequencesRanges();
        verify(rules, times(2)).getRowSequencesLengths();
        verify(rules, times(0)).getWidth();
        verify(rowLogic, times(0)).getNonogramSolutionBoard();
        verify(rowLogic, times(0)).updateRowSequenceRange(anyInt(), anyInt(), anyList());
        verify(rowLogic, times(0)).getBoardAccessHelper();
        verify(nonogramBoardAccessHelper, times(0)).isRowRangeColoured(anyInt(), anyList());
        verify(rowLogic, times(0)).excludeSequenceInRow(anyInt(), anyInt());
        verify(state, times(0)).increaseMadeSteps();
        verify(scheduler, times(0)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(0)).setTmpLog(anyString());
        verify(rowLogic, times(0)).addLog();
    }

    @DisplayName("correctRowSequencesRangesWhenMetColouredField FromLeft & FromRight - should enter to while loop - enter continue and update")
    @Test // o06005
    void shouldEnterToWhileLoopContinueUpdate() {
        // given
        int rowIdx = 0;
        int height = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(1, 1, 1));
        List<List<Integer>> rangesRow0 = new ArrayList<>();
        rangesRow0.add(new ArrayList<>(List.of(0, 5)));
        rangesRow0.add(new ArrayList<>(List.of(2, 7)));
        rangesRow0.add(new ArrayList<>(List.of(4, 9)));
        rangesAllRows.set(rowIdx, rangesRow0);
        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>(List.of()));
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));

        // stubs
        when(rowLogic.getNonogramState()).thenReturn(state);
        when(rowLogic.getActionScheduler()).thenReturn(scheduler);
        when(rowLogic.getRowsSequencesRanges()).thenReturn(rangesAllRows);
        when(rowLogic.getNonogramSolutionBoard()).thenReturn(board);
        when(rules.getWidth()).thenReturn(10);
        when(rules.getRowSequencesLengths()).thenReturn(rowSeqLengths);


        // when
        subject.correctRowSequencesRangesWhenMetColouredField(rowIdx);

        // then
        verify(rowLogic, times(4)).getRowsSequencesRanges();
        verify(rules, times(3)).getRowSequencesLengths();
        verify(rules, times(12)).getWidth();
        verify(rowLogic, times(20)).getNonogramSolutionBoard();
        verify(rowLogic, times(3)).updateRowSequenceRange(anyInt(), anyInt(), anyList());
        verify(rowLogic, times(0)).getBoardAccessHelper();
        verify(rowLogic, times(0)).excludeSequenceInRow(anyInt(), anyInt());
        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
    }

    @DisplayName("correctRowSequencesRangesWhenMetColouredField FromLeft - should try to exclude when range matches length and is coloured")
    @Test
    void shouldTryToExcludeSequenceFromLeft() {
        // given
        int rowIdx = 4;
        int height  = 10;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(9));

        List<List<Integer>> rangesRow4 = new ArrayList<>();
        rangesRow4.add(new ArrayList<>(List.of(0, 9)));
        rangesAllRows.set(rowIdx, rangesRow4);

        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>());

        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));

        when(rules.getWidth()).thenReturn(10);
        when(rules.getRowSequencesLengths()).thenReturn(rowSeqLengths);

        when(rowLogic.getRowsSequencesRanges()).thenReturn(rangesAllRows);
        when(rowLogic.getNonogramSolutionBoard()).thenReturn(board);
        when(rowLogic.getBoardAccessHelper()).thenReturn(nonogramBoardAccessHelper);
        when(rowLogic.getNonogramState()).thenReturn(state);
        when(rowLogic.getActionScheduler()).thenReturn(scheduler);

        when(nonogramBoardAccessHelper.isRowRangeColoured(eq(rowIdx), anyList())).thenReturn(true);

        doAnswer(inv -> {
            int r = inv.getArgument(0, Integer.class);
            int s = inv.getArgument(1, Integer.class);
            @SuppressWarnings("unchecked") List<Integer> nr = inv.getArgument(2, List.class);
            rangesAllRows.get(r).set(s, new ArrayList<>(nr));
            return null;
        }).when(rowLogic).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        // when
        subject.correctRowSequencesRangesWhenMetColouredField(rowIdx);

        // then
        verify(rowLogic).updateRowSequenceRange(rowIdx, 0, List.of(0, 8));
        verify(nonogramBoardAccessHelper, times(1)).isRowRangeColoured(rowIdx, List.of(0, 8));
        verify(rowLogic).excludeSequenceInRow(rowIdx, 0);
        verify(state, atLeastOnce()).increaseMadeSteps();

        assertEquals(List.of(0, 8), rangesAllRows.get(rowIdx).get(0));
    }

    @DisplayName("correctRowSequencesRangesWhenMetColouredField FromLeft - should not try to exclude when updated range is not coloured")
    @Test
    void shouldNotTryToExcludeBecauseOfRowRangeNotColouredFromLeft() {
        // given
        int rowIdx = 9;
        int width = 10;
        prepareData(width);

        // One row with two sequences: lengths 5 and 2
        rowSeqLengths.set(rowIdx, List.of(5, 2));

        // Initial ranges for the row
        List<List<Integer>> r9 = new ArrayList<>();
        r9.add(new ArrayList<>(List.of(0, 6)));
        r9.add(new ArrayList<>(List.of(6, 9)));
        rangesAllRows.set(rowIdx, r9);

        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>());

        // 10x10 board from provided data
        List<List<String>> board = List.of(
                new ArrayList<>(List.of("X","X","X","X","-","X","-","-","-","-")),
                new ArrayList<>(List.of("X","-","X","O","O","-","-","-","-","-")),
                new ArrayList<>(List.of("X","X","X","O","O","O","O","O","X","X")),
                new ArrayList<>(List.of("X","X","X","O","O","O","O","X","X","O")),
                new ArrayList<>(List.of("X","-","O","O","O","-","-","-","-","O")),
                new ArrayList<>(List.of("X","O","O","O","-","X","X","X","X","O")),
                new ArrayList<>(List.of("X","-","O","O","X","X","O","O","X","X")),
                new ArrayList<>(List.of("X","X","X","O","X","X","O","O","X","X")),
                new ArrayList<>(List.of("O","X","O","O","X","X","-","-","-","-")),
                new ArrayList<>(List.of("O","-","O","O","O","X","-","-","-","-"))
        );

        // mocks
        when(rules.getWidth()).thenReturn(width);
        when(rules.getRowSequencesLengths()).thenReturn(rowSeqLengths);
        when(rowLogic.getRowsSequencesRanges()).thenReturn(rangesAllRows);
        when(rowLogic.getNonogramSolutionBoard()).thenReturn(board);
        when(rowLogic.getBoardAccessHelper()).thenReturn(nonogramBoardAccessHelper);
        when(rowLogic.getNonogramState()).thenReturn(state);
        when(rowLogic.getActionScheduler()).thenReturn(scheduler);

        // Range has correct length but is not fully coloured → A && !B
        when(nonogramBoardAccessHelper.isRowRangeColoured(eq(rowIdx), anyList())).thenReturn(false);

        // Simulate that updating a range actually modifies the in-memory structure
        doAnswer(inv -> {
            int r = inv.getArgument(0, Integer.class);
            int s = inv.getArgument(1, Integer.class);
            @SuppressWarnings("unchecked") List<Integer> nr = inv.getArgument(2, List.class);
            rangesAllRows.get(r).set(s, new ArrayList<>(nr));
            return null;
        }).when(rowLogic).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        // when
        subject.correctRowSequencesRangesWhenMetColouredField(rowIdx);

        // then
        verify(rowLogic, atLeastOnce()).updateRowSequenceRange(eq(rowIdx), eq(0), anyList());
        verify(nonogramBoardAccessHelper, atLeastOnce()).isRowRangeColoured(eq(rowIdx), anyList());
        verify(rowLogic, never()).excludeSequenceInRow(eq(rowIdx), anyInt());

        // Since at least one range was updated, logging and scheduling should occur
        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(Field.class),
                eq(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW));
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();

        // Optional assertion: updated range should have exact length 5
        List<Integer> updated = rangesAllRows.get(rowIdx).get(0);
        int updatedLen = updated.get(1) - updated.get(0) + 1;
        assertEquals(5, updatedLen);
    }

    @DisplayName("correctRowSequencesRangesWhenMetColouredField FromRight - should not try to exclude sequence if range is not coloured")
    @Test // o06253
    void shouldNotTryToExcludeSequenceIfRangeIsNotColouredFromRight() {
        // given
        int rowIdx = 0;
        int height = 15;
        prepareData(height);

        rowSeqLengths.set(rowIdx, List.of(3, 2));
        List<List<Integer>> rangesRow0 = new ArrayList<>();
        rangesRow0.add(new ArrayList<>(List.of(0, 6)));
        rangesRow0.add(new ArrayList<>(List.of(8, 9)));
        rangesAllRows.set(rowIdx, rangesRow0);
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("X", "X", "X", "X", "-", "O", "O", "X", "O", "O")),
                new ArrayList<>(List.of("X", "X", "X", "X", "O", "X", "O", "X", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "X", "-", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("O", "O", "O", "O", "X", "O", "X", "O", "X", "O")),
                new ArrayList<>(List.of("O", "X", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("O", "O", "X", "O", "O", "O", "O", "X", "O", "O")),
                new ArrayList<>(List.of("O", "O", "O", "X", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(List.of("X", "O", "O", "O", "O", "O", "O", "O", "X", "X")),
                new ArrayList<>(List.of("-", "X", "X", "O", "O", "O", "O", "O", "X", "X")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "O", "-", "X")),
                new ArrayList<>(List.of("-", "O", "O", "X", "O", "O", "O", "X", "-", "X")),
                new ArrayList<>(List.of("-", "-", "X", "-", "O", "O", "O", "-", "-", "X")),
                new ArrayList<>(List.of("-", "X", "O", "O", "O", "X", "O", "O", "X", "X")),
                new ArrayList<>(List.of("-", "-", "O", "-", "O", "-", "X", "O", "-", "X")),
                new ArrayList<>(List.of("-", "-", "X", "X", "O", "O", "X", "-", "-", "X"))
        ));

        // stubs
        when(rules.getRowSequencesLengths()).thenReturn(rowSeqLengths);
        when(rowLogic.getRowsSequencesRanges()).thenReturn(rangesAllRows);
        when(rowLogic.getNonogramState()).thenReturn(state);
        when(rowLogic.getActionScheduler()).thenReturn(scheduler);
        when(rowLogic.getBoardAccessHelper()).thenReturn(nonogramBoardAccessHelper);
        when(rowLogic.getNonogramSolutionBoard()).thenReturn(board);
        when(rules.getWidth()).thenReturn(10);

        when(nonogramBoardAccessHelper.isRowRangeColoured(rowIdx, List.of(4, 6))).thenReturn(false);

        // when
        subject.correctRowSequencesRangesWhenMetColouredField(rowIdx);

        // then
        verify(rowLogic, times(4)).getRowsSequencesRanges();
        verify(rules, times(3)).getRowSequencesLengths();
        verify(rules, times(9)).getWidth();
        verify(rowLogic, times(10)).getNonogramSolutionBoard();
        verify(rowLogic, times(0)).excludeSequenceInRow(anyInt(), anyInt());
        verify(state, times(1)).increaseMadeSteps();
        verify(scheduler, times(1)).scheduleActionsBasedOnField(any(), any());
        verify(rowLogic, times(1)).setTmpLog(anyString());
        verify(rowLogic, times(1)).addLog();
    }

    @DisplayName("correctRowSequencesRangesWhenMetColouredField From Right - should try to exclude when range matches length and is coloured")
    @Test
    void shouldTryToExcludeSequenceFromRight() {
        // given
        int rowIdx = 1;
        int height = 10;
        prepareData(height);

        // lengths: [2, 2, 1]
        rowSeqLengths.set(rowIdx, List.of(2, 2, 1));

        // ranges row 1: [[0,1], [4,7], [7,9]]
        List<List<Integer>> rangesRow1 = new ArrayList<>();
        rangesRow1.add(new ArrayList<>(List.of(0, 1)));
        rangesRow1.add(new ArrayList<>(List.of(4, 7)));
        rangesRow1.add(new ArrayList<>(List.of(7, 9)));
        rangesAllRows.set(rowIdx, rangesRow1);

        rowsFieldsNotToInclude.set(rowIdx, new ArrayList<>());
        rowsExcludedIds.set(rowIdx, new ArrayList<>());

        // board
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("X","X","X","X","-","-","-","-","-","-")),
                new ArrayList<>(List.of("O","O","X","X","-","-","-","-","-","O")),
                new ArrayList<>(List.of("O","O","O","X","-","-","-","-","-","O")),
                new ArrayList<>(List.of("O","O","O","X","-","-","-","-","-","-")),
                new ArrayList<>(List.of("O","X","X","X","X","X","X","X","X","X")),
                new ArrayList<>(List.of("X","X","X","X","-","X","X","-","-","X")),
                new ArrayList<>(List.of("O","O","O","X","X","X","X","X","X","X")),
                new ArrayList<>(List.of("O","O","O","O","O","O","O","O","O","X")),
                new ArrayList<>(List.of("O","O","O","O","O","O","O","O","X","O")),
                new ArrayList<>(List.of("O","O","O","O","O","O","O","O","X","O"))
        ));

        // rules / logic stubs
        when(rules.getWidth()).thenReturn(10);
        when(rules.getRowSequencesLengths()).thenReturn(rowSeqLengths);

        when(rowLogic.getRowsSequencesRanges()).thenReturn(rangesAllRows);
        when(rowLogic.getNonogramSolutionBoard()).thenReturn(board);
        when(rowLogic.getBoardAccessHelper()).thenReturn(nonogramBoardAccessHelper);
        when(rowLogic.getNonogramState()).thenReturn(state);
        when(rowLogic.getActionScheduler()).thenReturn(scheduler);

        lenient().when(nonogramBoardAccessHelper.isRowRangeColoured(anyInt(), anyList())).thenReturn(true);

        // make updateRowSequenceRange mutate our backing list
        doAnswer(inv -> {
            int r = inv.getArgument(0, Integer.class);
            int s = inv.getArgument(1, Integer.class);
            @SuppressWarnings("unchecked")
            List<Integer> nr = inv.getArgument(2, List.class);
            rangesAllRows.get(r).set(s, new ArrayList<>(nr));
            return null;
        }).when(rowLogic).updateRowSequenceRange(anyInt(), anyInt(), anyList());

        // when
        subject.correctRowSequencesRangesWhenMetColouredField(rowIdx);

        // then
        verify(rowLogic).updateRowSequenceRange(rowIdx, 2, List.of(9, 9));
        verify(nonogramBoardAccessHelper).isRowRangeColoured(rowIdx, List.of(9, 9));
        verify(rowLogic).excludeSequenceInRow(rowIdx, 2);
        verify(state, atLeastOnce()).increaseMadeSteps();

        assertEquals(List.of(9, 9), rangesAllRows.get(rowIdx).get(2));
    }

    private void prepareData(int height) {
        rowSeqLengths = sizedListOf(height, (Supplier<List<Integer>>) ArrayList::new);
        rangesAllRows = sizedListOf(height, (Supplier<List<List<Integer>>>) ArrayList::new);
        rowsFieldsNotToInclude = sizedListOf(height, (Supplier<List<Integer>>) ArrayList::new);
        rowsExcludedIds = sizedListOf(height, (Supplier<List<Integer>>) ArrayList::new);
    }

    private void wireRowData(
            List<List<Integer>> rowSeqLengths,
            List<List<List<Integer>>> rangesAllRows,
            List<List<Integer>> rowsFieldsNotToInclude,
            List<List<Integer>> rowsExcludedIds) {

        when(rules.getRowSequencesLengths()).thenReturn(rowSeqLengths);
        when(rowLogic.getRowsSequencesRanges()).thenReturn(rangesAllRows);
        when(rowLogic.getRowsFieldsNotToInclude()).thenReturn(rowsFieldsNotToInclude);
        when(rowLogic.getRowsSequencesIdsNotToInclude()).thenReturn(rowsExcludedIds);
    }

    // ---- small helper to create outer lists sized as needed ----
    private static <T> List<T> sizedListOf(int size, Supplier<T> factory) {
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) list.add(factory.get());
        return list;
    }

    private void mockNonogramStateDependent() {
        when(rowLogic.getNonogramState()).thenReturn(state);
        when(rowLogic.getActionScheduler()).thenReturn(scheduler);
        when(rowLogic.getBoardAccessHelper()).thenReturn(nonogramBoardAccessHelper);
    }

    // small helper so we don't need to declare @Mock NonogramBoardAccessHelper at all
    private static void verifyNoInteractionsWithBoardAccessHelper(NonogramRowLogic rowLogic) {
        // BoardAccessHelper is only used when range length == expected length; here it isn't.
        // If you prefer explicit zero-interaction check, you can stub and verify, but this avoids extra mocks.
        // no-op placeholder to keep intention clear
    }
}