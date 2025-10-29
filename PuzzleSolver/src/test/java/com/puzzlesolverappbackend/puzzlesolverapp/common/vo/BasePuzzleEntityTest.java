package com.puzzlesolverappbackend.puzzlesolverapp.common.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BasePuzzleEntityTest {

    @Test
    @DisplayName("should not be instantiable via reflection because class is abstract")
    void shouldNotBeInstantiableViaReflection() throws Exception {
        // given
        Constructor<BasePuzzleEntity> constructor = BasePuzzleEntity.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // expect
        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InstantiationException.class);
    }
}