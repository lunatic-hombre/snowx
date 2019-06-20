package com.servicenow.snowx.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SnowXThreatDetails {

    public static SnowXThreatDetails parse(String str) {
        Matcher matcher = Pattern.compile("(?<name>.*?):(?<file>.*)").matcher(str);
        if (!matcher.matches())
            throw new IllegalArgumentException("Could not parse threat from string " + str);
        return new SnowXThreatDetails(matcher.group("name"), matcher.group("file"));
    }

    private final String name;
    private final String file;

    public SnowXThreatDetails(String name, String file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getFile() {
        return file;
    }

}
