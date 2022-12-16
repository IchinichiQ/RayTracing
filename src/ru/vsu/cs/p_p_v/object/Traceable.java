package ru.vsu.cs.p_p_v.object;

import ru.vsu.cs.p_p_v.Vector;
import ru.vsu.cs.p_p_v.object.material.Material;

public interface Traceable {
    double getIntersection(Vector u, Vector v);

    Vector getNormal(Vector p);

    Material getMaterial();
}
