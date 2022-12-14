package ru.vsu.cs.p_p_v;

public class Camera {
    private Vector position;
    private double yaw, pitch;
    private double fov;

    public Camera(Vector position) {
        this.position = position;
        this.yaw = 0.0;
        this.pitch = 0.0;
        this.fov = 0.0;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public void addYaw(double deltaYaw) {
        this.yaw += deltaYaw;
        this.yaw %= 360.0;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public void addPitch(double deltaPitch) {
        this.pitch += deltaPitch;
        this.pitch %= 360.0;
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }
}
