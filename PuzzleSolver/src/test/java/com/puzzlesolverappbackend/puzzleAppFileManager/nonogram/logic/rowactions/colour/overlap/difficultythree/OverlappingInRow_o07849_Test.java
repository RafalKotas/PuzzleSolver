package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07849_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(11, 18)),
                        List.of(7),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(4, 19)),
                        List.of(10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "O", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 12), List.of(2, 15), List.of(16, 19)),
                        List.of(1, 2, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 8 #1",
                        List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(6, 9), List.of(11, 15), List.of(14, 19)),
                        List.of(4, 4, 2, 3),
                        List.of("-", "O", "O", "O", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 10 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "X", "X"),
                        List.of(List.of(11, 17)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 11 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "-", "O", "X", "X"),
                        List.of(List.of(12, 12), List.of(16, 17)),
                        List.of(1, 2),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "O", "X", "X"),
                        List.of(List.of(0, 14), List.of(16, 17)),
                        List.of(1, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 13 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "X", "-", "O", "X", "X"),
                        List.of(List.of(0, 11), List.of(12, 14), List.of(16, 17)),
                        List.of(3, 2, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "X", "O", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(0, 14), List.of(15, 19)),
                        List.of(3, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(8, 13), List.of(14, 19)),
                        List.of(4, 5),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 1 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "-", "-", "-", "O", "-"),
                        List.of(List.of(9, 19)),
                        List.of(10),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 0 #1",
                        List.of("X", "X", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(9, 19)),
                        List.of(8),
                        List.of("X", "X", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "X", "-", "O", "-"),
                        List.of(List.of(0, 10), List.of(8, 15), List.of(17, 19)),
                        List.of(7, 2, 2),
                        List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "X", "-", "-", "-", "-", "X", "-", "O", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 6 #2",
                        List.of("-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "X", "-", "-", "-", "-", "X", "O", "O", "-"),
                        List.of(List.of(0, 12), List.of(2, 15), List.of(17, 19)),
                        List.of(1, 2, 3),
                        List.of("-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "X", "-", "-", "-", "-", "X", "O", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 8 #2",
                        List.of("-", "O", "O", "O", "-", "X", "O", "O", "O", "O", "X", "X", "-", "-", "-", "X", "O", "O", "O", "-"),
                        List.of(List.of(0, 4), List.of(6, 9), List.of(12, 14), List.of(16, 19)),
                        List.of(4, 4, 2, 3),
                        List.of("-", "O", "O", "O", "-", "X", "O", "O", "O", "O", "X", "X", "-", "O", "-", "X", "O", "O", "O", "-")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 3 #2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "O", "O", "O", "-"),
                        List.of(List.of(0, 13), List.of(16, 19)),
                        List.of(3, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 0 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X"),
                        List.of(List.of(10, 17)),
                        List.of(8),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 1 #3",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                        List.of(List.of(9, 18)),
                        List.of(10),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(0, 6), List.of(3, 7), List.of(12, 13), List.of(17, 17)),
                        List.of(2, 3, 2, 1),
                        List.of("-", "-", "-", "-", "-", "O", "-", "-", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 7 #1",
                        List.of("O", "X", "X", "X", "-", "-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X"),
                        List.of(List.of(0, 0), List.of(4, 6), List.of(17, 18)),
                        List.of(1, 3, 2),
                        List.of("O", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 13 #2",
                        List.of("-", "-", "-", "-", "X", "-", "-", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X"),
                        List.of(List.of(0, 3), List.of(12, 13), List.of(16, 17)),
                        List.of(3, 2, 2),
                        List.of("-", "O", "O", "-", "X", "-", "-", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 14 #2",
                        List.of("-", "-", "-", "-", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(0, 3), List.of(3, 5), List.of(12, 13), List.of(17, 17)),
                        List.of(2, 3, 2, 1),
                        List.of("-", "-", "-", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 14 #3",
                        List.of("-", "-", "-", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(0, 2), List.of(3, 5), List.of(12, 13), List.of(17, 17)),
                        List.of(2, 3, 2, 1),
                        List.of("-", "O", "-", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 14 #4",
                        List.of("-", "O", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(0, 1), List.of(3, 5), List.of(12, 13), List.of(17, 17)),
                        List.of(2, 3, 2, 1),
                        List.of("O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X")
                ),
                Arguments.of("o07849 / 20x20 / diff 3.0 / Row 13 #3",
                        List.of("X", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X"),
                        List.of(List.of(1, 3), List.of(12, 13), List.of(16, 17)),
                        List.of(3, 2, 2),
                        List.of("X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X")
                )
        );
    }
}
