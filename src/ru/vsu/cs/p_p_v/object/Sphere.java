package ru.vsu.cs.p_p_v.object;

import ru.vsu.cs.p_p_v.Vector;
import ru.vsu.cs.p_p_v.object.material.Material;

public class Sphere extends AbstractObject {
    private final double r;
    private final double rSquare;

    public Sphere(Vector position, double r, Material material)
    {
        super(position, material);

        this.r = r;
        this.rSquare = r * r;
    }

    @Override
    public double getIntersection(Vector u, Vector v)
    {
        /*
         * The intersection of the ray:
         *
         *     p = u + vt
         *
         * With this sphere:
         *
         *     (p - p0)^2 = r^2
         *
         * Has the solution t:
         *
         *     (v.v)t^2 + 2(q.v)t + (q.q - r^2) = 0, where q = u - p0
         *
         * Which is a quadratic equation with coefficients:
         *
         *     a = v.v
         *     b = 2v.q
         *     c = q.q - r^2
         */

        // q = u - p0

        Vector q = new Vector(u);
        q = q.subtract(getPosition());

        // Calculate quadratic coefficients

        double a = v.dot();
        double b = 2 * v.dot(q);
        double c = q.dot() - rSquare;

        // Complex solution => No intersection

        double d = b * b - 4 * a * c;

        if (d <= 0)
        {
            return Double.NaN;
        }

        // Solve quadratic for t

        d = Math.sqrt(d);
        double t1 = (b > 0) ? (-b - d) / (2 * a) : (-b + d) / (2 * a);
        double t2 = c / (a * t1);

        if (t1 < 0 && t2 < 0)
            return Double.NaN;

        if (t1 < 0.00001)
            return t2;
        if (t2 < 0.00001)
            return t1;

        return Math.min(t1, t2);
    }

    @Override
    public Vector getNormal(Vector p)
    {
        return p.subtract(this.getPosition());
    }

    public double getRadius()
    {
        return r;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "[p0=" + this.getPosition() + ",r=" + r + "]";
    }
}
