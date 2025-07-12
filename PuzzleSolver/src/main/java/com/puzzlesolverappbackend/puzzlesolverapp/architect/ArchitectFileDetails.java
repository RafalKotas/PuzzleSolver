package com.puzzlesolverappbackend.puzzlesolverapp.architect;

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
