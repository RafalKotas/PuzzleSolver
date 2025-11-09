package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringGenerateLogBaseContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlaceXIfOWillCauseAssignmentConflictLogHelperTest {

    @Test
    @DisplayName("PlaceXIfOWillCauseAssignmentConflictLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PlaceXIfOWillCauseAssignmentConflictLogHelper> constructor = PlaceXIfOWillCauseAssignmentConflictLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("PlaceXIfOWillCauseAssignmentConflictLogHelper - generate example log - o13757 row 15")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 15;
        List<String> initialLine = new ArrayList<>(List.of("X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "X"));
        List<String> updatedLine = new ArrayList<>(Arrays.asList("X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "-", "X", "-", "-", "-", "X"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(List.of(
                new ArrayList<>(List.of(5, 7)),
                new ArrayList<>(List.of(9, 24)),
                new ArrayList<>(List.of(21, 28))
        ));
        List<Integer> sequencesLengths = Arrays.asList(3, 7, 3);
        ColouringGenerateLogBaseContext context = new ColouringGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine,
                sequencesRanges,
                sequencesLengths
        );

        // when
        String actual = PlaceXIfOWillCauseAssignmentConflictLogHelper.generateLog(
                context
        );

        // then
        String expected =
                """
                        PLACING_X_IF_O_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_ROW: row=15
                        initialLine=[X, X, X, X, X, O, O, O, X, -, -, -, -, -, -, -, -, X, -, -, -, -, O, O, -, -, -, -, -, X]
                        updatedLine=[X, X, X, X, X, O, O, O, X, -, -, -, -, -, -, -, -, X, -, -, -, -, O, O, -, X, -, -, -, X]
                        sequencesRanges=[[5, 7], [9, 24], [21, 28]]
                        sequencesLengths=[3, 7, 3]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXIfOWillCauseAssignmentConflictLogHelper - convert example log to test arguments - o13757 row 15")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        PLACING_X_IF_O_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_ROW: row=15
                        initialLine=[X, X, X, X, X, O, O, O, X, -, -, -, -, -, -, -, -, X, -, -, -, -, O, O, -, -, -, -, -, X]
                        updatedLine=[X, X, X, X, X, O, O, O, X, -, -, -, -, -, -, -, -, X, -, -, -, -, O, O, -, X, -, -, -, X]
                        sequencesRanges=[[5, 7], [9, 24], [21, 28]]
                        sequencesLengths=[3, 7, 3]
                        """;

        // when
        String convertedLog = PlaceXIfOWillCauseAssignmentConflictLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro13757"
        );

        // then
        String expected = """
                Arguments.of("o13757 / row=15 - placing X if O will cause assignment conflict",
                    new ArrayList<>(List.of("X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "X")),
                    new ArrayList<>(List.of("X", "X", "X", "X", "X", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "O", "O", "-", "X", "-", "-", "-", "X")),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(5, 7)), new ArrayList<>(List.of(9, 24)), new ArrayList<>(List.of(21, 28)))),
                    List.of(3, 7, 3))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}