package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07942_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 15), List.of(12, 17), List.of(14, 19)),
                        List.of(11, 1, 1),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(3, 13), List.of(11, 17), List.of(15, 19)),
                        List.of(2, 7, 3, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(3, 14), List.of(11, 17), List.of(14, 19)),
                        List.of(2, 7, 2, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 15 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(3, 15), List.of(11, 17), List.of(13, 19)),
                        List.of(2, 7, 1, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(3, 14), List.of(11, 19)),
                        List.of(2, 7, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 19 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(5, 19)),
                        List.of(4, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "-", "O", "-"),
                        List.of(List.of(7, 10), List.of(15, 19)),
                        List.of(3, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "O", "-", "O", "-"),
                        List.of(List.of(0, 9), List.of(2, 12), List.of(9, 14), List.of(15, 19)),
                        List.of(1, 1, 1, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 19 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(11, 19)),
                        List.of(4, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 17 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "O", "-", "-", "X", "O", "X", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(0, 5), List.of(5, 10), List.of(12, 12), List.of(14, 19)),
                        List.of(1, 4, 1, 4),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "O", "X", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o0o07942 / 20x20 / diff 2.0 / Row 18 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "O", "-", "-", "X", "O", "X", "X", "X", "-", "O", "-", "-"),
                        List.of(List.of(5, 9), List.of(12, 12), List.of(14, 19)),
                        List.of(4, 1, 4),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "O", "X", "X", "X", "O", "O", "-", "-")
                ),
                Arguments.of("o0o07942 / 20x20 / diff 2.0 / Row 18 #2",
                        List.of("X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "X", "X", "O", "X", "X", "X", "O", "O", "-", "-"),
                        List.of(List.of(5, 9), List.of(12, 12), List.of(16, 19)),
                        List.of(4, 1, 4),
                        List.of("X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "X", "X", "O", "X", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 13 #2",
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "X", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 12), List.of(12, 16), List.of(16, 19)),
                        List.of(2, 7, 3, 1),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "O", "-", "O", "X", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 14 #2",
                        List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "X", "-", "-"),
                        List.of(List.of(0, 4), List.of(6, 12), List.of(14, 16), List.of(15, 19)),
                        List.of(2, 7, 2, 1),
                        List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-", "O", "-", "X", "-", "-")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 19 #3",
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "-", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O"),
                        List.of(List.of(6, 9), List.of(12, 19)),
                        List.of(4, 8),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 11 #1",
                        List.of("X", "X", "X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "X", "-", "X", "X", "O", "X", "O", "X"),
                        List.of(List.of(5, 5), List.of(7, 11), List.of(13, 13), List.of(16, 16), List.of(18, 18)),
                        List.of(1, 5, 1, 1, 1),
                        List.of("X", "X", "X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "X", "O", "X", "X", "O", "X", "O", "X")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 8), List.of(6, 14), List.of(9, 17)),
                        List.of(2, 5, 2),
                        List.of("-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 2 #2",
                        List.of("-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "-", "-", "X", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 8), List.of(6, 12), List.of(9, 17)),
                        List.of(2, 5, 2),
                        List.of("-", "-", "-", "-", "-", "X", "-", "-", "O", "O", "O", "-", "-", "X", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 5 #1",
                        List.of("X", "-", "-", "-", "-", "X", "O", "X", "-", "O", "-", "X", "O", "X", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(6, 6), List.of(8, 10), List.of(12, 12)),
                        List.of(1, 3, 1),
                        List.of("X", "-", "-", "-", "-", "X", "O", "X", "O", "O", "O", "X", "O", "X", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o07942 / 20x20 / diff 2.0 / Row 0 #1",
                        List.of("X", "-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "O", "-", "X", "X"),
                        List.of(List.of(1, 2), List.of(16, 17)),
                        List.of(2, 2),
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "O", "O", "X", "X")
                )
        );
    }
}
