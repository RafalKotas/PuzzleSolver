package com.puzzlesolverappbackend.puzzleAppFileManager.architect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchitectRepository extends JpaRepository<Architect, Integer>, JpaSpecificationExecutor {

    @Query(value = "SELECT *" +
            " FROM architect a" +
            " WHERE (a.filename LIKE %:filename%" +
            " AND a.source LIKE %:source%" +
            " AND a.year LIKE %:year%" +
            " AND a.month LIKE %:month%" +
            " AND a.height = :height" +
            " AND a.width = :width" +
            " AND a.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Architect> existsArchitectByGivenParamsFromFile(@Param("filename") String filename,
                                                            @Param("source") String source,
                                                            @Param("year") String year,
                                                            @Param("month") String month,
                                                            @Param("difficulty") Double difficulty,
                                                            @Param("height") Integer height,
                                                            @Param("width") Integer width);
}
