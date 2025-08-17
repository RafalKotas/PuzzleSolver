package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SymmetricNonogramLoggerInitializerTest {

    @Mock
    CommonService commonService;

    @TempDir
    Path tmp;

    @Test
    @DisplayName("run(): classifies 4/2/1 axis, ignores 'None', catches parsing error AND normalizes basePath (adds separator)")
    void run_classifies_allBuckets_and_normalizesPath_addsSeparator() throws Exception {
        String f4 = "four_axis.json";
        String f2 = "two_axis.json";
        String f1 = "one_axis.json";
        String fn = "none.json";
        String bad = "broken.json";

        String fourAxis = """
            {"rowSequences": [[1],[2],[1]], "columnSequences": [[1],[2],[1]],
             "filename":"four_axis.json","height":3,"width":3,"source":"x","year":"N/D","month":"N/D","difficulty":1.0}
            """;
        String twoAxis = """
            {"rowSequences": [[1],[2],[1]], "columnSequences": [[2],[1],[2]],
             "filename":"two_axis.json","height":3,"width":3,"source":"x","year":"N/D","month":"N/D","difficulty":1.0}
            """;
        String oneAxis = """
            {"rowSequences": [[1],[2],[1]], "columnSequences": [[1],[2],[3]],
             "filename":"one_axis.json","height":3,"width":3,"source":"x","year":"N/D","month":"N/D","difficulty":1.0}
            """;
        String none = """
            {"rowSequences": [[1],[2],[3]], "columnSequences": [[3],[1],[2]],
             "filename":"none.json","height":3,"width":3,"source":"x","year":"N/D","month":"N/D","difficulty":1.0}
            """;

        Files.writeString(tmp.resolve(f4), fourAxis);
        Files.writeString(tmp.resolve(f2), twoAxis);
        Files.writeString(tmp.resolve(f1), oneAxis);
        Files.writeString(tmp.resolve(fn), none);
        Files.writeString(tmp.resolve(bad), "{ invalid json");

        String basePathNoSep = tmp.toAbsolutePath().toString();
        String normalized = basePathNoSep + File.separator;

        when(commonService.listFilesUsingJavaIO(normalized))
                .thenReturn(Set.of(f4, f2, f1, fn, bad));

        SymmetricNonogramLoggerInitializer subject =
                new SymmetricNonogramLoggerInitializer(commonService, new ObjectMapper(), basePathNoSep);

        // when
        subject.run();

        // then (klasyfikacja)
        assertThat(subject.getNonograms3Dsymmetrical()).containsExactlyInAnyOrder(f4);
        assertThat(subject.getNonograms2Dsymmetrical()).containsExactlyInAnyOrder(f2);
        assertThat(subject.getNonograms1Dsymmetrical()).containsExactlyInAnyOrder(f1);
        assertThat(subject.getNonograms3Dsymmetrical()).doesNotContain(fn);
        assertThat(subject.getNonograms2Dsymmetrical()).doesNotContain(fn);
        assertThat(subject.getNonograms1Dsymmetrical()).doesNotContain(fn);

        verify(commonService).listFilesUsingJavaIO(normalized);
    }

    @Test
    @DisplayName("constructor: keeps basePath when it already ends with separator (no normalization needed)")
    void constructor_keepsPath_withSeparator() {
        String basePathWithSep = tmp.toAbsolutePath().toString() + File.separator;

        when(commonService.listFilesUsingJavaIO(basePathWithSep))
                .thenReturn(Set.of());

        SymmetricNonogramLoggerInitializer subject =
                new SymmetricNonogramLoggerInitializer(commonService, new ObjectMapper(), basePathWithSep);

        subject.run();

        verify(commonService).listFilesUsingJavaIO(basePathWithSep);
    }

    @Test
    @DisplayName("Public constructor (default ObjectMapper & path) – sanity check")
    void publicConstructor_coversDefaultWiring() {
        SymmetricNonogramLoggerInitializer init = new SymmetricNonogramLoggerInitializer(commonService);
        assertThat(init).isNotNull();
    }
}
