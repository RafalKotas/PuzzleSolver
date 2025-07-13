package com.puzzlesolverappbackend.puzzlesolverapp.slitherlink;

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

class SlitherlinkDataInitializerTest {

    @Mock
    private SlitherlinkRepository repository;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private SlitherlinkDataInitializer initializer;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldNotSaveSlitherlinkWhenAlreadyExists() throws Exception {
        // given
        String filename = "existing.json";
        String content = """
            {
              "board": [["1"]],
              "source": "src",
              "year": "2023",
              "month": "06",
              "height": 1,
              "width": 1,
              "difficulty": 2.0
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = SlitherlinkDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsSlitherlinkByGivenParamsFromFile(
                anyString(), anyString(), anyString(), anyString(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(Optional.of(new Slitherlink()));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Slitherlink.class));
    }

    @Test
    void shouldSaveNewSlitherlinkWhenNotPresentInRepository() throws Exception {
        // given
        String filename = "slither.json";
        String content = """
            {
              "board": [["1", "0"]],
              "source": "source",
              "year": "2024",
              "month": "07",
              "height": 2,
              "width": 2,
              "difficulty": 1.5
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = SlitherlinkDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsSlitherlinkByGivenParamsFromFile(
                anyString(), anyString(), anyString(), anyString(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        verify(repository).save(any(Slitherlink.class));
        assertThat(initializer.getSlitherlinkRepository()).isNotNull();
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
                      "year": "2023",
                      "month": "06",
                      "height": 5,
                      "width": 5,
                      "difficulty": 3.5
                """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, malformedJson);

        initializer.setPuzzlePath(tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString())).thenReturn(Set.of(malformedJson));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Slitherlink.class));
    }
}
