package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.puzzlesolverappbackend.puzzleAppFileManager.helpers.FileHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogic;
import lombok.Getter;
import lombok.Setter;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Setter
@Getter
public class NonogramSolution {
    List<List<String>> nonogramBoard;

    public static NonogramSolution loadSolutionFromFile(String filename) {
        Gson gson = new Gson();
        try {
            String filePath = FileHelper.nonogramSolutionLoadPathForFilename(filename);
            FileReader reader = new FileReader(filePath);
            JsonElement jsonElement = JsonParser.parseReader(reader);
            return gson.fromJson(jsonElement, NonogramSolution.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean subSolutionCorrect(NonogramLogic nonogramLogicSubSolution) {
        List<List<String>> subSolutionBoard = nonogramLogicSubSolution.getNonogramSolutionBoard();

        if (subSolutionBoard.size() != nonogramBoard.size()) {
            return false;
        }

        for (int i = 0; i < subSolutionBoard.size(); i++) {
            List<String> subRow = subSolutionBoard.get(i);
            List<String> fullRow = nonogramBoard.get(i);

            if (subRow.size() != fullRow.size()) {
                return false;
            }

            for (int j = 0; j < subRow.size(); j++) {
                String subCell = subRow.get(j);
                String fullCell = fullRow.get(j);

                if (subCell.equals("X")) {
                    if (!fullCell.equals("X")) {
                        return false;
                    }
                } else if (subCell.equals("O")) {
                    if (!fullCell.equals("O")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
