package com.puzzlesolverappbackend.puzzlesolverapp.architect;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArchitectTest {

    @Test
    void constructor_shouldSetAllFieldsCorrectly() {
        // given
        String filename = "arch_file.txt";
        String source = "journal";
        String year = "2022";
        String month = "11";
        Double difficulty = 4.2;
        Integer height = 25;
        Integer width = 35;

        // when
        Architect architect = new Architect(filename, source, year, month, difficulty, height, width);

        // then
        assertThat(architect.getFilename()).isEqualTo(filename);
        assertThat(architect.getSource()).isEqualTo(source);
        assertThat(architect.getYear()).isEqualTo(year);
        assertThat(architect.getMonth()).isEqualTo(month);
        assertThat(architect.getDifficulty()).isEqualTo(difficulty);
        assertThat(architect.getHeight()).isEqualTo(height);
        assertThat(architect.getWidth()).isEqualTo(width);
    }

    @Test
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // when
        Architect architect = new Architect();

        // then
        assertThat(architect).isNotNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        Architect architect = new Architect();

        String filename = "arch_test.txt";
        String source = "contest";
        String year = "2020";
        String month = "04";
        Double difficulty = 3.1;
        Integer height = 18;
        Integer width = 28;

        // when
        architect.setFilename(filename);
        architect.setSource(source);
        architect.setYear(year);
        architect.setMonth(month);
        architect.setDifficulty(difficulty);
        architect.setHeight(height);
        architect.setWidth(width);

        // then
        assertThat(architect.getFilename()).isEqualTo(filename);
        assertThat(architect.getSource()).isEqualTo(source);
        assertThat(architect.getYear()).isEqualTo(year);
        assertThat(architect.getMonth()).isEqualTo(month);
        assertThat(architect.getDifficulty()).isEqualTo(difficulty);
        assertThat(architect.getHeight()).isEqualTo(height);
        assertThat(architect.getWidth()).isEqualTo(width);
    }

    @Test
    void toString_includesAllFieldsFromHierarchy() {
        // given
        Architect architect = new Architect(
                "arch_file.txt", "magazine", "2023", "06",
                2.5, 15, 20
        );

        // when
        String result = architect.toString();

        // then
        assertThat(result)
                .contains("filename=arch_file.txt",
                        "source=magazine",
                        "year=2023",
                        "month=06",
                        "difficulty=2.5",
                        "height=15",
                        "width=20")
                .startsWith("Architect(super=TimedPuzzleEntity(");
    }
}