package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.extend.toright;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class ExtendColouredFieldsToRightRowDiff1Test
        extends ExtendColouredFieldsToRightRowTestBase
        implements ExtendColouredFieldsToRightRowTestExecutor {

    static Stream<Arguments> provideTestCasesForExtendRight() {
        return Stream.of(
                Arguments.of(
                        "o07836 / 10x10 / diff 1.0 / Row 2",
                        List.of("-", "-", "X", "O", "-", "-", "O", "O", "X", "-"),
                        List.of(List.of(3, 7)),
                        List.of(5),
                        List.of("-", "-", "X", "O", "O", "O", "O", "O", "X", "-")
                ),
                Arguments.of(
                        "o07836 / 10x10 / diff 1.0 / Row 1",
                        List.of("-", "-", "X", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(3, 9)),
                        List.of(2, 3),
                        List.of("-", "-", "X", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07836 / 10x10 / diff 1.0 / Row 5",
                        List.of("X", "O", "O", "O", "-", "X", "-", "-", "X", "O"),
                        List.of(List.of(0, 4), List.of(9, 9)),
                        List.of(4, 1),
                        List.of("X", "O", "O", "O", "O", "X", "-", "-", "X", "O")
                ),
                Arguments.of(
                        "o07836 / 10x10 / diff 1.0 / Row 0",
                        List.of("X", "X", "X", "X", "-", "X", "X", "O", "-", "-"),
                        List.of(List.of(5, 9)),
                        List.of(3),
                        List.of("X", "X", "X", "X", "-", "X", "X", "O", "O", "O")
                ),
                Arguments.of(
                        "o06041 / 10x15 / diff 1.0 / Row 4",
                        List.of("-", "-", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "-", "X", "O"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(6, 12), List.of(14, 14)),
                        List.of(1, 1, 7, 1),
                        List.of("-", "-", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O")
                ),
                Arguments.of(
                        "o06041 / 10x15 / diff 1.0 / Row 3",
                        List.of("-", "O", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "-", "X", "O"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(6, 12), List.of(14, 14)),
                        List.of(2, 1, 7, 1),
                        List.of("-", "O", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O")
                ),
                Arguments.of(
                        "o06041 / 10x15 / diff 1.0 / Row 9",
                        List.of("-", "-", "-", "-", "X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(5, 14)),
                        List.of(9),
                        List.of("-", "-", "-", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o07986 / 10x15 / diff 1.0 / Row 4",
                        List.of("-", "-", "-", "-", "-", "X", "-", "O", "-", "O", "-", "O", "-", "O", "-"),
                        List.of(List.of(0, 5), List.of(6, 9), List.of(11, 11), List.of(13, 14)),
                        List.of(2, 4, 1, 1),
                        List.of("-", "-", "-", "-", "-", "X", "-", "O", "O", "O", "-", "O", "-", "O", "-")
                ),
                Arguments.of(
                        "o07986 / 10x15 / diff 1.0 / Row 5",
                        List.of("-", "-", "X", "X", "-", "O", "-", "O", "-", "O", "-", "O", "-", "O", "-"),
                        List.of(List.of(0, 3), List.of(4, 7), List.of(9, 9), List.of(11, 11), List.of(13, 13)),
                        List.of(1, 4, 1, 1, 1),
                        List.of("-", "-", "X", "X", "-", "O", "O", "O", "-", "O", "-", "O", "-", "O", "-")
                ),
                Arguments.of(
                        "o01131 / 10x15 / diff 1.0 / Row 7",
                        List.of("-", "-", "O", "O", "O", "X", "O", "-", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(5, 9), List.of(10, 14)),
                        List.of(3, 4, 2),
                        List.of("-", "-", "O", "O", "O", "X", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o01131 / 10x15 / diff 1.0 / Row 2",
                        List.of("-", "X", "O", "X", "O", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O"),
                        List.of(List.of(2, 2), List.of(4, 5), List.of(13, 14)),
                        List.of(1, 2, 2),
                        List.of("-", "X", "O", "X", "O", "O", "-", "-", "-", "X", "-", "-", "-", "-", "O")
                ),
                Arguments.of(
                        "o01131 / 10x15 / diff 1.0 / Row 1",
                        List.of("X", "O", "-", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X"),
                        List.of(List.of(0, 2), List.of(5, 6), List.of(12, 13)),
                        List.of(2, 2, 2),
                        List.of("X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X")
                ),
                Arguments.of(
                        "o01131 / 10x15 / diff 1.0 / Row 9",
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(2, 14)),
                        List.of(12),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07387 / 10x15 / diff 1.0 / Row 6",
                        List.of("-", "-", "X", "X", "O", "-", "-", "X", "X", "X", "X", "X", "X", "X", "-"),
                        List.of(List.of(0, 6), List.of(4, 6)),
                        List.of(2, 2),
                        List.of("-", "-", "X", "X", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "-")
                ),
                Arguments.of(
                        "o05024 / 15x10 / diff 1.0 / Row 14",
                        List.of("-", "-", "X", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(1, 8)),
                        List.of(6),
                        List.of("-", "-", "X", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o06044 / 10x15 / diff 1.0 / Row 4",
                        List.of("X", "X", "X", "O", "O", "O", "O", "-", "-", "O", "X", "X", "X", "X", "X"),
                        List.of(List.of(3, 9)),
                        List.of(7),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o06044 / 10x15 / diff 1.0 / Row 8",
                        List.of("O", "-", "O", "-", "-", "O", "X", "X", "X", "X", "O", "-", "O", "-", "-"),
                        List.of(List.of(0, 2), List.of(5, 5), List.of(10, 12)),
                        List.of(3, 1, 3),
                        List.of("O", "-", "O", "-", "-", "O", "X", "X", "X", "X", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o06253 / 15x10 / diff 1.0 / Row 14",
                        List.of("-", "-", "X", "X", "O", "-", "X", "-", "-", "X"),
                        List.of(List.of(0, 5), List.of(3, 9)),
                        List.of(2, 2),
                        List.of("-", "-", "X", "X", "O", "O", "X", "-", "-", "X")
                )
        );
    }
}
