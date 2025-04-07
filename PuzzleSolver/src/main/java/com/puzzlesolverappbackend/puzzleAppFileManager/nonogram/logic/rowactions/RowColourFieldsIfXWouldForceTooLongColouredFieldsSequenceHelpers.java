package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;

public interface RowColourFieldsIfXWouldForceTooLongColouredFieldsSequenceHelpers {

    static List<List<Integer>> collectColouredSequencesRangesInRow(List<List<String>> solutionBoard, int rowIdx) {
        List<List<Integer>> colouredRanges = new ArrayList<>();
        List<String> row = solutionBoard.get(rowIdx);

        int columnStartIdx = -1; // -1 mean that is not set yet
        Field field;
        for (int columnIdx = 0; columnIdx < row.size(); columnIdx++) {
            field = new Field(rowIdx, columnIdx);
            if (isFieldColoured(solutionBoard, field)) {
                if (columnStartIdx == -1) {
                    columnStartIdx = columnIdx; // first coloured field in range
                }
            } else {
                if (columnStartIdx != -1) { // coloured range started and field is not coloured
                    colouredRanges.add(List.of(columnStartIdx, columnIdx - 1));
                    columnStartIdx = -1;
                }
            }
        }

        // if coloured sequence range has start index but end index not added (end of row)
        if (columnStartIdx != -1) {
            colouredRanges.add(List.of(columnStartIdx, row.size() - 1));
        }

        return colouredRanges;
    }

    static Map<List<Integer>, List<Integer>> matchColouredSequencesToPossibleSeqIDs(
            List<List<Integer>> colouredSequences,
            List<List<Integer>> rowSequencesRanges
    ) {
        // order of added elements is crucial
        Map<List<Integer>, List<Integer>> mapping = new LinkedHashMap<>();

        for (List<Integer> coloured : colouredSequences) {
            int start = coloured.get(0);
            int end = coloured.get(1);
            List<Integer> possibleSeqIDs = new ArrayList<>();

            for (int rowSeqIdx = 0; rowSeqIdx < rowSequencesRanges.size(); rowSeqIdx++) {
                List<Integer> range = rowSequencesRanges.get(rowSeqIdx);
                int rangeStart = range.get(0);
                int rangeEnd = range.get(1);

                if (start >= rangeStart && end <= rangeEnd) {
                    possibleSeqIDs.add(rowSeqIdx);
                }
            }

            mapping.put(coloured, possibleSeqIDs);
        }

        return mapping;
    }
}
