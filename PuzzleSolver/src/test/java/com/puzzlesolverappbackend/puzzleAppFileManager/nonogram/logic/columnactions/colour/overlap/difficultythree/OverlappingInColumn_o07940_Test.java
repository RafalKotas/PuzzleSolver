package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07940_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(4, 8), List.of(6, 19)),
                        List.of(3, 1, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(3, 9), List.of(5, 19)),
                        List.of(2, 1, 9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(6, 8), List.of(9, 10), List.of(11, 13), List.of(14, 19)),
                        List.of(5, 2, 1, 2, 5),
                        List.of("-", "O", "O", "O", "O", "-", "-", "O", "-", "-", "-", "-", "O", "-", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 17 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(4, 7), List.of(6, 12), List.of(11, 14), List.of(13, 17), List.of(16, 19)),
                        List.of(3, 1, 4, 1, 2, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 18 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(7, 19)),
                        List.of(6, 8),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 4 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(9, 19)),
                        List.of(3, 8),
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 5 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 12), List.of(7, 19)),
                        List.of(3, 2, 6),
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 13 #1",
                        List.of("-", "O", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(6, 16), List.of(9, 19)),
                        List.of(5, 2, 2),
                        List.of("-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 14 #1",
                        List.of("-", "O", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(7, 17), List.of(10, 19)),
                        List.of(6, 2, 1),
                        List.of("-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 3 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(3, 9), List.of(8, 19)),
                        List.of(2, 1, 9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 17 #2",
                        List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "O", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 5), List.of(6, 10), List.of(11, 12), List.of(15, 17), List.of(18, 19)),
                        List.of(3, 1, 4, 1, 2, 1),
                        List.of("-", "O", "O", "-", "-", "-", "-", "O", "O", "O", "-", "-", "O", "X", "X", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 18 #2",
                        List.of("-", "-", "O", "-", "-", "O", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(7, 19)),
                        List.of(6, 8),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 2 #2",
                        List.of("-", "O", "O", "X", "O", "X", "-", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(8, 19)),
                        List.of(3, 1, 10),
                        List.of("O", "O", "O", "X", "O", "X", "-", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 16 #2",
                        List.of("-", "O", "O", "O", "O", "X", "-", "O", "-", "-", "-", "-", "O", "-", "-", "O", "O", "O", "O", "-"),
                        List.of(List.of(0, 4), List.of(6, 8), List.of(9, 10), List.of(11, 13), List.of(14, 19)),
                        List.of(5, 2, 1, 2, 5),
                        List.of("O", "O", "O", "O", "O", "X", "-", "O", "-", "-", "-", "-", "O", "-", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 5 #2",
                        List.of("-", "O", "O", "X", "X", "-", "-", "X", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 2), List.of(5, 10), List.of(9, 17)),
                        List.of(3, 2, 6),
                        List.of("O", "O", "O", "X", "X", "-", "-", "X", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 5 #3",
                        List.of("O", "O", "O", "X", "X", "X", "X", "X", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 2), List.of(8, 10), List.of(9, 17)),
                        List.of(3, 2, 6),
                        List.of("O", "O", "O", "X", "X", "X", "X", "X", "-", "O", "-", "-", "O", "O", "O", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 5 #4",
                        List.of("O", "O", "O", "X", "X", "X", "X", "X", "-", "O", "-", "-", "O", "O", "O", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 2), List.of(8, 10), List.of(11, 17)),
                        List.of(3, 2, 6),
                        List.of("O", "O", "O", "X", "X", "X", "X", "X", "-", "O", "-", "-", "O", "O", "O", "O", "O", "-", "X", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 19 #1",
                        List.of("X", "-", "O", "O", "-", "O", "-", "X", "-", "-", "-", "-", "X", "X", "X", "O", "-", "X", "-", "-"),
                        List.of(List.of(1, 5), List.of(5, 16), List.of(8, 19)),
                        List.of(4, 2, 1),
                        List.of("X", "-", "O", "O", "O", "O", "-", "X", "-", "-", "-", "-", "X", "X", "X", "O", "-", "X", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 15 #1",
                        List.of("X", "O", "O", "X", "O", "X", "O", "O", "X", "-", "-", "-", "X", "-", "-", "O", "-", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(4, 4), List.of(6, 7), List.of(9, 13), List.of(14, 16), List.of(17, 19)),
                        List.of(2, 1, 2, 1, 2, 2),
                        List.of("X", "O", "O", "X", "O", "X", "O", "O", "X", "-", "-", "-", "X", "-", "-", "O", "-", "-", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 17 #3",
                        List.of("O", "O", "O", "X", "O", "X", "O", "O", "O", "O", "X", "X", "O", "X", "X", "X", "O", "O", "X", "-"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(6, 9), List.of(12, 12), List.of(16, 17), List.of(19, 19)),
                        List.of(3, 1, 4, 1, 2, 1),
                        List.of("O", "O", "O", "X", "O", "X", "O", "O", "O", "O", "X", "X", "O", "X", "X", "X", "O", "O", "X", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 7 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "O", "O", "-", "X", "-", "-", "-", "-"),
                        List.of(List.of(8, 10), List.of(11, 14), List.of(16, 19)),
                        List.of(1, 3, 3),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "O", "O", "-", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 1 #1",
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(8, 13), List.of(16, 19)),
                        List.of(5, 2, 4),
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "-", "-", "-", "-", "-", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 8 #1",
                        List.of("X", "X", "X", "X", "X", "O", "X", "X", "X", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(5, 5), List.of(8, 9), List.of(10, 13), List.of(15, 19)),
                        List.of(1, 1, 3, 2),
                        List.of("X", "X", "X", "X", "X", "O", "X", "X", "X", "-", "-", "O", "O", "-", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 2 #3",
                        List.of("O", "O", "O", "X", "O", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(9, 19)),
                        List.of(3, 1, 10),
                        List.of("O", "O", "O", "X", "O", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 8 #2",
                        List.of("X", "X", "X", "X", "X", "O", "X", "X", "X", "-", "-", "O", "O", "-", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(5, 5), List.of(9, 9), List.of(10, 13), List.of(15, 19)),
                        List.of(1, 1, 3, 2),
                        List.of("X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "-", "O", "O", "-", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 8 #3",
                        List.of("X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "-", "O", "O", "-", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(5, 5), List.of(9, 9), List.of(11, 13), List.of(15, 19)),
                        List.of(1, 1, 3, 2),
                        List.of("X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "-", "O", "O", "O", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 3 #3",
                        List.of("X", "O", "O", "X", "O", "X", "X", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "X"),
                        List.of(List.of(1, 2), List.of(4, 4), List.of(9, 18)),
                        List.of(2, 1, 9),
                        List.of("X", "O", "O", "X", "O", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 2 #4",
                        List.of("O", "O", "O", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(10, 19)),
                        List.of(3, 1, 10),
                        List.of("O", "O", "O", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 12 #1",
                        List.of("X", "O", "X", "X", "O", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X"),
                        List.of(List.of(1, 1), List.of(4, 4), List.of(10, 14), List.of(15, 17)),
                        List.of(1, 1, 3, 2),
                        List.of("X", "O", "X", "X", "O", "X", "X", "X", "X", "X", "-", "-", "O", "-", "-", "-", "O", "-", "-", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 0 #1",
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "-", "X", "X", "X", "X", "X", "-", "-", "X"),
                        List.of(List.of(0, 4), List.of(17, 18)),
                        List.of(5, 2),
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "-", "X", "X", "X", "X", "X", "O", "O", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 18 #3",
                        List.of("X", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "O", "O", "O", "O", "O", "-", "O", "O"),
                        List.of(List.of(1, 6), List.of(12, 19)),
                        List.of(6, 8),
                        List.of("X", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 1 #2",
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "-", "-", "X", "X", "O", "O", "O", "O"),
                        List.of(List.of(0, 4), List.of(12, 13), List.of(16, 19)),
                        List.of(5, 2, 4),
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Column 11 #1",
                        List.of("X", "O", "X", "X", "O", "X", "X", "X", "X", "X", "-", "X", "X", "X", "X", "X", "O", "X", "X", "X"),
                        List.of(List.of(1, 1), List.of(4, 4), List.of(10, 10), List.of(16, 16)),
                        List.of(1, 1, 1, 1),
                        List.of("X", "O", "X", "X", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X")
                )
        );
    }
}