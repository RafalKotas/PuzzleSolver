package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicFactory;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.mapper.NonogramMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramLogicService;
import org.junit.jupiter.api.BeforeEach;
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
    void initializeNonogram_returnsLogic() throws Exception {
        NonogramInitializationRequest request = new NonogramInitializationRequest();
        NonogramLogic logic = new NonogramLogic();
        when(nonogramLogicService.initializeLogicFromRequest(any())).thenReturn(logic);

        mockMvc.perform(post("/api/nonogram/logic/initializeNonogram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void fillOverlappingColumnSequences_returnsUpdatedLogic() throws Exception {
        NonogramLogic logic = new NonogramLogic();
        when(nonogramLogicService.fillOverlappingFieldsInColumn(any(), eq(1))).thenReturn(logic);

        mockMvc.perform(post("/api/nonogram/logic/fillOverlappingColumnSequences/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logic)))
                .andExpect(status().isOk());
    }

    @Test
    void fillOverlappingColumnsSequencesRange_returnsModifiedLogic() throws Exception {
        when(nonogramLogicService.fillOverlappingFieldsInColumnsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        mockMvc.perform(post("/api/nonogram/logic/fillOverlappingColumnsSequences/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        verify(nonogramLogicService).fillOverlappingFieldsInColumnsRange(any(), eq(0), eq(1));
    }

    @Test
    void fillOverlappingRowsSequencesRange_returnsModifiedLogic() throws Exception {
        when(nonogramLogicService.fillOverLappingFieldsInRowsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        mockMvc.perform(post("/api/nonogram/logic/fillOverlappingRowsSequences/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        verify(nonogramLogicService).fillOverLappingFieldsInRowsRange(any(), eq(0), eq(1));
    }


    @Test
    void markRowSequencesRange_returnsUpdatedLogic() throws Exception {
        NonogramLogic logic = new NonogramLogic();
        when(nonogramLogicService.markAvailableSequencesInRows(any(), eq(0), eq(1))).thenReturn(logic);

        mockMvc.perform(post("/api/nonogram/logic/markRowsSequences/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logic)))
                .andExpect(status().isOk());
    }

    @Test
    void markColumnsSequencesRange_returnsModifiedLogic() throws Exception {
        when(nonogramLogicService.markAvailableSequencesInColumns(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        mockMvc.perform(post("/api/nonogram/logic/markColumnsSequences/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        verify(nonogramLogicService).markAvailableSequencesInColumns(any(), eq(0), eq(1));
    }

    @Test
    void placeXinRowsRange_returnsModifiedLogic() throws Exception {
        when(nonogramLogicService.placeXsAroundLongestSequencesInRowsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);
        when(nonogramLogicService.placeXsAtUnreachableFieldsInRowsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        mockMvc.perform(post("/api/nonogram/logic/placeXinRowsRange/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        verify(nonogramLogicService).placeXsAroundLongestSequencesInRowsRange(any(), eq(0), eq(1));
        verify(nonogramLogicService).placeXsAtUnreachableFieldsInRowsRange(any(), eq(0), eq(1));
    }

    @Test
    void placeXinColumnsRange_returnsModifiedLogic() throws Exception {
        when(nonogramLogicService.placeXsAroundLongestSequencesInColumnsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);
        when(nonogramLogicService.placeXsAtUnreachableFieldsInColumnsRange(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        mockMvc.perform(post("/api/nonogram/logic/placeXinColumnsRange/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        verify(nonogramLogicService).placeXsAroundLongestSequencesInColumnsRange(any(), eq(0), eq(1));
        verify(nonogramLogicService).placeXsAtUnreachableFieldsInColumnsRange(any(), eq(0), eq(1));
    }


    @Test
    void customSolutionPart_returnsMappedResponse() throws Exception {
        NonogramSolvePayload payload = new NonogramSolvePayload();
        NonogramLogic solvedLogic = new NonogramLogic();
        NonogramLogicResponse expectedResponse = new NonogramLogicResponse();

        when(nonogramLogicFactory.createFromPayload(any())).thenReturn(dummyLogic);
        when(nonogramLogicService.runSolverWithCorrectnessCheck(any(), eq("test-file"))).thenReturn(solvedLogic);

        try (MockedStatic<NonogramMapper> mocked = Mockito.mockStatic(NonogramMapper.class)) {
            mocked.when(() -> NonogramMapper.toResponse(solvedLogic)).thenReturn(expectedResponse);

            mockMvc.perform(post("/api/nonogram/logic/customSolutionPart")
                            .param("fileName", "test-file")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void saveSolution_returnsOkIfPass() throws Exception {
        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setFileName("someFile");

        FinalNonogramSolutionDTO result = new FinalNonogramSolutionDTO();
        result.setVerifiedAgainstOriginal("PASS");

        when(nonogramLogicService.saveIfCorrect(any())).thenReturn(result);

        mockMvc.perform(post("/api/nonogram/logic/saveIfCorrect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void saveSolution_returnsBadRequestIfFail() throws Exception {
        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setFileName("someFile");

        FinalNonogramSolutionDTO result = new FinalNonogramSolutionDTO();
        result.setVerifiedAgainstOriginal("FAIL");

        when(nonogramLogicService.saveIfCorrect(any())).thenReturn(result);

        mockMvc.perform(post("/api/nonogram/logic/saveIfCorrect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveSolution_returnsInternalServerErrorOnIOException() throws Exception {
        NonogramSolutionSaveRequest request = new NonogramSolutionSaveRequest();
        request.setFileName("someFile");

        when(nonogramLogicService.saveIfCorrect(any()))
                .thenThrow(new IOException("Simulated IO error"));

        mockMvc.perform(post("/api/nonogram/logic/saveIfCorrect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void compareWithSolution_returnsSolutionFromFile() throws Exception {
        NonogramLogic expectedLogic = new NonogramLogic();
        String fileName = "temp-test-file";

        Path solutionPath = Path.of(nonogramSolutionSavePathForFilename(fileName));
        Files.createDirectories(solutionPath.getParent());

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(solutionPath.toFile(), expectedLogic);

        mockMvc.perform(post("/api/nonogram/logic/compareWithSolution")
                        .param("fileName", fileName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new NonogramLogic())))
                .andExpect(status().isOk());

        Files.deleteIfExists(solutionPath);
    }

    @Test
    void compareWithSolution_returnsNotFoundWhenFileMissing() throws Exception {
        String nonExistingFileName = "definitely-does-not-exist-file";
        NonogramLogic dummy = new NonogramLogic();

        mockMvc.perform(post("/api/nonogram/logic/compareWithSolution")
                        .param("fileName", nonExistingFileName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummy)))
                .andExpect(status().isNotFound());
    }

    @Test
    void compareWithSolution_returnsInternalServerErrorOnInvalidJson() throws Exception {
        String fileName = "io-error-file";
        Path filePath = Path.of(nonogramSolutionSavePathForFilename(fileName));
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, "corrupted content"); // niepoprawny JSON

        NonogramLogic dummy = new NonogramLogic();

        mockMvc.perform(post("/api/nonogram/logic/compareWithSolution")
                        .param("fileName", fileName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummy)))
                .andExpect(status().isInternalServerError());

        Files.deleteIfExists(filePath);
    }

    @Test
    void correctRangesSequences_returnsModifiedLogic() throws Exception {
        when(nonogramLogicService.correctRowsSequencesRanges(any(), eq(0), eq(2)))
                .thenReturn(dummyLogic);
        when(nonogramLogicService.correctColumnsSequencesRanges(any(), eq(0), eq(2)))
                .thenReturn(dummyLogic);

        mockMvc.perform(post("/api/nonogram/logic/correctRanges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());
    }

    @Test
    void correctColumnsRangesSequences_returnsModifiedLogic() throws Exception {
        when(nonogramLogicService.correctColumnsSequencesRanges(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        mockMvc.perform(post("/api/nonogram/logic/correctColumnsRanges/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        verify(nonogramLogicService).correctColumnsSequencesRanges(any(), eq(0), eq(1));
    }

    @Test
    void correctRowsRangesSequences_returnsModifiedLogic() throws Exception {
        when(nonogramLogicService.correctRowsSequencesRanges(any(), eq(0), eq(1)))
                .thenReturn(dummyLogic);

        mockMvc.perform(post("/api/nonogram/logic/correctRowsRanges/0/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyLogic)))
                .andExpect(status().isOk());

        verify(nonogramLogicService).correctRowsSequencesRanges(any(), eq(0), eq(1));
    }
}
