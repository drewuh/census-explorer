package com.censusexplorer.service;

import com.censusexplorer.client.CensusApiClient;
import com.censusexplorer.client.CensusApiResponse;
import com.censusexplorer.dto.PopulationDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service responsible for retrieving and transforming population data from the
 * US Census Bureau API.
 *
 * <p>All business logic related to population queries lives here. Controllers must
 * delegate to this service rather than calling {@link CensusApiClient} directly.
 *
 * <p>Valid years for ACS 1-Year Estimates are 2005 through 2024, inclusive, with the
 * exception of 2020, which was not released by the Census Bureau due to COVID-19
 * data collection disruptions. The valid year list is hardcoded; dynamic discovery
 * of available years is a future improvement.
 */
@Service
public class PopulationService {

  private static final int VALID_YEAR_MIN = 2005;
  private static final int VALID_YEAR_MAX = 2024;
  private static final int EXCLUDED_YEAR = 2020;

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
   * Returns population data for all geographic entities (50 states, DC, and Puerto Rico)
   * for the specified ACS 1-Year Estimate year, sorted alphabetically by name.
   *
   * @param year the data year to query (e.g. {@code "2022"}); must be a valid integer
   *             in the range 2005–2024 inclusive, and must not be {@code "2020"}
   * @return a list of {@link PopulationDto} objects sorted alphabetically by state name;
   *         never {@code null}, but may be empty if the Census API returns no data
   * @throws IllegalArgumentException if {@code year} is {@code null}, blank,
   *         non-numeric, equal to {@code "2020"}, or outside the range 2005–2024
   */
  public List<PopulationDto> getPopulationByState(String year) {
    validateYear(year);

    List<CensusApiResponse> responses =
        censusApiClient.fetchPopulationData(year, "state:*");

    List<PopulationDto> result = new ArrayList<>();
    for (CensusApiResponse response : responses) {
      PopulationDto dto = new PopulationDto();
      dto.setStateName(response.getName());
      dto.setPopulation(Long.parseLong(response.getPopulationEstimate()));
      result.add(dto);
    }

    Collections.sort(result, new Comparator<PopulationDto>() {
      @Override
      public int compare(PopulationDto a, PopulationDto b) {
        return a.getStateName().compareTo(b.getStateName());
      }
    });

    return result;
  }

  /**
   * Validates that the given year string is non-null, non-blank, numeric, and
   * within the supported ACS 1-Year range (2005–2024, excluding 2020).
   *
   * @param year the year string to validate
   * @throws IllegalArgumentException if the year fails any validation check
   */
  private void validateYear(String year) {
    if (year == null || year.trim().isEmpty()) {
      throw new IllegalArgumentException("year must not be null or blank");
    }

    int yearInt;
    try {
      yearInt = Integer.parseInt(year.trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "year must be a valid integer, but was: " + year);
    }

    if (yearInt == EXCLUDED_YEAR) {
      throw new IllegalArgumentException(
          "ACS 1-Year data for 2020 was not released by the Census Bureau");
    }

    if (yearInt < VALID_YEAR_MIN || yearInt > VALID_YEAR_MAX) {
      throw new IllegalArgumentException(
          "year must be between " + VALID_YEAR_MIN + " and " + VALID_YEAR_MAX
              + ", but was: " + year);
    }
  }
}
