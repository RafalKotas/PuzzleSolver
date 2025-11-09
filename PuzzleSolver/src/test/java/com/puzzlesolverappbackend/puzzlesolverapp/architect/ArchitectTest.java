package com.puzzlesolverappbackend.puzzlesolverapp.architect;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Dimensions;
import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Publication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ArchitectTest {

    @Test
    @DisplayName("AllArgsConstructor should set fields correctly")
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
        assertThat(architect.getFilename()).isEqualTo("arch_file.txt");
        assertThat(architect.getSource()).isEqualTo("journal");
        assertThat(architect.getPublication().getYear()).isEqualTo("2022");
        assertThat(architect.getPublication().getMonth()).isEqualTo("11");
        assertThat(architect.getDifficulty()).isEqualTo(4.2);
        assertThat(architect.getDimensions().getHeight()).isEqualTo(25);
        assertThat(architect.getDimensions().getWidth()).isEqualTo(35);
    }

    @Test
    @DisplayName("NoArgsConstructor should create empty instance")
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // given & when
        Architect architect = new Architect();

        // then
        assertNotNull(architect);
    }

    @Test
    @DisplayName("Should get fields after setting values")
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
        architect.setPublication(new Publication(year, month));
        architect.setDifficulty(difficulty);
        architect.setDimensions(new Dimensions(height, width));

        // then
        assertThat(architect.getFilename()).isEqualTo("arch_test.txt");
        assertThat(architect.getSource()).isEqualTo("contest");
        assertThat(architect.getPublication().getYear()).isEqualTo("2020");
        assertThat(architect.getPublication().getMonth()).isEqualTo("04");
        assertThat(architect.getDifficulty()).isEqualTo(3.1);
        assertThat(architect.getDimensions().getHeight()).isEqualTo(18);
        assertThat(architect.getDimensions().getWidth()).isEqualTo(28);
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_includesAllFieldsFromHierarchy() {
        // given
        Architect architect = new Architect(
                "arch_file.txt",
                "magazine",
                "2023",
                "06",
                2.5,
                15,
                20
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
                .startsWith("Architect(super=SizedPublishedEntity(");
    }
}