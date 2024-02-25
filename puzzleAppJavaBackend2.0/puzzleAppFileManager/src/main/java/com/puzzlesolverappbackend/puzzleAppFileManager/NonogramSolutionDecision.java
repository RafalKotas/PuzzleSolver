package com.puzzlesolverappbackend.puzzleAppFileManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NonogramSolutionDecision {
    String decisionMarker; // "X" or "O"
    int rowIdx;
    int columnIdx;

    @Override
    public String toString() {
        return "dec.{" +
                "'" + decisionMarker + '\'' +
                ", r" + rowIdx +
                ", c" + columnIdx +
                '}';
    }
}
