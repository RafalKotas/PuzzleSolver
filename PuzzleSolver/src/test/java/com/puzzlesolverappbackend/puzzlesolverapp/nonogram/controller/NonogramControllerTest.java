package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFiltersResponse;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramFilterRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NonogramController.class)
@AutoConfigureMockMvc
class NonogramControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NonogramService nonogramService;
    @MockBean
    private NonogramRepository nonogramRepository;

    @Test
    void saveNonogramToJsonFile_returnsOk() throws Exception {
        String fileName = "test-nonogram.json";
        NonogramFileDetails details = new NonogramFileDetails(); // add fields if needed

        when(nonogramService.saveCreatedNonogramToFile(eq(fileName), any())).thenReturn("Saved successfully");

        mockMvc.perform(post("/api/nonogram/save")
                        .param("fileName", fileName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved successfully"));

        verify(nonogramService).saveCreatedNonogramToFile(eq(fileName), any());
    }

    @Test
    void getFilteredNonograms_returnsPageContent() throws Exception {
        NonogramFilterRequest filters = new NonogramFilterRequest(List.of("A"), List.of("2024"), List.of("01"),
                1.0, 3.0, 10, 20, 10, 20);

        List<Nonogram> mockNonograms = List.of(new Nonogram("f", "s", "y", "m", 2.0, 15, 15));
        when(nonogramService.getNonogramsFiltered(any(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(mockNonograms));

        mockMvc.perform(get("/api/nonogram/getNonogramsUsingFilters")
                        .param("sources", "A")
                        .param("years", "2024")
                        .param("months", "01")
                        .param("minDifficulty", "1.0")
                        .param("maxDifficulty", "3.0")
                        .param("minHeight", "10")
                        .param("maxHeight", "20")
                        .param("minWidth", "10")
                        .param("maxWidth", "20")
                        .param("page", "0")
                        .param("itemsOnPage", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].filename").value("f"));

        verify(nonogramService).getNonogramsFiltered(any(), any());
    }

    @Test
    void getNonogramFilters_returnsFilters() throws Exception {
        NonogramFiltersResponse response = new NonogramFiltersResponse(
                List.of("A"), List.of("2023"), List.of("01"), List.of(1.0), List.of(10), List.of(15)
        );

        when(nonogramService.getNonogramFilters()).thenReturn(response);

        mockMvc.perform(get("/api/nonogram/getFilters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sources[0]").value("A"))
                .andExpect(jsonPath("$.years[0]").value("2023"));

        verify(nonogramService).getNonogramFilters();
    }

    @Test
    void getNonogramsList_returnsList() throws Exception {
        List<Nonogram> list = List.of(new Nonogram("fn", "src", "y", "m", 2.0, 10, 10));

        when(nonogramRepository.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/nonogram/getNonogramsList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].filename").value("fn"));

        verify(nonogramRepository).findAll();
    }
}