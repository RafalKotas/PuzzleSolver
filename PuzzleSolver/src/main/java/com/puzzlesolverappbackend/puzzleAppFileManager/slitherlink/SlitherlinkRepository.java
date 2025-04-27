package com.puzzlesolverappbackend.puzzleAppFileManager.slitherlink;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlitherlinkRepository extends JpaRepository<Slitherlink, Integer>, JpaSpecificationExecutor {

    @Query(value = "SELECT *" +
            " FROM slitherlink sli" +
            " WHERE (sli.filename LIKE %:filename%" +
            " AND sli.source LIKE %:source%" +
            " AND sli.year LIKE %:year%" +
            " AND sli.month LIKE %:month%" +
            " AND sli.height = :height" +
            " AND sli.width = :width" +
            " AND sli.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Slitherlink> existsSlitherlinkByGivenParamsFromFile(@Param("filename") String filename,
                                                           @Param("source") String source,
                                                           @Param("year") String year,
                                                           @Param("month") String month,
                                                           @Param("difficulty") Double difficulty,
                                                           @Param("height") Integer height,
                                                           @Param("width") Integer width);
}
