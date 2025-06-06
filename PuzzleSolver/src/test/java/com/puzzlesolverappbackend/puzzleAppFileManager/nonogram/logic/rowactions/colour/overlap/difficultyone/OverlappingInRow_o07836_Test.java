package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07836_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07836 / 10x10 / diff 1.0 / Row 9 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(6, 9)),
                        List.of(5, 2),
                        List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Row 3 #1",
                        List.of("X", "X", "X", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(3, 6), List.of(6, 9)),
                        List.of(4, 1),
                        List.of("X", "X", "X", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Row 8 #1",
                        List.of("O", "X", "O", "O", "X", "X", "O", "-", "-", "X"),
                        List.of(List.of(0, 0), List.of(2, 3), List.of(6, 8)),
                        List.of(1, 2, 2),
                        List.of("O", "X", "O", "O", "X", "X", "O", "O", "-", "X")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Row 9 #2",
                        List.of("O", "O", "O", "O", "O", "X", "-", "-", "-", "X"),
                        List.of(List.of(0, 4), List.of(6, 8)),
                        List.of(5, 2),
                        List.of("O", "O", "O", "O", "O", "X", "-", "O", "-", "X")
                ),
                Arguments.of("o07836 / 10x10 / diff 1.0 / Row 6 #1",
                        List.of("X", "-", "O", "O", "X", "X", "O", "O", "X", "X"),
                        List.of(List.of(1, 3), List.of(6, 7)),
                        List.of(3, 2),
                        List.of("X", "O", "O", "O", "X", "X", "O", "O", "X", "X")
                )
        );
    }
}
