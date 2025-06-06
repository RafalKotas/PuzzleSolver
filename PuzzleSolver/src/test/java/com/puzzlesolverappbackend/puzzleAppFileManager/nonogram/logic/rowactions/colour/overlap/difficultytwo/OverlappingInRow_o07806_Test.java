package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07806_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(2, 8), List.of(9, 13), List.of(14, 15), List.of(16, 19)),
                        List.of(1, 6, 4, 1, 3),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "O", "O", "O", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 17), List.of(16, 19)),
                        List.of(15, 1),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 5 #2",
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "X", "-", "O", "O", "O", "-", "-", "-", "-", "O", "O", "-"),
                        List.of(List.of(0, 0), List.of(2, 7), List.of(9, 13), List.of(14, 15), List.of(16, 19)),
                        List.of(1, 6, 4, 1, 3),
                        List.of("O", "-", "O", "O", "O", "O", "O", "O", "X", "-", "O", "O", "O", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 18 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(7, 15)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 19 #1",
                        List.of("X", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-"),
                        List.of(List.of(7, 15)),
                        List.of(7),
                        List.of("X", "X", "-", "-", "-", "X", "X", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 17 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(7, 15)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 2 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 11), List.of(6, 14), List.of(9, 19)),
                        List.of(3, 1, 2, 4),
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 4 #1",
                        List.of("O", "-", "-", "X", "-", "-", "-", "X", "-", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 15), List.of(13, 19)),
                        List.of(2, 2, 3),
                        List.of("O", "O", "-", "X", "-", "-", "-", "X", "-", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 8 #1",
                        List.of("-", "O", "X", "X", "-", "-", "-", "X", "X", "O", "O", "X", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 6), List.of(9, 10), List.of(9, 14), List.of(12, 19)),
                        List.of(2, 2, 2, 2, 4),
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "X", "O", "O", "X", "-", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 8 #2",
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "X", "O", "O", "X", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 6), List.of(9, 10), List.of(12, 14), List.of(15, 19)),
                        List.of(2, 2, 2, 2, 4),
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "X", "O", "O", "X", "-", "O", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 17 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(8, 15)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 18 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(8, 15)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 19 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(8, 15)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 4 #2",
                        List.of("O", "O", "X", "X", "-", "X", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-", "O", "-", "-", "X"),
                        List.of(List.of(0, 1), List.of(13, 14), List.of(14, 18)),
                        List.of(2, 2, 3),
                        List.of("O", "O", "X", "X", "-", "X", "-", "X", "X", "X", "X", "X", "X", "O", "O", "-", "O", "-", "-", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 0 #1",
                        List.of("X", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-"),
                        List.of(List.of(3, 13)),
                        List.of(8),
                        List.of("X", "X", "X", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "X", "-", "X", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 5 #3",
                        List.of("O", "X", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "-", "X", "O", "O", "O", "X"),
                        List.of(List.of(0, 0), List.of(2, 7), List.of(9, 12), List.of(14, 14), List.of(16, 18)),
                        List.of(1, 6, 4, 1, 3),
                        List.of("O", "X", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O", "X", "O", "O", "O", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 1 #1",
                        List.of("X", "X", "O", "O", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-"),
                        List.of(List.of(2, 4), List.of(6, 13), List.of(12, 19)),
                        List.of(3, 5, 1),
                        List.of("X", "X", "O", "O", "O", "-", "-", "-", "-", "O", "O", "-", "-", "-", "X", "-", "X", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 2 #2",
                        List.of("X", "O", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 3), List.of(4, 9), List.of(6, 14), List.of(11, 19)),
                        List.of(3, 1, 2, 4),
                        List.of("X", "O", "O", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 1 #2",
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "O", "O", "O", "-", "-", "-", "X", "-", "X", "-", "-", "-"),
                        List.of(List.of(2, 4), List.of(8, 12), List.of(12, 19)),
                        List.of(3, 5, 1),
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "-", "X", "-", "X", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 17 #3",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X"),
                        List.of(List.of(9, 15)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 18 #3",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X"),
                        List.of(List.of(9, 15)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 19 #3",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X"),
                        List.of(List.of(9, 15)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 2 #3",
                        List.of("X", "O", "O", "O", "X", "-", "X", "X", "X", "O", "X", "X", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 3), List.of(5, 9), List.of(12, 14), List.of(15, 19)),
                        List.of(3, 1, 2, 4),
                        List.of("X", "O", "O", "O", "X", "-", "X", "X", "X", "O", "X", "X", "-", "O", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 8 #3",
                        List.of("O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O", "X", "X", "O", "-", "X", "O", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(4, 5), List.of(9, 10), List.of(13, 14), List.of(16, 19)),
                        List.of(2, 2, 2, 2, 4),
                        List.of("O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 1 #3",
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "-"),
                        List.of(List.of(2, 4), List.of(8, 12), List.of(19, 19)),
                        List.of(3, 5, 1),
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Row 2 #4",
                        List.of("X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "O", "O", "-", "X", "O", "O", "O", "-"),
                        List.of(List.of(1, 3), List.of(9, 9), List.of(12, 14), List.of(16, 19)),
                        List.of(3, 1, 2, 4),
                        List.of("X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "O", "O", "-", "X", "O", "O", "O", "O")
                )
        );
    }
}
