package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o06044_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 12), List.of(8, 14)),
                        List.of(7, 1),
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(9),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 4 #1",
                        List.of("-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(7),
                        List.of("-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 3 #1",
                        List.of("X", "X", "O", "O", "O", "O", "X", "-", "-", "-", "X", "-", "X", "X", "X"),
                        List.of(List.of(2, 5), List.of(7, 9)),
                        List.of(4, 3),
                        List.of("X", "X", "O", "O", "O", "O", "X", "O", "O", "O", "X", "-", "X", "X", "X")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 1 #1",
                        List.of("-", "-", "O", "-", "-", "-", "X", "X", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 5)),
                        List.of(4),
                        List.of("-", "-", "O", "O", "-", "-", "X", "X", "X", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 1 #2",
                        List.of("-", "-", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 4)),
                        List.of(4),
                        List.of("-", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 8 #1",
                        List.of("-", "-", "-", "-", "-", "O", "X", "X", "X", "X", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(5, 10), List.of(10, 14)),
                        List.of(3, 1, 3),
                        List.of("-", "-", "-", "-", "-", "O", "X", "X", "X", "X", "O", "-", "O", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 0 #1",
                        List.of("X", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(1, 3)),
                        List.of(3),
                        List.of("X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 6 #1",
                        List.of("-", "-", "-", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "-", "-"),
                        List.of(List.of(4, 6), List.of(9, 10), List.of(13, 14)),
                        List.of(3, 2, 2),
                        List.of("-", "-", "-", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "O", "O")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 9 #2",
                        List.of("X", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X"),
                        List.of(List.of(2, 12)),
                        List.of(9),
                        List.of("X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Row 7 #1",
                        List.of("O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 5), List.of(5, 10), List.of(12, 14)),
                        List.of(1, 1, 1, 2),
                        List.of("O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O", "X", "-", "O", "-")
                )
        );
    }
}
