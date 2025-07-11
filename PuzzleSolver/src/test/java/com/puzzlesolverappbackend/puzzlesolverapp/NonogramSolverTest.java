package com.puzzlesolverappbackend.puzzlesolverapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicFactory;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramSolver;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionNode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramCorrectnessIndicator;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class NonogramSolverTest {

    private final String projectRootPath = System.getProperty("user.dir");

    @Autowired
    NonogramRepository nonogramRepository;

    @Autowired
    private NonogramService nonogramService;

    private final GuessMode guessMode = GuessMode.DISABLED;

    @Disabled("Test temporarily skipped due to long runtime")
    @Test
    @DisplayName("Should solve logi nonograms heuristically by difficulty")
    void shouldSolveAllLogiNonogramsHeuristically() {
        Map<Double, List<String>> notSolvedByDifficulty = new HashMap<>();

        for (double difficulty : List.of(1.0, 2.0/*, 3.0*/)) {
            solveNonogramsAtDifficulty(difficulty, notSolvedByDifficulty);
        }

        ensureAllDifficultyLevelsPresent(notSolvedByDifficulty);
        printRegressionOrProgress(notSolvedByDifficulty);

        assertThat(notSolvedByDifficulty.values().stream().flatMap(List::stream).toList())
                .isEmpty();
    }

    private void solveNonogramsAtDifficulty(double difficulty, Map<Double, List<String>> notSolvedMap) {
        List<String> filenames = nonogramRepository.findLogiNonogramsNamesByDifficultySortedByArea(difficulty);

        for (String filename : filenames) {
            Path filePath = Paths.get(projectRootPath, "../FrontReact", "public", "resources", "Nonograms", filename + ".json");
            NonogramFileDetails details = nonogramService.getNonogramDetailsFromFile(filePath.toString());
            NonogramCorrectnessIndicator indicator = nonogramService.checkNonogramCorrectness(details);

            if (indicator == NonogramCorrectnessIndicator.VALID) {
                NonogramRules rules = NonogramRules.mapNonogramFileDetailsToNonogramRules(details);
                NonogramLogic logic = new NonogramLogic(rules, guessMode);
                NonogramLogicFactory logicFactory = new NonogramLogicFactory();
                NonogramSolver solver = new NonogramSolver(logic, filename, guessMode, logicFactory);
                NonogramSolutionNode node = new NonogramSolutionNode(logic, logicFactory);
                NonogramLogic result = solver.runSolutionAtNode(node);

                if (!result.nonogramIsFullyAndCorrectSolved()) {
                    notSolvedMap.computeIfAbsent(difficulty, d -> new ArrayList<>()).add(filename);
                    log.warn("{} - NOT SOLVED (difficulty {})", filename, difficulty);
                } else {
                    log.info("{} - SOLVED! (difficulty {})", filename, difficulty);
                }
            }
        }
    }

    private void printRegressionOrProgress(Map<Double, List<String>> currentNotSolved) {
        String recordPath = "src/test/resources/notSolvedBefore.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Map<Double, List<String>> previous;
        try (Reader reader = new FileReader(recordPath)) {
            Type type = new TypeToken<Map<Double, List<String>>>() {}.getType();
            previous = gson.fromJson(reader, type);
        } catch (IOException e) {
            log.info("No previous record found, treating as first run.");
            previous = new HashMap<>();
        }

        log.info("=== REGRESSION CHECK ===");
        for (Map.Entry<Double, List<String>> entry : currentNotSolved.entrySet()) {
            Double difficulty = entry.getKey();
            List<String> currentList = entry.getValue();
            List<String> previousList = previous.getOrDefault(difficulty, Collections.emptyList());

            Set<String> newlyBroken = new HashSet<>(currentList);
            previousList.forEach(newlyBroken::remove);

            if (!newlyBroken.isEmpty()) {
                log.warn("Difficulty {} - newly unsolved: {}", difficulty, newlyBroken);
            }
        }

        ensureAllDifficultyLevelsPresent(currentNotSolved);

        try (Writer writer = new FileWriter(recordPath)) {
            gson.toJson(currentNotSolved, writer);
        } catch (IOException e) {
            log.error("Failed to save current regression state: {}", e.getMessage());
        }
    }

    private void ensureAllDifficultyLevelsPresent(Map<Double, List<String>> notSolvedMap) {
        for (double difficulty : List.of(1.0, 2.0, 3.0)) {
            notSolvedMap.putIfAbsent(difficulty, new ArrayList<>());
        }
    }
}