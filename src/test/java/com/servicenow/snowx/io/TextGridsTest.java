package com.servicenow.snowx.io;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

public class TextGridsTest {

    @Test
    public void test() throws IOException {
        TextGrids.Scanner scanner = TextGrids.scan(Path.of("TestData.snw"));

        assertEquals("File should contain number of blocks equal to points on the grid, minus partial blocks.",
                100*100 - 4*100 - 4*96, scanner.blocks(5, 5).count());

        String blocks = scanner
                .blocks(5, 5)
                .map(block -> Stream.of(block.getText()).collect(joining("\n")))
                .limit(10)
                .collect(joining("\n=====\n"));

        String expected = readFully("/io/SnowDataBlocks.snw");

        assertEquals(expected, blocks);
    }

    private static String readFully(String resource) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(TextGridsTest.class.getResourceAsStream(resource)))) {
            return reader.lines().collect(joining("\n"));
        }
    }

}