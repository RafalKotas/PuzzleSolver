package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.NonogramResult;
import org.springframework.boot.CommandLineRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService.justifiedString;
import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.getResultsPath;

@Component
@Order(8)
public class NonogramResultsFormatter implements CommandLineRunner {

    ObjectMapper objectMapper;
    List<NonogramResult> nonogramResults;

    File resultsFolder = new File(getResultsPath());
    File[] listOfFiles = resultsFolder.listFiles();

    Double[] logiDifficulties = {1.0, 2.0, 3.0, 4.0, 5.0};

    List<NonogramResult> allInGroup;
    List<NonogramResult> solvedLogic;
    List<NonogramResult> solvedTrialAndError;
    List<NonogramResult> solvedRecurison;

    @Override
    public void run(String... args) throws Exception {
        init();
        collectFilesFromResultsPath();

        initGroups();

        collectAndPrintPercentageSolved();
        collectAndPrintMeanSolveTime();
        collectAndPrintMinAndMaxSolveTime();
        collectAndPrintMeanDecisionsLength();
        collectAndPrintMinAndMaxDecisionsLength();
        collectAndPrintMinAndMaxProgressAfterDecision();
        collectAndPrintMinPercentageSolvedAfterLogicOnly();
        collectAndPrintPercentAfterLogicAndDecisionSizeCorrelation();
    }

    public void init() {
        objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    }

