package ru.vsu.cs.p_p_v.object.matter;

import java.awt.*;

public class ColorMatter implements Matter {
    private Color color;
    private double opacity;
    private double shine;
    private int phong;

    public ColorMatter(Color color, double opacity, double shine, int phong) {
        this.color = color;
        this.opacity = opacity;
        this.shine = shine;
        this.phong = phong;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public double getOpacity() {
        return opacity;
    }

    @Override
    public double getShine() {
        return shine;
    }

    @Override
    public int getPhong() {
        return phong;
    }
}