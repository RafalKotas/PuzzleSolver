package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

// TODO - generate logs and implement action
@UtilityClass
public class AssignmentConflictLogHelper {

    public static String generateLog(
            int index,
            boolean isRow,
            String direction,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges,
            List<String> initialLine,
            List<String> updatedLine
    ) {
        return "TODO ASSIGNMENT CONFLICT RAW LOG - TODO";
    }

    public static String convertLogToTestArguments(
            String log,
            String solutionName
    ) {
        return "TODO ASSIGNMENT CONFLICT CONVERTED LOG - TODO";
    }
}
