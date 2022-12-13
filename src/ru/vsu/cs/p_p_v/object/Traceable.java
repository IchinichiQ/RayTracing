package ru.vsu.cs.p_p_v.object;

import ru.vsu.cs.p_p_v.Vector;

public interface Traceable {
    double getIntersection(Vector u, Vector v);

    Vector getNormal(Vector p);
}
