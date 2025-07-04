package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.puzzlesolverappbackend.puzzleAppFileManager.common.FileHelper;
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
            //e.printStackTrace();
            return null;
        }
    }


}
