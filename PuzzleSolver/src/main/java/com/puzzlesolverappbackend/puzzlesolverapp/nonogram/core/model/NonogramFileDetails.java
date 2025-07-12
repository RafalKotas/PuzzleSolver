package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Min;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NonogramFileDetails {

    private List<List<Integer>> rowSequences;
    private List<List<Integer>> columnSequences;

    private String filename;

    @Min(1)
    private int height;
    @Min(1)
    private int width;

    private String source;
    private String year;
    private String month;
    private double difficulty;
    private String additionalContent;
}
