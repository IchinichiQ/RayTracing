package ru.vsu.cs.p_p_v;

import ru.vsu.cs.p_p_v.light.AmbientLight;
import ru.vsu.cs.p_p_v.light.Light;
import ru.vsu.cs.p_p_v.light.PointLight;
import ru.vsu.cs.p_p_v.object.Material;
import ru.vsu.cs.p_p_v.object.Traceable;
import ru.vsu.cs.p_p_v.object.matter.ColorMatter;
import ru.vsu.cs.p_p_v.object.matter.Matter;

import java.awt.*;

public class RayTracer
{
    private static final boolean SHADOWS = false;

    private static final Pixel EMPTY_PIXEL = new Pixel(77, 143, 172);

    private final Scene scene;

    private final Traceable[] objects;

    private final Light[] lights;

    public RayTracer(Scene scene)
    {
        this.scene = scene;

        objects = this.scene.getObjects().toArray(new Traceable[0]);
        lights = this.scene.getLights().toArray(new Light[0]);
    }

    public void trace(Canvas canvas, Camera camera, int depth)
    {
        int canvasHeight = canvas.getHeight();
        int canvasWidth = canvas.getWidth();

        for (int y = -canvasHeight / 2; y < canvasHeight / 2; y++)
        {
            for (int x = -canvasWidth / 2; x < canvasWidth / 2; x++)
            {
                Vector u = new Vector(camera.getPosition());
                // Z - fov
                // x -
                Vector v = canvasToViewport(x, y, canvasHeight, canvasWidth);
                v = v.rotateYP(camera.getYaw(), camera.getPitch());
                //v = cameraRotation.multiply(v);

                canvas.setPixel(x, y, getPixel(u, v, null, depth).getRGB());
            }
        }
    }

    private Vector canvasToViewport(double x, double y, int cHeight, int cWidth) {
        return new Vector((x / cWidth) * ((double) cWidth / cHeight), y /  cHeight, 1);
    }

    private Pixel getPixel(Vector u, Vector v, Traceable current, int depth)
    {
        Traceable closest = null;
        double minT = Double.MAX_VALUE;

        double t;

        for (Traceable object : objects)
        {
            if (object != current)
            {
                t = object.getIntersection(u, v);

                if (!Double.isNaN(t) && (t < minT))
                {
                    minT = t;
                    closest = object;
                }
            }
        }

        if (closest == null)
        {
            return EMPTY_PIXEL;
        }

        // p = u + v t_min
        Vector p = v.multiply(minT);
        p = p.add(u);

        // n = unit normal at p
        Vector n = closest.getNormal(p);
        n = n.normalize();

        Matter matter;
        if (closest instanceof Material) {
            matter = ((Material) closest).getMatter();
        } else {
            matter = new ColorMatter(Color.BLACK, 1.0, 0.0, 0);
        }

        Pixel pixel = new Pixel(0, 0, 0);
        pixel = applyLight(v.normalize(), p, n, closest, matter);

        pixel = pixel.scale(matter.getColor());

        double reflective = matter.getReflective();

        if (reflective == 0 || depth == 0)
        {
            return pixel;
        }

        // u = p
        u = p;

        // v = v - (2*v.n)n
        v = v.subtract(2 * v.dot(n), n);

        int nearRGB = getPixel(u, v, closest, depth - 1).getRGB();

        pixel = pixel.multiply(1.0 - reflective);
        Pixel nearPixel = new Pixel(nearRGB).multiply(reflective);

        pixel = pixel.mix(nearPixel.getRGB(), 1.0);

        return pixel;
    }

    private Pixel applyLight(Vector u, Vector p, Vector n, Traceable closest, Matter matter)
    {
        Pixel pixel = new Pixel(0, 0, 0, 255);

        // set u = unit(v) for phong
        int phong = matter.getPhong();

        for (Light light : lights)
        {
            if (light instanceof AmbientLight) {
                pixel = pixel.mix(light.getColor(), light.getIntensity());
                continue;
            }

            // l = l0 - p;
            // TODO: Hardcoded point light
            Vector l = ((PointLight) light).getPosition().subtract(p);

            // If not in shadow
            if (!intersects(p, l, closest, 1.0))
            {
                l = l.normalize();

                pixel = applyDiffuse(pixel, light, n, l);
                pixel = applyPhong(pixel, light, phong, u, n, l);
            }
        }

        return pixel;
    }

    private Pixel applyDiffuse(Pixel pixel, Light light, Vector n, Vector l)
    {
        // diffuse reflection
        double d = n.dot(l);

        if (d > 0)
        {
            pixel = pixel.mix(light.getColor(), d);
        }

        return pixel;
    }

    private Pixel applyPhong(Pixel pixel, Light light, int phong, Vector u, Vector n, Vector l)
    {
        // phong illumination

        if (phong > 0)
        {
            double d = l.subtract(2 * l.dot(n), n).dot(u);

            if (d > 0)
            {
                pixel = pixel.mix(light.getColor(), FastMath.pow(d, phong));
            }
        }

        return pixel;
    }

    private boolean intersects(Vector u, Vector v, Traceable ignore, double tMax)
    {
        for (Traceable object : objects)
        {
            if (object != ignore && object.getIntersection(u, v) < tMax)
            {
                return true;
            }
        }

        return false;
    }
}
