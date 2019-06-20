package com.servicenow.snowx.util;

public class Dimensions {

    protected int width, height;

    public Dimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getArea() {
        return width * height;
    }

}
