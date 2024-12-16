package com.puzzlesolverappbackend.puzzleAppFileManager.controllers;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.SudokuFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
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

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION_LENGTH;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/sudoku")
public class SudokuController {

    @Autowired
    CommonService commonService;

    @PostMapping("/save")
    public ResponseEntity<String> saveSudokuToJsonFile(@RequestParam String fileName, @Valid @RequestBody SudokuFileDetails nfd) throws IOException {

        System.out.println("SAVE SUDOKU START, FILENAME: " + fileName);
        System.out.println(nfd.toString());

        Set<String> existingFilesNames = commonService
                .listFilesUsingJavaIO("../../puzzle-solver-app/public/resources/Sudoku/");

        String[] fileNamesWithoutExtension = existingFilesNames.toArray(String[]::new);
        List<String> fileNamesWithoutExtensionArray = Arrays.stream(fileNamesWithoutExtension
                .clone())
                .map(fN -> fN.substring(0, fN.length() - JSON_EXTENSION_LENGTH))
                .toList();

        if(fileNamesWithoutExtensionArray.contains(fileName)) {
            return new ResponseEntity<>("Save failed. File with same name already exists.", HttpStatus.OK);
        }

        Gson gson = new Gson();

        FileWriter abc;
        try {
            abc = new FileWriter("../../puzzle-solver-app/public/resources/Sudoku/" + fileName + JSON_EXTENSION);
            gson.toJson(nfd, abc);
            abc.close();
            return new ResponseEntity<>("Save success!", HttpStatus.OK);
        } catch (IOException e) {
            throw new IOException("Can't save sudoku to file");
        }
    }
}

