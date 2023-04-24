package com.puzzlesolverappbackend.puzzleAppFileManager.templates.nonogram;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Getter
@Setter
public class NonogramBoardTemplate {

    private List<List<String>> board;
    private String filename = "non-specified";

    private final String nonogramBoardTemplatesPath = "./src/main/resources/templates/nonogramBoards/";

    public NonogramBoardTemplate(String filename) {

        this.setFilename(filename);
        Gson gson = new Gson();

        try {
            NonogramBoardTemplate nonogramBoardTemplate =
                    gson.fromJson(new FileReader(nonogramBoardTemplatesPath + filename + ".json"), NonogramBoardTemplate.class);
            this.board = nonogramBoardTemplate.getBoard();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void printBoard () {
        System.out.println("TEMPLATE BOARD " + this.filename);
        if(this.getBoard().size() > 0) {
            for (List<String> boardRow : this.getBoard()) {
                System.out.println(boardRow);
            }
        }
    }
}
