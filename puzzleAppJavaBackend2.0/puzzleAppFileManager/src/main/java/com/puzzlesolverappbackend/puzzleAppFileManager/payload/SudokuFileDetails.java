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
public class SudokuFileDetails {

    private String source;

    private double difficulty;
    private int filled;

    private String year;
    private String month;

    private List<List<Integer>> board;

    @Override
    public String toString() {
        return "SudokuFileDetails{" +
                "source='" + source + '\'' +
                ", difficulty=" + difficulty +
                ", filled=" + filled +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", board=" + board +
                '}';
    }
}

