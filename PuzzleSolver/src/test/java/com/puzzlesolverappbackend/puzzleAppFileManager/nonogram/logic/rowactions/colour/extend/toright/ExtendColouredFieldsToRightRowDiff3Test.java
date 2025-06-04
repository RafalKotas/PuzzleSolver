package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.extend.toright;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class ExtendColouredFieldsToRightRowDiff3Test
        extends ExtendColouredFieldsToRightRowTestBase
        implements ExtendColouredFieldsToRightRowTestExecutor  {

    static Stream<Arguments> provideTestCasesForExtendRight() {
        return Stream.of(
                Arguments.of(
                        "o10155 / diff 3.0 / 15x20 / Row 9",
                        List.of("-", "-", "-", "O", "-", "O", "O", "X", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 7), List.of(8, 10)),
                        List.of(6, 3),
                        List.of("-", "-", "-", "O", "-", "O", "O", "X", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o10155 / diff 3.0 / 15x20 / Row 10",
                        List.of("-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 12)),
                        List.of(10),
                        List.of("-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o10155 / diff 3.0 / 15x20 / Row 15",
                        List.of("-", "-", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(1, 6), List.of(8, 13)),
                        List.of(6, 6),
                        List.of("-", "-", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o10155 / diff 3.0 / 15x20 / Row 18",
                        List.of("-", "-", "-", "O", "X", "X", "O", "O", "X", "X", "O", "-", "-", "-", "-"),
                        List.of(List.of(0, 3), List.of(6, 7), List.of(10, 14)),
                        List.of(3, 2, 4),
                        List.of("-", "-", "-", "O", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o10155 / diff 3.0 / 15x20 / Row 5",
                        List.of("-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 1), List.of(3, 12)),
                        List.of(1, 8),
                        List.of("-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X")
                ),
                Arguments.of(
                        "o10155 / diff 3.0 / 15x20 / Row 7",
                        List.of("X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "X", "X", "O", "-"),
                        List.of(List.of(3, 3), List.of(5, 10), List.of(12, 14)),
                        List.of(1, 6, 2),
                        List.of("X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o10155 / diff 3.0 / 15x20 / Row 1",
                        List.of("-", "O", "O", "-", "X", "X", "O", "X", "X", "X", "X", "-", "O", "-", "-"),
                        List.of(List.of(0, 3), List.of(6, 10), List.of(10, 14)),
                        List.of(3, 1, 3),
                        List.of("-", "O", "O", "-", "X", "X", "O", "X", "X", "X", "X", "-", "O", "O", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 19",
                        List.of("-", "-", "-", "X", "X", "-", "X", "O", "-", "O", "X", "-", "-", "-", "X", "X", "X", "X", "-", "-"),
                        List.of(List.of(0, 9), List.of(7, 13), List.of(11, 19)),
                        List.of(3, 3, 2),
                        List.of("-", "-", "-", "X", "X", "-", "X", "O", "O", "O", "X", "-", "-", "-", "X", "X", "X", "X", "-", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 15",
                        List.of("X", "X", "O", "-", "X", "X", "X", "O", "O", "X", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(2, 3), List.of(7, 8), List.of(11, 19)),
                        List.of(2, 2, 1),
                        List.of("X", "X", "O", "O", "X", "X", "X", "O", "O", "X", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 2",
                        List.of("X", "-", "-", "-", "-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 7), List.of(6, 19)),
                        List.of(2, 2),
                        List.of("X", "-", "-", "-", "-", "X", "O", "O", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 0",
                        List.of("X", "-", "-", "-", "-", "X", "X", "O", "-", "X", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 8), List.of(7, 19)),
                        List.of(2, 2),
                        List.of("X", "-", "-", "-", "-", "X", "X", "O", "O", "X", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 8",
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "O", "-", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 9), List.of(8, 13), List.of(11, 19)),
                        List.of(2, 2, 2),
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "O", "O", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 8",
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "O", "O", "X", "O", "-", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(1, 9), List.of(8, 12), List.of(11, 19)),
                        List.of(2, 2, 2),
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "O", "O", "X", "O", "O", "X", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 11",
                        List.of("X", "X", "X", "O", "-", "X", "X", "O", "X", "X", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 4), List.of(7, 7), List.of(11, 11)),
                        List.of(2, 1, 1),
                        List.of("X", "X", "X", "O", "O", "X", "X", "O", "X", "X", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 6",
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "X", "O", "O", "X", "X", "O", "-", "X", "X", "-", "O", "-"),
                        List.of(List.of(1, 4), List.of(9, 10), List.of(13, 14), List.of(17, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "-", "-", "-", "-", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X", "-", "O", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 5",
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "O", "O", "O", "O", "-", "-"),
                        List.of(List.of(2, 10), List.of(12, 19)),
                        List.of(9, 6),
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 7",
                        List.of("X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "O", "-", "X", "X", "-", "-", "X", "-"),
                        List.of(List.of(2, 3), List.of(9, 9), List.of(12, 13)),
                        List.of(2, 1, 2),
                        List.of("X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "O", "O", "X", "X", "-", "-", "X", "-")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 5",
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "-"),
                        List.of(List.of(2, 10), List.of(14, 19)),
                        List.of(9, 6),
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07959 / diff 3.0 / 20x20 / Row 6",
                        List.of("X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "-"),
                        List.of(List.of(2, 3), List.of(9, 10), List.of(13, 14), List.of(18, 19)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o07940 / diff 3.0 / 20x20 / Row 12",
                        List.of("-", "-", "O", "O", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-", "O", "X"),
                        List.of(List.of(0, 9), List.of(9, 14), List.of(16, 18)),
                        List.of(8, 2, 3),
                        List.of("-", "-", "O", "O", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "X")
                ),
                Arguments.of(
                        "o07940 / diff 3.0 / 20x20 / Row 2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "O", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 11), List.of(10, 19)),
                        List.of(6, 7),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "O", "O", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07940 / diff 3.0 / 20x20 / Row 5",
                        List.of("-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "-", "X", "X", "O", "O", "X", "X", "X", "O", "-"),
                        List.of(List.of(0, 10), List.of(13, 14), List.of(17, 19)),
                        List.of(3, 2, 2),
                        List.of("-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "-", "X", "X", "O", "O", "X", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o07940 / diff 3.0 / 20x20 / Row 15",
                        List.of("-", "X", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "O", "-"),
                        List.of(List.of(2, 6), List.of(8, 13), List.of(15, 16), List.of(18, 19)),
                        List.of(5, 3, 2, 2),
                        List.of("-", "X", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "O", "O")
                ),
                Arguments.of(
                        "o07940 / diff 3.0 / 20x20 / Row 8",
                        List.of("-", "-", "-", "X", "X", "O", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "O", "X", "X"),
                        List.of(List.of(4, 6), List.of(17, 17)),
                        List.of(2, 1),
                        List.of("-", "-", "-", "X", "X", "O", "O", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "O", "X", "X")
                ),
                Arguments.of(
                        "o07940 / diff 3.0 / 20x20 / Row 16",
                        List.of("X", "O", "O", "O", "O", "O", "X", "O", "-", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "-"),
                        List.of(List.of(1, 5), List.of(7, 8), List.of(10, 14), List.of(15, 19)),
                        List.of(5, 2, 3, 4),
                        List.of("X", "O", "O", "O", "O", "O", "X", "O", "O", "-", "-", "-", "O", "-", "-", "-", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o07940 / diff 3.0 / 20x20 / Row 19",
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "-", "X"),
                        List.of(List.of(1, 2), List.of(15, 18)),
                        List.of(2, 4),
                        List.of("X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "O", "X")
                ),
                Arguments.of(
                        "o07940 / diff 3.0 / 20x20 / Row 15",
                        List.of("X", "X", "O", "O", "O", "O", "O", "X", "O", "-", "O", "X", "X", "-", "X", "O", "O", "X", "O", "O"),
                        List.of(List.of(2, 6), List.of(8, 10), List.of(15, 16), List.of(18, 19)),
                        List.of(5, 3, 2, 2),
                        List.of("X", "X", "O", "O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "-", "X", "O", "O", "X", "O", "O")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 2",
                        List.of("-", "-", "-", "-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 10), List.of(5, 16), List.of(10, 19)),
                        List.of(3, 5, 2),
                        List.of("-", "-", "-", "-", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 18",
                        List.of("X", "-", "-", "X", "-", "-", "X", "X", "X", "X", "-", "O", "-", "X", "O", "-", "X", "X", "-", "X"),
                        List.of(List.of(1, 5), List.of(4, 9), List.of(10, 12), List.of(13, 15)),
                        List.of(2, 2, 2, 2),
                        List.of("X", "-", "-", "X", "-", "-", "X", "X", "X", "X", "-", "O", "-", "X", "O", "O", "X", "X", "-", "X")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 8",
                        List.of("-", "-", "-", "-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 7), List.of(5, 12), List.of(8, 15), List.of(11, 19)),
                        List.of(3, 2, 2, 3),
                        List.of("-", "-", "-", "-", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 9",
                        List.of("-", "-", "-", "-", "-", "X", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 11), List.of(6, 15), List.of(9, 19)),
                        List.of(4, 3, 3),
                        List.of("-", "-", "-", "-", "-", "X", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 9",
                        List.of("X", "O", "O", "O", "O", "X", "X", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 4), List.of(6, 9), List.of(10, 19)),
                        List.of(4, 3, 3),
                        List.of("X", "O", "O", "O", "O", "X", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 5",
                        List.of("O", "-", "-", "-", "X", "X", "X", "X", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 8), List.of(8, 16), List.of(11, 19)),
                        List.of(1, 4, 2),
                        List.of("O", "-", "-", "-", "X", "X", "X", "X", "O", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 12",
                        List.of("X", "-", "-", "-", "-", "X", "O", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 9), List.of(6, 19)),
                        List.of(4, 5),
                        List.of("X", "-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 7",
                        List.of("-", "-", "X", "O", "O", "O", "O", "X", "-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(3, 6), List.of(8, 12), List.of(12, 19)),
                        List.of(1, 4, 3, 2),
                        List.of("-", "-", "X", "O", "O", "O", "O", "X", "-", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 15",
                        List.of("X", "X", "O", "-", "-", "O", "O", "-", "X", "O", "-", "X", "O", "-", "O", "-", "-", "-", "O", "-"),
                        List.of(List.of(2, 7), List.of(9, 10), List.of(12, 16), List.of(17, 19)),
                        List.of(5, 2, 3, 2),
                        List.of("X", "X", "O", "O", "O", "O", "O", "-", "X", "O", "O", "X", "O", "O", "O", "-", "-", "-", "O", "-")
                ),
                Arguments.of(
                        "o10357 / diff 3.0 / 20x20 / Row 19",
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "-", "O", "X", "-", "X", "X", "X"),
                        List.of(List.of(2, 4), List.of(12, 14)),
                        List.of(3, 3),
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "X", "X", "X")
                ),
                Arguments.of(
                        "o07982 / diff 3.0 / 20x20 / Row 15 #1",
                        List.of("-", "O", "-", "-", "O", "-", "X", "O", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 2), List.of(3, 5), List.of(7, 11), List.of(19, 19)),
                        List.of(2, 2, 5, 1),
                        List.of("-", "O", "-", "-", "O", "-", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of(
                        "o07982 / diff 3.0 / 20x20 / Row 15 #2",
                        List.of("-", "O", "-", "X", "O", "-", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of(List.of(0, 2), List.of(3, 5), List.of(7, 11), List.of(19, 19)),
                        List.of(2, 2, 5, 1),
                        List.of("-", "O", "-", "X", "O", "O", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O")
                ),
                Arguments.of(
                        "o07982 / diff 3.0 / 20x20 / Row 4",
                        List.of("-", "O", "-", "X", "X", "X", "-", "-", "X", "O", "O", "O", "-", "-", "-", "X", "O", "O", "O", "O"),
                        List.of(List.of(1, 1), List.of(7, 13), List.of(16, 19)),
                        List.of(1, 5, 4),
                        List.of("-", "O", "-", "X", "X", "X", "-", "-", "X", "O", "O", "O", "O", "O", "-", "X", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07982 / diff 3.0 / 20x20 / Row 6",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "O", "-", "X", "X", "O", "O"),
                        List.of(List.of(0, 0), List.of(11, 11), List.of(13, 15), List.of(18, 19)),
                        List.of(1, 1, 2, 2),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "O", "O", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o07982 / diff 3.0 / 20x20 / Row 2",
                        List.of("X", "-", "-", "O", "O", "O", "-", "-", "X", "X", "O", "X", "X", "O", "O", "O", "O", "O", "X", "O"),
                        List.of(List.of(1, 7), List.of(10, 10), List.of(13, 17), List.of(19, 19)),
                        List.of(6, 1, 5, 1),
                        List.of("X", "-", "-", "O", "O", "O", "O", "-", "X", "X", "O", "X", "X", "O", "O", "O", "O", "O", "X", "O")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 2",
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "-", "X", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 5), List.of(3, 10), List.of(8, 17)),
                        List.of(2, 4, 2),
                        List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-", "X", "X")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 3 #1",
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "O", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 3), List.of(3, 11), List.of(13, 17)),
                        List.of(2, 7, 2),
                        List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "X", "O", "O", "-", "-", "-", "X", "X")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 4",
                        List.of("-", "-", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "X"),
                        List.of(List.of(0, 4), List.of(4, 17)),
                        List.of(1, 12),
                        List.of("-", "-", "X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 3 #2",
                        List.of("X", "-", "-", "-", "X", "O", "O", "O", "O", "O", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X"),
                        List.of(List.of(1, 3), List.of(5, 11), List.of(13, 14)),
                        List.of(2, 7, 2),
                        List.of("X", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 6",
                        List.of("O", "O", "O", "O", "X", "X", "-", "O", "-", "X", "X", "O", "O", "O", "O", "O", "-", "X", "X", "X"),
                        List.of(List.of(0, 3), List.of(6, 8), List.of(11, 16)),
                        List.of(4, 2, 6),
                        List.of("O", "O", "O", "O", "X", "X", "-", "O", "-", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 8",
                        List.of("O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "O", "-", "-", "-"),
                        List.of(List.of(0, 2), List.of(4, 12), List.of(13, 19)),
                        List.of(3, 4, 4),
                        List.of("O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 12 #1",
                        List.of("-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "O", "-", "-", "-"),
                        List.of(List.of(2, 10), List.of(6, 12), List.of(8, 19)),
                        List.of(3, 1, 4),
                        List.of("-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 9",
                        List.of("O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "X", "O", "-", "-"),
                        List.of(List.of(0, 1), List.of(3, 12), List.of(7, 15), List.of(9, 19)),
                        List.of(2, 3, 1, 2),
                        List.of("O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "X", "O", "O", "-")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 10",
                        List.of("O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X", "X", "X", "X", "X", "O", "-"),
                        List.of(List.of(0, 1), List.of(3, 8), List.of(7, 12), List.of(10, 19)),
                        List.of(2, 3, 2, 2),
                        List.of("O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "X", "X", "X", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 12 #2",
                        List.of("X", "X", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "O", "O", "O", "O"),
                        List.of(List.of(3, 10), List.of(6, 12), List.of(16, 19)),
                        List.of(3, 1, 4),
                        List.of("X", "X", "X", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 16 #1",
                        List.of("O", "O", "O", "X", "-", "O", "-", "-", "-", "-", "O", "-", "-", "X", "-", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 2), List.of(4, 12), List.of(9, 12)),
                        List.of(3, 3, 2),
                        List.of("O", "O", "O", "X", "-", "O", "O", "-", "-", "-", "O", "-", "-", "X", "-", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 19 #1",
                        List.of("X", "X", "-", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 6), List.of(7, 12)),
                        List.of(4, 3),
                        List.of("X", "X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 16 #2",
                        List.of("O", "O", "O", "X", "X", "O", "O", "-", "X", "-", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 2), List.of(5, 7), List.of(9, 11)),
                        List.of(3, 3, 2),
                        List.of("O", "O", "O", "X", "X", "O", "O", "O", "X", "-", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o11517 / diff 3.0 / 20x20 / Row 19 #2",
                        List.of("X", "X", "-", "O", "O", "O", "-", "X", "O", "-", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(2, 6), List.of(8, 10)),
                        List.of(4, 3),
                        List.of("X", "X", "-", "O", "O", "O", "-", "X", "O", "O", "O", "-", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 15",
                        List.of("X", "-", "O", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(1, 11), List.of(9, 19)),
                        List.of(7, 7),
                        List.of("X", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 16",
                        List.of("X", "X", "O", "-", "-", "O", "O", "X", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-"),
                        List.of(List.of(2, 6), List.of(8, 19)),
                        List.of(5, 7),
                        List.of("X", "X", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 17",
                        List.of("X", "-", "O", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 7), List.of(8, 19)),
                        List.of(6, 6),
                        List.of("X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 18 #1",
                        List.of("X", "-", "O", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-"),
                        List.of(List.of(1, 7), List.of(8, 19)),
                        List.of(6, 5),
                        List.of("X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 18 #2",
                        List.of("X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "X", "O", "O", "-", "-", "-"),
                        List.of(List.of(1, 7), List.of(12, 19)),
                        List.of(6, 5),
                        List.of("X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 19",
                        List.of("X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "O", "-", "-", "-"),
                        List.of(List.of(1, 9), List.of(3, 11), List.of(5, 13), List.of(12, 19)),
                        List.of(1, 1, 1, 5),
                        List.of("X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "O", "O", "O", "O")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 7 #1",
                        List.of("-", "O", "-", "X", "X", "-", "-", "-", "-", "-", "X", "O", "-", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 2), List.of(5, 12), List.of(10, 19)),
                        List.of(2, 2, 2),
                        List.of("-", "O", "-", "X", "X", "-", "-", "-", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 9",
                        List.of("X", "-", "O", "-", "O", "X", "-", "-", "-", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(1, 4), List.of(8, 12)),
                        List.of(4, 4),
                        List.of("X", "-", "O", "O", "O", "X", "-", "-", "-", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 7 #2",
                        List.of("O", "O", "X", "X", "X", "O", "-", "X", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(5, 9), List.of(11, 12)),
                        List.of(2, 2, 2),
                        List.of("O", "O", "X", "X", "X", "O", "O", "X", "-", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 2",
                        List.of("X", "O", "O", "-", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 3), List.of(9, 11)),
                        List.of(3, 3),
                        List.of("X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07490 / diff 3.0 / 20x20 / Row 5",
                        List.of("O", "O", "X", "X", "X", "X", "X", "O", "O", "-", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of(List.of(0, 1), List.of(6, 9), List.of(11, 12)),
                        List.of(2, 3, 2),
                        List.of("O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X")
                ),
                Arguments.of(
                        "o07849 / diff 3.0 / 20x20 / Row 9",
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "O", "X", "X", "O", "-", "O", "-", "-"),
                        List.of(List.of(9, 12), List.of(15, 19)),
                        List.of(4, 3),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "-", "-", "-", "O", "X", "X", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o07849 / diff 3.0 / 20x20 / Row 8",
                        List.of("-", "O", "O", "O", "-", "X", "O", "O", "O", "O", "X", "X", "-", "-", "-", "X", "O", "O", "-", "-"),
                        List.of(List.of(0, 4), List.of(6, 9), List.of(12, 14), List.of(16, 19)),
                        List.of(4, 4, 2, 3),
                        List.of("-", "O", "O", "O", "-", "X", "O", "O", "O", "O", "X", "X", "-", "-", "-", "X", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o07849 / diff 3.0 / 20x20 / Row 4",
                        List.of("O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "X", "X", "O", "-", "-", "X", "X", "O", "O"),
                        List.of(List.of(0, 6), List.of(9, 15), List.of(18, 19)),
                        List.of(7, 2, 2),
                        List.of("O", "O", "O", "O", "O", "O", "O", "X", "X", "-", "-", "X", "X", "O", "O", "-", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o07849 / diff 3.0 / 20x20 / Row 6",
                        List.of("O", "X", "-", "X", "-", "-", "X", "-", "-", "-", "-", "X", "X", "O", "-", "-", "X", "O", "O", "O"),
                        List.of(List.of(0, 0), List.of(4, 15), List.of(17, 19)),
                        List.of(1, 2, 3),
                        List.of("O", "X", "-", "X", "-", "-", "X", "-", "-", "-", "-", "X", "X", "O", "O", "-", "X", "O", "O", "O")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 27",
                        List.of("O", "-", "-", "-", "X", "O", "O", "-", "-", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-"),
                        List.of(List.of(0, 1), List.of(5, 7), List.of(14, 15), List.of(17, 19)),
                        List.of(2, 3, 2, 1),
                        List.of("O", "-", "-", "-", "X", "O", "O", "O", "-", "-", "-", "-", "-", "X", "O", "O", "X", "-", "-", "-")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 11",
                        List.of("-", "X", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-"),
                        List.of(List.of(2, 19)),
                        List.of(16),
                        List.of("-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 29",
                        List.of("-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                        List.of(List.of(2, 19)),
                        List.of(17),
                        List.of("-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 21",
                        List.of("O", "X", "O", "-", "-", "-", "-", "X", "-", "O", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(2, 3), List.of(5, 9), List.of(7, 13), List.of(9, 19)),
                        List.of(1, 2, 1, 1, 3),
                        List.of("O", "X", "O", "O", "-", "-", "-", "X", "-", "O", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 23",
                        List.of("O", "X", "X", "O", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(3, 9), List.of(5, 11), List.of(8, 13), List.of(10, 19)),
                        List.of(1, 2, 1, 1, 4),
                        List.of("O", "X", "X", "O", "O", "-", "-", "X", "-", "-", "-", "-", "-", "-", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 28",
                        List.of("O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "-"),
                        List.of(List.of(0, 2), List.of(5, 6), List.of(14, 15), List.of(18, 19)),
                        List.of(3, 2, 2, 2),
                        List.of("O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 19",
                        List.of("O", "O", "O", "X", "-", "-", "-", "X", "X", "-", "O", "-", "-", "-", "X", "X", "-", "X", "O", "O"),
                        List.of(List.of(0, 2), List.of(8, 12), List.of(18, 19)),
                        List.of(3, 3, 2),
                        List.of("O", "O", "O", "X", "-", "-", "-", "X", "X", "-", "O", "O", "-", "-", "X", "X", "-", "X", "O", "O")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 14",
                        List.of("O", "O", "O", "X", "X", "X", "-", "-", "-", "X", "O", "-", "-", "-", "-", "O", "X", "-", "X", "O"),
                        List.of(List.of(0, 2), List.of(6, 15), List.of(15, 17), List.of(19, 19)),
                        List.of(3, 2, 1, 1),
                        List.of("O", "O", "O", "X", "X", "X", "-", "-", "-", "X", "O", "O", "-", "-", "-", "O", "X", "-", "X", "O")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 9",
                        List.of("X", "X", "O", "O", "-", "X", "O", "O", "X", "X", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-"),
                        List.of(List.of(2, 4), List.of(6, 7), List.of(10, 14), List.of(13, 19)),
                        List.of(3, 2, 2, 2),
                        List.of("X", "X", "O", "O", "O", "X", "O", "O", "X", "X", "-", "-", "X", "O", "O", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 0",
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "O", "-", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-"),
                        List.of(List.of(0, 0), List.of(8, 9)),
                        List.of(1, 2),
                        List.of("O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "-", "-", "-", "-")
                ),
                Arguments.of(
                        "o07804 / diff 3.0 / 30x20 / Row 7",
                        List.of("X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-", "O", "O", "X", "X", "O", "-", "O", "-"),
                        List.of(List.of(5, 6), List.of(12, 13), List.of(16, 19)),
                        List.of(2, 2, 3),
                        List.of("X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "-", "O", "O", "X", "X", "O", "O", "O", "-")
                )
        );
    }
}
