package com.puzzlesolverappbackend.puzzleAppFileManager.repository;

import com.puzzlesolverappbackend.puzzleAppFileManager.model.Akari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AkariRepository extends JpaRepository<Akari, Integer>, JpaSpecificationExecutor {

    List<Akari> findAll();

    Optional<Akari> findById(Integer Id);

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
