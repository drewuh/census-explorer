package com.censusexplorer.client;

/**
 * Represents a single data row returned by the US Census Bureau Data API.
 *
 * <p>This is an intermediate type used by {@link CensusApiClient} to capture the raw
 * Census API response before it is mapped to a frontend-facing DTO. Fields will be
 * added when the first feature that requires this data is implemented.
 */
public class CensusApiResponse {

  /**
   * Constructs an empty {@code CensusApiResponse}.
   */
  public CensusApiResponse() {
  }
}
