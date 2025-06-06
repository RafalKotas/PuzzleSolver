package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07849_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(3, 17), List.of(13, 19)),
                        List.of(2, 9, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(3, 5), List.of(5, 7), List.of(7, 9), List.of(9, 11), List.of(11, 17), List.of(17, 19)),
                        List.of(2, 1, 1, 1, 1, 5, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 17 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(5, 19)),
                        List.of(4, 11),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 13 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X", "-", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(3, 4), List.of(5, 6), List.of(7, 8), List.of(10, 10), List.of(12, 17), List.of(18, 19)),
                        List.of(2, 1, 1, 1, 1, 5, 1),
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 18 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 13)),
                        List.of(8),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 11 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 10), List.of(9, 19)),
                        List.of(3, 2, 1),
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 10 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 9), List.of(9, 19)),
                        List.of(3, 1, 1),
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 16 #1",
                        List.of("O", "O", "O", "O", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(8, 13), List.of(16, 19)),
                        List.of(4, 6, 2),
                        List.of("O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 17 #2",
                        List.of("-", "O", "O", "O", "-", "-", "O", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(5, 16)),
                        List.of(4, 11),
                        List.of("-", "O", "O", "O", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 14 #1",
                        List.of("O", "O", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 8), List.of(6, 10), List.of(8, 13), List.of(10, 15), List.of(12, 19)),
                        List.of(3, 1, 1, 1, 1, 3),
                        List.of("O", "O", "O", "-", "-", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 12 #2",
                        List.of("O", "O", "X", "X", "-", "-", "-", "X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(8, 17), List.of(13, 19)),
                        List.of(2, 9, 1),
                        List.of("O", "O", "X", "X", "-", "-", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 13 #3",
                        List.of("O", "O", "X", "X", "-", "-", "-", "X", "O", "X", "O", "X", "-", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 4), List.of(5, 6), List.of(8, 8), List.of(10, 10), List.of(12, 17), List.of(18, 19)),
                        List.of(2, 1, 1, 1, 1, 5, 1),
                        List.of("O", "O", "X", "X", "O", "-", "-", "X", "O", "X", "O", "X", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 13 #4",
                        List.of("O", "O", "X", "X", "O", "-", "-", "X", "O", "X", "O", "X", "X", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 4), List.of(6, 6), List.of(8, 8), List.of(10, 10), List.of(12, 17), List.of(18, 19)),
                        List.of(2, 1, 1, 1, 1, 5, 1),
                        List.of("O", "O", "X", "X", "O", "-", "O", "X", "O", "X", "O", "X", "X", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 14 #2",
                        List.of("O", "O", "O", "X", "O", "-", "O", "X", "-", "X", "O", "X", "X", "X", "X", "X", "X", "O", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(6, 6), List.of(8, 8), List.of(10, 10), List.of(17, 19)),
                        List.of(3, 1, 1, 1, 1, 3),
                        List.of("O", "O", "O", "X", "O", "-", "O", "X", "O", "X", "O", "X", "X", "X", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 13 #5",
                        List.of("O", "O", "X", "X", "O", "X", "O", "X", "O", "X", "O", "X", "X", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 4), List.of(6, 6), List.of(8, 8), List.of(10, 10), List.of(13, 17), List.of(19, 19)),
                        List.of(2, 1, 1, 1, 1, 5, 1),
                        List.of("O", "O", "X", "X", "O", "X", "O", "X", "O", "X", "O", "X", "X", "O", "O", "O", "O", "O", "-", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 9 #1",
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(1, 2), List.of(8, 9), List.of(18, 19)),
                        List.of(2, 2, 2),
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "-", "-", "-", "-", "-", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Column 7 #1",
                        List.of("X", "X", "X", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "-", "-", "-", "-", "O", "X", "X"),
                        List.of(List.of(3, 3), List.of(8, 8), List.of(16, 17)),
                        List.of(1, 1, 2),
                        List.of("X", "X", "X", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "-", "-", "-", "O", "O", "X", "X")
                )
        );
    }
}