package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o06148_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o06148 / 10x15 / diff 1.0 / Column 0 #1",
                        List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(9),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Column 5 #1",
                        List.of("-", "O", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(6),
                        List.of("-", "O", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Column 6 #1",
                        List.of("-", "O", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(8),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Column 8 #1",
                        List.of("O", "O", "O", "O", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(6, 9)),
                        List.of(5, 2),
                        List.of("O", "O", "O", "O", "O", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Column 11 #1",
                        List.of("O", "O", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(3, 4), List.of(5, 7), List.of(8, 9)),
                        List.of(2, 1, 2, 1),
                        List.of("O", "O", "-", "O", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Column 13 #1",
                        List.of("-", "O", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 9)),
                        List.of(3, 4),
                        List.of("-", "O", "O", "O", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o06148 / 10x15 / diff 1.0 / Column 13 #2",
                        List.of("X", "O", "O", "O", "X", "-", "O", "O", "-", "-"),
                        List.of(List.of(1, 3), List.of(5, 9)),
                        List.of(3, 4),
                        List.of("X", "O", "O", "O", "X", "-", "O", "O", "O", "-")
                )
        );
    }
}