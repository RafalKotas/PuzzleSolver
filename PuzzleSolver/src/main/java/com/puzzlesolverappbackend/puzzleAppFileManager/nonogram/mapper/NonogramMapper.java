package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.mapper;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.dto.NonogramSolutionSaveRequest;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;

public class NonogramMapper {

    public static NonogramSolutionSaveRequest toSaveRequest(NonogramLogic logic, String filename) {
        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setBoard(logic.getNonogramSolutionBoard());
        request.setRowSequences(logic.getNonogramRules().getRowSequencesLengths());
        request.setColumnSequences(logic.getNonogramRules().getColumnSequencesLengths());
        request.setFileName(filename);
        return request;
    }
}

