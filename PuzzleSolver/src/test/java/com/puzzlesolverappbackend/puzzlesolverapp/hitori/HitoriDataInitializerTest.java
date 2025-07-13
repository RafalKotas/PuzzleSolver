package com.puzzlesolverappbackend.puzzlesolverapp.hitori;

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

class HitoriDataInitializerTest {

    @Mock
    private HitoriRepository repository;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private HitoriDataInitializer initializer;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldNotSaveHitoriWhenAlreadyExists() throws Exception {
        // given
        String filename = "test.json";
        String content = """
            {
              "board": [["X"]],
              "source": "src",
              "difficulty": 3.2,
              "height": 3,
              "width": 3
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field pathField = HitoriDataInitializer.class.getDeclaredField("puzzlePath");
        pathField.setAccessible(true);
        pathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsHitoriByGivenParamsFromFile(
                anyString(), anyString(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(Optional.of(new Hitori()));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Hitori.class));
    }

    @Test
    void shouldSaveNewHitoriWhenNotPresentInRepository() throws Exception {
        // given
        String filename = "test.json";
        String content = """
            {
              "board": [["X"]],
              "source": "src",
              "difficulty": 3.2,
              "height": 3,
              "width": 3
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field pathField = HitoriDataInitializer.class.getDeclaredField("puzzlePath");
        pathField.setAccessible(true);
        pathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsHitoriByGivenParamsFromFile(
                anyString(), anyString(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        verify(repository).save(any(Hitori.class));
        assertThat(initializer.getHitoriRepository()).isNotNull();
        assertThat(initializer.getCommonService()).isNotNull();
    }

    @Test
    void shouldLogErrorWhenJsonIsMalformed() throws Exception {
        // given
        String filename = "malformedHitori.json";
        String malformedJson = """
        {
          "board": [[1, 2]],
          "source": "test-source",
          "height": 2,
          "width": 2,
          "difficulty": 3.0
        """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, malformedJson);

        initializer.setPuzzlePath(tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Hitori.class));
    }
}
