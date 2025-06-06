package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o11517_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14), List.of(10, 19)),
                        List.of(9, 4),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 10 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(3, 8), List.of(7, 13), List.of(12, 19)),
                        List.of(2, 3, 4, 5),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 12 #1",
                        List.of("-", "-", "-", "X", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 6), List.of(8, 16), List.of(10, 19)),
                        List.of(2, 3, 1, 2),
                        List.of("-", "O", "-", "X", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 7 #1",
                        List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(2, 7), List.of(8, 15), List.of(12, 19)),
                        List.of(1, 5, 3, 3),
                        List.of("-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 11 #1",
                        List.of("X", "O", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 1), List.of(3, 7), List.of(8, 15), List.of(11, 19)),
                        List.of(1, 4, 2, 3),
                        List.of("X", "O", "X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 12 #2",
                        List.of("X", "O", "-", "X", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(4, 6), List.of(8, 16), List.of(10, 19)),
                        List.of(2, 3, 1, 2),
                        List.of("X", "O", "O", "X", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 6 #1",
                        List.of("-", "-", "-", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(2, 4), List.of(6, 14), List.of(10, 19)),
                        List.of(1, 3, 3, 4),
                        List.of("-", "-", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 8 #1",
                        List.of("-", "-", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(6, 13), List.of(11, 16), List.of(14, 19)),
                        List.of(1, 3, 4, 2, 2),
                        List.of("O", "-", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 15 #1",
                        List.of("X", "-", "X", "X", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(4, 6), List.of(5, 11), List.of(7, 17), List.of(11, 19)),
                        List.of(3, 1, 1, 1),
                        List.of("X", "-", "X", "X", "O", "O", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 6 #2",
                        List.of("-", "-", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(6, 14), List.of(10, 19)),
                        List.of(1, 3, 3, 4),
                        List.of("O", "-", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 0 #1",
                        List.of("X", "-", "-", "-", "-", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 10), List.of(7, 19)),
                        List.of(6, 4),
                        List.of("X", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 1 #2",
                        List.of("X", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 13), List.of(10, 19)),
                        List.of(9, 4),
                        List.of("X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 2 #1",
                        List.of("X", "-", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 3), List.of(5, 8), List.of(10, 17), List.of(15, 19)),
                        List.of(2, 4, 4, 1),
                        List.of("X", "-", "O", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 9 #1",
                        List.of("-", "X", "-", "O", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(7, 12), List.of(9, 15), List.of(12, 19)),
                        List.of(1, 3, 2, 2, 3),
                        List.of("O", "X", "O", "O", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 0 #2",
                        List.of("X", "X", "X", "-", "-", "O", "O", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(3, 10), List.of(9, 19)),
                        List.of(6, 4),
                        List.of("X", "X", "X", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 4 #1",
                        List.of("X", "-", "X", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 1), List.of(4, 5), List.of(7, 15), List.of(12, 19)),
                        List.of(1, 2, 4, 3),
                        List.of("X", "O", "X", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 1 #3",
                        List.of("X", "X", "X", "O", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(3, 11), List.of(11, 19)),
                        List.of(9, 4),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 1 #4",
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(3, 11), List.of(13, 19)),
                        List.of(9, 4),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 16 #1",
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(6, 8), List.of(10, 13)),
                        List.of(3, 3),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 18 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "-", "O", "O", "-", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(8, 13)),
                        List.of(5),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 2 #2",
                        List.of("X", "X", "O", "O", "X", "O", "O", "O", "O", "X", "X", "X", "-", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(2, 3), List.of(5, 8), List.of(12, 17), List.of(17, 19)),
                        List.of(2, 4, 4, 1),
                        List.of("X", "X", "O", "O", "X", "O", "O", "O", "O", "X", "X", "X", "-", "-", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 0 #3",
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(5, 10), List.of(13, 19)),
                        List.of(6, 4),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 19 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "O", "-", "O", "X", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(10, 12)),
                        List.of(3),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "O", "O", "O", "X", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 3 #1",
                        List.of("X", "O", "O", "X", "X", "O", "O", "X", "X", "-", "-", "-", "-", "-", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(5, 6), List.of(9, 15), List.of(14, 19)),
                        List.of(2, 2, 4, 2),
                        List.of("X", "O", "O", "X", "X", "O", "O", "X", "X", "-", "-", "-", "O", "-", "O", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 5 #1",
                        List.of("O", "X", "X", "O", "O", "X", "X", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 0), List.of(3, 4), List.of(9, 14), List.of(15, 19)),
                        List.of(1, 2, 4, 4),
                        List.of("O", "X", "X", "O", "O", "X", "X", "-", "-", "-", "-", "O", "O", "-", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 6 #3",
                        List.of("O", "X", "O", "O", "O", "X", "-", "X", "-", "-", "-", "-", "X", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(8, 11), List.of(13, 19)),
                        List.of(1, 3, 3, 4),
                        List.of("O", "X", "O", "O", "O", "X", "-", "X", "-", "O", "O", "-", "X", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 4 #2",
                        List.of("X", "O", "X", "X", "O", "O", "X", "X", "X", "X", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 1), List.of(4, 5), List.of(10, 15), List.of(14, 19)),
                        List.of(1, 2, 4, 3),
                        List.of("X", "O", "X", "X", "O", "O", "X", "X", "X", "X", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 14 #1",
                        List.of("X", "X", "X", "O", "O", "O", "O", "X", "-", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(3, 6), List.of(8, 8), List.of(13, 13)),
                        List.of(4, 1, 1),
                        List.of("X", "X", "X", "O", "O", "O", "O", "X", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 3 #2",
                        List.of("X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "-", "O", "O", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(5, 6), List.of(12, 15), List.of(17, 19)),
                        List.of(2, 2, 4, 2),
                        List.of("X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "-", "O", "O", "O", "O", "X", "-", "O", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 4 #3",
                        List.of("X", "O", "X", "X", "O", "O", "X", "X", "X", "X", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 1), List.of(4, 5), List.of(10, 15), List.of(15, 19)),
                        List.of(1, 2, 4, 3),
                        List.of("X", "O", "X", "X", "O", "O", "X", "X", "X", "X", "-", "-", "O", "O", "-", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 4 #4",
                        List.of("X", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "-", "O", "O", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(1, 1), List.of(4, 5), List.of(11, 15), List.of(15, 19)),
                        List.of(1, 2, 4, 3),
                        List.of("X", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 0 #4",
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(5, 10), List.of(14, 19)),
                        List.of(6, 4),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 1 #5",
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(3, 11), List.of(14, 19)),
                        List.of(9, 4),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 4 #5",
                        List.of("X", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "-", "O", "-", "-"),
                        List.of(List.of(1, 1), List.of(4, 5), List.of(11, 15), List.of(16, 19)),
                        List.of(1, 2, 4, 3),
                        List.of("X", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 7 #2",
                        List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "O", "O", "X", "-", "-", "-", "O", "-", "X", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 6), List.of(8, 10), List.of(12, 16)),
                        List.of(1, 5, 3, 3),
                        List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "O", "O", "X", "-", "-", "O", "O", "-", "X", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 9 #2",
                        List.of("O", "X", "O", "O", "O", "X", "X", "O", "O", "X", "-", "X", "O", "O", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(7, 8), List.of(12, 13), List.of(15, 19)),
                        List.of(1, 3, 2, 2, 3),
                        List.of("O", "X", "O", "O", "O", "X", "X", "O", "O", "X", "-", "X", "O", "O", "X", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 8 #2",
                        List.of("O", "X", "O", "O", "O", "X", "O", "O", "O", "O", "X", "-", "-", "O", "-", "-", "X", "X", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(6, 9), List.of(12, 14), List.of(18, 19)),
                        List.of(1, 3, 4, 2, 2),
                        List.of("O", "X", "O", "O", "O", "X", "O", "O", "O", "O", "X", "-", "-", "O", "-", "-", "X", "X", "O", "O")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Column 12 #3",
                        List.of("X", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "-", "X", "O", "O", "X", "X", "X", "-", "-"),
                        List.of(List.of(1, 2), List.of(4, 6), List.of(11, 11), List.of(13, 14)),
                        List.of(2, 3, 1, 2),
                        List.of("X", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "O", "X", "O", "O", "X", "X", "X", "-", "-")
                )
        );
    }
}