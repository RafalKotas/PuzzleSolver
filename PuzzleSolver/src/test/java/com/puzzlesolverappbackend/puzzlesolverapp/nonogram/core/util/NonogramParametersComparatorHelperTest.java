package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramParametersComparatorHelper.rangesNotEqual;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramParametersComparatorHelper.sequencesRangesEqual;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NonogramParametersComparatorHelperTest {

    @Test
    @DisplayName("NonogramParametersComparatorHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<NonogramParametersComparatorHelper> constructor = NonogramParametersComparatorHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Should return false if first sequences ranges size is not equal to second sequences ranges size - o06005 row 0")
    void shouldReturnFalseSequencesRangesArraysSizeNotEquals() {
        // given
        List<List<Integer>> firstSequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(0, 5)),
                        new ArrayList<>(Arrays.asList(2, 7)),
                        new ArrayList<>(Arrays.asList(4, 9))
                )
        );
        // empty row checking -> (-1, -1) range
        List<List<Integer>> secondSequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(-1, -1))
                )
        );

        // when
        boolean areSequencesRangesEqual = sequencesRangesEqual(firstSequencesRanges, secondSequencesRanges);

        // then
        assertThat(areSequencesRangesEqual).isFalse();
    }

    @Test
    @DisplayName("rangesNotEqual - should return true when ranges first elements are not equal")
    void shouldReturnTrueIfFirstSequenceRangeFirstElementIsNotEqualToSecondSequenceRangeFirstElement() {
        // given
        List<Integer> firstRange = new ArrayList<>(List.of(1, 3));
        List<Integer> secondRange = new ArrayList<>(List.of(2, 3));

        // when
        boolean rangesNotEqual = rangesNotEqual(firstRange, secondRange);

        // then
        assertThat(rangesNotEqual).isTrue();
    }

    @Test
    @DisplayName("rangesNotEqual - should return true when ranges second elements are not equal")
    void shouldReturnTrueIfFirstSequenceRangeSecondElementIsNotEqualToSecondSequenceRangeSecondElement() {
        // given
        List<Integer> firstRange = new ArrayList<>(List.of(2, 3));
        List<Integer> secondRange = new ArrayList<>(List.of(2, 4));

        // when
        boolean rangesNotEqual = rangesNotEqual(firstRange, secondRange);

        // then
        assertThat(rangesNotEqual).isTrue();
    }

    @Test
    @DisplayName("rangesNotEqual - should return true if both 1st and 2nd elements of ranges are different")
    void shouldReturnTrueIfBothRangesElementsAreEqual() {
        // given
        List<Integer> firstRange = new ArrayList<>(List.of(1, 2));
        List<Integer> secondRange = new ArrayList<>(List.of(3, 4));

        // when
        boolean rangesNotEqual = rangesNotEqual(firstRange, secondRange);

        // then
        assertThat(rangesNotEqual).isTrue();
    }

    @Test
    @DisplayName("rangesNotEqual - should return false when ranges are equal")
    void shouldReturnTrueIfFirstSequenceRangeIsNotEqualToSecondSequenceRange() {
        // given
        List<Integer> firstRange = new ArrayList<>(List.of(12, 17));
        List<Integer> secondRange = new ArrayList<>(List.of(12, 17));

        // when
        boolean rangesNotEqual = rangesNotEqual(firstRange, secondRange);

        // then
        assertThat(rangesNotEqual).isFalse();
    }
}