    public void collectFilesFromResultsPath() {
        nonogramResults = new ArrayList<>();

        NonogramResult nonogramResult;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    nonogramResult = objectMapper.readValue(
                            file, NonogramResult.class
                    );
                    nonogramResults.add(nonogramResult);
                } catch (IOException e) {
                    System.out.println("Wrong format filename: " + file.getPath().substring(file.getPath().length() - 18));
                    //throw new RuntimeException(e);
                }
            }
        }
    }

    public void collectAndPrintPercentageSolved() {
        System.out.println("-".repeat(100));
        System.out.println("Percentage solved in all groups, all steps: ");

        double percentageLogic;
        double percentageTrialAndError;
        double percentageRecursion;

        for(double logiDifficulty : logiDifficulties) {
            initGroupsForDifficulty(logiDifficulty);

            percentageLogic = (solvedLogic.size() * 100.0) / allInGroup.size();
            percentageTrialAndError = (solvedTrialAndError.size() * 100.0) / allInGroup.size();
            percentageRecursion = (solvedRecurison.size() * 100.0) / allInGroup.size();

            System.out.printf("allInGroup(" + logiDifficulty + "): " + solvedLogic.size() + " , percentage: %.2f\n", percentageLogic);
            System.out.printf("allInGroup(" + logiDifficulty + "): " + solvedTrialAndError.size() + " , percentage: %.2f\n", percentageTrialAndError);
            System.out.printf("allInGroup(" + logiDifficulty + "): " + solvedRecurison.size() + " , percentage: %.2f\n\n", percentageRecursion);
        }
    }

    public void collectAndPrintMeanSolveTime() {
        System.out.println("-".repeat(100));
        System.out.println("Mean solve times in all groups, all steps: ");

        double meanTimeLogic;
        double meanTimeTrialAndError;
        double meanTimeRecursion;

        for(double logiDifficulty : logiDifficulties) {
            initGroupsForDifficulty(logiDifficulty);

            if(solvedLogic.size() == 0 ) {
                meanTimeLogic = 0;
            } else {
                meanTimeLogic = solvedLogic.stream().mapToDouble(NonogramResult::getTimeAfterHeuristics).sum() / solvedLogic.size();
            }

            meanTimeTrialAndError = solvedTrialAndError.stream().mapToDouble(NonogramResult::getTimeAfterTrialAndError).sum() / solvedTrialAndError.size();
            meanTimeRecursion = solvedRecurison.stream().mapToDouble(NonogramResult::getTimeAfterRecursion).sum() / solvedRecurison.size();

            System.out.printf("Nonograms solved by logic(" + logiDifficulty + "): " + solvedLogic.size() + " , meanTime: %.2f\n", meanTimeLogic);
            System.out.printf("Nongorams solved by trial and error(" + logiDifficulty + "): " + solvedTrialAndError.size() + " , meanTime: %.2f\n", meanTimeTrialAndError);
            System.out.printf("Nonogram solved by recursion(" + logiDifficulty + "): " + solvedRecurison.size() + " , meanTime: %.2f\n\n", meanTimeRecursion);
        }
    }

    public void collectAndPrintMinAndMaxSolveTime() {
        System.out.println("-".repeat(100));
        System.out.println("Minimum and maximum solve times in all groups, all steps: ");

        double minTimeLogic;
        double maxTimeLogic;
        double minTimeTrialAndError;
        double maxTimeTrialAndError;
        double minTimeRecursion;
        double maxTimeRecursion;

        for(double logiDifficulty : logiDifficulties) {
            initGroupsForDifficulty(logiDifficulty);

            minTimeLogic = getMinimumTimeAfterHeuristics();
            maxTimeLogic = getMaximumTimeAfterHeuristics();
            minTimeTrialAndError = getMinimumTimeAfterTrialAndError();
            maxTimeTrialAndError = getMaximumTimeAfterTrialAndError();
            minTimeRecursion = getMinimumTimeAfterRecursion();
            maxTimeRecursion = getMaximumTimeAfterRecursion();

            System.out.println("Minimum time to solve using only logic(" + logiDifficulty + "): " + minTimeLogic);
            System.out.println("Maximum time to solve using only logic(" + logiDifficulty + "): " + maxTimeLogic);
            System.out.println("Minimum time to solve using trial and error(" + logiDifficulty + "): " + minTimeTrialAndError);
            System.out.println("Maximum time to solve using trial and error(" + logiDifficulty + "): " + maxTimeTrialAndError);
            System.out.println("Minimum time to solve using recursive max depth 5(" + logiDifficulty + "): " + minTimeRecursion);
            System.out.println("Minimum time to solve using recursive max depth 5(" + logiDifficulty + "): " + maxTimeRecursion + "\n");
        }
    }

    public void collectAndPrintMeanDecisionsLength() {
        System.out.println("-".repeat(100));
        System.out.println("Mean decisions lengths in all groups, all steps: ");

        double meanDecisionsTrialAndError;

        for(double logiDifficulty : logiDifficulties) {
            initGroupsForDifficulty(logiDifficulty);

            meanDecisionsTrialAndError = solvedTrialAndError.stream().mapToInt(NonogramResult::getDecisionsListSize).sum() / solvedTrialAndError.size();

            System.out.println("Mean decisions trial and error(" + logiDifficulty + "): " + meanDecisionsTrialAndError);
        }
        System.out.println();
    }

    public void collectAndPrintMinAndMaxDecisionsLength() {
        System.out.println("-".repeat(100));
        System.out.println("Mean decisions lengths in all groups, all steps: ");

        int minDecisionsTrialAndError;
        int maxDecisionsTrialAndError;

        for(double logiDifficulty : logiDifficulties) {
            initGroupsForDifficulty(logiDifficulty);

            minDecisionsTrialAndError = solvedTrialAndError.stream().mapToInt(NonogramResult::getDecisionsListSize).min().getAsInt();
            maxDecisionsTrialAndError = solvedTrialAndError.stream().mapToInt(NonogramResult::getDecisionsListSize).max().getAsInt();

            System.out.println("Min decisions trial and error(" + logiDifficulty + "): " + minDecisionsTrialAndError + "\n");
            System.out.println("Max decisions trial and error(" + logiDifficulty + "): " + maxDecisionsTrialAndError + "\n");
        }
        System.out.println();
    }

    public double getMinimumTimeAfterHeuristics() {
        double minimumTime = 0.0;
        if(solvedLogic.size() > 0) {
            minimumTime = solvedLogic.get(0).getTimeAfterHeuristics();
        }
        for(NonogramResult nonogramResult : solvedLogic) {
            if(nonogramResult.getTimeAfterHeuristics() < minimumTime) {
                minimumTime = nonogramResult.getTimeAfterHeuristics();
            }
        }
        return minimumTime;
    }

    public double getMaximumTimeAfterHeuristics() {
        double maximumTime = 0.0;
        if(solvedLogic.size() > 0) {
            maximumTime = solvedLogic.get(0).getTimeAfterHeuristics();
        }
        for(NonogramResult nonogramResult : solvedLogic) {
            if(nonogramResult.getTimeAfterHeuristics() > maximumTime) {
                maximumTime = nonogramResult.getTimeAfterHeuristics();
            }
        }
        return maximumTime;
    }

    public double getMinimumTimeAfterTrialAndError() {
        double minimumTime = 0.0;
        if(solvedTrialAndError.size() > 0) {
            minimumTime = solvedTrialAndError.get(0).getTimeAfterTrialAndError();
        }
        for(NonogramResult nonogramResult : solvedTrialAndError) {
            if(nonogramResult.getTimeAfterTrialAndError() < minimumTime) {
                minimumTime = nonogramResult.getTimeAfterTrialAndError();
            }
        }
        return minimumTime;
    }

    public double getMaximumTimeAfterTrialAndError() {
        double maximumTime = 0.0;
        if(solvedTrialAndError.size() > 0) {
            maximumTime = solvedTrialAndError.get(0).getTimeAfterTrialAndError();
        }
        for(NonogramResult nonogramResult : solvedTrialAndError) {
            if(nonogramResult.getTimeAfterTrialAndError() > maximumTime) {
                maximumTime = nonogramResult.getTimeAfterTrialAndError();
            }
        }
        return maximumTime;
    }

    public double getMinimumTimeAfterRecursion() {
        double minimumTime = 0.0;
        if(solvedRecurison.size() > 0) {
            minimumTime = solvedRecurison.get(0).getTimeAfterRecursion();
        }
        for(NonogramResult nonogramResult : solvedRecurison) {
            if(nonogramResult.getTimeAfterRecursion() < minimumTime) {
                minimumTime = nonogramResult.getTimeAfterRecursion();
            }
        }
        return minimumTime;
    }

    public double getMaximumTimeAfterRecursion() {
        double maximumTime = 0.0;
        if(solvedRecurison.size() > 0) {
            maximumTime = solvedRecurison.get(0).getTimeAfterRecursion();
        }
        for(NonogramResult nonogramResult : solvedRecurison) {
            if(nonogramResult.getTimeAfterRecursion() > maximumTime) {
                maximumTime = nonogramResult.getTimeAfterRecursion();
            }
        }
        return maximumTime;
    }

    public void collectAndPrintMinAndMaxProgressAfterDecision() {
        double minStep = 100.00;
        double maxStep =   0.00;

        for(double logiDifficulty : logiDifficulties) {
            initGroupsForDifficulty(logiDifficulty);

            for(NonogramResult nonogramResult : solvedTrialAndError) {
                if (calculateMaxProgressAfterDecision(nonogramResult) > maxStep) {
                    maxStep = calculateMaxProgressAfterDecision(nonogramResult);
                }
                if (calculateMinProgressAfterDecision(nonogramResult) < minStep) {
                    minStep = calculateMinProgressAfterDecision(nonogramResult);
                }
            }
            System.out.println("Min step trial and error(" + logiDifficulty + "): " + minStep + "\n");
            System.out.println("Max step trial and error(" + logiDifficulty + "): " + maxStep + "\n");
        }
        System.out.println();
    }

    public double calculateMinProgressAfterDecision(NonogramResult nonogramResult) {
        double minDecisionProgress = 100.0;
        double progress;
        int firstDecisionIndex = 0;
        int secondDecisionIndex = 1;
        NonogramSolutionDecision nonogramSolutionDecision_a;
        NonogramSolutionDecision nonogramSolutionDecision_b;
        List<NonogramSolutionDecision> resultDecisions = nonogramResult.getNonogramSolutionDecisionList();


        if(nonogramResult.getDecisionsListSize() >= 2) {
            while(secondDecisionIndex < nonogramResult.getDecisionsListSize()) {

                nonogramSolutionDecision_a = resultDecisions.get(firstDecisionIndex);
                nonogramSolutionDecision_b = resultDecisions.get(secondDecisionIndex);

                progress = nonogramSolutionDecision_b.getPercentageSolved() - nonogramSolutionDecision_a.getPercentageSolved();

                if(progress < minDecisionProgress) {
                    minDecisionProgress = progress;
                }
                firstDecisionIndex += 1;
                secondDecisionIndex += 1;
            }
        }

        return minDecisionProgress;
    }

    public double calculateMaxProgressAfterDecision(NonogramResult nonogramResult) {
        double maxDecisionProgress = 0.0;
        double progress;
        int firstDecisionIndex = 0;
        int secondDecisionIndex = 1;
        NonogramSolutionDecision nonogramSolutionDecision_a;
        NonogramSolutionDecision nonogramSolutionDecision_b;
        List<NonogramSolutionDecision> resultDecisions = nonogramResult.getNonogramSolutionDecisionList();


        if(nonogramResult.getDecisionsListSize() >= 2) {
            while(secondDecisionIndex < nonogramResult.getDecisionsListSize()) {

                nonogramSolutionDecision_a = resultDecisions.get(firstDecisionIndex);
                nonogramSolutionDecision_b = resultDecisions.get(secondDecisionIndex);

                progress = nonogramSolutionDecision_b.getPercentageSolved() - nonogramSolutionDecision_a.getPercentageSolved();

                if(progress > maxDecisionProgress) {
                    maxDecisionProgress = progress;
                }
                firstDecisionIndex += 1;
                secondDecisionIndex += 1;
            }
        }

        return maxDecisionProgress;
    }

    public void collectAndPrintMinPercentageSolvedAfterLogicOnly() {
        double minSolutionPercentage;
        double maxSolutionPercentage;

        for(double logiDifficulty : logiDifficulties) {
            initGroupsForDifficulty(logiDifficulty);

            minSolutionPercentage = 100.0;
            maxSolutionPercentage =   0.0;

            for(NonogramResult nonogramResult : solvedTrialAndError) {
                if(nonogramResult.getPercentageCompletionAfterHeuristics() < minSolutionPercentage) {
                    minSolutionPercentage = nonogramResult.getPercentageCompletionAfterHeuristics();
                }
                if(nonogramResult.getPercentageCompletionAfterHeuristics() > maxSolutionPercentage) {
                    maxSolutionPercentage = nonogramResult.getPercentageCompletionAfterHeuristics();
                }
            }
            System.out.println("Min solution percentage after logic(" + logiDifficulty + "): " + minSolutionPercentage + "\n");
            System.out.println("Max solution percentage after logic(" + logiDifficulty + "): " + maxSolutionPercentage + "\n");
        }
        System.out.println();
    }

    public void collectAndPrintPercentAfterLogicAndDecisionSizeCorrelation() {
        System.out.println("-".repeat(100));

        for(double logiDifficulty : logiDifficulties) {
            initGroupsForDifficulty(logiDifficulty);
            printPercentAfterLogicAndDecisionSizeCorrelationInDifficultyGroup();
        }
    }

    public void printPercentAfterLogicAndDecisionSizeCorrelationInDifficultyGroup() {
        System.out.println("Percent after logic - decisions size: ");

        for(NonogramResult nonogramResult : solvedTrialAndError) {
            System.out.println(
                    justifiedString(7, String.valueOf(nonogramResult.getDifficulty()))
                            + ", percentage after logic: " +
                    justifiedString(7, String.valueOf(nonogramResult.getPercentageCompletionAfterHeuristics()))
                            + ", dec size:" +
                    justifiedString(7, String.valueOf(nonogramResult.getDecisionsListSize()))
            );
        }
    }

    public void initGroups() {
        allInGroup = new ArrayList<>();
        solvedLogic = new ArrayList<>();
        solvedTrialAndError = new ArrayList<>();
        solvedRecurison = new ArrayList<>();
    }
    public void initGroupsForDifficulty(double difficulty) {
        allInGroup = nonogramResults
                .stream()
                .filter(nonogramResult -> nonogramResult.getDifficulty() == difficulty)
                .collect(Collectors.toList());
        solvedLogic = allInGroup
                .stream()
                .filter(nonogramResult -> nonogramResult.getPercentageCompletionAfterHeuristics() == 100.0)
                .collect(Collectors.toList());
        solvedTrialAndError = allInGroup
                .stream()
                .filter(nonogramResult -> nonogramResult.getPercentageCompletionAfterTrialAndError() == 100.0)
                .collect(Collectors.toList());
        solvedRecurison = allInGroup
                .stream()
                .filter(nonogramResult -> nonogramResult.getPercentageCompletionAfterRecursion() == 100.0 ||
                        nonogramResult.getPercentageCompletionAfterTrialAndError() == 100.0)
                .collect(Collectors.toList());
    }
}
