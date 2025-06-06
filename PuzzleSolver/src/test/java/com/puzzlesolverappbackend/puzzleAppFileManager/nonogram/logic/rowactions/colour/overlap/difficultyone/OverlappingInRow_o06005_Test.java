package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o06005_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 2 #1",
                        List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(2, 7), List.of(8, 9)),
                        List.of(1, 5, 1),
                        List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 3 #1",
                        List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(9),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 4 #1",
                        List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(9),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 5 #1",
                        List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(8, 9)),
                        List.of(7, 1),
                        List.of("-", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 6 #1",
                        List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(9),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 1 #1",
                        List.of("-", "-", "O", "X", "-", "X", "O", "X", "-", "X"),
                        List.of(List.of(0, 0), List.of(2, 2), List.of(4, 4), List.of(6, 6), List.of(8, 9)),
                        List.of(1, 1, 1, 1, 1),
                        List.of("O", "-", "O", "X", "O", "X", "O", "X", "-", "X")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 2 #2",
                        List.of("-", "-", "O", "O", "O", "O", "O", "X", "-", "X"),
                        List.of(List.of(0, 0), List.of(2, 6), List.of(8, 9)),
                        List.of(1, 5, 1),
                        List.of("O", "-", "O", "O", "O", "O", "O", "X", "-", "X")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 9 #1",
                        List.of("X", "-", "O", "X", "X", "X", "O", "O", "X", "X"),
                        List.of(List.of(1, 2), List.of(6, 7)),
                        List.of(2, 2),
                        List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 1 #2",
                        List.of("O", "X", "O", "X", "O", "X", "O", "X", "-", "X"),
                        List.of(List.of(0, 0), List.of(2, 2), List.of(4, 4), List.of(6, 6), List.of(8, 8)),
                        List.of(1, 1, 1, 1, 1),
                        List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 2 #3",
                        List.of("O", "X", "O", "O", "O", "O", "O", "X", "-", "X"),
                        List.of(List.of(0, 0), List.of(2, 6), List.of(8, 8)),
                        List.of(1, 5, 1),
                        List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Row 0 #1",
                        List.of("X", "X", "O", "X", "-", "X", "O", "X", "X", "X"),
                        List.of(List.of(2, 2), List.of(4, 4), List.of(6, 6)),
                        List.of(1, 1, 1),
                        List.of("X", "X", "O", "X", "O", "X", "O", "X", "X", "X")
                )
        );
    }
}
