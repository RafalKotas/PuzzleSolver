package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o08007_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(9),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(9),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 9 #2",
                        List.of("-", "-", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10)),
                        List.of(9),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 10 #2",
                        List.of("-", "-", "-", "O", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 11)),
                        List.of(9),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 14 #1",
                        List.of("X", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-"),
                        List.of(List.of(1, 6)),
                        List.of(4),
                        List.of("X", "-", "-", "O", "O", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 9 #3",
                        List.of("O", "-", "O", "O", "O", "O", "O", "O", "O", "X", "-", "X", "X", "X", "X"),
                        List.of(List.of(0, 8)),
                        List.of(9),
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 10 #3",
                        List.of("O", "-", "O", "O", "O", "O", "O", "O", "O", "X", "-", "X", "X", "X", "X"),
                        List.of(List.of(0, 8)),
                        List.of(9),
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 12 #1",
                        List.of("O", "-", "-", "X", "X", "-", "-", "-", "X", "-", "-", "X", "X", "-", "-"),
                        List.of(List.of(0, 1), List.of(5, 14)),
                        List.of(2, 2),
                        List.of("O", "O", "-", "X", "X", "-", "-", "-", "X", "-", "-", "X", "X", "-", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 0 #1",
                        List.of("X", "X", "X", "O", "X", "-", "O", "-", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(3, 3), List.of(5, 7), List.of(11, 14)),
                        List.of(1, 2, 3),
                        List.of("X", "X", "X", "O", "X", "-", "O", "-", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Row 3 #1",
                        List.of("X", "X", "O", "X", "X", "-", "O", "X", "X", "X", "O", "O", "-", "-", "-"),
                        List.of(List.of(2, 2), List.of(5, 6), List.of(10, 14)),
                        List.of(1, 2, 2),
                        List.of("X", "X", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O", "-", "-", "-")
                )
        );
    }
}
