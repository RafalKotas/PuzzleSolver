package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConstants.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConstants.JSON_EXTENSION_LENGTH;

@RestController
@RequestMapping("/api/sudoku")
@Slf4j
public class SudokuController {

    @Autowired
    CommonService commonService;

    @PostMapping("/save")
    public ResponseEntity<String> saveSudokuToJsonFile(@RequestParam String fileName, @Valid @RequestBody SudokuFileDetails nfd) throws IOException {
        log.info("Saving sudoku with filename: {}", fileName);

        Set<String> existingFilesNames = commonService
                .listFilesUsingJavaIO("../../puzzle-solver-app/public/resources/Sudoku/");

        String[] fileNamesWithoutExtension = existingFilesNames.toArray(String[]::new);
        List<String> fileNamesWithoutExtensionArray = Arrays.stream(fileNamesWithoutExtension
                .clone())
                .map(fN -> fN.substring(0, fN.length() - JSON_EXTENSION_LENGTH))
                .toList();

        if (fileNamesWithoutExtensionArray.contains(fileName)) {
            log.error("Sudoku filename conflict: {}", fileName);
            return new ResponseEntity<>("Save failed. File with the same name already exists.", HttpStatus.CONFLICT);
        }

        Gson gson = new Gson();

        FileWriter sudokuFileWriter;
        try {
            sudokuFileWriter = new FileWriter("../../puzzle-solver-app/public/resources/Sudoku/" + fileName + JSON_EXTENSION);
            gson.toJson(nfd, sudokuFileWriter);
            sudokuFileWriter.close();
            return new ResponseEntity<>("Save success!", HttpStatus.OK);
        } catch (IOException e) {
            throw new IOException("Can't save sudoku to file");
        }
    }
}

