package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o06005_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o06005 / 10x10 / diff 1.0 / Column 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(7),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Column 8 #1",
                        List.of("-", "-", "-", "O", "O", "-", "O", "O", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(7),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Column 7 #1",
                        List.of("-", "-", "X", "O", "O", "X", "O", "O", "X", "-"),
                        List.of(List.of(3, 4), List.of(6, 7), List.of(9, 9)),
                        List.of(2, 2, 1),
                        List.of("-", "-", "X", "O", "O", "X", "O", "O", "X", "O")
                ),
                Arguments.of("o06005 / 10x10 / diff 1.0 / Column 1 #1",
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "-", "O"),
                        List.of(List.of(3, 9)),
                        List.of(7),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O")
                )
        );
    }
}