package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogFormatUtilsTest {

    @Test
    @DisplayName("LogFormatUtils constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
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

    // 1
    @Test
    @DisplayName("formatList(String) should wrap non-numeric elements in quotes and join with commas - o06005 row 7 exclude sequence")
    void formatListString_formatsCorrectly() {
        // given
        String input = "[O, O, O, O, O, O, O, O, O, O]";
        String expected = "\"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\"";

        // when
        String result = LogFormatUtils.formatList(input);

        // then
        assertEquals(expected, result);
    }

    // 2
    @Test
    @DisplayName("formatNestedList(String) should correctly format nested numeric list - o06005 row 7 exclude sequence")
    void formatNestedListString_formatsCorrectly() {
        // given
        String input = "[[0, 9]]";
        String expected = "List.of(0, 9)";

        // when
        String result = LogFormatUtils.formatNestedList(input);

        // then
        assertEquals(expected, result);
    }

    // 3
    @Test
    @DisplayName("formatList should quote non-numeric elements and join with commas - o06005 column 0")
    void formatList_quotesStringsAndJoins() {
        // given
        List<String> list = List.of("----", "----", "----", "--C-", "--C-", "--C-", "--C-", "Ra--", "----", "----");
        String expected = "\"----\", \"----\", \"----\", \"--C-\", \"--C-\", \"--C-\", \"--C-\", \"Ra--\", \"----\", \"----\"";

        // when
        String result = LogFormatUtils.formatList(list);

        // then
        assertEquals(expected, result);
    }

    // 4
    @Test
    @DisplayName("formatNestedList should format each inner list with List.of(...) syntax and join with commas - o06005 column 7")
    void formatNestedList_formatsCorrectly() {
        // given
        List<List<Integer>> nestedList = List.of(
                List.of(3, 4),
                List.of(5, 7),
                List.of(7, 9)
        );
        String expected = "List.of(3, 4), List.of(5, 7), List.of(7, 9)";

        // when
        String result = LogFormatUtils.formatNestedList(nestedList);

        // then
        assertEquals(expected, result);
    }

    // 5
    @Test
    @DisplayName("toQuotedStringList should wrap each element in quotes and join with commas - o06005 column 0")
    void toQuotedStringList_wrapsElementsInQuotes() {
        // given
        List<String> list = List.of(
                "\"----\"", "\"----\"", "\"----\"", "\"--C-\"", "\"--C-\"",
                "\"--C-\"", "\"--C-\"", "\"Ra--\"", "\"----\"", "\"----\""
        );
        String expected = "\"\"----\"\", \"\"----\"\", \"\"----\"\", \"\"--C-\"\", \"\"--C-\"\", " +
                "\"\"--C-\"\", \"\"--C-\"\", \"\"Ra--\"\", \"\"----\"\", \"\"----\"\"";

        // when
        String result = LogFormatUtils.toQuotedStringList(list);

        // then
        assertEquals(expected, result);
    }

    // 6
    @Test
    @DisplayName("toRangeStringList should format integer ranges as List.of(...) - o06005 column 0 mark fields")
    void toRangeStringList_formatsRanges() {
        // given
        List<List<Integer>> ranges = List.of(List.of(1, 7));
        String expected = "List.of(1, 7)";

        // when
        String result = LogFormatUtils.toRangeStringList(ranges);

        // then
        assertEquals(expected, result);
    }

    // 7
    @Test
    @DisplayName("parseStringListLine should parse a quoted, comma-separated list after '=' - o06005 column 0 mark fields")
    void parseStringListLine_parsesQuotedListWithEqualsPrefix() {
        // given
        String line = "initial=\"----\", \"----\", \"----\", \"--C-\", \"--C-\", \"--C-\", \"--C-\", \"Ra--\", \"----\", \"----\"";
        List<String> expected = List.of(
                "\"----\"", "\"----\"", "\"----\"", "\"--C-\"", "\"--C-\"",
                "\"--C-\"", "\"--C-\"", "\"Ra--\"", "\"----\"", "\"----\""
        );

        // when
        List<String> result = LogFormatUtils.parseStringListLine(line);

        // then
        assertEquals(expected, result);
    }

    // 8
    @Test
    @DisplayName("parseIntegerListLine should parse comma-separated integers without brackets - o06005 column 0 mark fields - o06005 column 7 correctRange when mark")
    void parseIntegerListLine_parsesCommaSeparatedWithoutBrackets() {
        // given
        String line = "2, 2, 1";
        List<Integer> expected = List.of(2, 2, 1);

        // when
        List<Integer> result = LogFormatUtils.parseIntegerListLine(line);

        // then
        assertEquals(expected, result);
    }

    // 10
    @Test
    @DisplayName("parseNestedListLineWrappedInListOf should parse formatted list into nested integer lists - o06005 column 7 correctRange when mark")
    void parseNestedListLineWrappedInListOf_parsesCorrectly() {
        // given
        String input = "List.of(3, 4), List.of(5, 7), List.of(7, 9)";
        List<List<Integer>> expected = List.of(
                List.of(3, 4),
                List.of(5, 7),
                List.of(7, 9)
        );

        // when
        List<List<Integer>> result = LogFormatUtils.parseNestedListLineWrappedInListOf(input);

        // then
        assertEquals(expected, result);
    }
}