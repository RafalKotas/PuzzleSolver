package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o11517_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(2, 19)),
                        List.of(1, 12),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(5, 11), List.of(8, 19)),
                        List.of(4, 2, 7),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 6 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(5, 12), List.of(8, 19)),
                        List.of(4, 2, 6),
                        List.of("-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 7 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 17), List.of(8, 19)),
                        List.of(3, 3, 1),
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 8 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 14), List.of(9, 19)),
                        List.of(3, 4, 4),
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(2, 10), List.of(6, 12), List.of(14, 19)),
                        List.of(1, 3, 2, 5),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 4 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "-"),
                        List.of(List.of(0, 6), List.of(2, 17)),
                        List.of(1, 12),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-"),
                        List.of(List.of(0, 10), List.of(6, 12), List.of(8, 17)),
                        List.of(5, 1, 6),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 6 #2",
                        List.of("-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-"),
                        List.of(List.of(0, 4), List.of(5, 12), List.of(8, 17)),
                        List.of(4, 2, 6),
                        List.of("-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-"),
                        List.of(List.of(0, 6), List.of(3, 14), List.of(11, 17)),
                        List.of(2, 7, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 5 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "-"),
                        List.of(List.of(0, 8), List.of(6, 10), List.of(8, 17)),
                        List.of(5, 1, 6),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 3 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "X", "-", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 6), List.of(3, 11), List.of(13, 17)),
                        List.of(2, 7, 2),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "-", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 10)),
                        List.of(6),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 5 #3",
                        List.of("-", "-", "-", "-", "O", "-", "-", "O", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 7), List.of(7, 9), List.of(9, 16)),
                        List.of(5, 1, 6),
                        List.of("-", "-", "-", "O", "O", "-", "-", "O", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 5 #4",
                        List.of("-", "-", "-", "O", "O", "X", "-", "O", "X", "-", "-", "O", "O", "O", "O", "-", "-", "X", "X", "X"),
                        List.of(List.of(0, 4), List.of(7, 9), List.of(9, 16)),
                        List.of(5, 1, 6),
                        List.of("O", "O", "O", "O", "O", "X", "-", "O", "X", "-", "-", "O", "O", "O", "O", "-", "-", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "-", "X", "X", "X", "X", "-", "-", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 12), List.of(10, 12)),
                        List.of(2, 3),
                        List.of("-", "-", "-", "-", "-", "X", "X", "X", "X", "-", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 4 #3",
                        List.of("-", "-", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X"),
                        List.of(List.of(0, 4), List.of(4, 16)),
                        List.of(1, 12),
                        List.of("-", "-", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 3 #3",
                        List.of("X", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "X", "X", "X"),
                        List.of(List.of(1, 3), List.of(5, 11), List.of(13, 14)),
                        List.of(2, 7, 2),
                        List.of("X", "-", "O", "-", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 16 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X", "-", "-", "X", "-", "X", "X"),
                        List.of(List.of(0, 3), List.of(4, 12), List.of(9, 15)),
                        List.of(3, 3, 2),
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X", "-", "-", "X", "-", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 18 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "X", "-", "X", "X"),
                        List.of(List.of(0, 3), List.of(2, 8), List.of(7, 12)),
                        List.of(1, 4, 3),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "X", "-", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 14 #1",
                        List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 5), List.of(5, 12), List.of(8, 12)),
                        List.of(4, 2, 2),
                        List.of("-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 15 #1",
                        List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(0, 5), List.of(5, 11), List.of(9, 19)),
                        List.of(4, 2, 2),
                        List.of("-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 8 #2",
                        List.of("O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "O", "O", "-", "X"),
                        List.of(List.of(0, 2), List.of(4, 12), List.of(14, 18)),
                        List.of(3, 4, 4),
                        List.of("O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "O", "O", "O", "-", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 7 #2",
                        List.of("O", "O", "O", "X", "-", "X", "-", "X", "-", "-", "-", "X", "X", "X", "X", "X", "O", "X", "X", "X"),
                        List.of(List.of(0, 2), List.of(8, 10), List.of(16, 16)),
                        List.of(3, 3, 1),
                        List.of("O", "O", "O", "X", "-", "X", "-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 8 #3",
                        List.of("O", "O", "O", "X", "-", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "O", "O", "O", "-", "X"),
                        List.of(List.of(0, 2), List.of(6, 12), List.of(14, 18)),
                        List.of(3, 4, 4),
                        List.of("O", "O", "O", "X", "-", "X", "-", "-", "-", "O", "-", "-", "-", "X", "-", "O", "O", "O", "-", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 6 #3",
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "-", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X"),
                        List.of(List.of(0, 3), List.of(7, 8), List.of(11, 16)),
                        List.of(4, 2, 6),
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 19 #1",
                        List.of("X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 8), List.of(5, 12)),
                        List.of(4, 3),
                        List.of("X", "X", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 15 #2",
                        List.of("O", "O", "O", "O", "X", "X", "-", "-", "-", "-", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 3), List.of(6, 8), List.of(9, 11)),
                        List.of(4, 2, 2),
                        List.of("O", "O", "O", "O", "X", "X", "-", "O", "-", "-", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 18 #2",
                        List.of("O", "X", "-", "O", "O", "O", "-", "X", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 0), List.of(2, 6), List.of(8, 12)),
                        List.of(1, 4, 3),
                        List.of("O", "X", "-", "O", "O", "O", "-", "X", "-", "-", "O", "-", "-", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 19 #2",
                        List.of("X", "X", "-", "O", "O", "O", "-", "X", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 6), List.of(8, 12)),
                        List.of(4, 3),
                        List.of("X", "X", "-", "O", "O", "O", "-", "X", "-", "-", "O", "-", "-", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 13 #2",
                        List.of("X", "X", "O", "O", "O", "O", "X", "X", "-", "-", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X"),
                        List.of(List.of(2, 5), List.of(8, 9), List.of(12, 18)),
                        List.of(4, 2, 7),
                        List.of("X", "X", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 14 #2",
                        List.of("X", "O", "O", "O", "O", "X", "-", "O", "-", "-", "X", "-", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(1, 4), List.of(6, 8), List.of(11, 12)),
                        List.of(4, 2, 2),
                        List.of("X", "O", "O", "O", "O", "X", "-", "O", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o11517 / 20x20 / diff 3.0 / Row 10 #1",
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "X", "X", "O", "-", "X", "X", "X", "X", "X", "X", "O", "O"),
                        List.of(List.of(0, 1), List.of(5, 7), List.of(10, 11), List.of(18, 19)),
                        List.of(2, 3, 2, 2),
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O")
                )
        );
    }
}
