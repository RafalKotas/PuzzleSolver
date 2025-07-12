package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NonogramTest {

    @Test
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
        assertThat(nonogram.getFilename()).isEqualTo(filename);
        assertThat(nonogram.getSource()).isEqualTo(source);
        assertThat(nonogram.getYear()).isEqualTo(year);
        assertThat(nonogram.getMonth()).isEqualTo(month);
        assertThat(nonogram.getDifficulty()).isEqualTo(difficulty);
        assertThat(nonogram.getHeight()).isEqualTo(height);
        assertThat(nonogram.getWidth()).isEqualTo(width);
    }

    @Test
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // when
        Nonogram nonogram = new Nonogram();

        // then
        assertThat(nonogram).isNotNull();
    }

    @Test
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
        nonogram.setYear(year);
        nonogram.setMonth(month);
        nonogram.setDifficulty(difficulty);
        nonogram.setHeight(height);
        nonogram.setWidth(width);

        // then
        assertThat(nonogram.getFilename()).isEqualTo(filename);
        assertThat(nonogram.getSource()).isEqualTo(source);
        assertThat(nonogram.getYear()).isEqualTo(year);
        assertThat(nonogram.getMonth()).isEqualTo(month);
        assertThat(nonogram.getDifficulty()).isEqualTo(difficulty);
        assertThat(nonogram.getHeight()).isEqualTo(height);
        assertThat(nonogram.getWidth()).isEqualTo(width);
    }

    @Test
    void toString_shouldIncludeAllFields() {
        // given
        Nonogram nonogram = new Nonogram("test.txt", "source", "2023", "12", 1.1, 5, 5);

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
                .startsWith("Nonogram(super=TimedPuzzleEntity(");
    }
}