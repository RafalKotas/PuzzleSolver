package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.extend.tobottom;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class ExtendColouredFieldsToBottomColumnDiff1Test
    extends ExtendColouredFieldsToBottomColumnTestBase
    implements ExtendColouredFieldsToBottomColumnTestExecutor {

    static Stream<Arguments> provideTestCasesForExtendBottom() {
        return Stream.of(
                Arguments.of("o07836.json / 10x10 / diff 1.0 / Column 5 / toBottom",
                        List.of("X", "X", "O", "O", "-", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 4)),
                        List.of(3),
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "X", "X")
                ),
                Arguments.of("o07836.json / 10x10 / diff 1.0 / Column 6 / toBottom",
                        List.of("X", "O", "O", "O", "X", "X", "O", "O", "O", "-"),
                        List.of(List.of(1, 3), List.of(6, 9)),
                        List.of(3, 4),
                        List.of("X", "O", "O", "O", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o06041.json / 10x15 / diff 1.0 / Column 3 / toBottom",
                        List.of("X", "O", "O", "X", "X", "X", "O", "-", "X", "X"),
                        List.of(List.of(1, 2), List.of(5, 7)),
                        List.of(2, 2),
                        List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
                ),
                Arguments.of("o06041.json / 10x15 / diff 1.0 / Column 0 / toBottom",
                        List.of("-", "X", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 6)),
                        List.of(5),
                        List.of("-", "X", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o06041.json / 10x15 / diff 1.0 / Column 9 / toBottom",
                        List.of("O", "X", "X", "O", "O", "O", "O", "-", "X", "O"),
                        List.of(List.of(0, 0), List.of(2, 7), List.of(9, 9)),
                        List.of(1, 5, 1),
                        List.of("O", "X", "X", "O", "O", "O", "O", "O", "X", "O")
                ),

                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 3 / toBottom",
                        List.of("-", "-", "-", "-", "-", "X", "O", "-", "-", "O"),
                        List.of(List.of(6, 9)),
                        List.of(4),
                        List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 6 / toBottom",
                        List.of("-", "-", "-", "-", "-", "X", "O", "-", "O", "O"),
                        List.of(List.of(0, 4), List.of(6, 9)),
                        List.of(2, 4),
                        List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 9 / toBottom",
                        List.of("-", "-", "X", "O", "O", "X", "O", "-", "X", "O"),
                        List.of(List.of(0, 1), List.of(3, 4), List.of(6, 7), List.of(9, 9)),
                        List.of(1, 2, 2, 1),
                        List.of("-", "-", "X", "O", "O", "X", "O", "O", "X", "O")
                ),
                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 0 / toBottom",
                        List.of("-", "-", "X", "X", "O", "-", "O", "-", "-", "-"),
                        List.of(List.of(4, 6)),
                        List.of(3),
                        List.of("-", "-", "X", "X", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 8 / toBottom",
                        List.of("O", "X", "X", "O", "O", "-", "X", "O", "O", "O"),
                        List.of(List.of(0, 0), List.of(3, 5), List.of(7, 9)),
                        List.of(1, 3, 3),
                        List.of("O", "X", "X", "O", "O", "O", "X", "O", "O", "O")
                ),
                Arguments.of("o01131.json / 10x15 / diff 1.0 / Column 14 / toBottom",
                        List.of("-", "X", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(2, 7), List.of(7, 9)),
                        List.of(6, 1),
                        List.of("-", "X", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o07387.json / 10x15 / diff 1.0 / Column 2 / toBottom",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "O", "-"),
                        List.of(List.of(0, 0), List.of(7, 9)),
                        List.of(1, 2),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o07387.json / 10x15 / diff 1.0 / Column 3 / toBottom",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "O", "-"),
                        List.of(List.of(0, 0), List.of(8, 9)),
                        List.of(1, 2),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "O", "O")
                ),
                Arguments.of("o05024.json / 15x10 / diff 1.0 / Column 6 / toBottom",
                        List.of("-", "O", "O", "-", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O", "-"),
                        List.of(List.of(0, 3), List.of(8, 8), List.of(11, 14)),
                        List.of(3, 1, 3),
                        List.of("-", "O", "O", "-", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of("o05024.json / 15x10 / diff 1.0 / Column 3 / toBottom",
                        List.of("X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O", "O", "O", "-"),
                        List.of(List.of(1, 4), List.of(6, 9), List.of(11, 14)),
                        List.of(4, 4, 4),
                        List.of("X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o05024.json / 15x10 / diff 1.0 / Column 0 / toBottom",
                        List.of("X", "X", "X", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(3, 12)),
                        List.of(10),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of("o06044.json / 10x15 / diff 1.0 / Column 6 / toBottom",
                        List.of("X", "X", "X", "X", "-", "O", "-", "-", "X", "O"),
                        List.of(List.of(3, 7), List.of(9, 9)),
                        List.of(3, 1),
                        List.of("X", "X", "X", "X", "-", "O", "O", "-", "X", "O")
                ),
                Arguments.of("o06044.json / 10x15 / diff 1.0 / Column 10 / toBottom",
                        List.of("-", "-", "X", "-", "X", "X", "O", "O", "O", "-"),
                        List.of(List.of(5, 9)),
                        List.of(4),
                        List.of("-", "-", "X", "-", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of("o06044.json / 10x15 / diff 1.0 / Column 5 / toBottom",
                        List.of("X", "-", "X", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(3, 9)),
                        List.of(7),
                        List.of("X", "-", "X", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o06044.json / 10x15 / diff 1.0 / Column 4 / toBottom",
                        List.of("X", "O", "O", "O", "O", "O", "-", "X", "-", "-"),
                        List.of(List.of(0, 6), List.of(8, 9)),
                        List.of(6, 1),
                        List.of("X", "O", "O", "O", "O", "O", "O", "X", "-", "-")
                ),
                Arguments.of("o06044.json / 10x15 / diff 1.0 / Column 0 / toBottom",
                        List.of("-", "X", "X", "X", "X", "X", "X", "O", "-", "X"),
                        List.of(List.of(6, 8)),
                        List.of(2),
                        List.of("-", "X", "X", "X", "X", "X", "X", "O", "O", "X")
                ),
                Arguments.of("o06148.json / 10x15 / diff 1.0 / Column 8 / toBottom",
                        List.of("O", "O", "O", "O", "O", "X", "O", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(6, 7)),
                        List.of(5, 2),
                        List.of("O", "O", "O", "O", "O", "X", "O", "O", "-", "-")
                ),
                Arguments.of("o06148.json / 10x15 / diff 1.0 / Column 14 / toBottom",
                        List.of("X", "X", "X", "O", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(3, 5)),
                        List.of(3),
                        List.of("X", "X", "X", "O", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of("o06148.json / 10x15 / diff 1.0 / Column 10 / toBottom",
                        List.of("O", "O", "O", "X", "-", "-", "X", "X", "O", "-"),
                        List.of(List.of(0, 2), List.of(7, 9)),
                        List.of(3, 2),
                        List.of("O", "O", "O", "X", "-", "-", "X", "X", "O", "O")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 1 / toBottom",
                        List.of("-", "-", "O", "O", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(3, 7), List.of(7, 12), List.of(10, 14)),
                        List.of(2, 3, 2, 1),
                        List.of("-", "-", "O", "O", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 2 / toBottom",
                        List.of("-", "-", "O", "O", "O", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 7), List.of(7, 11), List.of(10, 14)),
                        List.of(3, 2, 2, 2),
                        List.of("-", "-", "O", "O", "O", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 7 / toBottom",
                        List.of("-", "-", "O", "O", "O", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(4, 9), List.of(9, 14)),
                        List.of(3, 4, 3),
                        List.of("-", "-", "O", "O", "O", "X", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 4 / toBottom",
                        List.of("-", "O", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(0, 2), List.of(4, 14)),
                        List.of(2, 11),
                        List.of("-", "O", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 3 / toBottom",
                        List.of("-", "-", "X", "O", "O", "O", "X", "O", "-", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(3, 5), List.of(7, 9), List.of(9, 14)),
                        List.of(3, 3, 2),
                        List.of("-", "-", "X", "O", "O", "O", "X", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of("o06253.json / 15x10 / diff 1.0 / Column 0 / toBottom",
                        List.of("X", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "-", "-", "-"),
                        List.of(List.of(2, 6), List.of(11, 13)),
                        List.of(5, 3),
                        List.of("X", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O", "-")
                )
        );
    }
}
