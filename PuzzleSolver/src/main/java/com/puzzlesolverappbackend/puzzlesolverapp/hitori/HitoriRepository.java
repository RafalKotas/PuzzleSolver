package com.puzzlesolverappbackend.puzzlesolverapp.hitori;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HitoriRepository extends JpaRepository<Hitori, Integer> {

    @Query(value = "SELECT *" +
            " FROM hitori h" +
            " WHERE (h.filename LIKE %:filename%" +
            " AND h.source LIKE %:source%" +
            " AND h.height = :height" +
            " AND h.width = :width" +
            " AND h.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Hitori> existsHitoriByGivenParamsFromFile(@Param("filename") String filename,
                                                     @Param("source") String source,
                                                     @Param("difficulty") Double difficulty,
                                                     @Param("height") Integer height,
                                                     @Param("width") Integer width);
}
