package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07923_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 19)),
                        List.of(15),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 13), List.of(10, 19)),
                        List.of(9, 5),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 11), List.of(9, 19)),
                        List.of(8, 7),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 3 #1",
                        List.of("X", "-", "-", "X", "O", "O", "O", "O", "O", "O", "X", "-", "-", "X", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(4, 9), List.of(14, 19)),
                        List.of(6, 5),
                        List.of("X", "-", "-", "X", "O", "O", "O", "O", "O", "O", "X", "-", "-", "X", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "X", "X", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O"),
                        List.of(List.of(6, 6), List.of(16, 19)),
                        List.of(1, 4),
                        List.of("-", "-", "-", "-", "X", "X", "O", "X", "X", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 0 #2",
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X"),
                        List.of(List.of(1, 16)),
                        List.of(15),
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 10 #1",
                        List.of("O", "O", "X", "-", "-", "O", "O", "O", "O", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(3, 9), List.of(11, 11)),
                        List.of(2, 6, 1),
                        List.of("O", "O", "X", "-", "O", "O", "O", "O", "O", "-", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 12 #2",
                        List.of("X", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(4, 11), List.of(13, 19)),
                        List.of(8, 7),
                        List.of("X", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 17 #1",
                        List.of("-", "-", "-", "-", "-", "X", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(9, 19)),
                        List.of(7),
                        List.of("-", "-", "-", "-", "-", "X", "-", "X", "X", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 19 #1",
                        List.of("-", "-", "O", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(9, 19)),
                        List.of(5, 4),
                        List.of("-", "-", "O", "O", "O", "-", "-", "X", "X", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 17 #2",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "-", "X", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(13, 19)),
                        List.of(7),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o07923 / 20x20 / diff 2.0 / Row 9 #1",
                        List.of("O", "O", "O", "X", "X", "O", "O", "X", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 2), List.of(5, 6), List.of(8, 9), List.of(11, 12)),
                        List.of(3, 2, 2, 2),
                        List.of("O", "O", "O", "X", "X", "O", "O", "X", "O", "O", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                )
        );
    }
}
