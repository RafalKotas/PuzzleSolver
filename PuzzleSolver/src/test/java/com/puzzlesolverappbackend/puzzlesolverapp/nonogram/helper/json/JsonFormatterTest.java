package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JsonFormatterTest {

    @Test
    @DisplayName("JsonFormatter constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<JsonFormatter> constructor = JsonFormatter.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    // Helper: normalize line endings for cross-platform assertions
    private static String norm(String s) {
        return s.replace("\r\n", "\n");
    }

    @DisplayName("JsonFormatter.format should pretty-print nonogram JSON with special arrays split and indented")
    @Test
    void format_shouldPrettyPrintSpecialArrays() {
        // given
        String rawJson =
                "{\"filename\":\"jsonformattertest.json\",\"source\":\"logi\",\"year\":\"N/D\",\"month\":\"N/D\",\"difficulty\":1.0," +
                        "\"height\":10,\"width\":10," +
                        "\"rowSequences\":[[3],[2,2,1],[3,2],[3,1],[1],[1,1],[3],[9],[8,1],[8,1]]," +
                        "\"columnSequences\":[[4,4],[3,4],[2,4],[3],[1,3],[2,3],[2,3],[1,1,3],[1,1],[3,2]]}";
        JsonElement json = new Gson().fromJson(rawJson, JsonElement.class);

        // when
        String formatted = JsonFormatter.format(json);

        // then
        String expected =
                "{\n" +
                        "\t\"filename\" : \"jsonformattertest.json\",\n" +
                        "\t\"source\" : \"logi\",\n" +
                        "\t\"year\" : \"N/D\",\n" +
                        "\t\"month\" : \"N/D\",\n" +
                        "\t\"difficulty\" : 1.0,\n" +
                        "\t\"height\" : 10,\n" +
                        "\t\"width\" : 10,\n" +
                        "\t\"rowSequences\" : [\n" +
                        "\t\t[3], [2, 2, 1], [3, 2], [3, 1], [1], \n" +
                        "\t\t[1, 1], [3], [9], [8, 1], [8, 1]\n" +
                        "\t],\n" +
                        "\t\"columnSequences\" : [\n" +
                        "\t\t[4, 4], [3, 4], [2, 4], [3], [1, 3], \n" +
                        "\t\t[2, 3], [2, 3], [1, 1, 3], [1, 1], [3, 2]\n" +
                        "\t]\n" +
                        "}";

        assertThat(norm(formatted)).isEqualTo(norm(expected));
    }

    @DisplayName("NonogramJsonWriter.writeToFile should write exactly the same formatted JSON")
    @Test
    void writeToFile_shouldCreateFormattedFile(@TempDir Path tmp) throws IOException {
        // given
        NonogramFileDetails details = new NonogramFileDetails(
                List.of(
                        List.of(3),
                        List.of(2, 2, 1),
                        List.of(3, 2),
                        List.of(3, 1),
                        List.of(1),
                        List.of(1, 1),
                        List.of(3),
                        List.of(9),
                        List.of(8, 1),
                        List.of(8, 1)
                ),
                List.of(
                        List.of(4, 4),
                        List.of(3, 4),
                        List.of(2, 4),
                        List.of(3),
                        List.of(1, 3),
                        List.of(2, 3),
                        List.of(2, 3),
                        List.of(1, 1, 3),
                        List.of(1, 1),
                        List.of(3, 2)
                ),
                "jsonformattertest.json",
                10, 10,
                "logi", "N/D", "N/D",
                1.0,
                null
        );

        Path out = tmp.resolve("jsonformattertest.json");

        // when
        NonogramJsonWriter.writeToFile(details, out.toString());

        // then
        String fileContent = Files.readString(out);

        // Build expected via the same formatter to avoid brittle whitespace issues
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("filename", details.getFilename());
        jsonObject.addProperty("source", details.getSource());
        jsonObject.addProperty("year", details.getYear());
        jsonObject.addProperty("month", details.getMonth());
        jsonObject.addProperty("difficulty", details.getDifficulty());
        jsonObject.addProperty("height", details.getHeight());
        jsonObject.addProperty("width", details.getWidth());
        jsonObject.add("rowSequences", new Gson().toJsonTree(details.getRowSequences()));
        jsonObject.add("columnSequences", new Gson().toJsonTree(details.getColumnSequences()));
        String expected = JsonFormatter.format(jsonObject);

        assertThat(norm(fileContent)).isEqualTo(norm(expected));
    }
}