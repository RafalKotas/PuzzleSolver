package com.puzzlesolverappbackend.puzzlesolverapp.hitori;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class HitoriFileDetails {
    private String source;

    private double difficulty;

    private int height;
    private int width;

    private List<List<String>> board;
}
