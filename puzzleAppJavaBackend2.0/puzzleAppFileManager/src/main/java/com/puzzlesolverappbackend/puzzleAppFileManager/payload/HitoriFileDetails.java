package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import lombok.Getter;

import java.util.List;

@Getter
public class HitoriFileDetails {
    private String source;

    private double difficulty;

    private int height;
    private int width;

    private List<List<String>> board;
}
