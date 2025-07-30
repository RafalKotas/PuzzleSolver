package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import lombok.experimental.UtilityClass;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldWithX;

@UtilityClass
public final class NonogramStatsUtils {

    public static double fieldsWithXPlacedPercent(NonogramLogicParams logic) {
        return getPercent(fieldsWithXPlaced(logic), fieldsToPlaceXTotal(logic));
    }

    public static int fieldsWithXPlaced(NonogramLogicParams logic) {
        int fieldsWithX = 0;
        for (int row = 0; row < logic.getNonogramRules().getHeight(); row++) {
            for (int col = 0; col < logic.getNonogramRules().getWidth(); col++) {
                Field f = new Field(row, col);
                if (isFieldWithX(logic.getNonogramSolutionBoard(), f)) {
                    fieldsWithX++;
                }
            }
        }
        return fieldsWithX;
    }

    public static int fieldsToPlaceXTotal(NonogramLogicParams logic) {
        return areaInFields(logic) - fieldsToColourTotal(logic);
    }

    public static double fieldsColouredPercent(NonogramLogicParams logic) {
        return getPercent(fieldsColoured(logic), fieldsToColourTotal(logic));
    }

    public static int fieldsColoured(NonogramLogicParams logic) {
        int colouredFields = 0;
        for (int row = 0; row < logic.getNonogramRules().getHeight(); row++) {
            for (int col = 0; col < logic.getNonogramRules().getWidth(); col++) {
                Field f = new Field(row, col);
                if (isFieldColoured(logic.getNonogramSolutionBoard(), f)) {
                    colouredFields++;
                }
            }
        }
        return colouredFields;
    }

    public static int fieldsToColourTotal(NonogramLogicParams logic) {
        int fieldsToColourOnBoard = 0;
        int fieldsToColourInRow;

        for (int rowIndex = 0; rowIndex < logic.getNonogramRules().getHeight(); rowIndex++) {
            fieldsToColourInRow = logic.getNonogramRules().getRowSequencesLengths()
                    .get(rowIndex)
                    .stream()
                    .reduce(0, Integer::sum);
            fieldsToColourOnBoard += fieldsToColourInRow;
        }

        return fieldsToColourOnBoard;
    }

    public static int fieldsFilled(NonogramLogicParams logic) {
        return fieldsColoured(logic) + fieldsWithXPlaced(logic);
    }

    public static int areaInFields(NonogramLogicParams logic) {
        return logic.getNonogramRules().getHeight() * logic.getNonogramRules().getWidth();
    }

    public static boolean isSolved(NonogramLogicParams logic) {
        return fieldsFilled(logic) == areaInFields(logic);
    }

    public static double getCompletionPercentage(NonogramLogicParams logic) {
        return getPercent(fieldsFilled(logic), areaInFields(logic));
    }

    public static double getPercent(int part, int whole) {
        return Math.round(((double) part / whole) * 10000.0) / 100.0;
    }
}

