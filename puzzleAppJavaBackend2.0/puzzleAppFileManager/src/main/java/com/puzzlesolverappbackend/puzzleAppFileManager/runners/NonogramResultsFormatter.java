package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.config.NonogramConfig;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.NonogramResult;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.NonogramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService.justifiedString;

//@Component
//@Order(8)
public class NonogramResultsFormatter implements CommandLineRunner {

    String source = "katana";
    @Autowired
    NonogramRepository nonogramRepository;
    NonogramConfig nonogramConfig = new NonogramConfig();
    ObjectMapper objectMapper;
    List<NonogramResult> nonogramResults;

    File resultsFolder = new File(nonogramConfig.getResultsPath());
    File[] listOfFiles = resultsFolder.listFiles();

    List<Double> difficulties;
    List<String> dimensions;

    boolean squaredimensions = true;

    List<String> sources = new ArrayList<>(List.of("katana"));

    List<NonogramResult> allInGroup;
    List<NonogramResult> solvedLogic;
    List<NonogramResult> solvedTrialAndError;
    List<NonogramResult> solvedRecurison;

    int DECISION_SIZE_LOWER_LIMIT = 10;
    int DECISION_SIZE_UPPER_LIMIT = 50;

    double TIME_LOWER_LIMIT = 60.0;
    double TIME_UPPER_LIMIT = 180.0;

    @Override
    public void run(String... args) throws Exception {
        init();
        collectFilesFromResultsPath();
        initGroups();

        if(source.equals("logi")) {
            collectAndPrintPercentageSolvedDifficulties();
            collectAndPrintMeanSolveTimeDifficulties();
            collectAndPrintMinAndMaxSolveTimeDifficulties();
            collectAndPrintMeanDecisionsLengthDifficulties();
            collectAndPrintMinAndMaxDecisionsLengthDifficulties();
            collectAndPrintMinAndMaxProgressAfterDecisionDifficulties();
            collectAndPrintMinPercentageSolvedAfterLogicOnlyDifficulties();
            collectAndPrintPercentAfterLogicAndDecisionSizeCorrelationDifficulties();

            printSelectedNonogramsToFurtherAnalysisDifficulties();
        } else {
            collectAndPrintPercentageSolvedDimensions();
            collectAndPrintMeanSolveTimeDimensions();
            collectAndPrintMinAndMaxSolveTimeDimensions();
            collectAndPrintMeanDecisionsLengthDimensions();
            collectAndPrintMinAndMaxDecisionsLengthDimensions();
            collectAndPrintMinAndMaxProgressAfterDecisionDimensions();
            collectAndPrintMinPercentageSolvedAfterLogicOnlyDimensions();
            collectAndPrintPercentAfterLogicAndDecisionSizeCorrelationDimension();
        }
    }

