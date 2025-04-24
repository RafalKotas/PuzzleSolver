package com.puzzlesolverappbackend.puzzleAppFileManager.sudoku;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SudokuRepository extends JpaRepository<Sudoku, Integer> {

    @Query(value = "SELECT *" +
            " FROM sudoku sud" +
            " WHERE (sud.filename LIKE %:filename%" +
            " AND sud.source LIKE %:source%" +
            " AND sud.year LIKE %:year%" +
            " AND sud.month LIKE %:month%" +
            " AND sud.filled = :filled" +
            " AND sud.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Sudoku> existsSudokuByGivenParamsFromFile(@Param("filename") String filename,
                                                              @Param("source") String source,
                                                              @Param("year") String year,
                                                              @Param("month") String month,
                                                              @Param("filled") Integer filled,
                                                              @Param("difficulty") Double difficulty);
}
