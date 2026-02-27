package com.censusexplorer.controller;

import com.censusexplorer.dto.PopulationDto;
import com.censusexplorer.service.PopulationService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for population data endpoints.
 *
 * <p>This controller is intentionally thin — all business logic and validation
 * live in {@link PopulationService}. Exception-to-status mapping is handled by
 * {@code GlobalExceptionHandler}.
 */
@RestController
@RequestMapping("/api/population")
public class PopulationController {

  private final PopulationService populationService;

  /**
   * Constructs a {@code PopulationController} with the given population service.
   *
   * @param populationService the service used to retrieve population data
   */
  public PopulationController(PopulationService populationService) {
    this.populationService = populationService;
  }

  /**
   * Returns population data for all geographic entities (50 states, DC, and Puerto Rico)
   * for the given ACS 1-Year Estimate year.
   *
   * @param year the census year to query (e.g. {@code "2022"}); if absent, the service
   *             will receive {@code null} and throw an {@link IllegalArgumentException},
   *             resulting in a 400 response
   * @return 200 with a JSON array of {@link PopulationDto} objects on success;
   *         400 if the year is invalid; 500 on unexpected errors
   */
  @GetMapping("/states")
  public ResponseEntity<List<PopulationDto>> getPopulationByState(
      @RequestParam(required = false) String year) {
    List<PopulationDto> result = populationService.getPopulationByState(year);
    return ResponseEntity.ok(result);
  }
}
