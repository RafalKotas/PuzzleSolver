package com.puzzlesolverappbackend.puzzlesolverapp.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonServiceTest {

    private CommonService commonService;

    @BeforeEach
    void setUp() {
        commonService = new CommonService();
    }

    @Test
    void listFilesUsingJavaIO_returnsFileNames_whenFilesExist() throws IOException {
        // given
        File tempDir = Files.createTempDirectory("testDir").toFile();
        tempDir.deleteOnExit();
        File file1 = new File(tempDir, "file1.txt");
        File file2 = new File(tempDir, "file2.txt");
        assertTrue(file1.createNewFile());
        assertTrue(file2.createNewFile());
        file1.deleteOnExit();
        file2.deleteOnExit();

        // when
        Set<String> result = commonService.listFilesUsingJavaIO(tempDir.getAbsolutePath());

        // then
        assertEquals(Set.of("file1.txt", "file2.txt"), result);
    }

    @Test
    void listFilesUsingJavaIO_returnsEmptySet_whenDirInvalid() {
        // given
        String invalidDir = "non_existing_directory_123456";

        // when
        Set<String> result = commonService.listFilesUsingJavaIO(invalidDir);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void listFilesUsingJavaIO_skipsDirectories_whenPresentInFolder() throws IOException {
        // given
        File tempDir = Files.createTempDirectory("testDir").toFile();
        tempDir.deleteOnExit();

        File file = new File(tempDir, "file.txt");
        File subdirectory = new File(tempDir, "subdir");

        assertTrue(file.createNewFile());
        assertTrue(subdirectory.mkdir());

        file.deleteOnExit();
        subdirectory.deleteOnExit();

        // when
        Set<String> result = commonService.listFilesUsingJavaIO(tempDir.getAbsolutePath());

        // then
        assertEquals(Set.of("file.txt"), result);
    }
}
