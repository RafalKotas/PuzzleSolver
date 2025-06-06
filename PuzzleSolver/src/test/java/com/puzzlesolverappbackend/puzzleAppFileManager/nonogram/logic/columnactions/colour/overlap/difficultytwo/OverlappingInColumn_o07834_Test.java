package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07834_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(5, 12), List.of(13, 15), List.of(16, 19)),
                        List.of(4, 7, 2, 3),
                        List.of("-", "O", "O", "O", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "O", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(5, 12), List.of(11, 19)),
                        List.of(4, 5, 6),
                        List.of("-", "-", "-", "O", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(4, 19)),
                        List.of(3, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 1 #1",
                        List.of("-", "-", "X", "X", "-", "-", "-", "-", "X", "X", "X", "-", "-", "-", "O", "-", "-", "-", "X", "-"),
                        List.of(List.of(0, 7), List.of(11, 17), List.of(19, 19)),
                        List.of(2, 7, 1),
                        List.of("-", "-", "X", "X", "-", "-", "-", "-", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 13 #1",
                        List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(4, 19)),
                        List.of(1, 9),
                        List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 2 #2",
                        List.of("-", "O", "O", "O", "-", "O", "O", "O", "O", "O", "O", "O", "X", "-", "O", "O", "X", "O", "O", "O"),
                        List.of(List.of(0, 3), List.of(5, 11), List.of(14, 15), List.of(17, 19)),
                        List.of(4, 7, 2, 3),
                        List.of("O", "O", "O", "O", "-", "O", "O", "O", "O", "O", "O", "O", "X", "-", "O", "O", "X", "O", "O", "O")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 3 #1",
                        List.of("-", "-", "X", "O", "O", "-", "-", "-", "X", "-", "X", "X", "-", "-", "X", "O", "X", "X", "X", "O"),
                        List.of(List.of(0, 4), List.of(3, 7), List.of(15, 15), List.of(19, 19)),
                        List.of(2, 3, 1, 1),
                        List.of("-", "-", "X", "O", "O", "O", "-", "-", "X", "-", "X", "X", "-", "-", "X", "O", "X", "X", "X", "O")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 3 #2",
                        List.of("-", "-", "X", "O", "O", "O", "X", "-", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O"),
                        List.of(List.of(0, 1), List.of(3, 5), List.of(15, 15), List.of(19, 19)),
                        List.of(2, 3, 1, 1),
                        List.of("O", "O", "X", "O", "O", "O", "X", "-", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 5 #2",
                        List.of("-", "-", "-", "O", "-", "X", "-", "-", "O", "O", "-", "-", "-", "X", "O", "O", "O", "-", "-", "O"),
                        List.of(List.of(0, 6), List.of(5, 12), List.of(14, 19)),
                        List.of(4, 5, 6),
                        List.of("-", "-", "-", "O", "-", "X", "-", "-", "O", "O", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 7 #1",
                        List.of("-", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "-", "-", "-", "O"),
                        List.of(List.of(0, 14), List.of(14, 16), List.of(18, 19)),
                        List.of(2, 1, 2),
                        List.of("-", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "-", "-", "O", "O")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 5 #3",
                        List.of("-", "-", "-", "O", "-", "X", "-", "-", "O", "O", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O"),
                        List.of(List.of(0, 4), List.of(6, 12), List.of(14, 19)),
                        List.of(4, 5, 6),
                        List.of("-", "O", "O", "O", "-", "X", "-", "-", "O", "O", "O", "-", "-", "X", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 12 #2",
                        List.of("X", "-", "-", "X", "-", "X", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(6, 8), List.of(6, 19)),
                        List.of(3, 10),
                        List.of("X", "-", "-", "X", "-", "X", "O", "O", "O", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 13 #2",
                        List.of("X", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 9), List.of(6, 19)),
                        List.of(1, 9),
                        List.of("X", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 15 #1",
                        List.of("X", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(6, 9), List.of(6, 12), List.of(7, 15), List.of(9, 19)),
                        List.of(3, 2, 1, 3),
                        List.of("X", "-", "-", "X", "-", "X", "-", "O", "O", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 12 #3",
                        List.of("X", "-", "-", "X", "X", "X", "O", "O", "O", "-", "O", "O", "O", "O", "O", "O", "-", "-", "O", "-"),
                        List.of(List.of(6, 8), List.of(9, 19)),
                        List.of(3, 10),
                        List.of("X", "-", "-", "X", "X", "X", "O", "O", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 13 #3",
                        List.of("X", "-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "X", "-"),
                        List.of(List.of(1, 9), List.of(6, 17)),
                        List.of(1, 9),
                        List.of("X", "-", "-", "X", "X", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 14 #1",
                        List.of("X", "-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "O", "-", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 9), List.of(6, 12), List.of(9, 19)),
                        List.of(1, 4, 4),
                        List.of("X", "-", "-", "X", "X", "X", "-", "-", "-", "O", "-", "O", "-", "X", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 12 #4",
                        List.of("X", "-", "-", "X", "X", "X", "O", "O", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(6, 8), List.of(10, 19)),
                        List.of(3, 10),
                        List.of("X", "-", "-", "X", "X", "X", "O", "O", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 15 #2",
                        List.of("X", "-", "-", "X", "X", "X", "-", "O", "O", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(6, 9), List.of(10, 12), List.of(13, 15), List.of(15, 19)),
                        List.of(3, 2, 1, 3),
                        List.of("X", "-", "-", "X", "X", "X", "-", "O", "O", "-", "-", "O", "-", "-", "-", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 14 #2",
                        List.of("X", "-", "-", "X", "X", "X", "-", "-", "O", "O", "O", "O", "-", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 7), List.of(8, 11), List.of(15, 19)),
                        List.of(1, 4, 4),
                        List.of("X", "-", "-", "X", "X", "X", "-", "-", "O", "O", "O", "O", "-", "X", "X", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 16 #1",
                        List.of("X", "X", "X", "X", "X", "X", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(9, 17), List.of(12, 19)),
                        List.of(5, 1),
                        List.of("X", "X", "X", "X", "X", "X", "-", "-", "X", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 0 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-"),
                        List.of(List.of(7, 8), List.of(13, 14), List.of(19, 19)),
                        List.of(2, 2, 1),
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 9 #1",
                        List.of("X", "X", "X", "X", "X", "X", "-", "-", "-", "X", "O", "-", "-", "-", "X", "-", "-", "X", "O", "X"),
                        List.of(List.of(10, 11), List.of(18, 18)),
                        List.of(2, 1),
                        List.of("X", "X", "X", "X", "X", "X", "-", "-", "-", "X", "O", "O", "-", "-", "X", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 13 #4",
                        List.of("X", "X", "X", "X", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X"),
                        List.of(List.of(6, 6), List.of(7, 16)),
                        List.of(1, 9),
                        List.of("X", "X", "X", "X", "X", "X", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 14 #3",
                        List.of("X", "X", "X", "X", "X", "X", "-", "X", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "-"),
                        List.of(List.of(6, 6), List.of(8, 11), List.of(15, 18)),
                        List.of(1, 4, 4),
                        List.of("X", "X", "X", "X", "X", "X", "O", "X", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Column 15 #3",
                        List.of("X", "X", "X", "X", "X", "X", "-", "O", "O", "X", "O", "O", "-", "-", "X", "O", "X", "O", "O", "-"),
                        List.of(List.of(6, 8), List.of(10, 11), List.of(15, 15), List.of(17, 19)),
                        List.of(3, 2, 1, 3),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "O", "O", "-", "-", "X", "O", "X", "O", "O", "O")
                )
        );
    }
}