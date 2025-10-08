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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("Should return true if first sequence range is not equal to second sequence range - o06005 row 3")
    void shouldReturnTrueIfFirstSequenceRangeIsNotEqualToSecondSequenceRange() {
        // given
        List<Integer> firstRange = new ArrayList<>(List.of(0, 9));
        List<Integer> secondRange = new ArrayList<>(List.of(-1, -1));

        // when
        boolean rangesNotEqual = rangesNotEqual(firstRange, secondRange);

        // then
        assertThat(rangesNotEqual).isTrue();
    }

    @Test
    @DisplayName("Should return false if first sequence range is equal to second sequence range - o06005 column 3")
    void shouldReturnFalseIfFirstSequenceRangeIsNotEqualToSecondSequenceRange() {
        // given
        List<Integer> firstRange = new ArrayList<>(List.of(2, 7));
        List<Integer> secondRange = new ArrayList<>(List.of(2, 7));

        // when
        boolean rangesNotEqual = rangesNotEqual(firstRange, secondRange);

        // then
        assertThat(rangesNotEqual).isFalse();
    }
}