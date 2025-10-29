package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

// TODO - generate logs and implement action
@UtilityClass
public class AssignmentConflictLogHelper {

    private final String ASSIGNMENT_CONFLICT_RAW_LOG_TODO = "TODO ASSIGNMENT CONFLICT RAW LOG - TODO";

    private final String ASSIGNMENT_CONFLICT_CONVERTED_LOG_TODO = "TODO ASSIGNMENT CONFLICT CONVERTED LOG - TODO";

    public static String generateLog(
            int index,
            boolean isRow,
            String direction,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges,
            List<String> initialLine,
            List<String> updatedLine
    ) {
        return ASSIGNMENT_CONFLICT_RAW_LOG_TODO;
    }

    public static String convertLogToTestArguments(
            String log,
            String solutionName
    ) {
        return ASSIGNMENT_CONFLICT_CONVERTED_LOG_TODO;
    }
}
