package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.dto;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.logic.NonogramState;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.rules.NonogramRules;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class NonogramLogicResponse {
    private List<List<String>> nonogramSolutionBoard;
    private List<List<String>> nonogramSolutionBoardWithMarks;
    private List<List<List<Integer>>> rowsSequencesRanges;
    private List<List<List<Integer>>> columnsSequencesRanges;
    private List<List<Integer>> rowsFieldsNotToInclude;
    private List<List<Integer>> columnsFieldsNotToInclude;
    private List<List<Integer>> rowsSequencesIdsNotToInclude;
    private List<List<Integer>> columnsSequencesIdsNotToInclude;
    private NonogramRules nonogramRules;
    private NonogramState nonogramState;
}
