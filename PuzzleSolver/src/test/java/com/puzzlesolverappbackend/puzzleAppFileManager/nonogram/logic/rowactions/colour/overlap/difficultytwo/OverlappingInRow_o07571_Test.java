package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07571_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(5, 19)),
                        List.of(4, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14), List.of(10, 19)),
                        List.of(9, 4),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(3, 19)),
                        List.of(2, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(3, 19)),
                        List.of(2, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(2, 19)),
                        List.of(1, 11),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(12),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 1 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(11, 19)),
                        List.of(4, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(6, 16), List.of(10, 19)),
                        List.of(5, 2, 2),
                        List.of("-", "-", "-", "O", "O", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 18 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(6, 17), List.of(9, 19)),
                        List.of(5, 1, 1),
                        List.of("-", "-", "-", "O", "O", "-", "-", "-", "X", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 1 #3",
                        List.of("-", "-", "-", "X", "-", "-", "-", "-", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(4, 7), List.of(11, 19)),
                        List.of(4, 8),
                        List.of("-", "-", "-", "X", "O", "O", "O", "O", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 6 #2",
                        List.of("-", "-", "-", "O", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 11), List.of(10, 19)),
                        List.of(9, 4),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 10 #2",
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(3, 15)),
                        List.of(2, 10),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 18 #2",
                        List.of("-", "-", "-", "O", "O", "-", "-", "X", "X", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(6, 17), List.of(9, 19)),
                        List.of(5, 1, 1),
                        List.of("-", "-", "O", "O", "O", "-", "-", "X", "X", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 16), List.of(18, 19)),
                        List.of(7, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 15)),
                        List.of(9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 15)),
                        List.of(9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 19 #1",
                        List.of("-", "-", "X", "X", "O", "-", "-", "X", "X", "X", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(4, 6), List.of(4, 17), List.of(6, 19)),
                        List.of(3, 1, 1),
                        List.of("-", "-", "X", "X", "O", "O", "O", "X", "X", "X", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 8 #1",
                        List.of("X", "-", "O", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "O", "X", "-"),
                        List.of(List.of(2, 3), List.of(7, 9), List.of(12, 17)),
                        List.of(2, 2, 6),
                        List.of("X", "-", "O", "O", "-", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "O", "O", "O", "X", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 0 #1",
                        List.of("-", "-", "X", "-", "X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "X", "X", "X"),
                        List.of(List.of(11, 16)),
                        List.of(4),
                        List.of("-", "-", "X", "-", "X", "X", "X", "X", "X", "X", "X", "-", "-", "O", "O", "-", "-", "X", "X", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 18 #3",
                        List.of("-", "-", "O", "O", "O", "X", "X", "X", "X", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(10, 17), List.of(10, 19)),
                        List.of(5, 1, 1),
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 6 #3",
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "O", "-"),
                        List.of(List.of(0, 9), List.of(15, 19)),
                        List.of(9, 4),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 7 #1",
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-"),
                        List.of(List.of(0, 4), List.of(6, 9), List.of(14, 19)),
                        List.of(4, 3, 5),
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 9 #2",
                        List.of("X", "X", "O", "O", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X"),
                        List.of(List.of(2, 3), List.of(6, 16)),
                        List.of(2, 10),
                        List.of("X", "X", "O", "O", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 15 #1",
                        List.of("-", "O", "-", "X", "-", "O", "O", "O", "-", "-", "O", "O", "-", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 8), List.of(9, 12), List.of(16, 19)),
                        List.of(2, 4, 3, 3),
                        List.of("-", "O", "-", "X", "-", "O", "O", "O", "-", "-", "O", "O", "-", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 5 #2",
                        List.of("-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "X", "O", "O"),
                        List.of(List.of(0, 7), List.of(18, 19)),
                        List.of(7, 2),
                        List.of("-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "X", "O", "O")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 16 #1",
                        List.of("-", "O", "-", "-", "X", "-", "-", "-", "X", "-", "O", "O", "O", "-", "-", "X", "-", "O", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 8), List.of(9, 13), List.of(14, 19)),
                        List.of(3, 1, 4, 2),
                        List.of("-", "O", "O", "-", "X", "-", "-", "-", "X", "-", "O", "O", "O", "-", "-", "X", "-", "O", "-", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 15 #2",
                        List.of("-", "O", "X", "X", "-", "O", "O", "O", "-", "-", "O", "O", "-", "X", "X", "X", "-", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(4, 8), List.of(9, 12), List.of(16, 19)),
                        List.of(2, 4, 3, 3),
                        List.of("O", "O", "X", "X", "-", "O", "O", "O", "-", "-", "O", "O", "-", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o07571 / 20x20 / diff 2.0 / Row 3 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(11, 11), List.of(19, 19)),
                        List.of(1, 1),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "X", "X", "O")
                )
        );
    }
}
