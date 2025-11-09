package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFiltersResponse;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramFilterRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NonogramControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private NonogramService nonogramService;

    @InjectMocks
    private NonogramController nonogramController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(nonogramController).build();
    }

    @Test
    @DisplayName("Should return OK status when POST saving nonogram to json file")
    void saveNonogramToJsonFile_returnsOk() throws Exception {
        // given
        String fileName = "test-nonogram.json";
        NonogramFileDetails details = new NonogramFileDetails();

        when(nonogramService.saveCreatedNonogramToFile(eq(fileName), any()))
                .thenReturn("Saved successfully");

        // when
        mockMvc.perform(post("/api/nonogram/save")
                        .param("fileName", fileName)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved successfully"));

        // then
        verify(nonogramService).saveCreatedNonogramToFile(eq(fileName), any());
    }

    @Test
    @DisplayName("Should return OK status when GET nonograms by filters from")
    void getFilteredNonograms_returnsPageContent() throws Exception {
        // given
        doAnswer(invocation -> {
            NonogramFilterRequest f = invocation.getArgument(0);
            System.out.println(">>> FILTERS: " + f);
            return new PageImpl<>(List.of(new Nonogram("f", "s", "y", "m", 2.0, 15, 15)));
        }).when(nonogramService).getNonogramsFiltered(any(), any(PageRequest.class));

        // when
        mockMvc.perform(get("/api/nonogram/getNonogramsUsingFilters")
                        .param("sources", "A")
                        .param("years", "2024")
                        .param("months", "01")
                        .param("minDifficulty", "1.0")
                        .param("maxDifficulty", "3.0")
                        .param("minHeight", "10")
                        .param("maxHeight", "20")
                        .param("minWidth", "15")
                        .param("maxWidth", "25")
                        .param("page", "0")
                        .param("itemsOnPage", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].filename").value("f"));

        // then
        ArgumentCaptor<NonogramFilterRequest> filterCaptor = ArgumentCaptor.forClass(NonogramFilterRequest.class);
        ArgumentCaptor<PageRequest> pageCaptor = ArgumentCaptor.forClass(PageRequest.class);

        verify(nonogramService).getNonogramsFiltered(filterCaptor.capture(), pageCaptor.capture());

        NonogramFilterRequest filters = filterCaptor.getValue();
        PageRequest page = pageCaptor.getValue();

        assertThat(filters.getSources()).containsAll(List.of("A"));
        assertThat(filters.getYears()).containsAll(List.of("2024"));
        assertThat(filters.getMonths()).containsAll(List.of("01"));
        assertEquals(1.0, filters.getMinDifficulty());
        assertEquals(3.0, filters.getMaxDifficulty());
        assertEquals(10, filters.getMinHeight());
        assertEquals(20, filters.getMaxHeight());
        assertEquals(15, filters.getMinWidth());
        assertEquals(25, filters.getMaxWidth());

        assertEquals(0, page.getPageNumber());
        assertEquals(3, page.getPageSize());
    }

    @Test
    @DisplayName("Should GET all nonogram filters")
    void getNonogramFilters_returnsFilters() throws Exception {
        // given
        NonogramFiltersResponse response = new NonogramFiltersResponse(
                List.of("A"), List.of("2023"), List.of("01"), List.of(1.0), List.of(10), List.of(15)
        );

        when(nonogramService.getNonogramFilters()).thenReturn(response);

        // when
        mockMvc.perform(get("/api/nonogram/getFilters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sources[0]").value("A"))
                .andExpect(jsonPath("$.years[0]").value("2023"));

        // then
        verify(nonogramService).getNonogramFilters();
    }

    @Test
    @DisplayName("Should GET all nonograms list")
    void getNonogramsList_returnsList() throws Exception {
        // given
        List<Nonogram> list = List.of(new Nonogram("fn", "src", "y", "m", 2.0, 10, 10));

        when(nonogramService.getNonogramsList()).thenReturn(list);

        // when
        mockMvc.perform(get("/api/nonogram/getNonogramsList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].filename").value("fn"));

        // then
        verify(nonogramService).getNonogramsList();
    }
}
