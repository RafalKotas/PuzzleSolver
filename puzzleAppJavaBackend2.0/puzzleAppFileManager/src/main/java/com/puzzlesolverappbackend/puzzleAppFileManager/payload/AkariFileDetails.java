package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AkariFileDetails {

    private List<List<String>> board;

    private String source;

    private String year;

    private String month;

    private int height;

    private int width;

    private double difficulty;
}
