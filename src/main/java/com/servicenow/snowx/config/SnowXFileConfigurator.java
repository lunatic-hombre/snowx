package com.servicenow.snowx.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Supplier;

public class SnowXFileConfigurator implements Supplier<SnowXConfig> {

    private final String propertiesFile;

    public SnowXFileConfigurator(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    @Override
    public SnowXConfig get() {
        try {
            Properties properties = new Properties();
            try (Reader reader = Files.newBufferedReader(Paths.get(propertiesFile))) {
                properties.load(reader);
            }
            return new SnowXConfig() {
                @Override
                public Path getInputFile() {
                    return Paths.get(properties.getProperty("input.file").trim());
                }

                @Override
                public SnowXThreatDetails[] getThreats() {
                    return Arrays.stream(properties.getProperty("threats").split("\\s*,\\s*"))
                            .map(str -> SnowXThreatDetails.parse(str))
                            .toArray(SnowXThreatDetails[]::new);
                }

                @Override
                public char getBaseChar() {
                    return properties.getProperty("base.char").trim().charAt(0);
                }

                @Override
                public boolean isParallel() {
                    return Boolean.parseBoolean(properties.getProperty("parallel"));
                }

                @Override
                public double getMatchingThreshold() {
                    return Double.parseDouble(properties.getProperty("matching.threshold"));
                }
            };
        } catch (IOException e) {
            throw new SnowXConfigurationException(e);
        }
    }

    public static class SnowXConfigurationException extends RuntimeException {

        SnowXConfigurationException(Throwable throwable) {
            super(throwable);
        }

    }

}
