package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07490_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 17), List.of(13, 19)),
                        List.of(12, 1),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(2, 8), List.of(4, 10), List.of(6, 19)),
                        List.of(1, 1, 1, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 15 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 11), List.of(8, 19)),
                        List.of(7, 7),
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 12)),
                        List.of(9),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(7, 19)),
                        List.of(8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 16 #1",
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 9), List.of(8, 19)),
                        List.of(5, 7),
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 12), List.of(10, 19)),
                        List.of(5, 6),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 15 #2",
                        List.of("X", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 11), List.of(8, 19)),
                        List.of(7, 7),
                        List.of("X", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 15 #3",
                        List.of("X", "-", "O", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 11), List.of(9, 19)),
                        List.of(7, 7),
                        List.of("X", "-", "O", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 11 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(10, 19)),
                        List.of(5, 6),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 10 #2",
                        List.of("-", "-", "-", "-", "O", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 15), List.of(15, 19)),
                        List.of(12, 1),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 17 #1",
                        List.of("X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-"),
                        List.of(List.of(1, 7), List.of(12, 19)),
                        List.of(6, 6),
                        List.of("X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 10 #3",
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "X", "-", "-", "-"),
                        List.of(List.of(1, 15), List.of(15, 19)),
                        List.of(12, 1),
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 11 #3",
                        List.of("X", "-", "-", "-", "O", "-", "-", "-", "-", "X", "X", "-", "-", "-", "O", "O", "O", "X", "-", "-"),
                        List.of(List.of(1, 8), List.of(11, 16)),
                        List.of(5, 6),
                        List.of("X", "-", "-", "-", "O", "O", "-", "-", "-", "X", "X", "O", "O", "O", "O", "O", "O", "X", "-", "-")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 8), List.of(9, 11)),
                        List.of(3, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 5 #1",
                        List.of("-", "O", "-", "X", "X", "-", "-", "-", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 2), List.of(5, 9), List.of(10, 12)),
                        List.of(2, 3, 2),
                        List.of("-", "O", "-", "X", "X", "-", "-", "O", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 8)),
                        List.of(5),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 5 #2",
                        List.of("-", "O", "X", "X", "X", "-", "-", "O", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(5, 9), List.of(11, 12)),
                        List.of(2, 3, 2),
                        List.of("O", "O", "X", "X", "X", "-", "-", "O", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 6 #1",
                        List.of("-", "O", "X", "X", "X", "-", "-", "O", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(6, 8), List.of(11, 12)),
                        List.of(2, 2, 2),
                        List.of("O", "O", "X", "X", "X", "-", "-", "O", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 7 #1",
                        List.of("-", "O", "X", "X", "X", "-", "-", "X", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(5, 9), List.of(11, 12)),
                        List.of(2, 2, 2),
                        List.of("O", "O", "X", "X", "X", "-", "-", "X", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 4 #1",
                        List.of("-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(11, 12)),
                        List.of(2, 2),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 11 #4",
                        List.of("X", "-", "-", "X", "O", "O", "O", "O", "-", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X"),
                        List.of(List.of(4, 8), List.of(11, 16)),
                        List.of(5, 6),
                        List.of("X", "-", "-", "X", "O", "O", "O", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 3 #1",
                        List.of("X", "O", "-", "X", "X", "X", "X", "X", "-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(1, 2), List.of(10, 12)),
                        List.of(2, 3),
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 8 #1",
                        List.of("X", "O", "-", "X", "X", "O", "-", "X", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(1, 2), List.of(5, 8), List.of(10, 11)),
                        List.of(2, 1, 2),
                        List.of("X", "O", "O", "X", "X", "O", "-", "X", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 15 #4",
                        List.of("X", "-", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O"),
                        List.of(List.of(1, 7), List.of(13, 19)),
                        List.of(7, 7),
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 5 #3",
                        List.of("O", "O", "X", "X", "X", "X", "-", "O", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(6, 9), List.of(11, 12)),
                        List.of(2, 3, 2),
                        List.of("O", "O", "X", "X", "X", "X", "-", "O", "O", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 14 #2",
                        List.of("X", "X", "O", "-", "-", "-", "-", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O"),
                        List.of(List.of(2, 2), List.of(4, 4), List.of(5, 6), List.of(12, 19)),
                        List.of(1, 1, 1, 8),
                        List.of("X", "X", "O", "-", "O", "-", "-", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 14 #3",
                        List.of("X", "X", "O", "X", "O", "X", "-", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O"),
                        List.of(List.of(2, 2), List.of(4, 4), List.of(6, 6), List.of(12, 19)),
                        List.of(1, 1, 1, 8),
                        List.of("X", "X", "O", "X", "O", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07490 / 20x20 / diff 3.0 / Row 18 #1",
                        List.of("X", "X", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O"),
                        List.of(List.of(2, 7), List.of(15, 19)),
                        List.of(6, 5),
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O")
                )
        );
    }
}
