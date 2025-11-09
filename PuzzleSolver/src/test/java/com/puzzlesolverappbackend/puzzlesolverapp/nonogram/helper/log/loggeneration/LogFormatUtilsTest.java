package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogFormatUtilsTest {

    @Test
    @DisplayName("LogFormatUtils constructor should throw UnsupportedOperationException - reflect instantiation")
    void shouldNotBeInstantiableViaReflection() throws Exception {
        // given
        Constructor<LogFormatUtils> constructor = LogFormatUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    // -------------------- toMutableRangesList --------------------

    @Test
    @DisplayName("toMutableRangesList - should throw when string does not start with \"[[\"")
    void toMutableRangesList_shouldThrow_whenNoLeadingDoubleBracket() {
        // given
        String bad = "[0, 14], [3, 16]]"; // missing leading "[["

        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> LogFormatUtils.toMutableRangesList(bad));

        // then
        assertTrue(ex.getMessage().contains("Unsupported format (expected [[...]]):"));
    }

    @Test
    @DisplayName("toMutableRangesList - should throw when string does not end with \"]]\"")
    void toMutableRangesList_shouldThrow_whenNoTrailingDoubleBracket() {
        // given
        String bad = "[[0, 14], [3, 16]"; // missing trailing "]]"

        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> LogFormatUtils.toMutableRangesList(bad));

        // then
        assertTrue(ex.getMessage().contains("Unsupported format (expected [[...]]):"));
    }

    @Test
    @DisplayName("toMutableRangesList - should parse multiple inner lists with spaces")
    void toMutableRangesList_shouldParseRanges() {
        // given
        String literal = "[[0, 14], [  3 , 16  ], [18,18]]";

        // when
        List<List<Integer>> parsed = LogFormatUtils.toMutableRangesList(literal);

        // then
        assertEquals(List.of(
                List.of(0, 14),
                List.of(3, 16),
                List.of(18, 18)
        ), parsed);
        // ensure mutability of inner lists
        parsed.get(0).add(99);
        assertEquals(List.of(0, 14, 99), parsed.get(0));
    }

    @Test
    @DisplayName("toMutableRangesList - should return empty when inner lists don't match number pattern")
    void toMutableRangesList_shouldReturnEmpty_whenNoNumericMatches() {
        // given
        String literal = "[[a, b], [x, y]]"; // outer OK, inner won't match regex

        // when
        List<List<Integer>> parsed = LogFormatUtils.toMutableRangesList(literal);

        // then
        assertTrue(parsed.isEmpty());
    }

    // -------------------- toImmutableIntListLiteral / toMutableIntListLiteral --------------------

    @Test
    @DisplayName("Flat int parser should ignore empty tokens like \"[1,, 2]\" and produce valid literals")
    void flatIntParser_shouldIgnoreEmptyTokens() {
        // given
        String literal = "[1,, 2,  ,3]";

        // when
        String immutable = LogFormatUtils.toImmutableIntListLiteral(literal);
        String mutable   = LogFormatUtils.toMutableIntListLiteral(literal);

        // then
        assertEquals("List.of(1, 2, 3)", immutable);
        assertEquals("new ArrayList<>(List.of(1, 2, 3))", mutable);
    }

    @Test
    @DisplayName("Flat int parser should handle empty list []")
    void flatIntParser_shouldHandleEmptyList() {
        // given
        String empty = "[]";

        // when
        String immutable = LogFormatUtils.toImmutableIntListLiteral(empty);
        String mutable   = LogFormatUtils.toMutableIntListLiteral(empty);

        // then
        assertEquals("List.of()", immutable);
        assertEquals("new ArrayList<>(List.of())", mutable);
    }

    // -------------------- Ranges formatters --------------------

    @Test
    @DisplayName("toImmutableRangesListLiteral - should format nested ranges into List.of(...)")
    void toImmutableRangesListLiteral_shouldFormat() {
        // given
        String ranges = "[[0, 14], [3, 16], [18, 18]]";

        // when
        String out = LogFormatUtils.toImmutableRangesListLiteral(ranges);

        // then
        assertEquals("List.of(List.of(0, 14), List.of(3, 16), List.of(18, 18))", out);
    }

    @Test
    @DisplayName("toMutableRangesListLiteral - should format nested ranges into new ArrayList<>(List.of(...))")
    void toMutableRangesListLiteral_shouldFormat() {
        // given
        String ranges = "[[0, 14], [3, 16]]";

        // when
        String out = LogFormatUtils.toMutableRangesListLiteral(ranges);

        // then
        assertEquals("new ArrayList<>(List.of(new ArrayList<>(List.of(0, 14)), new ArrayList<>(List.of(3, 16))))", out);
    }

    // -------------------- String lists formatters --------------------

    @Test
    @DisplayName("toImmutableStringListLiteral - should convert strings like \"[-, X]\" to List.of(\"-\", \"X\")")
    void toImmutableStringListLiteral_shouldFormat() {
        // given
        String row = "[-, -, X, -, O]";

        // when
        String out = LogFormatUtils.toImmutableStringListLiteral(row);

        // then
        assertEquals("List.of(\"-\", \"-\", \"X\", \"-\", \"O\")", out);
    }

    @Test
    @DisplayName("toImmutableStringListLiteral - should return List.of() for []")
    void toImmutableStringListLiteral_empty() {
        // given
        String row = "[]";

        // when
        String out = LogFormatUtils.toImmutableStringListLiteral(row);

        // then
        assertEquals("List.of()", out);
    }

    @Test
    @DisplayName("toMutableStringListLiteral - should wrap immutable form in new ArrayList<>(...)")
    void toMutableStringListLiteral_shouldFormat() {
        // given
        String row = "[]";

        // when
        String out = LogFormatUtils.toMutableStringListLiteral(row);

        // then
        assertEquals("new ArrayList<>(List.of())", out);
    }

    // -------------------- Null safety --------------------

    @Nested
    @DisplayName("Null safety")
    class NullSafety {

        @Test
        @DisplayName("toMutableRangesList - throws IllegalArgumentException on null argument")
        void toMutableRangesList_null() {
            // given / when / then
            assertThrows(IllegalArgumentException.class, () -> LogFormatUtils.toMutableRangesList(null));
        }

        @Test
        @DisplayName("toImmutableIntListLiteral - throws IllegalArgumentException on null argument")
        void toImmutableIntListLiteral_null() {
            // given / when / then
            assertThrows(IllegalArgumentException.class, () -> LogFormatUtils.toImmutableIntListLiteral(null));
        }

        @Test
        @DisplayName("toImmutableStringListLiteral - throws IllegalArgumentException on null argument")
        void toImmutableStringListLiteral_null() {
            // given / when / then
            assertThrows(IllegalArgumentException.class, () -> LogFormatUtils.toImmutableStringListLiteral(null));
        }
    }
}