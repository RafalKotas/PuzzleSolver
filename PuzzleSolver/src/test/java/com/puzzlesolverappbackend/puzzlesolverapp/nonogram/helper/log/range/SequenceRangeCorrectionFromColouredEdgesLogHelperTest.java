package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SequenceRangeCorrectionFromColouredEdgesLogHelperTest {

    @Test
    @DisplayName("SequenceRangeCorrectionFromColouredEdgesLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<SequenceRangeCorrectionFromColouredEdgesLogHelper> constructor = SequenceRangeCorrectionFromColouredEdgesLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Generate example log - o10035 column 13")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 13;
        List<List<Integer>> initialRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(3, 5)),
                        new ArrayList<>(Arrays.asList(5, 10)),
                        new ArrayList<>(Arrays.asList(9, 18)),
                        new ArrayList<>(Arrays.asList(15, 29))
                )
        );
        List<List<Integer>> updatedRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(3, 5)),
                        new ArrayList<>(Arrays.asList(6, 10)),
                        new ArrayList<>(Arrays.asList(9, 18)),
                        new ArrayList<>(Arrays.asList(15, 29))
                )
        );
        List<Integer> sequencesLengths = List.of(2, 4, 6, 8);
        List<String> line = new ArrayList<>(List.of("-", "-", "-", "-", "O",
                "-", "-", "O", "O", "-",
                "-", "-", "-", "O", "-",
                "O", "O", "O", "-", "-",
                "-", "-", "O", "-", "-",
                "-", "-", "-", "-", "-")
        );

        // when
        String log = SequenceRangeCorrectionFromColouredEdgesLogHelper.generateLog(isRow,
                index,
                initialRanges,
                updatedRanges,
                sequencesLengths,
                line);

        // then
        String expected =
                """
                        CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES_IN_COLUMN: column=13
                        initialRanges=[[3, 5], [5, 10], [9, 18], [15, 29]]
                        updatedRanges=[[3, 5], [6, 10], [9, 18], [15, 29]]
                        sequencesLengths=[2, 4, 6, 8]
                        line=[-, -, -, -, O, -, -, O, O, -, -, -, -, O, -, O, O, O, -, -, -, -, O, -, -, -, -, -, -, -]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o10035 column 13")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES_IN_COLUMN: column=13
                        initialRanges=[[3, 5], [5, 10], [9, 18], [15, 29]]
                        updatedRanges=[[3, 5], [6, 10], [9, 18], [15, 29]]
                        sequencesLengths=[2, 4, 6, 8]
                        line=[-, -, -, -, O, -, -, O, O, -, -, -, -, O, -, O, O, O, -, -, -, -, O, -, -, -, -, -, -, -]
                        """;

        // when
        String convertedLog = SequenceRangeCorrectionFromColouredEdgesLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10035"
        );

        // then
        String expected = """
                Arguments.of("o10035 / column=13 - correct sequences ranges from coloured edges",
                    new ArrayList<>(List.of(new ArrayList<>(List.of(3, 5)), new ArrayList<>(List.of(5, 10)), new ArrayList<>(List.of(9, 18)), new ArrayList<>(List.of(15, 29)))),
                    List.of(List.of(3, 5), List.of(6, 10), List.of(9, 18), List.of(15, 29)),
                    List.of(2, 4, 6, 8),
                    new ArrayList<>(List.of("-", "-", "-", "-", "O", "-", "-", "O", "O", "-", "-", "-", "-", "O", "-", "O", "O", "O", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("Generate example log - o10035 row 13")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 13;
        List<List<Integer>> initialRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(0, 7)),
                        new ArrayList<>(Arrays.asList(6, 11)),
                        new ArrayList<>(Arrays.asList(12, 25)),
                        new ArrayList<>(Arrays.asList(23, 27)),
                        new ArrayList<>(Arrays.asList(25, 29))
                )
        );
        List<List<Integer>> updatedRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(0, 7)),
                        new ArrayList<>(Arrays.asList(6, 11)),
                        new ArrayList<>(Arrays.asList(12, 25)),
                        new ArrayList<>(Arrays.asList(23, 27)),
                        new ArrayList<>(Arrays.asList(26, 29))
                )
        );
        List<Integer> sequencesLengths = List.of(5, 2, 13, 1, 1);
        List<String> line = new ArrayList<>(List.of("-", "-", "-", "O", "O",
                "-", "-", "-", "-", "-",
                "-", "-", "-", "O", "O",
                "O", "O", "O", "O", "O",
                "O", "O", "-", "O", "O",
                "-", "-", "-", "-", "-")
        );

        // when
        String log = SequenceRangeCorrectionFromColouredEdgesLogHelper.generateLog(isRow,
                index,
                initialRanges,
                updatedRanges,
                sequencesLengths,
                line);

        // then
        String expected =
                """
                        CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES_IN_ROW: row=13
                        initialRanges=[[0, 7], [6, 11], [12, 25], [23, 27], [25, 29]]
                        updatedRanges=[[0, 7], [6, 11], [12, 25], [23, 27], [26, 29]]
                        sequencesLengths=[5, 2, 13, 1, 1]
                        line=[-, -, -, O, O, -, -, -, -, -, -, -, -, O, O, O, O, O, O, O, O, O, -, O, O, -, -, -, -, -]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o10035 row 13")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES_IN_ROW: row=13
                        initialRanges=[[0, 7], [6, 11], [12, 25], [23, 27], [25, 29]]
                        updatedRanges=[[0, 7], [6, 11], [12, 25], [23, 27], [26, 29]]
                        sequencesLengths=[5, 2, 13, 1, 1]
                        line=[-, -, -, O, O, -, -, -, -, -, -, -, -, O, O, O, O, O, O, O, O, O, -, O, O, -, -, -, -, -]
                        """;

        // when
        String convertedLog = SequenceRangeCorrectionFromColouredEdgesLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10035"
        );

        // then
        String expected = """
                Arguments.of("o10035 / row=13 - correct sequences ranges from coloured edges",
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 7)), new ArrayList<>(List.of(6, 11)), new ArrayList<>(List.of(12, 25)), new ArrayList<>(List.of(23, 27)), new ArrayList<>(List.of(25, 29)))),
                    List.of(List.of(0, 7), List.of(6, 11), List.of(12, 25), List.of(23, 27), List.of(26, 29)),
                    List.of(5, 2, 13, 1, 1),
                    new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "O", "O", "-", "-", "-", "-", "-"))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }

