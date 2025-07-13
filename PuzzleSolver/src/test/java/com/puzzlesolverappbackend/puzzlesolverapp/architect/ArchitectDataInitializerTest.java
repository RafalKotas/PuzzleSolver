package com.puzzlesolverappbackend.puzzlesolverapp.architect;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ArchitectDataInitializerTest {

    @Mock
    private ArchitectRepository repository;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private ArchitectDataInitializer initializer;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldNotSaveArchitectWhenAlreadyExists() throws Exception {
        // given
        String filename = "architect2.json";
        String content = """
        {
          "tanksInRows": [0],
          "tanksInColumns": [0],
          "board": [["Y"]],
          "height": 1,
          "width": 1,
          "source": "logiMix",
          "year": "2021",
          "month": "06",
          "difficulty": 1.0
        }
        """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = ArchitectDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsArchitectByGivenParamsFromFile(
                "architect2", "logiMix", "2021", "06", 1.0, 1, 1))
                .thenReturn(Optional.of(new Architect()));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Architect.class));
    }

    @Test
    void shouldSaveNewArchitectWhenNotPresentInRepository() throws Exception {
        // given
        String filename = "architect1.json";
        String content = """
        {
          "tanksInRows": [0],
          "tanksInColumns": [0],
          "board": [["Y"]],
          "height": 1,
          "width": 1,
          "source": "logiMix",
          "year": "2021",
          "month": "06",
          "difficulty": 1.0
        }
        """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = ArchitectDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsArchitectByGivenParamsFromFile(
                "architect1", "logiMix", "2021", "06", 1.0, 1, 1))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        verify(repository).save(any(Architect.class));
        assertThat(initializer.getArchitectRepository()).isNotNull();
        assertThat(initializer.getCommonService()).isNotNull();
    }

    @Test
    void shouldLogErrorWhenJsonIsMalformed() throws Exception {
        // given
        String filename = "malformedArchitect.json";
        String malformedJson = """
        {
          "tanksInRows": [0],
          "tanksInColumns": [0],
          "board": [["A"]],
          "source": "logiNonograms",
          "year": "2022",
          "month": "May",
          "height": 1,
          "width": 1,
          "difficulty": 1.0
        """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, malformedJson);

        initializer.setPuzzlePath(tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Architect.class));
    }
}
