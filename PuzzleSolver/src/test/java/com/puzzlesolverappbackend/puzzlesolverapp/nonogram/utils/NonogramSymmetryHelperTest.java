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
import static org.junit.jupiter.api.Assertions.*;

class NonogramSymmetryHelperTest {

    @Test
    @DisplayName("NonogramSymmetryHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<NonogramSymmetryHelper> constructor = NonogramSymmetryHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @DisplayName("getSymmetryGrade should return '1 axis' for asymmetric rows & symmetric columns")
    @Test
    void symmetryGrade_oneAxis() {
        // given
        NonogramLogic logic = buildDoggyChiotAuthorLiaaaaaaaaaa1DSymmetricalLogic();

        // when
        String grade = NonogramSymmetryHelper.getSymmetryGrade(logic);

        // then
        assertThat(grade).isEqualTo("1 axis");
    }

    @DisplayName("is1DSymmetrical should be true (row!=symmetrical, column=symmetrical)")
    @Test
    void is1DSymmetrical_true_whenExactlyOneAxisSymmetric() {
        // given
        NonogramLogic logic = buildDoggyChiotAuthorLiaaaaaaaaaa1DSymmetricalLogic();

        // when / then
        assertThat(NonogramSymmetryHelper.is1DSymmetrical(logic)).isTrue();
    }

    @DisplayName("is2DSymmetrical should be false (both axes NOT symmetric simultaneously)")
    @Test
    void is2DSymmetrical_false() {
        // given
        NonogramLogic logic = buildDoggyChiotAuthorLiaaaaaaaaaa1DSymmetricalLogic();

        // when / then
        assertThat(NonogramSymmetryHelper.is2DSymmetrical(logic)).isFalse();
    }

    @DisplayName("is3DSymmetrical should be false (rows not symmetric even if columns are)")
    @Test
    void is3DSymmetrical_false() {
        // given
        NonogramLogic logic = buildDoggyChiotAuthorLiaaaaaaaaaa1DSymmetricalLogic();

        // when / then
        assertThat(NonogramSymmetryHelper.is3DSymmetrical(logic)).isFalse();
    }

    @DisplayName("isColumnSymmetrical should be true for provided column sequences")
    @Test
    void columnSymmetry_true() {
        // given
        NonogramLogic logic = buildDoggyChiotAuthorLiaaaaaaaaaa1DSymmetricalLogic();

        // when / then
        assertThat(NonogramSymmetryHelper.isColumnSymmetrical(logic)).isTrue();
    }

    @DisplayName("isRowSymmetrical should be false for provided row sequences")
    @Test
    void rowSymmetry_false() {
        // given
        NonogramLogic logic = buildDoggyChiotAuthorLiaaaaaaaaaa1DSymmetricalLogic();

        // when / then
        assertThat(NonogramSymmetryHelper.isRowSymmetrical(logic)).isFalse();
    }

    // --- helper to build logic from provided JSON-like data ---
    private NonogramLogic buildDoggyChiotAuthorLiaaaaaaaaaa1DSymmetricalLogic() {
        List<List<Integer>> rowsSequencesLengths = List.of(
                List.of(0), List.of(1, 4, 1), List.of(3, 3, 3, 3), List.of(5, 5), List.of(4, 4),
                List.of(4, 4, 4, 4), List.of(6, 1, 1, 6), List.of(3, 1, 2, 2, 1, 3), List.of(1, 1, 3, 3, 1, 1), List.of(1, 1, 1, 1),
                List.of(2, 6, 2), List.of(1, 8, 1), List.of(1, 1, 4, 1, 1), List.of(1, 1, 2, 1, 1), List.of(1, 2, 1),
                List.of(1, 1, 1, 2, 1, 1, 1), List.of(1, 1, 1, 1), List.of(4, 4), List.of(2, 2), List.of(2)
        );

        List<List<Integer>> columnsSequencesLengths = List.of(
                List.of(3), List.of(6), List.of(6, 4), List.of(6, 1, 1), List.of(3, 5, 1, 1, 1),
                List.of(1, 2, 1, 1), List.of(1, 1, 2, 2, 1, 1), List.of(1, 1, 2, 2, 2), List.of(2, 4, 3, 1, 1), List.of(1, 6, 1),
                List.of(1, 6, 1), List.of(2, 4, 3, 1, 1), List.of(1, 1, 2, 2, 2), List.of(1, 1, 2, 2, 1, 1), List.of(1, 2, 1, 1),
                List.of(3, 5, 1, 1, 1), List.of(6, 1, 1), List.of(6, 4), List.of(6), List.of(3)
        );

        NonogramRules rules = new NonogramRules(rowsSequencesLengths, columnsSequencesLengths, 20, 20);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    @DisplayName("getSymmetryGrade should return '2 axis' for Pattern19x19(auth_wiki)")
    @Test
    void symmetryGrade_twoAxis() {
        // given
        NonogramLogic logic = buildPattern19x19AuthWiki2DSymmetricalLogic();

        // when
        String grade = NonogramSymmetryHelper.getSymmetryGrade(logic);

        // then
        assertThat(grade).isEqualTo("2 axis");
        // sanity: both axes symmetric, but rows != columns
        assertThat(NonogramSymmetryHelper.isRowSymmetrical(logic)).isTrue();
        assertThat(NonogramSymmetryHelper.isColumnSymmetrical(logic)).isTrue();
        assertThat(logic.areRowsSequencesIdenticalWithColumnsSequences()).isFalse();
    }

    @DisplayName("is2DSymmetrical should be true when both axes symmetric and rows != columns")
    @Test
    void is2DSymmetrical_true() {
        // given
        NonogramLogic logic = buildPattern19x19AuthWiki2DSymmetricalLogic();

        // when / then
        assertThat(NonogramSymmetryHelper.is2DSymmetrical(logic)).isTrue();
    }

    // helper: 2D-symmetrical logic for "Pattern19x19(auth_wiki)"
    private NonogramLogic buildPattern19x19AuthWiki2DSymmetricalLogic() {
        List<List<Integer>> rows = List.of(
                List.of(3), List.of(3, 3), List.of(1, 1, 1), List.of(2, 2, 2, 2), List.of(1, 1, 1, 1),
                List.of(2, 1, 3, 1, 2), List.of(1, 2, 1, 1, 2, 1), List.of(2, 1, 2, 2, 1, 2),
                List.of(1, 1, 1, 1, 1, 1, 1, 1), List.of(2, 1, 1, 1, 2),
                List.of(1, 1, 1, 1, 1, 1, 1, 1), List.of(2, 1, 2, 2, 1, 2),
                List.of(1, 2, 1, 1, 2, 1), List.of(2, 1, 3, 1, 2), List.of(1, 1, 1, 1),
                List.of(2, 2, 2, 2), List.of(1, 1, 1), List.of(3, 3), List.of(3)
        );

        List<List<Integer>> cols = List.of(
                List.of(2, 2), List.of(1, 1, 1), List.of(2, 1, 2), List.of(1, 2, 2, 1), List.of(2, 1, 1, 2),
                List.of(1, 1, 3, 1, 1), List.of(2, 2, 1, 1, 2, 2), List.of(1, 1, 2, 2, 1, 1),
                List.of(2, 1, 1, 1, 1, 1, 1, 2), List.of(1, 1, 1, 1, 1, 1, 1),
                List.of(2, 1, 1, 1, 1, 1, 1, 2), List.of(1, 1, 2, 2, 1, 1),
                List.of(2, 2, 1, 1, 2, 2), List.of(1, 1, 3, 1, 1), List.of(2, 1, 1, 2),
                List.of(1, 2, 2, 1), List.of(2, 1, 2), List.of(1, 1, 1), List.of(2, 2)
        );

        NonogramRules rules = new NonogramRules(rows, cols, 19, 19);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    @DisplayName("getSymmetryGrade should return '4 axis' for Doily(auth_Ricarix)")
    @Test
    void symmetryGrade_fourAxis() {
        // given
        NonogramLogic logic = buildDoilyAuthRicarix4AxisLogic();

        // when
        String grade = NonogramSymmetryHelper.getSymmetryGrade(logic);

        // then
        assertThat(grade).isEqualTo("4 axis");
        // sanity: both axes symmetric and rows == columns
        assertThat(NonogramSymmetryHelper.isRowSymmetrical(logic)).isTrue();        // row-wise mirror symmetry
        assertThat(NonogramSymmetryHelper.isColumnSymmetrical(logic)).isTrue();     // column-wise mirror symmetry
        assertThat(logic.areRowsSequencesIdenticalWithColumnsSequences()).isTrue(); // rows == columns
    }

    @DisplayName("is3DSymmetrical should be true when rows and columns are symmetric and identical")
    @Test
    void is3DSymmetrical_true() {
        // given
        NonogramLogic logic = buildDoilyAuthRicarix4AxisLogic();

        // when / then
        assertThat(NonogramSymmetryHelper.is3DSymmetrical(logic)).isTrue(); // 4-axis condition
    }

    // helper: 4-axis symmetrical logic for "Doily(auth_Ricarix)"
    private NonogramLogic buildDoilyAuthRicarix4AxisLogic() {
        List<List<Integer>> rows = List.of(
                List.of(1, 1), List.of(3, 2, 3), List.of(2, 2, 4, 2, 2), List.of(3, 2, 2, 3), List.of(1, 2, 2, 2, 1),
                List.of(1, 1, 1, 1), List.of(3, 2, 3), List.of(2, 1, 1, 2), List.of(2, 1, 1, 2, 1, 1, 2),
                List.of(2, 1, 1, 1, 1, 1, 1, 2), List.of(2, 1, 1, 1, 1, 1, 1, 2),
                List.of(2, 1, 1, 2, 1, 1, 2), List.of(2, 1, 1, 2), List.of(3, 2, 3),
                List.of(1, 1, 1, 1), List.of(1, 2, 2, 2, 1), List.of(3, 2, 2, 3),
                List.of(2, 2, 4, 2, 2), List.of(3, 2, 3), List.of(1, 1)
        );

        // columns are identical to rows in this puzzle (4-axis symmetry)
        List<List<Integer>> cols = List.of(
                List.of(1, 1), List.of(3, 2, 3), List.of(2, 2, 4, 2, 2), List.of(3, 2, 2, 3), List.of(1, 2, 2, 2, 1),
                List.of(1, 1, 1, 1), List.of(3, 2, 3), List.of(2, 1, 1, 2), List.of(2, 1, 1, 2, 1, 1, 2),
                List.of(2, 1, 1, 1, 1, 1, 1, 2), List.of(2, 1, 1, 1, 1, 1, 1, 2),
                List.of(2, 1, 1, 2, 1, 1, 2), List.of(2, 1, 1, 2), List.of(3, 2, 3),
                List.of(1, 1, 1, 1), List.of(1, 2, 2, 2, 1), List.of(3, 2, 2, 3),
                List.of(2, 2, 4, 2, 2), List.of(3, 2, 3), List.of(1, 1)
        );

        NonogramRules rules = new NonogramRules(rows, cols, 20, 20);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    @DisplayName("getSymmetryGrade should return 'None' for kot(cat)(auth_cyanida)")
    @Test
    void symmetryGrade_none() {
        // given
        NonogramLogic logic = buildCatAuthCyanidaNoneLogic();

        // when
        String grade = NonogramSymmetryHelper.getSymmetryGrade(logic);

        // then
        assertThat(grade).isEqualTo("None");
        // - neither rows nor columns are mirror-symmetric
        assertThat(NonogramSymmetryHelper.isRowSymmetrical(logic)).isFalse();
        assertThat(NonogramSymmetryHelper.isColumnSymmetrical(logic)).isFalse();
        // - rows and columns are not identical
        assertThat(logic.areRowsSequencesIdenticalWithColumnsSequences()).isFalse();
    }

    @DisplayName("is1D/2D/3D symmetrical should all be false for the asymmetrical puzzle")
    @Test
    void noSymmetry_flagsFalse() {
        // given
        NonogramLogic logic = buildCatAuthCyanidaNoneLogic();

        // then
        // 1D = XOR(row, column) -> false because both are false
        assertThat(NonogramSymmetryHelper.is1DSymmetrical(logic)).isFalse();
        // 2D requires both axes symmetric and rows!=columns
        assertThat(NonogramSymmetryHelper.is2DSymmetrical(logic)).isFalse();
        // 3D requires both axes symmetric and rows==columns
        assertThat(NonogramSymmetryHelper.is3DSymmetrical(logic)).isFalse();
    }

    // helper: assimetric nonogram "kot(cat)(auth_cyanida)" — 15x15
    private NonogramLogic buildCatAuthCyanidaNoneLogic() {
        List<List<Integer>> rows = List.of(
                List.of(2, 2), List.of(3, 3), List.of(2, 1, 2, 1), List.of(1, 4, 1), List.of(1, 2, 2, 3),
                List.of(1, 1, 2, 1, 2), List.of(1, 2, 1), List.of(2, 2, 2, 2), List.of(2, 2, 2, 1), List.of(1, 1, 1),
                List.of(2, 3, 1), List.of(2, 2, 2, 1), List.of(2, 2), List.of(3, 2), List.of(5)
        );

        List<List<Integer>> cols = List.of(
                List.of(4), List.of(2, 3), List.of(4, 1), List.of(2, 1), List.of(2, 2, 2, 1),
                List.of(2, 1, 2, 1, 1), List.of(4, 2, 1), List.of(4, 2, 1), List.of(5, 2, 1), List.of(3, 2, 1, 1),
                List.of(2, 2, 2, 2), List.of(3, 1, 1), List.of(3, 1), List.of(3, 2), List.of(4)
        );

        NonogramRules rules = new NonogramRules(rows, cols, 15, 15);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }
}