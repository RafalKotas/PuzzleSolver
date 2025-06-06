package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07571_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(2, 12), List.of(9, 15), List.of(12, 17), List.of(14, 19)),
                        List.of(1, 6, 2, 1, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(2, 9), List.of(5, 17), List.of(13, 19)),
                        List.of(1, 2, 7, 1),
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 8 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(3, 15)),
                        List.of(10),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 3 #2",
                        List.of("-", "X", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X"),
                        List.of(List.of(0, 2), List.of(2, 11), List.of(9, 14), List.of(12, 16), List.of(18, 18)),
                        List.of(1, 6, 2, 1, 1),
                        List.of("-", "X", "O", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 4 #1",
                        List.of("X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-"),
                        List.of(List.of(1, 2), List.of(4, 12), List.of(7, 16), List.of(18, 19)),
                        List.of(2, 2, 3, 2),
                        List.of("X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 18 #1",
                        List.of("X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(3, 17), List.of(7, 19)),
                        List.of(2, 3, 1),
                        List.of("X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 6 #2",
                        List.of("X", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "-", "-"),
                        List.of(List.of(1, 1), List.of(2, 7), List.of(5, 16), List.of(13, 19)),
                        List.of(1, 2, 7, 1),
                        List.of("X", "O", "-", "-", "-", "-", "O", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "X", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 7 #1",
                        List.of("X", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-"),
                        List.of(List.of(1, 1), List.of(2, 8), List.of(6, 16)),
                        List.of(1, 3, 7),
                        List.of("X", "O", "-", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "X", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 2 #1",
                        List.of("-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-"),
                        List.of(List.of(0, 6), List.of(2, 14), List.of(10, 16), List.of(12, 19)),
                        List.of(1, 7, 1, 1),
                        List.of("-", "X", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 6 #3",
                        List.of("X", "O", "X", "-", "-", "-", "O", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "X", "-", "-"),
                        List.of(List.of(1, 1), List.of(3, 7), List.of(6, 16), List.of(14, 19)),
                        List.of(1, 2, 7, 1),
                        List.of("X", "O", "X", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "X", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 7 #2",
                        List.of("X", "O", "X", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "X", "-", "-"),
                        List.of(List.of(1, 1), List.of(3, 8), List.of(7, 16)),
                        List.of(1, 3, 7),
                        List.of("X", "O", "X", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "X", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 7 #3",
                        List.of("X", "O", "X", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "X", "X", "X"),
                        List.of(List.of(1, 1), List.of(4, 8), List.of(8, 16)),
                        List.of(1, 3, 7),
                        List.of("X", "O", "X", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 18 #2",
                        List.of("X", "O", "O", "X", "X", "O", "-", "-", "-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(5, 7), List.of(8, 19)),
                        List.of(2, 3, 1),
                        List.of("X", "O", "O", "X", "X", "O", "O", "O", "-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 17 #1",
                        List.of("X", "O", "X", "-", "X", "X", "-", "-", "-", "-", "X", "X", "-", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 1), List.of(6, 9), List.of(7, 19)),
                        List.of(1, 3, 2),
                        List.of("X", "O", "X", "-", "X", "X", "-", "O", "O", "-", "X", "X", "-", "X", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 3 #3",
                        List.of("-", "X", "O", "-", "X", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X"),
                        List.of(List.of(2, 2), List.of(5, 11), List.of(9, 14), List.of(12, 16), List.of(18, 18)),
                        List.of(1, 6, 2, 1, 1),
                        List.of("-", "X", "O", "-", "X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 7 #4",
                        List.of("X", "O", "X", "X", "X", "-", "O", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X"),
                        List.of(List.of(1, 1), List.of(5, 8), List.of(8, 16)),
                        List.of(1, 3, 7),
                        List.of("X", "O", "X", "X", "X", "-", "O", "O", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 10 #1",
                        List.of("X", "X", "-", "-", "X", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(5, 19)),
                        List.of(11),
                        List.of("X", "X", "-", "-", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 11 #1",
                        List.of("-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 7), List.of(5, 17)),
                        List.of(3, 9),
                        List.of("-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 14 #1",
                        List.of("-", "O", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(5, 15), List.of(10, 19)),
                        List.of(2, 6, 3),
                        List.of("-", "O", "-", "-", "X", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 16 #1",
                        List.of("-", "O", "-", "-", "X", "-", "-", "-", "-", "-", "X", "-", "-", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(5, 9), List.of(8, 19)),
                        List.of(2, 4, 1),
                        List.of("-", "O", "-", "-", "X", "-", "O", "O", "O", "-", "X", "-", "-", "X", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 2 #2",
                        List.of("X", "X", "O", "X", "X", "-", "-", "O", "O", "O", "O", "O", "-", "X", "-", "-", "-", "X", "O", "X"),
                        List.of(List.of(2, 2), List.of(5, 12), List.of(14, 16), List.of(18, 18)),
                        List.of(1, 7, 1, 1),
                        List.of("X", "X", "O", "X", "X", "-", "O", "O", "O", "O", "O", "O", "-", "X", "-", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 1 #1",
                        List.of("-", "X", "O", "-", "X", "-", "-", "-", "X", "X", "X", "X", "-", "X", "X", "-", "-", "X", "-", "X"),
                        List.of(List.of(2, 2), List.of(5, 7), List.of(9, 16), List.of(12, 19)),
                        List.of(1, 3, 2, 1),
                        List.of("-", "X", "O", "-", "X", "O", "O", "O", "X", "X", "X", "X", "-", "X", "X", "-", "-", "X", "-", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 3 #4",
                        List.of("-", "X", "O", "-", "X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "X", "O", "X"),
                        List.of(List.of(2, 2), List.of(5, 11), List.of(12, 14), List.of(15, 16), List.of(18, 18)),
                        List.of(1, 6, 2, 1, 1),
                        List.of("-", "X", "O", "-", "X", "-", "O", "O", "O", "O", "O", "-", "-", "O", "-", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 5 #1",
                        List.of("X", "O", "X", "-", "X", "-", "O", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "O"),
                        List.of(List.of(1, 1), List.of(5, 7), List.of(8, 16), List.of(19, 19)),
                        List.of(1, 2, 5, 1),
                        List.of("X", "O", "X", "-", "X", "-", "O", "-", "X", "-", "-", "-", "O", "-", "-", "-", "-", "X", "X", "O")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 7 #5",
                        List.of("X", "O", "X", "X", "X", "-", "O", "O", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X"),
                        List.of(List.of(1, 1), List.of(5, 8), List.of(9, 16)),
                        List.of(1, 3, 7),
                        List.of("X", "O", "X", "X", "X", "-", "O", "O", "-", "-", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 1 #2",
                        List.of("X", "X", "O", "X", "X", "O", "O", "O", "X", "X", "X", "X", "-", "X", "X", "-", "-", "X", "-", "X"),
                        List.of(List.of(2, 2), List.of(5, 7), List.of(15, 16), List.of(12, 18)),
                        List.of(1, 3, 2, 1),
                        List.of("X", "X", "O", "X", "X", "O", "O", "O", "X", "X", "X", "X", "-", "X", "X", "O", "O", "X", "-", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 5 #2",
                        List.of("X", "O", "X", "X", "X", "-", "O", "-", "X", "-", "-", "-", "O", "O", "-", "O", "-", "X", "X", "O"),
                        List.of(List.of(1, 1), List.of(5, 7), List.of(11, 16), List.of(19, 19)),
                        List.of(1, 2, 5, 1),
                        List.of("X", "O", "X", "X", "X", "-", "O", "-", "X", "-", "-", "-", "O", "O", "O", "O", "-", "X", "X", "O")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 11 #2",
                        List.of("-", "-", "-", "-", "X", "-", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X"),
                        List.of(List.of(0, 3), List.of(9, 17)),
                        List.of(3, 9),
                        List.of("-", "O", "O", "-", "X", "-", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 14 #2",
                        List.of("O", "O", "X", "X", "X", "-", "X", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(7, 12), List.of(15, 19)),
                        List.of(2, 6, 3),
                        List.of("O", "O", "X", "X", "X", "-", "X", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 2 #3",
                        List.of("X", "X", "O", "X", "X", "-", "O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "-", "X", "O", "X"),
                        List.of(List.of(2, 2), List.of(5, 11), List.of(15, 16), List.of(18, 18)),
                        List.of(1, 7, 1, 1),
                        List.of("X", "X", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 0 #1",
                        List.of("X", "X", "O", "X", "X", "-", "-", "-", "X", "X", "X", "-", "X", "X", "X", "-", "-", "X", "O", "X"),
                        List.of(List.of(2, 2), List.of(5, 7), List.of(7, 16), List.of(18, 18)),
                        List.of(1, 2, 1, 1),
                        List.of("X", "X", "O", "X", "X", "-", "O", "-", "X", "X", "X", "-", "X", "X", "X", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 14 #3",
                        List.of("O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "O", "-", "-"),
                        List.of(List.of(0, 1), List.of(7, 12), List.of(16, 19)),
                        List.of(2, 6, 3),
                        List.of("O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 3 #5",
                        List.of("X", "X", "O", "X", "X", "O", "O", "O", "O", "O", "O", "X", "O", "O", "-", "X", "-", "X", "O", "X"),
                        List.of(List.of(2, 2), List.of(5, 10), List.of(12, 13), List.of(16, 16), List.of(18, 18)),
                        List.of(1, 6, 2, 1, 1),
                        List.of("X", "X", "O", "X", "X", "O", "O", "O", "O", "O", "O", "X", "O", "O", "-", "X", "O", "X", "O", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Column 16 #2",
                        List.of("O", "O", "-", "X", "X", "X", "O", "O", "O", "-", "X", "-", "X", "X", "X", "-", "-", "X", "X", "-"),
                        List.of(List.of(0, 1), List.of(6, 9), List.of(11, 19)),
                        List.of(2, 4, 1),
                        List.of("O", "O", "-", "X", "X", "X", "O", "O", "O", "O", "X", "-", "X", "X", "X", "-", "-", "X", "X", "-")
                )
        );
    }
}