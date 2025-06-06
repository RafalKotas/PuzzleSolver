package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07804_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 13), List.of(3, 29)),
                        List.of(2, 15),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 19 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 12), List.of(2, 29)),
                        List.of(1, 16),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 12), List.of(11, 18), List.of(17, 23), List.of(25, 29)),
                        List.of(2, 2, 1, 5),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 13), List.of(11, 18), List.of(17, 25), List.of(27, 29)),
                        List.of(3, 2, 1, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(7, 15), List.of(17, 17), List.of(26, 29)),
                        List.of(5, 1, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 15 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(8, 14), List.of(17, 17), List.of(26, 29)),
                        List.of(4, 1, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "X", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O"),
                        List.of(List.of(0, 13), List.of(4, 18), List.of(17, 27), List.of(29, 29)),
                        List.of(3, 2, 6, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "X", "X", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 14 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "O", "-", "-", "-", "X", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O"),
                        List.of(List.of(7, 14), List.of(17, 17), List.of(26, 29)),
                        List.of(5, 1, 4),
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "O", "O", "-", "-", "-", "X", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 0 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 13), List.of(14, 29)),
                        List.of(2, 15),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "O", "O", "O", "X", "O", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-"),
                        List.of(List.of(0, 10), List.of(3, 13), List.of(11, 17), List.of(17, 21), List.of(28, 29)),
                        List.of(2, 2, 7, 3, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "X", "O", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 9 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "X", "X", "O", "O", "-", "-", "-", "O", "-", "-", "-", "-", "X", "X", "O"),
                        List.of(List.of(0, 13), List.of(4, 18), List.of(17, 26), List.of(29, 29)),
                        List.of(3, 2, 6, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "X", "X", "O", "O", "-", "-", "O", "O", "-", "-", "-", "-", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 18 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "X", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O"),
                        List.of(List.of(0, 13), List.of(3, 17), List.of(16, 26), List.of(28, 29)),
                        List.of(2, 2, 8, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "X", "X", "O", "O", "-", "O", "O", "O", "O", "O", "-", "-", "-", "X", "O", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 9 #3",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-", "-", "-", "X", "X", "O", "O", "-", "-", "O", "O", "-", "-", "-", "X", "X", "X", "O"),
                        List.of(List.of(0, 13), List.of(4, 18), List.of(17, 25), List.of(29, 29)),
                        List.of(3, 2, 6, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-", "-", "-", "X", "X", "O", "O", "-", "O", "O", "O", "-", "-", "-", "X", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 8 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "X", "X", "O", "X", "-", "-", "X", "-", "-", "-", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 11), List.of(3, 14), List.of(17, 17), List.of(22, 24), List.of(29, 29)),
                        List.of(2, 2, 1, 3, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "X", "X", "O", "X", "-", "-", "X", "O", "O", "O", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 18 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "X", "X", "O", "O", "-", "O", "O", "O", "O", "O", "-", "X", "-", "X", "O", "O"),
                        List.of(List.of(0, 13), List.of(3, 17), List.of(16, 24), List.of(28, 29)),
                        List.of(2, 2, 8, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "-", "X", "O", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-", "-", "X", "X", "X", "O", "X", "-", "-", "X", "-", "O", "-", "-", "-", "X", "X", "O"),
                        List.of(List.of(0, 12), List.of(3, 17), List.of(11, 20), List.of(22, 26), List.of(29, 29)),
                        List.of(2, 1, 1, 4, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-", "-", "X", "X", "X", "O", "X", "-", "-", "X", "-", "O", "O", "O", "-", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "-", "-", "X", "X", "O", "O", "O", "X", "X", "X", "-", "-", "-", "X", "X", "X", "O"),
                        List.of(List.of(0, 6), List.of(5, 9), List.of(11, 14), List.of(17, 25), List.of(29, 29)),
                        List.of(4, 2, 4, 3, 1),
                        List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "X", "O", "O", "O", "X", "X", "X", "-", "-", "-", "X", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-", "-", "-", "X", "X", "O", "X", "X", "-", "-", "-", "-", "-", "-", "X", "X", "X", "O"),
                        List.of(List.of(0, 13), List.of(4, 17), List.of(11, 21), List.of(20, 25), List.of(29, 29)),
                        List.of(3, 1, 1, 4, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-", "-", "-", "X", "X", "O", "X", "X", "-", "-", "O", "O", "-", "-", "X", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 10 #2",
                        List.of("-", "-", "O", "O", "-", "-", "X", "-", "-", "-", "X", "O", "O", "O", "O", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 5), List.of(7, 9), List.of(11, 14), List.of(17, 19), List.of(29, 29)),
                        List.of(4, 2, 4, 3, 1),
                        List.of("-", "-", "O", "O", "-", "-", "X", "-", "O", "-", "X", "O", "O", "O", "O", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 9 #4",
                        List.of("-", "-", "-", "-", "X", "X", "-", "-", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 3), List.of(11, 12), List.of(17, 22), List.of(29, 29)),
                        List.of(3, 2, 6, 1),
                        List.of("-", "O", "O", "-", "X", "X", "-", "-", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 11 #1",
                        List.of("X", "X", "X", "-", "O", "O", "O", "-", "X", "-", "X", "O", "X", "X", "O", "X", "X", "O", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(3, 7), List.of(9, 9), List.of(11, 11), List.of(14, 14), List.of(17, 17), List.of(19, 20), List.of(29, 29)),
                        List.of(4, 1, 1, 1, 1, 2, 1),
                        List.of("X", "X", "X", "-", "O", "O", "O", "-", "X", "O", "X", "O", "X", "X", "O", "X", "X", "O", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 0 #3",
                        List.of("-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                        List.of(List.of(0, 1), List.of(14, 28)),
                        List.of(2, 15),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 8 #2",
                        List.of("-", "-", "X", "X", "X", "X", "X", "-", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 1), List.of(10, 11), List.of(17, 17), List.of(22, 24), List.of(29, 29)),
                        List.of(2, 2, 1, 3, 1),
                        List.of("O", "O", "X", "X", "X", "X", "X", "-", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 16 #2",
                        List.of("X", "-", "X", "X", "X", "X", "-", "-", "-", "X", "X", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "O"),
                        List.of(List.of(6, 8), List.of(11, 11), List.of(17, 17), List.of(23, 26), List.of(29, 29)),
                        List.of(2, 1, 1, 4, 1),
                        List.of("X", "-", "X", "X", "X", "X", "-", "O", "-", "X", "X", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 18 #3",
                        List.of("X", "-", "X", "X", "X", "X", "-", "-", "-", "X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O"),
                        List.of(List.of(6, 8), List.of(12, 13), List.of(16, 23), List.of(28, 29)),
                        List.of(2, 2, 8, 2),
                        List.of("X", "-", "X", "X", "X", "X", "-", "O", "-", "X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o07804 / 30x20 / diff 3.0 / Column 16 #3",
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "-", "X", "X", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "O"),
                        List.of(List.of(7, 8), List.of(11, 11), List.of(17, 17), List.of(23, 26), List.of(29, 29)),
                        List.of(2, 1, 1, 4, 1),
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "O")
                )
        );
    }
}