package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.ColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.TooLongMergeFieldHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.colouring.NonogramFieldColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ExtendLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.OverlappingLogHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicParams.SHOW_REPETITIONS;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldEmpty;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.TooLongMergeFieldHelper.collectColouredSequencesRanges;

@Slf4j
@Getter
@Setter
public class RowColouringHelperImpl implements RowColouringHelper, RefreshableRowHelper {

    private final NonogramRowLogic nonogramRowLogic;

    private final NonogramFieldColouringHelper colouringHelper;

    public RowColouringHelperImpl(NonogramRowLogic nonogramRowLogic) {
        this.nonogramRowLogic = nonogramRowLogic;
        this.colouringHelper = new NonogramFieldColouringHelper(
                this.nonogramRowLogic.getNonogramSolutionBoard(),
                this.nonogramRowLogic.getNonogramSolutionBoardWithMarks(),
                this.nonogramRowLogic.getBoardAccessHelper()
        );
    }

    @Override
    public void colourOverlappingFieldsInRow(int rowIdx) {
        List<String> rowBefore = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyFieldColoured = false;

        List<Integer> sequenceLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> sequenceRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

        for (int sequenceIdx = 0; sequenceIdx < sequenceLengths.size(); sequenceIdx++) {
            int sequenceLength = sequenceLengths.get(sequenceIdx);
            List<Integer> range = sequenceRanges.get(sequenceIdx);

            List<Integer> overlapRange = ColouringHelper.calculateOverlappingRange(range, sequenceLength);
            boolean coloured = colourAllEmptyFieldsInRangeForRow(rowIdx, overlapRange, sequenceIdx);
            anyFieldColoured |= coloured;
        }

        if (anyFieldColoured) {
            List<String> rowAfter = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
            String tmpLog = OverlappingLogHelper.generateLog(
                    true,
                    rowIdx,
                    rowBefore,
                    sequenceRanges,
                    sequenceLengths,
                    rowAfter
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
        }
    }

    private boolean colourAllEmptyFieldsInRangeForRow(int rowIdx, List<Integer> columns, int sequenceIdx) {
        if (columns.isEmpty()) return false;

        int sequenceLength = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx).get(sequenceIdx);
        boolean anyFieldColoured = false;

        for (int columnIdx : columns) {
            Field field = new Field(rowIdx, columnIdx);

            if (isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), field)) {
                colouringHelper.colourFieldAtGivenPosition(field, "R---");
                anyFieldColoured = true;
                nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW);
                nonogramRowLogic.getNonogramState().increaseMadeSteps();
            } else if (SHOW_REPETITIONS) {
                log.warn("Row field was coloured earlier (overlap).");
            }
        }

        if (columns.size() == sequenceLength) {
            nonogramRowLogic.excludeSequenceInRow(rowIdx, sequenceIdx);
        }

        return anyFieldColoured;
    }


    /* TODO - think about second coloring action:
        first about colour field near another if X will force to too long coloured fields sequence (this method)
        second about merging two coloured sequences with empty Field break (?)
     */
    @Override
    public void colourFieldsInRowIfXWouldForceTooLongColouredFieldsSequence(int rowIdx) {
        List<String> rowBefore = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyFieldColoured = false;

        List<Integer> sequenceLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> originalRanges = cloneAndMakeImmutable2DList(nonogramRowLogic.getRowsSequencesRanges().get(rowIdx));
        List<List<Integer>> colouredSequences = collectColouredSequencesRanges(nonogramRowLogic.getNonogramSolutionBoard(), rowIdx, true);

        anyFieldColoured |= handleLeftMergeScenarios(rowIdx, sequenceLengths, originalRanges, colouredSequences);
        anyFieldColoured |= handleRightMergeScenarios(rowIdx, sequenceLengths, originalRanges, colouredSequences);

        if (anyFieldColoured) {
            List<String> rowAfter = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
            String tmpLog = ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.generateLog(
                    true,
                    rowIdx,
                    rowBefore,
                    nonogramRowLogic.getRowsSequencesRanges().get(rowIdx),
                    sequenceLengths,
                    rowAfter
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
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
            if (nonogramRowLogic.getBoardAccessHelper().isColumnIndexValid(mergeStart - 1) &&
                    isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), tempX)) {
                nonogramRowLogic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(tempX);
                nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(tempX);
                nonogramRowLogic.correctRowSequencesRangesIfXOnWay(rowIdx, false);
                nonogramRowLogic.getNonogramFieldClearingHelper().clearField(tempX);
            }

            boolean shouldSkip = mergedSequenceViolatesConstraints(rowIdx, first, List.of(mergeStart, mergeEnd), mergePoint, seqLens);
            if (!shouldSkip) {
                Field toColour = new Field(rowIdx, mergeStart - 1);
                if (isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), toColour)) {
                    colouringHelper.colourFieldAtGivenPosition(toColour, "R---");
                    nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(toColour, NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE);
                    nonogramRowLogic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            }

            nonogramRowLogic.getNonogramFieldExclusionHelper().removeFieldFromExcludedInRow(tempX);
            nonogramRowLogic.setRowSequencesRanges(rowIdx, mutableClone2DList(originalRanges));
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
            if (nonogramRowLogic.getBoardAccessHelper().isColumnIndexValid(mergeEnd + 1) &&
                    isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), tempX)) {
                nonogramRowLogic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(tempX);
                nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(tempX);
                nonogramRowLogic.correctRowSequencesRangesIfXOnWay(rowIdx, false);
                nonogramRowLogic.getNonogramFieldClearingHelper().clearField(tempX);
            }

            boolean shouldSkip = mergedSequenceViolatesConstraints(rowIdx, second, List.of(mergeStart, mergeEnd), mergePoint, seqLens);
            if (!shouldSkip) {
                Field toColour = new Field(rowIdx, mergeEnd + 1);
                if (isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), toColour)) {
                    colouringHelper.colourFieldAtGivenPosition(toColour, "R---");
                    nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(toColour, NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE);
                    nonogramRowLogic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            }

            nonogramRowLogic.setRowSequencesRanges(rowIdx, mutableClone2DList(originalRanges));
            nonogramRowLogic.getNonogramFieldExclusionHelper().removeFieldFromExcludedInRow(tempX);
        }

        return anyFieldColoured;
    }

    private boolean mergedSequenceViolatesConstraints(int rowIdx, List<Integer> colouredPart,
                                                      List<Integer> mergedRange, int mergePoint,
                                                      List<Integer> seqLens) {
        Map<List<Integer>, List<Integer>> mapping = TooLongMergeFieldHelper.matchColouredSequencesToPossibleSeqIDs(
                collectColouredSequencesRanges(nonogramRowLogic.getNonogramSolutionBoard(), rowIdx, true),
                nonogramRowLogic.getRowsSequencesRanges().get(rowIdx)
        );

        List<Integer> possibleSeqIds = mapping.get(colouredPart);
        if (possibleSeqIds == null) return true;

        for (int seqId : possibleSeqIds) {
            int length = seqLens.get(seqId);

            if ((mergedRange.get(0) + length - 1 < mergePoint) ||
                    (mergedRange.get(1) - length + 1 > mergePoint)) {
                return true;
            }

            List<Integer> range = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx).get(seqId);
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
        List<String> rowBefore = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyGlobalFieldColoured = false;

        int colIdx = nonogramRowLogic.getNonogramRules().getWidth() - 1;
        while (colIdx >= 0) {
            Field currentField = new Field(rowIdx, colIdx);

            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), currentField)) {
                colIdx--;
                continue;
            }

            List<Integer> colouredRange = ColouringHelper.findColouredSequenceRangeLeft(
                    nonogramRowLogic.getNonogramSolutionBoard(), rowIdx, colIdx
            );

            List<Integer> possibleLengths = ColouringHelper.findPossibleSequenceLengths(
                    nonogramRowLogic.getRowsSequencesRanges().get(rowIdx),
                    colouredRange,
                    nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx)
            );

            if (possibleLengths.isEmpty()) {
                nonogramRowLogic.getNonogramState().invalidateSolution();
                break;
            }

            int minSequenceLength = Collections.min(possibleLengths);
            int distFromX = ColouringHelper.findDistanceFromRightX(
                    nonogramRowLogic.getNonogramSolutionBoard(), rowIdx, colouredRange, minSequenceLength
            );

            if (distFromX > 0) {
                int minExtensionIdx = colouredRange.get(0) + distFromX - minSequenceLength;
                boolean extended = ColouringHelper.extendToLeft(
                        nonogramRowLogic,
                        colouringHelper,
                        nonogramRowLogic.getActionScheduler(),
                        rowIdx,
                        colouredRange.get(0) - 1,
                        minExtensionIdx
                );

                anyGlobalFieldColoured |= extended;
            }

            colIdx = colouredRange.get(0) - 1;
        }

        if (anyGlobalFieldColoured) {
            List<String> rowAfter = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
            nonogramRowLogic.setTmpLog(ExtendLogHelper.generateLog(
                    rowIdx,
                    "toLeft",
                    rowBefore,
                    nonogramRowLogic.getRowsSequencesRanges().get(rowIdx),
                    nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    rowAfter,
                    true
            ));
            nonogramRowLogic.addLog();
        }
    }

    private void extendColouredFieldsToRightNearX(int rowIdx) {
        List<String> rowBefore = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyGlobalFieldColoured = false;

        int colIdx = 0;
        int width = nonogramRowLogic.getNonogramRules().getWidth();

        while (colIdx < width) {
            Field currentField = new Field(rowIdx, colIdx);

            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), currentField)) {
                colIdx++;
                continue;
            }

            List<Integer> colouredRange = ColouringHelper.findColouredSequenceRangeRight(
                    nonogramRowLogic.getNonogramSolutionBoard(), rowIdx, colIdx
            );

            List<Integer> possibleLengths = ColouringHelper.findPossibleSequenceLengths(
                    nonogramRowLogic.getRowsSequencesRanges().get(rowIdx),
                    colouredRange,
                    nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx)
            );

            if (possibleLengths.isEmpty()) {
                nonogramRowLogic.getNonogramState().invalidateSolution();
                break;
            }

            int minSequenceLength = Collections.min(possibleLengths);
            int distanceFromX = ColouringHelper.findDistanceFromLeftX(
                    nonogramRowLogic.getNonogramSolutionBoard(), rowIdx, colouredRange, minSequenceLength
            );

            if (distanceFromX > 0) {
                int maxExtensionIdx = colouredRange.get(1) - distanceFromX + minSequenceLength;
                boolean extended = ColouringHelper.extendToRight(
                        nonogramRowLogic,
                        colouringHelper,
                        nonogramRowLogic.getActionScheduler(),
                        rowIdx,
                        colouredRange.get(1) + 1,
                        maxExtensionIdx
                );

                anyGlobalFieldColoured |= extended;
            }

            colIdx = colouredRange.get(1) + 1;
        }

        if (anyGlobalFieldColoured) {
            List<String> rowAfter = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
            nonogramRowLogic.setTmpLog(ExtendLogHelper.generateLog(
                    rowIdx,
                    "toRight",
                    rowBefore,
                    nonogramRowLogic.getRowsSequencesRanges().get(rowIdx),
                    nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    rowAfter,
                    true
            ));
            nonogramRowLogic.addLog();
        }
    }

    @Override
    public void refreshFrom(NonogramRowLogic logicToCopy) {
        nonogramRowLogic.setNonogramSolutionBoard(logicToCopy.getNonogramSolutionBoard());

        nonogramRowLogic.setRowsSequencesRanges(logicToCopy.getRowsSequencesRanges());
        nonogramRowLogic.setRowsFieldsNotToInclude(logicToCopy.getRowsFieldsNotToInclude());
    }
}
