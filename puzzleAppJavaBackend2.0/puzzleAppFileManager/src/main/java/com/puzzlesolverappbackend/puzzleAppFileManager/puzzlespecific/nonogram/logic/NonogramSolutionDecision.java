package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

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
    Field decisionField;

    @Override
    public String toString() {
        return "dec.{" +
                "'" + decisionMarker + '\'' +
                ", r" + decisionField.getRowIdx() +
                ", c" + decisionField.getColumnIdx() +
                '}';
    }
}
