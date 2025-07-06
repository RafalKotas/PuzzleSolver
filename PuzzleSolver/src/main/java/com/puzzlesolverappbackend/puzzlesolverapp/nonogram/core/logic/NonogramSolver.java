package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFullSolutionData;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramGuessActionsLog;
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

import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConstants.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.COLOURED_FIELD;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.X_FIELD;

@Getter
@Setter
@Slf4j
@ToString(onlyExplicitlyIncluded = true)
public class NonogramSolver {

    private final NonogramLogicFactory logicFactory;

    private static final int MAX_TREE_HEIGHT = 50;
    private GuessMode guessMode = GuessMode.DISABLED;
    private static final boolean RECURSION_MODE_ENABLED = false;
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
        this.runHeuristicSolver(nonogramStartNode, solutionFileName,  0, MAX_TREE_HEIGHT);
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
        loadSolutionData(solutionFileName, nonogramSubsolutionNode);

        if (nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() == 100) {
            logIf(LOG_STEPS_SOLVER, "Solution found, recursion depth: {}", currentTreeHeight);
            replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
            return;
        }

        nonogramSubsolutionNode.getNonogramLogic().fillTrivialRowsAndColumns();
        nonogramSubsolutionNode.setNodeLogs(nonogramSubsolutionNode.getNonogramLogic().getLogs());
        nonogramSubsolutionNode.makeBasicSolverActions();

        if (LOG_STEPS_SOLVER) {
            logHeuristicResults(nonogramSubsolutionNode, solutionFileName);
        }

        List<NonogramGuessActionsLog> guessesLogs = new ArrayList<>();

        if (currentTreeHeight == 0) {
            replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
        }

