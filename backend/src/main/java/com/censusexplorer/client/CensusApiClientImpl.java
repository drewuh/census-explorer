package com.censusexplorer.client;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Default implementation of {@link CensusApiClient} that calls the live
 * US Census Bureau Data API using Spring's {@link WebClient}.
 *
 * <p>The Census API base URL is configured via {@code CensusWebClientConfig}.
 * The API key is read from the {@code census.api.key} property. If the key is
 * absent or still set to the placeholder value {@code REPLACE_ME}, requests are
 * made without a key and a warning is logged. This is intended for local
 * development only — production deployments must supply a valid key.
 *
 * <p>This class must never be used directly in tests — inject {@link CensusApiClient}
 * and substitute {@code MockCensusApiClient} instead.
 */
@Component
public class CensusApiClientImpl implements CensusApiClient {

  private static final Logger log = LoggerFactory.getLogger(CensusApiClientImpl.class);

  private static final String PLACEHOLDER_KEY = "REPLACE_ME";

  private final WebClient webClient;
  private final String censusApiKey;

  /**
   * Constructs a {@code CensusApiClientImpl} with the given {@link WebClient} and API key.
   *
   * @param webClient     the {@code WebClient} instance used for HTTP calls
   * @param censusApiKey  the Census Bureau API key from {@code census.api.key};
   *                      defaults to an empty string if the property is absent
   */
  public CensusApiClientImpl(
      WebClient webClient,
      @Value("${census.api.key:}") String censusApiKey) {
    this.webClient = webClient;
    this.censusApiKey = censusApiKey;
  }

  /**
   * Fetches ACS 1-Year population data from the Census Bureau API for the given
   * year and geography, mapping each row to a {@link CensusApiResponse}.
   *
   * <p>The Census API responds with a JSON array-of-arrays. The first element is
   * the header row and is skipped; each subsequent element contains
   * {@code [NAME, B01003_001E, state]} in that column order.
   *
   * @param year       the ACS survey year (e.g. {@code "2023"})
   * @param geography  the Census geography string (e.g. {@code "state:*"})
   * @return a list of {@link CensusApiResponse} objects, one per geographic entity
   */
  @Override
  public List<CensusApiResponse> fetchPopulationData(String year, String geography) {
    boolean useKey = censusApiKey != null
        && !censusApiKey.isBlank()
        && !censusApiKey.equals(PLACEHOLDER_KEY);

    if (!useKey) {
      log.warn("census.api.key is missing or placeholder — making Census API request without a key."
          + " Set a valid key in application.properties for production use.");
    }

    String uri = buildUri(year, geography, useKey);

    String[][] rawResponse = webClient.get()
        .uri(uri)
        .retrieve()
        .bodyToMono(String[][].class)
        .block();

    List<CensusApiResponse> result = new ArrayList<>();
    if (rawResponse != null) {
      for (int i = 1; i < rawResponse.length; i++) {
        String[] row = rawResponse[i];
        result.add(new CensusApiResponse(row[0], row[1], row[2]));
      }
    }
    return result;
  }

  /**
   * Builds the relative URI for the Census API request.
   *
   * @param year      the survey year
   * @param geography the geography string
   * @param useKey    whether to append the API key query parameter
   * @return the relative URI string
   */
  private String buildUri(String year, String geography, boolean useKey) {
    StringBuilder sb = new StringBuilder();
    sb.append("/").append(year).append("/acs/acs1");
    sb.append("?get=NAME,B01003_001E");
    sb.append("&for=").append(geography);
    if (useKey) {
      sb.append("&key=").append(censusApiKey);
    }
    return sb.toString();
  }
}
