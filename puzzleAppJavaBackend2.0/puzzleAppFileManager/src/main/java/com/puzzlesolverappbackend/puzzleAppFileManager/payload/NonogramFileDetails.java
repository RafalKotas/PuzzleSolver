package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NonogramFileDetails {

    private List<List<Integer>> rowSequences;
    private List<List<Integer>> columnSequences;

    private String filename;

    @Min(1)
    private int height;
    @Min(1)
    private int width;

    @Override
    public String toString() {
        return "NonogramFileDetails{" +
                "rowSequences=" + rowSequences +
                ", columnSequences=" + columnSequences +
                ", filename='" + filename + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", source='" + source + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }

    private String source;
    private String year;
    private String month;
    private double difficulty;
    private String additionalContent;
}
