package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class ExtendColouredFieldsToRightRowDiff2Test
        extends ExtendColouredFieldsToRightRowTestBase
        implements ExtendRightRowTestExecutor {

    static Stream<Arguments> provideTestCasesForExtendRight() {
        return Stream.of(
                Arguments.of(
                        "o08007 / 15x15 / diff 2.0 / Row 14",
                        List.of("X", "X", "O", "O", "O", "-", "-", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 6)),
                        List.of(4),
                        List.of("X", "X", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o08007 / 15x15 / diff 2.0 / Row 12",
                        List.of("O", "O", "X", "X", "X", "X", "O", "-", "X", "X", "X", "X", "X", "-", "-"),
                        List.of(List.of(0, 1), List.of(6, 14)),
                        List.of(2, 2),
                        List.of("O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "-", "-")
                ),
                Arguments.of(
                        "o08007 / 15x15 / diff 2.0 / Row 8",
                        List.of("X", "X", "O", "X", "X", "O", "X", "X", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 2), List.of(5, 5), List.of(8, 14)),
                        List.of(1, 1, 2),
                        List.of("X", "X", "O", "X", "X", "O", "X", "X", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o08007 / 15x15 / diff 2.0 / Row 6",
                        List.of("X", "X", "O", "O", "X", "X", "O", "-", "X", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 3), List.of(5, 7), List.of(9, 10)),
                        List.of(2, 2, 2),
                        List.of("X", "X", "O", "O", "X", "X", "O", "O", "X", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o08007 / 15x15 / diff 2.0 / Row 4",
                        List.of("X", "X", "O", "O", "X", "-", "O", "O", "X", "X", "O", "-", "-", "-", "-"),
                        List.of(List.of(2, 3), List.of(5, 7), List.of(10, 14)),
                        List.of(2, 2, 2),
                        List.of("X", "X", "O", "O", "X", "-", "O", "O", "X", "X", "O", "O", "-", "-", "-")
                ),
                Arguments.of(
                        "o08007 / 15x15 / diff 2.0 / Row 0",
                        List.of("X", "X", "X", "O", "X", "X", "O", "-", "X", "X", "X", "X", "O", "O", "O"),
                        List.of(List.of(3, 3), List.of(6, 7), List.of(12, 14)),
                        List.of(1, 2, 3),
                        List.of("X", "X", "X", "O", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of(
                        "o07942 / 20x20 / diff 2.0 / Row 4",
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(5, 8), List.of(10, 13)),
                        List.of(4, 4),
                        List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "X", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07942 / 20x20 / diff 2.0 / Row 11",
                        List.of("X", "-", "-", "-", "-", "-", "X", "O", "O", "O", "-", "O", "X", "-", "-", "X", "O", "X", "O", "X"),
                        List.of(List.of(1, 5), List.of(5, 11), List.of(11, 14), List.of(16, 16), List.of(18, 18)),
                        List.of(1, 5, 1, 1, 1),
                        List.of("X", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "X", "-", "-", "X", "O", "X", "O", "X")
                ),
                Arguments.of(
                        "o07942 / 20x20 / diff 2.0 / Row 8",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "-"),
                        List.of(List.of(0, 12), List.of(3, 15), List.of(17, 19)),
                        List.of(2, 2, 3),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O")
                ),
                Arguments.of(
                        "o07942 / 20x20 / diff 2.0 / Row 10",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "-", "X", "X", "X", "X", "-", "O", "O", "O", "-"),
                        List.of(List.of(8, 10), List.of(15, 19)),
                        List.of(3, 4),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "-", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o07942 / 20x20 / diff 2.0 / Row 3 #1",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "O", "-", "-", "-", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 8), List.of(3, 11), List.of(6, 14), List.of(10, 17)),
                        List.of(2, 2, 2, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "O", "O", "-", "-", "-", "-", "-", "-", "X", "X")
                ),
                Arguments.of(
                        "o07942 / 20x20 / diff 2.0 / Row 7",
                        List.of("X", "-", "-", "-", "-", "X", "O", "-", "X", "X", "X", "-", "O", "X", "-", "-", "-", "X", "O", "X"),
                        List.of(List.of(5, 7), List.of(11, 13), List.of(18, 18)),
                        List.of(2, 2, 1),
                        List.of("X", "-", "-", "-", "-", "X", "O", "O", "X", "X", "X", "-", "O", "X", "-", "-", "-", "X", "O", "X")
                ),
                Arguments.of(
                        "o07942 / 20x20 / diff 2.0 / Row 8",
                        List.of("X", "-", "-", "-", "-", "X", "O", "-", "X", "X", "X", "-", "O", "X", "-", "-", "X", "O", "O", "O"),
                        List.of(List.of(5, 7), List.of(11, 13), List.of(17, 19)),
                        List.of(2, 2, 3),
                        List.of("X", "-", "-", "-", "-", "X", "O", "O", "X", "X", "X", "-", "O", "X", "-", "-", "X", "O", "O", "O")
                ),
                Arguments.of(
                        "o07942 / 20x20 / diff 2.0 / Row 3 #2",
                        List.of("X", "-", "-", "-", "-", "O", "X", "O", "O", "X", "O", "O", "X", "O", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(1, 8), List.of(3, 11), List.of(7, 14), List.of(10, 17)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "-", "-", "-", "-", "O", "X", "O", "O", "X", "O", "O", "X", "O", "O", "-", "-", "-", "X", "X")
                ),
                Arguments.of(
                        "o07942 / 20x20 / diff 2.0 / Row 2",
                        List.of("X", "-", "-", "O", "O", "X", "X", "O", "O", "O", "O", "O", "X", "X", "O", "-", "-", "-", "X", "X"),
                        List.of(List.of(3, 4), List.of(7, 11), List.of(14, 17)),
                        List.of(2, 5, 2),
                        List.of("X", "-", "-", "O", "O", "X", "X", "O", "O", "O", "O", "O", "X", "X", "O", "O", "-", "-", "X", "X")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 4",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-", "-", "O"),
                        List.of(List.of(0, 10), List.of(4, 12), List.of(6, 14), List.of(16, 19)),
                        List.of(3, 1, 1, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 18 #1",
                        List.of("-", "O", "O", "O", "-", "-", "O", "O", "O", "O", "-", "-", "O", "O", "X", "O", "O", "O", "O", "-"),
                        List.of(List.of(0, 4), List.of(5, 10), List.of(11, 13), List.of(15, 19)),
                        List.of(4, 5, 2, 5),
                        List.of("-", "O", "O", "O", "-", "-", "O", "O", "O", "O", "-", "-", "O", "O", "X", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 0",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "X", "O", "-", "-", "-"),
                        List.of(List.of(0, 14), List.of(13, 19)),
                        List.of(1, 4),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "X", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 19",
                        List.of("-", "X", "-", "X", "-", "-", "O", "-", "X", "-", "O", "-", "X", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(5, 7), List.of(9, 11), List.of(13, 19)),
                        List.of(1, 2, 2, 7),
                        List.of("-", "X", "-", "X", "-", "-", "O", "-", "X", "-", "O", "-", "X", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 14",
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "-", "O", "-", "-", "X", "-", "-", "-", "-", "O", "O", "-"),
                        List.of(List.of(0, 3), List.of(5, 9), List.of(9, 15), List.of(17, 19)),
                        List.of(4, 3, 2, 2),
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "-", "-", "X", "-", "-", "-", "-", "O", "O", "-")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 18 #2",
                        List.of("X", "O", "O", "O", "-", "X", "O", "O", "O", "O", "O", "X", "O", "O", "X", "O", "O", "O", "O", "O"),
                        List.of(List.of(1, 4), List.of(6, 10), List.of(12, 13), List.of(15, 19)),
                        List.of(4, 5, 2, 5),
                        List.of("X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "X", "O", "O", "X", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 16",
                        List.of("O", "X", "X", "X", "O", "O", "O", "X", "X", "X", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 0), List.of(4, 6), List.of(9, 13)),
                        List.of(1, 3, 4),
                        List.of("O", "X", "X", "X", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 15 #1",
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "X", "X", "O", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(6, 7), List.of(9, 10), List.of(13, 14), List.of(16, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "X", "X", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 13",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(14, 19)),
                        List.of(1, 3),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o07929 / 20x20 / diff 2.0 / Row 15 #2",
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "-"),
                        List.of(List.of(6, 7), List.of(9, 10), List.of(13, 14), List.of(18, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 3",
                        List.of("-", "X", "O", "-", "-", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 5)),
                        List.of(4),
                        List.of("-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 0",
                        List.of("X", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(1, 5)),
                        List.of(4),
                        List.of("X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 1",
                        List.of("X", "X", "O", "O", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-"),
                        List.of(List.of(2, 3), List.of(5, 19)),
                        List.of(2, 2),
                        List.of("X", "X", "O", "O", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 18",
                        List.of("-", "X", "O", "X", "X", "O", "X", "O", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(5, 5), List.of(7, 12), List.of(14, 19)),
                        List.of(1, 1, 6, 2),
                        List.of("-", "X", "O", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 15 #1",
                        List.of("X", "O", "O", "O", "O", "O", "-", "X", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(8, 19)),
                        List.of(6, 5),
                        List.of("X", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 8 #1",
                        List.of("O", "X", "O", "X", "-", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 2), List.of(4, 7), List.of(12, 15)),
                        List.of(1, 1, 3, 4),
                        List.of("O", "X", "O", "X", "-", "O", "O", "-", "-", "-", "-", "-", "O", "-", "-", "O", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 8 #2",
                        List.of("O", "X", "O", "X", "-", "O", "O", "-", "-", "-", "-", "X", "O", "-", "-", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 2), List.of(4, 7), List.of(12, 15)),
                        List.of(1, 1, 3, 4),
                        List.of("O", "X", "O", "X", "-", "O", "O", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 17",
                        List.of("X", "O", "O", "X", "X", "O", "X", "X", "X", "X", "X", "X", "O", "X", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(1, 2), List.of(5, 5), List.of(12, 12), List.of(14, 19)),
                        List.of(2, 1, 1, 6),
                        List.of("X", "O", "O", "X", "X", "O", "X", "X", "X", "X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 5",
                        List.of("X", "X", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 4)),
                        List.of(3),
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 9",
                        List.of("X", "X", "O", "X", "X", "O", "O", "O", "-", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 2), List.of(4, 8), List.of(13, 14)),
                        List.of(1, 4, 2),
                        List.of("X", "X", "O", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 7",
                        List.of("O", "O", "O", "X", "O", "-", "X", "X", "-", "-", "-", "-", "O", "-", "X", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 13), List.of(11, 16), List.of(15, 19)),
                        List.of(3, 2, 2, 1),
                        List.of("O", "O", "O", "X", "O", "O", "X", "X", "-", "-", "-", "-", "O", "-", "X", "O", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 15 #2",
                        List.of("X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "X", "O", "-", "-", "-", "-", "X", "X", "X"),
                        List.of(List.of(1, 6), List.of(8, 16)),
                        List.of(6, 5),
                        List.of("X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "-", "X", "O", "O", "O", "O", "O", "X", "X", "X")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 11",
                        List.of("X", "O", "O", "X", "-", "O", "O", "X", "X", "O", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(1, 2), List.of(4, 9), List.of(8, 19)),
                        List.of(2, 2, 9),
                        List.of("X", "O", "O", "X", "-", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o07834 / 20x20 / diff 2.0 / Row 12",
                        List.of("X", "O", "X", "X", "X", "X", "O", "X", "X", "X", "-", "X", "O", "O", "X", "X", "O", "-", "-", "-"),
                        List.of(List.of(1, 1), List.of(6, 6), List.of(8, 13), List.of(11, 19)),
                        List.of(1, 1, 2, 3),
                        List.of("X", "O", "X", "X", "X", "X", "O", "X", "X", "X", "-", "X", "O", "O", "X", "X", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 2",
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "X", "-", "X", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 11), List.of(11, 17)),
                        List.of(7, 5),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "X", "-", "X", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 4",
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "-", "-", "-", "-"),
                        List.of(List.of(5, 7), List.of(11, 19)),
                        List.of(3, 5),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 1",
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(12, 16)),
                        List.of(9, 5),
                        List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "-", "-", "-")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 10 #1",
                        List.of("O", "O", "X", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(3, 10), List.of(10, 19)),
                        List.of(2, 6, 1),
                        List.of("O", "O", "X", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 12",
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "-", "O", "-", "O", "-", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(1, 11), List.of(9, 19)),
                        List.of(8, 7),
                        List.of("X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "O", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 9 #1",
                        List.of("O", "O", "O", "X", "-", "O", "-", "-", "-", "-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 6), List.of(7, 12), List.of(10, 19)),
                        List.of(3, 2, 2, 2),
                        List.of("O", "O", "O", "X", "-", "O", "-", "-", "-", "-", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 9 #2",
                        List.of("O", "O", "O", "X", "X", "O", "-", "X", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 6), List.of(8, 12), List.of(11, 19)),
                        List.of(3, 2, 2, 2),
                        List.of("O", "O", "O", "X", "X", "O", "O", "X", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 10 #2",
                        List.of("O", "O", "X", "X", "O", "O", "O", "O", "O", "-", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(4, 9), List.of(11, 11)),
                        List.of(2, 6, 1),
                        List.of("O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 19 #1",
                        List.of("-", "X", "O", "O", "O", "-", "-", "X", "X", "-", "-", "-", "-", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 6), List.of(9, 19)),
                        List.of(5, 4),
                        List.of("-", "X", "O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "-", "X", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07923 / 20x20 / diff 2.0 / Row 19 #2",
                        List.of("X", "X", "O", "O", "O", "O", "O", "X", "X", "-", "-", "O", "-", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(2, 6), List.of(9, 19)),
                        List.of(5, 4),
                        List.of("X", "X", "O", "O", "O", "O", "O", "X", "X", "-", "-", "O", "O", "X", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 9",
                        List.of("-", "X", "O", "O", "X", "-", "-", "-", "O", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 3), List.of(5, 17)),
                        List.of(2, 10),
                        List.of("-", "X", "O", "O", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 13",
                        List.of("X", "X", "X", "O", "-", "O", "O", "O", "O", "-", "O", "O", "-", "-", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(3, 11)),
                        List.of(9),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 14",
                        List.of("X", "X", "-", "-", "-", "-", "O", "O", "O", "-", "O", "-", "-", "-", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(2, 14)),
                        List.of(9),
                        List.of("X", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 12",
                        List.of("-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "O", "-", "-", "-", "X", "X", "X"),
                        List.of(List.of(0, 16)),
                        List.of(12),
                        List.of("-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "X", "X", "X")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 10",
                        List.of("X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X"),
                        List.of(List.of(2, 3), List.of(5, 15)),
                        List.of(2, 10),
                        List.of("X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 16",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "X", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 9), List.of(9, 16), List.of(14, 19)),
                        List.of(3, 1, 4, 2),
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 17",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(9, 14)),
                        List.of(5),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 2",
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "-", "X", "X", "-", "-", "X", "O", "O"),
                        List.of(List.of(0, 4), List.of(11, 16), List.of(18, 19)),
                        List.of(5, 2, 2),
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "-", "-", "X", "O", "O")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 11",
                        List.of("X", "X", "O", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X"),
                        List.of(List.of(2, 2), List.of(4, 16)),
                        List.of(1, 11),
                        List.of("X", "X", "O", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 0",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "O", "O", "-", "-", "X", "X", "X"),
                        List.of(List.of(13, 16)),
                        List.of(4),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "O", "O", "O", "O", "X", "X", "X")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 5",
                        List.of("X", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O"),
                        List.of(List.of(0, 7), List.of(18, 19)),
                        List.of(7, 2),
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 15 #1",
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "-", "-", "O", "O", "O", "X", "X", "X", "-", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(4, 8), List.of(9, 12), List.of(16, 19)),
                        List.of(2, 4, 3, 3),
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "O", "-", "O", "O", "O", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of(
                        "o07702 / 20x20 / diff 2.0 / Row 15 #2",
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(5, 8), List.of(10, 12), List.of(16, 19)),
                        List.of(2, 4, 3, 3),
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / diff 2.0 / 20x20 / Row 11",
                        List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(3, 19)),
                        List.of(2, 10),
                        List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / diff 2.0 / 20x20 / Row 3",
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "X", "O", "X", "O", "O", "X", "O", "O", "-", "X", "X"),
                        List.of(List.of(2, 8), List.of(7, 10), List.of(12, 13), List.of(15, 17)),
                        List.of(2, 1, 2, 3),
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "X", "O", "X", "O", "O", "X", "O", "O", "O", "X", "X")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / diff 2.0 / 20x20 / Row 2",
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "O", "O", "-", "O", "X", "O", "-", "O", "-", "-", "-"),
                        List.of(List.of(2, 7), List.of(6, 12), List.of(12, 17), List.of(16, 19)),
                        List.of(1, 4, 4, 1),
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "O", "O", "-", "O", "X", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / diff 2.0 / 20x20 / Row 0",
                        List.of("X", "X", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "O", "O", "O", "-", "X", "-", "O", "-"),
                        List.of(List.of(7, 10), List.of(12, 15), List.of(16, 19)),
                        List.of(4, 4, 3),
                        List.of("X", "X", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "-", "O", "O")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / diff 2.0 / 20x20 / Row 4",
                        List.of("X", "X", "X", "-", "-", "X", "-", "O", "O", "O", "O", "X", "O", "X", "X", "X", "O", "O", "O", "-"),
                        List.of(List.of(6, 10), List.of(11, 12), List.of(15, 19)),
                        List.of(4, 1, 4),
                        List.of("X", "X", "X", "-", "-", "X", "-", "O", "O", "O", "O", "X", "O", "X", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / diff 2.0 / 20x20 / Row 15",
                        List.of("-", "-", "X", "O", "O", "X", "X", "-", "-", "X", "X", "X", "X", "X", "X", "O", "-", "X", "X", "X"),
                        List.of(List.of(0, 4), List.of(3, 16)),
                        List.of(2, 2),
                        List.of("-", "-", "X", "O", "O", "X", "X", "-", "-", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / diff 2.0 / 20x20 / Row 9",
                        List.of("X", "X", "O", "-", "-", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "-"),
                        List.of(List.of(2, 5), List.of(7, 19)),
                        List.of(4, 1),
                        List.of("X", "X", "O", "O", "O", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "-")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / diff 2.0 / 20x20 / Row 19",
                        List.of("X", "X", "X", "X", "X", "O", "O", "-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(5, 8)),
                        List.of(4),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 9",
                        List.of("-", "-", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(2, 2), List.of(7, 18)),
                        List.of(1, 11),
                        List.of("-", "-", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 6 #1",
                        List.of("-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "O", "O", "O", "O", "-", "-", "O", "O", "-", "-"),
                        List.of(List.of(0, 8), List.of(8, 19)),
                        List.of(3, 10),
                        List.of("-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 6 #2",
                        List.of("X", "-", "O", "-", "-", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X"),
                        List.of(List.of(0, 4), List.of(8, 17)),
                        List.of(3, 10),
                        List.of("X", "-", "O", "O", "-", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 19",
                        List.of("X", "X", "O", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O"),
                        List.of(List.of(2, 11), List.of(5, 14), List.of(8, 17), List.of(19, 19)),
                        List.of(2, 2, 2, 1),
                        List.of("X", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 18",
                        List.of("O", "X", "X", "O", "X", "X", "O", "-", "O", "-", "-", "X", "X", "O", "O", "O", "O", "X", "X", "O"),
                        List.of(List.of(0, 0), List.of(3, 3), List.of(6, 8), List.of(13, 16), List.of(19, 19)),
                        List.of(1, 1, 3, 4, 1),
                        List.of("O", "X", "X", "O", "X", "X", "O", "O", "O", "-", "-", "X", "X", "O", "O", "O", "O", "X", "X", "O")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 15",
                        List.of("X", "X", "O", "O", "-", "-", "X", "-", "X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "X", "O"),
                        List.of(List.of(2, 5), List.of(5, 17), List.of(19, 19)),
                        List.of(3, 1, 1),
                        List.of("X", "X", "O", "O", "O", "-", "X", "-", "X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "X", "O")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 17 #1",
                        List.of("O", "O", "X", "X", "X", "O", "-", "X", "-", "O", "-", "-", "O", "O", "-", "-", "O", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(5, 6), List.of(8, 10), List.of(11, 14), List.of(15, 17)),
                        List.of(2, 2, 2, 3, 2),
                        List.of("O", "O", "X", "X", "X", "O", "O", "X", "-", "O", "-", "-", "O", "O", "-", "-", "O", "-", "X", "X")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 17 #2",
                        List.of("O", "O", "X", "X", "X", "O", "O", "X", "O", "O", "-", "-", "O", "O", "X", "X", "O", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(5, 6), List.of(8, 10), List.of(11, 14), List.of(15, 17)),
                        List.of(2, 2, 2, 3, 2),
                        List.of("O", "O", "X", "X", "X", "O", "O", "X", "O", "O", "-", "-", "O", "O", "X", "X", "O", "O", "X", "X")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / diff 2.0 / 20x20 / Row 11",
                        List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 4), List.of(3, 19)),
                        List.of(2, 10),
                        List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / 20x20 / Row 3",
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "X", "O", "X", "O", "O", "X", "O", "O", "-", "X", "X"),
                        List.of(List.of(2, 8), List.of(7, 10), List.of(12, 13), List.of(15, 17)),
                        List.of(2, 1, 2, 3),
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "X", "O", "X", "O", "O", "X", "O", "O", "O", "X", "X")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / 20x20 / Row 2",
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "O", "O", "-", "O", "X", "O", "-", "O", "-", "-", "-"),
                        List.of(List.of(2, 7), List.of(6, 12), List.of(12, 17), List.of(16, 19)),
                        List.of(1, 4, 4, 1),
                        List.of("X", "X", "-", "-", "-", "-", "-", "O", "-", "O", "O", "-", "O", "X", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / 20x20 / Row 0",
                        List.of("X", "X", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "O", "O", "O", "-", "X", "-", "O", "-"),
                        List.of(List.of(7, 10), List.of(12, 15), List.of(16, 19)),
                        List.of(4, 4, 3),
                        List.of("X", "X", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "-", "O", "O")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / 20x20 / Row 4",
                        List.of("X", "X", "X", "-", "-", "X", "-", "O", "O", "O", "O", "X", "O", "X", "X", "X", "O", "O", "O", "-"),
                        List.of(List.of(6, 10), List.of(11, 12), List.of(15, 19)),
                        List.of(4, 1, 4),
                        List.of("X", "X", "X", "-", "-", "X", "-", "O", "O", "O", "O", "X", "O", "X", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / 20x20 / Row 17",
                        List.of("O", "O", "X", "-", "-", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "-"),
                        List.of(List.of(0, 1), List.of(3, 7), List.of(6, 17)),
                        List.of(2, 2, 9),
                        List.of("O", "O", "X", "-", "-", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "-")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / 20x20 / Row 15",
                        List.of("-", "-", "X", "O", "O", "X", "X", "-", "-", "X", "X", "X", "X", "X", "X", "O", "-", "X", "X", "X"),
                        List.of(List.of(0, 4), List.of(3, 16)),
                        List.of(2, 2),
                        List.of("-", "-", "X", "O", "O", "X", "X", "-", "-", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / 20x20 / Row 9",
                        List.of("X", "X", "O", "-", "-", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "-"),
                        List.of(List.of(2, 5), List.of(7, 19)),
                        List.of(4, 1),
                        List.of("X", "X", "O", "O", "O", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "-")
                ),
                Arguments.of(
                        "o08436 / diff 2.0 / 20x20 / Row 19",
                        List.of("X", "X", "X", "X", "X", "O", "O", "-", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(5, 8)),
                        List.of(4),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 9",
                        List.of("-", "-", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "-", "-"),
                        List.of(List.of(2, 2), List.of(7, 18)),
                        List.of(1, 11),
                        List.of("-", "-", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 6",
                        List.of("-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "O", "O", "O", "O", "-", "-", "O", "O", "-", "-"),
                        List.of(List.of(0, 8), List.of(8, 19)),
                        List.of(3, 10),
                        List.of("-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 6",
                        List.of("X", "-", "O", "-", "-", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X"),
                        List.of(List.of(0, 4), List.of(8, 17)),
                        List.of(3, 10),
                        List.of("X", "-", "O", "O", "-", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 19",
                        List.of("X", "X", "O", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O"),
                        List.of(List.of(2, 11), List.of(5, 14), List.of(8, 17), List.of(19, 19)),
                        List.of(2, 2, 2, 1),
                        List.of("X", "X", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 18",
                        List.of("O", "X", "X", "O", "X", "X", "O", "-", "O", "-", "-", "X", "X", "O", "O", "O", "O", "X", "X", "O"),
                        List.of(List.of(0, 0), List.of(3, 3), List.of(6, 8), List.of(13, 16), List.of(19, 19)),
                        List.of(1, 1, 3, 4, 1),
                        List.of("O", "X", "X", "O", "X", "X", "O", "O", "O", "-", "-", "X", "X", "O", "O", "O", "O", "X", "X", "O")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 15",
                        List.of("X", "X", "O", "O", "-", "-", "X", "-", "X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "X", "O"),
                        List.of(List.of(2, 5), List.of(5, 17), List.of(19, 19)),
                        List.of(3, 1, 1),
                        List.of("X", "X", "O", "O", "O", "-", "X", "-", "X", "-", "-", "-", "-", "X", "X", "X", "X", "-", "X", "O")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 17",
                        List.of("O", "O", "X", "X", "X", "O", "-", "X", "-", "O", "-", "-", "O", "O", "-", "-", "O", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(5, 6), List.of(8, 10), List.of(11, 14), List.of(15, 17)),
                        List.of(2, 2, 2, 3, 2),
                        List.of("O", "O", "X", "X", "X", "O", "O", "X", "-", "O", "-", "-", "O", "O", "-", "-", "O", "-", "X", "X")
                ),
                Arguments.of(
                        "o07924 / diff 2.0 / 20x20 / Row 17",
                        List.of("O", "O", "X", "X", "X", "O", "O", "X", "O", "O", "-", "-", "O", "O", "X", "X", "O", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(5, 6), List.of(8, 10), List.of(11, 14), List.of(15, 17)),
                        List.of(2, 2, 2, 3, 2),
                        List.of("O", "O", "X", "X", "X", "O", "O", "X", "O", "O", "-", "-", "O", "O", "X", "X", "O", "O", "X", "X")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 2",
                        List.of("-", "-", "-", "O", "O", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-"),
                        List.of(List.of(0, 7), List.of(6, 16), List.of(17, 19)),
                        List.of(5, 2, 2),
                        List.of("-", "-", "-", "O", "O", "X", "X", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 9",
                        List.of("-", "X", "O", "O", "X", "-", "-", "-", "O", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 3), List.of(5, 17)),
                        List.of(2, 10),
                        List.of("-", "X", "O", "O", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 13",
                        List.of("X", "X", "X", "O", "-", "O", "O", "O", "O", "-", "O", "O", "-", "-", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(3, 11)),
                        List.of(9),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 14",
                        List.of("X", "X", "-", "-", "-", "-", "O", "O", "O", "-", "O", "-", "-", "-", "-", "-", "X", "X", "X", "X"),
                        List.of(List.of(2, 14)),
                        List.of(9),
                        List.of("X", "X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 12",
                        List.of("-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "O", "-", "-", "-", "X", "X", "X"),
                        List.of(List.of(0, 16)),
                        List.of(12),
                        List.of("-", "X", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "X", "X", "X")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 10",
                        List.of("X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X"),
                        List.of(List.of(2, 3), List.of(5, 15)),
                        List.of(2, 10),
                        List.of("X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 16",
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "X", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(4, 9), List.of(9, 16), List.of(14, 19)),
                        List.of(3, 1, 4, 2),
                        List.of("-", "O", "-", "-", "-", "-", "-", "-", "X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 17",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(9, 14)),
                        List.of(5),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 2",
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "-", "X", "X", "-", "-", "X", "O", "O"),
                        List.of(List.of(0, 4), List.of(11, 16), List.of(18, 19)),
                        List.of(5, 2, 2),
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "-", "-", "X", "O", "O")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 11",
                        List.of("X", "X", "O", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X"),
                        List.of(List.of(2, 2), List.of(4, 16)),
                        List.of(1, 11),
                        List.of("X", "X", "O", "X", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 0",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "O", "O", "-", "-", "X", "X", "X"),
                        List.of(List.of(13, 16)),
                        List.of(4),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "X", "O", "O", "O", "O", "X", "X", "X")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 5",
                        List.of("X", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O"),
                        List.of(List.of(0, 7), List.of(18, 19)),
                        List.of(7, 2),
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 15",
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "-", "-", "O", "O", "O", "X", "X", "X", "-", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(4, 8), List.of(9, 12), List.of(16, 19)),
                        List.of(2, 4, 3, 3),
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "O", "-", "O", "O", "O", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of(
                        "o07571 / diff 2.0 / 20x20 / Row 15",
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "O", "O", "-"),
                        List.of(List.of(0, 1), List.of(5, 8), List.of(10, 12), List.of(16, 19)),
                        List.of(2, 4, 3, 3),
                        List.of("O", "O", "X", "X", "X", "O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 10",
                        List.of("X", "O", "-", "-", "-", "X", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(3, 6), List.of(6, 10), List.of(9, 19)),
                        List.of(2, 1, 1, 2),
                        List.of("X", "O", "O", "-", "-", "X", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 12",
                        List.of("X", "-", "-", "O", "O", "X", "O", "-", "-", "-", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 7), List.of(4, 11), List.of(7, 17), List.of(10, 19)),
                        List.of(2, 2, 1, 1),
                        List.of("X", "-", "-", "O", "O", "X", "O", "O", "-", "-", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 13",
                        List.of("X", "X", "X", "-", "O", "O", "O", "O", "-", "X", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(3, 8), List.of(9, 12)),
                        List.of(5, 3),
                        List.of("X", "X", "X", "-", "O", "O", "O", "O", "-", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 7",
                        List.of("O", "O", "X", "X", "-", "O", "-", "X", "O", "O", "O", "X", "X", "-", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(4, 6), List.of(8, 11), List.of(13, 16), List.of(16, 19)),
                        List.of(2, 3, 3, 1, 1),
                        List.of("O", "O", "X", "X", "-", "O", "O", "X", "O", "O", "O", "X", "X", "-", "-", "-", "O", "-", "-", "-")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 3",
                        List.of("X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(3, 17), List.of(5, 19)),
                        List.of(2, 1, 1),
                        List.of("X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 1",
                        List.of("-", "X", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-"),
                        List.of(List.of(2, 4), List.of(5, 13), List.of(11, 19)),
                        List.of(3, 5, 1),
                        List.of("-", "X", "O", "O", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 4",
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "-", "-", "X"),
                        List.of(List.of(0, 1), List.of(13, 14), List.of(16, 18)),
                        List.of(2, 2, 3),
                        List.of("O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "O", "X")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 0",
                        List.of("X", "X", "X", "X", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "X", "-", "X", "-", "-", "-"),
                        List.of(List.of(3, 13)),
                        List.of(8),
                        List.of("X", "X", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "X", "-", "X", "-", "-", "-")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 10",
                        List.of("X", "O", "O", "X", "X", "X", "O", "X", "X", "X", "O", "X", "O", "-", "X", "-", "-", "X", "X", "-"),
                        List.of(List.of(1, 2), List.of(6, 6), List.of(10, 10), List.of(12, 16)),
                        List.of(2, 1, 1, 2),
                        List.of("X", "O", "O", "X", "X", "X", "O", "X", "X", "X", "O", "X", "O", "O", "X", "-", "-", "X", "X", "-")
                ),
                Arguments.of(
                        "o07806 / diff 2.0 / 20x20 / Row 0",
                        List.of("X", "X", "X", "X", "O", "-", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(4, 11)),
                        List.of(8),
                        List.of("X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X")
                )
        );
    }
}
