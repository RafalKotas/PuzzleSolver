package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07387_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o07387 / 10x15 / diff 1.0 / Column 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(6),
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Column 1 #1",
                        List.of("-", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(6, 9)),
                        List.of(2, 3),
                        List.of("-", "-", "-", "-", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Column 8 #1",
                        List.of("X", "X", "X", "-", "O", "O", "X", "X", "X", "X"),
                        List.of(List.of(3, 5)),
                        List.of(3),
                        List.of("X", "X", "X", "O", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Column 10 #1",
                        List.of("X", "X", "-", "-", "O", "O", "X", "X", "X", "X"),
                        List.of(List.of(2, 5)),
                        List.of(4),
                        List.of("X", "X", "O", "O", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Column 5 #1",
                        List.of("-", "-", "-", "O", "O", "O", "-", "-", "X", "-"),
                        List.of(List.of(0, 7)),
                        List.of(6),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "X", "-")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Column 14 #1",
                        List.of("X", "-", "X", "O", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(3, 5)),
                        List.of(3),
                        List.of("X", "-", "X", "O", "O", "O", "-", "-", "X", "X")
                ),
                Arguments.of("o07387 / 10x15 / diff 1.0 / Column 4 #1",
                        List.of("-", "O", "X", "-", "X", "X", "O", "O", "O", "X"),
                        List.of(List.of(0, 1), List.of(6, 8)),
                        List.of(2, 3),
                        List.of("O", "O", "X", "-", "X", "X", "O", "O", "O", "X")
                )
        );
    }
}