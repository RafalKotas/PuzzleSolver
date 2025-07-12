package com.puzzlesolverappbackend.puzzlesolverapp.slitherlink;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SlitherlinkFileDetails {

    private String source;
    private String year;
    private String month;

    private double difficulty;

    private List<List<Integer>> board;

    private int height;
    private int width;
}
