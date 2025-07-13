package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SudokuControllerTest {

    @InjectMocks
    private SudokuController sudokuController;

    @Mock
    private CommonService commonService;

    @TempDir
    Path tempDir;

    private static final String EXISTING_FILENAME = "duplicate";

    @BeforeEach
    void setup() throws IOException {
        Path existingFile = tempDir.resolve(EXISTING_FILENAME + ".json");
        Files.createFile(existingFile);

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(existingFile.getFileName().toString()));

        sudokuController.directory = tempDir.toString() + "/";
    }

    @Test
    void shouldSaveNewSudokuSuccessfully() throws IOException {
        // given
        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of());

        SudokuFileDetails validDetails = new SudokuFileDetails("source", 2.0, 50, "2020", "10",
                List.of(
                        List.of(1, 2, 3),
                        List.of(4, 5, 6),
                        List.of(7, 8, 9)
                ));

        // when
        ResponseEntity<String> response = sudokuController.saveSudokuToJsonFile(
                "unique_filename", validDetails
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Save success!", response.getBody());
    }

    @Test
    void shouldReturnConflictIfFilenameExists() throws IOException {
        // given
        SudokuFileDetails anyDetails = new SudokuFileDetails();

        // when
        ResponseEntity<String> response = sudokuController.saveSudokuToJsonFile(
                EXISTING_FILENAME, anyDetails
        );

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Save failed. File with the same name already exists.", response.getBody());
    }

    @Test
    void shouldThrowIOExceptionWhenWritingFails() {
        // given
        sudokuController.directory = "/this/path/does/not/exist/";

        SudokuFileDetails details = new SudokuFileDetails("fail_write", 1.0, 40, "2010", "01",
                List.of(List.of(1, 2), List.of(3, 4)));

        // when + then
        assertThrows(IOException.class, () -> {
            sudokuController.saveSudokuToJsonFile("fail_write", details);
        });
    }
}
