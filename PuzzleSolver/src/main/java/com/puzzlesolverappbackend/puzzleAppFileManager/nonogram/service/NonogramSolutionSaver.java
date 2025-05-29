package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.service;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.dto.FinalNonogramSolutionDTO;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.dto.NonogramSolutionSaveRequest;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramSolverUtils;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.SolutionJsonFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION;

// TODO difficulty 2 : o10381 o10375 - not full solved (Expecting empty but was: ["o10381", "o04065", "o10375"])
// TODO difficulty 2 : o09839 - incorrect

@Service
@RequiredArgsConstructor
public class NonogramSolutionSaver {

    @Value("${nonogram.solution-dir}")
    private String solutionDir;

    public FinalNonogramSolutionDTO saveIfCorrect(NonogramSolutionSaveRequest request) throws IOException {
        boolean isCorrect = NonogramSolverUtils.isBoardConsistentWithSequences(
                request.getBoard(),
                request.getRowSequences(),
                request.getColumnSequences()
        );

        var result = new FinalNonogramSolutionDTO();
        result.setFinalBoard(request.getBoard());
        result.setVerifiedAgainstOriginal(isCorrect ? "PASS" : "FAIL");

        if (!isCorrect) return result;

        result.setDerivedRowRanges(NonogramSolverUtils.inferSequenceRangesFromBoard(request.getBoard()));
        result.setDerivedColumnRanges(NonogramSolverUtils.inferSequenceRangesFromColumns(request.getBoard()));

        Path directory = Paths.get(solutionDir);
        Files.createDirectories(directory);

        Path filePath = directory.resolve("r" + request.getFileName() + JSON_EXTENSION);
        String formattedJson = SolutionJsonFormatter.format(result);

        try (Writer writer = Files.newBufferedWriter(filePath)) {
            writer.write(formattedJson);
        }

        return result;
    }
}