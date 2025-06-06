package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o06044_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(5, 9)),
                        List.of(4, 2),
                        List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(7, 9)),
                        List.of(6, 1),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(7, 9)),
                        List.of(6, 1),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(7),
                        List.of("-", "-", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 7 #1",
                        List.of("-", "-", "X", "-", "-", "-", "-", "-", "X", "O"),
                        List.of(List.of(3, 7), List.of(9, 9)),
                        List.of(3, 1),
                        List.of("-", "-", "X", "-", "-", "O", "-", "-", "X", "O")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 8 #1",
                        List.of("-", "-", "X", "-", "-", "-", "-", "-", "X", "O"),
                        List.of(List.of(3, 7), List.of(9, 9)),
                        List.of(3, 1),
                        List.of("-", "-", "X", "-", "-", "O", "-", "-", "X", "O")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 9 #1",
                        List.of("-", "-", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(3, 7), List.of(5, 9)),
                        List.of(4, 1),
                        List.of("-", "-", "X", "-", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 10 #1",
                        List.of("-", "-", "X", "-", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(5, 9)),
                        List.of(4),
                        List.of("-", "-", "X", "-", "X", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 2 #2",
                        List.of("-", "-", "-", "O", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(6, 9)),
                        List.of(4, 2),
                        List.of("O", "O", "O", "O", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 1 #1",
                        List.of("-", "-", "X", "X", "X", "X", "-", "-", "-", "X"),
                        List.of(List.of(0, 1), List.of(6, 8)),
                        List.of(2, 1),
                        List.of("O", "O", "X", "X", "X", "X", "-", "-", "-", "X")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 0 #1",
                        List.of("-", "X", "X", "X", "X", "X", "X", "-", "-", "X"),
                        List.of(List.of(6, 8)),
                        List.of(2),
                        List.of("-", "X", "X", "X", "X", "X", "X", "O", "-", "X")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 2 #3",
                        List.of("O", "O", "O", "O", "X", "X", "X", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(7, 9)),
                        List.of(4, 2),
                        List.of("O", "O", "O", "O", "X", "X", "X", "-", "O", "-")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 12 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "-", "O", "X"),
                        List.of(List.of(7, 8)),
                        List.of(2),
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X")
                ),
                Arguments.of("o06044 / 10x15 / diff 1.0 / Column 14 #1",
                        List.of("X", "X", "X", "X", "X", "-", "O", "X", "X", "X"),
                        List.of(List.of(5, 6)),
                        List.of(2),
                        List.of("X", "X", "X", "X", "X", "O", "O", "X", "X", "X")
                )
        );
    }
}