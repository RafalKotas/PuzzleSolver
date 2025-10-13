package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service;

import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.exception.NonogramFileReadException;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.exception.NonogramFileSaveException;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFiltersResponse;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramFilterRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramCorrectnessIndicator;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.json.NonogramJsonWriter;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.json.NonogramJsonWriter.saveSolutionBoard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NonogramServiceTest {

    @TempDir
    Path tempDir;

    private NonogramService subject;

    @Mock
    private CommonService commonService; // not used in this test path but required by ctor

    @Mock
    private NonogramRepository repository;

    @BeforeEach
    void setUp() {
        subject = new NonogramService(commonService, repository);
    }

    @Test
    @DisplayName("getNonogramsFiltered should delegate to repository.findAll with a Specification and the given Pageable")
    void getNonogramsFiltered_delegatesToRepository() {
        // given
        NonogramService service = new NonogramService(commonService, repository);

        // simple, non-empty filters just to exercise the path
        NonogramFilterRequest filters = new NonogramFilterRequest(
                List.of("logi", "example"),             // sources
                List.of("2024"),                         // years
                List.of("01", "02"),                     // months
                1.0, 5.0,                                // min/max difficulty
                5, 60,                                   // min/max height
                5, 85                                    // min/max width
        );

        PageRequest pageable = PageRequest.of(0, 20);
        Page<Nonogram> expectedPage = new PageImpl<>(List.of()); // content doesn't matter

        when(repository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(expectedPage);

        // when
        Page<Nonogram> result = service.getNonogramsFiltered(filters, pageable);

        // then
        assertThat(result).isSameAs(expectedPage);

        // capture & verify repository call
        ArgumentCaptor<Specification<Nonogram>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(repository).findAll(specCaptor.capture(), eq(pageable));

        // basic sanity: specification should not be null
        Specification<Nonogram> passedSpec = specCaptor.getValue();
        assertThat(passedSpec).isNotNull();
    }

    @Test
    @DisplayName("getNonogramFilters aggregates, sorts and infers ranges correctly")
    void getNonogramFilters_returnsAggregatedAndNormalizedValues() {
        // given (unsorted inputs from repository to verify service-side sorting)
        when(repository.selectNonogramSources())
                .thenReturn(List.of("logiMix", "logi", "example", "pazyl_pl", "src", "katana"));
        when(repository.selectNonogramYears())
                .thenReturn(List.of("2025", "2018", "N/D", "2024", "2021"));
        when(repository.selectNonogramMonths())
                .thenReturn(List.of("11", "N/D", "01", "02"));
        when(repository.selectNonogramDifficulties())
                .thenReturn(List.of(5.0, 1.0)); // min/max in reverse order
        when(repository.selectNonogramHeights())
                .thenReturn(List.of(60, 5));    // min/max in reverse order
        when(repository.selectNonogramWidths())
                .thenReturn(List.of(85, 5));    // min/max in reverse order

        // when
        NonogramFiltersResponse nonogramFiltersResponse = subject.getNonogramFilters();

        // then
        // -- sources sorted
        assertThat(nonogramFiltersResponse.getSources())
                .containsExactly("example", "katana", "logi", "logiMix", "pazyl_pl", "src");

        // -- years sorted lexicographically (N/D at the end)
        assertThat(nonogramFiltersResponse.getYears())
                .containsExactly("2018", "2021", "2024", "2025", "N/D");

        // -- months sorted
        assertThat(nonogramFiltersResponse.getMonths())
                .containsExactly("01", "02", "11", "N/D");

        // -- difficulties inferred as [min, max]
        assertThat(nonogramFiltersResponse.getDifficulties())
                .containsExactly(1.0, 5.0);

        // -- heights range inferred [min, max]
        assertThat(nonogramFiltersResponse.getHeights())
                .containsExactly(5, 60);

        // -- widths range inferred [min, max]
        assertThat(nonogramFiltersResponse.getWidths())
                .containsExactly(5, 85);

        // and repository methods were called once
        verify(repository, times(1)).selectNonogramSources();
        verify(repository, times(1)).selectNonogramYears();
        verify(repository, times(1)).selectNonogramMonths();
        verify(repository, times(1)).selectNonogramDifficulties();
        verify(repository, times(1)).selectNonogramHeights();
        verify(repository, times(1)).selectNonogramWidths();
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("getNonogramFilters falls back to defaults when lists too short")
    void getNonogramFilters_usesDefaultsWhenNotEnoughData() {
        // given: empty/short lists -> service should use defaults
        when(repository.selectNonogramSources()).thenReturn(List.of());
        when(repository.selectNonogramYears()).thenReturn(List.of());
        when(repository.selectNonogramMonths()).thenReturn(List.of());
        when(repository.selectNonogramDifficulties()).thenReturn(List.of(1.5)); // only one value
        when(repository.selectNonogramHeights()).thenReturn(List.of(8));        // only one value
        when(repository.selectNonogramWidths()).thenReturn(List.of());          // none

        // when
        NonogramFiltersResponse resp = subject.getNonogramFilters();

        // then
        // sources/years/months are just sorted empty lists
        assertThat(resp.getSources()).isEmpty();
        assertThat(resp.getYears()).isEmpty();
        assertThat(resp.getMonths()).isEmpty();

        // difficulties default range [1.0, 2.0] (per service constants)
        assertThat(resp.getDifficulties()).containsExactly(1.0, 2.0);

        // heights default [5, 10], widths default [5, 10]
        assertThat(resp.getHeights()).containsExactly(5, 10);
        assertThat(resp.getWidths()).containsExactly(5, 10);
    }

    @Test
    @DisplayName("Should return INVALID_DIMENSIONS_ROWS if height is less than rowSequences size")
    void shouldReturnInvalidDimensionsRowsHeightLessThanRowSequencesSize() {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        nonogramFileDetails.setHeight(2);

        // when
        NonogramCorrectnessIndicator nonogramCorrectnessIndicator = subject.checkNonogramCorrectness(nonogramFileDetails);

        // then
        assertThat(nonogramCorrectnessIndicator).isEqualTo(NonogramCorrectnessIndicator.INVALID_DIMENSIONS_ROWS);
    }

    @Test
    @DisplayName("Should return INVALID_DIMENSIONS_ROWS if height is greater than rowSequences size")
    void shouldReturnInvalidDimensionsRowsHeightGreaterThanRowSequencesSize() {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        nonogramFileDetails.setHeight(4);

        // when
        NonogramCorrectnessIndicator nonogramCorrectnessIndicator = subject.checkNonogramCorrectness(nonogramFileDetails);

        // then
        assertThat(nonogramCorrectnessIndicator).isEqualTo(NonogramCorrectnessIndicator.INVALID_DIMENSIONS_ROWS);
    }

    @Test
    @DisplayName("Should return INVALID_DIMENSIONS_COLUMNS if width is less than columnSequences size")
    void shouldReturnInvalidDimensionsColumnsWidthLessThanColumnSequencesSize() {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        nonogramFileDetails.setWidth(2);

        // when
        NonogramCorrectnessIndicator nonogramCorrectnessIndicator = subject.checkNonogramCorrectness(nonogramFileDetails);

        // then
        assertThat(nonogramCorrectnessIndicator).isEqualTo(NonogramCorrectnessIndicator.INVALID_DIMENSIONS_COLUMNS);
    }

    @Test
    @DisplayName("Should return INVALID_DIMENSIONS_COLUMNS if height is greater than rowSequences size")
    void shouldReturnInvalidDimensionsColumnsHeightGreaterThanColumnSequencesSize() {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        nonogramFileDetails.setWidth(4);

        // when
        NonogramCorrectnessIndicator nonogramCorrectnessIndicator = subject.checkNonogramCorrectness(nonogramFileDetails);

        // then
        assertThat(nonogramCorrectnessIndicator).isEqualTo(NonogramCorrectnessIndicator.INVALID_DIMENSIONS_COLUMNS);
    }

    @Test
    @DisplayName("Should return TOO_LONG_ROW_SEQUENCE if sum of lengths in row greater than width")
    void shouldReturnTooLongRowSequenceIfSumOfLengthsInRowGreaterThanWidth() {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        List<Integer> wrongRowSequences = new ArrayList<>(List.of(4));
        nonogramFileDetails.getRowSequences().set(0, wrongRowSequences);

        // when
        NonogramCorrectnessIndicator nonogramCorrectnessIndicator = subject.checkNonogramCorrectness(nonogramFileDetails);

        // then
        assertThat(nonogramCorrectnessIndicator).isEqualTo(NonogramCorrectnessIndicator.TOO_LONG_ROW_SEQUENCE);
    }

    @Test
    @DisplayName("Should return TOO_LONG_COLUMN_SEQUENCE if sum of lengths in column greater than height")
    void shouldReturnTooLongColumnSequenceIfSumOfLengthsInColumnGreaterThanHeight() {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        List<Integer> wrongColumnSequences = new ArrayList<>(List.of(4));
        nonogramFileDetails.getColumnSequences().set(0, wrongColumnSequences);

        // when
        NonogramCorrectnessIndicator nonogramCorrectnessIndicator = subject.checkNonogramCorrectness(nonogramFileDetails);

        // then
        assertThat(nonogramCorrectnessIndicator).isEqualTo(NonogramCorrectnessIndicator.TOO_LONG_COLUMN_SEQUENCE);
    }
    @Test
    @DisplayName("Should return SUM_MISMATCH_ROWS_COLUMNS if sum of lengths in rows not equals sum of lengths in columns")
    void shouldReturnSumMismatchRowColumnsIfSumOfLengthsInRowsNotEqualsSumOfLengthsInColumns() {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        List<Integer> wrongRowSequences = new ArrayList<>(List.of(0));
        nonogramFileDetails.getRowSequences().set(0, wrongRowSequences);

        // when
        NonogramCorrectnessIndicator nonogramCorrectnessIndicator = subject.checkNonogramCorrectness(nonogramFileDetails);

        // then
        assertThat(nonogramCorrectnessIndicator).isEqualTo(NonogramCorrectnessIndicator.SUM_MISMATCH_ROWS_COLUMNS);
    }

    @Test
    @DisplayName("Should return VALID nonogramCorrectnessIndicator")
    void shouldReturnValid() {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();

        // when
        NonogramCorrectnessIndicator nonogramCorrectnessIndicator = subject.checkNonogramCorrectness(nonogramFileDetails);

        // then
        assertThat(nonogramCorrectnessIndicator).isEqualTo(NonogramCorrectnessIndicator.VALID);
    }

    private NonogramFileDetails generateNonogramFileDetailsForTest() {
        NonogramFileDetails nonogramFileDetails = new NonogramFileDetails();
        int height = 3;
        int width = 3;
        List<List<Integer>> rowsSequences = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(3)),
                        new ArrayList<>(List.of(1, 1)),
                        new ArrayList<>(List.of(1, 1))
                )
        );
        List<List<Integer>> columnsSequences = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(3)),
                        new ArrayList<>(List.of(1, 1)),
                        new ArrayList<>(List.of(1, 1))
                )
        );
        nonogramFileDetails.setHeight(height);
        nonogramFileDetails.setWidth(width);
        nonogramFileDetails.setRowSequences(rowsSequences);
        nonogramFileDetails.setColumnSequences(columnsSequences);

        return nonogramFileDetails;
    }

    @Test
    @DisplayName("Should not save when file already exists")
    void fileAlreadyExists() {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        when(commonService.listFilesUsingJavaIO(any()))
                .thenReturn(Set.of("existing.json"));

        // when
        String result = subject.saveCreatedNonogramToFile("existing", nonogramFileDetails);

        // then
        assertThat(result).isEqualTo("Nonogram not saved. File with same name already exists.");

        // verify no call to writer
    }

    @Test
    @DisplayName("Should return exception message when writeToFile throws IOException")
    void writerThrowsIOException() throws Exception {
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        when(commonService.listFilesUsingJavaIO(any()))
                .thenReturn(Set.of("other.json"));

        try (MockedStatic<NonogramJsonWriter> writerMock = mockStatic(NonogramJsonWriter.class)) {
            writerMock.when(() -> NonogramJsonWriter.writeToFile(any(), anyString()))
                    .thenThrow(new IOException("disk full"));

            String result = subject.saveCreatedNonogramToFile("newfile", nonogramFileDetails);

            assertThat(result).isEqualTo("Exception external problem.");
            writerMock.verify(() -> NonogramJsonWriter.writeToFile(any(), contains("newfile.json")));
        }
    }

    @Test
    @DisplayName("Should save successfully when file does not exist and writer works")
    void writerSucceeds() throws Exception {
        // given
        NonogramFileDetails nonogramFileDetails = generateNonogramFileDetailsForTest();
        when(commonService.listFilesUsingJavaIO(any()))
                .thenReturn(Set.of("somethingElse.json"));

        // when
        try (MockedStatic<NonogramJsonWriter> writerMock = mockStatic(NonogramJsonWriter.class)) {
            writerMock.when(() -> NonogramJsonWriter.writeToFile(any(), anyString()))
                    .thenAnswer(inv -> null); // do nothing

            String result = subject.saveCreatedNonogramToFile("brandNew", nonogramFileDetails);

            // then
            assertThat(result).isEqualTo("Save success!");
            writerMock.verify(() -> NonogramJsonWriter.writeToFile(any(), contains("brandNew.json")));
        }
    }

    @Test
    @DisplayName("Should parse JSON file into NonogramFileDetails (happy path)")
    void readsJsonSuccessfully() throws Exception {
        // given
        String json = """
                {
                  "rowSequences": [[1],[2]],
                  "columnSequences": [[1],[2]],
                  "filename": "foo.json",
                  "height": 2,
                  "width": 2,
                  "source": "logi",
                  "year": "2024",
                  "month": "11",
                  "difficulty": 1.0,
                  "additionalContent": null
                }
                """;
        File file = tempDir.resolve("foo.json").toFile();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(json);
        }

        // when
        NonogramFileDetails details = subject.getNonogramDetailsFromFile(file.getAbsolutePath());

        // then
        assertThat(details).isNotNull();
        assertThat(details.getFilename()).isEqualTo("foo.json");
        assertThat(details.getHeight()).isEqualTo(2);
        assertThat(details.getWidth()).isEqualTo(2);
        assertThat(details.getRowSequences()).hasSize(2);
        assertThat(details.getRowSequences().get(0)).containsExactly(1);
        assertThat(details.getColumnSequences().get(1)).containsExactly(2);
        assertThat(details.getSource()).isEqualTo("logi");
        assertThat(details.getDifficulty()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("Should throw NonogramFileReadException when file cannot be read")
    void throwsWhenFileMissing() {
        // given
        String path = tempDir.resolve("missing.json").toString();

        // when
        NonogramFileReadException ex =
                assertThrows(NonogramFileReadException.class, () -> subject.getNonogramDetailsFromFile(path));

         // then
        assertThat(ex.getMessage()).contains(path);
        assertThat(ex.getCause()).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should save solution board successfully")
    void savesSuccessfully() throws Exception {
        try (MockedStatic<NonogramJsonWriter> mocked =
                     mockStatic(NonogramJsonWriter.class)) {
            // given
            List<List<String>> board = sampleBoard();

            // when
            subject.saveSolutionToFile("myFile", board);

            // then
            mocked.verify(() -> saveSolutionBoard(eq(board), contains("myFile")), times(1));
        }
    }

    @Test
    @DisplayName("Should throw NonogramFileSaveException when IOException occurs")
    void throwsWhenIOException() throws Exception {
        try (MockedStatic<NonogramJsonWriter> mocked =
                     mockStatic(NonogramJsonWriter.class)) {
            // given
            List<List<String>> board = sampleBoard();

            mocked.when(() -> saveSolutionBoard(eq(board), anyString()))
                    .thenThrow(new IOException("disk full"));

            // when / then
            assertThrows(NonogramFileSaveException.class,
                    () -> subject.saveSolutionToFile("badFile", board));

            mocked.verify(() -> saveSolutionBoard(eq(board), contains("badFile")), times(1));
        }
    }

    private static List<List<String>> sampleBoard() {
        return new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("X", "O", "X")),
                new ArrayList<>(Arrays.asList("O", "X", "O"))
        ));
    }
}