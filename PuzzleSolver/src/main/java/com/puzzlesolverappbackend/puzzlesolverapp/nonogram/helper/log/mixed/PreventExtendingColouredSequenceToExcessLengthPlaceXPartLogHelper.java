package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;

import java.util.List;

public class PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper {

    public static String generateExtendSequenceLog(
            int index,
            boolean isRow,
            String direction,
            List<Integer> sequenceLengths,
            List<List<Integer>> sequenceRanges,
            List<String> initialLine,
            List<String> finalLine
    ) {
        return "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW - RAW LOG TODO";
    }

    public static String convertLogToTestArguments(
            String logText,
            String solutionName,
            NonogramLogic logic
    ) {
        return "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW - CONVERTED LOG TODO";
    }
}
