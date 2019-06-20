package com.servicenow.snowx.io;

public class TextGridReadException extends RuntimeException {

    public TextGridReadException(Throwable throwable) {
        super(throwable);
    }

    public TextGridReadException(String s) {
        super(s);
    }
}
