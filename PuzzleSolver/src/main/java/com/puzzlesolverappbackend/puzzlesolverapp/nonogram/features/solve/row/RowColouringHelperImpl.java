package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.ColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.TooLongMergeFieldHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.colouring.NonogramFieldColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ExtendLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.OverlappingLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.TooLongMergeLogHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldEmpty;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.TooLongMergeFieldHelper.collectColouredSequencesRanges;

@Slf4j
@Getter
@Setter
public class RowColouringHelperImpl implements RowColouringHelper {

    private final NonogramRowLogic logic;

    private final NonogramFieldColouringHelper colouringHelper;

    public RowColouringHelperImpl(NonogramRowLogic nonogramRowLogic) {
        logic = nonogramRowLogic;
        this.colouringHelper = new NonogramFieldColouringHelper(
                logic.getNonogramSolutionBoard(),
                logic.getNonogramSolutionBoardWithMarks(),
                logic.getBoardAccessHelper()
        );
    }

    @Override
    public void colourOverlappingFieldsInRow(int rowIdx) {
        List<String> rowBefore = logic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyFieldColoured = false;

        List<Integer> sequenceLengths = logic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> sequenceRanges = logic.getRowsSequencesRanges().get(rowIdx);

        for (int sequenceIdx = 0; sequenceIdx < sequenceLengths.size(); sequenceIdx++) {
            int sequenceLength = sequenceLengths.get(sequenceIdx);
            List<Integer> range = sequenceRanges.get(sequenceIdx);

            List<Integer> overlapRange = ColouringHelper.calculateOverlappingRange(range, sequenceLength);
            boolean coloured = colourAllEmptyFieldsInRangeForRow(rowIdx, overlapRange, sequenceIdx);
            anyFieldColoured |= coloured;
        }

        if (anyFieldColoured) {
            List<String> rowAfter = logic.getBoardAccessHelper().getRowCopy(rowIdx);
            String tmpLog = OverlappingLogHelper.generateOverlappingSequenceLog(
                    rowIdx,
                    true,
                    rowBefore,
                    sequenceRanges,
                    sequenceLengths,
                    rowAfter
            );
            logic.getLogService().setTmpLog(tmpLog);
            logic.getLogService().addLog();
        }
    }

    private boolean colourAllEmptyFieldsInRangeForRow(int rowIdx, List<Integer> columns, int sequenceIdx) {
        if (columns.isEmpty()) return false;

        int sequenceLength = logic.getNonogramRules().getRowSequencesLengths().get(rowIdx).get(sequenceIdx);
        boolean anyFieldColoured = false;

        for (int columnIdx : columns) {
            Field field = new Field(rowIdx, columnIdx);

            if (isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
                colouringHelper.colourFieldAtGivenPosition(field, "R---");
                anyFieldColoured = true;
                logic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW);
                logic.getNonogramState().increaseMadeSteps();
            } else if (NonogramRowLogic.SHOW_REPETITIONS) {
                log.warn("Row field was coloured earlier (overlap).");
            }
        }

        if (columns.size() == sequenceLength) {
            logic.excludeSequenceInRow(rowIdx, sequenceIdx);
        }

