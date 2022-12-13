package ru.vsu.cs.p_p_v.light;

import java.awt.*;

public abstract class AbstractLight implements Light {
    double intensity;
    Color color;

    public AbstractLight(double intensity, Color color) {
        this.intensity = intensity;
        this.color = color;
    }

    @Override
    public double getIntensity() {
        return intensity;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
