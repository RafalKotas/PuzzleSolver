package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.solve.SequenceRangeCorrectionWhenMetXHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SequenceRangeCorrectionWhenMetXHelperTest {

    @Test
    @DisplayName("SequenceRangeCorrectionWhenMetXHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<SequenceRangeCorrectionWhenMetXHelper> constructor = SequenceRangeCorrectionWhenMetXHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Should calculated update range without X - vertical case - o08007 column 6")
    void  shouldCalculateCorrectedRangeWithoutXisVertical() {
        // given
        boolean isVertical = true;
        int index = 6;
        List<Integer> currentRange = new ArrayList<>(List.of(0, 2));
        int sequenceLength = 2;
        List<List<String>> board = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"))
                )
        );

        // when
        List<Integer> updatedRange = SequenceRangeCorrectionWhenMetXHelper.calculateCorrectedRangeWithoutX(
                isVertical,
                index,
                currentRange,
                sequenceLength,
                board
        );

        // then
        assertThat(updatedRange).isEqualTo(new ArrayList<>(List.of(1, 2)));
    }

    @Test
    @DisplayName("Should calculated update range without X - vertical case - o08007 row 7")
    void  shouldCalculateCorrectedRangeWithoutXisHorizontal() {
        // given
        boolean isVertical = false;
        int index = 7;
        List<Integer> currentRange = new ArrayList<>(List.of(2, 7));
        int sequenceLength = 2;
        List<List<String>> board = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "X", "O", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "O", "-", "-", "-", "O", "X", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "O", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-")),
                        new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"))
                )
        );

        // when
        List<Integer> updatedRange = SequenceRangeCorrectionWhenMetXHelper.calculateCorrectedRangeWithoutX(
                isVertical,
                index,
                currentRange,
                sequenceLength,
                board
        );

        // then
        assertThat(updatedRange).isEqualTo(new ArrayList<>(List.of(2, 6)));
    }
}