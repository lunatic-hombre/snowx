package com.servicenow.snowx;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.servicenow.snowx.SnowDataInputStreamReader.readSnowData;
import static org.junit.Assert.assertEquals;

public class SnowDataMatcherTest {

    private CharSequence[] torpedo;
    private SnowDataMatcher matcher;

    @Before
    public void setUp() {
        torpedo = readSnowData(getClass().getResourceAsStream("/threats/HPTorpedo.snw"));
        matcher = SnowDataMatcher.generate(torpedo, '+');
    }

    @Test
    public void match_exactReturns1() {
        assertEquals(1.0, matcher.match(torpedo), 0);
    }

    @Test
    public void match_closeResult() {
        CharSequence[] input = readSnowData("/snowdata/torpedo_close.snw");
        assertEquals(0.979, matcher.match(input), 0.01);
    }

    @Test
    public void match_farResult() {
        CharSequence[] input = readSnowData("/snowdata/torpedo_far.snw");
        assertEquals(0.787, matcher.match(input), 0.01);
    }

}
