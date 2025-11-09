package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.initializers;

import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NonogramsDataInitializerTest {

    @Mock
    private NonogramRepository repository;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private NonogramsDataInitializer initializer;

    @TempDir
    Path tempDir;

    @AfterEach
    void resetStaticLists() throws Exception {
        Field filesToCorrectField = NonogramsDataInitializer.class.getDeclaredField("filesToCorrect");
        filesToCorrectField.setAccessible(true);
        ((List<?>) filesToCorrectField.get(null)).clear();

        Field sourceMonthField = NonogramsDataInitializer.class.getDeclaredField("sourceMonthCombinations");
        sourceMonthField.setAccessible(true);
        ((List<?>) sourceMonthField.get(null)).clear();
    }

    @Test
    @DisplayName("Should not save existing nonogram")
    void shouldNotSaveNonogramWhenAlreadyExists() throws Exception {
        // given
        String filename = "existing.json";
        String content = """
            {
              "rowSequences": [[1]],
              "columnSequences": [[2]],
              "filename": "existing",
              "source": "src",
              "year": "2023",
              "month": "06",
              "difficulty": 3.0,
              "height": 4,
              "width": 4
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsNonogramByGivenParamsFromFile(
                anyString(), anyString(), anyString(), anyString(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(Optional.of(new Nonogram()));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Nonogram.class));
    }

    @Test
    @DisplayName("Should save new nonogram when is not present in repository")
    void shouldSaveNewNonogramWhenNotPresentInRepository() throws Exception {
        // given
        String filename = "to-save.json";
        String content = """
            {
              "rowSequences": [[1,2]],
              "columnSequences": [[3,4]],
              "filename": "to-save",
              "source": "logi",
              "year": "2024",
              "month": "07",
              "difficulty": 4.5,
              "height": 5,
              "width": 5,
              "additionalContent": "ignore"
            }
            """;
        Files.writeString(tempDir.resolve(filename), content);

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));
        when(repository.existsNonogramByGivenParamsFromFile(
                "to-save", "logi", "2024", "07", 4.5, 5, 5))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        verify(repository).save(any(Nonogram.class));
        assertThat(initializer.getNonogramRepository()).isNotNull();
        assertThat(initializer.getCommonService()).isNotNull();
    }

    @Test
    @DisplayName("Should not save nonogram with malformed json and log error")
    void shouldLogErrorWhenJsonIsMalformed() throws Exception {
        // given
        String filename = "malformed.json";
        String malformedJson = """
        {
          "rowSequences": [[1,2]],
          "columnSequences": [[3,4]],
          "filename": "malformed",
          "height": 5,
          "width": 5,
          "source": "s",
          "year": "2024",
          "month": "07",
          "difficulty": 4.5,
          "additionalContent": "x"
        """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, malformedJson);

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Nonogram.class));
    }

    @Test
    @DisplayName("analyze: marks file as incorrect when line count mismatches required")
    void analyzeMarksIncorrectWhenLineCountMismatch() throws Exception {
        // given
        // Single-line JSON => lines.size() will be 1, requiredLines > 1 -> false branch
        String filename = "wrong-lines.json";
        String oneLine = "{\"rowSequences\": [[1]], \"columnSequences\": [[2]], \"filename\":\"abc\",\"source\":\"s\",\"year\":\"2024\",\"month\":\"07\",\"difficulty\":2.0,\"height\":5,\"width\":5}";
        Files.writeString(tempDir.resolve(filename), oneLine);

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        // when
        initializer.run();

        // then
        // filesToCorrect contains name without ".json"
        List<String> filesToCorrect = getFilesToCorrect();
        assertThat(filesToCorrect).contains("wrong-lines");
    }

    @Test
    @DisplayName("analyze: marks file as incorrect when property order is wrong (with correct line count)")
    void analyzeMarksIncorrectWhenPropertyOrderWrong() throws Exception {
        // given
        // Build 15 lines total (4 fixed + 9 props + 1 + 1) so lineCountCorrect == true
        // Put 'source' before 'filename' to make order incorrect
        String filename = "wrong-order.json";
        String content = String.join("\n", List.of(
                "{",
                "  \"source\": \"s\",",
                "  \"filename\": \"wo\",",
                "  \"year\": \"2024\",",
                "  \"month\": \"07\",",
                "  \"difficulty\": 3.0,",
                "  \"height\": 5,",
                "  \"width\": 5,",
                "  \"rowSequences\": [[1]],",
                "  \"columnSequences\": [[2]]",
                "}",
                "", "", "", "" // 4 blank lines -> total 15
        ));
        Files.writeString(tempDir.resolve(filename), content);

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        // when
        initializer.run();

        // then
        List<String> filesToCorrect = getFilesToCorrect();
        assertThat(filesToCorrect).contains("wrong-order");
    }

    @Test
    @DisplayName("scan: valid file passes analyze (ceil branches) and is saved")
    void scanValidFilePassesAnalyzeAndSaved() throws Exception {
        // given
        // height=6 -> ceil(6/5)=2, width=7 -> ceil(7/5)=2
        // requiredLines = 4 + 9 + 2 + 2 = 17
        String filename = "ok.json";
        Path path = tempDir.resolve(filename);

        List<String> lines = new ArrayList<>(List.of(
                "{",
                "  \"filename\": \"ok\",",
                "  \"source\": \"s\",",
                "  \"year\": \"2024\",",
                "  \"month\": \"08\",",
                "  \"difficulty\": 1.5,",
                "  \"height\": 6,",
                "  \"width\": 7,",
                "  \"rowSequences\": [[1]],",
                "  \"columnSequences\": [[2]]",
                "}"
        ));
        while (lines.size() < 17) lines.add("");

        Files.write(path, lines);

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));
        when(repository.existsNonogramByGivenParamsFromFile(
                "ok", "s", "2024", "08", 1.5, 6, 7))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        verify(repository).save(any(Nonogram.class));
    }

    @Test
    @DisplayName("deduplicate (source, month) pairs while scanning")
    void deduplicateSourceMonthCombinations() throws Exception {
        // given
        String f1 = "a.json";
        String f2 = "b.json";

        String json1 = """
            {
              "rowSequences": [[1]],
              "columnSequences": [[1]],
              "filename": "a",
              "source": "logi",
              "year": "2024",
              "month": "07",
              "difficulty": 2.0,
              "height": 5,
              "width": 5
            }
            """;
        String json2 = """
            {
              "rowSequences": [[2]],
              "columnSequences": [[2]],
              "filename": "b",
              "source": "logi",
              "year": "2024",
              "month": "07",
              "difficulty": 2.5,
              "height": 5,
              "width": 5
            }
            """;

        Files.writeString(tempDir.resolve(f1), json1);
        Files.writeString(tempDir.resolve(f2), json2);

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(f1, f2));
        when(repository.existsNonogramByGivenParamsFromFile(anyString(), anyString(), anyString(), anyString(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        List<List<String>> combos = getSourceMonthCombinations();
        assertThat(combos).containsExactly(List.of("logi", "07"));
    }

    @Test
    @DisplayName("Incorrect property order is detected and file is added to filesToCorrect")
    void analyzeFlagsIncorrectPropertyOrder() throws Exception {
        // given
        String filename = "wrongOrder.json";
        List<String> lines = List.of(
                "{",
                "  \"filename\": \"wrongOrder\",",
                "  \"source\": \"s\",",
                "  \"year\": \"2024\",",
                "  \"month\": \"08\",",
                "  \"difficulty\": 1.0,",
                "  \"width\": 5,",
                "  \"height\": 5,",
                "  \"rowSequences\": [[1]],",
                "  \"columnSequences\": [[1]]",
                "}",                        // 11 lines
                "", "", "", ""              // and 4 empty (needed count)
        );
        Files.writeString(tempDir.resolve(filename), String.join("\n", lines));

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));
        when(repository.existsNonogramByGivenParamsFromFile(
                "wrongOrder", "s", "2024", "08", 1.0, 5, 5))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        assertThat(getFilesToCorrect()).contains("wrongOrder");
    }

    @SuppressWarnings("unchecked")
    private List<List<String>> getSourceMonthCombinations() throws Exception {
        Field f = NonogramsDataInitializer.class.getDeclaredField("sourceMonthCombinations");
        f.setAccessible(true);
        return (List<List<String>>) f.get(null);
    }

    // small helper to peek filesToCorrect static list
    @SuppressWarnings("unchecked")
    private List<String> getFilesToCorrect() throws Exception {
        Field f = NonogramsDataInitializer.class.getDeclaredField("filesToCorrect");
        f.setAccessible(true);
        return (List<String>) f.get(null);
    }
}
