package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07836_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 9)),
                        List.of(9),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 7), List.of(6, 9)),
                        List.of(5, 1),
                        List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "O")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 9)),
                        List.of(3, 4),
                        List.of("-", "-", "O", "-", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 7 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 9)),
                        List.of(3, 4),
                        List.of("-", "-", "O", "-", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 2 #1",
                        List.of("-", "-", "X", "-", "-", "-", "O", "X", "-", "O"),
                        List.of(List.of(4, 6), List.of(8, 9)),
                        List.of(3, 2),
                        List.of("-", "-", "X", "-", "O", "O", "O", "X", "O", "O")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 0 #1",
                        List.of("X", "-", "X", "X", "X", "-", "X", "X", "-", "-"),
                        List.of(List.of(8, 9)),
                        List.of(2),
                        List.of("X", "-", "X", "X", "X", "-", "X", "X", "O", "O")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 1 #1",
                        List.of("X", "-", "X", "X", "-", "-", "-", "X", "X", "-"),
                        List.of(List.of(4, 6), List.of(4, 9)),
                        List.of(2, 1),
                        List.of("X", "-", "X", "X", "-", "O", "-", "X", "X", "-")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 9 #1",
                        List.of("-", "-", "X", "-", "-", "-", "X", "X", "-", "-"),
                        List.of(List.of(0, 5), List.of(3, 5)),
                        List.of(1, 3),
                        List.of("-", "-", "X", "O", "O", "O", "X", "X", "-", "-")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 7 #2",
                        List.of("-", "-", "O", "X", "-", "-", "O", "O", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 9)),
                        List.of(3, 4),
                        List.of("O", "O", "O", "X", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Column 6 #2",
                        List.of("-", "-", "O", "O", "-", "-", "O", "O", "-", "-"),
                        List.of(List.of(1, 4), List.of(5, 9)),
                        List.of(3, 4),
                        List.of("-", "-", "O", "O", "-", "-", "O", "O", "O", "-")
                )
        );
    }
}