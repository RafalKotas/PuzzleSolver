package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NonogramRepository extends JpaRepository<Nonogram, Integer>, JpaSpecificationExecutor<Nonogram> {

    @Query("SELECT DISTINCT n.source FROM Nonogram n ORDER BY n.source ASC")
    List<String> selectNonogramSources();

    @Query("SELECT DISTINCT n.publication.year FROM Nonogram n ORDER BY n.publication.year ASC")
    List<String> selectNonogramYears();

    @Query("SELECT DISTINCT n.publication.month FROM Nonogram n ORDER BY n.publication.month ASC")
    List<String> selectNonogramMonths();

    @Query("SELECT DISTINCT n.difficulty FROM Nonogram n ORDER BY n.difficulty ASC")
    List<Double> selectNonogramDifficulties();

    @Query("SELECT DISTINCT n.dimensions.width FROM Nonogram n ORDER BY n.dimensions.width ASC")
    List<Integer> selectNonogramWidths();

    @Query("SELECT DISTINCT n.dimensions.height FROM Nonogram n ORDER BY n.dimensions.height ASC")
    List<Integer> selectNonogramHeights();

    @Query("""
           SELECT n
           FROM Nonogram n
           WHERE n.source IN :sources
             AND n.difficulty BETWEEN :minDifficulty AND :maxDifficulty
           """)
    List<Nonogram> selectNonogramBySourceAndDifficulty(
            @Param("sources") Collection<String> sources,
            @Param("minDifficulty") Double minDifficulty,
            @Param("maxDifficulty") Double maxDifficulty);

    @Query("""
           SELECT CASE WHEN COUNT(n) > 0 THEN TRUE ELSE FALSE END
           FROM Nonogram n
           WHERE (:filename  IS NULL OR n.filename = :filename)
             AND (:source    IS NULL OR LOWER(n.source) LIKE LOWER(CONCAT('%', :source, '%')))
             AND (:year      IS NULL OR n.publication.year  LIKE CONCAT('%', :year, '%'))
             AND (:month     IS NULL OR n.publication.month LIKE CONCAT('%', :month, '%'))
             AND (:height    IS NULL OR n.dimensions.height = :height)
             AND (:width     IS NULL OR n.dimensions.width  = :width)
             AND (:difficulty IS NULL OR n.difficulty = :difficulty)
           """)
    Optional<Nonogram> existsNonogramByGivenParamsFromFile(
            @Param("filename") String filename,
            @Param("source") String source,
            @Param("year")   String year,
            @Param("month")  String month,
            @Param("difficulty") Double difficulty,
            @Param("height") Integer height,
            @Param("width")  Integer width
    );

    @Query("""
           SELECT n.filename
           FROM Nonogram n
           WHERE n.difficulty = :difficulty
             AND LOWER(n.source) LIKE '%logi%'
           ORDER BY (n.dimensions.height * n.dimensions.width) ASC
           """)
    List<String> findLogiNonogramsNamesByDifficultySortedByArea(@Param("difficulty") double difficulty);
}
