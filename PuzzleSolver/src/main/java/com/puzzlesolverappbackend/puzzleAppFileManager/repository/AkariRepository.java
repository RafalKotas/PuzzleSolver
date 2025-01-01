package com.puzzlesolverappbackend.puzzleAppFileManager.repository;

import com.puzzlesolverappbackend.puzzleAppFileManager.model.Akari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AkariRepository extends JpaRepository<Akari, Integer>, JpaSpecificationExecutor {

    @Query(value = "SELECT *" +
            " FROM akari_puzzles_data apd" +
            " WHERE (apd.filename LIKE %:filename%" +
            " AND apd.source LIKE %:source%" +
            " AND apd.height = :height" +
            " AND apd.width = :width" +
            " AND apd.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Akari> existsAkariByGivenParamsFromFile(@Param("filename") String filename,
                                                            @Param("source") String source,
                                                            @Param("difficulty") Double difficulty,
                                                            @Param("height") Integer height,
                                                            @Param("width") Integer width);
}
