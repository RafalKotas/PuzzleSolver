package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.colour;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.ColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.TooLongMergeFieldHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.colouring.NonogramFieldColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.RefreshableColumnHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringGenerateLogBaseContext;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ExtendLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.OverlappingLogHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldEmpty;

@Slf4j
@Getter
@Setter
public class ColumnColouringHelperImpl implements ColumnColouringHelper, RefreshableColumnHelper {

    private final NonogramColumnLogic nonogramColumnLogic;

    private final NonogramFieldColouringHelper colouringHelper;

    public ColumnColouringHelperImpl(NonogramColumnLogic nonogramColumnLogic) {
        this.nonogramColumnLogic = nonogramColumnLogic;
        this.colouringHelper = new NonogramFieldColouringHelper(
                this.nonogramColumnLogic.getNonogramSolutionBoard(),
                this.nonogramColumnLogic.getNonogramSolutionBoardWithMarks(),
                this.nonogramColumnLogic.getBoardAccessHelper()
        );
    }

    @Override
    public void colourOverlappingFieldsInColumn(int columnIdx) {
        List<String> initialColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        boolean anyFieldColoured = false;

        List<Integer> sequenceLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> sequenceRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);

        for (int sequenceIdx = 0; sequenceIdx < sequenceLengths.size(); sequenceIdx++) {
            int sequenceLength = sequenceLengths.get(sequenceIdx);
            List<Integer> range = sequenceRanges.get(sequenceIdx);

            List<Integer> overlapRange = ColouringHelper.calculateOverlappingRange(range, sequenceLength);
            boolean coloured = colourAllEmptyFieldsInRangeForColumn(columnIdx, overlapRange, sequenceIdx);
            anyFieldColoured |= coloured;
        }

