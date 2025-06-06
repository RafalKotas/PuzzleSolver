package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07940_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(7, 19)),
                        List.of(6, 9),
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 11), List.of(7, 19)),
                        List.of(6, 7),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(5, 19)),
                        List.of(4, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 12), List.of(9, 15), List.of(12, 19)),
                        List.of(8, 2, 3),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14), List.of(9, 17), List.of(12, 19)),
                        List.of(8, 2, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(6, 10), List.of(9, 14), List.of(13, 19)),
                        List.of(5, 2, 3, 4),
                        List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 4 #2",
                        List.of("-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(7, 19)),
                        List.of(4, 10),
                        List.of("-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 13 #2",
                        List.of("-", "-", "O", "O", "O", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X"),
                        List.of(List.of(0, 9), List.of(9, 16), List.of(18, 18)),
                        List.of(8, 2, 1),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 11 #1",
                        List.of("-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(6, 15), List.of(8, 17), List.of(10, 19)),
                        List.of(5, 1, 1, 1),
                        List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 14 #1",
                        List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X"),
                        List.of(List.of(0, 7), List.of(7, 16), List.of(18, 18)),
                        List.of(6, 2, 1),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 4 #3",
                        List.of("-", "-", "-", "-", "X", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "O", "-"),
                        List.of(List.of(0, 3), List.of(7, 19)),
                        List.of(4, 10),
                        List.of("O", "O", "O", "O", "X", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 1 #2",
                        List.of("-", "-", "-", "-", "O", "O", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(0, 5), List.of(8, 19)),
                        List.of(6, 9),
                        List.of("O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 4 #4",
                        List.of("O", "O", "O", "O", "X", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "O", "-"),
                        List.of(List.of(0, 3), List.of(9, 19)),
                        List.of(4, 10),
                        List.of("O", "O", "O", "O", "X", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 17 #1",
                        List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X"),
                        List.of(List.of(0, 5), List.of(5, 11), List.of(7, 14), List.of(14, 18)),
                        List.of(4, 1, 2, 3),
                        List.of("-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 5 #1",
                        List.of("X", "X", "X", "X", "X", "-", "X", "-", "-", "-", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O"),
                        List.of(List.of(7, 10), List.of(13, 14), List.of(18, 19)),
                        List.of(3, 2, 2),
                        List.of("X", "X", "X", "X", "X", "-", "X", "-", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 6 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "X", "X", "X", "-", "X", "X", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(14, 19)),
                        List.of(5),
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "X", "X", "X", "-", "X", "X", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 16 #2",
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-"),
                        List.of(List.of(1, 6), List.of(7, 10), List.of(10, 14), List.of(15, 19)),
                        List.of(5, 2, 3, 4),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 19 #1",
                        List.of("-", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(0, 14), List.of(14, 19)),
                        List.of(2, 4),
                        List.of("-", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 18 #1",
                        List.of("O", "O", "O", "-", "-", "X", "X", "O", "X", "-", "-", "-", "X", "-", "-", "O", "O", "X", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 7), List.of(13, 16), List.of(15, 19)),
                        List.of(3, 1, 4, 2),
                        List.of("O", "O", "O", "-", "-", "X", "X", "O", "X", "-", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 11 #2",
                        List.of("X", "-", "O", "O", "O", "-", "O", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-"),
                        List.of(List.of(1, 6), List.of(6, 8), List.of(8, 16), List.of(10, 19)),
                        List.of(5, 1, 1, 1),
                        List.of("X", "-", "O", "O", "O", "O", "O", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 18 #2",
                        List.of("O", "O", "O", "X", "-", "X", "X", "O", "X", "-", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-"),
                        List.of(List.of(0, 2), List.of(7, 7), List.of(13, 16), List.of(18, 19)),
                        List.of(3, 1, 4, 2),
                        List.of("O", "O", "O", "X", "-", "X", "X", "O", "X", "-", "-", "-", "X", "O", "O", "O", "O", "X", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 10 #1",
                        List.of("X", "X", "O", "O", "O", "X", "O", "X", "X", "X", "X", "-", "-", "-", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 4), List.of(6, 6), List.of(11, 13)),
                        List.of(3, 1, 2),
                        List.of("X", "X", "O", "O", "O", "X", "O", "X", "X", "X", "X", "-", "O", "-", "X", "X", "X", "X", "X", "X")
                )
        );
    }
}
