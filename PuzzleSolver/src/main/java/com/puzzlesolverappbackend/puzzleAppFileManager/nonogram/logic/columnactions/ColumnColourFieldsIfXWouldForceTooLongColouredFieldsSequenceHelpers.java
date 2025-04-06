package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.get2dimArrayColumn;

public interface ColumnColourFieldsIfXWouldForceTooLongColouredFieldsSequenceHelpers {

    static List<List<Integer>> collectColouredSequencesRangesInColumn(List<List<String>> solutionBoard, int columnIdx) {
        List<List<Integer>> colouredRanges = new ArrayList<>();
        List<String> column = get2dimArrayColumn(solutionBoard, columnIdx);

        int rowStartIdx = -1; // -1 mean that is not set yet
        Field field;
        for (int rowIdx = 0; rowIdx < column.size(); rowIdx++) {
            field = new Field(rowIdx, columnIdx);
            if (isFieldColoured(solutionBoard, field)) {
                if (rowStartIdx == -1) {
                    rowStartIdx = columnIdx; // first coloured field in range
                }
            } else {
                if (rowStartIdx != -1) { // coloured range started and field is not coloured
                    colouredRanges.add(List.of(rowStartIdx, rowIdx - 1));
                    rowStartIdx = -1;
                }
            }
        }

        // if coloured sequence range has start index but end index not added (end of row)
        if (rowStartIdx != -1) {
            colouredRanges.add(List.of(rowStartIdx, column.size() - 1));
        }

        return colouredRanges;
    }

    static Map<List<Integer>, List<Integer>> matchColouredSequencesToPossibleSeqIDs(
            List<List<Integer>> colouredSequences,
            List<List<Integer>> columnSequencesRanges
    ) {
        Map<List<Integer>, List<Integer>> mapping = new HashMap<>();

        for (List<Integer> coloured : colouredSequences) {
            int start = coloured.get(0);
            int end = coloured.get(1);
            List<Integer> possibleSeqIDs = new ArrayList<>();

            for (int columnSeqIdx = 0; columnSeqIdx < columnSequencesRanges.size(); columnSeqIdx++) {
                List<Integer> range = columnSequencesRanges.get(columnSeqIdx);
                int rangeStart = range.get(0);
                int rangeEnd = range.get(1);

                if (start >= rangeStart && end <= rangeEnd) {
                    possibleSeqIDs.add(columnSeqIdx);
                }
            }

            mapping.put(coloured, possibleSeqIDs);
        }

        return mapping;
    }
}
