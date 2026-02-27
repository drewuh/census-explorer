package com.censusexplorer.service;

import com.censusexplorer.client.CensusApiClient;
import com.censusexplorer.client.CensusApiResponse;
import com.censusexplorer.dto.PopulationDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PopulationService}.
 */
@ExtendWith(MockitoExtension.class)
class PopulationServiceTest {

  @Mock
  private CensusApiClient censusApiClient;

  @InjectMocks
  private PopulationService populationService;

  // -------------------------------------------------------------------------
  // Year validation — invalid inputs
  // -------------------------------------------------------------------------

  @Test
  void getPopulationByState_nullYear_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> populationService.getPopulationByState(null));
  }

  @Test
  void getPopulationByState_blankYear_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> populationService.getPopulationByState("   "));
  }

  @Test
  void getPopulationByState_emptyYear_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> populationService.getPopulationByState(""));
  }

  @Test
  void getPopulationByState_nonNumericYear_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> populationService.getPopulationByState("abc"));
  }

  @Test
  void getPopulationByState_year2020_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> populationService.getPopulationByState("2020"));
  }

  @Test
  void getPopulationByState_yearBefore2005_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> populationService.getPopulationByState("2004"));
  }

  @Test
  void getPopulationByState_yearAfter2024_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> populationService.getPopulationByState("2025"));
  }

  // -------------------------------------------------------------------------
  // Year validation — boundary years that are valid
  // -------------------------------------------------------------------------

  @Test
  void getPopulationByState_year2005_returnsResult() {
    when(censusApiClient.fetchPopulationData(eq("2005"), eq("state:*")))
        .thenReturn(Collections.emptyList());

    List<PopulationDto> result = populationService.getPopulationByState("2005");

    assertNotNull(result);
  }

  @Test
  void getPopulationByState_year2024_returnsResult() {
    when(censusApiClient.fetchPopulationData(eq("2024"), eq("state:*")))
        .thenReturn(Collections.emptyList());

    List<PopulationDto> result = populationService.getPopulationByState("2024");

    assertNotNull(result);
  }

  @Test
  void getPopulationByState_year2019_returnsResult() {
    when(censusApiClient.fetchPopulationData(eq("2019"), eq("state:*")))
        .thenReturn(Collections.emptyList());

    List<PopulationDto> result = populationService.getPopulationByState("2019");

    assertNotNull(result);
  }

  @Test
  void getPopulationByState_year2021_returnsResult() {
    when(censusApiClient.fetchPopulationData(eq("2021"), eq("state:*")))
        .thenReturn(Collections.emptyList());

    List<PopulationDto> result = populationService.getPopulationByState("2021");

    assertNotNull(result);
  }

  // -------------------------------------------------------------------------
  // Client invocation
  // -------------------------------------------------------------------------

  @Test
  void getPopulationByState_validYear_callsClientWithStateStarGeography() {
    when(censusApiClient.fetchPopulationData(eq("2022"), eq("state:*")))
        .thenReturn(Collections.emptyList());

    populationService.getPopulationByState("2022");

    verify(censusApiClient).fetchPopulationData("2022", "state:*");
  }

  // -------------------------------------------------------------------------
  // DTO mapping
  // -------------------------------------------------------------------------

  @Test
  void getPopulationByState_validYear_mapsCensusApiResponseToDto() {
    CensusApiResponse response = new CensusApiResponse("Alabama", "5024279", "01");
    when(censusApiClient.fetchPopulationData(eq("2022"), eq("state:*")))
        .thenReturn(Collections.singletonList(response));

    List<PopulationDto> result = populationService.getPopulationByState("2022");

    assertEquals(1, result.size());
    assertEquals("Alabama", result.get(0).getStateName());
    assertEquals(5024279L, result.get(0).getPopulation());
  }

  // -------------------------------------------------------------------------
  // Alphabetical sorting
  // -------------------------------------------------------------------------

  @Test
  void getPopulationByState_validYear_returnsSortedAlphabeticallyByStateName() {
    List<CensusApiResponse> unsortedResponses = Arrays.asList(
        new CensusApiResponse("California", "39538223", "06"),
        new CensusApiResponse("Alabama", "5024279", "01"),
        new CensusApiResponse("Wyoming", "576851", "56")
    );
    when(censusApiClient.fetchPopulationData(eq("2022"), eq("state:*")))
        .thenReturn(unsortedResponses);

    List<PopulationDto> result = populationService.getPopulationByState("2022");

    assertEquals("Alabama", result.get(0).getStateName());
    assertEquals("California", result.get(1).getStateName());
    assertEquals("Wyoming", result.get(2).getStateName());
  }

  // -------------------------------------------------------------------------
  // Empty result
  // -------------------------------------------------------------------------

  @Test
  void getPopulationByState_clientReturnsEmptyList_returnsEmptyList() {
    when(censusApiClient.fetchPopulationData(eq("2022"), eq("state:*")))
        .thenReturn(Collections.emptyList());

    List<PopulationDto> result = populationService.getPopulationByState("2022");

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
