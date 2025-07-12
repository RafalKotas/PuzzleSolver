package com.puzzlesolverappbackend.puzzlesolverapp.common;

import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class FileHelperTest {

    @Test
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<FileHelper> constructor = FileHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
    }

    @Test
    void nonogramSolutionSavePathForFilename_returnsExpectedPath() {
        // given
        String filename = "oXXXXX.txt";

        // when
        String result = FileHelper.nonogramSolutionSavePathForFilename(filename);

        // then
        assertEquals(InitializerConstants.NONOGRAM_SOLUTIONS_PATH + "r" + filename, result);
    }

    @Test
    void nonogramSolutionLoadPathForFilename_returnsExpectedPath() {
        // given
        String filename = "oXXXXX.txt";

        // when
        String result = FileHelper.nonogramSolutionLoadPathForFilename(filename);

        // then
        assertEquals(InitializerConstants.NONOGRAM_SOLUTIONS_PATH + filename, result);
    }
}
