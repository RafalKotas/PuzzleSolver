package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramGuessActionsLog;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramNodeLog;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.COLOURED_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.X_FIELD;

@Getter
@Setter
@Slf4j
@ToString(onlyExplicitlyIncluded = true)
public class NonogramSolver {

    private final int maxTreeHeight = 50;
    private final GuessMode guessMode = GuessMode.DISABLED;
    private final boolean recursionModeEnabled = false;
    private boolean solved = false;

    private boolean LOG_STEPS_SOLVER = false;
    private boolean printNodeCompletionPercentage = true;

    private boolean oneOfTwoDecisionsWrong;
    private NonogramSolutionNode rootNode;
    private NonogramSolutionNode solutionNode;
    private NonogramLogic finalSolutionLogic;
    @ToString.Include
    private String solutionFileName;
    private NonogramLogic solutionLogic;
    private Gson gson;


    private List<NonogramSolutionNode> nonogramNodes;

    public NonogramSolver(NonogramLogic nonogramLogic, GuessMode guessMode) {
        this.solutionNode = new NonogramSolutionNode(nonogramLogic);
        this.solutionLogic = new NonogramLogic(nonogramLogic.getRowsSequences(), nonogramLogic.getColumnsSequences(), guessMode);
        this.nonogramNodes = new ArrayList<>();
        this.gson = new Gson();
    }

    public NonogramSolver(NonogramLogic nonogramLogic, String fileName) {
        this.solutionNode = new NonogramSolutionNode(nonogramLogic);
        this.rootNode = this.solutionNode;
        this.solutionLogic = new NonogramLogic(nonogramLogic.getRowsSequences(), nonogramLogic.getColumnsSequences(), guessMode);
        this.finalSolutionLogic = new NonogramLogic(nonogramLogic.getRowsSequences(),
                nonogramLogic.getColumnsSequences(), guessMode);
        this.solutionFileName = "r" + fileName;
        this.gson = new Gson();
        this.nonogramNodes = new ArrayList<>();
    }

    public NonogramLogic runSolutionAtNode(NonogramSolutionNode nonogramStartNode) {
        if (LOG_STEPS_SOLVER) {
            solvingAtNodeInitialLogs(nonogramStartNode);
        }
        this.runHeuristicSolver(nonogramStartNode,  0, maxTreeHeight);
        return this.getSolutionNode().getNonogramLogic();
    }

    private void solvingAtNodeInitialLogs(NonogramSolutionNode nonogramStartNode) {
        log.info("RUN SOLVER AT NODE, DECISIONS SIZE {}", nonogramStartNode.getNonogramGuessDecisions().size());
        log.info("ROW SEQUENCES RANGES: \n{}", nonogramStartNode.getNonogramLogic().getRowsSequencesRanges());
        log.info("COLUMN SEQUENCES RANGES: \n{}", nonogramStartNode.getNonogramLogic().getColumnsSequencesRanges());
    }

