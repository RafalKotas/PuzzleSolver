package com.puzzlesolverappbackend.puzzlesolverapp.akari;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AkariFileDetails {

    private List<List<String>> board;

    private String source;

    private String year;

    private String month;

    private int height;

    private int width;

    private double difficulty;
}
