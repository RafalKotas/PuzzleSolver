package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFullSolutionData;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramGuessActionsLog;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramNodeLog;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramSolution;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionNode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramSolverUtils;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogConverter;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogGroupingPrinter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConsts.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.COLOURED_FIELD;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.X_FIELD;

@Getter
@Setter
@Slf4j
@ToString(onlyExplicitlyIncluded = true)
public class NonogramSolver {

    private final NonogramLogicFactory logicFactory;

    private static final int maxTreeHeight = 50;
    private GuessMode guessMode = GuessMode.DISABLED;
    private static final boolean recursionModeEnabled = false;
    private boolean solved = false;

    private static final boolean LOG_STEPS_SOLVER = false;
    private boolean printNodeCompletionPercentage = true;

    private boolean oneOfTwoDecisionsWrong;
    private NonogramSolutionNode rootNode;
    private NonogramSolutionNode solutionNode;
    private NonogramLogic finalSolutionLogic;

    @ToString.Include
    private String solutionFileName;
    private NonogramSolution nonogramSolution;
    private NonogramLogic solutionLogic;
    private Gson gson = new Gson();


    private List<NonogramSolutionNode> nonogramNodes;

    public NonogramSolver(NonogramLogic nonogramLogic, String fileName, GuessMode guessMode, NonogramLogicFactory logicFactory) {
        this.logicFactory = logicFactory;
        this.solutionNode = new NonogramSolutionNode(nonogramLogic, logicFactory);

        NonogramRules rules = nonogramLogic.getNonogramRules();
        this.solutionLogic = new NonogramLogic(rules, guessMode);
        this.solutionFileName = "r" + fileName + JSON_EXTENSION;
        this.guessMode = guessMode;
    }

    public NonogramLogic runSolutionAtNode(NonogramSolutionNode nonogramStartNode) {
        if (LOG_STEPS_SOLVER) {
            solvingAtNodeInitialLogs(nonogramStartNode);
        }
        this.runHeuristicSolver(nonogramStartNode, solutionFileName,  0, maxTreeHeight);
        return this.getSolutionNode().getNonogramLogic();
    }

    private void solvingAtNodeInitialLogs(NonogramSolutionNode nonogramStartNode) {
        log.info("RUN SOLVER AT NODE, DECISIONS SIZE {}", nonogramStartNode.getNonogramGuessDecisions().size());
        log.info("ROW SEQUENCES RANGES: \n{}", nonogramStartNode.getNonogramLogic().getRowsSequencesRanges());
        log.info("COLUMN SEQUENCES RANGES: \n{}", nonogramStartNode.getNonogramLogic().getColumnsSequencesRanges());
    }