        if (anyFieldColoured) {
            List<String> updatedColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
            ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                    false,
                    columnIdx,
                    initialColumn,
                    updatedColumn,
                    sequenceRanges,
                    sequenceLengths
            );
            String tmpLog = OverlappingLogHelper.generateLog(
                    colouringGenerateLogBaseContext
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();
        }
    }

    private boolean colourAllEmptyFieldsInRangeForColumn(int columnIdx, List<Integer> rows, int sequenceIdx) {
        if (rows.isEmpty()) return false;

        int sequenceLength = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx).get(sequenceIdx);
        boolean anyFieldColoured = false;

        for (int rowIdx : rows) {
            Field field = new Field(rowIdx, columnIdx);

            if (isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), field)) {
                colouringHelper.colourFieldAtGivenPosition(field, "--C-");
                anyFieldColoured = true;
                nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN);
                nonogramColumnLogic.getNonogramState().increaseMadeSteps();
            } else if (NonogramLogicParams.SHOW_REPETITIONS) {
                log.warn("Column field was coloured earlier (overlap).");
            }
        }

        if (rows.size() == sequenceLength) {
            nonogramColumnLogic.excludeSequenceInColumn(columnIdx, sequenceIdx);
        }

        return anyFieldColoured;
    }

    /**
     * Attempts to color fields in a given column if placing an X before or after
     * a colored sequence would result in a merged sequence that exceeds the allowed sequence lengths.
     * <p>
     * This method simulates placing Xs temporarily at merge points between colored blocks and checks
     * whether such merges would violate Nonogram constraints. If placing X would make the merged
     * sequence invalid, a coloring action is triggered at that field instead.
     *
     * @param columnIdx the index of the column to be evaluated and potentially modified
     */
    @Override
    public void colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(int columnIdx) {
        List<String> initialColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        boolean anyFieldColoured = false;

        List<Integer> sequenceLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> sequencesRanges = cloneAndMakeImmutable2DList(nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx));
        List<List<Integer>> colouredSequences = TooLongMergeFieldHelper.collectColouredSequencesRanges(nonogramColumnLogic.getNonogramSolutionBoard(), columnIdx, false);

        anyFieldColoured |= handleTopMergeScenarios(columnIdx, sequenceLengths, sequencesRanges, colouredSequences);
        anyFieldColoured |= handleBottomMergeScenarios(columnIdx, sequenceLengths, sequencesRanges, colouredSequences);

        if (anyFieldColoured) {
            List<String> updatedColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
            ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                    false,
                    columnIdx,
                    initialColumn,
                    updatedColumn,
                    sequencesRanges,
                    sequenceLengths
            );
            String tmpLog = ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.generateLog(
                    colouringGenerateLogBaseContext
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();
        }
    }

    /**
     * Handles coloring logic at the top (above) merge points between consecutive colored sequences in a column.
     * <p>
     * For each adjacent pair of colored sequences, it checks if merging them would create a block
     * that violates sequence constraints. If so, a field directly before the first sequence is colored
     * to prevent invalid merging.
     * <p>
     * The method also temporarily places and clears an X to simulate its effect on possible ranges.
     *
     * @param columnIdx      the index of the column being evaluated
     * @param seqLens        list of required sequence lengths for the column
     * @param sequencesRanges original column sequence ranges before mutation
     * @param colouredSeqs   list of currently identified colored sequences in the column
     * @return true if any field was colored as a result of this analysis; false otherwise
     */
    private boolean handleTopMergeScenarios(int columnIdx, List<Integer> seqLens,
                                            List<List<Integer>> sequencesRanges, List<List<Integer>> colouredSeqs) {
        boolean anyFieldColoured = false;

        for (int i = 0; i < colouredSeqs.size() - 1; i++) {
            List<Integer> first = colouredSeqs.get(i);
            List<Integer> second = colouredSeqs.get(i + 1);

            int mergeStart = first.get(0);
            int mergePoint = second.get(0) - 1;
            int mergeEnd = second.get(1);

            Field tempX = new Field(mergeStart - 1, columnIdx);
            if (nonogramColumnLogic.getBoardAccessHelper().isRowIndexValid(mergeStart - 1) &&
                    isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), tempX)) {
                nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(tempX);
                nonogramColumnLogic.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(tempX);
                nonogramColumnLogic.correctColumnSequencesRangesIfXOnWay(columnIdx);
                nonogramColumnLogic.getNonogramFieldClearingHelper().clearField(tempX);
            }

            boolean shouldSkip = mergedSequenceViolatesConstraints(columnIdx, first, List.of(mergeStart, mergeEnd), mergePoint, seqLens);
            if (!shouldSkip) {
                Field toColour = new Field(mergeStart - 1, columnIdx);
                if (isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), toColour)) {
                    colouringHelper.colourFieldAtGivenPosition(toColour, "--C-");
                    nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(toColour, NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_COLUMN);
                    nonogramColumnLogic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            }

            nonogramColumnLogic.setColumnSequencesRanges(columnIdx, mutableClone2DList(sequencesRanges));
            nonogramColumnLogic.getNonogramFieldExclusionHelper().removeFieldFromExcludedInColumn(tempX);
        }

        return anyFieldColoured;
    }

    /**
     * Handles coloring logic at the bottom (below) merge points between consecutive colored sequences in a column.
     * <p>
     * This method mirrors {@code handleTopMergeScenarios}, but focuses on the field after the second sequence.
     * It places an X, evaluates whether a merge would be invalid, and, if necessary, colors the field to prevent merging.
     *
     * @param columnIdx      the index of the column being evaluated
     * @param seqLens        list of required sequence lengths for the column
     * @param sequencesRanges original column sequence ranges before mutation
     * @param colouredSeqs   list of currently identified colored sequences in the column
     * @return true if any field was colored as a result of this analysis; false otherwise
     */
    private boolean handleBottomMergeScenarios(int columnIdx, List<Integer> seqLens,
                                               List<List<Integer>> sequencesRanges, List<List<Integer>> colouredSeqs) {
        boolean anyFieldColoured = false;

        for (int i = colouredSeqs.size() - 1; i > 0; i--) {
            List<Integer> second = colouredSeqs.get(i);
            List<Integer> first = colouredSeqs.get(i - 1);

            int mergeStart = first.get(0);
            int mergePoint = first.get(1) + 1;
            int mergeEnd = second.get(1);

            Field tempX = new Field(mergeEnd + 1, columnIdx);
            if (nonogramColumnLogic.getBoardAccessHelper().isRowIndexValid(mergeEnd + 1) &&
                    isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), tempX)) {
                nonogramColumnLogic.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(tempX);
                nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(tempX);
                nonogramColumnLogic.correctColumnSequencesRangesIfXOnWay(columnIdx);
                nonogramColumnLogic.getNonogramFieldClearingHelper().clearField(tempX);
            }

            boolean shouldSkip = mergedSequenceViolatesConstraints(columnIdx, second, List.of(mergeStart, mergeEnd), mergePoint, seqLens);
            if (!shouldSkip) {
                Field toColour = new Field(mergeEnd + 1, columnIdx);
                if (isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), toColour)) {
                    colouringHelper.colourFieldAtGivenPosition(toColour, "--C-");
                    nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(toColour, NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_COLUMN);
                    nonogramColumnLogic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            }

            nonogramColumnLogic.setColumnSequencesRanges(columnIdx, mutableClone2DList(sequencesRanges));
            nonogramColumnLogic.getNonogramFieldExclusionHelper().removeFieldFromExcludedInColumn(tempX);
        }

        return anyFieldColoured;
    }

    /**
     * Determines whether a hypothetically merged sequence would violate Nonogram constraints
     * for the given column.
     * <p>
     * For each possible matching sequence index (ID), the method checks whether a sequence of the
     * required length could physically overlap the merge point. If no sequence can do so,
     * the merge is deemed invalid.
     * <p>
     * Additionally, it verifies if the merged sequence is fully contained within a declared range
     * but shorter than expected, which would also be invalid.
     *
     * @param columnIdx    index of the column being analyzed
     * @param colouredPart the original colored segment that initiated the merge
     * @param mergedRange  range representing the full extent of the merged sequence
     * @param mergePoint   the point where the two sequences meet
     * @param seqLens      list of target sequence lengths for the column
     * @return true if the merged sequence would break any constraints; false otherwise
     */
    private boolean mergedSequenceViolatesConstraints(int columnIdx, List<Integer> colouredPart,
                                                      List<Integer> mergedRange, int mergePoint,
                                                      List<Integer> seqLens) {
        Map<List<Integer>, List<Integer>> mapping = TooLongMergeFieldHelper.matchColouredSequencesToPossibleSeqIDs(
                TooLongMergeFieldHelper.collectColouredSequencesRanges(nonogramColumnLogic.getNonogramSolutionBoard(), columnIdx, false),
                nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx)
        );

        List<Integer> possibleSeqIds = mapping.get(colouredPart);
        if (possibleSeqIds == null) return true;

        for (int seqId : possibleSeqIds) {
            int length = seqLens.get(seqId);
            if ((mergedRange.get(0) + length - 1 < mergePoint) || (mergedRange.get(1) - length + 1 > mergePoint)) {
                return true;
            }

            List<Integer> range = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx).get(seqId);
            if (rangeInsideAnotherRange(mergedRange, range) && rangeLength(mergedRange) <= length) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void extendColouredFieldsNearXToMaximumPossibleLengthInColumn(int columnIdx) {
        extendColouredFieldsToTopNearX(columnIdx);
        extendColouredFieldsToBottomNearX(columnIdx);
    }

    private void extendColouredFieldsToTopNearX(int columnIdx) {
        List<String> initialColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        boolean anyGlobalFieldColoured = false;

        int rowIdx = nonogramColumnLogic.getNonogramRules().getHeight() - 1;
        List<Integer> sequenceLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> sequencesRanges = cloneAndMakeImmutable2DList(nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx));

        while (rowIdx >= 0) {
            Field currentField = new Field(rowIdx, columnIdx);
            boolean shouldDecrement = true;

            if (isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), currentField)) {
                List<Integer> colouredRange = ColouringHelper.findColouredSequenceRangeTop(
                        nonogramColumnLogic.getNonogramSolutionBoard(), columnIdx, rowIdx
                );

                List<Integer> possibleSequenceLengths = ColouringHelper.findPossibleSequenceLengths(
                        sequencesRanges,
                        colouredRange,
                        sequenceLengths
                );

                if (possibleSequenceLengths.isEmpty()) {
                    nonogramColumnLogic.getNonogramState().invalidateSolution();
                    break;
                }

                int minSequenceLength = Collections.min(possibleSequenceLengths);
                int distanceFromX = ColouringHelper.findDistanceFromBottomX(
                        nonogramColumnLogic.getNonogramSolutionBoard(), columnIdx, colouredRange, minSequenceLength
                );

                if (distanceFromX > 0) {
                    int minExtensionIdx = colouredRange.get(0) + distanceFromX - minSequenceLength;
                    boolean extended = ColouringHelper.extendToTop(
                            nonogramColumnLogic,
                            colouringHelper,
                            nonogramColumnLogic.getActionScheduler(),
                            columnIdx,
                            colouredRange.get(0) - 1,
                            minExtensionIdx
                    );
                    anyGlobalFieldColoured |= extended;
                }

                rowIdx = colouredRange.get(0) - 1;
                shouldDecrement = false;
            }

            if (shouldDecrement) {
                rowIdx--;
            }
        }

        if (anyGlobalFieldColoured) {
            List<String> updatedColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
            ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                    false,
                    columnIdx,
                    initialColumn,
                    updatedColumn,
                    sequencesRanges,
                    sequenceLengths
            );
            String tmpLog = ExtendLogHelper.generateLog(
                    colouringGenerateLogBaseContext,
                    "toTop"
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();
        }
    }

    private void extendColouredFieldsToBottomNearX(int columnIdx) {
        List<String> initialColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        boolean anyGlobalFieldColoured = false;

        int rowIdx = 0;
        int height = nonogramColumnLogic.getNonogramRules().getHeight();
        List<Integer> sequenceLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> sequencesRanges = cloneAndMakeImmutable2DList(nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx));

        while (rowIdx < height) {
            Field currentField = new Field(rowIdx, columnIdx);
            boolean shouldAdvance = true;

            if (isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), currentField)) {
                List<Integer> colouredRange = ColouringHelper.findColouredSequenceRangeBottom(
                        nonogramColumnLogic.getNonogramSolutionBoard(), columnIdx, rowIdx
                );

                List<Integer> possibleSequenceLengths = ColouringHelper.findPossibleSequenceLengths(
                        sequencesRanges,
                        colouredRange,
                        sequenceLengths
                );

                if (possibleSequenceLengths.isEmpty()) {
                    nonogramColumnLogic.getNonogramState().invalidateSolution();
                    break;
                }

                int minSequenceLength = Collections.min(possibleSequenceLengths);
                int distanceFromX = ColouringHelper.findDistanceFromTopX(
                        nonogramColumnLogic.getNonogramSolutionBoard(), columnIdx, colouredRange, minSequenceLength
                );

                if (distanceFromX > 0) {
                    int maxExtensionIdx = colouredRange.get(1) - distanceFromX + minSequenceLength;
                    boolean extended = ColouringHelper.extendToBottom(
                            nonogramColumnLogic,
                            colouringHelper,
                            nonogramColumnLogic.getActionScheduler(),
                            columnIdx,
                            colouredRange.get(1) + 1,
                            maxExtensionIdx
                    );

                    anyGlobalFieldColoured |= extended;
                }

                rowIdx = colouredRange.get(1) + 1;
                shouldAdvance = false;
            }

            if (shouldAdvance) {
                rowIdx++;
            }
        }

        if (anyGlobalFieldColoured) {
            List<String> updatedColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
            ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                    false,
                    columnIdx,
                    initialColumn,
                    updatedColumn,
                    sequencesRanges,
                    sequenceLengths
            );
            String tmpLog = ExtendLogHelper.generateLog(
                    colouringGenerateLogBaseContext,
                    "toBottom"
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();
        }
    }

    @Override
    public void refreshFrom(NonogramColumnLogic logicToCopy) {
        nonogramColumnLogic.setLogs(logicToCopy.getLogs());
        nonogramColumnLogic.setNonogramSolutionBoard(logicToCopy.getNonogramSolutionBoard());

        nonogramColumnLogic.setColumnsSequencesRanges(logicToCopy.getColumnsSequencesRanges());
        nonogramColumnLogic.setColumnsFieldsNotToInclude(logicToCopy.getColumnsFieldsNotToInclude());
    }
}
