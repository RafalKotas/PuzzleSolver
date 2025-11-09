package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.generateLog;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelperTest {

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper> constructor = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Generate example log - o10401 row 8")
    void shouldGenerateExampleLog() {
        // given
        PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                true,
                8,
                new ArrayList<>(List.of(
                        "-", "-", "-", "-", "-",
                        "-", "-", "-", "-", "-",
                        "-", "-", "-", "-", "O",
                        "O", "-", "-", "O", "X",
                        "X", "X", "X", "X", "X",
                        "O", "O", "O", "X", "-",
                        "X", "-", "-", "-", "X",
                        "X", "X", "O", "O", "O"
                )),
                new ArrayList<>(List.of(
                        "-", "-", "-", "-", "-",
                        "-", "-", "-", "-", "-",
                        "-", "-", "-", "-", "O",
                        "O", "-", "X", "O", "X",
                        "X", "X", "X", "X", "X",
                        "O", "O", "O", "X", "-",
                        "X", "-", "-", "-", "X",
                        "X", "X", "O", "O", "O"
                ))
        );
        String direction = "left";
        int onlyValidSequenceIdx = 3;
        List<Integer> sequencesLengths = List.of(1, 4, 3, 1, 3, 3);
        List<List<Integer>> sequencesRanges = new ArrayList<>(List.of(
                new ArrayList<>(List.of(0, 13)),
                new ArrayList<>(List.of(2, 18)),
                new ArrayList<>(List.of(13, 27)),
                new ArrayList<>(List.of(18, 29)),
                new ArrayList<>(List.of(25, 33)),
                new ArrayList<>(List.of(37, 39))
        ));

        // when
        String log = generateLog(
                placeXGenerateLogBaseContext,
                direction,
                onlyValidSequenceIdx,
                sequencesLengths,
                sequencesRanges
        );

        // then
        String expected = "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW: row=8\n" +
                "initialLine=[-, -, -, -, -, -, -, -, -, -, -, -, -, -, O, O, -, -, O, X, X, X, X, X, X, O, O, O, X, -, X, -, -, -, X, X, X, O, O, O]\n" +
                "updatedLine=[-, -, -, -, -, -, -, -, -, -, -, -, -, -, O, O, -, X, O, X, X, X, X, X, X, O, O, O, X, -, X, -, -, -, X, X, X, O, O, O]\n" +
                "direction=left\n" +
                "onlyValidSequenceIdx=3\n" +
                "sequencesLengths=[1, 4, 3, 1, 3, 3]\n" +
                "sequencesRanges=[[0, 13], [2, 18], [13, 27], [18, 29], [25, 33], [37, 39]]\n";
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper - convert example log to test arguments - o08007 column 7")
    void shouldConvertGeneratedLogToTestArguments() {
        // given
        String generatedLog =  "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW: row=18\n" +
                "initialLine=[-, -, -, -, -, -, -, -, -, -, -, -, -, -, O, O, -, -, O, X, X, X, X, X, X, O, O, O, X, -, X, -, -, -, X, X, X, O, O, O]\n" +
                "updatedLine=[-, -, -, -, -, -, -, -, -, -, -, -, -, -, O, O, -, X, O, X, X, X, X, X, X, O, O, O, X, -, X, -, -, -, X, X, X, O, O, O]\n" +
                "direction=left\n" +
                "onlyValidSequenceIdx=3\n" +
                "sequencesLengths=[1, 4, 3, 1, 3, 3]\n" +
                "sequencesRanges=[[0, 13], [2, 18], [13, 27], [18, 29], [25, 33], [37, 39]]\n";

        // when
        String convertedLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10401"
        );

        // then
        String expected = """
                Arguments.of("o10401 / row=18 - prevent extending coloured sequence to excess length place X part",
                    direction=left,
                    3,
                    List.of(1, 4, 3, 1, 3, 3),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 13)), new ArrayList<>(List.of(2, 18)), new ArrayList<>(List.of(13, 27)), new ArrayList<>(List.of(18, 29)), new ArrayList<>(List.of(25, 33)), new ArrayList<>(List.of(37, 39)))),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "O", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "X", "-", "-", "-", "X", "X", "X", "O", "O", "O")),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "X", "O", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "X", "-", "-", "-", "X", "X", "X", "O", "O", "O")))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}