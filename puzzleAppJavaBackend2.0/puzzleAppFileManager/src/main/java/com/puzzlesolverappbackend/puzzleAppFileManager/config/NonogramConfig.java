package com.puzzlesolverappbackend.puzzleAppFileManager.config;

import com.puzzlesolverappbackend.puzzleAppFileManager.runners.InitializerConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NonogramConfig {

    private DecisionMode decisionMode = DecisionMode.DECISION_SUM_OF_RANGES_PIXEL_BELONG_ASC;
    private RecursiveMode recursiveMode = RecursiveMode.RECURSION_MAX_FROM_BOTH_NODES;

    public NonogramConfig(DecisionMode decisionMode, RecursiveMode recursiveMode) {
        this.decisionMode = decisionMode;
        this.recursiveMode = recursiveMode;
    }

    public String getNonogramResultsOutputPath() {
        return this.decisionMode.toString() + "/" + this.recursiveMode.toString() + "/";
    }

    public String getResultsPath() {
        String nonogramsPath = InitializerConstants.NONOGRAM_RESULTS_PATH;
        return nonogramsPath + getNonogramResultsOutputPath();
    }
}
