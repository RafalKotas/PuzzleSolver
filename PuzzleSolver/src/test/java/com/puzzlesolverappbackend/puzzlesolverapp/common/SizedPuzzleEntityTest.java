package com.puzzlesolverappbackend.puzzlesolverapp.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SizedPuzzleEntityTest {

    @Test
    @DisplayName("should not be instantiable via reflection because class is abstract")
    void shouldNotBeInstantiableViaReflection() throws Exception {
        // given
        Constructor<SizedPuzzleEntity> constructor = SizedPuzzleEntity.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when & then
        InstantiationException exception = assertThrows(InstantiationException.class, constructor::newInstance);
    }


    static class TestSizedEntity extends SizedPuzzleEntity {
        protected TestSizedEntity(String filename, String source, Double difficulty, Integer height, Integer width) {
            super(filename, source, difficulty, height, width);
        }
    }

    @Test
    @DisplayName("constructor should correctly assign all fields")
    void constructorShouldAssignFields() {
        // given
        String filename = "puzzle1.json";
        String source = "testSource";
        Double difficulty = 3.5;
        Integer height = 10;
        Integer width = 15;

        // when
        SizedPuzzleEntity entity = new TestSizedEntity(filename, source, difficulty, height, width);

        // then
        assertThat(entity.getFilename()).isEqualTo("puzzle1.json");
        assertThat(entity.getSource()).isEqualTo("testSource");
        assertThat(entity.getDifficulty()).isEqualTo(3.5);
        assertThat(entity.getHeight()).isEqualTo(10);
        assertThat(entity.getWidth()).isEqualTo(15);

        entity.setHeight(20);
        entity.setWidth(30);
        assertThat(entity.getHeight()).isEqualTo(20);
        assertThat(entity.getWidth()).isEqualTo(30);

        String str = entity.toString();
        assertThat(str)
                .contains("puzzle1.json")
                .contains("testSource")
                .contains("difficulty=3.5")
                .contains("height=20")
                .contains("width=30");
    }
}