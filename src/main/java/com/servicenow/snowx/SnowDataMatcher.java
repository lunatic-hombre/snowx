package com.servicenow.snowx;

import com.servicenow.snowx.util.BinaryDataMatcher;
import com.servicenow.snowx.util.DataMatcher;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;

public class SnowDataMatcher implements DataMatcher<CharSequence[]> {

    private final DataMatcher<BigInteger> binaryMatcher;
    private final Function<CharSequence[], BigInteger> encode;
    private final CharSequence[] target;

    public SnowDataMatcher(DataMatcher<BigInteger> binaryMatcher,
                           Function<CharSequence[], BigInteger> encode,
                           CharSequence[] target) {
        this.binaryMatcher = binaryMatcher;
        this.encode = encode;
        this.target = target;
    }

    @Override
    public double match(CharSequence[] input) {
        return binaryMatcher.match(encode.apply(input));
    }

    @Override
    public CharSequence[] getTarget() {
        return target;
    }

    public int getWidth() {
        return target[0].length();
    }

    public int getHeight() {
        return target.length;
    }

    public static SnowDataMatcher generate(CharSequence[] target, char hit) {
        final Function<CharSequence[], BigInteger> conversion = encode(hit);
        final BinaryDataMatcher binaryMatcher = new BinaryDataMatcher(conversion.apply(target));
        return new SnowDataMatcher(binaryMatcher, conversion, target);
    }

    private static Function<CharSequence[], BigInteger> encode(char hit) {
        return chars -> {
            StringBuilder sb = new StringBuilder(chars.length * chars[0].length());
            Arrays.stream(chars)
                .flatMapToInt(CharSequence::chars)
                .map(c -> c == hit ? 1 : 0)
                .forEach(sb::append);
            return new BigInteger(sb.toString(), 2);
        };
    }

}
