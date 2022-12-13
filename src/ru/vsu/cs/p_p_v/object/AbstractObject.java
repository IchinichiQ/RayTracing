package ru.vsu.cs.p_p_v.object;

import ru.vsu.cs.p_p_v.Vector;
import ru.vsu.cs.p_p_v.object.matter.Matter;

public abstract class AbstractObject implements Locatable, Traceable, Material {
    private final Vector position;
    private final Matter matter;

    public AbstractObject(Vector position, Matter material) {
        this.position = position;
        this.matter = material;
    }

    @Override
    public Vector getPosition() {
        return position;
    }

    public Matter getMatter() {
        return matter;
    }
}
