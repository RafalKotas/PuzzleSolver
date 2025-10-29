package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.Getter;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@Getter
public class ColouringLogContext {

    private final String fileName;
    private final String axisLabel;
    private final int index;
    private final String initialLine;
    private final String sequencesRanges;
    private final String sequencesLengths;
    private final String updatedLine;

    public ColouringLogContext(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        axisLabel = isRow ? ROW : COLUMN;

        index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        initialLine = toMutableStringListLiteral(extractValue(lines, "initialLine"));
        sequencesRanges = toMutableRangesListLiteral(extractValue(lines, "sequencesRanges"));
        sequencesLengths = toImmutableIntListLiteral(extractValue(lines, "sequencesLengths"));
        updatedLine = toMutableStringListLiteral(extractValue(lines, "updatedLine"));
    }
}
