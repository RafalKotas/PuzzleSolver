package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07924_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 8), List.of(8, 19)),
                        List.of(3, 3, 10),
                        List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(19),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(4, 19)),
                        List.of(3, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(2, 19)),
                        List.of(1, 11),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(17),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(17),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(14),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 13 #2",
                        List.of("-", "-", "-", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(4, 19)),
                        List.of(14),
                        List.of("-", "-", "-", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 1 #2",
                        List.of("-", "-", "O", "-", "-", "-", "O", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 7), List.of(9, 19)),
                        List.of(3, 3, 10),
                        List.of("-", "-", "O", "-", "-", "O", "O", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "X", "-", "X", "-", "X", "X", "X", "X", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(0, 5), List.of(14, 19)),
                        List.of(5, 4),
                        List.of("-", "O", "O", "O", "O", "-", "X", "-", "X", "-", "X", "X", "X", "X", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 1 #3",
                        List.of("-", "-", "O", "X", "-", "O", "O", "O", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(0, 3), List.of(4, 7), List.of(9, 19)),
                        List.of(3, 3, 10),
                        List.of("-", "O", "O", "X", "-", "O", "O", "O", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 0 #1",
                        List.of("-", "O", "X", "-", "-", "-", "X", "O", "X", "O", "X", "X", "X", "X", "-", "-", "X", "O", "-", "-"),
                        List.of(List.of(0, 5), List.of(3, 9), List.of(5, 15), List.of(17, 19)),
                        List.of(2, 1, 1, 3),
                        List.of("-", "O", "X", "-", "-", "-", "X", "O", "X", "O", "X", "X", "X", "X", "-", "-", "X", "O", "O", "O")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 13 #3",
                        List.of("X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(5, 19)),
                        List.of(14),
                        List.of("X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 16 #1",
                        List.of("X", "-", "O", "-", "O", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(1, 2), List.of(4, 11), List.of(9, 15), List.of(17, 19)),
                        List.of(2, 2, 3, 3),
                        List.of("X", "O", "O", "-", "O", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "O", "O", "O")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 16 #2",
                        List.of("X", "O", "O", "X", "O", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "O", "O", "O"),
                        List.of(List.of(1, 2), List.of(4, 5), List.of(9, 12), List.of(17, 19)),
                        List.of(2, 2, 3, 3),
                        List.of("X", "O", "O", "X", "O", "O", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "O", "O", "O")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 2 #2",
                        List.of("X", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "-", "-", "O", "O", "X", "-"),
                        List.of(List.of(1, 5), List.of(14, 17)),
                        List.of(5, 4),
                        List.of("X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "-")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 17 #1",
                        List.of("O", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(4, 7), List.of(6, 10), List.of(9, 14), List.of(13, 17)),
                        List.of(2, 2, 2, 3, 2),
                        List.of("O", "O", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 17 #2",
                        List.of("O", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(4, 7), List.of(7, 10), List.of(10, 14), List.of(14, 17)),
                        List.of(2, 2, 2, 3, 2),
                        List.of("O", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 4 #2",
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(1, 19)),
                        List.of(19),
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 17 #3",
                        List.of("O", "O", "X", "X", "-", "-", "-", "X", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(4, 6), List.of(8, 10), List.of(10, 14), List.of(14, 17)),
                        List.of(2, 2, 2, 3, 2),
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "-", "O", "-", "-", "O", "-", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 17 #4",
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "-", "O", "-", "-", "O", "-", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(4, 6), List.of(8, 10), List.of(11, 14), List.of(14, 17)),
                        List.of(2, 2, 2, 3, 2),
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "-", "O", "-", "-", "O", "O", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 17 #5",
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "-", "O", "-", "-", "O", "O", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(4, 6), List.of(8, 10), List.of(11, 14), List.of(15, 17)),
                        List.of(2, 2, 2, 3, 2),
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "-", "O", "-", "-", "O", "O", "-", "-", "O", "-", "X", "X")
                ),
                Arguments.of("o07924 / 20x20 / diff 2.0 / Row 19 #1",
                        List.of("X", "X", "O", "O", "X", "X", "X", "X", "-", "X", "O", "O", "X", "X", "-", "-", "X", "X", "X", "O"),
                        List.of(List.of(2, 3), List.of(10, 11), List.of(14, 15), List.of(19, 19)),
                        List.of(2, 2, 2, 1),
                        List.of("X", "X", "O", "O", "X", "X", "X", "X", "-", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O")
                )
        );
    }
}
