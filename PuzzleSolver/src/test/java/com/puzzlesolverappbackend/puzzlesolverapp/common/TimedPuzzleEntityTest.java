package com.puzzlesolverappbackend.puzzlesolverapp.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimedPuzzleEntityTest {

    @Test
    @DisplayName("should not be instantiable via reflection because class is abstract")
    void shouldNotBeInstantiableViaReflection() throws Exception {
        // given
        Constructor<TimedPuzzleEntity> constructor = TimedPuzzleEntity.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // expect
        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InstantiationException.class);
    }

    static class TestTimedEntity extends TimedPuzzleEntity {
        protected TestTimedEntity(String filename, String source, String year, String month,
                                  Double difficulty, Integer height, Integer width) {
            super(filename, source, year, month, difficulty, height, width);
        }
    }

    @Test
    @DisplayName("constructor should correctly assign all fields including year and month")
    void constructorShouldAssignFields() {
        // given
        String filename = "puzzle2021.json";
        String source = "unitTestSource";
        String year = "2021";
        String month = "03";
        Double difficulty = 4.0;
        Integer height = 25;
        Integer width = 40;

        // when
        TestTimedEntity entity = new TestTimedEntity(filename, source, year, month, difficulty, height, width);

        // then – // fields from BasePuzzleEntity
        assertThat(entity.getFilename()).isEqualTo(filename);
        assertThat(entity.getSource()).isEqualTo(source);
        assertThat(entity.getDifficulty()).isEqualTo(difficulty);

        // fields from SizedPuzzleEntity
        assertThat(entity.getHeight()).isEqualTo(height);
        assertThat(entity.getWidth()).isEqualTo(width);

        // fields from TimedPuzzleEntity
        assertThat(entity.getYear()).isEqualTo(year);
        assertThat(entity.getMonth()).isEqualTo(month);

        // setters
        entity.setYear("2022");
        entity.setMonth("04");
        entity.setHeight(30);
        entity.setWidth(50);
        assertThat(entity.getYear()).isEqualTo("2022");
        assertThat(entity.getMonth()).isEqualTo("04");
        assertThat(entity.getHeight()).isEqualTo(30);
        assertThat(entity.getWidth()).isEqualTo(50);

        // toString (Lombok @ToString(callSuper = true))
        String str = entity.toString();
        assertThat(str)
                .contains("puzzle2021.json")
                .contains("unitTestSource")
                .contains("difficulty=4.0")
                .contains("year=2022")
                .contains("month=04")
                .contains("height=30")
                .contains("width=50");
    }
}