package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.mapper;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramLogicResponse;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolutionSaveRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class NonogramMapper {

    public static NonogramSolutionSaveRequest toSaveRequest(NonogramLogic logic, String filename) {
        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setBoard(logic.getNonogramSolutionBoard());
        request.setRowSequences(logic.getNonogramRules().getRowSequencesLengths());
        request.setColumnSequences(logic.getNonogramRules().getColumnSequencesLengths());
        request.setFileName(filename);
        return request;
    }

    public static NonogramLogicResponse toResponse(NonogramLogic logic) {
        NonogramLogicResponse dto = new NonogramLogicResponse();
        dto.setNonogramSolutionBoard(logic.getNonogramSolutionBoard());
        dto.setNonogramSolutionBoardWithMarks(logic.getNonogramSolutionBoardWithMarks());
        dto.setRowsSequencesRanges(logic.getRowsSequencesRanges());
        dto.setColumnsSequencesRanges(logic.getColumnsSequencesRanges());
        dto.setRowsFieldsNotToInclude(logic.getRowsFieldsNotToInclude());
        dto.setColumnsFieldsNotToInclude(logic.getColumnsFieldsNotToInclude());
        dto.setRowsSequencesIdsNotToInclude(logic.getRowsSequencesIdsNotToInclude());
        dto.setColumnsSequencesIdsNotToInclude(logic.getColumnsSequencesIdsNotToInclude());
        dto.setNonogramRules(logic.getNonogramRules());
        dto.setNonogramState(logic.getNonogramState());
        return dto;
    }
}

