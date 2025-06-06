package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07986_Test
        extends ColourOverlappingFieldsColumnTestBase
        implements ColourOverlappingFieldsColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        return Stream.of(
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "O"),
                        List.of(List.of(0, 2), List.of(2, 4), List.of(4, 9)),
                        List.of(1, 1, 4),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "-", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 5 #1",
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "O"),
                        List.of(List.of(0, 3), List.of(4, 9)),
                        List.of(3, 5),
                        List.of("-", "O", "O", "-", "-", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 7 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 2), List.of(2, 9)),
                        List.of(1, 6),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "-", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 1), List.of(2, 9)),
                        List.of(1, 7),
                        List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 9)),
                        List.of(8),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "-", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 9)),
                        List.of(9),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 3 #2",
                        List.of("-", "-", "-", "-", "-", "X", "O", "O", "-", "O"),
                        List.of(List.of(0, 2), List.of(2, 4), List.of(6, 9)),
                        List.of(1, 1, 4),
                        List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 7 #2",
                        List.of("-", "-", "-", "X", "O", "O", "O", "O", "-", "O"),
                        List.of(List.of(0, 2), List.of(4, 9)),
                        List.of(1, 6),
                        List.of("-", "-", "-", "X", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 11 #2",
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "-", "O"),
                        List.of(List.of(2, 9)),
                        List.of(8),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 6 #1",
                        List.of("-", "-", "-", "X", "O", "O", "X", "X", "X", "O"),
                        List.of(List.of(0, 0), List.of(2, 2), List.of(4, 5), List.of(9, 9)),
                        List.of(1, 1, 2, 1),
                        List.of("O", "-", "O", "X", "O", "O", "X", "X", "X", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 10 #1",
                        List.of("-", "-", "-", "O", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 3), List.of(2, 3)),
                        List.of(1, 2),
                        List.of("-", "-", "O", "O", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 10 #2",
                        List.of("-", "X", "O", "O", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 0), List.of(2, 3)),
                        List.of(1, 2),
                        List.of("O", "X", "O", "O", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 4 #1",
                        List.of("-", "-", "X", "-", "X", "O", "O", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(3, 3), List.of(5, 6)),
                        List.of(1, 1, 2),
                        List.of("-", "-", "X", "O", "X", "O", "O", "X", "X", "X")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 1 #1",
                        List.of("X", "-", "O", "-", "-", "-", "X", "O", "O", "O"),
                        List.of(List.of(1, 4), List.of(7, 9)),
                        List.of(3, 3),
                        List.of("X", "-", "O", "O", "-", "-", "X", "O", "O", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 2 #1",
                        List.of("X", "X", "O", "X", "-", "X", "O", "O", "X", "O"),
                        List.of(List.of(2, 2), List.of(4, 4), List.of(6, 7), List.of(9, 9)),
                        List.of(1, 1, 2, 1),
                        List.of("X", "X", "O", "X", "O", "X", "O", "O", "X", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 3 #3",
                        List.of("X", "-", "X", "O", "X", "X", "O", "O", "O", "O"),
                        List.of(List.of(1, 1), List.of(3, 3), List.of(6, 9)),
                        List.of(1, 1, 4),
                        List.of("X", "O", "X", "O", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07986 / 10x15 / diff 1.0 / Column 4 #2",
                        List.of("-", "X", "X", "O", "X", "O", "O", "X", "X", "X"),
                        List.of(List.of(0, 0), List.of(3, 3), List.of(5, 6)),
                        List.of(1, 1, 2),
                        List.of("O", "X", "X", "O", "X", "O", "O", "X", "X", "X")
                )
        );
    }
}