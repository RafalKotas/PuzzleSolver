package com.puzzlesolverappbackend.puzzleAppFileManager.repository;

import com.puzzlesolverappbackend.puzzleAppFileManager.model.Sudoku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SudokuRepository extends JpaRepository<Sudoku, Integer> {

    @Query(value = "SELECT *" +
            " FROM sudoku_puzzles_data spd" +
            " WHERE (spd.filename LIKE %:filename%" +
            " AND spd.source LIKE %:source%" +
            " AND spd.year LIKE %:year%" +
            " AND spd.month LIKE %:month%" +
            " AND spd.filled = :filled" +
            " AND spd.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Sudoku> existsSudokuByGivenParamsFromFile(@Param("filename") String filename,
                                                              @Param("source") String source,
                                                              @Param("year") String year,
                                                              @Param("month") String month,
                                                              @Param("filled") Integer filled,
                                                              @Param("difficulty") Double difficulty);
}
