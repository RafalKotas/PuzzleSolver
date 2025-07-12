package com.puzzlesolverappbackend.puzzlesolverapp.hitori;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HitoriTest {

    @Test
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
        assertThat(hitori.getFilename()).isEqualTo(filename);
        assertThat(hitori.getSource()).isEqualTo(source);
        assertThat(hitori.getDifficulty()).isEqualTo(difficulty);
        assertThat(hitori.getHeight()).isEqualTo(height);
        assertThat(hitori.getWidth()).isEqualTo(width);
    }

    @Test
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // when
        Hitori hitori = new Hitori();

        // then
        assertThat(hitori).isNotNull();
    }

    @Test
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
        hitori.setHeight(height);
        hitori.setWidth(width);

        // then
        assertThat(hitori.getFilename()).isEqualTo(filename);
        assertThat(hitori.getSource()).isEqualTo(source);
        assertThat(hitori.getDifficulty()).isEqualTo(difficulty);
        assertThat(hitori.getHeight()).isEqualTo(height);
        assertThat(hitori.getWidth()).isEqualTo(width);
    }

    @Test
    void toString_shouldIncludeAllFields() {
        // given
        Hitori hitori = new Hitori("test.txt", "source", 3.3, 6, 6);

        // when
        String result = hitori.toString();

        // then
        assertThat(result)
                .contains("filename=test.txt",
                        "source=source",
                        "difficulty=3.3",
                        "height=6",
                        "width=6")
                .startsWith("Hitori(super=SizedPuzzleEntity(");
    }
}