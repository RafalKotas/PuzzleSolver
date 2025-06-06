package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o06253_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o06253 / 15x10 / diff 1.0 / Column 4 #1",
                        List.of("-", "-", "-", "X", "O", "O", "-", "O", "-", "O", "-", "-", "O", "-", "-"),
                        List.of(List.of(0, 2), List.of(3, 14)),
                        List.of(2, 11),
                        List.of("-", "O", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Column 5 #1",
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(2, 12), List.of(13, 14)),
                        List.of(1, 10, 1),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Column 6 #1",
                        List.of("-", "-", "O", "X", "O", "O", "O", "O", "-", "O", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 14)),
                        List.of(3, 9),
                        List.of("-", "-", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Column 7 #1",
                        List.of("-", "-", "O", "O", "O", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(4, 10), List.of(9, 14)),
                        List.of(3, 4, 3),
                        List.of("-", "-", "O", "O", "O", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Column 2 #1",
                        List.of("-", "X", "O", "O", "O", "X", "O", "O", "X", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(2, 4), List.of(6, 7), List.of(9, 10), List.of(12, 13)),
                        List.of(3, 2, 2, 2),
                        List.of("-", "X", "O", "O", "O", "X", "O", "O", "X", "O", "O", "-", "O", "O", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Column 5 #2",
                        List.of("-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 11), List.of(13, 14)),
                        List.of(1, 10, 1),
                        List.of("O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Column 7 #2",
                        List.of("-", "X", "O", "O", "O", "X", "O", "O", "O", "O", "X", "-", "O", "-", "-"),
                        List.of(List.of(2, 4), List.of(6, 9), List.of(11, 14)),
                        List.of(3, 4, 3),
                        List.of("-", "X", "O", "O", "O", "X", "O", "O", "O", "O", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Column 1 #1",
                        List.of("X", "X", "O", "O", "X", "O", "O", "O", "X", "-", "-", "-", "X", "-", "-"),
                        List.of(List.of(2, 3), List.of(5, 7), List.of(9, 11), List.of(13, 14)),
                        List.of(2, 3, 2, 1),
                        List.of("X", "X", "O", "O", "X", "O", "O", "O", "X", "-", "O", "-", "X", "-", "-")
                ),
                Arguments.of("o06253 / 15x10 / diff 1.0 / Column 3 #1",
                        List.of("X", "X", "X", "O", "O", "O", "X", "O", "O", "O", "X", "-", "O", "X", "X"),
                        List.of(List.of(3, 5), List.of(7, 9), List.of(11, 12)),
                        List.of(3, 3, 2),
                        List.of("X", "X", "X", "O", "O", "O", "X", "O", "O", "O", "X", "O", "O", "X", "X")
                )
        );
    }
}