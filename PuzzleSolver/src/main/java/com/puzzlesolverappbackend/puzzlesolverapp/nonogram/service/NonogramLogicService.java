package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicFactory;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramSolver;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionNode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.FinalNonogramSolutionDTO;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramInitializationRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolutionSaveRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.ColumnColouringHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.ColumnSequencesCorrectionHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.ColumnXPlacementHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.RowColouringHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.RowSequencesCorrectionHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.RowXPlacementHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.mapper.NonogramMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.saver.NonogramSolutionSaver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils.NonogramStatsUtils.isSolved;

@Service
@Slf4j
public class NonogramLogicService {

    private final NonogramLogicFactory logicFactory;

    private final NonogramSolutionSaver nonogramSolutionSaver;

    private static final boolean SHOW_REPETITIONS = false;

    public NonogramLogicService(NonogramSolutionSaver nonogramSolutionSaver,
                                NonogramLogicFactory logicFactory) {
        this.nonogramSolutionSaver = nonogramSolutionSaver;
        this.logicFactory = logicFactory;
    }

    public NonogramLogic initializeLogicFromRequest(NonogramInitializationRequest request) {
        NonogramRules rules = new NonogramRules(request.getRowSequences(), request.getColumnSequences(), request.getHeight(), request.getWidth());

        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    /**
     * Applies the overlapping field fill logic for each column in the given range.
     *
     * @param logic the {@link NonogramLogic} object representing the current puzzle state
     * @param columnBegin the index of the first column (inclusive)
     * @param columnEnd the index of the last column (exclusive)
     * @return the updated {@link NonogramLogic} with applied column overlap fills
     */
    public NonogramLogic fillOverlappingFieldsInColumnsRange(NonogramLogic logic, int columnBegin, int columnEnd) {
        for (int columnIdx = columnBegin; columnIdx <= columnEnd; columnIdx++) {
            logic = fillOverlappingFieldsInColumn(logic, columnIdx);
        }
        return logic;
    }

    /**
     * <p>
     * Fills in overlapping fields in a column with coloured values and sequence marks.
     * </p>
     *
     * <p><strong>Steps:</strong></p>
     * <ol>
     *     <li>Get sequence lengths and ranges in the column.</li>
     *     <li>For each sequence, calculate overlapping (definitely coloured) range.</li>
     *     <li>If such a range exists, mark and colour all fields in it.</li>
     *     <li>Return updated logic object.</li>
     * </ol>
     *
     * @param logic the {@link NonogramLogic} object representing the current state of the nonogram
     * @param columnIdx the index of the column being processed
     * @return the updated {@link NonogramLogic} object with newly coloured and marked fields
     */
    public NonogramLogic fillOverlappingFieldsInColumn(NonogramLogic logic, int columnIdx) {
        ColumnColouringHelperImpl columnColouringHelper = new ColumnColouringHelperImpl(
                logic.getNonogramColumnLogic()
        );

        columnColouringHelper.colourOverlappingFieldsInColumn(columnIdx);

        return logic;
    }

    /**
     * Applies the overlapping field fill logic for each column in the given range.
     *
     * @param logic the {@link NonogramLogic} object representing the current puzzle state
     * @param rowBegin the index of the first row (inclusive)
     * @param rowEnd the index of the last row (exclusive)
     * @return the updated {@link NonogramLogic} with applied row overlap fills
     */
    public NonogramLogic fillOverLappingFieldsInRowsRange(NonogramLogic logic, int rowBegin, int rowEnd) {
        for (int rowIdx = rowBegin; rowIdx <= rowEnd; rowIdx++) {
            fillOverlappingFieldsInRow(logic, rowIdx);
        }
        return logic;
    }

    /**
     * <p>
     * Fills in overlapping fields in a row with coloured values and sequence marks.
     * </p>
     *
     * <p>
     * Steps:
     * <ol>
     *     <li>Get sequence lengths and ranges in the row.</li>
     *     <li>For each sequence, calculate overlapping (definitely coloured) range.</li>
     *     <li>If such a range exists, mark and colour all fields in it.</li>
     *     <li>Return updated logic object.</li>
     * </ol>
     * </p>
     *
     * @param logic the {@link NonogramLogic} object representing the current state of the nonogram
     * @param rowIdx the row index to apply the operation on
     * @return updated {@link NonogramLogic} with overlapping row fields filled
     */
    private NonogramLogic fillOverlappingFieldsInRow(NonogramLogic logic, int rowIdx) {
        RowColouringHelperImpl rowColouringHelper = new RowColouringHelperImpl(
                logic.getNonogramRowLogic()
        );

        rowColouringHelper.colourOverlappingFieldsInRow(rowIdx);

        return logic;
    }

    // mark iterations through all rows
    public NonogramLogic markAvailableSequencesInRows(NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {
        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for (int rowIdx = rowBegin; rowIdx <= rowEnd; rowIdx++) {
            nonogramLogicDataToChange = markAvailableSequencesInRow(nonogramLogicDataToChange, rowIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic markAvailableSequencesInRow(NonogramLogic nonogramLogicObject, int rowIdx) {
        nonogramLogicObject.getNonogramRowLogic().markAvailableFieldsInRow(rowIdx);

        return nonogramLogicObject;
    }

    // iterations through all columns
    public NonogramLogic markAvailableSequencesInColumns(NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {
        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for (int columnIdx = columnBegin; columnIdx <= columnEnd; columnIdx++) {
            nonogramLogicDataToChange = markAvailableSequencesInColumn(nonogramLogicDataToChange, columnIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic markAvailableSequencesInColumn(NonogramLogic nonogramLogicObject, int columnIdx) {
        nonogramLogicObject.getNonogramColumnLogic().markAvailableFieldsInColumn(columnIdx);

        return nonogramLogicObject;
    }

    // iterations through all rows
    public NonogramLogic placeXsAroundLongestSequencesInRowsRange(NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {
        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for (int rowIdx = rowBegin; rowIdx <= rowEnd; rowIdx++) {
            nonogramLogicDataToChange = placeXsAroundLongestSequencesInRow(nonogramLogicDataToChange, rowIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic placeXsAroundLongestSequencesInRow(NonogramLogic nonogramLogicObject, int rowIdx) {
        RowXPlacementHelperImpl rowXPlacementHelper = new RowXPlacementHelperImpl(
                nonogramLogicObject.getNonogramRowLogic()
        );

        rowXPlacementHelper.placeXsAroundLongestSequencesInRow(rowIdx);

        return nonogramLogicObject;
    }

    // iterations through all columns
    public NonogramLogic placeXsAroundLongestSequencesInColumnsRange(NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {
        for (int columnIdx = columnBegin; columnIdx <= columnEnd; columnIdx++) {
            placeXsAroundLongestSequencesInColumn(nonogramLogicObject, columnIdx);
        }

        return nonogramLogicObject;
    }

    private void placeXsAroundLongestSequencesInColumn(NonogramLogic nonogramLogicObject, int columnIdx) {
        ColumnXPlacementHelperImpl columnXPlacementHelper = new ColumnXPlacementHelperImpl(
                nonogramLogicObject.getNonogramColumnLogic()
        );

        columnXPlacementHelper.placeXsAroundLongestSequencesInColumn(columnIdx);
    }

    // iterations through all rows
    public NonogramLogic placeXsAtUnreachableFieldsInRowsRange(NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {
        for (int rowIdx = rowBegin; rowIdx <= rowEnd; rowIdx++) {
            placeXsAtUnreachableFieldsInRow(nonogramLogicObject, rowIdx);
        }

        return nonogramLogicObject;
    }

    private void placeXsAtUnreachableFieldsInRow(NonogramLogic nonogramLogicObject, int rowIdx) {
        RowXPlacementHelperImpl rowXPlacementHelper = new RowXPlacementHelperImpl(
                nonogramLogicObject.getNonogramRowLogic()
        );

        rowXPlacementHelper.placeXsRowAtUnreachableFields(rowIdx);
    }

    // iterations through all columns
    public NonogramLogic placeXsAtUnreachableFieldsInColumnsRange(NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {
        for (int columnIdx = columnBegin; columnIdx <= columnEnd; columnIdx++) {
            placeXsAtUnreachableFieldsInColumn(nonogramLogicObject, columnIdx);
        }

        return nonogramLogicObject;
    }

    private void placeXsAtUnreachableFieldsInColumn(NonogramLogic nonogramLogicObject, int columnIdx) {
        ColumnXPlacementHelperImpl columnXPlacementHelper = new ColumnXPlacementHelperImpl(
                nonogramLogicObject.getNonogramColumnLogic()
        );

        columnXPlacementHelper.placeXsColumnAtUnreachableFields(columnIdx);
    }

    // iterations through all rows
    public NonogramLogic correctRowsSequencesRanges (NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {
        for (int rowIdx = rowBegin; rowIdx <= rowEnd; rowIdx++) {
            correctRowSequencesRanges(nonogramLogicObject, rowIdx);
            correctRowSequencesWhenMetColouredField(nonogramLogicObject, rowIdx);
            changeRowRangeIndexesIfXOnWay(nonogramLogicObject, rowIdx);
        }

        return nonogramLogicObject;
    }

    private void correctRowSequencesRanges (NonogramLogic nonogramLogicObject, int rowIdx) {
        RowSequencesCorrectionHelperImpl rowSequencesCorrectionHelper = new RowSequencesCorrectionHelperImpl(
                nonogramLogicObject.getNonogramRowLogic()
        );

        rowSequencesCorrectionHelper.correctRowSequencesRanges(rowIdx);
    }

    // TODO - create test just for this action
    private void correctRowSequencesWhenMetColouredField(NonogramLogic nonogramLogicObject, int rowIdx) {
        RowSequencesCorrectionHelperImpl rowSequencesCorrectionHelper = new RowSequencesCorrectionHelperImpl(
                nonogramLogicObject.getNonogramRowLogic()
        );

        rowSequencesCorrectionHelper.correctRowSequencesRangesWhenMetColouredField(rowIdx);
    }

    // TODO - create test just for this action
    private void changeRowRangeIndexesIfXOnWay (NonogramLogic nonogramLogicObject, int rowIdx) {
        RowSequencesCorrectionHelperImpl rowSequencesCorrectionHelper = new RowSequencesCorrectionHelperImpl(
                nonogramLogicObject.getNonogramRowLogic()
        );

        rowSequencesCorrectionHelper.correctRowSequencesRangesIfXOnWay(rowIdx, true);
    }

    // iterations through all columns
    public NonogramLogic correctColumnsSequencesRanges (NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {
        for (int columnIdx = columnBegin; columnIdx <= columnEnd; columnIdx++) {
            correctColumnSequencesRanges(nonogramLogicObject, columnIdx);
            changeColumnRangeIndexesIfXOnWay(nonogramLogicObject, columnIdx);
            correctColumnSequencesWhenMetColouredField(nonogramLogicObject, columnIdx);
        }

        return nonogramLogicObject;
    }

    public void correctColumnSequencesRanges(NonogramLogic nonogramLogicObject, int columnIdx) {
        ColumnSequencesCorrectionHelperImpl columnSequencesCorrectionHelper = new ColumnSequencesCorrectionHelperImpl(
                nonogramLogicObject.getNonogramColumnLogic()
        );

        columnSequencesCorrectionHelper.correctColumnSequencesRanges(columnIdx);
    }

    // TODO - create test just for this action
    public void changeColumnRangeIndexesIfXOnWay(NonogramLogic nonogramLogicObject, int columnIdx) {
        ColumnSequencesCorrectionHelperImpl columnSequencesCorrectionHelper = new ColumnSequencesCorrectionHelperImpl(
                nonogramLogicObject.getNonogramColumnLogic()
        );

        columnSequencesCorrectionHelper.correctColumnSequencesRangesIfXOnWay(columnIdx, true);
    }

    // TODO - create test just for this action
    public void correctColumnSequencesWhenMetColouredField(NonogramLogic nonogramLogicObject, int columnIdx) {
        ColumnSequencesCorrectionHelperImpl columnSequencesCorrectionHelper = new ColumnSequencesCorrectionHelperImpl(
                nonogramLogicObject.getNonogramColumnLogic()
        );

        columnSequencesCorrectionHelper.correctColumnSequencesRangesWhenMetColouredField(columnIdx);
    }

    public static boolean rangesListIncludingAnotherRange (List<List<Integer>> listOfRanges, List<Integer> range) {
        for (List<Integer> listOfRange : listOfRanges) {
            if (rangeInsideAnotherRange(range, listOfRange)) {
                return true;
            }
        }

        return false;
    }

    public NonogramLogic runSolverWithCorrectnessCheck(NonogramLogic logic, String fileName) {
        log.info("Running heuristic solver with correctness check...");

        NonogramSolver solver = new NonogramSolver(logic, fileName, GuessMode.DISABLED, logicFactory);
        log.info("Initialized NonogramSolver: {}", solver);

        NonogramSolutionNode rootNode = new NonogramSolutionNode(logic, logicFactory);
        log.info("Initialized NonogramSolutionNode (decisions: {})", rootNode.getNonogramGuessDecisions().size());

        NonogramLogic solvedLogic = solver.runSolutionAtNode(rootNode);

        if (isSolved(solvedLogic)) {
            try {
                NonogramSolutionSaveRequest request = NonogramMapper.toSaveRequest(solvedLogic, fileName);
                saveIfCorrect(request);
                log.info("Solved nonogram was successfully saved.");
            } catch (IOException e) {
                log.error("Failed to save solved nonogram: {}", e.getMessage());
            }
        }

        return solvedLogic;
    }

    public FinalNonogramSolutionDTO saveIfCorrect(NonogramSolutionSaveRequest request) throws IOException {
        return nonogramSolutionSaver.saveIfCorrect(request);
    }
}
