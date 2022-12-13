package ru.vsu.cs.p_p_v.light;

import ru.vsu.cs.p_p_v.object.AbstractObject;

import java.awt.*;

public class AmbientLight extends AbstractLight {
    public AmbientLight(double intensity, Color color) {
        super(intensity, color);
    }
}
