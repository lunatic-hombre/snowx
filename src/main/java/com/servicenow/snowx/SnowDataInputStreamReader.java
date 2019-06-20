package com.servicenow.snowx;

import com.servicenow.snowx.io.TextGridReadException;

import java.io.*;

public class SnowDataInputStreamReader {

    public static CharSequence[] readSnowData(String resource) {
        return readSnowData(SnowDataInputStreamReader.class.getResourceAsStream(resource));
    }

    public static CharSequence[] readSnowData(InputStream stream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return reader.lines().toArray(CharSequence[]::new);
        } catch (IOException e) {
            throw new TextGridReadException(e);
        }
    }

}
