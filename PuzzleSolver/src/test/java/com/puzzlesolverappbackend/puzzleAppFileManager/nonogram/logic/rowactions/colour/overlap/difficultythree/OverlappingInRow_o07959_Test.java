package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07959_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 17), List.of(12, 19)),
                        List.of(11, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 12), List.of(10, 19)),
                        List.of(9, 6),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 18 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 11), List.of(11, 19)),
                        List.of(10, 7),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 15 #1",
                        List.of("-", "-", "O", "-", "-", "X", "X", "-", "O", "X", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 3), List.of(7, 8), List.of(8, 19)),
                        List.of(2, 2, 1),
                        List.of("-", "-", "O", "-", "-", "X", "X", "O", "O", "X", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 17 #1",
                        List.of("-", "O", "O", "-", "-", "-", "X", "X", "O", "X", "X", "-", "X", "O", "X", "X", "X", "X", "-", "-"),
                        List.of(List.of(0, 5), List.of(8, 8), List.of(8, 13), List.of(13, 19)),
                        List.of(5, 1, 1, 1),
                        List.of("-", "O", "O", "O", "O", "-", "X", "X", "O", "X", "X", "-", "X", "O", "X", "X", "X", "X", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 18 #2",
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(0, 9), List.of(11, 19)),
                        List.of(10, 7),
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 18 #3",
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "X", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(0, 9), List.of(13, 19)),
                        List.of(10, 7),
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "X", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 19 #1",
                        List.of("-", "-", "-", "X", "X", "X", "X", "O", "O", "O", "X", "-", "X", "-", "X", "X", "X", "X", "-", "-"),
                        List.of(List.of(0, 9), List.of(7, 9), List.of(18, 19)),
                        List.of(3, 3, 2),
                        List.of("-", "-", "-", "X", "X", "X", "X", "O", "O", "O", "X", "-", "X", "-", "X", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 19 #2",
                        List.of("-", "-", "-", "X", "X", "X", "X", "O", "O", "O", "X", "-", "X", "-", "X", "X", "X", "X", "O", "O"),
                        List.of(List.of(0, 4), List.of(7, 9), List.of(18, 19)),
                        List.of(3, 3, 2),
                        List.of("-", "-", "O", "X", "X", "X", "X", "O", "O", "O", "X", "-", "X", "-", "X", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 4 #2",
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 14), List.of(12, 19)),
                        List.of(11, 1),
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 5 #2",
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(1, 12), List.of(10, 19)),
                        List.of(9, 6),
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 5 #3",
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(1, 12), List.of(11, 19)),
                        List.of(9, 6),
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 6 #1",
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-"),
                        List.of(List.of(1, 4), List.of(9, 11), List.of(9, 14), List.of(12, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "O", "-", "-", "-", "-", "X", "X", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 6 #2",
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "O", "-", "-", "-", "-", "X", "X", "-", "-", "-"),
                        List.of(List.of(1, 4), List.of(9, 11), List.of(12, 14), List.of(15, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "O", "-", "-", "O", "-", "X", "X", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 6 #3",
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "O", "-", "-", "O", "-", "X", "X", "-", "-", "-"),
                        List.of(List.of(1, 4), List.of(9, 11), List.of(12, 14), List.of(17, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "O", "-", "-", "O", "-", "X", "X", "-", "O", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 5 #4",
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(2, 12), List.of(12, 19)),
                        List.of(9, 6),
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 3 #1",
                        List.of("X", "-", "-", "-", "-", "X", "O", "X", "X", "X", "-", "-", "-", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 4), List.of(6, 6), List.of(10, 12), List.of(16, 19)),
                        List.of(2, 1, 2, 2),
                        List.of("X", "-", "-", "-", "-", "X", "O", "X", "X", "X", "-", "O", "-", "X", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 4 #3",
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "-", "X", "X", "-", "-", "-"),
                        List.of(List.of(1, 12), List.of(14, 19)),
                        List.of(11, 1),
                        List.of("X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "-", "X", "X", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 12 #1",
                        List.of("X", "X", "-", "O", "-", "X", "X", "O", "X", "X", "X", "-", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 4), List.of(7, 7), List.of(11, 12)),
                        List.of(2, 1, 2),
                        List.of("X", "X", "-", "O", "-", "X", "X", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 12 #2",
                        List.of("X", "X", "X", "O", "-", "X", "X", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(3, 4), List.of(7, 7), List.of(11, 12)),
                        List.of(2, 1, 2),
                        List.of("X", "X", "X", "O", "O", "X", "X", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 3 #2",
                        List.of("X", "-", "-", "-", "X", "X", "O", "X", "X", "X", "-", "O", "X", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 3), List.of(6, 6), List.of(10, 11), List.of(16, 19)),
                        List.of(2, 1, 2, 2),
                        List.of("X", "-", "O", "-", "X", "X", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 4 #4",
                        List.of("X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "X", "X", "-", "-", "-"),
                        List.of(List.of(1, 11), List.of(14, 19)),
                        List.of(11, 1),
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "X", "X", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 10 #1",
                        List.of("X", "-", "X", "X", "O", "X", "X", "O", "-", "X", "X", "O", "X", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 7), List.of(7, 8), List.of(11, 19)),
                        List.of(1, 2, 1),
                        List.of("X", "-", "X", "X", "O", "X", "X", "O", "O", "X", "X", "O", "X", "X", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 6 #4",
                        List.of("X", "X", "-", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X", "-", "O", "-"),
                        List.of(List.of(1, 3), List.of(9, 10), List.of(13, 14), List.of(17, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X", "-", "O", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Row 3 #3",
                        List.of("X", "O", "O", "X", "X", "X", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-", "-", "-", "O"),
                        List.of(List.of(1, 2), List.of(6, 6), List.of(10, 11), List.of(18, 19)),
                        List.of(2, 1, 2, 2),
                        List.of("X", "O", "O", "X", "X", "X", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-", "-", "O", "O")
                )
        );
    }
}