        if (this.guessMode == GuessMode.ENABLED && nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() != 100) {
            int wrongDecisionsCount = runGuessLoop(nonogramSubsolutionNode, currentTreeHeight, guessesLogs);

            if (currentTreeHeight == 0) {
                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
            }

            if (wrongDecisionsCount == 0) {
                recurseIfNeeded(nonogramSubsolutionNode, currentTreeHeight, maxTreeHeight, solutionFileName);
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

    private void loadSolutionData(String fileName, NonogramSolutionNode node) {
        NonogramFullSolutionData solutionData = NonogramSolverUtils.loadFullSolutionData(fileName);
        if (solutionData != null) {
            NonogramLogic logic = node.getNonogramLogic();
            logic.setCorrectSolutionBoard(solutionData.getFinalBoard());
            logic.setCorrectRowRanges(solutionData.getDerivedRowRanges());
            logic.setCorrectColumnRanges(solutionData.getDerivedColumnRanges());
        }
    }

    private void logIf(boolean condition, String message, Object... args) {
        if (condition) {
            log.info(message, args);
        }
    }

    private void logHeuristicResults(NonogramSolutionNode node, String solutionFileName) {
        printOverallHeuristicsResult(node);

        List<String> rawLogs = node.getNodeLogs();
        List<String> convertedLogs = new ArrayList<>();

        for (String rawLog : rawLogs) {
            NonogramLogic logic = node.getNonogramLogic();
            LogConverter.convertLogByAction(rawLog, solutionFileName, logic, LogConverter.detectActionTypeFromRawLog(rawLog))
                    .ifPresentOrElse(convertedLogs::add, () -> log.info(rawLog));
        }

        LogGroupingPrinter.printLogsGroupedByDetectedType(rawLogs, convertedLogs);
    }

    private void recurseIfNeeded(NonogramSolutionNode node, int currentTreeHeight, int maxTreeHeight, String fileName) {
        if (currentTreeHeight == 0) {
            if (!node.getNonogramLogic().isSolved()) {
                logIf(LOG_STEPS_SOLVER, "Need to use recursion, completion percentage at start: {}",
                        node.getNonogramLogic().getCompletionPercentage());

                NonogramSolutionDecision bestDecision = findBestDecision(node);

                runDecisionRecursively(node, bestDecision, fileName, currentTreeHeight);
            } else {
                logIf(LOG_STEPS_SOLVER, "Don't need to use recursion. Nonogram solved at ");
            }
        } else if (RECURSION_MODE_ENABLED && currentTreeHeight <= maxTreeHeight) {
            if (node.getNonogramLogic().getCompletionPercentage() == 100) {
                logIf(LOG_STEPS_SOLVER, "Solution found recursion (tree height: {})!!!\n", currentTreeHeight);
                replaceSolutionNodeWithMoreBeneficialSolution(node);
            } else {
                NonogramSolutionDecision bestDecision = findBestDecision(node);

                runDecisionRecursively(node, bestDecision, fileName, currentTreeHeight);
            }
        }
    }

    private NonogramSolutionDecision findBestDecision(NonogramSolutionNode node) {
        NonogramSolutionDecision best = new NonogramSolutionDecision();
        int maxFilled = 0;

        for (NonogramSolutionDecision decision : node.getNonogramLogic().getAvailableChoices()) {
            int leftFilled = evaluateFilledFields(decision, node, COLOURED_FIELD);
            int rightFilled = evaluateFilledFields(decision, node, X_FIELD);

            if (Math.max(leftFilled, rightFilled) > maxFilled) {
                best = new NonogramSolutionDecision(decision.getDecisionMarker(), decision.getDecisionField());
                maxFilled = Math.max(leftFilled, rightFilled);
            }
        }

        return best;
    }

    private void runDecisionRecursively(NonogramSolutionNode node, NonogramSolutionDecision decision, String fileName, int depth) {
        NonogramSolutionNode left = copyNodeWithDecisionAddedAndSolve(decision, COLOURED_FIELD, node);
        left.colourOrPlaceX();
        left.makeBasicSolverActions();

        NonogramSolutionNode right = copyNodeWithDecisionAddedAndSolve(decision, X_FIELD, node);
        right.colourOrPlaceX();
        right.makeBasicSolverActions();

        runHeuristicSolver(left, fileName, depth + 1, depth + 5);
        runHeuristicSolver(right, fileName, depth + 1, depth + 5);
    }

    private int evaluateFilledFields(NonogramSolutionDecision decision, NonogramSolutionNode node, String marker) {
        NonogramSolutionNode copy = copyNodeWithDecisionAddedAndSolve(decision, marker, node);
        copy.makeBasicSolverActions();
        return copy.getNonogramLogic().fieldsFilled();
    }

    private int runGuessLoop(NonogramSolutionNode node, int treeDepth, List<NonogramGuessActionsLog> guessLogs) {
        int wrongDecisionsCount = -1;

        do {
            node.getNonogramLogic().updateCurrentAvailableChoices();
            node.getNonogramLogic().clearLogs();
            this.oneOfTwoDecisionsWrong = false;
            Optional<NonogramSolutionDecision> correctDecision = Optional.empty();

            for (NonogramSolutionDecision decision : node.getNonogramLogic().getAvailableChoices()) {
                DecisionOutcome outcome = evaluateDecision(decision, node);

                if (outcome.shouldBreak()) {
                    correctDecision = outcome.correctDecision();
                    node = outcome.updatedNode().orElse(node);
                    this.oneOfTwoDecisionsWrong = outcome.oneOfTwoWrong();
                    wrongDecisionsCount = outcome.wrongCount();
                    break;
                } else {
                    wrongDecisionsCount = 0;
                }
            }

            handlePostDecision(treeDepth, node, correctDecision, guessLogs, wrongDecisionsCount);

        } while (guessModeContinueDecision("oneOfTwoWrong"));

        return wrongDecisionsCount;
    }

    private void handlePostDecision(int treeDepth, NonogramSolutionNode node,
                                    Optional<NonogramSolutionDecision> correctDecision,
                                    List<NonogramGuessActionsLog> guessLogs,
                                    int wrongDecisionsCount) {
        if (treeDepth == 0) {
            correctDecision.ifPresent(dec ->
                    guessLogs.add(new NonogramGuessActionsLog(dec, node.getNonogramLogic().getLogs()))
            );

            if (wrongDecisionsCount == -1) {
                replaceSolutionNodeWithMoreBeneficialSolution(node);
            }

        } else if (node.getNonogramLogic().isSolved()) {
            replaceSolutionNodeWithMoreBeneficialSolution(node);
            logIf(LOG_STEPS_SOLVER, "Nonogram solved, recursion depth: {}", treeDepth);
        }
    }

    private DecisionOutcome evaluateDecision(NonogramSolutionDecision decision, NonogramSolutionNode node) {
        NonogramSolutionNode left = copyNodeWithDecisionAddedAndSolve(decision, COLOURED_FIELD, node);
        NonogramSolutionNode right = copyNodeWithDecisionAddedAndSolve(decision, X_FIELD, node);

        boolean leftValid = !left.getNonogramLogic().getNonogramState().isInvalidSolution();
        boolean rightValid = !right.getNonogramLogic().getNonogramState().isInvalidSolution();

        if (!leftValid && !rightValid) {
            return DecisionOutcome.bothWrong();
        }

        if (leftValid && !rightValid) {
            return DecisionOutcome.oneCorrect(decision.withMarker(COLOURED_FIELD), left, 1);
        }

        if (!leftValid) {
            return DecisionOutcome.oneCorrect(decision.withMarker(X_FIELD), right, 1);
        }

        return DecisionOutcome.bothValid();
    }

    private void printOverallHeuristicsResult(NonogramSolutionNode nonogramSubsolutionNode) {
            log.info("decisions {}", nonogramSubsolutionNode.getNonogramLogic().getActionsToDoList().size());

            log.info("Fields filled after fill trivial rows and columns: {}", nonogramSubsolutionNode.getNonogramLogic().fieldsFilled());
            log.info("COMPLETION PERCENTAGE: {}, DECISIONS SIZE: {}", nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage(), nonogramSubsolutionNode.getNonogramGuessDecisions().size());
            log.info("SOLUTION STEPS: ");
    }

    public NonogramSolutionNode copyNodeWithDecisionAddedAndSolve(NonogramSolutionDecision decision, String decisionMarker, NonogramSolutionNode nodeToCopy) {
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
