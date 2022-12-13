package ru.vsu.cs.p_p_v;

public class Vector {
    private final double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vector other) {
        x = other.getX();
        y = other.getY();
        z = other.getZ();
    }

    public Vector() {
        this(0, 0, 0);
    }

    public Vector add(Vector other)
    {
        return new Vector(x + other.getX(), y + other.getY(), z + other.getZ());
    }

    public Vector subtract(Vector other)
    {
        return new Vector(x - other.getX(), y - other.getY(), z - other.getZ());
    }

    public Vector subtract(double s, Vector other)
    {
        return new Vector(x - (s * other.getX()), y - (s * other.getY()), z - (s * other.getZ()));
    }

    public Vector scale(double s)
    {
        return new Vector(x * s, y * s, z * s);
    }

    public Vector unit()
    {
        double mod = Math.sqrt(x * x + y * y + z * z);

        return new Vector(x / mod, y / mod, z / mod);
    }

    public double dot()
    {
        return x * x + y * y + z * z;
    }

    public double dot(Vector other)
    {
        return x * other.x + y * other.y + z * other.z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
