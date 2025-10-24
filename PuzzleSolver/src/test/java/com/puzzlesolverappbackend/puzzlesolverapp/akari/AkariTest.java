package com.puzzlesolverappbackend.puzzlesolverapp.akari;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Dimensions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AkariTest {

    @Test
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
        assertEquals("akari_01.txt", akari.getFilename());
        assertEquals("example_source", akari.getSource());
        assertEquals(3.5, akari.getDifficulty());
        assertEquals(8, akari.getSize().getHeight());
        assertEquals(5, akari.getSize().getWidth());
    }

    @Test
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // when
        Akari akari = new Akari();

        // then
        assertNotNull(akari);
    }

    @Test
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
        akari.setSize(new Dimensions(height, width));

        // then
        assertEquals("test.txt", akari.getFilename());
        assertEquals("generated", akari.getSource());
        assertEquals(2.0, akari.getDifficulty());
        assertEquals(10, akari.getSize().getHeight());
        assertEquals(15, akari.getSize().getWidth());
    }

    @Test
    void toString_includesAllFieldsFromHierarchy() {
        // given
        Akari akari = new Akari("akari.txt", "source", 1.0, 10, 10);

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