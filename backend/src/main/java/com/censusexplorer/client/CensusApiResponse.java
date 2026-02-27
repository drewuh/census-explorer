package com.censusexplorer.client;

/**
 * Represents a single data row returned by the US Census Bureau Data API.
 *
 * <p>The Census API returns a two-dimensional JSON array. The first row contains
 * column headers ({@code NAME}, {@code B01003_001E}, {@code state}); every subsequent
 * row is a data row. This class holds the parsed values of one data row before they
 * are mapped to a frontend-facing DTO.
 *
 * <p>All fields are stored as {@code String} because the Census API returns all values
 * as strings. Callers are responsible for parsing numeric fields as needed.
 */
public class CensusApiResponse {

  private String name;
  private String populationEstimate;
  private String stateCode;

  /**
   * Constructs an empty {@code CensusApiResponse} with all fields set to {@code null}.
   */
  public CensusApiResponse() {
  }

  /**
   * Constructs a {@code CensusApiResponse} with the given field values.
   *
   * @param name               the geographic entity name from the {@code NAME} column
   * @param populationEstimate the total population estimate from the {@code B01003_001E}
   *                           column, as a raw string (e.g. {@code "5024279"})
   * @param stateCode          the FIPS state code from the {@code state} column
   *                           (e.g. {@code "01"})
   */
  public CensusApiResponse(String name, String populationEstimate, String stateCode) {
    this.name = name;
    this.populationEstimate = populationEstimate;
    this.stateCode = stateCode;
  }

  /**
   * Returns the geographic entity name (Census {@code NAME} column).
   *
   * @return the name, or {@code null} if not set
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the geographic entity name.
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the raw population estimate string (Census {@code B01003_001E} column).
   *
   * @return the population estimate as a string, or {@code null} if not set
   */
  public String getPopulationEstimate() {
    return populationEstimate;
  }

  /**
   * Sets the raw population estimate string.
   *
   * @param populationEstimate the population estimate to set
   */
  public void setPopulationEstimate(String populationEstimate) {
    this.populationEstimate = populationEstimate;
  }

  /**
   * Returns the FIPS state code (Census {@code state} column).
   *
   * @return the state code, or {@code null} if not set
   */
  public String getStateCode() {
    return stateCode;
  }

  /**
   * Sets the FIPS state code.
   *
   * @param stateCode the state code to set
   */
  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }
}
