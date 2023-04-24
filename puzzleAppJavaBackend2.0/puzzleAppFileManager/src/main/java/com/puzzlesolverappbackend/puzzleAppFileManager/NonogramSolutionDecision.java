package com.puzzlesolverappbackend.puzzleAppFileManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NonogramSolutionDecision {
    String decisionMarker; // "XXXX" or "OOOO"
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
