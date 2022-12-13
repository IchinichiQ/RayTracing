package ru.vsu.cs.p_p_v;

import java.awt.*;

public class Canvas {
    private final int width;
    private final int height;
    private int[] pixels;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;

        this.pixels = new int[width * height];
    }

    public void setPixel(int i, int color) {
        pixels[i] = color;
    }

    public int getPixel(int i) {
        return pixels[i];
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
