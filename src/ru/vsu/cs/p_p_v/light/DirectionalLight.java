package ru.vsu.cs.p_p_v.light;

import ru.vsu.cs.p_p_v.Vector;
import ru.vsu.cs.p_p_v.object.Locatable;

import java.awt.*;

public class DirectionalLight extends AbstractLight {
    private Vector direction;

    public DirectionalLight(Vector direction, double intensity, Color color) {
        super(intensity, color);
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }
}