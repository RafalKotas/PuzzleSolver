package com.puzzlesolverappbackend.puzzleAppFileManager.repository;

import com.puzzlesolverappbackend.puzzleAppFileManager.model.Nonogram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NonogramRepository extends JpaRepository<Nonogram, Integer>, JpaSpecificationExecutor {

    @Query(value = "SELECT distinct(npd.source)" +
            " FROM nonogram npd",
            nativeQuery = true)
    List<String> selectNonogramSources();

    @Query(value = "SELECT distinct(npd.year)" +
            " FROM nonogram npd",
            nativeQuery = true)
    List<String> selectNonogramYears();

    @Query(value = "SELECT distinct(npd.month)" +
            " FROM nonogram npd",
            nativeQuery = true)
    List<String> selectNonogramMonths();

    @Query(value = "SELECT distinct(npd.difficulty)" +
            " FROM nonogram npd",
            nativeQuery = true)
    List<Double> selectNonogramDifficulties();

    @Query(value = "SELECT distinct(npd.width)" +
            " FROM nonogram npd",
            nativeQuery = true)
    List<Integer> selectNonogramWidths();

    @Query(value = "SELECT distinct(npd.height)" +
            " FROM nonogram npd",
            nativeQuery = true)
    List<Integer> selectNonogramHeights();

    @Query(value = "SELECT *" +
            " FROM nonogram npd" +
            " WHERE source IN :sources" +
            " AND npd.difficulty BETWEEN :minDifficulty and :maxDifficulty",
            nativeQuery = true)
    List<Nonogram> selectNonogramBySourceAndDifficulty(@Param("sources") Collection<String> sources,
                                                      @Param("minDifficulty") Double minDifficulty,
                                                      @Param("maxDifficulty") Double maxDifficulty);

    @Query(value = "SELECT *" +
            " FROM nonogram npd" +
            " WHERE (npd.filename=:filename" + //LIKE %:filename% +
            " AND npd.source LIKE %:source%" +
            " AND npd.year LIKE %:year%" +
            " AND npd.month LIKE %:month%" +
            " AND npd.height = :height" +
            " AND npd.width = :width" +
            " AND npd.difficulty = :difficulty)",
            nativeQuery = true)
    Optional<Nonogram> existsNonogramByGivenParamsFromFile(@Param("filename") String filename,
                                                            @Param("source") String source,
                                                            @Param("year") String year,
                                                            @Param("month") String month,
                                                            @Param("difficulty") Double difficulty,
                                                            @Param("height") Integer height,
                                                            @Param("width") Integer width);

    // all available - page
    @Query(value = "SELECT *" +
            " FROM nonogram npd" +
            " WHERE (npd.source IN :sources" +
            " AND npd.year IN :years" +
            " AND npd.month IN :months" +
            " AND npd.difficulty BETWEEN :minDifficulty and :maxDifficulty" +
            " AND npd.difficulty BETWEEN :minWidth and :maxWidth" +
            " AND npd.height BETWEEN :minHeight and :maxHeight)",
            nativeQuery = true)
    Page<Nonogram> getNonogramsUsingFilters(
                                        @Param("sources") Collection<String> sources,
                                        @Param("years") Collection<String> years,
                                        @Param("months") Collection<String> months,
                                        @Param("minDifficulty") Double minDifficulty,
                                        @Param("maxDifficulty") Double maxDifficulty,
                                        @Param("minHeight") Integer minHeight,
                                        @Param("maxHeight") Integer maxHeight,
                                        @Param("minWidth") Integer minWidth,
                                        @Param("maxWidth") Integer maxWidth,
                               Pageable pageable);

}
