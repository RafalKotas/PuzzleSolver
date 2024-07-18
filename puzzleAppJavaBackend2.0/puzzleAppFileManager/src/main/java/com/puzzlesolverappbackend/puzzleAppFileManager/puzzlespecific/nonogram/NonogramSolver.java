package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.logs.nonogram.NonogramGuessActionsLog;
import com.puzzlesolverappbackend.puzzleAppFileManager.logs.nonogram.NonogramNodeLog;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Slf4j
public class NonogramSolver {
    @Override
    public String toString() {
        return "NonogramSolver{" +
                "solutionFileName='" + solutionFileName +
                '}';
    }

    final int maxTreeHeight = 50;
    long start;
    long end;
    long timeElapsed;

    final int secondTimeLimitForNode = 100;
    final int secondTimeLimitTrialAndError = 100;

    boolean printNodeCompletionPercentage = true;

    NonogramLogicService nonogramLogicService = new NonogramLogicService();

    private boolean solved = false;
    private boolean oneOfTwoWrong;
    private NonogramSolutionNode rootNode;
    private NonogramSolutionNode solutionNode;
    private NonogramLogic finalSolutionLogic;
    private String solutionFileName;
    private NonogramLogic solutionLogic;
    private Gson gson;
    private final boolean guessModeEnabled = false;
    private final boolean recursionModeEnabled = false;

    public NonogramSolver(NonogramLogic nonogramLogic, String fileName) {
        this.gson = new Gson();
        this.solutionNode = new NonogramSolutionNode(nonogramLogic);
        this.rootNode = this.solutionNode;
        this.solutionLogic = new NonogramLogic(nonogramLogic.getRowsSequences(), nonogramLogic.getColumnsSequences());
        this.finalSolutionLogic = new NonogramLogic(nonogramLogic.getRowsSequences(),
                nonogramLogic.getColumnsSequences());
        this.solutionFileName = "r" + fileName;
    }

    public NonogramLogic runSolutionAtNode(NonogramSolutionNode nonogramStartNode) {
        log.info("RUN SOLVER AT NODE, DECISIONS SIZE {}", nonogramStartNode.getNonogramGuessDecisions().size());
        this.runHeuristicSolver(nonogramStartNode,  0, maxTreeHeight);
        return this.getSolutionNode().getNonogramLogic();
    }

