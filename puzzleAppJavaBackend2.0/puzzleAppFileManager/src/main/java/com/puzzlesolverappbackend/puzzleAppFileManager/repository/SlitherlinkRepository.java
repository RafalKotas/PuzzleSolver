package com.puzzlesolverappbackend.puzzleAppFileManager.repository;

import com.puzzlesolverappbackend.puzzleAppFileManager.model.Nonogram;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.Slitherlink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SlitherlinkRepository extends JpaRepository<Slitherlink, Integer>, JpaSpecificationExecutor {

    List<Slitherlink> findAll();

    Optional<Slitherlink> findById(Integer Id);

    @Query(value = "SELECT *" +
            " FROM slitherlink_puzzles_data spd" +
            " WHERE (spd.filename LIKE %:filename%" +
            " AND spd.source LIKE %:source%" +
            " AND spd.year LIKE %:year%" +
            " AND spd.month LIKE %:month%" +
            " AND spd.height = :height" +
            " AND spd.width = :width" +
            " AND spd.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Slitherlink> existsSlitherlinkByGivenParamsFromFile(@Param("filename") String filename,
                                                           @Param("source") String source,
                                                           @Param("year") String year,
                                                           @Param("month") String month,
                                                           @Param("difficulty") Double difficulty,
                                                           @Param("height") Integer height,
                                                           @Param("width") Integer width);
}
