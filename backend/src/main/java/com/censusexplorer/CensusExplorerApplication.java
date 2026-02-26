package com.censusexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Census Explorer Spring Boot application.
 *
 * <p>This application exposes a REST API that proxies and transforms data from
 * the US Census Bureau Data API, targeting municipal planners and researchers.
 */
@SpringBootApplication
public class CensusExplorerApplication {

  /**
   * Launches the Spring Boot application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(CensusExplorerApplication.class, args);
  }
}