    public void runHeuristicSolver(NonogramSolutionNode nonogramStartNode,
                                   int currentTreeHeight, int maxTreeHeight) {
        NonogramSolutionNode nonogramSubsolutionNode = gson.fromJson(gson.toJson(nonogramStartNode), NonogramSolutionNode.class);
        NonogramSolutionNode leftNodeO;
        NonogramSolutionNode rightNodeX;

        if (nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() == 100) {
            if (LOG_STEPS_SOLVER) {
                log.info("Solution found, recursion depth: ", currentTreeHeight);
            }
            replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
        }

        Optional<NonogramSolutionDecision> correctDecision;

        //solve without guesses - only heuristics
        nonogramSubsolutionNode.getNonogramLogic().fillTrivialRowsAndColumns();
        nonogramSubsolutionNode.setNodeLogs(nonogramSubsolutionNode.getNonogramLogic().getLogs());
        nonogramSubsolutionNode.makeBasicSolverActions();

        if (LOG_STEPS_SOLVER) {

            nonogramSubsolutionNode.getNonogramLogic().printSolutionBoard();
            nonogramSubsolutionNode.getNonogramLogic().printSolutionBoardWithMarks();
            System.out.println(nonogramSubsolutionNode.getNonogramLogic().getActionsToDoList().size());
            System.out.println("-".repeat(100));

            log.info("Fields filled after fill trivial rows and columns: {}", nonogramSubsolutionNode.getNonogramLogic().fieldsFilled());
            log.info("COMPLETION PERCENTAGE: {}, DECISIONS SIZE: {}", nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage(), nonogramSubsolutionNode.getNonogramGuessDecisions().size());
            log.info("SOLUTION STEPS: ");
            for (String nonogramNodeLog : nonogramSubsolutionNode.getNodeLogs()) {
                log.info("nodeLog: {}", nonogramNodeLog);
            }
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

        if (guessMode == GuessMode.ENABLED && nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() != 100) {
            if (LOG_STEPS_SOLVER) {
                log.info("Completion percentage after heuristics only: {}", nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
            }

            int wrongDecisionsCount;

            do {
                nonogramSubsolutionNode.getNonogramLogic().updateCurrentAvailableChoices();
                nonogramSubsolutionNode.getNonogramLogic().clearLogs();

                correctDecision = Optional.empty();

                // two decisions are 'possibly' ok
                oneOfTwoDecisionsWrong = false;

                wrongDecisionsCount = -1;

                List<NonogramSolutionDecision> availableChoices = nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices();
                //start = System.currentTimeMillis();
                for (NonogramSolutionDecision decision : availableChoices) {
                    leftNodeO = copyNodeAndAddDecision(decision, COLOURED_FIELD, nonogramSubsolutionNode);
                    rightNodeX = copyNodeAndAddDecision(decision, X_FIELD, nonogramSubsolutionNode);

                    if (!leftNodeO.getNonogramLogic().getNonogramState().isInvalidSolution()) {
                        if (rightNodeX.getNonogramLogic().getNonogramState().isInvalidSolution()) {
                            decision.setDecisionMarker(COLOURED_FIELD);
                            correctDecision = Optional.of(decision);
                            nonogramSubsolutionNode = gson.fromJson(gson.toJson(leftNodeO), NonogramSolutionNode.class);
                            if (currentTreeHeight == 0) {
                                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                            }
                            oneOfTwoDecisionsWrong = true;
                            wrongDecisionsCount = 1;
                            break;
                        } else {
                            wrongDecisionsCount = 0;
                        }
                    } else { //leftNode0 solution invalid
                        if (rightNodeX.getNonogramLogic().getNonogramState().isInvalidSolution()) {
                            wrongDecisionsCount = 2;
                        } else {
                            decision.setDecisionMarker(X_FIELD);
                            correctDecision = Optional.of(decision);
                            nonogramSubsolutionNode = gson.fromJson(gson.toJson(rightNodeX), NonogramSolutionNode.class);
                            if (currentTreeHeight == 0) {
                                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                            }
                            oneOfTwoDecisionsWrong = true;
                            wrongDecisionsCount = 1;
                        }
                        break;
                    }
                }

                //only guesses, not recursive - can replace solution with more overall completeness %
                if (currentTreeHeight == 0) {
                    // one of decision wrong -> another is correct, or full solution on currentTreeHeight == 0
                    if (correctDecision.isPresent()) {
                        nonogramGuessActionsLog = new NonogramGuessActionsLog(correctDecision.get(), nonogramSubsolutionNode.getNonogramLogic().getLogs());
                        guessesLogs.add(nonogramGuessActionsLog);
                    }
                    if (wrongDecisionsCount == -1) {
                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                    }
                } else {
                    //fully completion while recursive solving
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
                            log.info("Need to use recursion, completion percentage at start: " + nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
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
                                decisionCoefficientsMax = gson.fromJson(gson.toJson(decision), NonogramSolutionDecision.class);
                                maxNextFilled = Math.max(leftNodeFilled, rightNodeFilled);
                            }
                        }
                        NonogramSolutionNode leftNodeRecursive = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                        decisionCoefficientsMax.setDecisionMarker(COLOURED_FIELD);
                        leftNodeRecursive.addDecision(decisionCoefficientsMax);
                        leftNodeRecursive.colourOrPlaceX();
                        leftNodeRecursive.makeBasicSolverActions();

                        NonogramSolutionNode rightNodeRecursive = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                        decisionCoefficientsMax.setDecisionMarker(X_FIELD);
                        rightNodeRecursive.addDecision(decisionCoefficientsMax);
                        rightNodeRecursive.colourOrPlaceX();
                        rightNodeRecursive.makeBasicSolverActions();

                        runHeuristicSolver(leftNodeRecursive, currentTreeHeight + 1, maxTreeHeight);
                        runHeuristicSolver(rightNodeRecursive, currentTreeHeight + 1, maxTreeHeight);
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
                                decisionCoefficientsMax = gson.fromJson(gson.toJson(decision), NonogramSolutionDecision.class);
                                maxNextFilled = Math.max(leftNodeFilled, rightNodeFilled);
                            }
                        }
                        NonogramSolutionNode leftNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, COLOURED_FIELD, nonogramSubsolutionNode);
                        NonogramSolutionNode rightNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, X_FIELD, nonogramSubsolutionNode);

                        runHeuristicSolver(leftNodeRecursive,currentTreeHeight + 1, maxTreeHeight);
                        runHeuristicSolver(rightNodeRecursive, currentTreeHeight + 1, maxTreeHeight);
                    }
                }
            } else if (wrongDecisionsCount == 2) {
                if (LOG_STEPS_SOLVER) {
                   log.info("Solver ends at node, both decisions wrong (treeHeight: {}, completeness: {}).\n",
                            currentTreeHeight, nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
                }
            }
        } else {
            if (LOG_STEPS_SOLVER) {
                log.info("full solution:");
                log.info(".".repeat(50));
                log.info("currentTreeHeight: {} , completion percentage without guess enabled: {}",
                        currentTreeHeight, nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
            }
        }
    }

    public NonogramSolutionNode copyNodeAndAddDecision(NonogramSolutionDecision decision, String decisionMarker, NonogramSolutionNode nodeToCopy) {
        NonogramSolutionDecision nodeDecision = new NonogramSolutionDecision(decisionMarker, decision.getDecisionField());
        NonogramSolutionNode nodeToAddDecision = gson.fromJson(gson.toJson(nodeToCopy), NonogramSolutionNode.class);
        nodeToAddDecision.addDecision(nodeDecision);
        nodeToAddDecision.colourOrPlaceX();
        nodeToAddDecision.makeBasicSolverActions();
        return gson.fromJson(gson.toJson(nodeToAddDecision), NonogramSolutionNode.class);
    }

    //replace solutionNode with this with higher completion percentage
    public void replaceSolutionNodeWithMoreBeneficialSolution(NonogramSolutionNode nodeToCheck) {

        double oldNodeCompletionPercentage = this.solutionNode.getNonogramLogic().getCompletionPercentage();
        double nodeToCheckCompletionPercentage = nodeToCheck.getNonogramLogic().getCompletionPercentage();

        if (printNodeCompletionPercentage) {
            if (LOG_STEPS_SOLVER) {
                log.info("old Node cp: {}", oldNodeCompletionPercentage);
                log.info("current Node cp: {}", nodeToCheckCompletionPercentage);
            }
        }

        if (nodeToCheckCompletionPercentage > oldNodeCompletionPercentage) {
            if (LOG_STEPS_SOLVER) {
                log.info("Replace solutionLogic with new nonogramLogic, percentage completion: {}", nodeToCheckCompletionPercentage);
                nodeToCheck.getNonogramLogic().printSolutionBoard();
                nodeToCheck.getNonogramLogic().printSolutionBoardAsCode();
            }

            this.solutionNode = gson.fromJson(gson.toJson(nodeToCheck), NonogramSolutionNode.class);
            this.solutionLogic = new NonogramLogic(nodeToCheck.getNonogramLogic());// gson.fromJson(gson.toJson(nodeToCheck.getNonogramLogic()), NonogramLogic.class);
        } else {
            this.solutionNode = gson.fromJson(gson.toJson(this.solutionNode), NonogramSolutionNode.class);
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
