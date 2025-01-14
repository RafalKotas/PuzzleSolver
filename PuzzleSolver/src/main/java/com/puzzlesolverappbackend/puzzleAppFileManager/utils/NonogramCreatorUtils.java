package com.puzzlesolverappbackend.puzzleAppFileManager.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramConstants.EMPTY_FIELD;

@UtilityClass
public class NonogramCreatorUtils {

    public static List<List<String>> generateEmptyBoard(int height, int width) {
        List<List<String>> nonogramBoard = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            List<String> nonogramBoardRow = new ArrayList<>();
            for (int columnIdx = 0; columnIdx < width; columnIdx++) {
                nonogramBoardRow.add(EMPTY_FIELD);
            }
            nonogramBoard.add(nonogramBoardRow);
        }

        return nonogramBoard;
    }

    public static List<List<String>> generateEmptyBoardWithMarks(int height, int width) {
        List<List<String>> nonogramBoardWithMarks = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            List<String> nonogramBoardWithMarksRow = new ArrayList<>();
            for (int columnIdx = 0; columnIdx < width; columnIdx++) {
                nonogramBoardWithMarksRow.add(EMPTY_FIELD.repeat(4));
            }
            nonogramBoardWithMarks.add(nonogramBoardWithMarksRow);
        }

        return nonogramBoardWithMarks;
    }

    public static List<List<Integer>> generateEmptyColumnsFieldsNotToInclude(int width) {
        List<List<Integer>> columnsFieldsNotToInclude = new ArrayList<>();

        for (int columnIdx = 0; columnIdx < width; columnIdx++) {
            List<Integer> columnFieldsNotToInclude = new ArrayList<>();
            columnsFieldsNotToInclude.add(columnFieldsNotToInclude);
        }

        return columnsFieldsNotToInclude;
    }

    public static List<List<Integer>> generateEmptyColumnsSequencesNotToInclude(int width) {
        List<List<Integer>> columnsSequencesNotToInclude = new ArrayList<>();

        for (int columnIdx = 0; columnIdx < width; columnIdx++) {
            List<Integer> columnSequencesNotToInclude = new ArrayList<>();
            columnsSequencesNotToInclude.add(columnSequencesNotToInclude);
        }

        return columnsSequencesNotToInclude;
    }

    public static List<List<List<Integer>>> generateEmptyColumnSequencesRanges(int width) {
        List<List<List<Integer>>> emptyColumnSequencesRanges = new ArrayList<>();

        for (int columnIdx = 0; columnIdx < width; columnIdx++) {
            emptyColumnSequencesRanges.add(new ArrayList<>());
        }

        return emptyColumnSequencesRanges;
    }

    public static List<List<Integer>> generateEmptyColumnSequencesLengths(int width) {
        List<List<Integer>> emptyColumnSequencesLengths = new ArrayList<>();

        for (int columnIdx = 0; columnIdx < width; columnIdx++) {
            emptyColumnSequencesLengths.add(new ArrayList<>());
        }

        return emptyColumnSequencesLengths;
    }

    public static List<List<Integer>> generateEmptyRowsFieldsNotToInclude(int height) {
        List<List<Integer>> rowsFieldsNotToInclude = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            List<Integer> rowFieldsNotToInclude = new ArrayList<>();
            rowsFieldsNotToInclude.add(rowFieldsNotToInclude);
        }

        return rowsFieldsNotToInclude;
    }

    public static List<List<Integer>> generateEmptyRowsSequencesNotToInclude(int height) {
        List<List<Integer>> rowsSequencesNotToInclude = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            List<Integer> rowSequencesNotToInclude = new ArrayList<>();
            rowsSequencesNotToInclude.add(rowSequencesNotToInclude);
        }

        return rowsSequencesNotToInclude;
    }

    public static List<List<List<Integer>>> generateEmptyRowSequencesRanges(int height) {
        List<List<List<Integer>>> emptyRowSequencesRanges = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            emptyRowSequencesRanges.add(new ArrayList<>());
        }

        return emptyRowSequencesRanges;
    }

    public static List<List<Integer>> generateEmptyRowSequencesLengths(int height) {
        List<List<Integer>> emptyRowSequencesLengths = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            emptyRowSequencesLengths.add(new ArrayList<>());
        }

        return emptyRowSequencesLengths;
    }
}
