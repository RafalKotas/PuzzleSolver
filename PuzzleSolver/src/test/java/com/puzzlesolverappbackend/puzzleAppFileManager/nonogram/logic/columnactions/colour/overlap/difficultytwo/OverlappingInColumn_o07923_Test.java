package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07923_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(5, 9), List.of(8, 17), List.of(16, 19)),
                        List.of(4, 2, 7, 1),
                        List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 5 #1",
                        List.of("O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(6, 9), List.of(9, 15), List.of(15, 17), List.of(17, 19)),
                        List.of(5, 2, 5, 1, 1),
                        List.of("O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 6 #1",
                        List.of("O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(7, 17), List.of(16, 19)),
                        List.of(6, 8, 1),
                        List.of("O", "O", "-", "O", "O", "O", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 9 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(5, 17), List.of(13, 19)),
                        List.of(4, 7, 1),
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 7 #1",
                        List.of("O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(9, 14)),
                        List.of(5, 4),
                        List.of("O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 8 #1",
                        List.of("O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(6, 16)),
                        List.of(4, 6),
                        List.of("O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 9 #2",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(5, 17), List.of(13, 19)),
                        List.of(4, 7, 1),
                        List.of("O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 10 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(3, 14), List.of(6, 17), List.of(9, 19)),
                        List.of(2, 2, 2, 1),
                        List.of("O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 12 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(3, 16), List.of(5, 19)),
                        List.of(2, 1, 2),
                        List.of("O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 13 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 12), List.of(11, 19)),
                        List.of(3, 1, 2),
                        List.of("O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 14 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(5, 12), List.of(12, 19)),
                        List.of(4, 1, 1),
                        List.of("O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 15 #1",
                        List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(6, 12), List.of(12, 19)),
                        List.of(5, 1, 1),
                        List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 19 #1",
                        List.of("-", "X", "X", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(4, 7), List.of(5, 17), List.of(7, 19)),
                        List.of(4, 1, 1),
                        List.of("-", "X", "X", "X", "O", "O", "O", "O", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 5 #2",
                        List.of("O", "O", "O", "O", "O", "X", "-", "-", "X", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(6, 7), List.of(9, 13), List.of(15, 17), List.of(17, 19)),
                        List.of(5, 2, 5, 1, 1),
                        List.of("O", "O", "O", "O", "O", "X", "O", "O", "X", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 0 #1",
                        List.of("-", "X", "X", "X", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(5, 11), List.of(7, 19)),
                        List.of(6, 1),
                        List.of("-", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 4 #2",
                        List.of("O", "O", "O", "O", "X", "-", "O", "O", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(6, 7), List.of(9, 17), List.of(16, 19)),
                        List.of(4, 2, 7, 1),
                        List.of("O", "O", "O", "O", "X", "-", "O", "O", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 1 #1",
                        List.of("-", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(5, 10), List.of(11, 19)),
                        List.of(1, 5, 1),
                        List.of("O", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 6 #2",
                        List.of("O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(9, 17), List.of(18, 19)),
                        List.of(6, 8, 1),
                        List.of("O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 11 #1",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(8, 12), List.of(10, 17), List.of(12, 19)),
                        List.of(1, 4, 1, 1),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "X", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 9 #3",
                        List.of("O", "O", "O", "O", "X", "X", "X", "X", "X", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(9, 17), List.of(13, 19)),
                        List.of(4, 7, 1),
                        List.of("O", "O", "O", "O", "X", "X", "X", "X", "X", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 11 #2",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "-", "X", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(9, 12), List.of(11, 17), List.of(14, 19)),
                        List.of(1, 4, 1, 1),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 8 #2",
                        List.of("O", "O", "O", "O", "X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "-", "-", "-", "X", "X", "X"),
                        List.of(List.of(0, 3), List.of(9, 15)),
                        List.of(4, 6),
                        List.of("O", "O", "O", "O", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 10 #2",
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(12, 14), List.of(12, 17), List.of(12, 19)),
                        List.of(2, 2, 2, 1),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "-", "X", "X", "-", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 10 #3",
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "-", "X", "X", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(12, 14), List.of(15, 17), List.of(18, 19)),
                        List.of(2, 2, 2, 1),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "-", "X", "X", "-", "O", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 2 #1",
                        List.of("O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(6, 9), List.of(17, 19)),
                        List.of(2, 4, 2),
                        List.of("O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "-", "O", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 11 #3",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "-", "-", "X", "-"),
                        List.of(List.of(0, 0), List.of(9, 12), List.of(16, 17), List.of(19, 19)),
                        List.of(1, 4, 1, 1),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "-", "-", "X", "O")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 10 #4",
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "-", "O", "X", "X", "O"),
                        List.of(List.of(0, 1), List.of(12, 13), List.of(15, 16), List.of(19, 19)),
                        List.of(2, 2, 2, 1),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "X", "X", "O")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Column 11 #4",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "-", "X", "X", "O"),
                        List.of(List.of(0, 0), List.of(9, 12), List.of(16, 16), List.of(19, 19)),
                        List.of(1, 4, 1, 1),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "O", "X", "X", "O")
                )
        );
    }
}