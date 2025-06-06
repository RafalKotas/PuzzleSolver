package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07982_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(7, 11), List.of(9, 17), List.of(15, 19)),
                        List.of(6, 1, 5, 1),
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(2, 9), List.of(5, 19)),
                        List.of(1, 2, 9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 19 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 15), List.of(12, 19)),
                        List.of(11, 3),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 14 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 6), List.of(2, 9), List.of(11, 19)),
                        List.of(1, 2, 9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 8), List.of(2, 14), List.of(16, 19)),
                        List.of(1, 5, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 11), List.of(2, 13), List.of(4, 16), List.of(18, 19)),
                        List.of(1, 1, 2, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 15 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "X", "O"),
                        List.of(List.of(0, 3), List.of(3, 6), List.of(6, 12), List.of(19, 19)),
                        List.of(2, 2, 5, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "-", "-", "-", "-", "X", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 18 #1",
                        List.of("-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(11, 16), List.of(17, 19)),
                        List.of(1, 5, 2),
                        List.of("-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "-", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 19 #2",
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X", "-", "-", "O", "-"),
                        List.of(List.of(0, 12), List.of(16, 19)),
                        List.of(11, 3),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 18 #2",
                        List.of("-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "X", "O", "-"),
                        List.of(List.of(0, 1), List.of(11, 16), List.of(18, 19)),
                        List.of(1, 5, 2),
                        List.of("-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "X", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 8 #1",
                        List.of("O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 0), List.of(12, 19)),
                        List.of(1, 7),
                        List.of("O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 9 #1",
                        List.of("O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 0), List.of(2, 15), List.of(16, 19)),
                        List.of(1, 3, 3),
                        List.of("O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 15 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 2), List.of(3, 5), List.of(6, 11), List.of(19, 19)),
                        List.of(2, 2, 5, 1),
                        List.of("-", "O", "-", "-", "O", "-", "-", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 9 #2",
                        List.of("O", "X", "-", "-", "X", "-", "-", "-", "-", "-", "X", "-", "-", "X", "X", "X", "X", "O", "O", "-"),
                        List.of(List.of(0, 0), List.of(5, 9), List.of(17, 19)),
                        List.of(1, 3, 3),
                        List.of("O", "X", "-", "-", "X", "-", "-", "O", "-", "-", "X", "-", "-", "X", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 8 #2",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "X"),
                        List.of(List.of(0, 0), List.of(12, 18)),
                        List.of(1, 7),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 19 #3",
                        List.of("-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X", "-", "O", "O", "X"),
                        List.of(List.of(2, 12), List.of(16, 18)),
                        List.of(11, 3),
                        List.of("-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "X")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 17 #1",
                        List.of("-", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 2), List.of(19, 19)),
                        List.of(2, 1),
                        List.of("-", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 1 #1",
                        List.of("-", "-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "X", "O", "-", "X", "X", "X", "O", "O", "O"),
                        List.of(List.of(0, 10), List.of(12, 13), List.of(17, 19)),
                        List.of(1, 2, 3),
                        List.of("-", "-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "X", "O", "O", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 3 #1",
                        List.of("-", "-", "-", "X", "X", "X", "-", "-", "-", "-", "O", "X", "-", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 8), List.of(7, 10), List.of(19, 19)),
                        List.of(2, 4, 1),
                        List.of("-", "-", "-", "X", "X", "X", "-", "O", "O", "O", "O", "X", "-", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 6 #2",
                        List.of("O", "X", "-", "X", "X", "X", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "X", "X", "O", "O"),
                        List.of(List.of(0, 0), List.of(2, 11), List.of(13, 15), List.of(18, 19)),
                        List.of(1, 1, 2, 2),
                        List.of("O", "X", "-", "X", "X", "X", "-", "-", "-", "-", "X", "O", "X", "-", "O", "-", "X", "X", "O", "O")
                ),
                Arguments.of("o07940 / 20x20 / diff 3.0 / Row 3 #2",
                        List.of("X", "O", "-", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(1, 2), List.of(7, 10), List.of(19, 19)),
                        List.of(2, 4, 1),
                        List.of("X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O")
                )
        );
    }
}
