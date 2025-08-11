package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NonogramRules {

    protected List<List<Integer>> rowSequencesLengths;
    protected List<List<Integer>> columnSequencesLengths;

    protected int height;
    protected int width;

    public static NonogramRules mapNonogramFileDetailsToNonogramRules(NonogramFileDetails nonogramFileDetails) {
        return NonogramRules.builder()
                .rowSequencesLengths(nonogramFileDetails.getRowSequences())
                .columnSequencesLengths(nonogramFileDetails.getColumnSequences())
                .width(nonogramFileDetails.getWidth())
                .height(nonogramFileDetails.getHeight())
                .build();
    }
}
