package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilsTest {

    @Test
    @DisplayName("CollectionUtils constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<CollectionUtils> constructor = CollectionUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("reverseList - should reverse a list of integers correctly")
    void shouldReverseListOfIntegers() {
        // given
        List<Integer> input = List.of(1, 2, 3, 4, 5);

        // when
        List<Integer> result = CollectionUtils.reverseList(input);

        // then
        assertThat(result)
                .containsExactly(5, 4, 3, 2, 1)
                .isNotSameAs(input); // ensure defensive copy
    }

    @Test
    @DisplayName("reverseList - should reverse a list of strings correctly")
    void shouldReverseListOfStrings() {
        // given
        List<String> input = List.of("A", "B", "C");

        // when
        List<String> result = CollectionUtils.reverseList(input);

        // then
        assertThat(result)
                .containsExactly("C", "B", "A")
                .isNotSameAs(input);
    }

    @Test
    @DisplayName("reverseList - should return empty list when given empty list")
    void shouldReturnEmptyListWhenInputIsEmpty() {
        // given
        List<String> input = List.of();

        // when
        List<String> result = CollectionUtils.reverseList(input);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("reverseList - should throw NullPointerException when list is null")
    void shouldThrowExceptionWhenListIsNull() {
        // given
        List<String> input = null;

        // when & then
        assertThrows(NullPointerException.class, () -> CollectionUtils.reverseList(input));
    }
}