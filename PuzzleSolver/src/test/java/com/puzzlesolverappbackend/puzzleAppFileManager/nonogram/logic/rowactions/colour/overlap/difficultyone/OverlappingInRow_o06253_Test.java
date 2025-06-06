package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o06253_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 9)),
                        List.of(3, 5),
                        List.of("-", "O", "O", "-", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 9)),
                        List.of(3, 5),
                        List.of("-", "O", "O", "-", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 7 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(7),
                        List.of("-", "-", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(7),
                        List.of("-", "-", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(3, 7), List.of(7, 9)),
                        List.of(2, 3, 1),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(3, 9)),
                        List.of(2, 4),
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(2, 6), List.of(6, 9)),
                        List.of(1, 3, 2),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 10 #2",
                        List.of("-", "O", "O", "X", "O", "O", "O", "X", "-", "X"),
                        List.of(List.of(0, 2), List.of(4, 6), List.of(8, 8)),
                        List.of(2, 3, 1),
                        List.of("-", "O", "O", "X", "O", "O", "O", "X", "O", "X")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 11 #2",
                        List.of("-", "-", "X", "-", "O", "O", "O", "-", "-", "X"),
                        List.of(List.of(0, 2), List.of(3, 7)),
                        List.of(2, 4),
                        List.of("-", "O", "X", "-", "O", "O", "O", "-", "-", "X")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 11 #3",
                        List.of("-", "O", "X", "-", "O", "O", "O", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(3, 7)),
                        List.of(2, 4),
                        List.of("O", "O", "X", "-", "O", "O", "O", "-", "X", "X")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Row 14 #1",
                        List.of("X", "-", "X", "X", "O", "O", "X", "-", "-", "X"),
                        List.of(List.of(4, 5), List.of(7, 8)),
                        List.of(2, 2),
                        List.of("X", "-", "X", "X", "O", "O", "X", "O", "O", "X")
                )
        );
    }
}
