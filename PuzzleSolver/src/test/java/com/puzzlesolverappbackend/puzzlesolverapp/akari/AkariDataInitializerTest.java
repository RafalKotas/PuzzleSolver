package com.puzzlesolverappbackend.puzzlesolverapp.akari;

import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AkariDataInitializerTest {

    @Mock
    private AkariRepository repository;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private AkariDataInitializer initializer;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldNotSaveAkariWhenAlreadyExists() throws Exception {
        // given
        String filename = "sample.json";
        String content = """
            {
              "board": [["1", "0"]],
              "source": "src",
              "year": "2024",
              "month": "07",
              "height": 2,
              "width": 2,
              "difficulty": 2.5
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = AkariDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsAkariByGivenParamsFromFile(
                anyString(), anyString(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(Optional.of(new Akari()));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Akari.class));
    }

    @Test
    void shouldSaveNewAkariWhenNotPresentInRepository() throws Exception {
        // given
        String filename = "sample.json";
        String content = """
            {
              "board": [["1", "0"]],
              "source": "src",
              "year": "2024",
              "month": "07",
              "height": 2,
              "width": 2,
              "difficulty": 2.5
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = AkariDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsAkariByGivenParamsFromFile(
                anyString(), anyString(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        verify(repository).save(any(Akari.class));
        assertThat(initializer.getAkariRepository()).isNotNull();
        assertThat(initializer.getCommonService()).isNotNull();
    }

    @Test
    void shouldLogErrorWhenJsonIsMalformed() throws Exception {
        // given
        String filename = "malformed.json";
        String malformedJson = """
        {
          "board": [["1", "0"]],
          "source": "src",
          "year": "2024",
          "month": "07",
          "height": 2,
          "width": 2,
          "difficulty": 2.5,
    """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, malformedJson);

        initializer.setPuzzlePath(tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString())).thenReturn(Set.of(filename));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Akari.class));
    }
}
