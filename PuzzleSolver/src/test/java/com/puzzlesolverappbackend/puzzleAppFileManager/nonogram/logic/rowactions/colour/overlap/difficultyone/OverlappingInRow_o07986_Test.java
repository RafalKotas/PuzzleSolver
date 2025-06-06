package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07986_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(2, 8), List.of(7, 10), List.of(9, 12), List.of(11, 14)),
                        List.of(1, 4, 1, 1, 1),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(5, 8), List.of(7, 10), List.of(9, 12), List.of(11, 14)),
                        List.of(4, 1, 1, 1, 1),
                        List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 7 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 6), List.of(6, 8), List.of(8, 10), List.of(10, 12), List.of(12, 14)),
                        List.of(3, 1, 1, 1, 1, 1),
                        List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 8 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(3, 4), List.of(5, 6), List.of(7, 8), List.of(9, 10), List.of(11, 12), List.of(13, 14)),
                        List.of(2, 1, 1, 1, 1, 1, 1),
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 8 #2",
                        List.of("O", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X", "-", "X", "-", "X"),
                        List.of(List.of(0, 1), List.of(3, 3), List.of(5, 5), List.of(7, 7), List.of(9, 9), List.of(11, 11), List.of(13, 13)),
                        List.of(2, 1, 1, 1, 1, 1, 1),
                        List.of("O", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "X", "X", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(2, 5), List.of(8, 12), List.of(11, 14)),
                        List.of(1, 2, 4, 1),
                        List.of("-", "-", "-", "-", "-", "-", "X", "X", "-", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(4, 8), List.of(10, 14)),
                        List.of(3, 2, 4),
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "X", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 2 #2",
                        List.of("-", "-", "-", "-", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O", "X"),
                        List.of(List.of(0, 3), List.of(5, 6), List.of(10, 13)),
                        List.of(3, 2, 4),
                        List.of("-", "O", "O", "-", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O", "X")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 5 #2",
                        List.of("-", "X", "X", "X", "O", "O", "O", "O", "X", "O", "X", "O", "X", "O", "X"),
                        List.of(List.of(0, 0), List.of(4, 7), List.of(9, 9), List.of(11, 11), List.of(13, 13)),
                        List.of(1, 4, 1, 1, 1),
                        List.of("O", "X", "X", "X", "O", "O", "O", "O", "X", "O", "X", "O", "X", "O", "X")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 1 #1",
                        List.of("-", "-", "X", "-", "-", "O", "X", "-", "-", "-", "X", "X", "O", "O", "O"),
                        List.of(List.of(0, 3), List.of(3, 5), List.of(7, 9), List.of(12, 14)),
                        List.of(1, 1, 2, 3),
                        List.of("-", "-", "X", "-", "-", "O", "X", "-", "O", "-", "X", "X", "O", "O", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 4 #1",
                        List.of("X", "-", "-", "-", "X", "X", "O", "O", "O", "O", "X", "O", "X", "O", "X"),
                        List.of(List.of(1, 3), List.of(6, 9), List.of(11, 11), List.of(13, 13)),
                        List.of(2, 4, 1, 1),
                        List.of("X", "-", "O", "-", "X", "X", "O", "O", "O", "O", "X", "O", "X", "O", "X")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 0 #1",
                        List.of("X", "X", "X", "-", "O", "-", "O", "-", "X", "-", "-", "X", "X", "X", "O"),
                        List.of(List.of(3, 6), List.of(6, 10), List.of(14, 14)),
                        List.of(3, 2, 1),
                        List.of("X", "X", "X", "-", "O", "O", "O", "-", "X", "-", "-", "X", "X", "X", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Row 0 #2",
                        List.of("X", "X", "X", "-", "O", "O", "O", "-", "X", "-", "-", "X", "X", "X", "O"),
                        List.of(List.of(4, 6), List.of(9, 10), List.of(14, 14)),
                        List.of(3, 2, 1),
                        List.of("X", "X", "X", "-", "O", "O", "O", "-", "X", "O", "O", "X", "X", "X", "O")
                )
        );
    }
}
