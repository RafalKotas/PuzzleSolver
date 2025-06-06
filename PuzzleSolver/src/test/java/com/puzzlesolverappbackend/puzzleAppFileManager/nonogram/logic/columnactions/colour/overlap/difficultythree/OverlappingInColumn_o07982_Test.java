package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07982_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(13),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 19 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(8, 19)),
                        List.of(7, 10),
                        List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(13, 19)),
                        List.of(3, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 18 #1",
                        List.of("-", "-", "X", "X", "O", "X", "O", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(6, 9), List.of(14, 14), List.of(18, 19)),
                        List.of(2, 1, 4, 1, 2),
                        List.of("-", "O", "X", "X", "O", "X", "O", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 17 #1",
                        List.of("-", "-", "-", "X", "O", "X", "X", "X", "-", "-", "-", "-", "-", "-", "O", "X", "-", "-", "X", "O"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(8, 14), List.of(14, 17), List.of(19, 19)),
                        List.of(2, 1, 2, 1, 1),
                        List.of("-", "O", "-", "X", "O", "X", "X", "X", "-", "-", "-", "-", "-", "-", "O", "X", "-", "-", "X", "O")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 17 #2",
                        List.of("X", "O", "-", "X", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O"),
                        List.of(List.of(1, 2), List.of(4, 4), List.of(8, 9), List.of(14, 14), List.of(19, 19)),
                        List.of(2, 1, 2, 1, 1),
                        List.of("X", "O", "O", "X", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "O", "O", "-", "X", "X", "O"),
                        List.of(List.of(0, 12), List.of(12, 16), List.of(19, 19)),
                        List.of(2, 4, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "O", "-", "X", "X", "O")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "X", "O", "X", "X", "X", "O"),
                        List.of(List.of(0, 7), List.of(15, 15), List.of(19, 19)),
                        List.of(6, 1, 1),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "X", "-", "-", "-", "-", "-", "X", "O", "X", "X", "X", "O")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 16 #1",
                        List.of("-", "X", "O", "X", "O", "X", "-", "X", "O", "X", "-", "-", "-", "-", "O", "X", "X", "X", "-", "O"),
                        List.of(List.of(0, 2), List.of(2, 4), List.of(4, 8), List.of(14, 14), List.of(18, 19)),
                        List.of(1, 1, 1, 1, 2),
                        List.of("-", "X", "O", "X", "O", "X", "-", "X", "O", "X", "-", "-", "-", "-", "O", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 11 #1",
                        List.of("-", "-", "X", "-", "-", "-", "-", "-", "X", "X", "X", "-", "X", "-", "O", "O", "X", "-", "-", "O"),
                        List.of(List.of(0, 2), List.of(3, 7), List.of(14, 15), List.of(19, 19)),
                        List.of(1, 4, 2, 1),
                        List.of("-", "-", "X", "-", "O", "O", "O", "-", "X", "X", "X", "-", "X", "-", "O", "O", "X", "-", "-", "O")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 12 #1",
                        List.of("-", "-", "X", "-", "-", "-", "-", "-", "O", "X", "X", "-", "X", "X", "O", "X", "X", "X", "O", "O"),
                        List.of(List.of(0, 3), List.of(3, 5), List.of(7, 8), List.of(14, 14), List.of(18, 19)),
                        List.of(2, 1, 2, 1, 2),
                        List.of("-", "-", "X", "-", "-", "-", "-", "O", "O", "X", "X", "-", "X", "X", "O", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 12 #2",
                        List.of("-", "-", "X", "-", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O"),
                        List.of(List.of(0, 1), List.of(3, 5), List.of(7, 8), List.of(14, 14), List.of(18, 19)),
                        List.of(2, 1, 2, 1, 2),
                        List.of("O", "O", "X", "-", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 0 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "X"),
                        List.of(List.of(0, 18)),
                        List.of(13),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "X")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 1 #2",
                        List.of("X", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X", "-", "-", "O", "O", "O", "-", "X"),
                        List.of(List.of(1, 5), List.of(14, 18)),
                        List.of(3, 4),
                        List.of("X", "-", "-", "O", "-", "-", "X", "X", "X", "X", "X", "X", "X", "-", "-", "O", "O", "O", "-", "X")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 0 #3",
                        List.of("X", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "X"),
                        List.of(List.of(2, 18)),
                        List.of(13),
                        List.of("X", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 1 #3",
                        List.of("X", "X", "-", "O", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "X"),
                        List.of(List.of(2, 5), List.of(14, 18)),
                        List.of(3, 4),
                        List.of("X", "X", "-", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "X")
                ),
                Arguments.of("o07982 / 20x20 / diff 3.0 / Column 0 #4",
                        List.of("X", "X", "-", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X"),
                        List.of(List.of(5, 18)),
                        List.of(13),
                        List.of("X", "X", "-", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X")
                )
        );
    }
}