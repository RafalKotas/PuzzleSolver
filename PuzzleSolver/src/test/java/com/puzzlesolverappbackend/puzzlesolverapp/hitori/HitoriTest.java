package com.puzzlesolverappbackend.puzzlesolverapp.hitori;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Dimensions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HitoriTest {

    @Test
    @DisplayName("AllArgsConstructor should set fields correctly")
    void constructor_shouldSetAllFieldsCorrectly() {
        // given
        String filename = "hitori.txt";
        String source = "puzzleMag";
        Double difficulty = 2.7;
        Integer height = 10;
        Integer width = 10;

        // when
        Hitori hitori = new Hitori(filename, source, difficulty, height, width);

        // then
        assertThat(hitori.getFilename()).isEqualTo("hitori.txt");
        assertThat(hitori.getSource()).isEqualTo("puzzleMag");
        assertThat(hitori.getDifficulty()).isEqualTo(2.7);
        assertThat(hitori.getDimensions().getHeight()).isEqualTo(10);
        assertThat(hitori.getDimensions().getWidth()).isEqualTo(10);
    }

    @Test
    @DisplayName("NoArgsConstructor should create empty instance")
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // given & when
        Hitori hitori = new Hitori();

        // then
        assertNotNull(hitori);
    }

    @Test
    @DisplayName("Should get fields after setting values")
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        Hitori hitori = new Hitori();

        String filename = "game.txt";
        String source = "website";
        Double difficulty = 1.5;
        Integer height = 8;
        Integer width = 8;

        // when
        hitori.setFilename(filename);
        hitori.setSource(source);
        hitori.setDifficulty(difficulty);
        hitori.setDimensions(new Dimensions(height, width));

        // then
        assertThat(hitori.getFilename()).isEqualTo("game.txt");
        assertThat(hitori.getSource()).isEqualTo("website");
        assertThat(hitori.getDifficulty()).isEqualTo(1.5);
        assertThat(hitori.getDimensions().getHeight()).isEqualTo(8);
        assertThat(hitori.getDimensions().getWidth()).isEqualTo(8);
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_shouldIncludeAllFields() {
        // given
        Hitori hitori = new Hitori(
                "test.txt",
                "source",
                3.3,
                6,
                6
        );

        // when
        String result = hitori.toString();

        // then
        assertThat(result)
                .contains("filename=test.txt",
                        "source=source",
                        "difficulty=3.3",
                        "height=6",
                        "width=6")
                .startsWith("Hitori(super=BasePuzzleEntity(");
    }
}