//    private static NonogramLogic createCustomLogic(NonogramRulesParameters nonogramRulesParameters,
//                                                   boolean isRow,
//                                                   int index,
//                                                   List<Integer> sequencesLengths,
//                                                   List<Integer> placedXIndexes,
//                                                   List<Integer> colouredIndexes,
//                                                   List<List<Integer>> sequencesRangesBefore
//                                                   ) {
//
//        int height = nonogramRulesParameters.getHeight();
//        int width = nonogramRulesParameters.getWidth();
//        List<List<Integer>> rowsSequencesLengths = generateEmptySequences(height);
//        List<List<Integer>> columnsSequencesLengths = generateEmptySequences(width);
//        NonogramRules nonogramRules = new NonogramRules(
//                rowsSequencesLengths,
//                columnsSequencesLengths,
//                height,
//                width
//        );
//        NonogramLogic customLogic = new NonogramLogic(nonogramRules, GuessMode.DISABLED);
//
//        if (isRow) {
//            customLogic.getNonogramRules().getRowSequencesLengths().set(index, sequencesLengths);
//            for (int columnIdx = 0; columnIdx < width; columnIdx++) {
//                if (placedXIndexes.contains(columnIdx)) {
//                    customLogic.placeXAtGivenPosition(new Field(index, columnIdx));
//                } else if (colouredIndexes.contains(columnIdx)) {
//                    customLogic.colourFieldAtGivenPosition(new Field(index, columnIdx));
//                }
//            }
//            customLogic.getRowsSequencesRanges().set(index, sequencesRangesBefore);
//        } else {
//            customLogic.getNonogramRules().getColumnSequencesLengths().set(index, sequencesLengths);
//            for (int rowIndex = 0; rowIndex < height; rowIndex++) {
//                if (placedXIndexes.contains(rowIndex)) {
//                    customLogic.placeXAtGivenPosition(new Field(rowIndex, index));
//                } else if (colouredIndexes.contains(rowIndex)) {
//                    customLogic.colourFieldAtGivenPosition(new Field(rowIndex, index));
//                }
//            }
//            customLogic.getColumnsSequencesRanges().set(index, sequencesRangesBefore);
//        }
//
//        return customLogic;
//    }
//
//    @Getter
//    @AllArgsConstructor
//    public static class NonogramRulesParameters {
//        private int height;
//        private int width;
//    }
//
//    private static List<List<Integer>> generateEmptySequences(int size) {
//        List<List<Integer>> sequences = new ArrayList<>(size);
//        for (int i = 0; i < size; i++) {
//            sequences.add(new ArrayList<>());
//        }
//        return sequences;
//    }
//
//    private static List<String> generateLine(int length, List<Integer> xIndexes, List<Integer> oIndexes) {
//        List<String> line = new ArrayList<>(Collections.nCopies(length, "-"));
//
//        if (xIndexes != null) {
//            for (Integer i : xIndexes) {
//                if (i >= 0 && i < length) {
//                    line.set(i, "X");
//                } else {
//                    throw new IllegalArgumentException("X index out of bounds: " + i);
//                }
//            }
//        }
//
//        if (oIndexes != null) {
//            for (Integer i : oIndexes) {
//                if (i >= 0 && i < length) {
//                    line.set(i, "O");
//                } else {
//                    throw new IllegalArgumentException("O index out of bounds: " + i);
//                }
//            }
//        }
//
//        return line;
//    }

}