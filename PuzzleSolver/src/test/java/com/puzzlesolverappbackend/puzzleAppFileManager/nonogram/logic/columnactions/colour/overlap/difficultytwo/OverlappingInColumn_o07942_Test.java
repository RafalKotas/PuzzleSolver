package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07942_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(7, 19)),
                        List.of(6, 8),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 7 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(4, 9), List.of(7, 19)),
                        List.of(3, 2, 9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 8 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(6, 19)),
                        List.of(5, 10),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(3, 7), List.of(6, 19)),
                        List.of(2, 2, 11),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 10), List.of(7, 19)),
                        List.of(6, 8),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(3, 19)),
                        List.of(2, 11),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 18 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(13),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "O", "-", "-", "-", "-", "-", "-", "X"),
                        List.of(List.of(0, 8), List.of(10, 18)),
                        List.of(5, 7),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 12 #2",
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "O", "O", "O", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 10), List.of(12, 19)),
                        List.of(6, 8),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 17 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "X", "X", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 6), List.of(8, 10), List.of(13, 19)),
                        List.of(1, 3, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "X", "X", "-", "-", "-", "O", "-", "-", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 19 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(13, 19)),
                        List.of(3, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "X", "X", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 6 #2",
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "-", "O", "O", "O", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 9), List.of(12, 19)),
                        List.of(6, 8),
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(4, 12), List.of(11, 19)),
                        List.of(3, 2, 6),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "O", "-", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 12 #3",
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O"),
                        List.of(List.of(0, 9), List.of(12, 19)),
                        List.of(6, 8),
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 16 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O"),
                        List.of(List.of(0, 7), List.of(9, 19)),
                        List.of(2, 11),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 18 #2",
                        List.of("-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O"),
                        List.of(List.of(7, 19)),
                        List.of(13),
                        List.of("-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 7 #2",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "-", "O", "O", "O"),
                        List.of(List.of(0, 6), List.of(4, 9), List.of(11, 19)),
                        List.of(3, 2, 9),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 19 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "X", "X", "-", "-", "-", "O", "-", "O", "-"),
                        List.of(List.of(8, 10), List.of(15, 19)),
                        List.of(3, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "X", "X", "-", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 14 #1",
                        List.of("-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "X", "-", "-", "O", "X", "-", "X", "X", "X", "O"),
                        List.of(List.of(0, 13), List.of(12, 13), List.of(19, 19)),
                        List.of(2, 2, 1),
                        List.of("-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "X", "-", "O", "O", "X", "-", "X", "X", "X", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 9 #2",
                        List.of("-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(5, 7), List.of(8, 19)),
                        List.of(2, 2, 11),
                        List.of("-", "-", "-", "-", "X", "-", "O", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 2 #1",
                        List.of("-", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X"),
                        List.of(List.of(0, 2), List.of(14, 15)),
                        List.of(2, 2),
                        List.of("-", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 15 #1",
                        List.of("-", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 2), List.of(13, 14), List.of(19, 19)),
                        List.of(2, 2, 1),
                        List.of("-", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 16 #3",
                        List.of("-", "-", "-", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                        List.of(List.of(0, 2), List.of(9, 19)),
                        List.of(2, 11),
                        List.of("-", "O", "-", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 2 #2",
                        List.of("-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(14, 15)),
                        List.of(2, 2),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Column 16 #4",
                        List.of("-", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                        List.of(List.of(0, 1), List.of(9, 19)),
                        List.of(2, 11),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                )
        );
    }
}