package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelperTest {

    @Test
    @DisplayName("PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelper> constructor = PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
    }
}