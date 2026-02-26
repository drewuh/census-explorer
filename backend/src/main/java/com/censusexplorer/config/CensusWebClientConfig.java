package com.censusexplorer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spring configuration that provides a {@link WebClient} bean pre-configured
 * with the US Census Bureau Data API base URL.
 */
@Configuration
public class CensusWebClientConfig {

  /** Base URL for the US Census Bureau Data API. */
  private static final String CENSUS_API_BASE_URL = "https://api.census.gov/data";

  /**
   * Creates a {@link WebClient} instance pointing at the Census Bureau API.
   *
   * @param builder the auto-configured {@link WebClient.Builder} provided by Spring Boot
   * @return a configured {@code WebClient}
   */
  @Bean
  public WebClient censusWebClient(WebClient.Builder builder) {
    return builder.baseUrl(CENSUS_API_BASE_URL).build();
  }
}
