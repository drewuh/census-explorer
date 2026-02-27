package com.censusexplorer.dto;

/**
 * Data transfer object representing population data for a single geographic entity,
 * returned to the frontend.
 *
 * <p>This DTO shields the frontend from the raw Census Bureau API response shape.
 * It is produced by the service layer and must never contain raw Census API fields.
 */
public class PopulationDto {

  private String stateName;
  private long population;

  /**
   * Constructs an empty {@code PopulationDto} with null state name and zero population.
   */
  public PopulationDto() {
  }

  /**
   * Constructs a {@code PopulationDto} with the given state name and population.
   *
   * @param stateName  the name of the geographic entity (e.g. {@code "California"})
   * @param population the total population estimate
   */
  public PopulationDto(String stateName, long population) {
    this.stateName = stateName;
    this.population = population;
  }

  /**
   * Returns the name of the geographic entity.
   *
   * @return the state name, or {@code null} if not set
   */
  public String getStateName() {
    return stateName;
  }

  /**
   * Sets the name of the geographic entity.
   *
   * @param stateName the state name to set
   */
  public void setStateName(String stateName) {
    this.stateName = stateName;
  }

  /**
   * Returns the total population estimate.
   *
   * @return the population count
   */
  public long getPopulation() {
    return population;
  }

  /**
   * Sets the total population estimate.
   *
   * @param population the population count to set
   */
  public void setPopulation(long population) {
    this.population = population;
  }
}
