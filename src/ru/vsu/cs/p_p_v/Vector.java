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

    public Vector multiply(double s)
    {
        return new Vector(x * s, y * s, z * s);
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector normalize()
    {
        double mod = getLength();

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

    public Vector rotateYP(double yaw, double pitch) {
        // Convert to radians
        double yawRads = Math.toRadians(yaw);
        double pitchRads = Math.toRadians(pitch);

        // Step one: Rotate around X axis (pitch)
        float newY = (float) (y*Math.cos(pitchRads) - z*Math.sin(pitchRads));
        float newZ = (float) (y*Math.sin(pitchRads) + z*Math.cos(pitchRads));

        // Step two: Rotate around the Y axis (yaw)
        float newX = (float) (x*Math.cos(yawRads) + newZ*Math.sin(yawRads));
        newZ = (float) (-x*Math.sin(yawRads) + newZ*Math.cos(yawRads));

        return new Vector(newX, newY, newZ);
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
