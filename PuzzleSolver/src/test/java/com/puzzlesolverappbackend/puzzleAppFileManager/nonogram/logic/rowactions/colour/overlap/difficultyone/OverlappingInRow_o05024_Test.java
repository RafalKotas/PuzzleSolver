package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultyone;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o05024_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 1 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(8),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 2 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(7, 9)),
                        List.of(6, 1),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 3 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(6),
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 7 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(6),
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 8 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(7),
                        List.of("-", "-", "-", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 11 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(7, 9)),
                        List.of(6, 1),
                        List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 13 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(9),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 14 #1",
                        List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9)),
                        List.of(6),
                        List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 4 #1",
                        List.of("-", "O", "O", "-", "O", "-", "X", "X", "-", "-"),
                        List.of(List.of(0, 5)),
                        List.of(5),
                        List.of("-", "O", "O", "O", "O", "-", "X", "X", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 10 #1",
                        List.of("O", "O", "-", "X", "X", "X", "X", "X", "-", "-"),
                        List.of(List.of(0, 3)),
                        List.of(3),
                        List.of("O", "O", "O", "X", "X", "X", "X", "X", "-", "-")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 0 #1",
                        List.of("X", "X", "X", "X", "O", "-", "-", "-", "-", "X"),
                        List.of(List.of(4, 8)),
                        List.of(5),
                        List.of("X", "X", "X", "X", "O", "O", "O", "O", "O", "X")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 1 #2",
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(2, 9)),
                        List.of(8),
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o05024 / 15x10 / diff 1.0 / Row 3 #2",
                        List.of("-", "O", "O", "O", "O", "O", "X", "X", "X", "X"),
                        List.of(List.of(0, 5)),
                        List.of(6),
                        List.of("O", "O", "O", "O", "O", "O", "X", "X", "X", "X")
                )
        );
    }
}
