package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07834_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(3, 9), List.of(6, 19)),
                        List.of(2, 2, 9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 11 #2",
                        List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 3), List.of(4, 9), List.of(7, 19)),
                        List.of(2, 2, 9),
                        List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 15 #1",
                        List.of("-", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(7, 19)),
                        List.of(6, 5),
                        List.of("-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 19 #1",
                        List.of("-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(9, 17), List.of(11, 19)),
                        List.of(8, 1, 1),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 13 #1",
                        List.of("-", "O", "X", "X", "-", "X", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 10), List.of(6, 14), List.of(8, 16), List.of(11, 19)),
                        List.of(2, 1, 2, 1, 2),
                        List.of("O", "O", "X", "X", "-", "X", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 9 #1",
                        List.of("-", "X", "O", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 8), List.of(7, 19)),
                        List.of(1, 4, 2),
                        List.of("-", "X", "O", "X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 7 #1",
                        List.of("-", "-", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 14), List.of(7, 17), List.of(10, 19)),
                        List.of(3, 2, 2, 1),
                        List.of("O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 18 #1",
                        List.of("-", "X", "O", "X", "X", "O", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(2, 9), List.of(7, 16), List.of(14, 19)),
                        List.of(1, 1, 6, 2),
                        List.of("-", "X", "O", "X", "X", "O", "X", "O", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 18 #2",
                        List.of("-", "X", "O", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "X", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 2), List.of(5, 5), List.of(7, 12), List.of(14, 15)),
                        List.of(1, 1, 6, 2),
                        List.of("-", "X", "O", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "X", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 17 #1",
                        List.of("X", "O", "O", "X", "X", "O", "X", "X", "X", "X", "X", "X", "O", "-", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(5, 5), List.of(12, 12), List.of(12, 19)),
                        List.of(2, 1, 1, 6),
                        List.of("X", "O", "O", "X", "X", "O", "X", "X", "X", "X", "X", "X", "O", "-", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 11 #3",
                        List.of("X", "O", "O", "X", "-", "-", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(4, 9), List.of(8, 19)),
                        List.of(2, 2, 9),
                        List.of("X", "O", "O", "X", "-", "-", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 10 #1",
                        List.of("X", "X", "O", "X", "-", "O", "X", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 2), List.of(2, 5), List.of(7, 10), List.of(10, 16)),
                        List.of(1, 1, 3, 5),
                        List.of("X", "X", "O", "X", "-", "O", "X", "-", "O", "O", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 10 #2",
                        List.of("X", "X", "O", "X", "-", "O", "X", "-", "O", "O", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 2), List.of(4, 5), List.of(7, 10), List.of(11, 16)),
                        List.of(1, 1, 3, 5),
                        List.of("X", "X", "O", "X", "-", "O", "X", "-", "O", "O", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07834 / 20x20 / diff 2.0 / Row 13 #2",
                        List.of("O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "X", "-", "-"),
                        List.of(List.of(0, 1), List.of(6, 6), List.of(12, 13), List.of(16, 16), List.of(18, 19)),
                        List.of(2, 1, 2, 1, 2),
                        List.of("O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "X", "O", "O")
                )
        );
    }
}
