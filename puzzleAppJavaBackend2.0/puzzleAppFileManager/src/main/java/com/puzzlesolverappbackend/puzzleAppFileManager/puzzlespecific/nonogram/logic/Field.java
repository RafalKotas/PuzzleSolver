package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Field {

    private int rowIdx;
    private int columnIdx;

    @Override
    public String toString() {
        return "Field{" +
                "rowIdx=" + rowIdx +
                ", columnIdx=" + columnIdx +
                '}';
    }
}
