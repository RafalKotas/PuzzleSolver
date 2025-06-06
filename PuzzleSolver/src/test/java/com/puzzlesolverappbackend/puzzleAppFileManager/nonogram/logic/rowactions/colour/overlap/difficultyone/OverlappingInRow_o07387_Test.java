package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07387_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(2, 14)),
                        List.of(1, 10),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(2, 14)),
                        List.of(1, 10),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-"),
                        List.of(List.of(0, 5)),
                        List.of(4),
                        List.of("-", "-", "O", "O", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 8 #1",
                        List.of("-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-"),
                        List.of(List.of(0, 5)),
                        List.of(4),
                        List.of("-", "-", "O", "O", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 3 #1",
                        List.of("O", "-", "X", "X", "-", "-", "-", "X", "O", "X", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 6), List.of(5, 8), List.of(8, 10), List.of(10, 12), List.of(12, 14)),
                        List.of(1, 2, 1, 1, 1, 1),
                        List.of("O", "-", "X", "X", "-", "O", "-", "X", "O", "X", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 4 #2",
                        List.of("O", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(4, 14)),
                        List.of(1, 10),
                        List.of("O", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 5 #2",
                        List.of("O", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(4, 14)),
                        List.of(1, 10),
                        List.of("O", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 1 #1",
                        List.of("O", "-", "X", "X", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 14)),
                        List.of(2, 2),
                        List.of("O", "O", "X", "X", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 8 #2",
                        List.of("X", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(1, 4)),
                        List.of(4),
                        List.of("X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 3 #2",
                        List.of("O", "X", "X", "X", "-", "O", "-", "X", "O", "X", "O", "X", "-", "X", "-"),
                        List.of(List.of(0, 0), List.of(4, 6), List.of(8, 8), List.of(10, 10), List.of(12, 12), List.of(14, 14)),
                        List.of(1, 2, 1, 1, 1, 1),
                        List.of("O", "X", "X", "X", "-", "O", "-", "X", "O", "X", "O", "X", "O", "X", "O")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 1 #2",
                        List.of("O", "O", "X", "X", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(4, 5)),
                        List.of(2, 2),
                        List.of("O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Row 6 #1",
                        List.of("-", "-", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 2), List.of(4, 5)),
                        List.of(2, 2),
                        List.of("-", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                )
        );
    }
}
