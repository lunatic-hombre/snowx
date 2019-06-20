package com.servicenow.snowx;

import com.servicenow.snowx.config.SnowXConfig;
import com.servicenow.snowx.config.SnowXFileConfigurator;
import com.servicenow.snowx.config.SnowXThreatDetails;
import com.servicenow.snowx.io.TextBlock;
import com.servicenow.snowx.io.TextGrids;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Supplier;

import static com.servicenow.snowx.SnowDataInputStreamReader.readSnowData;
import static java.util.stream.Collectors.joining;

/**
 * Main class for SnowX application.
 *
 * By default, will load properties from application.properties and run input file from command-line argument.
 */
public class SnowXMain implements Runnable {

    private static final String MATCH_MSG_FORMAT =
            "\n\u001B[31mTHREAT DETECTED\u001B[0m" +
            "\n===========================" +
            "\n \u001B[34mThreat type:\u001B[0m   %s" +
            "\n \u001B[34mLocation:\u001B[0m      (%d, %d)" +
            "\n \u001B[34mCertainty:\u001B[0m     %.3f" +
            "\n \u001B[34mAppearance:\u001B[0m\n%s";

    public static void main(String[] args) {
        SnowXMain main = new SnowXMain(
            new SnowXFileConfigurator("application.properties"),
            System.out,
            System.err,
            args
        );
        main.run();
    }

    private final Supplier<SnowXConfig> configurator;
    private final PrintStream out, err;
    private final String[] args;

    SnowXMain(Supplier<SnowXConfig> configurator, PrintStream out, PrintStream err, String[] args) {
        this.configurator = configurator;
        this.out = out;
        this.err = err;
        this.args = args;
    }

    @Override
    public void run() {
        try {
            SnowXConfig config = configurator.get();
            Path dataFile = getInputFile(args, config);
            TextGrids.Scanner scanner = TextGrids.scan(dataFile).parallel(config.isParallel());

            for (SnowXThreatDetails threat : config.getThreats()) {
                SnowDataMatcher matcher = SnowDataMatcher.generate(readSnowData(threat.getFile()), config.getBaseChar());
                scanner.blocks(matcher.getWidth(), matcher.getHeight())
                        .forEach(block -> {
                            double score = matcher.match(block.getText());
                            if (score > config.getMatchingThreshold())
                                printMatchDetails(threat, block, score);
                        });
            }
        } catch (RuntimeException e) {
            err.println("An exception occurred while searching, giving up!");
            e.printStackTrace();
        }
    }

    private Path getInputFile(String[] args, SnowXConfig config) {
        if (args.length != 1) {
            err.println("Usage: ./run.sh <filename>\nDefaulting to " + config.getInputFile());
            return config.getInputFile();
        }
        return Paths.get(args[0]);
    }

    private void printMatchDetails(SnowXThreatDetails threat, TextBlock block, double score) {
        String message = String.format(
            MATCH_MSG_FORMAT,
            threat.getName(),
            block.getBounds().getStartX(),
            block.getBounds().getStartY(),
            score,
            Arrays.stream(block.getText()).map(row -> "\n        " + row).collect(joining())
        );
        out.println(message);
    }

}
