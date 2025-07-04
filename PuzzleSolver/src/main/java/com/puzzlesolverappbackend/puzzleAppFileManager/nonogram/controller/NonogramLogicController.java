package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.controller;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.logic.NonogramLogicFactory;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.dto.*;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.mapper.NonogramMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.service.NonogramLogicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static com.puzzlesolverappbackend.puzzleAppFileManager.common.FileHelper.nonogramSolutionSavePathForFilename;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/nonogram/logic")
public class NonogramLogicController {

    private final NonogramLogicFactory nonogramLogicFactory;

    private final NonogramLogicService nonogramLogicService;

    @SuppressWarnings("unused")
    @PostMapping("/initializeNonogram")
    public ResponseEntity<NonogramLogic> initializeNonogram(@RequestBody NonogramInitializationRequest request) {
        NonogramLogic logic = nonogramLogicService.initializeLogicFromRequest(request);
        return ResponseEntity.ok(logic);
    }

    @SuppressWarnings("unused")
    @PostMapping("/fillOverlappingColumnSequences/{columnID}")
    public ResponseEntity<NonogramLogic> fillOverlappingColumnSequences(@Valid @RequestBody NonogramLogic nonogramLogic, @PathVariable("columnID") int columnID) {
        NonogramLogic nonogramWithColumnOverlappingFilled = nonogramLogicService.fillOverlappingFieldsInColumn(nonogramLogic, columnID);
        return new ResponseEntity<>(nonogramWithColumnOverlappingFilled, HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    @PostMapping("/fillOverlappingColumnsSequences/{columnBegin}/{columnEnd}")
    public ResponseEntity<NonogramLogic> fillOverlappingColumnsSequencesRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                                              @PathVariable("columnBegin") int columnBegin,
                                                                              @PathVariable("columnEnd") int columnEnd) {
        NonogramLogic solutionPart = nonogramLogicService.fillOverLappingFieldsInColumnsRange(nonogramLogic, columnBegin, columnEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    @PostMapping("/fillOverlappingRowsSequences/{rowBegin}/{rowEnd}")
    public ResponseEntity<NonogramLogic> fillOverlappingRowsSequencesRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                                              @PathVariable("rowBegin") int rowBegin,
                                                                              @PathVariable("rowEnd") int rowEnd) {
        NonogramLogic solutionPart = nonogramLogicService.fillOverLappingFieldsInRowsRange(nonogramLogic, rowBegin, rowEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    @PostMapping("/markRowsSequences/{rowBegin}/{rowEnd}")
    public ResponseEntity<NonogramLogic> markRowSequencesRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                                           @PathVariable("rowBegin") int rowBegin,
                                                                           @PathVariable("rowEnd") int rowEnd) {
        NonogramLogic solutionPart = nonogramLogicService.markAvailableSequencesInRows(nonogramLogic, rowBegin, rowEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    @PostMapping("/markColumnsSequences/{columnBegin}/{columnEnd}")
    public ResponseEntity<NonogramLogic> markColumnsSequencesRange(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                               @PathVariable("columnBegin") int columnBegin,
                                                               @PathVariable("columnEnd") int columnEnd) {
        NonogramLogic solutionPart = nonogramLogicService.markAvailableSequencesInColumns(nonogramLogic, columnBegin, columnEnd);
        return new ResponseEntity<>(solutionPart, HttpStatus.OK);
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    @PostMapping("/customSolutionPart")
    public ResponseEntity<NonogramLogicResponse> customSolutionPart(
            @RequestBody NonogramSolvePayload payload,
            @RequestParam String fileName) {

        log.info("Custom solving endpoint triggered (heuristics)");

        NonogramLogic nonogramLogic = nonogramLogicFactory.createFromPayload(payload);
        NonogramLogic solved = nonogramLogicService.runSolverWithCorrectnessCheck(nonogramLogic, fileName);

        NonogramLogicResponse nonogramLogicResponse = NonogramMapper.toResponse(solved);

        return ResponseEntity.ok(nonogramLogicResponse);
    }

    @SuppressWarnings("unused")
    @PostMapping("/saveIfCorrect")
    public ResponseEntity<FinalNonogramSolutionDTO> saveSolution(@RequestBody NonogramSolutionSaveRequest request) {
        try {
            FinalNonogramSolutionDTO result = nonogramLogicService.saveIfCorrect(request);
            if ("PASS".equals(result.getVerifiedAgainstOriginal())) {
                log.info("Correct nonogram solution saved: {}", request.getFileName());
                return ResponseEntity.ok(result);
            } else {
                log.warn("Incorrect nonogram solution for saving: {}", request.getFileName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (IOException e) {
            log.error("Error saving solution for file: {}", request.getFileName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @SuppressWarnings("unused")
    @PostMapping("/compareWithSolution")
    public ResponseEntity<NonogramLogic> compareWithSolution(@Valid @RequestBody NonogramLogic nonogramLogic, @RequestParam String fileName) {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(nonogramSolutionSavePathForFilename(fileName))) {

            // Convert JSON File to Java Object
            NonogramLogic solution = gson.fromJson(reader, NonogramLogic.class);

            return new ResponseEntity<>(solution, HttpStatus.OK);

        } catch (IOException e) {
            System.out.println("Exception...");
            return new ResponseEntity<>(nonogramLogic, HttpStatus.OK);
        }
    }

    @SuppressWarnings("unused")
    @PostMapping("/correctRanges")
    public ResponseEntity<NonogramLogic> correctRangesSequences(@Valid @RequestBody NonogramLogic nonogramLogic) {

        NonogramLogic logicObjectModified = nonogramLogicService.correctRowsSequencesRanges(nonogramLogic, 0, nonogramLogic.getNonogramRules().getHeight());
        logicObjectModified = nonogramLogicService.correctColumnsSequencesRanges(logicObjectModified, 0, nonogramLogic.getNonogramRules().getWidth());

        return new ResponseEntity<>(
                logicObjectModified,
                HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    @PostMapping("/correctColumnsRanges/{columnBegin}/{columnEnd}")
    public ResponseEntity<NonogramLogic> correctColumnsRangesSequences(@Valid @RequestBody NonogramLogic nonogramLogic,
                                                                @PathVariable("columnBegin") int columnBegin,
                                                                @PathVariable("columnEnd") int columnEnd) {

        NonogramLogic logicObjectModified = nonogramLogicService.correctColumnsSequencesRanges(nonogramLogic, columnBegin, columnEnd);

        return new ResponseEntity<>(
                logicObjectModified,
                HttpStatus.OK);
    }

    @SuppressWarnings("unused")
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
