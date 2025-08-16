package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.offset;
import static org.junit.jupiter.api.Assertions.*;

class NonogramStatsUtilsTest {

    @Test
    @DisplayName("NonogramStatsUtils constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<NonogramStatsUtils> constructor = NonogramStatsUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    // --- helpers ------------------------------------------------------------

    /** Builds NonogramLogic with given rules (10x10 o06005). */
    private NonogramLogic buildLogic_o06005() {
        List<List<Integer>> rows = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1, 1, 1),
                List.of(1, 5, 1),
                List.of(9),
                List.of(9),
                List.of(7, 1),
                List.of(9),
                List.of(10),
                List.of(1, 1),
                List.of(2, 2)
        );

        List<List<Integer>> cols = List.of(
                List.of(7),
                List.of(7),
                List.of(8, 1),
                List.of(6),
                List.of(8),
                List.of(6),
                List.of(10),
                List.of(2, 2, 1),
                List.of(7),
                List.of(1)
        );

        NonogramRules rules = new NonogramRules(rows, cols, 10, 10);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    /** Sets the solution board (strings "-", "O", "X") from a 2D array. */
    private void setBoard(NonogramLogic logic, String[][] board) {
        // fill solution board row by row
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                logic.getNonogramSolutionBoard().get(r).set(c, board[r][c]);
            }
        }
    }

    /** 10x10 board from the prompt. */
    private String[][] sampleBoard() {
        return new String[][]{
                {"X","X","O","X","-","X","O","X","X","X"},
                {"O","X","O","X","O","X","O","X","O","X"},
                {"O","X","O","O","O","O","O","X","O","X"},
                {"O","O","O","O","O","O","O","O","O","X"},
                {"O","O","O","O","O","O","O","O","O","X"},
                {"O","O","O","O","O","O","O","X","O","X"},
                {"O","O","O","O","O","O","O","O","O","X"},
                {"O","O","O","O","O","O","O","O","O","O"},
                {"X","-","X","X","-","X","O","X","-","X"},
                {"X","O","O","X","X","X","O","O","X","X"}
        };
    }

    /** Fully filled board (all X) to trigger isSolved = true. */
    private String[][] fullXBoard(int h, int w) {
        String[][] b = new String[h][w];
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) b[r][c] = "X";
        }
        return b;
    }

    // --- tests --------------------------------------------------------------

    @DisplayName("fieldsToColourTotal sums row sequences (66) and area/fieldsToPlaceXTotal are consistent")
    @Test
    void totals_computation() {
        // given
        NonogramLogic logic = buildLogic_o06005();

        // when
        int toColour = NonogramStatsUtils.fieldsToColourTotal(logic); // sum of row sequences
        int area = NonogramStatsUtils.areaInFields(logic);
        int toPlaceX = NonogramStatsUtils.fieldsToPlaceXTotal(logic);

        // then
        // rows sum: 3 + 5 + 7 + 9 + 9 + 8 + 9 + 10 + 2 + 4 = 66
        assertThat(toColour).isEqualTo(66);
        assertThat(area).isEqualTo(100);
        assertThat(toPlaceX).isEqualTo(34); // 100 - 66
    }

    @DisplayName("fieldsColoured / fieldsWithXPlaced / fieldsFilled on sample board are computed exactly")
    @Test
    void counts_onSampleBoard() {
        // given
        NonogramLogic logic = buildLogic_o06005();
        setBoard(logic, sampleBoard());

        // when
        int coloured = NonogramStatsUtils.fieldsColoured(logic);
        int withX = NonogramStatsUtils.fieldsWithXPlaced(logic);
        int filled = NonogramStatsUtils.fieldsFilled(logic);

        // then
        // manual count from the prompt: O=64, X=32, filled=96
        assertThat(coloured).isEqualTo(64);
        assertThat(withX).isEqualTo(32);
        assertThat(filled).isEqualTo(96);
    }

    @DisplayName("fieldsColouredPercent (64/66=96.97%) and fieldsWithXPlacedPercent (32/34=94.12%) are rounded to 2 decimals")
    @Test
    void percentages_specific() {
        // given
        NonogramLogic logic = buildLogic_o06005();
        setBoard(logic, sampleBoard());

        // when
        double colouredPct = NonogramStatsUtils.fieldsColouredPercent(logic);
        double xPct = NonogramStatsUtils.fieldsWithXPlacedPercent(logic);

        // then
        assertThat(colouredPct).isCloseTo(96.97, offset(0.0001));
        assertThat(xPct).isCloseTo(94.12, offset(0.0001));
    }

    @DisplayName("getCompletionPercentage uses filled/area (96/100=96.00%)")
    @Test
    void completionPercentage() {
        // given
        NonogramLogic logic = buildLogic_o06005();
        setBoard(logic, sampleBoard());

        // when
        double pct = NonogramStatsUtils.getCompletionPercentage(logic);

        // then
        assertThat(pct).isCloseTo(96.00, offset(0.0001));
        assertThat(NonogramStatsUtils.isSolved(logic)).isFalse();
    }

    @DisplayName("isSolved returns true when whole board is filled (all cells X)")
    @Test
    void isSolved_trueWhenAllFilled() {
        // given
        NonogramLogic logic = buildLogic_o06005();
        setBoard(logic, fullXBoard(
                logic.getNonogramRules().getHeight(),
                logic.getNonogramRules().getWidth()
        ));

        // when / then
        assertThat(NonogramStatsUtils.fieldsFilled(logic))
                .isEqualTo(NonogramStatsUtils.areaInFields(logic));
        assertThat(NonogramStatsUtils.isSolved(logic)).isTrue();
    }

    @DisplayName("getPercent rounds half-up to two decimals (sanity: 1/3 -> 33.33)")
    @Test
    void getPercent_roundingSanity() {
        // given / when
        double pct = NonogramStatsUtils.getPercent(1, 3);

        // then
        assertThat(pct).isCloseTo(33.33, offset(0.0001));
    }
}