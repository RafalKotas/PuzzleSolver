package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.mutableClone2DList;

public interface ColumnCorrectSequencesRangesHelper {

    static List<List<Integer>> reduceColouredSequenceMatches(
            List<Integer> columnSequencesLengths,
            List<List<Integer>> colouredSequencesRanges,
            List<List<Integer>> colouredSequencesPartsMatches
    ) {
        List<List<Integer>> reducedMatches = mutableClone2DList(colouredSequencesPartsMatches);

        for (int seqId = 0; seqId < columnSequencesLengths.size(); seqId++) {
            final int currentSeqId = seqId;

            List<Integer> matchedFragmentIndices =
                    IntStream.range(0, colouredSequencesRanges.size())
                            .filter(i -> reducedMatches.get(i).contains(currentSeqId))
                            .boxed()
                            .collect(Collectors.toList());

            if (matchedFragmentIndices.size() < 2) continue;

            matchedFragmentIndices.sort(Comparator.comparingInt(i -> colouredSequencesRanges.get(i).get(0)));

            // FORWARD reduction
            for (int i = 0; i < matchedFragmentIndices.size() - 1; i++) {
                int firstColouredFragment = matchedFragmentIndices.get(i);
                int secondColouredFragment = matchedFragmentIndices.get(i + 1);

                List<Integer> rangeA = colouredSequencesRanges.get(firstColouredFragment);
                List<Integer> rangeB = colouredSequencesRanges.get(secondColouredFragment);

                int mergedColouredFieldsLength = rangeB.get(1) - rangeA.get(0) + 1;

                // remove coloured seq part assignment to seqId only if seqId is first element on the list
                List<Integer> firstMatchList = reducedMatches.get(firstColouredFragment);
                List<Integer> secondMatchList = reducedMatches.get(secondColouredFragment);

                if (firstMatchList.get(0) == currentSeqId && secondMatchList.get(0) == currentSeqId) {
                    if (mergedColouredFieldsLength > columnSequencesLengths.get(seqId)) {
                        secondMatchList.remove((Integer) currentSeqId);
                    }
                }
            }

            // BACKWARD reduction
            for (int i = matchedFragmentIndices.size() - 1; i > 0; i--) {
                int secondColouredFragment = matchedFragmentIndices.get(i);
                int firstColouredFragment = matchedFragmentIndices.get(i - 1);

                List<Integer> rangeA = colouredSequencesRanges.get(firstColouredFragment);
                List<Integer> rangeB = colouredSequencesRanges.get(secondColouredFragment);

                int mergedColouredFieldsLength = rangeB.get(1) - rangeA.get(0) + 1;

                List<Integer> firstMatchList = reducedMatches.get(firstColouredFragment);
                List<Integer> secondMatchList = reducedMatches.get(secondColouredFragment);
                int firstMatchListSize = firstMatchList.size();
                int secondMatchListSize = secondMatchList.size();

                if (firstMatchList.get(firstMatchListSize - 1) == currentSeqId &&  secondMatchList.get(secondMatchListSize - 1) == currentSeqId
                        && mergedColouredFieldsLength > columnSequencesLengths.get(seqId)) {
                        firstMatchList.remove((Integer) currentSeqId);
                }
            }
        }

        return reducedMatches;
    }

    static boolean sequenceAssignmentAppearsAsFirstLater(List<List<Integer>> reducedMatches, int currentIndex, int seqId) {
        for (int i = currentIndex + 1; i < reducedMatches.size(); i++) {
            List<Integer> next = reducedMatches.get(i);
            if (!next.isEmpty() && next.get(0) == seqId) {
                return true;
            }
        }
        return false;
    }
}
