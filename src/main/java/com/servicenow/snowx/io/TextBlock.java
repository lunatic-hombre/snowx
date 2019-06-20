package com.servicenow.snowx.io;

import com.servicenow.snowx.util.Bounds;

public class TextBlock {

    private final Bounds bounds;
    private final CharSequence[] text;

    public TextBlock(Bounds bounds, CharSequence[] text) {
        this.bounds = bounds;
        this.text = text;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public CharSequence[] getText() {
        return text;
    }

}
