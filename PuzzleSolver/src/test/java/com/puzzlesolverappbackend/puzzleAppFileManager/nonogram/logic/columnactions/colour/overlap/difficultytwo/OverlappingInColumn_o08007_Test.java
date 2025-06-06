package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o08007_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(5, 11), List.of(11, 14)),
                        List.of(4, 5, 2),
                        List.of("-", "-", "O", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(3, 5), List.of(6, 8), List.of(9, 11), List.of(12, 14)),
                        List.of(2, 2, 2, 2, 2),
                        List.of("-", "O", "-", "-", "O", "-", "-", "O", "-", "O", "O", "-", "-", "O", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 3 #1",
                        List.of("-", "-", "X", "-", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(3, 7), List.of(9, 10), List.of(12, 14)),
                        List.of(2, 3, 2, 1),
                        List.of("-", "-", "X", "-", "-", "O", "-", "-", "X", "O", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 3 #2",
                        List.of("-", "-", "X", "-", "-", "O", "-", "-", "X", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(3, 7), List.of(9, 10), List.of(12, 14)),
                        List.of(2, 3, 2, 1),
                        List.of("O", "O", "X", "-", "-", "O", "-", "-", "X", "O", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "O", "-"),
                        List.of(List.of(0, 6), List.of(7, 11), List.of(12, 14)),
                        List.of(3, 4, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "O", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 2 #2",
                        List.of("-", "O", "O", "O", "-", "X", "-", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(6, 11), List.of(12, 14)),
                        List.of(4, 5, 2),
                        List.of("-", "O", "O", "O", "-", "X", "-", "O", "O", "O", "O", "-", "-", "O", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 2 #3",
                        List.of("X", "O", "O", "O", "-", "X", "-", "O", "O", "O", "O", "-", "-", "O", "-"),
                        List.of(List.of(1, 4), List.of(6, 11), List.of(12, 14)),
                        List.of(4, 5, 2),
                        List.of("X", "O", "O", "O", "O", "X", "-", "O", "O", "O", "O", "-", "-", "O", "-")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 0 #1",
                        List.of("X", "X", "-", "-", "X", "-", "-", "-", "X", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(9, 12)),
                        List.of(4),
                        List.of("X", "X", "-", "-", "X", "-", "-", "-", "X", "O", "O", "O", "O", "X", "X")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 2 #4",
                        List.of("X", "O", "O", "O", "O", "X", "-", "O", "O", "O", "O", "-", "X", "O", "-"),
                        List.of(List.of(1, 4), List.of(6, 11), List.of(13, 14)),
                        List.of(4, 5, 2),
                        List.of("X", "O", "O", "O", "O", "X", "-", "O", "O", "O", "O", "-", "X", "O", "O")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 3 #3",
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "X", "O", "O", "X", "X", "X", "O"),
                        List.of(List.of(0, 1), List.of(4, 6), List.of(9, 10), List.of(14, 14)),
                        List.of(2, 3, 2, 1),
                        List.of("O", "O", "X", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 5 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "X", "X", "O", "O"),
                        List.of(List.of(0, 6), List.of(7, 10), List.of(13, 14)),
                        List.of(3, 4, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "X", "X", "O", "O")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "X", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 5)),
                        List.of(4),
                        List.of("-", "-", "O", "O", "-", "-", "X", "-", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 12 #1",
                        List.of("O", "-", "-", "X", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 2)),
                        List.of(3),
                        List.of("O", "O", "O", "X", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o08007 / 15x15 / diff 2.0 / Column 14 #1",
                        List.of("-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 0)),
                        List.of(1),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                )
        );
    }
}