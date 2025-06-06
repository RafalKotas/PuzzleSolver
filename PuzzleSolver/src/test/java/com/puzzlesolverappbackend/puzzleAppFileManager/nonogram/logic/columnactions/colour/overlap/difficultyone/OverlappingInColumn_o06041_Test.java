package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o06041_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 4 #1",
                        List.of("O", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(9),
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 7 #1",
                        List.of("O", "-", "-", "O", "O", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 2), List.of(2, 7), List.of(7, 9)),
                        List.of(1, 4, 1),
                        List.of("O", "-", "-", "O", "O", "O", "-", "-", "-", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 8 #1",
                        List.of("O", "-", "-", "O", "O", "O", "-", "-", "-", "O"),
                        List.of(List.of(0, 1), List.of(2, 7), List.of(8, 9)),
                        List.of(1, 5, 1),
                        List.of("O", "-", "-", "O", "O", "O", "O", "-", "-", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 9 #1",
                        List.of("O", "-", "-", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(2, 7), List.of(8, 9)),
                        List.of(1, 5, 1),
                        List.of("O", "-", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 10 #1",
                        List.of("O", "-", "-", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(2, 7), List.of(8, 9)),
                        List.of(1, 5, 1),
                        List.of("O", "-", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 11 #1",
                        List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(2, 7), List.of(7, 9)),
                        List.of(1, 4, 1),
                        List.of("-", "-", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(9),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 1 #1",
                        List.of("-", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 5), List.of(5, 9)),
                        List.of(3, 3),
                        List.of("-", "O", "O", "O", "-", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 11 #2",
                        List.of("-", "-", "O", "O", "O", "O", "X", "-", "-", "O"),
                        List.of(List.of(0, 0), List.of(2, 5), List.of(9, 9)),
                        List.of(1, 4, 1),
                        List.of("O", "-", "O", "O", "O", "O", "X", "-", "-", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Column 5 #1",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "-", "-"),
                        List.of(List.of(0, 0), List.of(8, 9)),
                        List.of(1, 2),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "O", "O")
                )
        );
    }
}