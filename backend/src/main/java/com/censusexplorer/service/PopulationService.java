package com.censusexplorer.service;

import com.censusexplorer.client.CensusApiClient;
import com.censusexplorer.dto.PopulationDto;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service responsible for retrieving and transforming population data from the
 * US Census Bureau API.
 *
 * <p>All business logic related to population queries lives here. Controllers must
 * delegate to this service rather than calling {@link CensusApiClient} directly.
 */
@Service
public class PopulationService {

  private final CensusApiClient censusApiClient;

  /**
   * Constructs a {@code PopulationService} with the given Census API client.
   *
   * @param censusApiClient the client used to fetch raw Census Bureau data
   */
  public PopulationService(CensusApiClient censusApiClient) {
    this.censusApiClient = censusApiClient;
  }

  /**
   * Returns population data for all US states for the specified year.
   *
   * @param year the data year to query (e.g. {@code "2022"})
   * @return a list of {@link PopulationDto} objects, one per state; never {@code null}
   * @throws UnsupportedOperationException until this method is implemented
   */
  public List<PopulationDto> getPopulationByState(String year) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
