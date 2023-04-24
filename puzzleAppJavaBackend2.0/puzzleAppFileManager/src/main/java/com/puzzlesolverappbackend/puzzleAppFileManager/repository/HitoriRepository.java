package com.puzzlesolverappbackend.puzzleAppFileManager.repository;

import com.puzzlesolverappbackend.puzzleAppFileManager.model.Akari;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.Hitori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HitoriRepository extends JpaRepository<Hitori, Integer> {

    List<Hitori> findAll();

    Optional<Hitori> findById(Integer Id);

    @Query(value = "SELECT *" +
            " FROM hitori_puzzles_data hpd" +
            " WHERE (hpd.filename LIKE %:filename%" +
            " AND hpd.source LIKE %:source%" +
            " AND hpd.height = :height" +
            " AND hpd.width = :width" +
            " AND hpd.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Hitori> existsHitoriByGivenParamsFromFile(@Param("filename") String filename,
                                                     @Param("source") String source,
                                                     @Param("difficulty") Double difficulty,
                                                     @Param("height") Integer height,
                                                     @Param("width") Integer width);
}
