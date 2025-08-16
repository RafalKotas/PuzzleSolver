package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NonogramLogicUtilsTest {

    @Test
    @DisplayName("NonogramLogicUtils constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<NonogramLogicUtils> constructor = NonogramLogicUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
    }

    // FF: inside=false, lengthOk=false -> expected false (TODO: provide real case scenario from existing nonogram)
    @DisplayName("colouredSequenceInRowIsValid → false when not inside any range AND too long (FF)")
    @Test
    void row_shouldReturnFalse_whenNotInsideAndTooLong() {
        // given
        List<Integer> coloured = List.of(2, 8);                 // length = 7
        List<Integer> lengths  = List.of(4, 1, 1);
        List<List<Integer>> ranges = List.of(
                List.of(1, 7), List.of(6, 12), List.of(14, 14)
        ); // [2, 8] not inside [1, 7] -> F, 7 > 4 -> F

        // when
        boolean valid = NonogramLogicUtils.colouredSequenceInRowIsValid(coloured, lengths, ranges);

        // then
        assertFalse(valid);
    }

    // FT: inside=true, lengthOk=false -> expected false (TODO: provide real case scenario from existing nonogram)
    @DisplayName("colouredSequenceInRowIsValid → false when inside a range BUT too long for that sequence (FT)")
    @Test
    void row_shouldReturnFalse_whenInsideButTooLong() {
        // given
        List<Integer> coloured = List.of(0, 3);                 // length = 4
        List<Integer> lengths  = List.of(4, 1, 1);
        List<List<Integer>> ranges = List.of(
                List.of(1, 7), List.of(6, 12), List.of(14, 14)
        ); // [0,3] not inside [1,7] -> F, 4 <= 4 -> T

        // when
        boolean valid = NonogramLogicUtils.colouredSequenceInRowIsValid(coloured, lengths, ranges);

        // then
        assertFalse(valid);
    }

    // TF: inside=true, lengthOk=false -> expected false (TODO: provide real case scenario from existing nonogram)
    @DisplayName("colouredSequenceInRowIsValid → false for case labeled (TF)")
    @Test
    void row_shouldReturnFalse_caseLabeledTF_butEvaluatesLikeFT() {
        // given
        List<Integer> coloured = List.of(1, 6);                 // length = 6
        List<Integer> lengths  = List.of(4, 1, 1);
        List<List<Integer>> ranges = List.of(
                List.of(1, 7), List.of(6, 12), List.of(14, 14)
        ); // [1,6] inside [1,7] -> T, but 6 <= 4 -> F

        // when
        boolean valid = NonogramLogicUtils.colouredSequenceInRowIsValid(coloured, lengths, ranges);

        // then
        assertFalse(valid);
    }

    // TT: inside=true, lengthOk=true -> expected true
    @DisplayName("colouredSequenceInRowIsValid → true when inside a range AND <= sequence length (TT)")
    @Test
    void row_shouldReturnTrue_whenInsideAndShortEnough() {
        // given
        List<Integer> coloured = List.of(4, 5);                 // length = 2
        List<Integer> lengths  = List.of(4, 1, 1);
        List<List<Integer>> ranges = List.of(
                List.of(1, 7), List.of(6, 12), List.of(14, 14)
        ); // [4,5] inside [1,7] -> T, 2 <= 4 -> T

        // when
        boolean valid = NonogramLogicUtils.colouredSequenceInRowIsValid(coloured, lengths, ranges);

        // then
        assertTrue(valid);
    }

    // FF (TODO: provide real case scenario from existing nonogram)
    @DisplayName("colouredSequenceInColumnIsValid → false when not inside any range AND too long (FF)")
    @Test
    void shouldReturnFalse_whenNotInsideAndTooLong() {
        // given
        List<Integer> coloured = List.of(0, 6);                // length = 7
        List<Integer> lengths = List.of(3, 5);
        List<List<Integer>> ranges = List.of(List.of(0, 3), List.of(5, 9)); // [0,6] not inside any

        // when
        boolean valid = NonogramLogicUtils.colouredSequenceInColumnIsValid(coloured, lengths, ranges);

        // then
        assertFalse(valid);
    }

    // FT (TODO: provide real case scenario from existing nonogram)
    @DisplayName("colouredSequenceInColumnIsValid → false when inside a range BUT too long for that sequence (FT)")
    @Test
    void shouldReturnFalse_whenInsideButTooLong() {
        // given
        List<Integer> coloured = List.of(0, 3);                // length = 4
        List<Integer> lengths = List.of(3, 5);                 // 4 <= 3 -> false for the matching range
        List<List<Integer>> ranges = List.of(List.of(0, 3), List.of(5, 9)); // inside [0,3]

        // when
        boolean valid = NonogramLogicUtils.colouredSequenceInColumnIsValid(coloured, lengths, ranges);

        // then
        assertFalse(valid);
    }

    // TF (TODO: provide real case scenario from existing nonogram)
    @DisplayName("colouredSequenceInColumnIsValid → true when inside a range and short enough (TF)")
    @Test
    void shouldReturnTrue_whenInsideAndShortEnough_case3() {
        // given
        List<Integer> coloured = List.of(7, 8);                // length = 2
        List<Integer> lengths = List.of(4, 1);                 // 2 <= 1 -> false
        List<List<Integer>> ranges = List.of(List.of(0, 3), List.of(5, 9)); // inside [5,9]

        // when
        boolean valid = NonogramLogicUtils.colouredSequenceInColumnIsValid(coloured, lengths, ranges);

        // then
        assertFalse(valid);
    }

    // TT
    @DisplayName("colouredSequenceInColumnIsValid → true when inside a range AND <= sequence length (TT)")
    @Test
    void shouldReturnTrue_whenInsideAndShortEnough_case4() {
        // given
        List<Integer> coloured = List.of(0, 2);                // length = 3
        List<Integer> lengths = List.of(3, 5);                 // 3 <= 3 -> true
        List<List<Integer>> ranges = List.of(List.of(0, 3), List.of(5, 9)); // inside [0,3]

        // when
        boolean valid = NonogramLogicUtils.colouredSequenceInColumnIsValid(coloured, lengths, ranges);

        // then
        assertTrue(valid);
    }
}