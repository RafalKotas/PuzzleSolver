package com.puzzlesolverappbackend.puzzleAppFileManager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolutionDecision;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NonogramResult {
    private String source;
    private String filename;
    private int height;

    private int width;

    private double difficulty;

    private int area;

    private boolean solvedOnlyHeuristic;

    private boolean solvedTrialAndError;

    private boolean solvedRecursion;

    private double timeAfterHeuristics;
    private double percentageCompletionAfterHeuristics;
    private double timeAfterTrialAndError;
    private double percentageCompletionAfterTrialAndError;
    private double timeAfterRecursion;
    private double percentageCompletionAfterRecursion;

    private int recursionDepth;

    @JsonProperty("nonogramSolutionDecisionList")
    private List<NonogramSolutionDecision> nonogramSolutionDecisionList;

    private void inferSolvedOnlyHeuristic() {
        this.solvedOnlyHeuristic = percentageCompletionAfterHeuristics == 100.00;
    }

    private void inferSolvedTrialAndError() {
        this.solvedTrialAndError = percentageCompletionAfterHeuristics != 100.00
            && percentageCompletionAfterTrialAndError == 100.00;
    }

    private void inferSolvedRecursion() {
        this.solvedRecursion = percentageCompletionAfterHeuristics != 100.00
                && percentageCompletionAfterTrialAndError != 100.00
                && percentageCompletionAfterRecursion == 100;
    }

    public NonogramResult(String source,
                          String filename,
                          int height,
                          int width,
                          double difficulty,
                          double timeAfterHeuristics,
                          double percentageCompletionAfterHeuristics,
                          double timeAfterTrialAndError,
                          double percentageCompletionAfterTrialAndError,
                          double timeAfterRecursion,
                          double percentageCompletionAfterRecursion,
                          List<NonogramSolutionDecision> nonogramSolutionDecisionList,
                          int recursionDepth) {
        this.source = source;
        this.filename = filename;
        this.height = height;
        this.width = width;
        this.area = height * width;
        this.difficulty = difficulty;
        this.timeAfterHeuristics = timeAfterHeuristics;
        this.percentageCompletionAfterHeuristics = percentageCompletionAfterHeuristics;
        this.timeAfterTrialAndError = timeAfterTrialAndError;
        this.percentageCompletionAfterTrialAndError = percentageCompletionAfterTrialAndError;
        this.timeAfterRecursion = timeAfterRecursion;
        this.percentageCompletionAfterRecursion = percentageCompletionAfterRecursion;
        this.nonogramSolutionDecisionList = nonogramSolutionDecisionList;

        this.recursionDepth = recursionDepth;

        this.inferSolvedOnlyHeuristic();
        this.inferSolvedTrialAndError();
        this.inferSolvedRecursion();
    }

    @Override
    public String toString() {
        return "{\n" +
                "\t\"source\": \"" + source + "\",\n" +
                "\t\"filename\": \"" + filename + "\",\n" +
                "\t\"height\": " + height + ",\n" +
                "\t\"width\": " + width + ",\n" +
                "\t\"difficulty\": " + difficulty + ",\n" +
                "\t\"area\": " + area + ",\n" +
                "\t\"solvedOnlyHeuristic\": " + solvedOnlyHeuristic + ",\n" +
                "\t\"solvedTrialAndError\": " + solvedTrialAndError + ",\n" +
                "\t\"solvedRecursion\": " + solvedRecursion + ",\n" +
                "\t\"timeAfterHeuristics\": " + timeAfterHeuristics + ",\n" +
                "\t\"percentageCompletionAfterHeuristics\": " + percentageCompletionAfterHeuristics + ",\n" +
                "\t\"timeAfterTrialAndError\": " + timeAfterTrialAndError + ",\n" +
                "\t\"percentageCompletionAfterTrialAndError\": " + percentageCompletionAfterTrialAndError + ",\n" +
                "\t\"nonogramSolutionDecisionList\": " + printNonogramSolutionDecisionList() + ",\n" +
                "\t\"timeAfterRecursion\": " + timeAfterRecursion + ",\n" +
                "\t\"recursionDepth\": " + recursionDepth + ",\n" +
                "\t\"percentageCompletionAfterRecursion\": " + percentageCompletionAfterRecursion + "\n" +
                '}';
    }

    private String printNonogramSolutionDecisionList() {
        String decisionListToPrint = "[\n";
        int decisionNo = 0;
        for(NonogramSolutionDecision nonogramSolutionDecision : nonogramSolutionDecisionList) {
            if(decisionNo < nonogramSolutionDecisionList.size() - 1) {
                decisionListToPrint = decisionListToPrint + "\t\t" + nonogramSolutionDecision  + ",\n";
            } else {
                decisionListToPrint = decisionListToPrint + "\t\t" + nonogramSolutionDecision  + "\n";
            }
        }
        decisionListToPrint = decisionListToPrint + "\t]";
        return decisionListToPrint;
    }

    public int getDecisionsListSize() {
        return nonogramSolutionDecisionList.size();
    }
}
