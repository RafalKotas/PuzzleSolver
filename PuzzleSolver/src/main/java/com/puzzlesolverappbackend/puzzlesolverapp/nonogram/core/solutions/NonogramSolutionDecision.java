package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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

    public NonogramSolutionDecision withMarker(String newMarker) {
        return new NonogramSolutionDecision(newMarker, this.decisionField);
    }
}
