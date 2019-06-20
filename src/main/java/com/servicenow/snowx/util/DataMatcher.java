package com.servicenow.snowx.util;

public interface DataMatcher<I> {

    /**
     * Get the score for matching your target against another input, using some kind of fuzzy logic.
     * @param input the input to match against
     * @return a score from 0 to 1
     */
    double match(I input);

    I getTarget();

}
