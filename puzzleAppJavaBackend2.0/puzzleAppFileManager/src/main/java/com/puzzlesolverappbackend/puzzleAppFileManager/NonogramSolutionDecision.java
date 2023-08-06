package com.puzzlesolverappbackend.puzzleAppFileManager;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NonogramSolutionDecision {
    @JsonProperty("decisionMarker")
    String decisionMarker; // "XXXX" or "OOOO"
    @JsonProperty("rowIdx")
    int rowIdx;
    @JsonProperty("columnIdx")
    int columnIdx;

    @JsonProperty("decisionTime")
    double decisionTime;

    @JsonProperty("percentageSolved")
    double percentageSolved;

    @Override
    public String toString() {
        return "{" +
                "\"decisionMarker\": \"" + decisionMarker + "\"" +
                ", \"rowIdx\": " + rowIdx +
                ", \"columnIdx\": " + columnIdx +
                ", \"decisionTime\": " + decisionTime +
                ", \"percentageSolved\": " + percentageSolved +
                '}';
    }

    private String decisionTimeToSeconds() {
        double secondsWithMillis = this.decisionTime / 1000.0;
        return String.format("%.3f", secondsWithMillis);
    }

    public NonogramSolutionDecision(String decisionMarker, int rowIdx, int columnIdx) {
        this.decisionMarker = decisionMarker;
        this.rowIdx = rowIdx;
        this.columnIdx = columnIdx;
        this.percentageSolved = 0;
        this.decisionTime = 0;
    }
}
