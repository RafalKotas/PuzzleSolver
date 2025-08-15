package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            String direction,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges,
            List<String> initialLine,
            List<String> updatedLine
    ) {
        return "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW - RAW LOG TODO";
    }

    public static String convertLogToTestArguments(
            String logText,
            String solutionName
    ) {
        return "PREVENT EXTENDING COLOURED SEQUENCE TO EXCESS LENGTH COLOURING PART - CONVERTED LOG TODO";
    }
}
