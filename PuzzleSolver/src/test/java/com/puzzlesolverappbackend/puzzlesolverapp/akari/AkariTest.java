package com.puzzlesolverappbackend.puzzlesolverapp.akari;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Dimensions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AkariTest {

    @Test
    @DisplayName("AllArgsConstructor should set fields correctly")
    void constructor_shouldSetAllFieldsCorrectly() {
        // given
        String filename = "akari_01.txt";
        String source = "example_source";
        Double difficulty = 3.5;
        Integer height = 8;
        Integer width = 5;

        // when
        Akari akari = new Akari(filename, source, difficulty, height, width);

        // then
        assertThat(akari.getFilename()).isEqualTo("akari_01.txt");
        assertThat(akari.getSource()).isEqualTo("example_source");
        assertThat(akari.getDifficulty()).isEqualTo(3.5);
        assertThat(akari.getDimensions().getHeight()).isEqualTo(8);
        assertThat(akari.getDimensions().getWidth()).isEqualTo(5);
    }

    @Test
    @DisplayName("NoArgsConstructor should create empty instance")
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // given & when
        Akari akari = new Akari();

        // then
        assertNotNull(akari);
    }

    @Test
    @DisplayName("Should get fields after setting values")
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        Akari akari = new Akari();

        String filename = "test.txt";
        String source = "generated";
        Double difficulty = 2.0;
        Integer height = 10;
        Integer width = 15;

        // when
        akari.setFilename(filename);
        akari.setSource(source);
        akari.setDifficulty(difficulty);
        akari.setDimensions(new Dimensions(height, width));

        // then
        assertThat(akari.getFilename()).isEqualTo("test.txt");
        assertThat(akari.getSource()).isEqualTo("generated");
        assertThat(akari.getDifficulty()).isEqualTo(2.0);
        assertThat(akari.getDimensions().getHeight()).isEqualTo(10);
        assertThat(akari.getDimensions().getWidth()).isEqualTo(15);
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_includesAllFieldsFromHierarchy() {
        // given
        Akari akari = new Akari(
                "akari.txt",
                "source",
                1.0,
                10,
                10
        );

        // when
        String result = akari.toString();

        // then
        assertThat(result)
                .contains("filename=akari.txt",
                        "source=source",
                        "difficulty=1.0",
                        "height=10",
                        "width=10")
                .startsWith("Akari(super=BasePuzzleEntity(");
    }
}