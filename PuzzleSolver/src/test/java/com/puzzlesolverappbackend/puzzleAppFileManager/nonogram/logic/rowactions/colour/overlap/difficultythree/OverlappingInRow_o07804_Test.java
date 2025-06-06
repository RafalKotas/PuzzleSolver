package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07804_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(16),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(8, 13), List.of(11, 16), List.of(14, 19)),
                        List.of(7, 2, 2, 2),
                        List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 29 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(17),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 15 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "X", "O"),
                        List.of(List.of(0, 2), List.of(19, 19)),
                        List.of(3, 1),
                        List.of("O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "X", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 16 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "O"),
                        List.of(List.of(0, 2), List.of(18, 19)),
                        List.of(3, 2),
                        List.of("O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 28 #1",
                        List.of("-", "-", "-", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(5, 6), List.of(14, 15), List.of(17, 19)),
                        List.of(3, 2, 2, 2),
                        List.of("O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "-", "O", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 18 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(3, 10), List.of(9, 19)),
                        List.of(2, 2, 2),
                        List.of("O", "O", "-", "-", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 19 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 13), List.of(8, 19)),
                        List.of(3, 3, 2),
                        List.of("O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 27 #1",
                        List.of("O", "-", "-", "-", "X", "O", "O", "O", "X", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(5, 7), List.of(14, 15), List.of(17, 19)),
                        List.of(2, 3, 2, 1),
                        List.of("O", "O", "-", "-", "X", "O", "O", "O", "X", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 13 #1",
                        List.of("-", "O", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(6, 13), List.of(8, 16), List.of(11, 19)),
                        List.of(3, 1, 2, 2),
                        List.of("-", "O", "O", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 12 #2",
                        List.of("-", "O", "O", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(8, 13), List.of(11, 16), List.of(14, 19)),
                        List.of(7, 2, 2, 2),
                        List.of("-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 25 #1",
                        List.of("O", "X", "X", "-", "-", "O", "X", "O", "X", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(3, 5), List.of(5, 11), List.of(7, 13), List.of(9, 17), List.of(11, 19)),
                        List.of(1, 2, 1, 1, 1, 1),
                        List.of("O", "X", "X", "-", "O", "O", "X", "O", "X", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 21 #1",
                        List.of("O", "X", "O", "O", "X", "-", "-", "X", "X", "O", "X", "-", "-", "-", "X", "X", "X", "-", "O", "O"),
                        List.of(List.of(0, 0), List.of(2, 3), List.of(5, 9), List.of(9, 13), List.of(17, 19)),
                        List.of(1, 2, 1, 1, 3),
                        List.of("O", "X", "O", "O", "X", "-", "-", "X", "X", "O", "X", "-", "-", "-", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 23 #1",
                        List.of("O", "X", "X", "O", "O", "-", "-", "X", "O", "-", "-", "-", "-", "-", "X", "X", "-", "-", "O", "O"),
                        List.of(List.of(0, 0), List.of(3, 4), List.of(6, 8), List.of(8, 13), List.of(16, 19)),
                        List.of(1, 2, 1, 1, 4),
                        List.of("O", "X", "X", "O", "O", "-", "-", "X", "O", "-", "-", "-", "-", "-", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 22 #1",
                        List.of("O", "X", "X", "O", "X", "-", "-", "X", "O", "O", "X", "-", "-", "-", "X", "X", "-", "-", "O", "O"),
                        List.of(List.of(0, 0), List.of(3, 3), List.of(5, 9), List.of(8, 13), List.of(17, 19)),
                        List.of(1, 1, 2, 2, 3),
                        List.of("O", "X", "X", "O", "X", "-", "-", "X", "O", "O", "X", "-", "-", "-", "X", "X", "-", "O", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 12 #3",
                        List.of("X", "O", "O", "O", "O", "O", "O", "-", "X", "O", "O", "X", "-", "-", "-", "O", "-", "-", "-", "X"),
                        List.of(List.of(1, 7), List.of(9, 10), List.of(12, 16), List.of(15, 18)),
                        List.of(7, 2, 2, 2),
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "-", "-", "-", "O", "-", "-", "-", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 13 #2",
                        List.of("X", "O", "O", "O", "X", "X", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "-", "-", "-", "O"),
                        List.of(List.of(1, 3), List.of(6, 13), List.of(14, 16), List.of(18, 19)),
                        List.of(3, 1, 2, 2),
                        List.of("X", "O", "O", "O", "X", "X", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "-", "-", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 12 #4",
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "-", "-", "-", "O", "X", "O", "O", "X"),
                        List.of(List.of(1, 7), List.of(9, 10), List.of(14, 15), List.of(17, 18)),
                        List.of(7, 2, 2, 2),
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "-", "-", "O", "O", "X", "O", "O", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 13 #3",
                        List.of("X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "-", "O", "X", "X", "O", "O"),
                        List.of(List.of(1, 3), List.of(10, 10), List.of(14, 15), List.of(18, 19)),
                        List.of(3, 1, 2, 2),
                        List.of("X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O", "X", "X", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 20 #1",
                        List.of("O", "X", "O", "X", "X", "X", "X", "X", "X", "O", "X", "-", "X", "X", "X", "X", "X", "O", "O", "O"),
                        List.of(List.of(0, 0), List.of(2, 2), List.of(9, 9), List.of(11, 11), List.of(17, 19)),
                        List.of(1, 1, 1, 1, 3),
                        List.of("O", "X", "O", "X", "X", "X", "X", "X", "X", "O", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(6, 11), List.of(9, 14), List.of(13, 19)),
                        List.of(3, 2, 2, 2),
                        List.of("-", "-", "O", "-", "-", "X", "O", "O", "X", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 9 #2",
                        List.of("X", "-", "O", "-", "-", "X", "O", "O", "X", "X", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 4), List.of(6, 7), List.of(10, 14), List.of(13, 19)),
                        List.of(3, 2, 2, 2),
                        List.of("X", "-", "O", "O", "-", "X", "O", "O", "X", "X", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 6 #1",
                        List.of("X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "-", "X", "-", "-"),
                        List.of(List.of(4, 5), List.of(11, 12), List.of(18, 19)),
                        List.of(2, 2, 2),
                        List.of("X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "-", "X", "O", "O")
                )
        );
    }
}
