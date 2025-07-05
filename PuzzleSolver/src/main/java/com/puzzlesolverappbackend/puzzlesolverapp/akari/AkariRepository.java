package com.puzzlesolverappbackend.puzzlesolverapp.akari;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AkariRepository extends JpaRepository<Akari, Integer>, JpaSpecificationExecutor {

    @Query(value = "SELECT *" +
            " FROM akari a" +
            " WHERE (a.filename LIKE %:filename%" +
            " AND a.source LIKE %:source%" +
            " AND a.height = :height" +
            " AND a.width = :width" +
            " AND a.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Akari> existsAkariByGivenParamsFromFile(@Param("filename") String filename,
                                                            @Param("source") String source,
                                                            @Param("difficulty") Double difficulty,
                                                            @Param("height") Integer height,
                                                            @Param("width") Integer width);
}
