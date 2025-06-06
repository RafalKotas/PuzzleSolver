package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o10155_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(2, 16), List.of(15, 19)),
                        List.of(1, 12, 2),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(5, 19)),
                        List.of(4, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "O", "O", "O", "O", "-", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 13), List.of(11, 19)),
                        List.of(10, 5),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 7 #1",
                        List.of("-", "-", "-", "-", "-", "O", "-", "O", "O", "-", "O", "O", "O", "O", "O", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(8, 14), List.of(14, 19)),
                        List.of(7, 5, 4),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "O", "O", "O", "O", "O", "-", "O", "O", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 8 #1",
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "O", "-", "O", "O", "O", "X", "O", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(4, 14), List.of(11, 19)),
                        List.of(3, 6, 4),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "O", "O", "O", "O", "O", "X", "O", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "O", "-", "O", "O", "-", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(13),
                        List.of("-", "-", "-", "-", "-", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(2, 5), List.of(4, 11), List.of(10, 16), List.of(15, 19)),
                        List.of(1, 1, 5, 4, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(4, 9), List.of(6, 17), List.of(14, 19)),
                        List.of(3, 1, 7, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 2 #1",
                        List.of("-", "-", "-", "-", "-", "X", "O", "X", "-", "O", "X", "O", "O", "O", "X", "O", "O", "X", "-", "-"),
                        List.of(List.of(0, 4), List.of(6, 6), List.of(8, 9), List.of(11, 13), List.of(15, 16), List.of(18, 19)),
                        List.of(3, 1, 2, 3, 2, 1),
                        List.of("-", "-", "O", "-", "-", "X", "O", "X", "O", "O", "X", "O", "O", "O", "X", "O", "O", "X", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 3 #2",
                        List.of("-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 16), List.of(17, 19)),
                        List.of(1, 12, 2),
                        List.of("-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "O", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 4 #2",
                        List.of("-", "-", "X", "-", "O", "-", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(3, 6), List.of(8, 17)),
                        List.of(4, 10),
                        List.of("-", "-", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 9 #2",
                        List.of("-", "-", "-", "-", "X", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(4, 17)),
                        List.of(13),
                        List.of("-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 10 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "X", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(2, 5), List.of(7, 11), List.of(13, 16), List.of(18, 19)),
                        List.of(1, 1, 5, 4, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "-", "O", "O")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 3 #3",
                        List.of("-", "-", "-", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "O", "-"),
                        List.of(List.of(0, 2), List.of(5, 16), List.of(18, 19)),
                        List.of(1, 12, 2),
                        List.of("-", "-", "-", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "O", "O")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 11 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "O", "O", "O", "O", "O", "-", "X", "O", "X"),
                        List.of(List.of(0, 6), List.of(4, 8), List.of(10, 16), List.of(18, 18)),
                        List.of(3, 1, 7, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "X")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 12 #1",
                        List.of("-", "O", "X", "O", "O", "-", "-", "-", "X", "X", "O", "X", "O", "O", "O", "O", "X", "X", "O", "X"),
                        List.of(List.of(0, 1), List.of(3, 4), List.of(10, 10), List.of(12, 15), List.of(18, 18)),
                        List.of(2, 2, 1, 4, 1),
                        List.of("O", "O", "X", "O", "O", "-", "-", "-", "X", "X", "O", "X", "O", "O", "O", "O", "X", "X", "O", "X")
                ),
                Arguments.of("o10155 / 20x15 / diff 3.0 / Column 0 #1",
                        List.of("-", "-", "X", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "-", "X", "X", "X"),
                        List.of(List.of(3, 5), List.of(13, 14)),
                        List.of(3, 2),
                        List.of("-", "-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "-", "X", "X", "X")
                )
        );
    }
}