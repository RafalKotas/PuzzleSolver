package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.EMPTY_FIELD_MARKED_BOARD;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BoardUtilsTest {

    @Test
    @DisplayName("BoardUtils constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<BoardUtils> constructor = BoardUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    // getColumn

    @Test
    @DisplayName("getColumn: returns N-th column values")
    void getColumn_returnsExpectedColumn() {
        List<List<String>> board = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of("O", "O", "O")),
                        new ArrayList<>(List.of("O", "X", "X")),
                        new ArrayList<>(List.of("O", "O", "X"))
                )
        );

        List<String> col1 = getColumn(board, 1);
        assertThat(col1).containsExactly("O", "X", "O");

        List<String> col0 = getColumn(board, 0);
        assertThat(col0).containsExactly("O", "O", "O");
    }

    // allFieldsAreColouredInRowRange

    @Test
    @DisplayName("Should return true if all fields in row range are coloured - o07836")
    void allFieldsAreColouredInRowRangePositiveCase() {
        // given
        int columnIndex = 6;
        List<Integer> range = new ArrayList<>(List.of(6, 9)); // rows
        List<List<String>> board = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("X", "X", "X", "X", "-", "X", "X", "-", "-", "-")),
                new ArrayList<>(Arrays.asList("X", "X", "X", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(Arrays.asList("X", "X", "X", "O", "O", "O", "O", "O", "X", "X")),
                new ArrayList<>(Arrays.asList("X", "X", "X", "O", "O", "O", "O", "X", "X", "O")),
                new ArrayList<>(Arrays.asList("X", "-", "O", "O", "O", "-", "-", "-", "X", "O")),
                new ArrayList<>(Arrays.asList("X", "O", "O", "O", "O", "X", "X", "X", "X", "O")),
                new ArrayList<>(Arrays.asList("X", "-", "O", "O", "X", "X", "O", "O", "X", "X")), // 6
                new ArrayList<>(Arrays.asList("X", "X", "X", "O", "X", "X", "O", "O", "X", "X")), // 7
                new ArrayList<>(Arrays.asList("O", "X", "O", "O", "X", "X", "O", "-", "-", "X")), // 8
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "X", "O", "-", "-", "X"))  // 9
        ));

        // when
        boolean allFieldsColouredInRange = allFieldsAreColouredInRowRange(columnIndex, range, board);

        // then
        assertThat(allFieldsColouredInRange).isTrue();
    }

    @Test
    @DisplayName("Should return false if at least one of fields in row range is not coloured")
    void allFieldsAreColouredInRowRangeNegativeCase() {
        // given
        int columnIdx = 8;
        List<Integer> range = new ArrayList<>(List.of(2, 3)); // columns
        List<List<String>> board = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("X", "X", "O", "X", "-", "X", "O", "X", "X", "X")),
                new ArrayList<>(Arrays.asList("O", "X", "O", "X", "O", "X", "O", "X", "-", "X")),
                new ArrayList<>(Arrays.asList("O", "X", "O", "O", "O", "O", "O", "X", "-", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "X", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(Arrays.asList("-", "-", "X", "X", "-", "X", "O", "X", "-", "X")),
                new ArrayList<>(Arrays.asList("X", "O", "O", "X", "X", "X", "O", "O", "X", "X"))
        ));

        // when
        boolean allFieldsColouredInRange = allFieldsAreColouredInRowRange(columnIdx, range, board);

        // then
        assertThat(allFieldsColouredInRange).isFalse();
    }

    // allFieldsAreColouredInColumnRange

    @Test
    @DisplayName("Should return true if all fields in column range are coloured - o06005")
    void allFieldsAreColouredInColumnRangePositiveCase() {
        // given
        int rowIdx = 9;
        List<Integer> range = new ArrayList<Integer>(List.of(1, 2)); // columns
        List<List<String>> board = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("X", "X", "O", "X", "-", "X", "O", "X", "X", "X")),
                new ArrayList<>(Arrays.asList("O", "X", "O", "X", "O", "X", "O", "X", "-", "X")),
                new ArrayList<>(Arrays.asList("O", "X", "O", "O", "O", "O", "O", "X", "-", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "X", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(Arrays.asList("-", "-", "X", "X", "-", "X", "O", "X", "-", "X")),
                new ArrayList<>(Arrays.asList("X", "O", "O", "X", "X", "X", "O", "O", "X", "X"))
        ));

        // when
        boolean allFieldsColouredInRange = allFieldsAreColouredInColumnRange(rowIdx, range, board);

        // then
        assertThat(allFieldsColouredInRange).isTrue();
    }

    @Test
    @DisplayName("Should return false if at least one of fields in column range is not coloured")
    void allFieldsAreColouredInColumnRangeNegativeCase() {
        // given
        int rowIdx = 9;
        List<Integer> range = new ArrayList<>(List.of(1, 2)); // columns
        List<List<String>> board = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("X", "X", "O", "X", "-", "X", "O", "X", "X", "X")),
                new ArrayList<>(Arrays.asList("O", "X", "O", "X", "O", "X", "O", "X", "-", "X")),
                new ArrayList<>(Arrays.asList("O", "X", "O", "O", "O", "O", "O", "X", "-", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "X", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(Arrays.asList("-", "-", "X", "X", "-", "X", "O", "X", "-", "X")),
                new ArrayList<>(Arrays.asList("X", "O", "O", "X", "X", "X", "O", "O", "X", "X"))
        ));

        // when
        boolean allFieldsColouredInRange = allFieldsAreColouredInColumnRange(rowIdx, range, board);

        // then
        assertThat(allFieldsColouredInRange).isTrue();
    }

    // isFieldColoured

    @Test
    @DisplayName("isFieldColoured: true only for COLOURED_FIELD")
    void isFieldColoured_works() {
        // given
        List<List<String>> board = new ArrayList<>(
          List.of(
                  new ArrayList<>(List.of("X", "O", "-"))
          )
        );
        Field firstField = new Field(0, 0);
        Field secondField = new Field(0, 1);
        Field thirdField = new Field(0, 2);

        // when

        // then
        assertThat(isFieldColoured(board, firstField)).isFalse();   // "X"
        assertThat(isFieldColoured(board, secondField)).isTrue();  // "O"
        assertThat(isFieldColoured(board, thirdField)).isFalse();  // "-"
    }

    // isFieldEmpty

    @Test
    @DisplayName("isFieldEmpty: true only for EMPTY_FIELD")
    void isFieldEmpty_works() {
        // given
        List<List<String>> board = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of("X", "O", "-"))
                )
        );
        Field firstField = new Field(0, 0);
        Field secondField = new Field(0, 1);
        Field thirdField = new Field(0, 2);

        // when

        // then
        assertThat(isFieldEmpty(board, firstField)).isFalse();     // "X"
        assertThat(isFieldEmpty(board, secondField)).isFalse();    // "O"
        assertThat(isFieldEmpty(board, thirdField)).isTrue();    // "-"
    }

    // isFieldWithX

    @Test
    @DisplayName("isFieldWithX: true only for X_FIELD")
    void isFieldWithX_works() {
        // given
        List<List<String>> board = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of("X", "O", "-"))
                )
        );
        Field firstField = new Field(0, 0);
        Field secondField = new Field(0, 1);
        Field thirdField = new Field(0, 2);

        // when

        // then
        assertThat(isFieldWithX(board, firstField)).isTrue();     // "X"
        assertThat(isFieldWithX(board, secondField)).isFalse();    // "O"
        assertThat(isFieldWithX(board, thirdField)).isFalse();    // "-"
    }

    // findColouredFieldsInRow

    @Test
    @DisplayName("findColouredFieldsInRow: indices of coloured cells in row")
    void findColouredFieldsInRow_works() {
        // given
        List<List<String>> board = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of("-", "O", "O", "-")),
                        new ArrayList<>(List.of("O", "O", "O", "O")),
                        new ArrayList<>(List.of("-", "-", "-", "-"))
                )
        );

        // when
        List<Integer> firstRowColouredFields = findColouredFieldsInRow(board, 0);
        List<Integer> secondRowColouredFields = findColouredFieldsInRow(board, 1);
        List<Integer> thirdRowColouredFields = findColouredFieldsInRow(board, 2);

        // then
        assertThat(firstRowColouredFields).containsExactly(1, 2);
        assertThat(secondRowColouredFields).containsExactly(0, 1, 2, 3);
        assertThat(thirdRowColouredFields).isEmpty();
    }

    // findColouredFieldsInColumn

    @Test
    @DisplayName("findColouredFieldsInColumn: indices of coloured cells in column")
    void findColouredFieldsInColumn_works() {
        // given
        List<List<String>> board = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of("-", "O", "-")),
                        new ArrayList<>(List.of("-", "O", "-")),
                        new ArrayList<>(List.of("O", "-", "-")),
                        new ArrayList<>(List.of("-", "-", "-"))
                )
        );

        // when
        List<Integer> firstColumnColouredFields = findColouredFieldsInColumn(board, 0);
        List<Integer> secondColumnColouredFields = findColouredFieldsInColumn(board, 1);
        List<Integer> thirdColumnColouredFields = findColouredFieldsInColumn(board, 2);

        assertThat(firstColumnColouredFields).containsExactly(2);
        assertThat(secondColumnColouredFields).containsExactly(0, 1);
        assertThat(thirdColumnColouredFields).isEmpty();
    }

    // groupConsecutiveIndices

    @DisplayName("Should group subsequent indexes into disjoint sets")
    @ParameterizedTest
    @MethodSource("provideIndicesToGroup")
    void shouldGroupSubsequentIndexesIntoDisjointSets(List<Integer> indices, List<List<Integer>> rangesExpected) {
        // when
        List<List<Integer>> resultRanges = groupConsecutiveIndices(indices);

        // then
        assertEquals(rangesExpected, resultRanges);
    }

    private static Stream<Arguments> provideIndicesToGroup() {
        return Stream.of(
                Arguments.of(List.of(2, 3, 7, 16, 17, 18), List.of(List.of(2, 3), List.of(7, 7), List.of(16, 18))),
                Arguments.of(List.of(1), List.of(List.of(1, 1))),
                Arguments.of(List.of(), List.of()),
                Arguments.of(null, List.of())
        );
    }

    // findColouredSequenceRange

    @DisplayName("findColouredSequenceRange: should return [startIdx, endIdx] of coloured run around start field")
    @ParameterizedTest(name = "[{index}] isRow={3} start=({1},{2}) -> {5}")
    @MethodSource("findColouredSequenceRangeCases")
    void findsRange(List<List<String>> board, int startRow, int startCol, boolean isRow, List<Integer> expected) {
        // given
        NonogramRules rules = new NonogramRules(
                null, null,
                board.size(),                     // height
                board.isEmpty() ? 0 : board.get(0).size() // width
        );
        Field start = new Field(startRow, startCol);

        // when
        List<Integer> actual = findColouredSequenceRange(board, start, isRow, rules);

        // then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> findColouredSequenceRangeCases() {
        return Stream.of(
                // --- ROWS ---
                // middle of a run: "- - O O O - -", start at column 3 (0-based) => 2..4
                Arguments.of(rowBoard("- - O O O - -"), 0, 3, true, List.of(2, 4)),

                // single coloured cell
                Arguments.of(rowBoard("- - O - -"), 0, 2, true, List.of(2, 2)),

                // entire row coloured
                Arguments.of(rowBoard("O O O O"), 0, 1, true, List.of(0, 3)),

                // start cell is uncoloured -> []
                Arguments.of(rowBoard("- O O -"), 0, 0, true, emptyList()),

                // run at the left edge
                Arguments.of(rowBoard("O - - -"), 0, 0, true, List.of(0, 0)),

                // run at the right edge
                Arguments.of(rowBoard("- - - O"), 0, 3, true, List.of(3, 3)),

                // pos < 0
                Arguments.of(rowBoard("O O O"), 0, -1, true, emptyList()),

                // pos >= lineLimit (startCol == width)
                Arguments.of(rowBoard("O O O"), 0, 3, true, emptyList()),

                // --- COLUMNS ---
                // in column 1 we have 'O','O', then gap, then 'O'
                Arguments.of(grid(new String[]{
                        "- - -",
                        "- O -",
                        "- O -",
                        "- - -",
                        "- O -"
                }), 1, 1, false, List.of(1, 2)),

                // single coloured cell in column
                Arguments.of(grid(new String[]{
                        "- - -",
                        "- - -",
                        "- O -",
                        "- - -"
                }), 2, 1, false, List.of(2, 2)),

                // full column coloured
                Arguments.of(grid(new String[]{
                        "O -",
                        "O -",
                        "O -",
                        "O -"
                }), 1, 0, false, List.of(0, 3)),

                // start cell in column is uncoloured
                Arguments.of(grid(new String[]{
                        "- -",
                        "O -",
                        "O -"
                }), 0, 0, false, emptyList()),

                // pos >= lineLimit for column (startRow == height)
                Arguments.of(grid(new String[]{
                        "O",
                        "O"
                }), 2, 0, false, emptyList())
        );
    }


    /** CREATES BOARD 1×N from one row given with spaces around coloured fields, np. "-- O O --" */
    private static List<List<String>> rowBoard(String spacedRow) {
        String[] tokens = spacedRow.trim().split("\\s+");
        return new ArrayList<>(List.of(new ArrayList<>(Arrays.asList(tokens))));
    }

    /** CREATES BOARD NxM from strings array, where every string is a single row, f. e. "- O -" */
    private static List<List<String>> grid(String[] rows) {
        List<List<String>> board = new ArrayList<>();
        for (String r : rows) {
            String[] t = r.trim().split("\\s+");
            board.add(new ArrayList<>(Arrays.asList(t)));
        }
        return board;
    }

    // createCandidateRangesAroundSequences

    @Test
    @DisplayName("createCandidateRangesAroundSequences: two candidate ranges per sequence")
    void createCandidateRangesAroundSequences_works() {
        // given 1st
        List<List<Integer>> seqs = List.of(List.of(3, 5), List.of(8, 9));

        // when 1st
        List<List<List<Integer>>> candidatesRanges = createCandidateRangesAroundSequences(seqs);

        // then 1st
        List<List<List<Integer>>> expected = List.of(
                List.of(List.of(2, 5), List.of(3, 6)),
                List.of(List.of(7, 9), List.of(8, 10))
        );
        assertEquals(expected, candidatesRanges);

        // single element [4,4]
        // given 2nd
        List<List<List<Integer>>> expectedSingle = List.of(
                List.of(List.of(3, 4), List.of(4, 5))
        );

        // when 2nd
        List<List<List<Integer>>> candidatesRangesSingleElement = createCandidateRangesAroundSequences(List.of(List.of(4, 4)));

        // then 2nd
        assertEquals(expectedSingle, candidatesRangesSingleElement);

        // empty input -> empty output
        // given
        List<List<List<Integer>>> emptyInput = List.of();

        // when
        List<List<List<Integer>>> candidatesRangesEmptyInput = createCandidateRangesAroundSequences(List.of());

        // then
        assertThat(candidatesRangesEmptyInput).isEmpty();
    }

    // mergeWithPreviousIfAdjacent

    @Test
    @DisplayName("mergeWithPreviousIfAdjacent: merges only when prev.end+1 == current.start")
    void mergeWithPreviousIfAdjacent_works() {
        // given
        List<Integer> previousRange = List.of(2, 4);
        List<Integer> firstCurrentRange = List.of(5, 7);
        List<Integer> secondCurrentRange = List.of(6, 7);

        // when
        List<Integer> mergedWithPrevious = mergeWithPreviousIfAdjacent(previousRange, firstCurrentRange);
        List<Integer> notMergedWithPrevious = mergeWithPreviousIfAdjacent(previousRange, secondCurrentRange);

        // then
        List<Integer> expectedMerged1 = List.of(2, 7);
        List<Integer> expectedMerged2 = List.of(6, 7);

        assertEquals(expectedMerged1, mergedWithPrevious);
        assertEquals(expectedMerged2, notMergedWithPrevious);
    }

    // mergeWithNextIfAdjacent

    @Test
    @DisplayName("mergeWithNextIfAdjacent: merges only when current.end+1 == next.start")
    void mergeWithNextIfAdjacent_works() {
        // given
        List<Integer> currentRange = List.of(2, 4);
        List<Integer> firstNextRange = List.of(5, 7);
        List<Integer> secondNextRange = List.of(6, 7);

        // when
        List<Integer> mergedWithNext = mergeWithNextIfAdjacent(currentRange, firstNextRange);
        List<Integer> notMergedWithNext = mergeWithNextIfAdjacent(currentRange, secondNextRange);

        // then
        List<Integer> expectedMerged1 = List.of(2, 7);
        List<Integer> expectedMerged2 = List.of(2, 4);

        assertEquals(expectedMerged1, mergedWithNext);
        assertEquals(expectedMerged2, notMergedWithNext);
    }

    // calculateNewMarkedRange
    @Test
    @DisplayName("Should calculate new marked range")
    void shouldCalculateNewMarkedRange() {
        // given
        List<Integer> oldRange = new ArrayList<>(List.of(1, 9));
        List<Integer> coloured = new ArrayList<>(List.of(3, 7));
        int length = 7;

        // when
        List<Integer> newMarkedRange = BoardUtils.calculateNewMarkedRange(oldRange, coloured, length);

        // then
        assertThat(newMarkedRange).isEqualTo(new ArrayList<>(List.of(1, 9)));
    }

    // generateSequenceMarks

    @Test
    @DisplayName("generateSequenceMarks: returns a.. for count")
    void generateSequenceMarks_works() {
        // given
        int zeroCount = 0;
        int oneCount = 1;
        int fourCount = 4;

        // when
        List<String> zeroGeneratedMarks = generateSequenceMarks(zeroCount);
        List<String> oneGeneratedMarks = generateSequenceMarks(oneCount);
        List<String> fourGeneratedMarks = generateSequenceMarks(fourCount);

        // then
        assertThat(zeroGeneratedMarks).isEmpty();
        assertThat(oneGeneratedMarks).containsExactly("a");
        assertThat(fourGeneratedMarks).containsExactly("a", "b", "c", "d");
    }

    // indexToSequenceCharMark
    @DisplayName("indexToSequenceCharMark: 0->'a', 25->'z'")
    void indexToSequenceCharMark_works() {
        // given
        int zeroIndex = 0;
        int threeIndex = 3;
        int twentyFiveIndex = 25;

        // when
        String charMark0 = indexToSequenceCharMark(zeroIndex);
        String charMark3 = indexToSequenceCharMark(threeIndex);
        String charMark25 = indexToSequenceCharMark(twentyFiveIndex);

        // given
        String charMark0Expected = "a";
        String charMark3Expected = "d";
        String charMark25Expected = "z";
        assertEquals(charMark0Expected, charMark0);
        assertEquals(charMark3Expected, charMark3);
        assertEquals(charMark25Expected, charMark25);
    }

    // createEmptyMarkedLine
    @Test
    @DisplayName("createEmptyMarkedLine: filled with EMPTY_FIELD_MARKED_BOARD")
    void createEmptyMarkedLine_works() {
        // given
        int elementsInFirstLine = 0;
        int elementsInSecondLine = 3;

        // when
        List<String> firstLine = createEmptyMarkedLine(elementsInFirstLine);
        List<String> secondLine = createEmptyMarkedLine(elementsInSecondLine);

        // then
        assertThat(firstLine).isEmpty();
        assertThat(secondLine)
                .containsExactly(EMPTY_FIELD_MARKED_BOARD, EMPTY_FIELD_MARKED_BOARD, EMPTY_FIELD_MARKED_BOARD);
    }
}