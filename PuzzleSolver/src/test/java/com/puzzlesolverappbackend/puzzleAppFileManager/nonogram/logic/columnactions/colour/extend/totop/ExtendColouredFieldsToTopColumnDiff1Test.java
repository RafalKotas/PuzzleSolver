package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.extend.totop;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class ExtendColouredFieldsToTopColumnDiff1Test
    extends ExtendColouredFieldsToTopColumnTestBase
    implements ExtendColouredFieldsToTopColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForExtendTop() {
        return Stream.of(
                Arguments.of("o07836.json / 10x10 / diff 1.0 / Column 6 / toTop",
                        List.of("X", "-", "O", "O", "X", "X", "O", "O", "O", "-"),
                        List.of(List.of(1, 3), List.of(6, 9)),
                        List.of(3, 4),
                        List.of("X", "O", "O", "O", "X", "X", "O", "O", "O", "-")
                ),
                Arguments.of("o06041.json / 10x15 / diff 1.0 / Column 7 / toTop",
                        List.of("O", "X", "-", "O", "O", "O", "X", "X", "X", "O"),
                        List.of(List.of(0, 0), List.of(2, 6), List.of(9, 9)),
                        List.of(1, 4, 1),
                        List.of("O", "X", "O", "O", "O", "O", "X", "X", "X", "O")
                ),
                Arguments.of("o07986.json / 10x15 / diff 1.0 / Column 14 / toTop",
                        List.of("-", "O", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 1), List.of(9, 9)),
                        List.of(2, 1),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 5 / toTop",
                        List.of("X", "-", "O", "X", "-", "O", "-", "X", "O", "O"),
                        List.of(List.of(1, 3), List.of(4, 6), List.of(8, 9)),
                        List.of(2, 2, 2),
                        List.of("X", "O", "O", "X", "-", "O", "-", "X", "O", "O")
                ),
                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 10 / toTop",
                        List.of("-", "-", "-", "X", "-", "O", "O", "X", "X", "O"),
                        List.of(List.of(0, 3), List.of(4, 7), List.of(9, 9)),
                        List.of(1, 3, 1),
                        List.of("-", "-", "-", "X", "O", "O", "O", "X", "X", "O")
                ),
                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 13 / toTop",
                        List.of("-", "-", "O", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(6, 9)),
                        List.of(2, 3),
                        List.of("-", "O", "O", "X", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 4 / toTop",
                        List.of("-", "X", "O", "O", "-", "O", "X", "O", "O", "O"),
                        List.of(List.of(2, 5), List.of(7, 9)),
                        List.of(4, 3),
                        List.of("-", "X", "O", "O", "O", "O", "X", "O", "O", "O")
                ),
                Arguments.of("o07387.json / 10x15 / diff 1.0 / Column 0 / toTop",
                        List.of("-", "-", "-", "-", "O", "O", "-", "X", "-", "-"),
                        List.of(List.of(0, 6)),
                        List.of(6),
                        List.of("-", "O", "O", "O", "O", "O", "-", "X", "-", "-")
                ),
                Arguments.of("o07387.json / 10x15 / diff 1.0 / Column 1 / toTop",
                        List.of("-", "O", "X", "X", "X", "X", "-", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(6, 9)),
                        List.of(2, 3),
                        List.of("O", "O", "X", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of("o07387.json / 10x15 / diff 1.0 / Column 4 / toTop",
                        List.of("-", "-", "X", "-", "-", "-", "-", "-", "O", "X"),
                        List.of(List.of(0, 5), List.of(6, 8)),
                        List.of(2, 3),
                        List.of("-", "-", "X", "-", "-", "-", "O", "O", "O", "X")
                ),
                Arguments.of("o06044.json / 10x15 / diff 1.0 / Column 8 / toTop",
                        List.of("X", "X", "X", "-", "O", "O", "X", "-", "X", "O"),
                        List.of(List.of(3, 6), List.of(9, 9)),
                        List.of(3, 1),
                        List.of("X", "X", "X", "O", "O", "O", "X", "-", "X", "O")
                ),
                Arguments.of("o06044.json / 10x15 / diff 1.0 / Column 4 / toTop",
                        List.of("-", "-", "O", "O", "O", "O", "-", "X", "-", "-"),
                        List.of(List.of(0, 6), List.of(8, 9)),
                        List.of(6, 1),
                        List.of("-", "O", "O", "O", "O", "O", "-", "X", "-", "-")
                ),
                Arguments.of("o06044.json / 10x15 / diff 1.0 / Column 3 / toTop",
                        List.of("-", "O", "O", "O", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 5), List.of(7, 9)),
                        List.of(6, 1),
                        List.of("O", "O", "O", "O", "O", "O", "X", "-", "-", "-")
                ),
                Arguments.of("o06148.json / 10x15 / diff 1.0 / Column 12 / toTop",
                        List.of("-", "O", "X", "X", "-", "X", "O", "X", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 6), List.of(6, 9)),
                        List.of(2, 1, 1),
                        List.of("O", "O", "X", "X", "-", "X", "O", "X", "-", "-")
                ),
                Arguments.of("o06148.json / 10x15 / diff 1.0 / Column 11 / toTop",
                        List.of("O", "O", "X", "O", "X", "-", "O", "X", "-", "-"),
                        List.of(List.of(0, 1), List.of(3, 3), List.of(5, 7), List.of(8, 9)),
                        List.of(2, 1, 2, 1),
                        List.of("O", "O", "X", "O", "X", "O", "O", "X", "-", "-")
                ),
                Arguments.of("o06148.json / 10x15 / diff 1.0 / Column 4 / toTop",
                        List.of("-", "O", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1)),
                        List.of(2),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 6 / toTop",
                        List.of("-", "-", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 14)),
                        List.of(3, 9),
                        List.of("O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 8 / toTop",
                        List.of("-", "-", "O", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 8), List.of(8, 12), List.of(11, 14)),
                        List.of(3, 3, 2, 1),
                        List.of("O", "O", "O", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 9 / toTop",
                        List.of("O", "X", "-", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 0), List.of(2, 5)),
                        List.of(1, 4),
                        List.of("O", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 8 / toTop",
                        List.of("O", "O", "O", "X", "O", "O", "O", "X", "X", "-", "O", "X", "X", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 6), List.of(9, 10), List.of(13, 14)),
                        List.of(3, 3, 2, 1),
                        List.of("O", "O", "O", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "-", "-")
                )
        );
    }
}
