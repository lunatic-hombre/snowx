package com.servicenow.snowx.util;

public class Bounds extends Dimensions {

    protected int startX, startY;

    public Bounds(int startX, int startY, int width, int height) {
        super(width, height);
        this.startX = startX;
        this.startY = startY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return startX + width;
    }

    public int getEndY() {
        return startY + height;
    }

    public void setEndX(int endX) {
        this.width = endX - startX;
    }

    public void setEndY(int endY) {
        this.height = endY - startY;
    }

}
