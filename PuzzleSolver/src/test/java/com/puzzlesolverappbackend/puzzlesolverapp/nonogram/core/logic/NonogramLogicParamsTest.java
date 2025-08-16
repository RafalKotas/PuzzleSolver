package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NonogramLogicParamsTest {


    private NonogramLogic logic;

    @BeforeEach
    void setUp() {
        logic = buildLogic_o07836();
    }

    @Test
    @DisplayName("Should getRowCopy")
    void shouldGetRowCopy() {
        // given
        int rowIdx = 0;

        // when
        List<String> rowCopy = logic.getRowCopy(rowIdx);

        // then
        List<String> expectedRow = List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-");
        assertThat(rowCopy).isEqualTo(expectedRow);
    }

    @Test
    @DisplayName("Should getColumnCopy")
    void shouldGetColumnCopy() {
        // given
        int columnIdx = 0;

        // when
        List<String> columnCopy = logic.getColumnCopy(columnIdx);

        // then
        List<String> expectedColumn = List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-");
        assertThat(columnCopy).isEqualTo(expectedColumn);
    }

    @Test
    @DisplayName("Should getNonogramBoardRow")
    void shouldGetNonogramBoardRow() {
        // given
        int rowIdx = 0;

        // when
        List<String> nonogramBoardRow = logic.getNonogramBoardRow(rowIdx);

        // then
        List<String> expectedBoardRow = List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-");
        assertThat(nonogramBoardRow).isEqualTo(expectedBoardRow);
    }

    @Test
    @DisplayName("Should getNonogramBoardColumn")
    void shouldGetNonogramBoardColumn() {
        // given
        int columnIndex = 0;

        // when
        List<String> nonogramBoardColumn = logic.getNonogramBoardColumn(columnIndex);

        // then
        List<String> expectedBoardColumn = List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-");
        assertThat(nonogramBoardColumn).isEqualTo(expectedBoardColumn);
    }

    @Test
    @DisplayName("Should warn when adding empty tmpLog")
    void shouldWarnWhenAddingEmptyTmpLog() {
        // given
        logic.setTmpLog("");

        // when
        logic.addLog();

        // then
        assertThat(logic.getLogs()).isEmpty();
    }

    @Test
    @DisplayName("Should add tmpLog to logs when not empty")
    void shouldAddTmpLogToLogs() {
        // given
        logic.setTmpLog("COLOUR_OVERLAPPING_FIELDS_IN_ROW: row=9\n" +
                "initialLine=[-, -, -, -, -, -, -, -, -, -]\n" +
                "sequencesRanges=[[0, 6], [6, 9]]\n" +
                "sequencesLengths=[5, 2]\n" +
                "updatedLine=[-, -, O, O, O, -, -, -, -, -]\n");

        // when
        logic.addLog();

        // then
        assertThat(logic.getLogs())
                .containsExactly("COLOUR_OVERLAPPING_FIELDS_IN_ROW: row=9\n" +
                        "initialLine=[-, -, -, -, -, -, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 6], [6, 9]]\n" +
                        "sequencesLengths=[5, 2]\n" +
                        "updatedLine=[-, -, O, O, O, -, -, -, -, -]\n");
    }

    private NonogramLogic buildLogic_o07836() {
        List<List<Integer>> rowsSequences = List.of(
                List.of(3),
                List.of(2, 3),
                List.of(5),
                List.of(4, 1),
                List.of(4, 1),
                List.of(4, 1),
                List.of(3, 2),
                List.of(1, 2),
                List.of(1, 2, 2),
                List.of(5, 2)
        );

        List<List<Integer>> colsSequences = List.of(
                List.of(2),
                List.of(2, 1),
                List.of(3, 2),
                List.of(9),
                List.of(5, 1),
                List.of(3),
                List.of(3, 4),
                List.of(3, 4),
                List.of(2),
                List.of(1, 3)
        );

        NonogramRules rules = new NonogramRules(rowsSequences, colsSequences, 10, 10);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }
}