    public void runHeuristicSolver(NonogramSolutionNode nonogramStartNode, String solutionFileName,
                                   int currentTreeHeight, int maxTreeHeight) {
        NonogramSolutionNode nonogramSubsolutionNode = logicFactory.copyNode(nonogramStartNode);
        NonogramSolutionNode leftNodeO;
        NonogramSolutionNode rightNodeX;

        NonogramFullSolutionData solutionData = NonogramSolverUtils.loadFullSolutionData(solutionFileName);
        if (solutionData != null) {
            NonogramLogic logic = nonogramSubsolutionNode.getNonogramLogic();
            logic.setCorrectSolutionBoard(solutionData.getFinalBoard());
            logic.setCorrectRowRanges(solutionData.getDerivedRowRanges());
            logic.setCorrectColumnRanges(solutionData.getDerivedColumnRanges());
        }

        if (nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() == 100) {
            if (LOG_STEPS_SOLVER) {
                log.info("Solution found, recursion depth: {}", currentTreeHeight);
            }
            replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
        }

        Optional<NonogramSolutionDecision> correctDecision;

        //solve without guesses - only heuristics
        nonogramSubsolutionNode.getNonogramLogic().fillTrivialRowsAndColumns();
        nonogramSubsolutionNode.setNodeLogs(nonogramSubsolutionNode.getNonogramLogic().getLogs());
        nonogramSubsolutionNode.makeBasicSolverActions();

        if (LOG_STEPS_SOLVER) {
            printOverallHeuristicsResult(nonogramSubsolutionNode);

            List<String> rawLogs = nonogramSubsolutionNode.getNodeLogs();
            List<String> convertedLogs = new ArrayList<>();

            for (String rawLog : rawLogs) {
                NonogramLogic logic = nonogramSubsolutionNode.getNonogramLogic();
                LogConverter.convertLogByAction(rawLog, solutionFileName, logic, LogConverter.detectActionTypeFromRawLog(rawLog))
                        .ifPresentOrElse(
                                convertedLogs::add,
                                () -> System.out.println(rawLog)
                        );
            }

            LogGroupingPrinter.printLogsGroupedByDetectedType(rawLogs, convertedLogs);
        }

        // heuristic logs
        NonogramNodeLog nonogramNodeLog = new NonogramNodeLog(nonogramSubsolutionNode.getNonogramLogic().getLogs());
        // guess logs
        List<NonogramGuessActionsLog> guessesLogs = new ArrayList<>();
        // guess log
        NonogramGuessActionsLog nonogramGuessActionsLog;

        if (currentTreeHeight == 0) {
            replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
        }

        if (this.guessMode == GuessMode.ENABLED && nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() != 100) {
            if (LOG_STEPS_SOLVER) {
                log.info("Completion percentage after heuristics only: {}", nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
            }

            int wrongDecisionsCount;

            do {
                nonogramSubsolutionNode.getNonogramLogic().updateCurrentAvailableChoices();
                nonogramSubsolutionNode.getNonogramLogic().clearLogs();

                correctDecision = Optional.empty();

                this.oneOfTwoDecisionsWrong = false;

                wrongDecisionsCount = -1;

                List<NonogramSolutionDecision> availableChoices = nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices();
                // START time measure
                for (NonogramSolutionDecision decision : availableChoices) {
                    leftNodeO = copyNodeAndAddDecision(decision, COLOURED_FIELD, nonogramSubsolutionNode);
                    rightNodeX = copyNodeAndAddDecision(decision, X_FIELD, nonogramSubsolutionNode);

                    if (!leftNodeO.getNonogramLogic().getNonogramState().isInvalidSolution()) {
                        if (rightNodeX.getNonogramLogic().getNonogramState().isInvalidSolution()) {
                            decision.setDecisionMarker(COLOURED_FIELD);
                            correctDecision = Optional.of(decision);
                            nonogramSubsolutionNode = logicFactory.copyNode(leftNodeO);
                            if (currentTreeHeight == 0) {
                                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                            }
                            this.oneOfTwoDecisionsWrong = true;
                            wrongDecisionsCount = 1;
                            break;
                        } else {
                            wrongDecisionsCount = 0;
                        }
                    } else {
                        if (rightNodeX.getNonogramLogic().getNonogramState().isInvalidSolution()) {
                            wrongDecisionsCount = 2;
                        } else {
                            decision.setDecisionMarker(X_FIELD);
                            correctDecision = Optional.of(decision);
                            nonogramSubsolutionNode = logicFactory.copyNode(rightNodeX);
                            if (currentTreeHeight == 0) {
                                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                            }
                            this.oneOfTwoDecisionsWrong = true;
                            wrongDecisionsCount = 1;
                        }
                        break;
                    }
                }

                if (currentTreeHeight == 0) {
                    if (correctDecision.isPresent()) {
                        nonogramGuessActionsLog = new NonogramGuessActionsLog(correctDecision.get(), nonogramSubsolutionNode.getNonogramLogic().getLogs());
                        guessesLogs.add(nonogramGuessActionsLog);
                    }
                    if (wrongDecisionsCount == -1) {
                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                    }
                } else {
                    if (nonogramSubsolutionNode.getNonogramLogic().isSolved()) {
                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                        if (LOG_STEPS_SOLVER) {
                            log.info("Nonogram solved, recursion depth: {}", currentTreeHeight);
                        }
                    }
                }
            } while (
                guessModeContinueDecision("oneOfTwoWrong")
            );

            if (currentTreeHeight == 0) {
                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
            }

            if (wrongDecisionsCount == 0) {
                if (currentTreeHeight == 0) {
                    nonogramNodeLog.setGuessesLogs(guessesLogs);
                    replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);

                    if (!nonogramSubsolutionNode.getNonogramLogic().isSolved()) {
                        if (LOG_STEPS_SOLVER) {
                            log.info("Need to use recursion, completion percentage at start: {}", nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
                        }

                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);

                        NonogramSolutionDecision decisionCoefficientsMax = new NonogramSolutionDecision();
                        int maxNextFilled = 0;
                        int leftNodeFilled;
                        int rightNodeFilled;
                        for (NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {
                            leftNodeO = copyNodeAndAddDecision(decision, COLOURED_FIELD, nonogramSubsolutionNode);
                            leftNodeO.makeBasicSolverActions();
                            leftNodeFilled = leftNodeO.getNonogramLogic().fieldsFilled();

                            rightNodeX = copyNodeAndAddDecision(decision, X_FIELD, nonogramSubsolutionNode);
                            rightNodeX.makeBasicSolverActions();
                            rightNodeFilled = rightNodeX.getNonogramLogic().fieldsFilled();

                            if (maxNextFilled < Math.max(leftNodeFilled, rightNodeFilled)) {
                                decisionCoefficientsMax = new NonogramSolutionDecision(
                                        decision.getDecisionMarker(),
                                        decision.getDecisionField()
                                );
                                maxNextFilled = Math.max(leftNodeFilled, rightNodeFilled);
                            }
                        }
                        NonogramSolutionNode leftNodeRecursive = logicFactory.copyNode(nonogramSubsolutionNode);
                        decisionCoefficientsMax.setDecisionMarker(COLOURED_FIELD);
                        leftNodeRecursive.addDecision(decisionCoefficientsMax);
                        leftNodeRecursive.colourOrPlaceX();
                        leftNodeRecursive.makeBasicSolverActions();

                        NonogramSolutionNode rightNodeRecursive = logicFactory.copyNode(nonogramSubsolutionNode);
                        decisionCoefficientsMax.setDecisionMarker(X_FIELD);
                        rightNodeRecursive.addDecision(decisionCoefficientsMax);
                        rightNodeRecursive.colourOrPlaceX();
                        rightNodeRecursive.makeBasicSolverActions();

                        runHeuristicSolver(leftNodeRecursive, solutionFileName, currentTreeHeight + 1, maxTreeHeight);
                        runHeuristicSolver(rightNodeRecursive, solutionFileName, currentTreeHeight + 1, maxTreeHeight);
                    } else {
                        if (LOG_STEPS_SOLVER) {
                            log.info("Don't need to use recursion. Nonogram solved at ");
                        }
                    }
                } else if (recursionModeEnabled && currentTreeHeight <= maxTreeHeight) {
                    if (nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() == 100) {
                        if (LOG_STEPS_SOLVER) {
                            log.info("Solution found recursion (tree height: {})!!!\n", currentTreeHeight);
                        }
                        replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                    } else {
                        NonogramSolutionDecision decisionCoefficientsMax = new NonogramSolutionDecision();
                        int maxNextFilled = 0;
                        int leftNodeFilled;
                        int rightNodeFilled;

                        for (NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {
                            leftNodeO = copyNodeAndAddDecision(decision, COLOURED_FIELD, nonogramSubsolutionNode);
                            leftNodeO.makeBasicSolverActions();
                            leftNodeFilled = leftNodeO.getNonogramLogic().fieldsFilled();

                            rightNodeX = copyNodeAndAddDecision(decision, X_FIELD, nonogramSubsolutionNode);
                            rightNodeX.makeBasicSolverActions();
                            rightNodeFilled = rightNodeX.getNonogramLogic().fieldsFilled();

                            if (maxNextFilled < Math.max(leftNodeFilled, rightNodeFilled)) {
                                decisionCoefficientsMax = new NonogramSolutionDecision(
                                        decision.getDecisionMarker(),
                                        decision.getDecisionField()
                                );
                                maxNextFilled = Math.max(leftNodeFilled, rightNodeFilled);
                            }
                        }
                        NonogramSolutionNode leftNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, COLOURED_FIELD, nonogramSubsolutionNode);
                        NonogramSolutionNode rightNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, X_FIELD, nonogramSubsolutionNode);

                        runHeuristicSolver(leftNodeRecursive, solutionFileName,currentTreeHeight + 1, maxTreeHeight);
                        runHeuristicSolver(rightNodeRecursive, solutionFileName, currentTreeHeight + 1, maxTreeHeight);
                    }
                }
            } else if (wrongDecisionsCount == 2 && LOG_STEPS_SOLVER) {
                log.info("Solver ends at node, both decisions wrong (treeHeight: {}, completeness: {}).\n",
                        currentTreeHeight, nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
            }
        } else if (LOG_STEPS_SOLVER) {
            log.info("full solution:");
            log.info(".".repeat(50));
            log.info("currentTreeHeight: {} , completion percentage without guess enabled: {}",
                    currentTreeHeight, nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
        }
    }

    private void printOverallHeuristicsResult(NonogramSolutionNode nonogramSubsolutionNode) {
            log.info("decisions {}", nonogramSubsolutionNode.getNonogramLogic().getActionsToDoList().size());
            System.out.println("-".repeat(100));

            log.info("Fields filled after fill trivial rows and columns: {}", nonogramSubsolutionNode.getNonogramLogic().fieldsFilled());
            log.info("COMPLETION PERCENTAGE: {}, DECISIONS SIZE: {}", nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage(), nonogramSubsolutionNode.getNonogramGuessDecisions().size());
            log.info("SOLUTION STEPS: ");
    }

    public NonogramSolutionNode copyNodeAndAddDecision(NonogramSolutionDecision decision, String decisionMarker, NonogramSolutionNode nodeToCopy) {
        NonogramSolutionDecision nodeDecision = new NonogramSolutionDecision(decisionMarker, decision.getDecisionField());
        NonogramSolutionNode nodeToAddDecision = logicFactory.copyNode(nodeToCopy);
        nodeToAddDecision.addDecision(nodeDecision);
        nodeToAddDecision.colourOrPlaceX();
        nodeToAddDecision.makeBasicSolverActions();
        return logicFactory.copyNode(nodeToAddDecision);
    }

    //replace solutionNode with this with higher completion percentage
    public void replaceSolutionNodeWithMoreBeneficialSolution(NonogramSolutionNode nodeToCheck) {

        double oldNodeCompletionPercentage = this.solutionNode.getNonogramLogic().getCompletionPercentage();
        double nodeToCheckCompletionPercentage = nodeToCheck.getNonogramLogic().getCompletionPercentage();

        if (printNodeCompletionPercentage && LOG_STEPS_SOLVER) {
            log.info("old Node cp: {}", oldNodeCompletionPercentage);
            log.info("current Node cp: {}", nodeToCheckCompletionPercentage);
        }

        if (nodeToCheckCompletionPercentage > oldNodeCompletionPercentage) {
            if (LOG_STEPS_SOLVER) {
                log.info("Replace solutionLogic with new nonogramLogic, percentage completion: {}", nodeToCheckCompletionPercentage);
            }

            this.solutionNode = logicFactory.copyNode(nodeToCheck);
            this.solutionLogic = logicFactory.copy(nodeToCheck.getNonogramLogic());
            solutionLogic.initializeHelpers();
        } else {
            this.solutionNode = logicFactory.copyNode(this.solutionNode);
        }
    }

    private boolean guessModeContinueDecision (String wordExpression) {
        return switch (wordExpression) {
            case "toFirstDecision" -> this.solutionNode.getNonogramGuessDecisions().isEmpty();
            case "toInvalidSolution" -> !this.solutionNode.getNonogramLogic().getNonogramState().isInvalidSolution();
            default -> this.oneOfTwoDecisionsWrong;
        };
    }
}
