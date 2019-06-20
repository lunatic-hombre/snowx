package com.servicenow.snowx.util;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class BinaryDataMatcherTest {

    private final BigInteger expectedValue = new BigInteger("1234567890123456789");
    private final BinaryDataMatcher matcher = new BinaryDataMatcher(expectedValue);

    @Test
    public void match_exactReturns1() {
        assertScore(this.expectedValue, 1.0d);
    }

    @Test
    public void match_differences() {
        assertScore("1234567890123456788", 0.9615);
        assertScore("1234567890123456777", 0.9615);
        assertScore("1231231231231231123", 0.6923);
        assertScore("6666666666666666666", 0);
    }

    private void assertScore(String numberString, double expected) {
        assertScore(new BigInteger(numberString), expected);
    }

    private void assertScore(BigInteger input, double expectedScore) {
        final double actualScore = matcher.match(input);
        assertEquals(expectedScore, actualScore, 0.0001);
    }

}