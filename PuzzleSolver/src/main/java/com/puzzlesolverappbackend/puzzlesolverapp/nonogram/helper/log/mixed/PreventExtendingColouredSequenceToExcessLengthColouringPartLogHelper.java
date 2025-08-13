package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;

import java.util.List;

public class PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper {

    public static String generateExtendSequenceLog(
            int index,
            boolean isRow,
            String direction,
            List<Integer> sequenceLengths,
            List<List<Integer>> sequenceRanges,
            List<String> initialLine,
            List<String> finalLine
    ) {
        return "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW - RAW LOG TODO";
    }

    public static String convertLogToTestArguments(
            String logText,
            String solutionName,
            NonogramLogic logic
    ) {
        return "PREVENT EXTENDING COLOURED SEQUENCE TO EXCESS LENGTH COLOURING PART - CONVERTED LOG TODO";
    }
}
