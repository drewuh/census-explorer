package com.censusexplorer.client;

import java.util.List;

/**
 * Client interface for retrieving data from the US Census Bureau Data API.
 *
 * <p>All implementations must ensure that the Census API key is never exposed outside
 * the backend. Use this interface in service-layer tests to inject a mock implementation
 * rather than hitting the real API.
 */
public interface CensusApiClient {

  /**
   * Fetches raw population data rows from the Census Bureau API for the given year
   * and geographic scope.
   *
   * @param year      the data year to query (e.g. {@code "2022"})
   * @param geography the geographic level to query (e.g. {@code "state:*"})
   * @return a list of raw Census API response rows; never {@code null}
   */
  List<CensusApiResponse> fetchPopulationData(String year, String geography);
}
