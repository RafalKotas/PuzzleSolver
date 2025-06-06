package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o01131_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(8),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 5), List.of(5, 9)),
                        List.of(4, 3),
                        List.of("-", "-", "O", "O", "-", "-", "-", "O", "-", "O")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O"),
                        List.of(List.of(0, 4), List.of(3, 9)),
                        List.of(2, 4),
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "O", "O")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 8 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 1), List.of(2, 5), List.of(6, 9)),
                        List.of(1, 3, 3),
                        List.of("O", "-", "-", "O", "O", "-", "-", "O", "O", "O")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 1), List.of(2, 4), List.of(5, 7), List.of(8, 9)),
                        List.of(1, 2, 2, 1),
                        List.of("-", "-", "-", "O", "O", "-", "O", "-", "-", "O")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(7, 9)),
                        List.of(6, 1),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 5 #1",
                        List.of("-", "-", "O", "X", "-", "-", "-", "X", "O", "O"),
                        List.of(List.of(1, 3), List.of(4, 6), List.of(8, 9)),
                        List.of(2, 2, 2),
                        List.of("-", "-", "O", "X", "-", "O", "-", "X", "O", "O")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 10 #1",
                        List.of("-", "-", "-", "X", "-", "-", "O", "X", "X", "O"),
                        List.of(List.of(0, 3), List.of(4, 7), List.of(9, 9)),
                        List.of(1, 3, 1),
                        List.of("-", "-", "-", "X", "-", "O", "O", "X", "X", "O")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 7 #1",
                        List.of("-", "-", "X", "X", "-", "X", "X", "O", "O", "O"),
                        List.of(List.of(0, 1), List.of(4, 4), List.of(7, 9)),
                        List.of(1, 1, 3),
                        List.of("-", "-", "X", "X", "O", "X", "X", "O", "O", "O")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 12 #1",
                        List.of("O", "-", "X", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(5, 9)),
                        List.of(2, 2),
                        List.of("O", "O", "X", "X", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 1 #1",
                        List.of("-", "-", "X", "X", "O", "X", "O", "X", "X", "-"),
                        List.of(List.of(0, 1), List.of(4, 4), List.of(6, 6)),
                        List.of(2, 1, 1),
                        List.of("O", "O", "X", "X", "O", "X", "O", "X", "X", "-")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 6 #2",
                        List.of("-", "-", "X", "X", "-", "X", "O", "O", "O", "O"),
                        List.of(List.of(0, 1), List.of(6, 9)),
                        List.of(2, 4),
                        List.of("O", "O", "X", "X", "-", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 13 #1",
                        List.of("X", "O", "O", "X", "X", "X", "-", "-", "O", "-"),
                        List.of(List.of(1, 2), List.of(6, 9)),
                        List.of(2, 3),
                        List.of("X", "O", "O", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o01131 / 10x15 / diff 1.0 / Column 12 #2",
                        List.of("O", "O", "X", "X", "X", "X", "-", "-", "-", "O"),
                        List.of(List.of(0, 1), List.of(8, 9)),
                        List.of(2, 2),
                        List.of("O", "O", "X", "X", "X", "X", "-", "-", "O", "O")
                )
        );
    }
}