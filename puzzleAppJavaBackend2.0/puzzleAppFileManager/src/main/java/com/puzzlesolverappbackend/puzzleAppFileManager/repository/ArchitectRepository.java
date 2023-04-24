package com.puzzlesolverappbackend.puzzleAppFileManager.repository;

import com.puzzlesolverappbackend.puzzleAppFileManager.model.Architect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchitectRepository extends JpaRepository<Architect, Integer>, JpaSpecificationExecutor {

    List<Architect> findAll();

    Optional<Architect> findById(Integer Id);

    @Query(value = "SELECT *" +
            " FROM architect_puzzles_data apd" +
            " WHERE (apd.filename LIKE %:filename%" +
            " AND apd.source LIKE %:source%" +
            " AND apd.year LIKE %:year%" +
            " AND apd.month LIKE %:month%" +
            " AND apd.height = :height" +
            " AND apd.width = :width" +
            " AND apd.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Architect> existsArchitectByGivenParamsFromFile(@Param("filename") String filename,
                                                            @Param("source") String source,
                                                            @Param("year") String year,
                                                            @Param("month") String month,
                                                            @Param("difficulty") Double difficulty,
                                                            @Param("height") Integer height,
                                                            @Param("width") Integer width);
}