    public void runHeuristicSolver(NonogramSolutionNode nonogramStartNode,
                                   int currentTreeHeight, int maxTreeHeight) {
        NonogramSolutionNode nonogramSubsolutionNode = gson.fromJson(gson.toJson(nonogramStartNode), NonogramSolutionNode.class);
        NonogramSolutionNode leftNodeO;
        NonogramSolutionNode rightNodeX;

        if(nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() == 100) {
            System.out.println("Solution found, recursion depth: " + currentTreeHeight);
            replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
        }

        Optional<NonogramSolutionDecision> correctDecision;

        //solve without guesses - only heuristics
        nonogramSubsolutionNode.getNonogramLogic().printRowsSequencesRanges();
        nonogramSubsolutionNode.getNonogramLogic().printColumnsSequencesRanges();
        nonogramSubsolutionNode.getNonogramLogic().fillTrivialRowsAndColumns();
        nonogramSubsolutionNode.setNodeLogs(nonogramSubsolutionNode.getNonogramLogic().getLogs());
        nonogramSubsolutionNode.makeBasicSolverActions();
        log.info("COMPLETION PERCENTAGE: {}, DECISIONS SIZE: {}", nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage(), nonogramSubsolutionNode.getNonogramGuessDecisions().size());
        log.info("SOLUTION STEPS: ");
        for(String nonogramNodeLog : nonogramSubsolutionNode.getNodeLogs()) {
            System.out.println(nonogramNodeLog);
        }

        // heuristic logs
        NonogramNodeLog nonogramNodeLog = new NonogramNodeLog(nonogramSubsolutionNode.getNonogramLogic().getLogs());
        // guess logs
        List<NonogramGuessActionsLog> guessesLogs = new ArrayList<>();
        // guess log
        NonogramGuessActionsLog nonogramGuessActionsLog;

        if(currentTreeHeight == 0) {
            replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
        }

        if(guessModeEnabled && nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() != 100) {
            System.out.println("Guess mode needed!");

            int wrongCount;

            NonogramSolutionDecision nodeDecision;

            do {
                nonogramSubsolutionNode.getNonogramLogic().updateCurrentAvailableChoices();
                nonogramSubsolutionNode.getNonogramLogic().clearLogs();

                correctDecision = Optional.empty();

                // two decisions are 'possibly' ok
                oneOfTwoWrong = false;

                wrongCount = -1;

                List<NonogramSolutionDecision> availableChoices = nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices();
                start = System.currentTimeMillis();
                for(NonogramSolutionDecision decision : availableChoices) {

                    gson = new Gson();
                    nodeDecision = new NonogramSolutionDecision("O", decision.getRowIdx(), decision.getColumnIdx());
                    leftNodeO = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                    leftNodeO.addDecision(nodeDecision);
                    leftNodeO.colourOrPlaceX();
                    leftNodeO.makeBasicSolverActions();

                    nodeDecision = new NonogramSolutionDecision("X", decision.getRowIdx(), decision.getColumnIdx());
                    rightNodeX = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                    rightNodeX.addDecision(nodeDecision);
                    rightNodeX.colourOrPlaceX();
                    rightNodeX.makeBasicSolverActions();

                    if(!leftNodeO.getNonogramLogic().getNonogramState().isInvalidSolution()) {
                        if(rightNodeX.getNonogramLogic().getNonogramState().isInvalidSolution()) {
                            decision.setDecisionMarker("O");
                            correctDecision = Optional.of(decision);
                            nonogramSubsolutionNode = gson.fromJson(gson.toJson(leftNodeO), NonogramSolutionNode.class);
                            if(currentTreeHeight == 0) {
                                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                            }
                            oneOfTwoWrong = true;
                            wrongCount = 1;
                            break;
                        } else {
                            wrongCount = 0;
                        }
                    } else { //leftNode0 solution invalid
                        if(rightNodeX.getNonogramLogic().getNonogramState().isInvalidSolution()) {
                            wrongCount = 2;
                        } else {
                            decision.setDecisionMarker("X");
                            correctDecision = Optional.of(decision);
                            nonogramSubsolutionNode = gson.fromJson(gson.toJson(rightNodeX), NonogramSolutionNode.class);
                            if(currentTreeHeight == 0) {
                                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                            }
                            oneOfTwoWrong = true;
                            wrongCount = 1;
                        }
                        break;
                    }
                }

                //only guesses, not recursive - can replace solution with more overall completeness %
                if(currentTreeHeight == 0) {
                    // one of decision wrong -> another is correct, or full solution on currentTreeHeight == 0
                    if(correctDecision.isPresent()) {
                        nonogramGuessActionsLog = new NonogramGuessActionsLog(correctDecision.get(), nonogramSubsolutionNode.getNonogramLogic().getLogs());
                        guessesLogs.add(nonogramGuessActionsLog);
                    }
                    if(wrongCount == -1) {
                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                    }
                } else {
                    //fully completion while recursive solving
                    if(nonogramSubsolutionNode.getNonogramLogic().fieldsFilled() == nonogramSubsolutionNode.getNonogramLogic().area()) {
                        //System.out.printf("Recursive solved on tree height: %d\n", currentTreeHeight);
                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                        System.out.println("Completed, recursion depth: " + currentTreeHeight);
                    }
                }
            } while (
                    guessModeContinueDecision("oneOfTwoWrong")
            );

            if(currentTreeHeight == 0) {
                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
            }

            if(wrongCount == 0) {
                if(currentTreeHeight == 0) {
                    nonogramNodeLog.setGuessesLogs(guessesLogs);
                    replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);

                    if(nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() != 100.00) {
                        System.out.println("Need to use recursion, completion percentage at start: " + nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());

                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);

                        NonogramSolutionDecision decisionCoefficientsMax = new NonogramSolutionDecision();
                        int maxNextFilled = 0;
                        int leftNodeFilled;
                        int rightNodeFilled;
                        for(NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {
                            leftNodeO = copyNodeAndAddDecision(decision, "O", nonogramSubsolutionNode);
                            leftNodeO.makeBasicSolverActions();
                            leftNodeFilled = leftNodeO.getNonogramLogic().fieldsFilled();

                            rightNodeX = copyNodeAndAddDecision(decision, "X", nonogramSubsolutionNode);
                            rightNodeX.makeBasicSolverActions();
                            rightNodeFilled = rightNodeX.getNonogramLogic().fieldsFilled();

                            if(maxNextFilled < Math.max(leftNodeFilled, rightNodeFilled)) {
                                decisionCoefficientsMax = gson.fromJson(gson.toJson(decision), NonogramSolutionDecision.class);
                                maxNextFilled = Math.max(leftNodeFilled, rightNodeFilled);
                            }
                        }
                        NonogramSolutionNode leftNodeRecursive = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                        decisionCoefficientsMax.setDecisionMarker("O");
                        leftNodeRecursive.addDecision(decisionCoefficientsMax);
                        leftNodeRecursive.colourOrPlaceX();
                        leftNodeRecursive.makeBasicSolverActions();

                        NonogramSolutionNode rightNodeRecursive = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                        decisionCoefficientsMax.setDecisionMarker("X");
                        rightNodeRecursive.addDecision(decisionCoefficientsMax);
                        rightNodeRecursive.colourOrPlaceX();
                        rightNodeRecursive.makeBasicSolverActions();

                        runHeuristicSolver(leftNodeRecursive, currentTreeHeight + 1, maxTreeHeight);
                        runHeuristicSolver(rightNodeRecursive, currentTreeHeight + 1, maxTreeHeight);
                    } else {
                        System.out.println("Don't need to use recursion. Nonogram solved at ");
                    }
                } else if(recursionModeEnabled && currentTreeHeight <= maxTreeHeight) {
                    if(nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() == 100) {
                        System.out.printf("Solution found recursion (tree height: %d)!!!\n", currentTreeHeight);
                        replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                    } else {
                        NonogramSolutionDecision decisionCoefficientsMax = new NonogramSolutionDecision();
                        int maxNextFilled = 0;
                        int leftNodeFilled;
                        int rightNodeFilled;

                        start = System.currentTimeMillis();
                        for(NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {
                            leftNodeO = copyNodeAndAddDecision(decision, "O", nonogramSubsolutionNode);
                            leftNodeO.makeBasicSolverActions();
                            leftNodeFilled = leftNodeO.getNonogramLogic().fieldsFilled();

                            rightNodeX = copyNodeAndAddDecision(decision, "X", nonogramSubsolutionNode);
                            rightNodeX.makeBasicSolverActions();
                            rightNodeFilled = rightNodeX.getNonogramLogic().fieldsFilled();

                            if(maxNextFilled < Math.max(leftNodeFilled, rightNodeFilled)) {
                                decisionCoefficientsMax = gson.fromJson(gson.toJson(decision), NonogramSolutionDecision.class);
                                maxNextFilled = Math.max(leftNodeFilled, rightNodeFilled);
                            }
                            end = System.currentTimeMillis();
                            timeElapsed = end - start;
                            if((double) timeElapsed / 1000.0 > secondTimeLimitForNode) {
                                break;
                            }
                        }
                        NonogramSolutionNode leftNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, "O", nonogramSubsolutionNode);
                        NonogramSolutionNode rightNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, "X", nonogramSubsolutionNode);

                        runHeuristicSolver(leftNodeRecursive,currentTreeHeight + 1, maxTreeHeight);
                        runHeuristicSolver(rightNodeRecursive, currentTreeHeight + 1, maxTreeHeight);
                    }
                }
            } else if(wrongCount == 2) {
                System.out.printf("Solver ends at node, both decisions wrong (treeHeight: %d, completeness: %,.2f).\n",
                        currentTreeHeight, nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
            }
        } else {
            System.out.println("full solution:");
            System.out.println(".".repeat(50));
            System.out.println("currentTreeHeight: " + currentTreeHeight + " , completion percentage without guess enabled: " + nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
        }
    }

    public NonogramSolutionNode copyNodeAndAddDecision(NonogramSolutionDecision decision, String decisionMarker, NonogramSolutionNode nodeToCopy) {
        NonogramSolutionDecision nodeDecision = new NonogramSolutionDecision(decisionMarker, decision.getRowIdx(), decision.getColumnIdx());
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

        if(printNodeCompletionPercentage) {
            System.out.println("old Node cp: " + oldNodeCompletionPercentage);
            System.out.println("current Node cp: "+ nodeToCheckCompletionPercentage);
        }

        if(nodeToCheckCompletionPercentage > oldNodeCompletionPercentage) {
            System.out.println("Replace solutionLogic with new nonogramLogic, percentage completion: " + nodeToCheckCompletionPercentage);
            nodeToCheck.getNonogramLogic().printSolutionBoard();
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
            default -> this.oneOfTwoWrong;
        };
    }
}
