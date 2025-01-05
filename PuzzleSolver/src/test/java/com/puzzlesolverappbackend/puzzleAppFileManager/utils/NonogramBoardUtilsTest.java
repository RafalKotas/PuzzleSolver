package com.puzzlesolverappbackend.puzzleAppFileManager.utils;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramRowLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.NonogramBoardUtils.groupConsecutiveIndices;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NonogramBoardUtilsTest {

    NonogramRowLogic nonogramRowLogic;

    @BeforeEach
    void setUp() {
        nonogramRowLogic = generate_nonogram_o07940_logic();
        nonogramRowLogic.setNonogramSolutionBoard(List.of(
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                List.of("-", "-", "O", "O", "-", // 0  1  2  3  4
                        "-", "-", "O", "-", "-",          //  5  6  7  8  9
                        "-", "-", "-", "-", "-",          // 10 11 12 13 14
                        "-", "O", "O", "-", "X"),         // 15 16 17 18 19
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-")
        ));
    }

    @Test
    void shouldFindColouredFieldsIndexesInSimulated_o07940_17th_row() {
        // given
        int ROW_TO_TEST_INDEX = 17;

        // when
        List<Integer> boardFieldsWithX = NonogramBoardUtils.findColouredFieldsIndexesInRow(
                nonogramRowLogic.getNonogramSolutionBoard(), ROW_TO_TEST_INDEX);

        // then
        assertThat(boardFieldsWithX).hasSize(5)
                .isEqualTo(List.of(2, 3, 7, 16, 17));
    }

    NonogramRowLogic generate_nonogram_o07940_logic() {
        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic();
        nonogramRowLogic.setColumnsSequences(
                List.of(
                        List.of(3, 2, 1, 2),
                        List.of(6, 9),
                        List.of(6, 7),
                        List.of(2, 2, 1, 2),
                        List.of(4, 10),
                        List.of(3, 2, 2),
                        List.of(5),
                        List.of(3),
                        List.of(2, 1),
                        List.of(6, 3),
                        List.of(3, 1, 2),
                        List.of(5, 1, 1, 1),
                        List.of(8, 2, 3),
                        List.of(8, 2, 1),
                        List.of(6, 2, 1),
                        List.of(5, 3, 2, 2),
                        List.of(5, 2, 3, 4),
                        List.of(4, 1, 2, 3),
                        List.of(3, 1, 4, 2),
                        List.of(2, 4)
                )
        );
        return nonogramRowLogic;
    }

    @ParameterizedTest
    @MethodSource("provideIndicesToGroup")
    void isBlank_ShouldReturnTrueForNullOrBlankStrings(List<Integer> indices, List<List<Integer>> rangesExpected) {
        // when
        List<List<Integer>> resultRanges = groupConsecutiveIndices(indices);

        // then
        assertEquals(rangesExpected, resultRanges);
    }

    private static Stream<Arguments> provideIndicesToGroup() {
        return Stream.of(
                Arguments.of(List.of(2, 3, 7, 16, 17, 18), List.of(List.of(2, 3), List.of(7, 7), List.of(16, 18)))
        );
    }
}