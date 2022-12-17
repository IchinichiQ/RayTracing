package ru.vsu.cs.p_p_v;

import ru.vsu.cs.p_p_v.light.AmbientLight;
import ru.vsu.cs.p_p_v.light.Light;
import ru.vsu.cs.p_p_v.light.PointLight;
import ru.vsu.cs.p_p_v.object.Traceable;
import ru.vsu.cs.p_p_v.object.material.Material;

import java.util.ArrayList;
import java.util.List;

public class RayTracer {
    private static final Pixel EMPTY_PIXEL = new Pixel(77, 143, 172);
    private static final Pixel DEFAULT_PIXEL = new Pixel(0, 0, 0);

    private final Scene scene;

    private final Traceable[] objects;

    private final Light[] lights;

    private final double bias = 0.00001;

    public RayTracer(Scene scene) {
        this.scene = scene;

        objects = this.scene.getObjects().toArray(new Traceable[0]);
        lights = this.scene.getLights().toArray(new Light[0]);
    }

    public void trace(Canvas canvas, Camera camera, int depth) {
        int canvasHeight = canvas.getHeight();
        int canvasWidth = canvas.getWidth();

        for (int y = -canvasHeight / 2; y < canvasHeight / 2; y++) {
            for (int x = -canvasWidth / 2; x < canvasWidth / 2; x++) {
                Vector u = new Vector(camera.getPosition());
                Vector v = canvasToViewport(x, y, canvasHeight, canvasWidth);
                v = v.rotateYP(camera.getYaw(), camera.getPitch());

                canvas.setPixel(x, y, getPixel(u, v, depth).getRGB());
            }
        }
    }

    private Vector canvasToViewport(double x, double y, int cHeight, int cWidth) {
        return new Vector((x / cWidth) * ((double) cWidth / cHeight), y / cHeight, 1);
    }

    private Pixel getPixel(Vector u, Vector v, int depth) {
        Traceable closest = null;

        double closestT = Double.MAX_VALUE;
        for (Traceable object : objects) {
            double t = object.getIntersection(u, v);
            if (!Double.isNaN(t) && t < closestT && t > bias) {
                closestT = t;
                closest = object;
            }
        }

        if (closest == null)
            return EMPTY_PIXEL;

        // p = u + v t_min
        Vector p = v.multiply(closestT).add(u);

        // n = unit normal at p
        Vector n = closest.getNormal(p).normalize();

        Material material = closest.getMaterial();

        Pixel pixel = applyLight(v.normalize(), p, n, material);

        pixel = pixel.scale(material.getColor());

        double reflective = material.getReflective();
        if (reflective > 0 && depth > 0) {
            // v = v - (2*v.n)n
            Vector vRef = v.subtract(2 * v.dot(n), n);

            int nearRGB = getPixel(p, vRef, depth - 1).getRGB();

            pixel = pixel.multiply(1.0 - reflective);
            Pixel nearPixel = new Pixel(nearRGB).multiply(reflective);

            pixel = pixel.mix(nearPixel.getRGB(), 1.0);
        }

        double opacity = material.getOpacity();
        if (opacity > 0.0) {
            Vector pos;
            if (v.dot(n) < 0) {
                pos = p.subtract(n.multiply(bias));
            } else {
                pos = p.add(n.multiply(bias));
            }

            int nextRGB = getPixel(pos, v.normalize(), depth).getRGB();

            pixel = pixel.multiply(1.0 - opacity);
            Pixel nextPixel = new Pixel(nextRGB).multiply(opacity);

            pixel = pixel.mix(nextPixel.getRGB(), 1.0);
        }

        return pixel;
    }

    private Pixel applyLight(Vector u, Vector p, Vector n, Material material) {
        Pixel pixel = DEFAULT_PIXEL;

        // set u = unit(v) for phong
        int phong = material.getPhong();
        double opacity = material.getOpacity();

        for (Light light : lights) {
            if (light instanceof AmbientLight) {
                pixel = pixel.mix(light.getColor(), light.getIntensity());
                continue;
            }

            // l = l0 - p;
            // TODO: Hardcoded point light
            Vector l = ((PointLight) light).getPosition().subtract(p);

            List<Traceable> intersectedObjects = allIntersects(p, l, 1.0);
            boolean allOpacityObjects = true;
            double totalOpacity = 1.0;
            for (Traceable currObject : intersectedObjects) {
                if (currObject.getMaterial().getOpacity() == 0.0) {
                    allOpacityObjects = false;
                    break;
                } else {
                    totalOpacity *= currObject.getMaterial().getOpacity();
                }
            }

            // If not in shadow
            if (intersectedObjects.size() == 0 || allOpacityObjects) {
                l = l.normalize();
                if (opacity > 0.0 && intersectedObjects.size() > 0 && allOpacityObjects)
                    l = l.multiply(-1);

                pixel = applyDiffuse(pixel, light, n, l, totalOpacity);
                pixel = applyPhong(pixel, light, phong, u, n, l, totalOpacity);
            }
        }

        return pixel;
    }

    private Pixel applyDiffuse(Pixel pixel, Light light, Vector n, Vector l, double opacity) {
        // diffuse reflection
        double d = n.dot(l);
        Pixel lightColorAdjusted = new Pixel(light.getColor());
        lightColorAdjusted = lightColorAdjusted.multiply(light.getIntensity());

        if (d > 0) {
            pixel = pixel.mix(lightColorAdjusted.getColor(), d * opacity);
        }

        return pixel;
    }

    private Pixel applyPhong(Pixel pixel, Light light, int phong, Vector u, Vector n, Vector l, double opacity) {
        // phong illumination
        Pixel lightColorAdjusted = new Pixel(light.getColor());
        lightColorAdjusted = lightColorAdjusted.multiply(light.getIntensity());

        if (phong > 0) {
            double d = l.subtract(2 * l.dot(n), n).dot(u);

            if (d > 0) {
                pixel = pixel.mix(lightColorAdjusted.getColor(), FastMath.pow(d, phong) * opacity);
            }
        }

        return pixel;
    }

    private List<Traceable> allIntersects(Vector u, Vector v, double tMax) {
        // TODO: Sort by opacity
        List<Traceable> intersectedObjects = new ArrayList<>();

        for (Traceable object : objects) {
            double t = object.getIntersection(u, v);
            if (t < tMax && t > bias) {
                intersectedObjects.add(object);
            }
        }

        return intersectedObjects;
    }
}
