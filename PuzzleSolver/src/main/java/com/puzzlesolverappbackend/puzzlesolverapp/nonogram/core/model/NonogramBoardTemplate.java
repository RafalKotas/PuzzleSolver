package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.exception.NonogramFileReadException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConstants.JSON_EXTENSION;

@Getter
@Setter
@Slf4j
public class NonogramBoardTemplate {

    private List<List<String>> board;

    public NonogramBoardTemplate(String filename) {
        Gson gson = new Gson();
        try {
            NonogramBoardTemplate nonogramBoardTemplate =
                    gson.fromJson(
                            new FileReader(InitializerConstants.NONOGRAM_SOLUTIONS_PATH + filename + JSON_EXTENSION),
                            NonogramBoardTemplate.class
                    );
            this.board = nonogramBoardTemplate.getBoard();
        } catch (FileNotFoundException e) {
            throw new NonogramFileReadException("Could not find nonogram solution file: " + filename, e);
        }
    }


    public void printBoard () {
        if (!this.getBoard().isEmpty()) {
            for (List<String> boardRow : this.getBoard()) {
                log.info("{}", boardRow);
            }
        }
    }
}
