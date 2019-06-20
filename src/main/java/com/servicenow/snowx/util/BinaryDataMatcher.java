package com.servicenow.snowx.util;

import java.math.BigInteger;

public class BinaryDataMatcher implements DataMatcher<BigInteger> {

    private final BigInteger target;
    private final double bitCount;

    public BinaryDataMatcher(BigInteger target) {
        this.target = target;
        this.bitCount = target.bitCount();
    }

    /**
     * Gets the percentage of matching bits using XOR to spot differences.
     * @param input a big integer representing the bits
     * @return percentage of matching bits
     */
    @Override
    public double match(BigInteger input) {
        final int nonMatchingOnes = countOnes(target.xor(input));
        final double nonMatchingRatio = ((double) nonMatchingOnes) / bitCount;
        return Math.max(1d - nonMatchingRatio, 0);
    }

    @Override
    public BigInteger getTarget() {
        return target;
    }

    private int countOnes(BigInteger input) {
        int bitCount = input.bitCount(), ones = 0;
        for (int i=0; i < bitCount; i++) {
            if (input.testBit(i))
                ones++;
        }
        return ones;
    }

}