        return anyFieldColoured;
    }


    @Override
    public void colourFieldsIfXWouldForceTooLongColouredFieldsSequence(int rowIdx) {
        List<String> rowBefore = logic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyFieldColoured = false;

        List<Integer> sequenceLengths = logic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> originalRanges = cloneAndMakeImmutable2DList(logic.getRowsSequencesRanges().get(rowIdx));
        List<List<Integer>> colouredSequences = collectColouredSequencesRanges(logic.getNonogramSolutionBoard(), rowIdx, true);

        anyFieldColoured |= handleLeftMergeScenarios(rowIdx, sequenceLengths, originalRanges, colouredSequences);
        anyFieldColoured |= handleRightMergeScenarios(rowIdx, sequenceLengths, originalRanges, colouredSequences);

        if (anyFieldColoured) {
            List<String> rowAfter = logic.getBoardAccessHelper().getRowCopy(rowIdx);
            String tmpLog = TooLongMergeLogHelper.generateTooLongMergeSequenceLog(
                    rowIdx,
                    true,
                    rowBefore,
                    logic.getRowsSequencesRanges().get(rowIdx),
                    sequenceLengths,
                    rowAfter
            );
            logic.getLogService().setTmpLog(tmpLog);
            logic.getLogService().addLog();
        }
    }

    private boolean handleLeftMergeScenarios(int rowIdx, List<Integer> seqLens, List<List<Integer>> originalRanges, List<List<Integer>> colouredSequences) {
        boolean anyFieldColoured = false;

        for (int i = 0; i < colouredSequences.size() - 1; i++) {
            List<Integer> first = colouredSequences.get(i);
            List<Integer> second = colouredSequences.get(i + 1);

            int mergeStart = first.get(0);
            int mergePoint = second.get(0) - 1;
            int mergeEnd = second.get(1);

            Field tempX = new Field(rowIdx, mergeStart - 1);
            if (logic.getBoardAccessHelper().isColumnIndexValid(mergeStart - 1) &&
                    isFieldEmpty(logic.getNonogramSolutionBoard(), tempX)) {
                logic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(tempX);
                logic.getNonogramFieldExclusionHelper().excludeFieldInRow(tempX);
                logic.correctRowSequencesRangesIfXOnWay(rowIdx, false);
                logic.getNonogramFieldClearingHelper().clearField(tempX);
            }

            boolean shouldSkip = mergedSequenceViolatesConstraints(rowIdx, first, List.of(mergeStart, mergeEnd), mergePoint, seqLens);
            if (!shouldSkip) {
                Field toColour = new Field(rowIdx, mergeStart - 1);
                if (isFieldEmpty(logic.getNonogramSolutionBoard(), toColour)) {
                    colouringHelper.colourFieldAtGivenPosition(toColour, "R---");
                    logic.getActionScheduler().scheduleActionsBasedOnField(toColour, NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE);
                    logic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            }

            logic.getNonogramFieldExclusionHelper().removeFieldFromExcludedInRow(tempX);
            logic.setRowSequencesRanges(rowIdx, mutableClone2DList(originalRanges));
        }

        return anyFieldColoured;
    }

    private boolean handleRightMergeScenarios(int rowIdx, List<Integer> seqLens, List<List<Integer>> originalRanges, List<List<Integer>> colouredSequences) {
        boolean anyFieldColoured = false;

        for (int i = colouredSequences.size() - 1; i > 0; i--) {
            List<Integer> second = colouredSequences.get(i);
            List<Integer> first = colouredSequences.get(i - 1);

            int mergeStart = first.get(0);
            int mergePoint = first.get(1) + 1;
            int mergeEnd = second.get(1);

            Field tempX = new Field(rowIdx, mergeEnd + 1);
            if (logic.getBoardAccessHelper().isColumnIndexValid(mergeEnd + 1) &&
                    isFieldEmpty(logic.getNonogramSolutionBoard(), tempX)) {
                logic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(tempX);
                logic.getNonogramFieldExclusionHelper().excludeFieldInRow(tempX);
                logic.correctRowSequencesRangesIfXOnWay(rowIdx, false);
                logic.getNonogramFieldClearingHelper().clearField(tempX);
            }

            boolean shouldSkip = mergedSequenceViolatesConstraints(rowIdx, second, List.of(mergeStart, mergeEnd), mergePoint, seqLens);
            if (!shouldSkip) {
                Field toColour = new Field(rowIdx, mergeEnd + 1);
                if (isFieldEmpty(logic.getNonogramSolutionBoard(), toColour)) {
                    colouringHelper.colourFieldAtGivenPosition(toColour, "R---");
                    logic.getActionScheduler().scheduleActionsBasedOnField(toColour, NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE);
                    logic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            }

            logic.setRowSequencesRanges(rowIdx, mutableClone2DList(originalRanges));
            logic.getNonogramFieldExclusionHelper().removeFieldFromExcludedInRow(tempX);
        }

        return anyFieldColoured;
    }

    private boolean mergedSequenceViolatesConstraints(int rowIdx, List<Integer> colouredPart,
                                                      List<Integer> mergedRange, int mergePoint,
                                                      List<Integer> seqLens) {
        Map<List<Integer>, List<Integer>> mapping = TooLongMergeFieldHelper.matchColouredSequencesToPossibleSeqIDs(
                collectColouredSequencesRanges(logic.getNonogramSolutionBoard(), rowIdx, true),
                logic.getRowsSequencesRanges().get(rowIdx)
        );

        List<Integer> possibleSeqIds = mapping.get(colouredPart);
        if (possibleSeqIds == null) return true;

        for (int seqId : possibleSeqIds) {
            int length = seqLens.get(seqId);

            if ((mergedRange.get(0) + length - 1 < mergePoint) ||
                    (mergedRange.get(1) - length + 1 > mergePoint)) {
                return true;
            }

            List<Integer> range = logic.getRowsSequencesRanges().get(rowIdx).get(seqId);
            if (rangeInsideAnotherRange(mergedRange, range) && rangeLength(mergedRange) <= length) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx) {
        extendColouredFieldsToLeftNearX(rowIdx);
        extendColouredFieldsToRightNearX(rowIdx);
    }

    private void extendColouredFieldsToLeftNearX(int rowIdx) {
        List<String> rowBefore = logic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyGlobalFieldColoured = false;

        for (int colIdx = logic.getNonogramRules().getWidth() - 1; colIdx >= 0; colIdx--) {
            Field currentField = new Field(rowIdx, colIdx);

            if (isFieldColoured(logic.getNonogramSolutionBoard(), currentField)) {
                List<Integer> colouredRange =  ColouringHelper.findColouredSequenceRangeLeft(logic.getNonogramSolutionBoard(), rowIdx, colIdx);
                List<Integer> possibleLengths = ColouringHelper.findPossibleSequenceLengths(
                        logic.getRowsSequencesRanges().get(rowIdx),
                        colouredRange,
                        logic.getNonogramRules().getRowSequencesLengths().get(rowIdx)
                );

                if (possibleLengths.isEmpty()) {
                    logic.getNonogramState().invalidateSolution();
                    break;
                }

                int minSequenceLength = Collections.min(possibleLengths);
                int distFromX = ColouringHelper.findDistanceFromRightX(logic.getNonogramSolutionBoard(), rowIdx, colouredRange, minSequenceLength);

                if (distFromX > 0) {
                    int minExtensionIdx = colouredRange.get(0) + distFromX - minSequenceLength;
                    boolean extended = ColouringHelper.extendToLeft(
                            logic,
                            colouringHelper,
                            logic.getActionScheduler(),
                            rowIdx,
                            colouredRange.get(0) - 1,
                            minExtensionIdx
                    );

                    anyGlobalFieldColoured |= extended;
                }

                colIdx = colouredRange.get(0) - 1;
            }
        }

        if (anyGlobalFieldColoured) {
            List<String> rowAfter = logic.getBoardAccessHelper().getRowCopy(rowIdx);
            logic.getLogService().setTmpLog(ExtendLogHelper.generateExtendSequenceLog(
                    rowIdx,
                    "toLeft",
                    rowBefore,
                    logic.getRowsSequencesRanges().get(rowIdx),
                    logic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    rowAfter,
                    true
            ));
            logic.getLogService().addLog();
        }
    }

    private void extendColouredFieldsToRightNearX(int rowIdx) {
        List<String> rowBefore = logic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyGlobalFieldColoured = false;

        for (int colIdx = 0; colIdx < logic.getNonogramRules().getWidth(); colIdx++) {
            Field currentField = new Field(rowIdx, colIdx);

            if (isFieldColoured(logic.getNonogramSolutionBoard(), currentField)) {
                List<Integer> colouredRange = ColouringHelper.findColouredSequenceRangeRight(logic.getNonogramSolutionBoard(), rowIdx, colIdx);
                List<Integer> possibleLengths = ColouringHelper.findPossibleSequenceLengths(
                        logic.getRowsSequencesRanges().get(rowIdx),
                        colouredRange,
                        logic.getNonogramRules().getRowSequencesLengths().get(rowIdx)
                );

                if (possibleLengths.isEmpty()) {
                    logic.getNonogramState().invalidateSolution();
                    break;
                }

                int minSequenceLength = Collections.min(possibleLengths);
                int distanceFromX = ColouringHelper.findDistanceFromLeftX(
                        logic.getNonogramSolutionBoard(),
                        rowIdx,
                        colouredRange,
                        minSequenceLength
                );

                if (distanceFromX > 0) {
                    int maxExtensionIdx = colouredRange.get(1) - distanceFromX + minSequenceLength;
                    boolean extended = ColouringHelper.extendToRight(
                            logic,
                            colouringHelper,
                            logic.getActionScheduler(),
                            rowIdx,
                            colouredRange.get(1) + 1,
                            maxExtensionIdx);

                    anyGlobalFieldColoured |= extended;
                }

                colIdx = colouredRange.get(1) + 1;
            }
        }

        if (anyGlobalFieldColoured) {
            List<String> rowAfter = logic.getBoardAccessHelper().getRowCopy(rowIdx);
            logic.getLogService().setTmpLog(ExtendLogHelper.generateExtendSequenceLog(
                    rowIdx,
                    "toRight",
                    rowBefore,
                    logic.getRowsSequencesRanges().get(rowIdx),
                    logic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    rowAfter,
                    true
            ));
            logic.getLogService().addLog();
        }
    }
}
