package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.rules;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.NonogramFileDetails;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
@ToString
public class NonogramRules {

    protected List<List<Integer>> rowSequencesLengths;
    protected List<List<Integer>> columnSequencesLengths;

    protected int height;
    protected int width;

    public NonogramRules(List<List<Integer>> rowSequencesLengths, List<List<Integer>> columnSequencesLengths, int height, int width) {
        this.rowSequencesLengths = rowSequencesLengths;
        this.columnSequencesLengths = columnSequencesLengths;
        this.height = height;
        this.width = width;
    }

    public static NonogramRules buildInitialEmptyNonogramRules() {
        return NonogramRules.builder()
                .rowSequencesLengths(Collections.emptyList())
                .columnSequencesLengths(Collections.emptyList())
                .height(0)
                .width(0)
                .build();
    }

    public static NonogramRules mapNonogramFileDetailsToNonogramRules(NonogramFileDetails nonogramFileDetails) {
        return NonogramRules.builder()
                .rowSequencesLengths(nonogramFileDetails.getRowSequences())
                .columnSequencesLengths(nonogramFileDetails.getColumnSequences())
                .width(nonogramFileDetails.getWidth())
                .height(nonogramFileDetails.getHeight())
                .build();
    }
}
