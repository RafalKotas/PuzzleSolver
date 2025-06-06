package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o08436_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 10 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(3, 19)),
                        List.of(2, 13),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(3, 19)),
                        List.of(2, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 12 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(3, 19)),
                        List.of(2, 11),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 16 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 17), List.of(12, 19)),
                        List.of(11, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 17 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(3, 9), List.of(6, 19)),
                        List.of(2, 2, 9),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 9 #1",
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 8), List.of(7, 19)),
                        List.of(4, 1),
                        List.of("X", "X", "-", "-", "-", "O", "-", "O", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 12 #2",
                        List.of("X", "X", "X", "O", "O", "X", "-", "X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(3, 4), List.of(8, 19)),
                        List.of(2, 11),
                        List.of("X", "X", "X", "O", "O", "X", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 10 #2",
                        List.of("X", "X", "X", "O", "O", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(3, 4), List.of(6, 19)),
                        List.of(2, 13),
                        List.of("X", "X", "X", "O", "O", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 3 #1",
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "-", "O", "-", "O", "-", "-", "O", "-", "-", "-", "-"),
                        List.of(List.of(2, 8), List.of(7, 10), List.of(9, 13), List.of(14, 17)),
                        List.of(2, 1, 2, 3),
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "-", "O", "-", "O", "-", "-", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 3 #2",
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "-", "O", "X", "O", "-", "-", "O", "O", "-", "X", "X"),
                        List.of(List.of(2, 8), List.of(7, 10), List.of(12, 13), List.of(14, 17)),
                        List.of(2, 1, 2, 3),
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "-", "O", "X", "O", "O", "-", "O", "O", "-", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 19 #1",
                        List.of("X", "X", "X", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-", "X", "-"),
                        List.of(List.of(3, 8)),
                        List.of(4),
                        List.of("X", "X", "X", "-", "-", "O", "O", "-", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 4 #1",
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "-", "O", "X", "O", "X", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 10), List.of(7, 12), List.of(15, 19)),
                        List.of(4, 1, 4),
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "-", "O", "X", "O", "X", "X", "-", "O", "O", "O", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 17 #2",
                        List.of("O", "-", "X", "-", "-", "X", "-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "X", "-"),
                        List.of(List.of(0, 4), List.of(3, 9), List.of(6, 17)),
                        List.of(2, 2, 9),
                        List.of("O", "-", "X", "-", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 18 #1",
                        List.of("-", "X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "-", "-", "-", "X", "-"),
                        List.of(List.of(0, 6), List.of(3, 9), List.of(6, 9)),
                        List.of(1, 2, 4),
                        List.of("-", "X", "X", "-", "-", "-", "O", "O", "O", "O", "X", "X", "X", "X", "X", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 8 #1",
                        List.of("X", "X", "-", "-", "-", "O", "X", "O", "X", "-", "X", "-", "X", "O", "O", "-", "X", "-", "X", "-"),
                        List.of(List.of(2, 7), List.of(4, 11), List.of(7, 15), List.of(13, 15)),
                        List.of(1, 1, 1, 3),
                        List.of("X", "X", "-", "-", "-", "O", "X", "O", "X", "-", "X", "-", "X", "O", "O", "O", "X", "-", "X", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 2 #1",
                        List.of("X", "X", "-", "-", "-", "-", "X", "O", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "-"),
                        List.of(List.of(7, 7), List.of(9, 12), List.of(14, 17), List.of(19, 19)),
                        List.of(1, 4, 4, 1),
                        List.of("X", "X", "-", "-", "-", "-", "X", "O", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 4 #2",
                        List.of("X", "X", "X", "-", "-", "X", "-", "O", "-", "-", "O", "X", "O", "X", "X", "X", "O", "O", "O", "-"),
                        List.of(List.of(6, 10), List.of(11, 12), List.of(15, 19)),
                        List.of(4, 1, 4),
                        List.of("X", "X", "X", "-", "-", "X", "-", "O", "O", "O", "O", "X", "O", "X", "X", "X", "O", "O", "O", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 17 #3",
                        List.of("O", "-", "X", "-", "-", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "-"),
                        List.of(List.of(0, 1), List.of(3, 7), List.of(6, 17)),
                        List.of(2, 2, 9),
                        List.of("O", "O", "X", "-", "-", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 18 #2",
                        List.of("-", "X", "X", "-", "-", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(3, 4), List.of(6, 9)),
                        List.of(1, 2, 4),
                        List.of("-", "X", "X", "O", "O", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 17 #4",
                        List.of("O", "O", "X", "-", "-", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(3, 4), List.of(7, 16)),
                        List.of(2, 2, 9),
                        List.of("O", "O", "X", "O", "O", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 18 #3",
                        List.of("-", "X", "X", "O", "O", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 0), List.of(3, 4), List.of(6, 9)),
                        List.of(1, 2, 4),
                        List.of("O", "X", "X", "O", "O", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 7 #1",
                        List.of("X", "X", "O", "-", "O", "O", "X", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 5), List.of(7, 14), List.of(14, 14)),
                        List.of(4, 1, 1),
                        List.of("X", "X", "O", "O", "O", "O", "X", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 14 #1",
                        List.of("X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "-", "X", "X"),
                        List.of(List.of(3, 4), List.of(16, 17)),
                        List.of(2, 2),
                        List.of("X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 12 #3",
                        List.of("X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(3, 4), List.of(9, 19)),
                        List.of(2, 11),
                        List.of("X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Row 10 #3",
                        List.of("X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(3, 4), List.of(7, 19)),
                        List.of(2, 13),
                        List.of("X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                )
        );
    }
}
