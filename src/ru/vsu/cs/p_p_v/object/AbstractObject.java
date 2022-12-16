package ru.vsu.cs.p_p_v.object;

import ru.vsu.cs.p_p_v.Vector;
import ru.vsu.cs.p_p_v.object.material.Material;

public abstract class AbstractObject implements Locatable, Traceable {
    private final Vector position;
    private final Material material;

    public AbstractObject(Vector position, Material material) {
        this.position = position;
        this.material = material;
    }

    @Override
    public Vector getPosition() {
        return position;
    }

    public Material getMaterial() {
        return material;
    }
}
