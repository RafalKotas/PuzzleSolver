package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramSolution;
import lombok.experimental.UtilityClass;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@UtilityClass
public class NonogramJsonWriter {

    public static void writeToFile(NonogramFileDetails details, String filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("filename", details.getFilename());
        jsonObject.addProperty("source", details.getSource());
        jsonObject.addProperty("year", details.getYear());
        jsonObject.addProperty("month", details.getMonth());
        jsonObject.addProperty("difficulty", details.getDifficulty());
        jsonObject.addProperty("height", details.getHeight());
        jsonObject.addProperty("width", details.getWidth());
        jsonObject.add("rowSequences", gson.toJsonTree(details.getRowSequences()));
        jsonObject.add("columnSequences", gson.toJsonTree(details.getColumnSequences()));

        String formattedJson = JsonFormatter.format(jsonObject);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(formattedJson);
        }
    }

    public static void saveSolutionBoard(List<List<String>> board, String filePath) throws IOException {
        NonogramSolution solution = new NonogramSolution();
        solution.setNonogramBoard(board);

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("\t\"nonogramBoard\" : [\n");

        List<List<String>> nonogramBoard = solution.getNonogramBoard();
        for (int i = 0; i < nonogramBoard.size(); i++) {
            List<String> row = nonogramBoard.get(i);

            jsonBuilder.append("\t\t[");

            for (int j = 0; j < row.size(); j++) {
                jsonBuilder.append("\"").append(row.get(j)).append("\"");
                if (j < row.size() - 1) {
                    jsonBuilder.append(", ");
                }
            }

            jsonBuilder.append("]");

            if (i < nonogramBoard.size() - 1) {
                jsonBuilder.append(",");
            }

            jsonBuilder.append("\n");
        }

        jsonBuilder.append("\t]\n");
        jsonBuilder.append("}\n");

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonBuilder.toString());
        }
    }
}
