package com.puzzlesolverappbackend.puzzlesolverapp.slitherlink;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlitherlinkTest {
    @Test
    void constructor_shouldSetAllFieldsCorrectly() {
        // given
        String filename = "slitherlink.txt";
        String source = "puzzleBook";
        String year = "2022";
        String month = "05";
        Double difficulty = 3.7;
        Integer height = 14;
        Integer width = 10;

        // when
        Slitherlink slitherlink = new Slitherlink(filename, source, year, month, difficulty, height, width);

        // then
        assertThat(slitherlink.getFilename()).isEqualTo(filename);
        assertThat(slitherlink.getSource()).isEqualTo(source);
        assertThat(slitherlink.getYear()).isEqualTo(year);
        assertThat(slitherlink.getMonth()).isEqualTo(month);
        assertThat(slitherlink.getDifficulty()).isEqualTo(difficulty);
        assertThat(slitherlink.getHeight()).isEqualTo(height);
        assertThat(slitherlink.getWidth()).isEqualTo(width);
    }

    @Test
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // when
        Slitherlink slitherlink = new Slitherlink();

        // then
        assertThat(slitherlink).isNotNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        Slitherlink slitherlink = new Slitherlink();

        String filename = "daily.txt";
        String source = "online";
        String year = "2025";
        String month = "07";
        Double difficulty = 2.9;
        Integer height = 16;
        Integer width = 16;

        // when
        slitherlink.setFilename(filename);
        slitherlink.setSource(source);
        slitherlink.setYear(year);
        slitherlink.setMonth(month);
        slitherlink.setDifficulty(difficulty);
        slitherlink.setHeight(height);
        slitherlink.setWidth(width);

        // then
        assertThat(slitherlink.getFilename()).isEqualTo(filename);
        assertThat(slitherlink.getSource()).isEqualTo(source);
        assertThat(slitherlink.getYear()).isEqualTo(year);
        assertThat(slitherlink.getMonth()).isEqualTo(month);
        assertThat(slitherlink.getDifficulty()).isEqualTo(difficulty);
        assertThat(slitherlink.getHeight()).isEqualTo(height);
        assertThat(slitherlink.getWidth()).isEqualTo(width);
    }

    @Test
    void toString_shouldIncludeAllFields() {
        // given
        Slitherlink slitherlink = new Slitherlink("slither.txt", "source", "2024", "03", 4.0, 12, 12);

        // when
        String result = slitherlink.toString();

        // then
        assertThat(result)
                .contains("filename=slither.txt",
                        "source=source",
                        "year=2024",
                        "month=03",
                        "difficulty=4.0",
                        "height=12",
                        "width=12")
                .startsWith("Slitherlink(super=TimedPuzzleEntity(");
    }
}