package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07956_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07956 / 20x30 / diff 3.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(11),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Column 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(5, 19)),
                        List.of(4, 9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Column 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(2, 17), List.of(17, 19)),
                        List.of(1, 14, 1),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Column 9 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(5, 19)),
                        List.of(11),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Column 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(10, 17), List.of(17, 19)),
                        List.of(2, 6, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Column 15 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(10, 16), List.of(15, 19)),
                        List.of(2, 6, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Column 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(7, 19)),
                        List.of(3, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Column 15 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(10, 16), List.of(17, 19)),
                        List.of(2, 6, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "O", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Column 9 #3",
                        List.of("X", "X", "X", "X", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "X"),
                        List.of(List.of(5, 18)),
                        List.of(11),
                        List.of("X", "X", "X", "X", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "X")
                )
        );
    }
}