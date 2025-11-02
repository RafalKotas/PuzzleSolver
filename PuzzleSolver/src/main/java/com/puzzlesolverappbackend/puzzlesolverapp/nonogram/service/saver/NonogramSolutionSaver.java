package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.saver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramSolverUtils;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.FinalNonogramSolutionDTO;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolutionSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        Path filePath = directory.resolve("r" + request.getFileName());

        String formattedJson = SolutionJsonFormatter.format(result);

        try (Writer writer = Files.newBufferedWriter(filePath)) {
            writer.write(formattedJson);
        }

        return result;
    }
}