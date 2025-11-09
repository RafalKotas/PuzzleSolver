package com.puzzlesolverappbackend.puzzlesolverapp.slitherlink;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Dimensions;
import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Publication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SlitherlinkTest {

    @Test
    @DisplayName("AllArgsConstructor should set fields correctly")
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
        assertThat(slitherlink.getFilename()).isEqualTo("slitherlink.txt");
        assertThat(slitherlink.getSource()).isEqualTo("puzzleBook");
        assertThat(slitherlink.getPublication().getYear()).isEqualTo("2022");
        assertThat(slitherlink.getPublication().getMonth()).isEqualTo("05");
        assertThat(slitherlink.getDifficulty()).isEqualTo(3.7);
        assertThat(slitherlink.getDimensions().getHeight()).isEqualTo(14);
        assertThat(slitherlink.getDimensions().getWidth()).isEqualTo(10);
    }

    @Test
    @DisplayName("NoArgsConstructor should create empty instance")
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // given & when
        Slitherlink slitherlink = new Slitherlink();

        // then
        assertNotNull(slitherlink);
    }

    @Test
    @DisplayName("Should get fields after setting values")
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
        slitherlink.setPublication(new Publication(year, month));
        slitherlink.setDifficulty(difficulty);
        slitherlink.setDimensions(new Dimensions(height, width));

        // then
        assertThat(slitherlink.getFilename()).isEqualTo("daily.txt");
        assertThat(slitherlink.getSource()).isEqualTo("online");
        assertThat(slitherlink.getPublication().getYear()).isEqualTo("2025");
        assertThat(slitherlink.getPublication().getMonth()).isEqualTo("07");
        assertThat(slitherlink.getDifficulty()).isEqualTo(2.9);
        assertThat(slitherlink.getDimensions().getHeight()).isEqualTo(16);
        assertThat(slitherlink.getDimensions().getWidth()).isEqualTo(16);
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_shouldIncludeAllFields() {
        // given
        Slitherlink slitherlink = new Slitherlink(
                "slither.txt",
                "source",
                "2024",
                "03",
                4.0,
                12,
                12);

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
                .startsWith("Slitherlink(super=SizedPublishedEntity(");
    }
}