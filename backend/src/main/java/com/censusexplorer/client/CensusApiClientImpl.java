package com.censusexplorer.client;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Default implementation of {@link CensusApiClient} that calls the live
 * US Census Bureau Data API using Spring's {@link WebClient}.
 *
 * <p>The Census API base URL and key are configured via {@code application.properties}.
 * This class must never be used directly in tests — inject {@link CensusApiClient}
 * and substitute a mock implementation instead.
 */
@Component
public class CensusApiClientImpl implements CensusApiClient {

  private final WebClient webClient;

  /**
   * Constructs a {@code CensusApiClientImpl} with the given {@link WebClient}.
   *
   * @param webClient the {@code WebClient} instance used for HTTP calls
   */
  public CensusApiClientImpl(WebClient webClient) {
    this.webClient = webClient;
  }

  /**
   * {@inheritDoc}
   *
   * @throws UnsupportedOperationException until this method is implemented
   */
  @Override
  public List<CensusApiResponse> fetchPopulationData(String year, String geography) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
