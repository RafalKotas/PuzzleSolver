package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Dimensions;
import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Publication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NonogramTest {

    @Test
    @DisplayName("AllArgsConstructor should set fields correctly")
    void constructor_shouldSetAllFieldsCorrectly() {
        // given
        String filename = "nonogram.txt";
        String source = "magazine";
        String year = "2024";
        String month = "07";
        Double difficulty = 4.5;
        Integer height = 12;
        Integer width = 15;

        // when
        Nonogram nonogram = new Nonogram(filename, source, year, month, difficulty, height, width);

        // then
        assertThat(nonogram.getFilename()).isEqualTo("nonogram.txt");
        assertThat(nonogram.getSource()).isEqualTo("magazine");
        assertThat(nonogram.getPublication().getYear()).isEqualTo("2024");
        assertThat(nonogram.getPublication().getMonth()).isEqualTo("07");
        assertThat(nonogram.getDifficulty()).isEqualTo(4.5);
        assertThat(nonogram.getDimensions().getHeight()).isEqualTo(12);
        assertThat(nonogram.getDimensions().getWidth()).isEqualTo(15);
    }

    @Test
    @DisplayName("NoArgsConstructor should create empty instance")
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // given & when
        Nonogram nonogram = new Nonogram();

        // then
        assertNotNull(nonogram);
    }

    @Test
    @DisplayName("Should get fields after setting values")
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        Nonogram nonogram = new Nonogram();

        String filename = "daily.txt";
        String source = "site";
        String year = "2025";
        String month = "01";
        Double difficulty = 2.0;
        Integer height = 20;
        Integer width = 10;

        // when
        nonogram.setFilename(filename);
        nonogram.setSource(source);
        nonogram.setPublication(new Publication(year, month));
        nonogram.setDifficulty(difficulty);
        nonogram.setDimensions(new Dimensions(height, width));

        // then
        assertThat(nonogram.getFilename()).isEqualTo("daily.txt");
        assertThat(nonogram.getSource()).isEqualTo("site");
        assertThat(nonogram.getPublication().getYear()).isEqualTo("2025");
        assertThat(nonogram.getPublication().getMonth()).isEqualTo("01");
        assertThat(nonogram.getDifficulty()).isEqualTo(2.0);
        assertThat(nonogram.getDimensions().getHeight()).isEqualTo(20);
        assertThat(nonogram.getDimensions().getWidth()).isEqualTo(10);
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_shouldIncludeAllFields() {
        // given
        Nonogram nonogram = new Nonogram(
                "test.txt",
                "source",
                "2023",
                "12",
                1.1,
                5,
                5
        );

        // when
        String result = nonogram.toString();

        // then
        assertThat(result)
                .contains("filename=test.txt",
                        "source=source",
                        "year=2023",
                        "month=12",
                        "difficulty=1.1",
                        "height=5",
                        "width=5")
                .startsWith("Nonogram(super=SizedPublishedEntity(");
    }
}