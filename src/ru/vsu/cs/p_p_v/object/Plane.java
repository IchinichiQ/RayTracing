package ru.vsu.cs.p_p_v.object;

import ru.vsu.cs.p_p_v.Vector;
import ru.vsu.cs.p_p_v.object.material.Material;

public class Plane extends AbstractObject
{
    private final Vector normal;

    public Plane(Vector position, Vector normal, Material material)
    {
        super(position, material);

        this.normal = normal;
    }

    @Override
    public double getIntersection(Vector u, Vector v)
    {
        /*
         * The intersection of the ray:
         *
         *     p = u + vt
         *
         * With this plane:
         *
         *     (p0 - p).n = 0
         *
         * Has the solution t:
         *
         *     t = (p0 - u).n / v.n
         */

        // v.n = 0 => No intersection

        double d = v.dot(normal);

        if (d == 0)
        {
            return Double.NaN;
        }

        // t = (p0 - u).n / v.n

        double t = this.getPosition().subtract(u).dot(normal) / d;

        // 0 <= t <= 1 => Intersection

        if (t < 0)
        {
            return Double.NaN;
        }

        return t;
    }

    @Override
    public Vector getNormal(Vector p)
    {
        return this.normal;
    }
}
