package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SudokuDataInitializerTest {

    @Mock
    private SudokuRepository repository;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private SudokuDataInitializer initializer;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldNotSaveSudokuWhenAlreadyExists() throws Exception {
        // given
        String filename = "sudoku2.json";
        String content = """
            {
              "filename": "sudoku2",
              "source": "sourceB",
              "year": "2023",
              "month": "06",
              "difficulty": 2.0,
              "filled": 25
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = SudokuDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString())).thenReturn(Set.of(filename));
        when(repository.existsSudokuByGivenParamsFromFile(anyString(), anyString(), anyString(), anyString(), anyInt(), anyDouble()))
                .thenReturn(Optional.of(new Sudoku()));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Sudoku.class));
    }

    @Test
    void shouldSaveNewSudokuWhenNotPresentInRepository() throws Exception {
        // given
        String filename = "sudoku1.json";
        String content = """
            {
              "filename": "sudoku1",
              "source": "sourceA",
              "year": "2024",
              "month": "07",
              "difficulty": 1.5,
              "filled": 30
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = SudokuDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString())).thenReturn(Set.of(filename));
        when(repository.existsSudokuByGivenParamsFromFile(anyString(), anyString(), anyString(), anyString(), anyInt(), anyDouble()))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        verify(repository).save(any(Sudoku.class));
        assertThat(initializer.getSudokuRepository()).isNotNull();
        assertThat(initializer.getCommonService()).isNotNull();
    }

    @Test
    void shouldLogErrorWhenJsonIsMalformed() throws Exception {
        // given
        String filename = "malformed.json";
        String malformedJson = """
            {
              "filename": "sudoku3",
              "source": "sourceC",
              "year": "2022",
              "month": "05",
              "difficulty": 3.0,
              "filled": 20,
    """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, malformedJson);

        initializer.setPuzzlePath(tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString())).thenReturn(Set.of(filename));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Sudoku.class));
    }
}
