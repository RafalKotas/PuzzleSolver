package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.difficultythree;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestExecutor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class OverlappingInRow_o07956_Test
        extends ColourOverlappingFieldsRowTestBase
        implements ColourOverlappingFieldsRowTestExecutor {

    // TODO - NOT FULLY SOLVED
    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        return Stream.of(
                Arguments.of("o07956 / 20x30 / diff 3.0 / Row 14 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 25), List.of(23, 29)),
                        List.of(22, 3),
                        List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Row 15 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 21), List.of(17, 29)),
                        List.of(16, 7),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o07956 / 20x30 / diff 3.0 / Row 8 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(11, 21), List.of(13, 29)),
                        List.of(9, 1, 7),
                        List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                )
        );
    }
}
