package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.controller;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFiltersResponse;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramFilterRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/nonogram")
public class NonogramController {

    private final NonogramService nonogramService;

    public NonogramController(NonogramService nonogramService,
                              NonogramRepository nonogramRepository) {
        this.nonogramService = nonogramService;
    }

    private static final String DEFAULT_PAGE = "0";

    private static final String DEFAULT_ITEMS_ON_PAGE_COUNT = "3";

    @PostMapping("/save")
    public ResponseEntity<String> saveNonogramToJsonFile(@RequestParam String fileName,
                                                         @Valid @RequestBody NonogramFileDetails nonogramFileDetails) {
        log.info("Saving nonogram with name {}", fileName);
        String responseMessage = nonogramService.saveCreatedNonogramToFile(fileName, nonogramFileDetails);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/getNonogramsUsingFilters")
    public ResponseEntity<List<Nonogram>> getFilteredNonograms(
            @ModelAttribute NonogramFilterRequest filters,
            @RequestParam(name="page", defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name="itemsOnPage", defaultValue = DEFAULT_ITEMS_ON_PAGE_COUNT) int itemsOnPage
    ) {
        Pageable pageable = PageRequest.of(page, itemsOnPage);
        Page<Nonogram> resultPage = nonogramService.getNonogramsFiltered(filters, pageable);
        return ResponseEntity.ok(resultPage.getContent());
    }


    @GetMapping("/getFilters")
    public ResponseEntity<NonogramFiltersResponse> getNonogramFilters() {
        return new ResponseEntity<>(nonogramService.getNonogramFilters(), HttpStatus.OK);
    }

    @GetMapping("/getNonogramsList")
    public ResponseEntity<List<Nonogram>> getNonogramsList() {
        return new ResponseEntity<>(nonogramService.getNonogramsList(), HttpStatus.OK);
    }
}

