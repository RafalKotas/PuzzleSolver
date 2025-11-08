package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.specification;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Dimensions;
import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Publication;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramFilterRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NonogramSpecificationIntegrationTest {

    @Autowired
    private NonogramRepository nonogramRepository;

    @AfterEach
    void cleanUpDatabase() {
        nonogramRepository.deleteAll();
    }

    @Test
    @DisplayName("withFilters - should filter Nonograms by source and difficulty range")
    void shouldFilterBySourceAndDifficultyRange() {
        // given
        Nonogram nonogram1 = new Nonogram();
        nonogram1.setFilename("puzzle1.json");
        nonogram1.setSource("source1");
        Publication publication1 = new Publication("2020", "05");
        nonogram1.setPublication(publication1);
        nonogram1.setDifficulty(3.0);
        Dimensions dimensions1 = new Dimensions(10, 20);
        nonogram1.setDimensions(dimensions1);

        Nonogram nonogram2 = new Nonogram();
        nonogram2.setFilename("puzzle2.json");
        nonogram2.setSource("source2");
        Publication publication2 = new Publication("2021", "06");
        nonogram2.setPublication(publication2);
        nonogram2.setDifficulty(5.0);
        Dimensions dimensions2 = new Dimensions(15, 25);
        nonogram2.setDimensions(dimensions2);

        nonogramRepository.saveAll(List.of(nonogram1, nonogram2));

        NonogramFilterRequest filters = new NonogramFilterRequest(
                List.of("source1"),
                List.of("2020"),
                List.of("05"),
                2.0, 4.0, // difficulty range
                9, 27, // width range
                5, 50  // height range
        );

        // when
        Specification<Nonogram> spec = NonogramSpecification.withFilters(filters);
        List<Nonogram> result = nonogramRepository.findAll(spec);

        // then
        assertThat(result)
                .hasSize(1)
                .extracting(Nonogram::getSource)
                .containsExactly("source1");
    }
}

