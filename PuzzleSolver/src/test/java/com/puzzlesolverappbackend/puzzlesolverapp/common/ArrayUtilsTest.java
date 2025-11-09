package com.puzzlesolverappbackend.puzzlesolverapp.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.copyTwoDeepList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilsTest {

    @Test
    @DisplayName("ArrayUtils constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<ArrayUtils> constructor = ArrayUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        // then
        Throwable cause = thrown.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("copyTwoDeepList - should return empty list if argument is null")
    void shouldReturnEmptyListIfTwoDeepListIsNull() {
        // given
        List<List<String>> twoDeepList = null;

        // when
        List<List<String>> result = copyTwoDeepList(twoDeepList);

        // then
        assertThat(result).isNotNull().hasSize(0);
    }

    @Test
    @DisplayName("copyTwoDeepList - should return two dimensional list containing null row")
    void shouldReturnBoardWithNullRow() {
        // given
        List<List<String>> twoDeepList = new ArrayList<>();
        twoDeepList.add(null);

        // when
        List<List<String>> result = copyTwoDeepList(twoDeepList);

        // then
        assertThat(result)
                .isNotNull()
                .hasSize(1);
        assertThat(result.get(0)).isNull();
    }

    @Test
    @DisplayName("copyTwoDeepList - should return two dimensional array")
    void shouldReturnBoardWithFilledRow() {
        // given
        List<String> list = new ArrayList<>(List.of("X", "O", "X", "O"));
        List<List<String>> twoDeepList = new ArrayList<>();
        twoDeepList.add(list);

        // when
        List<List<String>> result = copyTwoDeepList(twoDeepList);

        // then
        assertThat(result).isNotNull()
                .hasSize(1)
                .isEqualTo(
                        new ArrayList<>(
                                List.of(new ArrayList<>(List.of("X", "O", "X", "O")))
                        )
                );
    }

    @Test
    @DisplayName("rangeInsideAnotherRange - should return true when range inside another range")
    void rangeInsideAnotherRange_returnsTrueWhenInside() {
        // given
        List<Integer> inner = List.of(2, 3);
        List<Integer> outer = List.of(1, 4);

        // when
        boolean result = ArrayUtils.rangeInsideAnotherRange(inner, outer);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("rangeInsideAnotherRange - should return true when not whole range inside another range")
    void rangeInsideAnotherRange_returnsFalseWhenOutside() {
        // given
        List<Integer> inner = List.of(0, 5);
        List<Integer> outer = List.of(1, 4);

        // when
        boolean result = ArrayUtils.rangeInsideAnotherRange(inner, outer);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("rangeInsideAnotherRange - should return false when inner range list is empty")
    void rangeInsideAnotherRange_returnsFalseWhenInnerEmpty() {
        // given
        List<Integer> inner = Collections.emptyList();
        List<Integer> outer = List.of(0, 5);

        // when
        boolean result = ArrayUtils.rangeInsideAnotherRange(inner, outer);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("rangeInsideAnotherRange - should return false when outer range hasn't 2 elements")
    void rangeInsideAnotherRange_returnsFalseWhenOuterHasLessThanTwoElements() {
        // given
        List<Integer> inner = List.of(1, 2);
        List<Integer> outer = List.of(0);

        // when
        boolean result = ArrayUtils.rangeInsideAnotherRange(inner, outer);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("rangeLength - should return range int elements count")
    void rangeLength_returnsCorrectLength() {
        // given
        List<Integer> range = List.of(2, 5);

        // when
        int length = ArrayUtils.rangeLength(range);

        // then
        assertEquals(4, length);
    }

    @Test
    @DisplayName("deepCopy - should not modify original array if copy is changed")
    void deepCopy_returnsIndependentCopy() {
        // given
        List<List<Integer>> original = new ArrayList<>();
        original.add(new ArrayList<>(List.of(1, 2)));

        // when
        List<List<Integer>> copy = ArrayUtils.deepCopy(original);
        copy.get(0).set(0, 99);

        // then
        assertNotEquals(original.get(0).get(0), copy.get(0).get(0));
    }

    @Test
    @DisplayName("rangesListNotEqual - should return true if ranges list have different size")
    void rangesListNotEqual_returnsTrueWhenSizesDiffer() {
        // given
        List<List<Integer>> a = List.of(List.of(1, 2));
        List<List<Integer>> b = List.of(List.of(1, 2), List.of(3));

        // when
        boolean result = ArrayUtils.rangesListNotEqual(a, b);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("rangesListNotEqual - should return true if ranges list have same size but not elements")
    void rangesListNotEqual_returnsTrueWhenContentsDiffer() {
        // given
        List<List<Integer>> a = List.of(List.of(1, 2), List.of(3, 4));
        List<List<Integer>> b = List.of(List.of(1, 2), List.of(3, 5));

        // when
        boolean result = ArrayUtils.rangesListNotEqual(a, b);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("rangesListNotEqual - should return false if ranges list are identical")
    void rangesListNotEqual_returnsFalseWhenEqual() {
        // given
        List<List<Integer>> a = List.of(List.of(1, 2), List.of(3, 4));
        List<List<Integer>> b = List.of(List.of(1, 2), List.of(3, 4));

        // when
        boolean result = ArrayUtils.rangesListNotEqual(a, b);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("sumListElements - should return sum of list elements")
    void sumListElements_returnsCorrectSum() {
        // given
        List<Integer> list = List.of(1, 2, 3, 4);

        // when
        int sum = ArrayUtils.sumListElements(list);

        // then
        assertEquals(10, sum);
    }

    @Test
    @DisplayName("cloneAndMakeImmutable2DList - should clone 2d list and make it immutable")
    void cloneAndMakeImmutable2DList_returnsImmutableCopy() {
        // given
        List<List<Integer>> original = List.of(List.of(1, 2), List.of(3));
        List<Integer> listToAdd = List.of(9);

        // when
        List<List<Integer>> cloned = ArrayUtils.cloneAndMakeImmutable2DList(original);

        // then
        assertEquals(original, cloned);
        assertThrows(UnsupportedOperationException.class, () -> cloned.add(listToAdd));

        List<Integer> innerList = cloned.get(0);
        assertThrows(UnsupportedOperationException.class, () -> innerList.add(10));
    }

    @Test
    @DisplayName("mutableClone2DList - should clone 2d list and make it mutable")
    void mutableClone2DList_returnsIndependentMutableCopy() {
        // given
        List<List<Integer>> original = new ArrayList<>();
        original.add(new ArrayList<>(List.of(1, 2)));

        // when
        List<List<Integer>> cloned = ArrayUtils.mutableClone2DList(original);
        cloned.get(0).set(0, 99);

        // then
        assertNotEquals(original.get(0).get(0), cloned.get(0).get(0));
    }
}
