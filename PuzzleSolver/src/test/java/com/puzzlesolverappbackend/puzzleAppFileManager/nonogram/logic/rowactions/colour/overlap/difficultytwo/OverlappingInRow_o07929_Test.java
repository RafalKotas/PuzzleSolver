package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07929_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(6, 19)),
                        List.of(5, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 18 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(5, 10), List.of(11, 13), List.of(14, 19)),
                        List.of(4, 5, 2, 5),
                        List.of("-", "O", "O", "O", "-", "-", "O", "O", "O", "O", "-", "-", "O", "-", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 19 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(2, 8), List.of(5, 11), List.of(8, 19)),
                        List.of(1, 2, 2, 7),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 10 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "-", "-", "-", "-", "-", "O"),
                        List.of(List.of(0, 10), List.of(12, 19)),
                        List.of(5, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "O"),
                        List.of(List.of(0, 10), List.of(12, 19)),
                        List.of(3, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 7 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-"),
                        List.of(List.of(0, 10), List.of(10, 19)),
                        List.of(3, 8),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 17 #1",
                        List.of("-", "-", "-", "X", "-", "-", "X", "O", "-", "O", "-", "-", "O", "X", "-", "X", "O", "O", "O", "O"),
                        List.of(List.of(0, 4), List.of(4, 6), List.of(7, 10), List.of(12, 12), List.of(16, 19)),
                        List.of(2, 1, 3, 1, 4),
                        List.of("-", "-", "-", "X", "-", "-", "X", "O", "O", "O", "-", "-", "O", "X", "-", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 19 #2",
                        List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(4, 8), List.of(7, 11), List.of(10, 19)),
                        List.of(1, 2, 2, 7),
                        List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 17 #2",
                        List.of("-", "-", "-", "X", "-", "-", "X", "O", "O", "O", "X", "X", "O", "X", "X", "X", "O", "O", "O", "O"),
                        List.of(List.of(0, 2), List.of(4, 5), List.of(7, 9), List.of(12, 12), List.of(16, 19)),
                        List.of(2, 1, 3, 1, 4),
                        List.of("-", "O", "-", "X", "-", "-", "X", "O", "O", "O", "X", "X", "O", "X", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 19 #3",
                        List.of("-", "-", "-", "X", "-", "-", "O", "-", "X", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 7), List.of(9, 11), List.of(10, 19)),
                        List.of(1, 2, 2, 7),
                        List.of("-", "-", "-", "X", "-", "-", "O", "-", "X", "-", "O", "-", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(2, 14), List.of(9, 15)),
                        List.of(1, 3, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "O", "-", "-", "-", "X", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 8 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "X", "O", "X", "O", "X", "-"),
                        List.of(List.of(0, 10), List.of(12, 13), List.of(15, 15), List.of(17, 17), List.of(19, 19)),
                        List.of(4, 2, 1, 1, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "X", "O", "X", "O", "X", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 14 #1",
                        List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 3), List.of(5, 11), List.of(9, 16), List.of(17, 19)),
                        List.of(4, 3, 2, 2),
                        List.of("O", "O", "O", "O", "-", "-", "-", "-", "-", "O", "-", "-", "X", "-", "-", "-", "-", "-", "O", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 16 #2",
                        List.of("-", "X", "X", "X", "-", "-", "-", "-", "X", "-", "-", "-", "O", "-", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(0, 0), List.of(4, 7), List.of(9, 15)),
                        List.of(1, 3, 4),
                        List.of("O", "X", "X", "X", "-", "O", "O", "-", "X", "-", "-", "-", "O", "-", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 15 #1",
                        List.of("X", "X", "X", "X", "-", "X", "O", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(6, 7), List.of(8, 11), List.of(10, 16), List.of(13, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "X", "X", "X", "-", "X", "O", "O", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 15 #2",
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "X", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(6, 7), List.of(9, 11), List.of(10, 16), List.of(13, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "X", "-", "O", "-", "X", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 14 #2",
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "X", "-", "X", "-", "-", "-", "X", "O", "O", "X"),
                        List.of(List.of(0, 3), List.of(7, 9), List.of(13, 15), List.of(17, 18)),
                        List.of(4, 3, 2, 2),
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "X", "-", "X", "-", "O", "-", "X", "O", "O", "X")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 19 #4",
                        List.of("X", "X", "-", "X", "-", "X", "O", "-", "X", "-", "O", "-", "X", "O", "O", "O", "O", "O", "O", "O"),
                        List.of(List.of(2, 4), List.of(6, 7), List.of(9, 11), List.of(13, 19)),
                        List.of(1, 2, 2, 7),
                        List.of("X", "X", "-", "X", "-", "X", "O", "O", "X", "-", "O", "-", "X", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07929 / 20x20 / diff 2.0 / Row 15 #3",
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "X", "-", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(6, 7), List.of(9, 10), List.of(13, 16), List.of(16, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-")
                )
        );
    }
}
