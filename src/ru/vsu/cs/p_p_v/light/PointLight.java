package ru.vsu.cs.p_p_v.light;

import ru.vsu.cs.p_p_v.Vector;
import ru.vsu.cs.p_p_v.object.AbstractObject;
import ru.vsu.cs.p_p_v.object.Locatable;

import java.awt.*;

public class PointLight extends AbstractLight implements Locatable {
    private Vector position;

    public PointLight(Vector position, double intensity, Color color) {
        super(intensity, color);

        this.position = position;
    }

    @Override
    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }
}
