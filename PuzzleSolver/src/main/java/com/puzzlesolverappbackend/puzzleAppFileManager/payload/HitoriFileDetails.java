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
public class HitoriFileDetails {
    private String source;

    private double difficulty;

    private int height;
    private int width;

    private List<List<String>> board;
}
