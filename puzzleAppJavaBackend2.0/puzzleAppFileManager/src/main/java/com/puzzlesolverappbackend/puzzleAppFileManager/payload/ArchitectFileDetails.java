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
public class ArchitectFileDetails {

    private List<Integer> tanksInRows;
    private List<Integer> tanksInColumns;

    private List<List<String>> board;

    @Min(1)
    private int height;
    @Min(1)
    private int width;

    private String source;
    private String year;
    private String month;

    private double difficulty;
}
