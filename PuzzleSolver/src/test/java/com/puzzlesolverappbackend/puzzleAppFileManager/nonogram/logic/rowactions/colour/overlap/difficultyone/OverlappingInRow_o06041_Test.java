package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o06041_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 0 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(11),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(6, 9), List.of(9, 12), List.of(12, 14)),
                        List.of(5, 2, 2, 1),
                        List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(3, 4), List.of(5, 12), List.of(13, 14)),
                        List.of(2, 1, 7, 1),
                        List.of("-", "O", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 4 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(2, 4), List.of(4, 12), List.of(12, 14)),
                        List.of(1, 1, 7, 1),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 5 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(3, 6), List.of(5, 12), List.of(11, 14)),
                        List.of(2, 1, 5, 1),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(6, 12), List.of(10, 14)),
                        List.of(5, 3, 1),
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 14)),
                        List.of(9),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 3 #2",
                        List.of("-", "O", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-"),
                        List.of(List.of(0, 2), List.of(4, 4), List.of(6, 12), List.of(14, 14)),
                        List.of(2, 1, 7, 1),
                        List.of("-", "O", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 5 #2",
                        List.of("-", "-", "-", "-", "O", "X", "X", "O", "O", "O", "O", "O", "X", "-", "-"),
                        List.of(List.of(0, 2), List.of(3, 4), List.of(7, 11), List.of(13, 14)),
                        List.of(2, 1, 5, 1),
                        List.of("-", "O", "-", "-", "O", "X", "X", "O", "O", "O", "O", "O", "X", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 7 #1",
                        List.of("X", "O", "-", "-", "O", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 5), List.of(8, 12), List.of(8, 14)),
                        List.of(4, 1, 1),
                        List.of("X", "O", "O", "O", "O", "-", "X", "X", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 2 #2",
                        List.of("O", "O", "O", "O", "O", "X", "X", "O", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(7, 8), List.of(10, 12), List.of(13, 14)),
                        List.of(5, 2, 2, 1),
                        List.of("O", "O", "O", "O", "O", "X", "X", "O", "O", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 0 #2",
                        List.of("-", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(4, 14)),
                        List.of(11),
                        List.of("-", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 4 #2",
                        List.of("O", "X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(4, 4), List.of(6, 12), List.of(12, 14)),
                        List.of(1, 1, 7, 1),
                        List.of("O", "X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 4 #3",
                        List.of("O", "X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-"),
                        List.of(List.of(0, 0), List.of(4, 4), List.of(6, 12), List.of(14, 14)),
                        List.of(1, 1, 7, 1),
                        List.of("O", "X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 7 #2",
                        List.of("X", "O", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "-"),
                        List.of(List.of(1, 4), List.of(9, 9), List.of(14, 14)),
                        List.of(4, 1, 1),
                        List.of("X", "O", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 2 #3",
                        List.of("O", "O", "O", "O", "O", "X", "X", "O", "O", "X", "O", "O", "X", "X", "-"),
                        List.of(List.of(0, 4), List.of(7, 8), List.of(10, 11), List.of(14, 14)),
                        List.of(5, 2, 2, 1),
                        List.of("O", "O", "O", "O", "O", "X", "X", "O", "O", "X", "O", "O", "X", "X", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 8 #1",
                        List.of("X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "-", "-"),
                        List.of(List.of(4, 5), List.of(13, 14)),
                        List.of(2, 2),
                        List.of("X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 1 #1",
                        List.of("X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-"),
                        List.of(List.of(1, 4), List.of(14, 14)),
                        List.of(4, 1),
                        List.of("X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 6 #2",
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "X", "X", "X", "-"),
                        List.of(List.of(0, 4), List.of(8, 10), List.of(14, 14)),
                        List.of(5, 3, 1),
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "X", "X", "X", "O")
                ),
                Arguments.of("o06041 / 10x15 / diff 1.0 / Row 5 #3",
                        List.of("O", "O", "X", "X", "O", "X", "X", "O", "O", "O", "O", "O", "X", "X", "-"),
                        List.of(List.of(0, 1), List.of(4, 4), List.of(7, 11), List.of(14, 14)),
                        List.of(2, 1, 5, 1),
                        List.of("O", "O", "X", "X", "O", "X", "X", "O", "O", "O", "O", "O", "X", "X", "O")
                )
        );
    }
}
