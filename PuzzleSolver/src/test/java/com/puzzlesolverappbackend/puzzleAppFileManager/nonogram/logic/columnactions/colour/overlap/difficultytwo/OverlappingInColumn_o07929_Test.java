package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07929_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 10), List.of(9, 13), List.of(12, 15), List.of(14, 19)),
                        List.of(8, 2, 1, 3),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 17 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 4), List.of(2, 12), List.of(10, 15), List.of(13, 19)),
                        List.of(1, 7, 2, 3),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 19 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(7, 13), List.of(13, 15), List.of(15, 19)),
                        List.of(6, 5, 1, 3),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 8), List.of(6, 14), List.of(16, 19)),
                        List.of(5, 5, 3),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 6), List.of(3, 8), List.of(5, 13), List.of(10, 16), List.of(18, 19)),
                        List.of(2, 1, 4, 2, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 7 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 12), List.of(4, 15), List.of(16, 19)),
                        List.of(3, 2, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 10), List.of(4, 12), List.of(6, 15), List.of(16, 19)),
                        List.of(3, 1, 2, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 16 #2",
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 10), List.of(9, 11), List.of(12, 15), List.of(16, 19)),
                        List.of(8, 2, 1, 3),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "X", "-", "O", "-", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 17 #2",
                        List.of("-", "-", "-", "X", "O", "-", "O", "O", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 4), List.of(2, 12), List.of(10, 15), List.of(16, 19)),
                        List.of(1, 7, 2, 3),
                        List.of("-", "-", "-", "X", "O", "-", "O", "O", "O", "-", "O", "-", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 18 #1",
                        List.of("-", "-", "X", "X", "O", "-", "-", "-", "X", "X", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 4), List.of(2, 7), List.of(6, 8), List.of(7, 11), List.of(10, 15), List.of(16, 19)),
                        List.of(1, 2, 1, 2, 2, 3),
                        List.of("-", "-", "X", "X", "O", "-", "-", "-", "X", "X", "O", "-", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 18 #2",
                        List.of("-", "-", "X", "X", "O", "O", "X", "-", "X", "X", "O", "O", "X", "-", "-", "-", "-", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(4, 5), List.of(7, 7), List.of(10, 11), List.of(13, 15), List.of(16, 19)),
                        List.of(1, 2, 1, 2, 2, 3),
                        List.of("-", "-", "X", "X", "O", "O", "X", "O", "X", "X", "O", "O", "X", "-", "O", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 12 #2",
                        List.of("X", "-", "-", "-", "O", "O", "-", "O", "-", "O", "O", "-", "-", "-", "-", "X", "-", "O", "O", "-"),
                        List.of(List.of(1, 8), List.of(7, 14), List.of(16, 19)),
                        List.of(5, 5, 3),
                        List.of("X", "-", "-", "-", "O", "O", "-", "O", "-", "O", "O", "O", "-", "-", "-", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 12 #3",
                        List.of("X", "-", "-", "-", "O", "O", "-", "O", "-", "O", "O", "O", "-", "-", "-", "X", "-", "O", "O", "-"),
                        List.of(List.of(1, 7), List.of(7, 13), List.of(16, 19)),
                        List.of(5, 5, 3),
                        List.of("X", "-", "-", "O", "O", "O", "-", "O", "-", "O", "O", "O", "-", "-", "-", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 19 #2",
                        List.of("O", "-", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "-"),
                        List.of(List.of(0, 5), List.of(7, 13), List.of(13, 15), List.of(16, 19)),
                        List.of(6, 5, 1, 3),
                        List.of("O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 10 #1",
                        List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "-", "O"),
                        List.of(List.of(1, 5), List.of(5, 11), List.of(10, 16), List.of(18, 19)),
                        List.of(3, 1, 2, 2),
                        List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 7 #2",
                        List.of("-", "X", "-", "X", "O", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "O", "O", "-"),
                        List.of(List.of(4, 6), List.of(4, 15), List.of(16, 19)),
                        List.of(3, 2, 3),
                        List.of("-", "X", "-", "X", "O", "O", "O", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 8 #1",
                        List.of("X", "X", "X", "O", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "O", "O", "X"),
                        List.of(List.of(3, 5), List.of(5, 12), List.of(11, 15), List.of(17, 18)),
                        List.of(3, 2, 1, 2),
                        List.of("X", "X", "X", "O", "O", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "O", "O", "X")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 5 #1",
                        List.of("X", "X", "X", "X", "X", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-"),
                        List.of(List.of(6, 8), List.of(8, 19)),
                        List.of(3, 1),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 6 #1",
                        List.of("X", "X", "X", "X", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O"),
                        List.of(List.of(5, 7), List.of(8, 16), List.of(18, 19)),
                        List.of(3, 2, 2),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 0 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(7, 16), List.of(12, 19)),
                        List.of(6, 2),
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 2 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(7, 10), List.of(9, 16), List.of(17, 19)),
                        List.of(3, 1, 2),
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 0 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(9, 16), List.of(13, 19)),
                        List.of(6, 2),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 0 #3",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(9, 14), List.of(16, 19)),
                        List.of(6, 2),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 9 #2",
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "-", "-", "-", "-", "-", "O", "O", "-"),
                        List.of(List.of(2, 4), List.of(11, 11), List.of(13, 15), List.of(16, 19)),
                        List.of(3, 1, 2, 3),
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "-", "-", "O", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 4 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "-", "-", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(7, 10), List.of(15, 19)),
                        List.of(4, 3),
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "-", "-", "X", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 6 #2",
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "-", "-", "-", "O", "X", "O", "O"),
                        List.of(List.of(5, 7), List.of(15, 16), List.of(18, 19)),
                        List.of(3, 2, 2),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "-", "-", "O", "O", "X", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 17 #3",
                        List.of("O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "X", "O", "O", "O"),
                        List.of(List.of(0, 0), List.of(4, 10), List.of(13, 15), List.of(17, 19)),
                        List.of(1, 7, 2, 3),
                        List.of("O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "O", "-", "X", "O", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 19 #3",
                        List.of("O", "O", "O", "O", "O", "O", "X", "-", "O", "O", "O", "O", "X", "-", "-", "-", "X", "O", "O", "O"),
                        List.of(List.of(0, 5), List.of(7, 11), List.of(13, 15), List.of(17, 19)),
                        List.of(6, 5, 1, 3),
                        List.of("O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "X", "-", "-", "-", "X", "O", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 7 #3",
                        List.of("X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "O", "O", "-"),
                        List.of(List.of(4, 6), List.of(13, 15), List.of(16, 19)),
                        List.of(3, 2, 3),
                        List.of("X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "-", "O", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Column 15 #1",
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "X", "X", "O", "O"),
                        List.of(List.of(5, 11), List.of(13, 15), List.of(18, 19)),
                        List.of(7, 2, 2),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-", "O", "-", "X", "X", "O", "O")
                )
        );
    }
}