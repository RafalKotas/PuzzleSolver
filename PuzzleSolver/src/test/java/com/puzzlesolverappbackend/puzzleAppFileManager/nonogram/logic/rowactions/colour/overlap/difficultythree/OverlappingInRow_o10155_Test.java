package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o10155_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(2, 14)),
                        List.of(1, 8),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(7, 12), List.of(9, 14)),
                        List.of(6, 1, 1),
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 7 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(2, 11), List.of(9, 14)),
                        List.of(1, 6, 2),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 8 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 12), List.of(11, 14)),
                        List.of(10, 1),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(7, 14)),
                        List.of(6, 3),
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(10),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(10),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(5, 10), List.of(10, 14)),
                        List.of(4, 4, 3),
                        List.of("-", "-", "O", "O", "-", "-", "O", "O", "O", "-", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 15 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(7, 14)),
                        List.of(6, 6),
                        List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(10),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 3 #1",
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 9), List.of(10, 14)),
                        List.of(1, 5, 3),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "X", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 7 #2",
                        List.of("-", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(3, 3), List.of(5, 10), List.of(12, 14)),
                        List.of(1, 6, 2),
                        List.of("-", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "X", "-", "O", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 2 #1",
                        List.of("-", "X", "O", "X", "X", "O", "O", "O", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 2), List.of(5, 7), List.of(10, 11), List.of(13, 14)),
                        List.of(1, 3, 2, 2),
                        List.of("-", "X", "O", "X", "X", "O", "O", "O", "X", "X", "O", "O", "-", "O", "O")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 8 #2",
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(2, 11), List.of(12, 14)),
                        List.of(10, 1),
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "X", "X", "O", "X", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(6, 10), List.of(10, 14)),
                        List.of(3, 1, 3),
                        List.of("-", "O", "O", "-", "X", "X", "O", "X", "X", "X", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 3 #2",
                        List.of("-", "-", "-", "X", "O", "O", "O", "O", "O", "X", "X", "-", "O", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 8), List.of(11, 14)),
                        List.of(1, 5, 3),
                        List.of("-", "-", "-", "X", "O", "O", "O", "O", "O", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 1 #2",
                        List.of("X", "O", "O", "-", "X", "X", "O", "X", "X", "X", "X", "O", "O", "O", "X"),
                        List.of(List.of(1, 3), List.of(6, 6), List.of(11, 13)),
                        List.of(3, 1, 3),
                        List.of("X", "O", "O", "O", "X", "X", "O", "X", "X", "X", "X", "O", "O", "O", "X")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Row 4 #1",
                        List.of("O", "-", "X", "X", "O", "O", "X", "O", "O", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(0, 1), List.of(4, 5), List.of(7, 8), List.of(12, 12)),
                        List.of(2, 2, 2, 1),
                        List.of("O", "O", "X", "X", "O", "O", "X", "O", "O", "X", "X", "X", "O", "X", "X")
                )
        );
    }
}
