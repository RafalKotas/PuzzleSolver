package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class NonogramSymmetryHelper {

    public static boolean isRowSymmetrical(NonogramLogic logic) {
        return areOriginalAndReversedListIdentical(logic.getNonogramRules().getRowSequencesLengths());
    }

    public static boolean isColumnSymmetrical(NonogramLogic logic) {
        return areOriginalAndReversedListIdentical(logic.getNonogramRules().getColumnSequencesLengths());
    }

    public static boolean is1DSymmetrical(NonogramLogic logic) {
        return isRowSymmetrical(logic) ^ isColumnSymmetrical(logic);
    }

    public static boolean is2DSymmetrical(NonogramLogic logic) {
        return isRowSymmetrical(logic) && isColumnSymmetrical(logic) &&
                !logic.areRowsSequencesIdenticalWithColumnsSequences();
    }

    public static boolean is3DSymmetrical(NonogramLogic logic) {
        return isRowSymmetrical(logic) && isColumnSymmetrical(logic) &&
                logic.areRowsSequencesIdenticalWithColumnsSequences();
    }

    public static String getSymmetryGrade(NonogramLogic logic) {
        if (is3DSymmetrical(logic)) {
            return "4 axis";
        } else if (is2DSymmetrical(logic)) {
            return "2 axis";
        } else if (is1DSymmetrical(logic)) {
            return "1 axis";
        } else {
            return "None";
        }
    }

    private static boolean areOriginalAndReversedListIdentical(List<List<Integer>> original) {
        int size = original.size();
        for (int i = 0; i < size / 2; i++) {
            if (!original.get(i).equals(original.get(size - i - 1))) {
                return false;
            }
        }
        return true;
    }
}