    public void init() {
        objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        difficulties = nonogramRepository.selectNonogramDifficultiesBySource(source);
        if(squaredimensions) {
            dimensions = nonogramRepository.selectSquareNonogramDimensions(sources, 15);
        } else {
            dimensions = nonogramRepository.selectNonogramDimensions(sources);
        }
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

                    if(nonogramResult.getSource().equals(source)) {
                        nonogramResults.add(nonogramResult);
                    }

                } catch (IOException e) {
                    System.out.println("Wrong format filename: " + file.getPath());
                    //throw new RuntimeException(e);
                }
            }
        }
    }

    public void collectAndPrintPercentageSolvedDifficulties() {
        System.out.println("-".repeat(100));
        System.out.println("Percentage solved in all groups, all steps: ");

        double percentageLogic;
        double percentageTrialAndError;
        double percentageRecursion;

        for(double difficulty : difficulties) {
            initGroupsForDifficulty(difficulty);

            percentageLogic = (solvedLogic.size() * 100.0) / allInGroup.size();
            percentageTrialAndError = (solvedTrialAndError.size() * 100.0) / allInGroup.size();
            percentageRecursion = (solvedRecurison.size() * 100.0) / allInGroup.size();

            System.out.printf("allInGroup(" + difficulty + "): " + solvedLogic.size() + " , percentage: %.2f\n", percentageLogic);
            System.out.printf("allInGroup(" + difficulty + "): " + solvedTrialAndError.size() + " , percentage: %.2f\n", percentageTrialAndError);
            System.out.printf("allInGroup(" + difficulty + "): " + solvedRecurison.size() + " , percentage: %.2f\n\n", percentageRecursion);
        }
    }

    public void collectAndPrintPercentageSolvedDimensions() {
        System.out.println("-".repeat(100));
        System.out.println("Percentage solved in all groups, all steps: ");

        double percentageLogic;
        double percentageTrialAndError;
        double percentageRecursion;

        for(String dimension : dimensions) {
            initGroupsForDimension(dimension);

            percentageLogic = (solvedLogic.size() * 100.0) / allInGroup.size();
            percentageTrialAndError = (solvedTrialAndError.size() * 100.0) / allInGroup.size();
            percentageRecursion = (solvedRecurison.size() * 100.0) / allInGroup.size();

            System.out.printf("allInGroup(" + dimension + "): " + solvedLogic.size() + " , percentage: %.2f\n", percentageLogic);
            System.out.printf("allInGroup(" + dimension + "): " + solvedTrialAndError.size() + " , percentage: %.2f\n", percentageTrialAndError);
            System.out.printf("allInGroup(" + dimension + "): " + solvedRecurison.size() + " , percentage: %.2f\n\n", percentageRecursion);
        }
    }

    public void collectAndPrintMeanSolveTimeDifficulties() {
        System.out.println("-".repeat(100));
        System.out.println("Mean solve times in all groups, all steps: ");

        double meanTimeLogic;
        double meanTimeTrialAndError;
        double meanTimeRecursion;

        for(double difficulty : difficulties) {
            initGroupsForDifficulty(difficulty);

            if(solvedLogic.size() == 0 ) {
                meanTimeLogic = 0;
            } else {
                meanTimeLogic = solvedLogic.stream().mapToDouble(NonogramResult::getTimeAfterHeuristics).sum() / solvedLogic.size();
            }

            meanTimeTrialAndError = solvedTrialAndError.stream().mapToDouble(NonogramResult::getTimeAfterTrialAndError).sum() / solvedTrialAndError.size();
            meanTimeRecursion = solvedRecurison.stream().mapToDouble(NonogramResult::getTimeAfterRecursion).sum() / solvedRecurison.size();

            System.out.printf("Nonograms solved by logic(" + difficulty + "): " + solvedLogic.size() + " , meanTime: %.2f\n", meanTimeLogic);
            System.out.printf("Nongorams solved by trial and error(" + difficulty + "): " + solvedTrialAndError.size() + " , meanTime: %.2f\n", meanTimeTrialAndError);
            System.out.printf("Nonogram solved by recursion(" + difficulty + "): " + solvedRecurison.size() + " , meanTime: %.2f\n\n", meanTimeRecursion);
        }
    }

    public void collectAndPrintMeanSolveTimeDimensions() {
        System.out.println("-".repeat(100));
        System.out.println("Mean solve times in all groups, all steps: ");

        double meanTimeLogic;
        double meanTimeTrialAndError;
        double meanTimeRecursion;

        for(String dimension : dimensions) {
            initGroupsForDimension(dimension);

            meanTimeLogic = solvedLogic.size() > 0 ? solvedLogic.stream().mapToDouble(NonogramResult::getTimeAfterHeuristics).sum() / solvedLogic.size() : 0;

            meanTimeTrialAndError = solvedTrialAndError.size() > 0 ? solvedTrialAndError.stream().mapToDouble(NonogramResult::getTimeAfterTrialAndError).sum() / solvedTrialAndError.size() : 0;

            meanTimeRecursion = solvedRecurison.size() > 0 ? solvedRecurison.stream().mapToDouble(NonogramResult::getTimeAfterRecursion).sum() / solvedRecurison.size() : 0;

            System.out.printf("Nonograms solved by logic(" + dimension + "): " + solvedLogic.size() + " , meanTime: %.2f\n", meanTimeLogic);
            System.out.printf("Nongorams solved by trial and error(" + dimension + "): " + solvedTrialAndError.size() + " , meanTime: %.2f\n", meanTimeTrialAndError);
            System.out.printf("Nonogram solved by recursion(" + dimension + "): " + solvedRecurison.size() + " , meanTime: %.2f\n\n", meanTimeRecursion);
        }
    }



    public void collectAndPrintMinAndMaxSolveTimeDifficulties() {
        System.out.println("-".repeat(100));
        System.out.println("Minimum and maximum solve times in all groups, all steps: ");

        double minTimeLogic;
        double maxTimeLogic;
        double minTimeTrialAndError;
        double maxTimeTrialAndError;
        double minTimeRecursion;
        double maxTimeRecursion;

        for(double difficulty : difficulties) {
            initGroupsForDifficulty(difficulty);

            minTimeLogic = getMinimumTimeAfterHeuristics();
            maxTimeLogic = getMaximumTimeAfterHeuristics();
            minTimeTrialAndError = getMinimumTimeAfterTrialAndError();
            maxTimeTrialAndError = getMaximumTimeAfterTrialAndError();
            minTimeRecursion = getMinimumTimeAfterRecursion();
            maxTimeRecursion = getMaximumTimeAfterRecursion();

            System.out.println("Minimum time to solve using only logic(" + difficulty + "): " + minTimeLogic);
            System.out.println("Maximum time to solve using only logic(" + difficulty + "): " + maxTimeLogic);
            System.out.println("Minimum time to solve using trial and error(" + difficulty + "): " + minTimeTrialAndError);
            System.out.println("Maximum time to solve using trial and error(" + difficulty + "): " + maxTimeTrialAndError);
            System.out.println("Minimum time to solve using recursive max depth 5(" + difficulty + "): " + minTimeRecursion);
            System.out.println("Minimum time to solve using recursive max depth 5(" + difficulty + "): " + maxTimeRecursion + "\n");
        }
    }

    public void collectAndPrintMinAndMaxSolveTimeDimensions() {
        System.out.println("-".repeat(100));
        System.out.println("Minimum and maximum solve times in all groups, all steps: ");

        double minTimeLogic;
        double maxTimeLogic;
        double minTimeTrialAndError;
        double maxTimeTrialAndError;
        double minTimeRecursion;
        double maxTimeRecursion;

        for(String dimension : dimensions) {
            initGroupsForDimension(dimension);

            minTimeLogic = getMinimumTimeAfterHeuristics();
            maxTimeLogic = getMaximumTimeAfterHeuristics();
            minTimeTrialAndError = getMinimumTimeAfterTrialAndError();
            maxTimeTrialAndError = getMaximumTimeAfterTrialAndError();
            minTimeRecursion = getMinimumTimeAfterRecursion();
            maxTimeRecursion = getMaximumTimeAfterRecursion();

            System.out.println("Minimum time to solve using only logic(" + dimension + "): " + minTimeLogic);
            System.out.println("Maximum time to solve using only logic(" + dimension + "): " + maxTimeLogic);
            System.out.println("Minimum time to solve using trial and error(" + dimension + "): " + minTimeTrialAndError);
            System.out.println("Maximum time to solve using trial and error(" + dimension + "): " + maxTimeTrialAndError);
            System.out.println("Minimum time to solve using recursive max depth 5(" + dimension + "): " + minTimeRecursion);
            System.out.println("Minimum time to solve using recursive max depth 5(" + dimension + "): " + maxTimeRecursion + "\n");
        }
    }

    public void collectAndPrintMeanDecisionsLengthDifficulties() {
        System.out.println("-".repeat(100));
        System.out.println("Mean decisions lengths in all groups, all steps: ");

        double meanDecisionsTrialAndError;

        for(double difficulty : difficulties) {
            initGroupsForDifficulty(difficulty);

            meanDecisionsTrialAndError = solvedTrialAndError.stream().mapToInt(NonogramResult::getDecisionsListSize).sum() / solvedTrialAndError.size();

            System.out.println("Mean decisions trial and error(" + difficulty + "): " + meanDecisionsTrialAndError);
        }
        System.out.println();
    }

    public void collectAndPrintMeanDecisionsLengthDimensions() {
        System.out.println("-".repeat(100));
        System.out.println("Mean decisions lengths in all groups, all steps: ");

        double meanDecisionsTrialAndError;

        for(String dimension : dimensions) {
            initGroupsForDimension(dimension);

            meanDecisionsTrialAndError = solvedTrialAndError.size() > 0 ? (solvedTrialAndError.stream().mapToInt(NonogramResult::getDecisionsListSize).sum() / solvedTrialAndError.size()) : 0.0;

            System.out.println("Mean decisions trial and error(" + dimension + "): " + meanDecisionsTrialAndError);
        }
        System.out.println();
    }

    public void collectAndPrintMinAndMaxDecisionsLengthDifficulties() {
        System.out.println("-".repeat(100));
        System.out.println("Mean decisions lengths in all groups, all steps: ");

        int minDecisionsTrialAndError;
        int maxDecisionsTrialAndError;

        for(double difficulty : difficulties) {
            initGroupsForDifficulty(difficulty);

            minDecisionsTrialAndError = solvedTrialAndError.stream().mapToInt(NonogramResult::getDecisionsListSize).min().getAsInt();
            maxDecisionsTrialAndError = solvedTrialAndError.stream().mapToInt(NonogramResult::getDecisionsListSize).max().getAsInt();

            System.out.println("Min decisions trial and error(" + difficulty + "): " + minDecisionsTrialAndError + "\n");
            System.out.println("Max decisions trial and error(" + difficulty + "): " + maxDecisionsTrialAndError + "\n");
        }
        System.out.println();
    }

    public void collectAndPrintMinAndMaxDecisionsLengthDimensions() {
        System.out.println("-".repeat(100));
        System.out.println("Mean decisions lengths in all groups, all steps: ");

        int minDecisionsTrialAndError;
        int maxDecisionsTrialAndError;

        for(String dimension : dimensions) {
            initGroupsForDimension(dimension);

            minDecisionsTrialAndError = solvedTrialAndError.size() > 0 ?
                    solvedTrialAndError.stream().mapToInt(NonogramResult::getDecisionsListSize).min().getAsInt() : 0;
            maxDecisionsTrialAndError = solvedTrialAndError.size() > 0 ?
                    solvedTrialAndError.stream().mapToInt(NonogramResult::getDecisionsListSize).max().getAsInt() : 0;

            System.out.println("Min decisions trial and error(" + dimension + "): " + minDecisionsTrialAndError + "\n");
            System.out.println("Max decisions trial and error(" + dimension + "): " + maxDecisionsTrialAndError + "\n");
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

    public void collectAndPrintMinAndMaxProgressAfterDecisionDifficulties() {
        double minStep;
        double maxStep;

        for(double difficulty : difficulties) {
            initGroupsForDifficulty(difficulty);

            minStep = 100.00;
            maxStep =   0.00;

            for(NonogramResult nonogramResult : solvedTrialAndError) {
                if (calculateMaxProgressAfterDecision(nonogramResult) > maxStep) {
                    maxStep = calculateMaxProgressAfterDecision(nonogramResult);
                }
                if (calculateMinProgressAfterDecision(nonogramResult) < minStep) {
                    minStep = calculateMinProgressAfterDecision(nonogramResult);
                }
            }

            System.out.println("Min step trial and error(" + difficulty + "): " + minStep + "\n");
            System.out.println("Max step trial and error(" + difficulty + "): " + maxStep + "\n");
        }
        System.out.println();
    }

    public void collectAndPrintMinAndMaxProgressAfterDecisionDimensions() {
        double minStep;
        double maxStep;

        for(String dimension : dimensions) {
            initGroupsForDimension(dimension);

            minStep = 100.00;
            maxStep =   0.00;

            for(NonogramResult nonogramResult : solvedTrialAndError) {
                if (calculateMaxProgressAfterDecision(nonogramResult) > maxStep) {
                    maxStep = calculateMaxProgressAfterDecision(nonogramResult);
                }
                if (calculateMinProgressAfterDecision(nonogramResult) < minStep) {
                    minStep = calculateMinProgressAfterDecision(nonogramResult);
                }
            }

            System.out.println("Min step trial and error(" + dimension + "): " + minStep + "\n");
            System.out.println("Max step trial and error(" + dimension + "): " + maxStep + "\n");
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

    public void collectAndPrintMinPercentageSolvedAfterLogicOnlyDifficulties() {
        double minSolutionPercentage;
        double maxSolutionPercentage;

        for(double difficulty : difficulties) {
            initGroupsForDifficulty(difficulty);

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
            System.out.println("Min solution percentage after logic(" + difficulty + "): " + minSolutionPercentage + "\n");
            System.out.println("Max solution percentage after logic(" + difficulty + "): " + maxSolutionPercentage + "\n");
        }
        System.out.println();
    }

    public void collectAndPrintMinPercentageSolvedAfterLogicOnlyDimensions() {
        double minSolutionPercentage;
        double maxSolutionPercentage;

        for(String dimension : dimensions) {
            initGroupsForDimension(dimension);

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
            System.out.println("Min solution percentage after logic(" + dimension + "): " + minSolutionPercentage + "\n");
            System.out.println("Max solution percentage after logic(" + dimension + "): " + maxSolutionPercentage + "\n");
        }
        System.out.println();
    }

    public void collectAndPrintPercentAfterLogicAndDecisionSizeCorrelationDifficulties() {
        System.out.println("-".repeat(100));

        for(double difficulty : difficulties) {
            initGroupsForDifficulty(difficulty);
            printPercentAfterLogicAndDecisionSizeCorrelationForDifficultyGroup(difficulty);
        }
    }

    public void collectAndPrintPercentAfterLogicAndDecisionSizeCorrelationDimension() {
        System.out.println("-".repeat(100));

        for(String dimension : dimensions) {
            initGroupsForDimension(dimension);
            if(solvedTrialAndError.size() > 0) {
                printPercentAfterLogicAndDecisionSizeCorrelationForDimensionGroup(dimension);
            }
        }
    }

    public void printPercentAfterLogicAndDecisionSizeCorrelationForDifficultyGroup(double difficulty) {
        System.out.println("Percent after logic - decisions size: ");

        for(NonogramResult nonogramResult : solvedTrialAndError) {
            System.out.println(
                    justifiedString(7, String.valueOf(difficulty))
                            + ", percentage after logic: " +
                    justifiedString(7, String.valueOf(nonogramResult.getPercentageCompletionAfterHeuristics()))
                            + ", dec size:" +
                    justifiedString(7, String.valueOf(nonogramResult.getDecisionsListSize()))
            );
        }
    }

    public void printPercentAfterLogicAndDecisionSizeCorrelationForDimensionGroup(String dimension) {
        System.out.println("Percent after logic - decisions size: ");

        for(NonogramResult nonogramResult : solvedTrialAndError) {
            System.out.println(
                    justifiedString(7, dimension)
                            + ", percentage after logic: " +
                            justifiedString(7, String.valueOf(nonogramResult.getPercentageCompletionAfterHeuristics()))
                            + ", dec size:" +
                            justifiedString(7, String.valueOf(nonogramResult.getDecisionsListSize()))
            );
        }
    }

    public void printSelectedNonogramsToFurtherAnalysisDifficulties() {
        List<NonogramResult> filteredResults = nonogramResults
                .stream()
                .filter(nonogramResult -> nonogramResult.getDifficulty() >= 3.0
                    && nonogramResult.getDecisionsListSize() >= DECISION_SIZE_LOWER_LIMIT
                    && nonogramResult.getDecisionsListSize() <= DECISION_SIZE_UPPER_LIMIT
                    && nonogramResult.getTimeAfterTrialAndError() >= TIME_LOWER_LIMIT
                    && nonogramResult.getTimeAfterTrialAndError() <= TIME_UPPER_LIMIT)
                .collect(Collectors.toList());

        List<NonogramResult> nonogramResultsDifficulty3 = filteredResults
                .stream()
                .filter(nonogramResult -> nonogramResult.getDifficulty() == 3.0)
                .collect(Collectors.toList());

        List<NonogramResult> nonogramResultsDifficulty4 = filteredResults
                .stream()
                .filter(nonogramResult -> nonogramResult.getDifficulty() == 4.0)
                .collect(Collectors.toList());

        List<NonogramResult> nonogramResultsDifficulty5 = filteredResults
                .stream()
                .filter(nonogramResult -> nonogramResult.getDifficulty() == 5.0)
                .collect(Collectors.toList());

        for(int loopsNo = 0; loopsNo < 100; loopsNo++) {

            for(int size3index = 0; size3index < 5; size3index++) {
                System.out.printf("%s ", nonogramResultsDifficulty3.get(size3index).getFilename());
            }
            System.out.println();
            for(int size4index = 0; size4index < 5; size4index++) {
                System.out.printf("%s ", nonogramResultsDifficulty4.get(size4index).getFilename());
            }
            System.out.println();
            for(int size5index = 0; size5index < 5; size5index++) {
                System.out.printf("%s ", nonogramResultsDifficulty3.get(size5index).getFilename());
            }

            nonogramResultsDifficulty3 = shuffleNonogramResultsArray(nonogramResultsDifficulty3);
            nonogramResultsDifficulty4 = shuffleNonogramResultsArray(nonogramResultsDifficulty4);
            nonogramResultsDifficulty5 = shuffleNonogramResultsArray(nonogramResultsDifficulty5);

            System.out.println();
            System.out.println("-".repeat(100));

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
                .filter(nonogramResult -> nonogramResult.getDifficulty() == difficulty
                    && nonogramResult.getSource().equals(source))
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

    public void initGroupsForDimension(String dimension) {
        allInGroup = nonogramResults
                .stream()
                .filter(nonogramResult -> (nonogramResult.getWidth() + "x" + nonogramResult.getHeight()).equals(dimension)
                        && nonogramResult.getSource().equals(source))
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

    public static List<NonogramResult> shuffleNonogramResultsArray(List<NonogramResult> inputList) {
        Collections.shuffle(inputList);
        return inputList;
    }
}
