package com.censusexplorer.controller;

import com.censusexplorer.dto.PopulationDto;
import com.censusexplorer.service.PopulationService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MockMvc integration tests for {@link PopulationController}.
 */
@WebMvcTest(PopulationController.class)
class PopulationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PopulationService populationService;

  // -------------------------------------------------------------------------
  // Success — 200
  // -------------------------------------------------------------------------

  @Test
  void getPopulationByState_validYear_returns200() throws Exception {
    when(populationService.getPopulationByState(eq("2022")))
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/population/states").param("year", "2022"))
        .andExpect(status().isOk());
  }

  @Test
  void getPopulationByState_validYear_returnsJsonContentType() throws Exception {
    when(populationService.getPopulationByState(eq("2022")))
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/population/states").param("year", "2022"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void getPopulationByState_validYear_responseContainsStateNameAndPopulationFields() throws Exception {
    List<PopulationDto> dtos = Arrays.asList(
        new PopulationDto("Alabama", 5024279L),
        new PopulationDto("California", 39538223L)
    );
    when(populationService.getPopulationByState(eq("2022"))).thenReturn(dtos);

    mockMvc.perform(get("/api/population/states").param("year", "2022"))
        .andExpect(jsonPath("$[0].stateName").value("Alabama"))
        .andExpect(jsonPath("$[0].population").value(5024279))
        .andExpect(jsonPath("$[1].stateName").value("California"))
        .andExpect(jsonPath("$[1].population").value(39538223));
  }

  @Test
  void getPopulationByState_emptyResult_returns200WithEmptyJsonArray() throws Exception {
    when(populationService.getPopulationByState(eq("2022")))
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/population/states").param("year", "2022"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }

  // -------------------------------------------------------------------------
  // Validation errors — 400
  // -------------------------------------------------------------------------

  @Test
  void getPopulationByState_serviceThrowsIllegalArgumentException_returns400() throws Exception {
    when(populationService.getPopulationByState(eq("2020")))
        .thenThrow(new IllegalArgumentException("Year 2020 is not available"));

    mockMvc.perform(get("/api/population/states").param("year", "2020"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getPopulationByState_missingYearParam_returns400() throws Exception {
    when(populationService.getPopulationByState(null))
        .thenThrow(new IllegalArgumentException("year must not be null or blank"));

    mockMvc.perform(get("/api/population/states"))
        .andExpect(status().isBadRequest());
  }

  // -------------------------------------------------------------------------
  // Unexpected errors — 500
  // -------------------------------------------------------------------------

  @Test
  void getPopulationByState_serviceThrowsRuntimeException_returns500() throws Exception {
    when(populationService.getPopulationByState(eq("2022")))
        .thenThrow(new RuntimeException("Upstream Census API unavailable"));

    mockMvc.perform(get("/api/population/states").param("year", "2022"))
        .andExpect(status().isInternalServerError());
  }
}
