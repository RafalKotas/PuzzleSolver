package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramService;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramCorrectnessIndicator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Slf4j
class NonogramSolverTest {

    private final String projectRootPath = System.getProperty("user.dir");

    @Autowired
    NonogramRepository nonogramRepository;

    @Autowired
    private NonogramService nonogramService;

    private final GuessMode guessMode = GuessMode.DISABLED;

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 2.0, /*3.0*/})
    @DisplayName("Should solve logi nonograms heuristically by difficulty")
    void shouldSolveLogiNonogramsByDifficultyHeuristicsOnly(double difficulty) {
        // given
        List<String> logiNonogramsNamesDifficulty1 = nonogramRepository.findLogiNonogramsNamesByDifficultySortedByArea(difficulty);
        assertFalse(logiNonogramsNamesDifficulty1.isEmpty(), "Not found any nonograms which met condition");

        NonogramFileDetails currentNonogramDetails;
        NonogramCorrectnessIndicator correctnessIndicator;
        NonogramLogic currentNonogramLogic;
        NonogramLogic nonogramSolutionLogic;
        NonogramSolutionNode nonogramSolutionNode;
        NonogramSolver nonogramSolver;

        List<String> notSolvedNonograms = new ArrayList<>();

        int orderId = 1;

        // when
        for (String filename : logiNonogramsNamesDifficulty1) {
            Path filePath = Paths.get(projectRootPath, "../FrontReact", "public", "resources", "Nonograms", filename + JSON_EXTENSION);
            currentNonogramDetails = nonogramService.getNonogramDetailsFromFile(filePath.toString());
            correctnessIndicator = nonogramService.checkNonogramCorrectness(currentNonogramDetails);

            if (correctnessIndicator == NonogramCorrectnessIndicator.VALID) {
                currentNonogramLogic = new NonogramLogic(
                        currentNonogramDetails.getRowSequences(), currentNonogramDetails.getColumnSequences(), guessMode);
                nonogramSolutionNode = new NonogramSolutionNode(currentNonogramLogic);
                nonogramSolver = new NonogramSolver(currentNonogramLogic, guessMode);
                nonogramSolutionLogic = nonogramSolver.runSolutionAtNode(nonogramSolutionNode);

                if (!nonogramSolutionLogic.isSolved()) {
                    System.out.println(orderId + " " + filename + " " + difficulty + " niepełne");
                    notSolvedNonograms.add(filename);
                } else {
                    System.out.println(orderId + " " + filename + " " + difficulty + " TAK");
                }
            }
            // TODO - write test/s for checking invalid nonograms existing
            /*else {
                log.error("Nonogram {} data not correct ({})", filename, correctnessIndicator);
                invalidNonograms++;
            }*/
            orderId++;
        }

        System.out.println("--------------------------------------------------------------------------------------------");


        // then
        assertThat(notSolvedNonograms).isEmpty();
    }
}