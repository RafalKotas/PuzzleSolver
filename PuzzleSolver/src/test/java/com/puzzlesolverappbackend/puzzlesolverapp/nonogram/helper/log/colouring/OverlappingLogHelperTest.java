package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OverlappingLogHelperTest {

    @Test
    @DisplayName("OverlappingLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<OverlappingLogHelper> constructor = OverlappingLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("ExtendLogHelper - generate example log - o08007")
    void shouldGenerateLogWhenAtLeastOneSequenceIsExtended() {
        // given
        int index = 9;
        boolean isRow = true;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(0, 14))
                )
        );
        List<Integer> sequencesLengths = List.of(9);
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-"));

        // when
        String actual = OverlappingLogHelper.generateLog(
                index,
                isRow,
                initialLine,
                sequencesRanges,
                sequencesLengths,
                updatedLine
        );

        // then
        String expected =
                "OVERLAP_ROW_SEQUENCE: row=9\n" +
                        "initialLine=[-, -, -, -, -, -, -, -, -, -, -, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 14]]\n" +
                        "sequencesLengths=[9]\n" +
                        "updatedLine=[-, -, -, -, -, -, O, O, O, -, -, -, -, -, -]\n";
        assertEquals(expected, actual);
    }
}