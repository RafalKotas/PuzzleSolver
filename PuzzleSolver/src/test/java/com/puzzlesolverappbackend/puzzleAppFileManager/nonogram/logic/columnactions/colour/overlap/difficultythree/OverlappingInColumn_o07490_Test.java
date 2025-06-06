package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07490_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 13), List.of(9, 15), List.of(11, 17), List.of(13, 19)),
                        List.of(8, 1, 1, 1),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 17), List.of(11, 19)),
                        List.of(10, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(3, 10), List.of(6, 13), List.of(12, 19)),
                        List.of(2, 2, 2, 5),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(9, 19)),
                        List.of(7),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-"),
                        List.of(List.of(9, 19)),
                        List.of(7),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 1 #2",
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 12), List.of(9, 15), List.of(11, 17), List.of(14, 19)),
                        List.of(8, 1, 1, 1),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 15 #1",
                        List.of("-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 19)),
                        List.of(10),
                        List.of("-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 1 #3",
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "X", "X", "-", "-", "X", "-", "-", "-"),
                        List.of(List.of(0, 11), List.of(9, 15), List.of(11, 17), List.of(14, 19)),
                        List.of(8, 1, 1, 1),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "X", "X", "-", "-", "X", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(4, 11), List.of(14, 19)),
                        List.of(3, 3, 5),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 15 #2",
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "O", "-", "-", "-", "-"),
                        List.of(List.of(6, 19)),
                        List.of(10),
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 16 #1",
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(11, 19)),
                        List.of(9),
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 15 #3",
                        List.of("X", "X", "X", "X", "X", "X", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(10, 19)),
                        List.of(10),
                        List.of("X", "X", "X", "X", "X", "X", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 17 #1",
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(10, 19)),
                        List.of(8),
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "X", "X", "-", "-", "X", "X", "X", "X", "-"),
                        List.of(List.of(0, 10), List.of(8, 10)),
                        List.of(3, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "X", "X", "-", "-", "X", "X", "X", "X", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 11 #2",
                        List.of("X", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "X", "-", "-", "-", "-", "X", "X", "-"),
                        List.of(List.of(2, 11), List.of(13, 19)),
                        List.of(10, 1),
                        List.of("X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "X", "X", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 12 #1",
                        List.of("X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "O", "O", "-", "-", "-", "X", "X"),
                        List.of(List.of(1, 11), List.of(9, 17)),
                        List.of(5, 6),
                        List.of("X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 13 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "-", "O", "O", "O", "O", "-", "X", "-"),
                        List.of(List.of(10, 17)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "-", "X", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 18 #1",
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "-", "O", "O"),
                        List.of(List.of(13, 19)),
                        List.of(7),
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 19 #1",
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "-", "O", "O"),
                        List.of(List.of(14, 19)),
                        List.of(6),
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "-", "-", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 12 #2",
                        List.of("X", "-", "-", "-", "-", "-", "-", "O", "X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "X", "X"),
                        List.of(List.of(1, 8), List.of(9, 15)),
                        List.of(5, 6),
                        List.of("X", "-", "-", "-", "O", "O", "-", "O", "X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 12 #3",
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "X", "-", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of(List.of(3, 7), List.of(9, 14)),
                        List.of(5, 6),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 9 #1",
                        List.of("-", "-", "O", "X", "X", "-", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(1, 2), List.of(5, 7), List.of(9, 10)),
                        List.of(2, 1, 2),
                        List.of("-", "O", "O", "X", "X", "-", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 2 #2",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "X", "X", "-", "O", "O", "O", "O", "-"),
                        List.of(List.of(0, 3), List.of(8, 11), List.of(14, 19)),
                        List.of(3, 3, 5),
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "O", "-", "X", "X", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 6 #2",
                        List.of("O", "O", "X", "-", "X", "-", "-", "-", "-", "X", "O", "-", "X", "X", "-", "O", "O", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(5, 8), List.of(10, 11), List.of(14, 19)),
                        List.of(2, 2, 2, 5),
                        List.of("O", "O", "X", "-", "X", "-", "-", "-", "-", "X", "O", "O", "X", "X", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 5 #1",
                        List.of("O", "O", "X", "-", "X", "-", "X", "-", "-", "X", "O", "O", "X", "X", "-", "O", "O", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(7, 8), List.of(10, 11), List.of(14, 19)),
                        List.of(2, 2, 2, 5),
                        List.of("O", "O", "X", "-", "X", "-", "X", "O", "O", "X", "O", "O", "X", "X", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 1 #4",
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "O", "X", "-", "-", "-"),
                        List.of(List.of(2, 9), List.of(15, 15), List.of(17, 17), List.of(19, 19)),
                        List.of(8, 1, 1, 1),
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "O", "X", "O", "-", "O")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Column 3 #1",
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "-"),
                        List.of(List.of(1, 2), List.of(9, 10), List.of(15, 19)),
                        List.of(2, 2, 5),
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O")
                )
        );
    }
}