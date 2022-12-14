package ru.vsu.cs.p_p_v;

import java.awt.Color;

/**
 *
 */
public class Pixel
{
    private final int r;

    private final int g;

    private final int b;

    private final int a;

    public Pixel(Color color)
    {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public Pixel(int rgb)
    {
        this((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }

    public Pixel(int r, int g, int b)
    {
        this(r, g, b, 0xFF);
    }

    public Pixel(int r, int g, int b, int a)
    {
        this.r = Math.min(r, 255);
        this.g = Math.min(g, 255);
        this.b = Math.min(b, 255);
        this.a = Math.min(a, 255);
    }

    public Pixel mix(Color color, double brightness)
    {
        return mix(color.getRGB(), brightness);
    }

    public Pixel mix(int rgb, double brightness)
    {
        int newR = this.r + (int) (((rgb >> 16) & 0xFF) * brightness);
        int newG = this.g + (int) (((rgb >> 8) & 0xFF) * brightness);
        int newB = this.b + (int) ((rgb & 0xFF) * brightness);

        return new Pixel(newR, newG, newB, this.a);
    }

    public Pixel scale(Color color)
    {
        return scale(color.getRGB());
    }

    public Pixel scale(int rgb)
    {
        int newR = (int) (this.r * (double) ((rgb >> 16) & 0xFF) / 0xFF);
        int newG = (int) (this.g * (double) ((rgb >> 8) & 0xFF) / 0xFF);
        int newB = (int) (this.b * (double) (rgb & 0xFF) / 0xFF);

        return new Pixel(newR, newG, newB, this.a);
    }

    public Pixel multiply(double value) {
        return new Pixel((int) (r * value), (int) (g * value), (int) (b * value), this.a);
    }

//    public Pixel scale(int rgb)
//    {
//        // Fix when scaling color 0, 0, 0 with any other or vice versa
//        double scaleR = Math.max(1, (double) ((rgb >> 16) & 0xFF) / 0xFF);
//        double scaleG = Math.max(1, (double) ((rgb >> 8) & 0xFF) / 0xFF);
//        double scaleB = Math.max(1, (double) (rgb & 0xFF) / 0xFF);
//
//        int newR = (int) (Math.max(1, this.r) * (double) scaleR);
//        int newG = (int) (Math.max(1, this.g) * (double) scaleG);
//        int newB = (int) (Math.max(1, this.b) * (double) scaleB);
//
//        return new Pixel(newR, newG, newB, this.a);
//    }

    public int getRGB()
    {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public Color getColor()
    {
        return new Color(getRGB());
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "[r=" + r + ",g=" + g + ",b=" + b + ",a=" + a + "]";
    }
}

