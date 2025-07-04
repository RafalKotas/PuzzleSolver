package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.dto;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramRules;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonogramSolvePayload {

    private NonogramRules nonogramRules;
    private List<List<String>> nonogramSolutionBoard;
    private List<List<String>> nonogramSolutionBoardWithMarks;
    private List<List<Integer>> rowSequences;
    private List<List<Integer>> columnSequences;
    private List<List<List<Integer>>> rowsSequencesRanges;
    private List<List<List<Integer>>> columnsSequencesRanges;
    private List<List<Integer>> rowsFieldsNotToInclude;
    private List<List<Integer>> columnsFieldsNotToInclude;
    private List<List<Integer>> rowsSequencesIdsNotToInclude;
    private List<List<Integer>> columnsSequencesIdsNotToInclude;
}

