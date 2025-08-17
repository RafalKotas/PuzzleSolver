package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.saver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramSolverUtils;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.FinalNonogramSolutionDTO;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolutionSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

class NonogramSolutionSaverTest {

    @TempDir
    Path tempDir;

    private NonogramSolutionSaver buildSaver(Path dir) {
        NonogramSolutionSaver saver = new NonogramSolutionSaver();
        try {
            Field field = NonogramSolutionSaver.class.getDeclaredField("solutionDir");
            field.setAccessible(true);
            field.set(saver, dir.toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to inject solutionDir", e);
        }
        return saver;
    }

    @Test
    @DisplayName("Should save solution if board is correct and return populated DTO")
    void shouldSaveSolutionIfCorrect() throws IOException {
        // given
        List<List<String>> board = List.of(
                List.of("X", "X", "O", "X", "O", "X", "O", "X", "X", "X"),
                List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("X", "O", "X", "X", "X", "X", "O", "X", "X", "X"),
                List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
        );

        List<List<Integer>> rowSequences = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1, 1, 1),
                List.of(1, 5, 1),
                List.of(9),
                List.of(9),
                List.of(7, 1),
                List.of(9),
                List.of(10),
                List.of(1, 1),
                List.of(2, 2)
        );

        List<List<Integer>> columnSequences = List.of(
                List.of(7),
                List.of(7),
                List.of(8, 1),
                List.of(6),
                List.of(8),
                List.of(6),
                List.of(10),
                List.of(2, 2, 1),
                List.of(7),
                List.of(1)
        );

        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setFileName("o06005_ok"); // unikalna nazwa dla tego testu
        request.setBoard(board);
        request.setRowSequences(rowSequences);
        request.setColumnSequences(columnSequences);

        NonogramSolutionSaver saver = buildSaver(tempDir);

        List<List<List<Integer>>> derivedRowRanges = List.of(
                List.of(List.of(2, 2), List.of(4, 4), List.of(6, 6)),
                List.of(List.of(0, 0), List.of(2, 2), List.of(4, 4), List.of(6, 6), List.of(8, 8)),
                List.of(List.of(0, 0), List.of(2, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 9)),
                List.of(List.of(1, 1), List.of(6, 6)),
                List.of(List.of(1, 2), List.of(6, 7))
        );

        List<List<List<Integer>>> derivedColumnRanges = List.of(
                List.of(List.of(1, 7)),
                List.of(List.of(3, 9)),
                List.of(List.of(0, 7), List.of(9, 9)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 7)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 9)),
                List.of(List.of(3, 4), List.of(6, 7), List.of(9, 9)),
                List.of(List.of(1, 7)),
                List.of(List.of(7, 7))
        );

        try (MockedStatic<NonogramSolverUtils> utilsMock = mockStatic(NonogramSolverUtils.class);
             MockedStatic<SolutionJsonFormatter> formatterMock = mockStatic(SolutionJsonFormatter.class)) {

            utilsMock.when(() -> NonogramSolverUtils.isBoardConsistentWithSequences(any(), any(), any()))
                    .thenReturn(true);
            utilsMock.when(() -> NonogramSolverUtils.inferSequenceRangesFromBoard(any()))
                    .thenReturn(derivedRowRanges);
            utilsMock.when(() -> NonogramSolverUtils.inferSequenceRangesFromColumns(any()))
                    .thenReturn(derivedColumnRanges);

            formatterMock.when(() -> SolutionJsonFormatter.format(any()))
                    .thenReturn("{\"mocked\":true}");

            // when
            FinalNonogramSolutionDTO result = saver.saveIfCorrect(request);

            // then
            assertThat(result.getFinalBoard()).isEqualTo(board);
            assertThat(result.getDerivedRowRanges()).isEqualTo(derivedRowRanges);
            assertThat(result.getDerivedColumnRanges()).isEqualTo(derivedColumnRanges);
            assertThat(result.getVerifiedAgainstOriginal()).isEqualTo("PASS");

            Path expectedFile = tempDir.resolve("ro06005_ok.json");
            assertThat(Files.exists(expectedFile)).isTrue();
        }
    }

    @Test
    @DisplayName("Should not save solution if board is not correct and return FAIL DTO")
    void shouldNotSaveSolutionIfNotCorrect() throws IOException {
        // given
        List<List<String>> board = List.of(
                List.of("X", "X", "O", "X", "O", "X", "O", "O", "X", "X"),
                List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("X", "O", "X", "X", "X", "X", "O", "X", "X", "X"),
                List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
        );

        List<List<Integer>> rowSequences = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1, 1, 1),
                List.of(1, 5, 1),
                List.of(9),
                List.of(9),
                List.of(7, 1),
                List.of(9),
                List.of(10),
                List.of(1, 1),
                List.of(2, 2)
        );

        List<List<Integer>> columnSequences = List.of(
                List.of(7),
                List.of(7),
                List.of(8, 1),
                List.of(6),
                List.of(8),
                List.of(6),
                List.of(10),
                List.of(2, 2, 1),
                List.of(7),
                List.of(1)
        );

        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setFileName("o06005_fail"); // inna nazwa
        request.setBoard(board);
        request.setRowSequences(rowSequences);
        request.setColumnSequences(columnSequences);

        NonogramSolutionSaver saver = buildSaver(tempDir);

        List<List<List<Integer>>> derivedRowRanges = List.of(
                List.of(List.of(2, 2), List.of(4, 4), List.of(6, 7)),
                List.of(List.of(0, 0), List.of(2, 2), List.of(4, 4), List.of(6, 6), List.of(8, 8)),
                List.of(List.of(0, 0), List.of(2, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 9)),
                List.of(List.of(1, 1), List.of(6, 6)),
                List.of(List.of(1, 2), List.of(6, 7))
        );

        List<List<List<Integer>>> derivedColumnRanges = List.of(
                List.of(List.of(1, 7)),
                List.of(List.of(3, 9)),
                List.of(List.of(0, 7), List.of(9, 9)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 7)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 9)),
                List.of(List.of(0, 0), List.of(3, 4), List.of(6, 7), List.of(9, 9)),
                List.of(List.of(1, 7)),
                List.of(List.of(7, 7))
        );

        try (MockedStatic<NonogramSolverUtils> utilsMock = mockStatic(NonogramSolverUtils.class);
             MockedStatic<SolutionJsonFormatter> formatterMock = mockStatic(SolutionJsonFormatter.class)) {

            utilsMock.when(() -> NonogramSolverUtils.isBoardConsistentWithSequences(any(), any(), any()))
                    .thenReturn(false);
            utilsMock.when(() -> NonogramSolverUtils.inferSequenceRangesFromBoard(any()))
                    .thenReturn(derivedRowRanges);
            utilsMock.when(() -> NonogramSolverUtils.inferSequenceRangesFromColumns(any()))
                    .thenReturn(derivedColumnRanges);

            formatterMock.when(() -> SolutionJsonFormatter.format(any()))
                    .thenReturn("{\"mocked\":true}");

            // when
            FinalNonogramSolutionDTO result = saver.saveIfCorrect(request);

            // then
            assertThat(result.getFinalBoard()).isEqualTo(board);
            assertThat(result.getDerivedRowRanges()).isNull();
            assertThat(result.getDerivedColumnRanges()).isNull();
            assertThat(result.getVerifiedAgainstOriginal()).isEqualTo("FAIL");

            Path unexpectedFile = tempDir.resolve("ro06005_fail.json");
            assertThat(Files.exists(unexpectedFile)).isFalse();
        }
    }
}
