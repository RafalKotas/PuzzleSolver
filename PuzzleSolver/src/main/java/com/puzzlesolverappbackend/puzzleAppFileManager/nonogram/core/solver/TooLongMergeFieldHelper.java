package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.Field;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver.BoardUtils.isFieldColoured;

public interface TooLongMergeFieldHelper {

    static List<List<Integer>> collectColouredSequencesRanges(List<List<String>> board, int index, boolean isRow) {
        List<List<Integer>> colouredRanges = new ArrayList<>();
        List<String> line = isRow ? board.get(index) : getColumn(board, index);

        int start = -1;
        for (int i = 0; i < line.size(); i++) {
            Field f = isRow ? new Field(index, i) : new Field(i, index);
            if (isFieldColoured(board, f)) {
                if (start == -1) start = i;
            } else {
                if (start != -1) {
                    colouredRanges.add(List.of(start, i - 1));
                    start = -1;
                }
            }
        }
        if (start != -1) {
            colouredRanges.add(List.of(start, line.size() - 1));
        }

        return colouredRanges;
    }

    static Map<List<Integer>, List<Integer>> matchColouredSequencesToPossibleSeqIDs(
            List<List<Integer>> colouredSequences,
            List<List<Integer>> sequenceRanges
    ) {
        Map<List<Integer>, List<Integer>> mapping = new LinkedHashMap<>();

        for (List<Integer> coloured : colouredSequences) {
            int start = coloured.get(0);
            int end = coloured.get(1);
            List<Integer> possibleSeqIDs = new ArrayList<>();

            for (int seqIdx = 0; seqIdx < sequenceRanges.size(); seqIdx++) {
                List<Integer> range = sequenceRanges.get(seqIdx);
                int rangeStart = range.get(0);
                int rangeEnd = range.get(1);

                if (start >= rangeStart && end <= rangeEnd) {
                    possibleSeqIDs.add(seqIdx);
                }
            }

            mapping.put(coloured, possibleSeqIDs);
        }

        return mapping;
    }

    static List<String> getColumn(List<List<String>> board, int colIdx) {
        List<String> column = new ArrayList<>();
        for (List<String> row : board) {
            column.add(row.get(colIdx));
        }
        return column;
    }
}
