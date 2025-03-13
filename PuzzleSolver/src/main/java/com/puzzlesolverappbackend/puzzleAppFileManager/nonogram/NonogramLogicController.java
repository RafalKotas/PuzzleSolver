package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.GuessMode;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.helpers.FileHelper.generateSavePathForFilename;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/nonogram/logic")
public class NonogramLogicController {

    final
    NonogramLogicService nonogramLogicService;

    public NonogramLogicController(NonogramLogicService nonogramLogicService) {
        this.nonogramLogicService = nonogramLogicService;
    }

    @PostMapping("/init")
    public ResponseEntity<NonogramLogic> initNonogramLogicObject(@Valid @RequestBody NonogramLogic nonogramLogic) {
        NonogramLogic initialNonogramLogicObject = new NonogramLogic(nonogramLogic.getRowsSequences(), nonogramLogic.getColumnsSequences(), GuessMode.DISABLED);

        return new ResponseEntity<>(initialNonogramLogicObject, HttpStatus.OK);
    }

    @PostMapping("/fillOverlappingColumnSequences/{columnID}")
    public ResponseEntity<NonogramLogic> fillOverlappingColumnSequences(@Valid @RequestBody NonogramLogic nonogramLogic, @PathVariable("columnID") int columnID) {
        NonogramLogic nonogramWithColumnOverlappingFilled = nonogramLogicService.fillOverlappingFieldsInColumn(nonogramLogic, columnID);
        return new ResponseEntity<>(nonogramWithColumnOverlappingFilled, HttpStatus.OK);
    }

