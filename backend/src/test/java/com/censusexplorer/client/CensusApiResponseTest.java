package com.censusexplorer.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link CensusApiResponse}.
 */
class CensusApiResponseTest {

  @Test
  void noArgConstructor_created_hasNullFields() {
    CensusApiResponse response = new CensusApiResponse();

    assertNull(response.getName());
    assertNull(response.getPopulationEstimate());
    assertNull(response.getStateCode());
  }

  @Test
  void allArgsConstructor_created_hasCorrectValues() {
    CensusApiResponse response = new CensusApiResponse("Alabama", "5024279", "01");

    assertEquals("Alabama", response.getName());
    assertEquals("5024279", response.getPopulationEstimate());
    assertEquals("01", response.getStateCode());
  }

  @Test
  void setName_setToValue_updatesField() {
    CensusApiResponse response = new CensusApiResponse();
    response.setName("California");

    assertEquals("California", response.getName());
  }

  @Test
  void setPopulationEstimate_setToValue_updatesField() {
    CensusApiResponse response = new CensusApiResponse();
    response.setPopulationEstimate("39538223");

    assertEquals("39538223", response.getPopulationEstimate());
  }

  @Test
  void setStateCode_setToValue_updatesField() {
    CensusApiResponse response = new CensusApiResponse();
    response.setStateCode("06");

    assertEquals("06", response.getStateCode());
  }
}
