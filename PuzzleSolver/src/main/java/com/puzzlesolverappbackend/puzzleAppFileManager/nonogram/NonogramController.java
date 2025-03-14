package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram;

import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/nonogram")
public class NonogramController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private NonogramService nonogramService;

    @Autowired
    private NonogramRepository nonogramRepository;

    private static final String DEFAULT_PAGE = "0";

    private static final String DEFAULT_ITEMS_ON_PAGE_COUNT = "3";

    @PostMapping("/save")
    public ResponseEntity<String> saveNonogramToJsonFile(@RequestParam String fileName,
                                                         @Valid @RequestBody NonogramFileDetails nonogramFileDetails) {
        log.info("Saving nonogram with name {}", fileName);
        String responeMessage = nonogramService.saveCreatedNonogramToFile(fileName, nonogramFileDetails);
        return new ResponseEntity<>(responeMessage, HttpStatus.OK);
    }

    @GetMapping("/getNonogramsUsingFilters")
    public ResponseEntity<List<Nonogram>> getNonogramsUsingFilters(@RequestParam(name="page", defaultValue = DEFAULT_PAGE) int page,
                                                                   @RequestParam(name="itemsOnPage", defaultValue = DEFAULT_ITEMS_ON_PAGE_COUNT) int itemsOnPage,
                                                                   @RequestParam List<String> sources,
                                                                   @RequestParam List<String> years,
                                                                   @RequestParam List<String> months,
                                                                   @RequestParam Double minDifficulty, @RequestParam Double maxDifficulty,
                                                                   @RequestParam Integer minHeight, @RequestParam Integer maxHeight,
                                                                   @RequestParam Integer minWidth, @RequestParam Integer maxWidth) {
        Pageable nonogramsPageable = PageRequest.of(page, itemsOnPage);

        Page<Nonogram> nonogramsMatching = nonogramRepository.getNonogramsUsingFilters(sources, years, months, minDifficulty, maxDifficulty,
                minHeight, maxHeight, minWidth, maxWidth, nonogramsPageable);

        return new ResponseEntity<>(nonogramsMatching.getContent(), HttpStatus.OK);
    }

    @GetMapping("/getFilters")
    public ResponseEntity<NonogramFiltersResponse> getNonogramFilters() {
        return new ResponseEntity<>(nonogramService.getNonogramFilters(), HttpStatus.OK);
    }

    @GetMapping("/getNonogramsList")
    public ResponseEntity<List<Nonogram>> getNonogramsList() {
        return new ResponseEntity<>(nonogramRepository.findAll(), HttpStatus.OK);
    }
}

