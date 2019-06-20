package com.servicenow.snowx;

import com.servicenow.snowx.config.SnowXConfig;
import com.servicenow.snowx.config.SnowXFileConfigurator;
import org.junit.Test;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class SnowXMainIntegrationTest {

    private static final String DEFAULT_PROPERTIES = "application.properties";

    @Test
    public void run_defaultFileNormal() throws Exception {
        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputBytes, true);
        SnowXMain main = new SnowXMain(
                getDefaultConfiguratorSingleThread(),
                out, out, new String[0]
        );
        main.run();
        assertEquals(readFully("/expected_default_output.txt"), outputBytes.toString());
    }

    @Test
    public void run_defaultFileParallel() throws Exception {
        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputBytes, true);
        SnowXMain main = new SnowXMain(
                new SnowXFileConfigurator(DEFAULT_PROPERTIES),
                out, out, new String[0]
        );
        main.run();
        Matcher matcher = Pattern.compile("THREAT DETECTED").matcher(outputBytes.toString());
        int resultCount = 0;
        while (matcher.find())
            resultCount++;
        assertEquals(4, resultCount);
    }

    private SnowXFileConfigurator getDefaultConfiguratorSingleThread() {
        return new SnowXFileConfigurator(DEFAULT_PROPERTIES) {
            @Override
            public SnowXConfig get() {
                SnowXConfig config = spy(super.get());
                // parallel makes results unpredictable order
                doReturn(false).when(config).isParallel();
                return config;
            }
        };
    }

    private String readFully(String resource) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resource)))) {
            return reader.lines().collect(joining("\n"));
        }
    }

}