package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o05024_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o05024 / 15x10 / diff 1.0 / Column 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(10),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "O", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Column 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-"),
                        List.of(List.of(0, 14)),
                        List.of(12),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Column 2 #1",
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-"),
                        List.of(List.of(0, 9), List.of(9, 14)),
                        List.of(8, 4),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Column 3 #1",
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "O", "-", "-", "O", "O", "O", "-"),
                        List.of(List.of(0, 4), List.of(5, 9), List.of(10, 14)),
                        List.of(4, 4, 4),
                        List.of("-", "O", "O", "O", "-", "-", "O", "O", "O", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Column 0 #2",
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "O", "-", "-"),
                        List.of(List.of(3, 14)),
                        List.of(10),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Column 6 #1",
                        List.of("-", "O", "-", "-", "-", "-", "-", "X", "O", "X", "-", "-", "O", "O", "-"),
                        List.of(List.of(0, 3), List.of(8, 8), List.of(11, 14)),
                        List.of(3, 1, 3),
                        List.of("-", "O", "O", "-", "-", "-", "-", "X", "O", "X", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Column 9 #1",
                        List.of("X", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "-", "-"),
                        List.of(List.of(1, 2), List.of(11, 14)),
                        List.of(2, 3),
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Column 9 #2",
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "X"),
                        List.of(List.of(1, 2), List.of(11, 13)),
                        List.of(2, 3),
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X")
                )
        );
    }
}