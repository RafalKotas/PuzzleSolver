package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.initializers;

import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NonogramsDataInitializerTest {

    @Mock
    private NonogramRepository repository;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private NonogramsDataInitializer initializer;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        repository = mock(NonogramRepository.class);
        commonService = mock(CommonService.class);
        initializer = new NonogramsDataInitializer(repository, commonService);
    }

    @Test
    void shouldNotSaveNonogramWhenAlreadyExists() throws Exception {
        // given
        String filename = "existing.json";
        String content = """
            {
              "rowSequences": [[1]],
              "columnSequences": [[2]],
              "filename": "existing",
              "source": "src",
              "year": "2023",
              "month": "06",
              "difficulty": 3.0,
              "height": 4,
              "width": 4
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsNonogramByGivenParamsFromFile(
                anyString(), anyString(), anyString(), anyString(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(Optional.of(new Nonogram()));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Nonogram.class));
    }

    @Test
    void shouldSaveNewNonogramWhenNotPresentInRepository() throws Exception {
        // given
        String filename = "test-nonogram.json";
        String content = """
            {
              "rowSequences": [[1,2]],
              "columnSequences": [[3,4]],
              "filename": "test-nonogram",
              "source": "logi",
              "year": "2024",
              "month": "07",
              "difficulty": 4.5,
              "height": 5,
              "width": 5,
              "additionalContent": "ignore"
            }
            """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);

        Field puzzlePathField = NonogramsDataInitializer.class.getDeclaredField("puzzlePath");
        puzzlePathField.setAccessible(true);
        puzzlePathField.set(initializer, tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        when(repository.existsNonogramByGivenParamsFromFile(
                "test-nonogram", "logi", "2024", "07", 4.5, 5, 5))
                .thenReturn(Optional.empty());

        // when
        initializer.run();

        // then
        verify(repository).save(any(Nonogram.class));
        assertThat(initializer.getNonogramRepository()).isNotNull();
        assertThat(initializer.getCommonService()).isNotNull();
    }

    @Test
    void shouldLogErrorWhenJsonIsMalformed() throws Exception {
        // given
        String filename = "malformedNonogram.json";
        String malformedJson = """
        {
          "rowSequences": [[1, 2]],
          "columnSequences": [[3, 4]],
          "filename": "malformedNonogram",
          "height": 5,
          "width": 5,
          "source": "source",
          "year": "2024",
          "month": "07",
          "difficulty": 4.5,
          "additionalContent": "extraData"
        """;

        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, malformedJson);

        initializer.setPuzzlePath(tempDir.toString() + "/");

        when(commonService.listFilesUsingJavaIO(anyString()))
                .thenReturn(Set.of(filename));

        // when
        initializer.run();

        // then
        verify(repository, never()).save(any(Nonogram.class));
    }
}
