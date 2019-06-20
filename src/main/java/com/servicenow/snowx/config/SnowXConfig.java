package com.servicenow.snowx.config;

import java.nio.file.Path;

public interface SnowXConfig {

    /**
     * Data file which is used to scan by default when no argument is supplied.
     */
    Path getInputFile();

    /**
     * Files used for identifying threats.
     */
    SnowXThreatDetails[] getThreats();

    /**
     * Character which indicates a 1 in the text grid (spaces are assumed to be 0).
     */
    char getBaseChar();

    /**
     * Determines whether or not to use multi-threaded search.
     */
    boolean isParallel();

    /**
     * Threshold for matching a target threat in the grid.
     */
    double getMatchingThreshold();

}
