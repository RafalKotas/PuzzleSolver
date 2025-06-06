package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07959_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 7 #1",
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 6), List.of(4, 9), List.of(7, 16), List.of(14, 19)),
                        List.of(3, 2, 6, 2),
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "O", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 8 #1",
                        List.of("-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 2), List.of(4, 5), List.of(7, 13), List.of(14, 19)),
                        List.of(1, 2, 3, 5),
                        List.of("-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 13), List.of(14, 19)),
                        List.of(5, 5),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 15), List.of(16, 19)),
                        List.of(2, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 5 #1",
                        List.of("X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "X"),
                        List.of(List.of(4, 5), List.of(17, 18)),
                        List.of(2, 2),
                        List.of("X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 13 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "X"),
                        List.of(List.of(0, 15), List.of(16, 18)),
                        List.of(2, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "X")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 19 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O"),
                        List.of(List.of(0, 15), List.of(17, 19)),
                        List.of(4, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 8 #2",
                        List.of("-", "-", "X", "X", "O", "O", "X", "-", "-", "-", "-", "X", "X", "X", "X", "O", "O", "O", "O", "O"),
                        List.of(List.of(0, 1), List.of(4, 5), List.of(7, 10), List.of(15, 19)),
                        List.of(1, 2, 3, 5),
                        List.of("-", "-", "X", "X", "O", "O", "X", "-", "O", "O", "-", "X", "X", "X", "X", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 19 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O"),
                        List.of(List.of(0, 6), List.of(17, 19)),
                        List.of(4, 3),
                        List.of("-", "-", "-", "O", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 17 #1",
                        List.of("-", "-", "-", "-", "-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X"),
                        List.of(List.of(0, 4), List.of(5, 6), List.of(18, 18)),
                        List.of(3, 1, 1),
                        List.of("-", "-", "O", "-", "-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 17 #2",
                        List.of("-", "-", "O", "-", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X"),
                        List.of(List.of(0, 3), List.of(5, 5), List.of(18, 18)),
                        List.of(3, 1, 1),
                        List.of("-", "O", "O", "-", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X")
                ),
                Arguments.of("o07959 / 20x20 / diff 3.0 / Column 17 #3",
                        List.of("-", "O", "O", "X", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X"),
                        List.of(List.of(0, 2), List.of(5, 5), List.of(18, 18)),
                        List.of(3, 1, 1),
                        List.of("O", "O", "O", "X", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X")
                )
        );
    }
}