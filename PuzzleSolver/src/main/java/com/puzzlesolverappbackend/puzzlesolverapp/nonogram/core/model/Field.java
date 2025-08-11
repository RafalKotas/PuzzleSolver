package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Field {

    private int rowIdx;
    private int columnIdx;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;
        Field field = (Field) o;
        return rowIdx == field.rowIdx &&
                columnIdx == field.columnIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIdx, columnIdx);
    }

    @Override
    public String toString() {
        return "Field(rowIdx=" + rowIdx + ", columnIdx=" + columnIdx + ")";
    }
}
