package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.difficultytwo;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInColumn_o08436_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(2, 19)),
                        List.of(1, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(2, 19)),
                        List.of(1, 10),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 7 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 14), List.of(12, 19)),
                        List.of(11, 4),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 14 #1",
                        List.of("-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "O", "X", "-"),
                        List.of(List.of(0, 4), List.of(7, 11), List.of(9, 17), List.of(17, 19)),
                        List.of(3, 2, 3, 1),
                        List.of("-", "-", "O", "-", "-", "X", "X", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "O", "X", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 15 #1",
                        List.of("-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(2, 4), List.of(7, 8), List.of(9, 12), List.of(13, 19)),
                        List.of(1, 2, 1, 3, 3),
                        List.of("-", "-", "-", "O", "-", "X", "X", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 7 #2",
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(13, 19)),
                        List.of(11, 4),
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 9 #1",
                        List.of("-", "-", "-", "-", "-", "X", "X", "-", "-", "X", "O", "X", "O", "X", "-", "-", "O", "-", "-", "X"),
                        List.of(List.of(0, 4), List.of(4, 10), List.of(10, 12), List.of(12, 14), List.of(14, 18)),
                        List.of(3, 1, 1, 1, 3),
                        List.of("-", "-", "O", "-", "-", "X", "X", "-", "-", "X", "O", "X", "O", "X", "-", "-", "O", "-", "-", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 10 #1",
                        List.of("-", "-", "-", "-", "-", "X", "X", "-", "-", "X", "O", "O", "O", "X", "-", "-", "O", "-", "-", "X"),
                        List.of(List.of(0, 7), List.of(2, 4), List.of(10, 12), List.of(15, 17)),
                        List.of(1, 3, 3, 2),
                        List.of("-", "-", "O", "O", "O", "X", "X", "-", "-", "X", "O", "O", "O", "X", "-", "-", "O", "-", "-", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 10 #2",
                        List.of("-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "-", "O", "-", "X", "X"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(10, 12), List.of(15, 17)),
                        List.of(1, 3, 3, 2),
                        List.of("O", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "-", "O", "-", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 12 #1",
                        List.of("-", "-", "-", "-", "-", "X", "X", "-", "-", "X", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(0, 4), List.of(2, 4), List.of(10, 12), List.of(17, 17)),
                        List.of(1, 3, 3, 1),
                        List.of("-", "-", "O", "O", "O", "X", "X", "-", "-", "X", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 12 #2",
                        List.of("-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(10, 12), List.of(17, 17)),
                        List.of(1, 3, 3, 1),
                        List.of("O", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 14 #2",
                        List.of("-", "-", "O", "X", "-", "X", "X", "-", "-", "X", "O", "O", "O", "X", "-", "-", "X", "O", "X", "-"),
                        List.of(List.of(0, 4), List.of(7, 8), List.of(10, 12), List.of(17, 17)),
                        List.of(3, 2, 3, 1),
                        List.of("-", "-", "O", "X", "-", "X", "X", "O", "O", "X", "O", "O", "O", "X", "-", "-", "X", "O", "X", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 14 #3",
                        List.of("-", "-", "O", "X", "-", "X", "X", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(0, 2), List.of(7, 8), List.of(10, 12), List.of(17, 17)),
                        List.of(3, 2, 3, 1),
                        List.of("O", "O", "O", "X", "-", "X", "X", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 18 #1",
                        List.of("-", "-", "-", "X", "-", "X", "X", "-", "-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 2), List.of(4, 8), List.of(10, 12)),
                        List.of(2, 1, 3),
                        List.of("-", "O", "-", "X", "-", "X", "X", "-", "-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 0 #1",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "-", "-", "X"),
                        List.of(List.of(15, 18)),
                        List.of(3),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "-", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 18 #2",
                        List.of("-", "O", "X", "X", "-", "X", "X", "-", "-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(4, 8), List.of(10, 12)),
                        List.of(2, 1, 3),
                        List.of("O", "O", "X", "X", "-", "X", "X", "-", "-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 13 #1",
                        List.of("O", "X", "X", "O", "X", "X", "X", "X", "-", "X", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(0, 0), List.of(3, 3), List.of(8, 8), List.of(10, 12), List.of(17, 17)),
                        List.of(1, 1, 1, 3, 1),
                        List.of("O", "X", "X", "O", "X", "X", "X", "X", "O", "X", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 17 #1",
                        List.of("-", "X", "O", "O", "O", "X", "X", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "X", "-"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(8, 14)),
                        List.of(1, 3, 5),
                        List.of("O", "X", "O", "O", "O", "X", "X", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "X", "-")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 2 #1",
                        List.of("X", "-", "X", "X", "X", "X", "X", "-", "-", "-", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X"),
                        List.of(List.of(7, 9), List.of(16, 16)),
                        List.of(3, 1),
                        List.of("X", "-", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 17 #2",
                        List.of("O", "X", "O", "O", "O", "X", "X", "X", "X", "-", "O", "O", "O", "-", "-", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 0), List.of(2, 4), List.of(9, 14)),
                        List.of(1, 3, 5),
                        List.of("O", "X", "O", "O", "O", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o08436 / 20x20 / diff 2.0 / Column 7 #3",
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "O", "-", "O", "-"),
                        List.of(List.of(0, 10), List.of(15, 19)),
                        List.of(11, 4),
                        List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "O", "O", "O", "-")
                )
        );
    }
}