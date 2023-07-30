package com.puzzlesolverappbackend.puzzleAppFileManager.controllers;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.Nonogram;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramFiltersResponse;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.runners.InitializerConstants;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Arrays;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/nonogram")
public class NonogramController {

    @Autowired
    CommonService commonService;

    @Autowired
    NonogramService nonogramService;

    @Autowired
    NonogramRepository nonogramRepository;

    Pageable nonogramsPageable;

    @PostMapping("/save")
    public ResponseEntity<String> saveNonogramToJsonFile(@RequestParam String fileName,
                                                         @Valid @RequestBody NonogramFileDetails nonogramFileDetails) {
        System.out.println("saving nonogram: " + fileName);

        String nonogramsPath = InitializerConstants.PUZZLE_RELATIVE_PATH + InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

        Set<String> existingFilesNames = commonService
                .listFilesUsingJavaIO(nonogramsPath);

        String[] fileNamesWithoutExtension = existingFilesNames.toArray(String[]::new);
        List<String> fileNamesWithoutExtensionArray = Arrays.stream(fileNamesWithoutExtension
                .clone())
                .map(fN -> fN.substring(0, fN.length() - 5)) // ".json"
                .collect(Collectors.toList());

        if(fileNamesWithoutExtensionArray.contains(fileName)) {
            return new ResponseEntity<>("Save failed. File with same name already exists.", HttpStatus.OK);
        }

        Gson gson = new Gson();

        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(nonogramsPath + fileName + ".json");
            gson.toJson(nonogramFileDetails, fileWriter);
            fileWriter.close();
            return new ResponseEntity<>("Save success!", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Exception external problem.", HttpStatus.OK);
        }
    }

    @GetMapping("/getNonogramsUsingFilters")
    public ResponseEntity<List<Nonogram>> getNonogramsUsingFilters(@RequestParam(name="page", defaultValue = "0") int page,
                                                                   @RequestParam(name="itemsOnPage", defaultValue = "3") int itemsOnPage,
                                                                   @RequestParam List<String> sources,
                                                                   @RequestParam List<String> years,
                                                                   @RequestParam List<String> months,
                                                                   @RequestParam Double minDifficulty, @RequestParam Double maxDifficulty,
                                                                   @RequestParam Integer minHeight, @RequestParam Integer maxHeight,
                                                                   @RequestParam Integer minWidth, @RequestParam Integer maxWidth) {
        nonogramsPageable = PageRequest.of(page, itemsOnPage);

        Page<Nonogram> nonogramsMatching = nonogramRepository.getNonogramsUsingFilters(sources, years, months, minDifficulty, maxDifficulty,
                minHeight, maxHeight, minWidth, maxWidth, nonogramsPageable);

        return new ResponseEntity<>(nonogramsMatching.getContent(), HttpStatus.OK);
    }

    @GetMapping("/getFilters")
    public ResponseEntity<NonogramFiltersResponse> getNonogramFilters() {
        return new ResponseEntity<>(nonogramService.getNonogramFilters(), HttpStatus.OK );
    }

    @GetMapping("/getNonogramsList")
    public ResponseEntity<List<Nonogram>> getNonogramsList() {
        return new ResponseEntity<>(nonogramRepository.findAll(), HttpStatus.OK);
    }
}

