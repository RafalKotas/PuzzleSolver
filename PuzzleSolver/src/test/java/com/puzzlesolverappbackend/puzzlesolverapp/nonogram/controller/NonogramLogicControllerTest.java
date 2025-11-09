package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicFactory;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.mapper.NonogramMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramLogicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.FileHelper.nonogramSolutionSavePathForFilename;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NonogramLogicControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NonogramLogicFactory nonogramLogicFactory;

    @Mock
    private NonogramLogicService nonogramLogicService;

    @InjectMocks
    private NonogramLogicController controller;

    private ObjectMapper objectMapper;

    NonogramLogic dummyLogic;
    NonogramRules rules;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        dummyLogic = new NonogramLogic();
        rules = new NonogramRules();
        rules.setHeight(2);
        rules.setWidth(2);
        dummyLogic.setNonogramRules(rules);
    }

    @Test
    @DisplayName("Should create NonogramLogic from POST method with NonogramInitializationRequest")
    void initializeNonogram_returnsLogic() throws Exception {
        // given
        NonogramInitializationRequest request = new NonogramInitializationRequest();
        NonogramLogic logic = new NonogramLogic();
        when(nonogramLogicService.initializeLogicFromRequest(any())).thenReturn(logic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/initializeNonogram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).initializeLogicFromRequest(any());
    }

    @Test
    @DisplayName("Should return updated NonogramLogic from POST method /api/nonogram/logic/fillOverlappingColumnSequences/1")
    void fillOverlappingColumnSequences_returnsUpdatedLogic() throws Exception {
        // given
        NonogramLogic logic = new NonogramLogic();
        when(nonogramLogicService.fillOverlappingFieldsInColumn(any(), eq(1))).thenReturn(logic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/fillOverlappingColumnSequences/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logic)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).fillOverlappingFieldsInColumn(any(), eq(1));
    }

    @Test
    @DisplayName("Should return updated NonogramLogic from POST method /api/nonogram/logic/fillOverlappingColumnsSequences/0/1")
    void fillOverlappingColumnsSequencesRange_returnsUpdatedLogic() throws Exception {
        // given
        when(nonogramLogicService.fillOverlappingFieldsInColumnsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/fillOverlappingColumnsSequences/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).fillOverlappingFieldsInColumnsRange(any(), eq(0), eq(1));
    }

    @Test
    @DisplayName("Should return updated NonogramLogic from POST method /fillOverlappingRowsSequences/0/1")
    void fillOverlappingRowsSequencesRange_returnsModifiedLogic() throws Exception {
        // given
        when(nonogramLogicService.fillOverLappingFieldsInRowsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/fillOverlappingRowsSequences/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).fillOverLappingFieldsInRowsRange(any(), eq(0), eq(1));
    }


    @Test
    @DisplayName("Should return updated NonogramLogic from POST method /markRowsSequences/0/1")
    void markRowSequencesRange_returnsUpdatedLogic() throws Exception {
        // given
        NonogramLogic logic = new NonogramLogic();
        when(nonogramLogicService.markAvailableSequencesInRows(any(), eq(0), eq(1))).thenReturn(logic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/markRowsSequences/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logic)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).markAvailableSequencesInRows(any(), eq(0), eq(1));
    }

    @Test
    @DisplayName("Should return updated NonogramLogic from POST method /api/nonogram/logic/markColumnsSequences/0/1")
    void markColumnsSequencesRange_returnsModifiedLogic() throws Exception {
        // given
        when(nonogramLogicService.markAvailableSequencesInColumns(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/markColumnsSequences/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).markAvailableSequencesInColumns(any(), eq(0), eq(1));
    }

    @Test
    @DisplayName("Should return updated NonogramLogic from POST method /api/nonogram/logic/placeXinRowsRange/0/1")
    void placeXinRowsRange_returnsModifiedLogic() throws Exception {
        // given
        when(nonogramLogicService.placeXsAroundLongestSequencesInRowsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);
        when(nonogramLogicService.placeXsAtUnreachableFieldsInRowsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/placeXinRowsRange/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).placeXsAroundLongestSequencesInRowsRange(any(), eq(0), eq(1));
        verify(nonogramLogicService).placeXsAtUnreachableFieldsInRowsRange(any(), eq(0), eq(1));
    }

    @Test
    @DisplayName("Should return updated NonogramLogic from POST method /api/nonogram/logic/placeXinColumnsRange/0/1")
    void placeXinColumnsRange_returnsModifiedLogic() throws Exception {
        // given
        when(nonogramLogicService.placeXsAroundLongestSequencesInColumnsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);
        when(nonogramLogicService.placeXsAtUnreachableFieldsInColumnsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/placeXinColumnsRange/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).placeXsAroundLongestSequencesInColumnsRange(any(), eq(0), eq(1));
        verify(nonogramLogicService).placeXsAtUnreachableFieldsInColumnsRange(any(), eq(0), eq(1));
    }

    @Test
    @DisplayName("Should return NonogramLogicResponse from POST method /api/nonogram/logic/customSolutionPart")
    void customSolutionPart_returnsMappedResponse() throws Exception {
        // given
        NonogramSolvePayload payload = new NonogramSolvePayload();
        NonogramLogic solvedLogic = new NonogramLogic();
        NonogramLogicResponse expectedResponse = new NonogramLogicResponse();

        when(nonogramLogicFactory.createFromPayload(any())).thenReturn(dummyLogic);
        when(nonogramLogicService.runSolverWithCorrectnessCheck(any(), eq("test-file"))).thenReturn(solvedLogic);

        // when
        try (MockedStatic<NonogramMapper> mocked = Mockito.mockStatic(NonogramMapper.class)) {
            mocked.when(() -> NonogramMapper.toResponse(solvedLogic)).thenReturn(expectedResponse);

            mockMvc.perform(post("/api/nonogram/logic/customSolutionPart")
                            .param("fileName", "test-file")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isOk());

            // then
            verify(nonogramLogicFactory).createFromPayload(any());
            verify(nonogramLogicService).runSolverWithCorrectnessCheck(any(), eq("test-file"));
            mocked.verify(() -> NonogramMapper.toResponse(solvedLogic));
        }
    }

    @Test
    @DisplayName("Should return 200 FinalNonogramSolutionDTO from POST method /api/nonogram/logic/saveIfCorrect when PASS")
    void saveSolution_returnsOkIfPass() throws Exception {
        // given
        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setFileName("someFile");

        FinalNonogramSolutionDTO result = new FinalNonogramSolutionDTO();
        result.setVerifiedAgainstOriginal("PASS");

        when(nonogramLogicService.saveIfCorrect(any())).thenReturn(result);

        // when
        mockMvc.perform(post("/api/nonogram/logic/saveIfCorrect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).saveIfCorrect(any());
    }

    @Test
    @DisplayName("Should return 400 from POST method /api/nonogram/logic/saveIfCorrect when FAIL")
    void saveSolution_returnsBadRequestIfFail() throws Exception {
        // given
        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setFileName("someFile");

        FinalNonogramSolutionDTO result = new FinalNonogramSolutionDTO();
        result.setVerifiedAgainstOriginal("FAIL");

        when(nonogramLogicService.saveIfCorrect(any())).thenReturn(result);

        // when
        mockMvc.perform(post("/api/nonogram/logic/saveIfCorrect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // then
        verify(nonogramLogicService).saveIfCorrect(any());
    }

    @Test
    @DisplayName("Should return 500 from POST method /api/nonogram/logic/saveIfCorrect when Exception thrown")
    void saveSolution_returnsInternalServerErrorOnIOException() throws Exception {
        // given
        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setFileName("someFile");

        when(nonogramLogicService.saveIfCorrect(any()))
                .thenThrow(new IOException("Simulated IO error"));

        // when
        mockMvc.perform(post("/api/nonogram/logic/saveIfCorrect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        // then
        verify(nonogramLogicService).saveIfCorrect(any());
    }

    @Test
    @DisplayName("Should return 200 from POST method /api/nonogram/logic/compareWithSolution when no Exception thrown")
    void compareWithSolution_returnsSolutionFromFile() throws Exception {
        // given
        NonogramLogic expectedLogic = new NonogramLogic();
        String fileName = "temp-test-file";

        Path solutionPath = Path.of(nonogramSolutionSavePathForFilename(fileName));
        Files.createDirectories(solutionPath.getParent());

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(solutionPath.toFile(), expectedLogic);

        NonogramLogic requestLogic = new NonogramLogic();

        try {
            // when
            mockMvc.perform(post("/api/nonogram/logic/compareWithSolution")
                            .param("fileName", fileName)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestLogic)))
                    .andExpect(status().isOk());

            // then - NO ASSERTIONS - not using service
        } finally {
            // cleanup
            Files.deleteIfExists(solutionPath);
        }
    }

    @Test
    @DisplayName("Should return 404 from POST method /api/nonogram/logic/compareWithSolution when solution file not found")
    void compareWithSolution_returnsNotFoundWhenFileMissing() throws Exception {
        // given
        String nonExistingFileName = "definitely-does-not-exist-file";
        NonogramLogic dummyLogic = new NonogramLogic();

        // when & then
        mockMvc.perform(post("/api/nonogram/logic/compareWithSolution")
                        .param("fileName", nonExistingFileName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 500 from POST method /api/nonogram/logic/compareWithSolution when reading solution file")
    void compareWithSolution_returnsInternalServerErrorOnInvalidJson() throws Exception {
        // given
        String fileName = "io-error-file";
        Path filePath = Path.of(nonogramSolutionSavePathForFilename(fileName));
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, "corrupted content"); // not valid JSON

        NonogramLogic dummyLogic = new NonogramLogic();

        // when & then
        mockMvc.perform(post("/api/nonogram/logic/compareWithSolution")
                        .param("fileName", fileName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isInternalServerError());

        // cleanup
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("Should return 200 from POST method /api/nonogram/logic/correctRanges")
    void correctRangesSequences_returnsModifiedLogic() throws Exception {
        // given
        when(nonogramLogicService.correctRowsSequencesRanges(any(), eq(0), eq(2)))
                .thenReturn(dummyLogic);
        when(nonogramLogicService.correctColumnsSequencesRanges(any(), eq(0), eq(2)))
                .thenReturn(dummyLogic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/correctRanges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).correctRowsSequencesRanges(any(), eq(0), eq(2));
        verify(nonogramLogicService).correctColumnsSequencesRanges(any(), eq(0), eq(2));
    }

    @Test
    @DisplayName("Should return 200 from POST method /api/nonogram/logic/correctColumnsRanges/0/1")
    void correctColumnsRangesSequences_returnsModifiedLogic() throws Exception {
        // given
        when(nonogramLogicService.correctColumnsSequencesRanges(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        // when & then
        mockMvc.perform(post("/api/nonogram/logic/correctColumnsRanges/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        verify(nonogramLogicService).correctColumnsSequencesRanges(any(), eq(0), eq(1));
    }

    @Test
    @DisplayName("Should return 200 from POST method /api/nonogram/logic/correctRowsRanges/2/3")
    void correctRowsRangesSequences_returnsModifiedLogic() throws Exception {
        // given
        when(nonogramLogicService.correctRowsSequencesRanges(any(), eq(2), eq(3)))
                .thenReturn(dummyLogic);

        // when
        mockMvc.perform(post("/api/nonogram/logic/correctRowsRanges/2/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        // then
        verify(nonogramLogicService).correctRowsSequencesRanges(any(), eq(2), eq(3));
    }
}
