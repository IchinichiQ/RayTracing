package ru.vsu.cs.p_p_v.object.material;

import java.awt.*;

public class ColorMaterial implements Material {
    private Color color;
    private double opacity;
    private double reflective;
    private int phong;

    public ColorMaterial(Color color, double opacity, double reflective, int phong) {
        this.color = color;
        this.opacity = opacity;
        this.reflective = reflective;
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
    public double getReflective() {
        return reflective;
    }

    @Override
    public int getPhong() {
        return phong;
    }
}
