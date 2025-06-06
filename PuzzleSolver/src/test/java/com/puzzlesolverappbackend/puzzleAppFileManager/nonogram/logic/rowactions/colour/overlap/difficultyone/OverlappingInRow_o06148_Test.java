package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o06148_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o06148 / 10x15 / diff 1.0 / Row 0 #1",
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(5, 14)),
                        List.of(4, 7),
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Row 1 #1",
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(14),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Row 2 #1",
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(5, 12), List.of(12, 14)),
                        List.of(4, 6, 1),
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Row 6 #1",
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(5, 10), List.of(10, 14)),
                        List.of(4, 4, 3),
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "O", "-", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Row 8 #1",
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "X", "X", "-", "-", "-", "-", "X"),
                        List.of(List.of(0, 3), List.of(7, 7), List.of(10, 13)),
                        List.of(4, 1, 4),
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "X", "X", "O", "O", "O", "O", "X")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Row 9 #1",
                        List.of("-", "O", "O", "O", "X", "X", "X", "O", "X", "X", "-", "-", "-", "-", "X"),
                        List.of(List.of(0, 3), List.of(7, 7), List.of(7, 13)),
                        List.of(4, 1, 1),
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "X", "X", "-", "-", "-", "-", "X")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Row 5 #1",
                        List.of("O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "-", "-", "X", "-", "-"),
                        List.of(List.of(0, 3), List.of(5, 7), List.of(10, 11), List.of(13, 14)),
                        List.of(4, 3, 1, 2),
                        List.of("O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "-", "-", "X", "O", "O")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Row 0 #2",
                        List.of("-", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "-", "X", "X"),
                        List.of(List.of(0, 4), List.of(6, 12)),
                        List.of(4, 7),
                        List.of("-", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Row 9 #2",
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "X", "X", "-", "X", "X", "X", "X"),
                        List.of(List.of(0, 3), List.of(7, 7), List.of(10, 10)),
                        List.of(4, 1, 1),
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "X", "X", "O", "X", "X", "X", "X")
                )
        );
    }
}
