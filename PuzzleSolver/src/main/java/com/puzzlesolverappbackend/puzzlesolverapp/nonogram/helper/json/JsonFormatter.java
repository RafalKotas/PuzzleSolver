package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

@UtilityClass
class JsonFormatter {

    public static String format(JsonElement jsonElement) {
        StringBuilder sb = new StringBuilder();
        format(jsonElement, sb, "");
        return sb.toString();
    }

    private static void format(JsonElement element, StringBuilder sb, String indent) {
        if (element.isJsonObject()) {
            formatObject(element.getAsJsonObject(), sb, indent);
        } else if (element.isJsonArray()) {
            formatArray(element.getAsJsonArray(), sb, indent);
        } else {
            sb.append(element);
        }
    }

    private static void formatObject(JsonObject obj, StringBuilder sb, String indent) {
        sb.append("{\n");
        int count = obj.entrySet().size();
        int i = 0;
        for (var entry : obj.entrySet()) {
            sb.append(indent).append("\t\"").append(entry.getKey()).append("\" : ");
            if (isSpecialArrayKey(entry.getKey())) {
                formatSpecialArray(entry.getValue(), sb, indent);
            } else {
                format(entry.getValue(), sb, indent + "\t");
            }
            if (++i < count) sb.append(",");
            sb.append("\n");
        }
        sb.append(indent).append("}");
    }

    private static boolean isSpecialArrayKey(String key) {
        return "rowSequences".equals(key) || "columnSequences".equals(key);
    }

    private static void formatSpecialArray(JsonElement element, StringBuilder sb, String indent) {
        sb.append("[\n").append(indent).append("\t\t");
        format(element, sb, indent + "\t");
        sb.append("\n").append(indent).append("\t]");
    }

    private static void formatArray(JsonArray array, StringBuilder sb, String indent) {
        int count = 0;
        for (JsonElement item : array) {
            if (count % 5 == 0 && count > 0) {
                sb.append("\n").append(indent).append("\t");
            }
            formatInnerArray(item.getAsJsonArray(), sb);
            if (++count < array.size()) {
                sb.append(", ");
            }
        }
    }

    private static void formatInnerArray(JsonArray innerArray, StringBuilder sb) {
        sb.append("[");
        for (int i = 0; i < innerArray.size(); i++) {
            sb.append(innerArray.get(i));
            if (i < innerArray.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
    }
}
