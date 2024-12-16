package com.puzzlesolverappbackend.puzzleAppFileManager.templates.nonogram;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.runners.InitializerConstants;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION;

@Getter
@Setter
public class NonogramBoardTemplate {

    private List<List<String>> board;

    public NonogramBoardTemplate(String filename) {
        Gson gson = new Gson();

        try {
            NonogramBoardTemplate nonogramBoardTemplate =
                    gson.fromJson(new FileReader(InitializerConstants.NONOGRAM_SOLUTIONS_PATH + filename + JSON_EXTENSION), NonogramBoardTemplate.class);
            this.board = nonogramBoardTemplate.getBoard();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public NonogramBoardTemplate(NonogramLogic solvedNonogramLogic) {
        this.setBoard(solvedNonogramLogic.getNonogramSolutionBoard());
    }

    public void printBoard () {
        if(!this.getBoard().isEmpty()) {
            for (List<String> boardRow : this.getBoard()) {
                System.out.println(boardRow);
            }
        }
    }
}
