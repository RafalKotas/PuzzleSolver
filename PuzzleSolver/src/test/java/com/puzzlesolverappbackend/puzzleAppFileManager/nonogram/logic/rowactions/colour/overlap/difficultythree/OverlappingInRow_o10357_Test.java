package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o10357_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(7, 11), List.of(10, 14), List.of(13, 19)),
                        List.of(6, 2, 2, 4),
                        List.of("-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(16),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 13), List.of(12, 15), List.of(14, 17), List.of(16, 19)),
                        List.of(11, 1, 1, 1),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 18 #1",
                        List.of("X", "-", "-", "X", "-", "-", "X", "X", "X", "X", "-", "-", "-", "X", "-", "-", "X", "X", "-", "X"),
                        List.of(List.of(1, 5), List.of(4, 9), List.of(10, 12), List.of(13, 15)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "-", "-", "X", "-", "-", "X", "X", "X", "X", "-", "O", "-", "X", "O", "-", "X", "X", "-", "X")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 18 #2",
                        List.of("X", "-", "-", "X", "-", "-", "X", "X", "X", "X", "-", "O", "-", "X", "O", "O", "X", "X", "X", "X"),
                        List.of(List.of(1, 5), List.of(4, 5), List.of(10, 12), List.of(14, 15)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "-", "-", "X", "O", "O", "X", "X", "X", "X", "-", "O", "-", "X", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 18 #3",
                        List.of("X", "-", "-", "X", "O", "O", "X", "X", "X", "X", "-", "O", "-", "X", "O", "O", "X", "X", "X", "X"),
                        List.of(List.of(1, 2), List.of(4, 5), List.of(10, 12), List.of(14, 15)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "O", "O", "X", "O", "O", "X", "X", "X", "X", "-", "O", "-", "X", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 15)),
                        List.of(10),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 19 #1",
                        List.of("-", "X", "O", "-", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "X", "-"),
                        List.of(List.of(2, 4), List.of(12, 16)),
                        List.of(3, 3),
                        List.of("-", "X", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 3 #1",
                        List.of("-", "-", "O", "O", "O", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(6, 15), List.of(10, 17), List.of(12, 19)),
                        List.of(5, 3, 1, 1),
                        List.of("O", "O", "O", "O", "O", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 16 #1",
                        List.of("-", "O", "-", "X", "-", "O", "-", "X", "X", "X", "O", "O", "-", "X", "-", "-", "-", "X", "O", "X"),
                        List.of(List.of(0, 5), List.of(4, 6), List.of(10, 12), List.of(11, 16), List.of(18, 18)),
                        List.of(2, 2, 3, 2, 1),
                        List.of("-", "O", "-", "X", "-", "O", "-", "X", "X", "X", "O", "O", "O", "X", "-", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 0 #1",
                        List.of("-", "-", "-", "-", "-", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-"),
                        List.of(List.of(2, 10)),
                        List.of(6),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 15 #1",
                        List.of("X", "X", "-", "-", "-", "O", "O", "-", "-", "O", "-", "X", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(2, 9), List.of(8, 10), List.of(12, 16), List.of(17, 19)),
                        List.of(5, 2, 3, 2),
                        List.of("X", "X", "-", "-", "-", "O", "O", "-", "-", "O", "-", "X", "-", "-", "O", "-", "-", "-", "O", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 8 #1",
                        List.of("-", "-", "-", "X", "X", "O", "O", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(5, 12), List.of(9, 15), List.of(12, 19)),
                        List.of(3, 2, 2, 3),
                        List.of("O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 16 #2",
                        List.of("X", "O", "O", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "X", "-", "-", "-", "X", "O", "X"),
                        List.of(List.of(1, 2), List.of(4, 5), List.of(10, 12), List.of(14, 16), List.of(18, 18)),
                        List.of(2, 2, 3, 2, 1),
                        List.of("X", "O", "O", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "X", "-", "O", "-", "X", "O", "X")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 0 #2",
                        List.of("X", "X", "X", "-", "-", "O", "O", "O", "-", "X", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(3, 8)),
                        List.of(6),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 2 #1",
                        List.of("X", "O", "-", "-", "X", "O", "O", "O", "O", "O", "X", "-", "O", "-", "-", "-", "X", "-", "-", "-"),
                        List.of(List.of(1, 3), List.of(5, 9), List.of(11, 19)),
                        List.of(3, 5, 2),
                        List.of("X", "O", "O", "O", "X", "O", "O", "O", "O", "O", "X", "-", "O", "-", "-", "-", "X", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 6 #2",
                        List.of("O", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 15)),
                        List.of(16),
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 10 #2",
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(3, 13), List.of(12, 15), List.of(14, 17), List.of(16, 19)),
                        List.of(11, 1, 1, 1),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 12 #1",
                        List.of("X", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 9), List.of(11, 19)),
                        List.of(4, 5),
                        List.of("X", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 18 #4",
                        List.of("X", "O", "O", "X", "O", "O", "X", "X", "X", "X", "X", "O", "-", "X", "O", "O", "X", "X", "X", "X"),
                        List.of(List.of(1, 2), List.of(4, 5), List.of(11, 12), List.of(14, 15)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "O", "O", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 10 #3",
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "O", "-", "-"),
                        List.of(List.of(3, 13), List.of(15, 15), List.of(17, 17), List.of(19, 19)),
                        List.of(11, 1, 1, 1),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "-", "O", "-", "O")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 13 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(7, 8), List.of(16, 19)),
                        List.of(2, 3),
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 13 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-", "X", "X", "-", "O", "O", "X"),
                        List.of(List.of(7, 8), List.of(16, 18)),
                        List.of(2, 3),
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-", "X", "X", "O", "O", "O", "X")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 4 #2",
                        List.of("O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "O", "O", "X", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(7, 8), List.of(11, 12), List.of(14, 19)),
                        List.of(6, 2, 2, 4),
                        List.of("O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "O", "O", "X", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o10357 / 20x20 / diff 3.0 / Row 4 #3",
                        List.of("O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O", "O", "-"),
                        List.of(List.of(0, 5), List.of(7, 8), List.of(11, 12), List.of(16, 19)),
                        List.of(6, 2, 2, 4),
                        List.of("O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O")
                )
        );
    }
}
