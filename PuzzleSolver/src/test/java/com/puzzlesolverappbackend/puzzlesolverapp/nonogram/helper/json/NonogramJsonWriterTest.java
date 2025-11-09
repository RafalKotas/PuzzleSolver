package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NonogramJsonWriterTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("NonogramJsonWriter constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<NonogramJsonWriter> constructor = NonogramJsonWriter.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("saveSolutionBoard should write file matching json structure")
    void saveSolutionBoard_writesFile_andJsonMatchesStructure() throws Exception {
        // given
        List<List<String>> expectedBoard = testBoard();
        Path target = tempDir.resolve("ro07931.json");
        ObjectMapper mapper = new ObjectMapper();

        // when
        NonogramJsonWriter.saveSolutionBoard(expectedBoard, target.toString());

        // then
        assertTrue(Files.exists(target), "File should not be saved");
        String content = Files.readString(target);
        JsonNode root = mapper.readTree(content);

        assertTrue(root.has("nonogramBoard"), "JSON should contain field 'nonogramBoard'");

        List<List<String>> parsed = mapper.convertValue(
                root.get("nonogramBoard"),
                new TypeReference<>() {
                }
        );

        assertEquals(expectedBoard, parsed, "Saved board should be equal to entry board");
    }

    @Test
    @DisplayName("saveSolutionBoard should throw Exception when path is directory")
    void saveSolutionBoard_throwsIOException_whenPathIsDirectory() throws Exception {
        // given
        List<List<String>> board = Arrays.asList(
                Arrays.asList("X", "O"),
                Arrays.asList("O", "X")
        );

        Path dirAsTarget = Files.createDirectory(tempDir.resolve("as_directory"));

        // when & then
        assertThrows(IOException.class,
                () -> NonogramJsonWriter.saveSolutionBoard(board, dirAsTarget.toString()),
                "Attempt to path folder should throw IOException");
    }

    private static List<List<String>> testBoard() {
        return new ArrayList<>(Arrays.asList(
                row("X X X X X X X X X X O O X X X X X X X X X X X X X X X X X X"),
                row("X X X X X X X X X X O O O X X X X X X X X X X X X X X X X X"),
                row("X X X X X X X X X O O O O O O O X X X X X X X X X X X X X X"),
                row("X X X X X X X X O O O O X O O O O X X X X X X X X X X X X X"),
                row("X X X X X X X X O O O X X X X X O O O X X X X X X X X X X X"),
                row("X X X X X X X O O O O O O O O X X O O O O X X X X X X X X X"),
                row("X X X X X X X X O O X X X O O O X X X O O O O O X X X X X X"),
                row("X X X X X X X X O X O O X O X O O X X X X X O O O O X X X X"),
                row("X X X X X X X X O X O O X O X X O O X X X X X X X O O O O O"),
                row("X X X X X X X X O X X X X O X X X O O X X X X X X X X X X X"),
                row("O O O O X X X X O O O O O O X X X X O O X X X X X X X X X X"),
                row("X X X O O O X X O O O O O O O O O X X O O O X X X X X X X X"),
                row("X X X X X O O X O O O O O O X X O O X X X O X X X X X X X X"),
                row("O O O O X X O X O X X X X O X X X O O X X O O O X X X X X X"),
                row("O O X O O X O X O X X X X O X O X X O X X X X O X X X X X X"),
                row("O X X X O X O X O X X X X O X O O O O X X X X O O O X X X X"),
                row("O X X X O X O X O X X X X O X O O O O O O O X X O O X X X X"),
                row("O X O X O X O X O O O O O O X O X X X X X O O X X O O O X X"),
                row("O X O X O X O X O O O O O O X O X O O O X X O O X X O O X X"),
                row("O O O X O X O X O O O O O O X O X O X O O X X O X X X O O O"),
                row("O O O X O X O X O X X X X O X O X O X X X X O O X X X X X X"),
                row("X X X X O X O X O X X X X O X O X O X X O O O X X O O X X X"),
                row("O O O O O X O X O X X X X O X O X O O O O X X X O O O O X X"),
                row("X X X X X X O X O X X X X O X O X X X X X X O O O X X O O O"),
                row("X X O O O O O X O O O O O O X O O O O X X X O X X X X X X O"),
                row("O O O X X X X X O O O O O O O X X X O O X X O X O O O X X O"),
                row("O X X X O O O O X X O O O O O O O X X O O X O X O X X O X O"),
                row("O X O O O X X X X O X X X X X X O O X X O X O X O X X X X O"),
                row("O X O O X X O O O O O O O O O X X O O X O X O X O X X X O O"),
                row("O X O O O O O X X X X X X X O X X X O X O X O X O O O O O O")
        ));
    }

    private static List<String> row(String spaced) {
        String[] parts = spaced.split("\\s+");
        ArrayList<String> list = new ArrayList<>(parts.length);
        Collections.addAll(list, parts);
        return list;
    }
}