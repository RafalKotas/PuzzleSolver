package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils;

import com.google.gson.*;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramFileDetails;

import java.io.FileWriter;
import java.io.IOException;

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
}

class JsonFormatter {
    public static String format(JsonElement jsonElement) {
        StringBuilder sb = new StringBuilder();
        format(jsonElement, sb, "");
        return sb.toString();
    }

    private static void format(JsonElement element, StringBuilder sb, String indent) {
        if (element.isJsonObject()) {
            sb.append("{\n");
            JsonObject obj = element.getAsJsonObject();
            int count = obj.entrySet().size();
            int i = 0;
            for (var entry : obj.entrySet()) {
                sb.append(indent).append("\t\"").append(entry.getKey()).append("\" : ");
                if (entry.getKey().equals("rowSequences") || entry.getKey().equals("columnSequences")) {
                    sb.append("[\n").append(indent).append("\t\t");
                    format(entry.getValue(), sb, indent + "\t");
                    sb.append("\n").append(indent).append("\t]");
                } else {
                    format(entry.getValue(), sb, indent + "\t");
                }
                if (++i < count) sb.append(",");
                sb.append("\n");
            }
            sb.append(indent).append("}");
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            int count = 0;
            for (JsonElement item : array) {
                if (count % 5 == 0 && count > 0) sb.append("\n").append(indent).append("\t");
                sb.append("[");
                JsonArray innerArray = item.getAsJsonArray();
                for (int i = 0; i < innerArray.size(); i++) {
                    sb.append(innerArray.get(i).toString());
                    if (i < innerArray.size() - 1) sb.append(", ");
                }
                sb.append("]");
                if (++count < array.size()) sb.append(", ");
            }
        } else {
            sb.append(element);
        }
    }
}
