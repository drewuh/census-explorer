package com.censusexplorer.client;

import java.util.Collections;
import java.util.List;

/**
 * Test-only stub implementation of {@link CensusApiClient}.
 *
 * <p>Returns empty collections for all methods. Use this class in service-layer
 * unit tests to avoid any real HTTP calls to the Census Bureau API. Extend or
 * configure it per test to return the data you need.
 */
public class MockCensusApiClient implements CensusApiClient {

  /**
   * Constructs a {@code MockCensusApiClient}.
   */
  public MockCensusApiClient() {
  }

  /**
   * Returns an empty list. Override in tests to supply fixture data.
   *
   * @param year      ignored
   * @param geography ignored
   * @return an empty, unmodifiable list
   */
  @Override
  public List<CensusApiResponse> fetchPopulationData(String year, String geography) {
    return Collections.emptyList();
  }
}
