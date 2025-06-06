package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o07806_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 10 #1",
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(3, 19)),
                        List.of(2, 15),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 11 #1",
                        List.of("-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(3, 11), List.of(8, 19)),
                        List.of(2, 2, 7),
                        List.of("-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 12 #1",
                        List.of("-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(5, 9), List.of(8, 14), List.of(13, 19)),
                        List.of(2, 2, 4, 4),
                        List.of("-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "O", "-", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 6 #1",
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 7), List.of(8, 14)),
                        List.of(1, 3, 6),
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "O", "O", "O", "O", "O", "-", "X", "X", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 1 #1",
                        List.of("-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 4), List.of(6, 13)),
                        List.of(3, 5),
                        List.of("-", "-", "O", "-", "-", "X", "-", "-", "-", "O", "O", "-", "-", "-", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 4 #1",
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "X", "X", "X", "X", "X", "-"),
                        List.of(List.of(0, 6), List.of(3, 13), List.of(12, 13)),
                        List.of(2, 4, 2),
                        List.of("-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "O", "O", "X", "X", "X", "X", "X", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "-", "X", "-", "X", "-", "-", "-", "-", "X", "O", "O", "O"),
                        List.of(List.of(0, 3), List.of(4, 8), List.of(9, 15), List.of(17, 19)),
                        List.of(3, 4, 2, 3),
                        List.of("-", "O", "O", "-", "-", "O", "O", "O", "-", "X", "-", "X", "-", "-", "-", "-", "X", "O", "O", "O")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-", "O", "O", "O"),
                        List.of(List.of(0, 8), List.of(4, 10), List.of(6, 10), List.of(16, 19)),
                        List.of(3, 1, 3, 4),
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "O", "-", "-", "X", "-", "X", "-", "-", "O", "O", "O", "O")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 12)),
                        List.of(7),
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 13 #2",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "O", "-", "-", "X", "-", "X", "-", "-", "O", "O", "O", "O"),
                        List.of(List.of(0, 4), List.of(4, 6), List.of(6, 10), List.of(16, 19)),
                        List.of(3, 1, 3, 4),
                        List.of("-", "-", "O", "-", "-", "-", "O", "-", "O", "-", "-", "X", "-", "X", "-", "-", "O", "O", "O", "O")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 2 #1",
                        List.of("-", "-", "O", "-", "-", "O", "-", "-", "-", "X", "O", "O", "X", "-", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 3), List.of(4, 6), List.of(10, 11)),
                        List.of(3, 2, 2),
                        List.of("-", "O", "O", "-", "-", "O", "-", "-", "-", "X", "O", "O", "X", "-", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 9 #2",
                        List.of("-", "O", "O", "-", "-", "O", "O", "O", "-", "X", "X", "X", "X", "-", "-", "O", "X", "O", "O", "O"),
                        List.of(List.of(0, 3), List.of(4, 8), List.of(14, 15), List.of(17, 19)),
                        List.of(3, 4, 2, 3),
                        List.of("-", "O", "O", "-", "-", "O", "O", "O", "-", "X", "X", "X", "X", "-", "O", "O", "X", "O", "O", "O")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 16 #2",
                        List.of("-", "-", "-", "-", "-", "-", "O", "-", "O", "-", "-", "X", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 10)),
                        List.of(7),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "-", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 6 #2",
                        List.of("O", "-", "-", "-", "X", "O", "O", "O", "X", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 0), List.of(5, 7), List.of(9, 14)),
                        List.of(1, 3, 6),
                        List.of("O", "-", "-", "-", "X", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 8 #1",
                        List.of("O", "-", "-", "-", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(6, 7), List.of(13, 15)),
                        List.of(2, 2, 3),
                        List.of("O", "O", "-", "-", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o07806 / 20x20 / diff 2.0 / Column 4 #2",
                        List.of("-", "O", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(5, 8), List.of(12, 13)),
                        List.of(2, 4, 2),
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X")
                )
        );
    }
}