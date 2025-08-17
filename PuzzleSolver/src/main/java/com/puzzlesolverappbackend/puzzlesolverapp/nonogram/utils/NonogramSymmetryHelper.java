package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class NonogramSymmetryHelper {

    public static String getSymmetryGrade(NonogramLogic logic) {
        Sym s = analyze(logic);
        if (s.rowsMirror && s.colsMirror) {
            if (s.rowsEqualsCols) {
                return "4 axis";
            } else {
                return "2 axis";
            }
        }
        if (s.rowsMirror ^ s.colsMirror) return "1 axis";
        return "None";
    }

    // --- helpers ---

    private static boolean isMirror(List<List<Integer>> list) {
        int n = list.size();
        for (int i = 0; i < n / 2; i++) {
            if (!list.get(i).equals(list.get(n - 1 - i))) return false;
        }
        return true;
    }

    private static Sym analyze(NonogramLogic logic) {
        boolean rowsMirror = isMirror(logic.getNonogramRules().getRowSequencesLengths());
        boolean colsMirror = isMirror(logic.getNonogramRules().getColumnSequencesLengths());
        boolean rowsEqualsCols = logic.getNonogramRules().getRowSequencesLengths()
                .equals(logic.getNonogramRules().getColumnSequencesLengths());
        return new Sym(rowsMirror, colsMirror, rowsEqualsCols);
    }

    private record Sym(boolean rowsMirror, boolean colsMirror, boolean rowsEqualsCols) {}
}