    @PostMapping("/fillOverlappingColumnsSequences/{columnBegin}/{columnEnd}")
    public ResponseEntity<NonogramLogic> fillOverlappingColumnsSequencesRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                                              @PathVariable("columnBegin") int columnBegin,
                                                                              @PathVariable("columnEnd") int columnEnd) {
        NonogramLogic solutionPart = nonogramLogicService.fillOverLappingFieldsInColumnsRange(nonogramLogic, columnBegin, columnEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @PostMapping("/fillOverlappingRowsSequences/{rowBegin}/{rowEnd}")
    public ResponseEntity<NonogramLogic> fillOverlappingRowsSequencesRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                                              @PathVariable("rowBegin") int rowBegin,
                                                                              @PathVariable("rowEnd") int rowEnd) {
        NonogramLogic solutionPart = nonogramLogicService.fillOverLappingFieldsInRowsRange(nonogramLogic, rowBegin, rowEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @PostMapping("/markRowsSequences/{rowBegin}/{rowEnd}")
    public ResponseEntity<NonogramLogic> markRowSequencesRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                                           @PathVariable("rowBegin") int rowBegin,
                                                                           @PathVariable("rowEnd") int rowEnd) {
        NonogramLogic solutionPart = nonogramLogicService.markAvailableSequencesInRows(nonogramLogic, rowBegin, rowEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @PostMapping("/markColumnsSequences/{columnBegin}/{columnEnd}")
    public ResponseEntity<NonogramLogic> markColumnsSequencesRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                               @PathVariable("columnBegin") int columnBegin,
                                                               @PathVariable("columnEnd") int columnEnd) {
        NonogramLogic solutionPart = nonogramLogicService.markAvailableSequencesInColumns(nonogramLogic, columnBegin, columnEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @PostMapping("/placeXinRowsRange/{rowBegin}/{rowEnd}")
    public ResponseEntity<NonogramLogic> placeXinRowsRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                           @PathVariable("rowBegin") int rowBegin,
                                                           @PathVariable("rowEnd") int rowEnd) {
        NonogramLogic solutionPart = nonogramLogicService.placeXsAroundLongestSequencesInRowsRange(nonogramLogic,
                rowBegin,
                rowEnd);
        solutionPart = nonogramLogicService.placeXsAtUnreachableFieldsInRowsRange(solutionPart, rowBegin, rowEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @PostMapping("/placeXinColumnsRange/{columnBegin}/{columnEnd}")
    public ResponseEntity<NonogramLogic> placeXinColumnsRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                           @PathVariable("columnBegin") int columnBegin,
                                                           @PathVariable("columnEnd") int columnEnd) {
        NonogramLogic solutionPart = nonogramLogicService.placeXsAroundLongestSequencesInColumnsRange(nonogramLogic,
                columnBegin,
                columnEnd);
        solutionPart = nonogramLogicService.placeXsAtUnreachableFieldsInColumnsRange(solutionPart, columnBegin, columnEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @PostMapping("/customSolutionPart")
    public ResponseEntity<NonogramLogic> customSolutionPart(@Valid @RequestBody NonogramLogic nonogramLogic, @RequestParam String solutionFileName) {
        log.info("Custom solving endpoint triggered (heuristics)!");
        NonogramLogic logicWithAffectedRowsAndColumns = new NonogramLogic(nonogramLogic.getRowsSequences(), nonogramLogic.getColumnsSequences(), GuessMode.DISABLED);

        NonogramLogic customSolution = nonogramLogicService.runSolverWithCorrectnessCheck(logicWithAffectedRowsAndColumns, solutionFileName);

        return new ResponseEntity<>(
                customSolution,
                HttpStatus.OK);
    }

    @PostMapping("/saveSolution")
    public ResponseEntity<String> saveSolution(@Valid @RequestBody NonogramLogic nonogramLogic, @RequestParam String fileName) {
        List<List<Integer>> rowsSequences = nonogramLogic.getRowsSequences();
        List<List<Integer>> columnsSequences = nonogramLogic.getColumnsSequences();
        List<List<String>> nonogramSolutionBoard = nonogramLogic.getNonogramSolutionBoard();

        NonogramLogic solution = new NonogramLogic(rowsSequences, columnsSequences, nonogramSolutionBoard);

        Gson gson = new Gson();

        FileWriter nonogramSolutionWriter;
        try {
            nonogramSolutionWriter = new FileWriter(generateSavePathForFilename(fileName));
            gson.toJson(solution, nonogramSolutionWriter);
            nonogramSolutionWriter.close();
            return new ResponseEntity<>("Save success!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Exception external problem.", HttpStatus.OK);
        }
    }

    @PostMapping("/compareWithSolution")
    public ResponseEntity<NonogramLogic> compareWithSolution(@Valid @RequestBody NonogramLogic nonogramLogic, @RequestParam String fileName) {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(generateSavePathForFilename(fileName))) {

            // Convert JSON File to Java Object
            NonogramLogic solution = gson.fromJson(reader, NonogramLogic.class);

            return new ResponseEntity<>(solution, HttpStatus.OK);

        } catch (IOException e) {
            System.out.println("Exception...");
            return new ResponseEntity<>(nonogramLogic, HttpStatus.OK);
        }
    }

    @PostMapping("/correctRanges")
    public ResponseEntity<NonogramLogic> correctRangesSequences(@Valid @RequestBody NonogramLogic nonogramLogic) {

        NonogramLogic logicObjectModified = nonogramLogicService.correctRowsSequencesRanges(nonogramLogic, 0, nonogramLogic.getHeight());
        logicObjectModified = nonogramLogicService.correctColumnsSequencesRanges(logicObjectModified, 0, nonogramLogic.getWidth());

        return new ResponseEntity<>(
                logicObjectModified,
                HttpStatus.OK);
    }

    @PostMapping("/correctColumnsRanges/{columnBegin}/{columnEnd}")
    public ResponseEntity<NonogramLogic> correctColumnsRangesSequences(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                                @PathVariable("columnBegin") int columnBegin,
                                                                @PathVariable("columnEnd") int columnEnd) {

        NonogramLogic logicObjectModified = nonogramLogicService.correctColumnsSequencesRanges(nonogramLogic, columnBegin, columnEnd);

        return new ResponseEntity<>(
                logicObjectModified,
                HttpStatus.OK);
    }

    @PostMapping("/correctRowsRanges/{rowBegin}/{rowEnd}")
    public ResponseEntity<NonogramLogic> correctRowsRangesSequences(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                                       @PathVariable("rowBegin") int rowBegin,
                                                                       @PathVariable("rowEnd") int rowEnd) {

        NonogramLogic logicObjectModified = nonogramLogicService.correctRowsSequencesRanges(nonogramLogic, rowBegin, rowEnd);

        return new ResponseEntity<>(
                logicObjectModified,
                HttpStatus.OK);
    }
}
