package ru.vsu.cs.p_p_v;

import ru.vsu.cs.p_p_v.light.Light;
import ru.vsu.cs.p_p_v.light.PointLight;
import ru.vsu.cs.p_p_v.object.Material;
import ru.vsu.cs.p_p_v.object.Traceable;
import ru.vsu.cs.p_p_v.object.matter.ColorMatter;
import ru.vsu.cs.p_p_v.object.matter.Matter;

import java.awt.*;

public class RayTracer
{
    private static final boolean SHADOWS = true;

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

    public void trace(Canvas canvas, Vector cameraPosition, Matrix3X3 cameraRotation)
    {
        int i = 0;
        int canvasHeight = canvas.getHeight();
        int canvasWidth = canvas.getWidth();

        for (int y = -canvasHeight / 2; y < canvasHeight / 2; y++)
        {
            for (int x = -canvasWidth / 2; x < canvasWidth / 2; x++)
            {
                Vector u = new Vector(cameraPosition);
                // Z - fov
                // x -
                Vector v = canvasToViewport(x, y, canvasHeight, canvasWidth);
                //v = cameraRotation.multiply(v);

                canvas.setPixel(x, y, getPixel(u, v, null, 3).getRGB());
            }
        }
    }

    private Vector canvasToViewport(double x, double y, int cHeight, int cWidth) {
        return new Vector(x / cWidth, y /  cHeight, 1);
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
        Vector p = v.scale(minT);
        p = p.add(u);

        // n = unit normal at p
        Vector n = closest.getNormal(p);
        n = n.unit();

        Matter matter;
        if (closest instanceof Material) {
            matter = ((Material) closest).getMatter();
        } else {
            matter = new ColorMatter(Color.BLACK, 1.0, 0.0, 0);
        }

        u = v.unit();
        Pixel pixel = new Pixel(0, 0, 0);
        pixel = applyLight(u, p, n, closest, matter);

        pixel = pixel.scale(matter.getColor());

        double shine = matter.getShine();

        if (shine == 0 || depth == 0)
        {
            return pixel;
        }

        // u = p
        u = p;

        // v = v - (2*v.n)n
        v = v.subtract(2 * v.dot(n), n);

        Pixel newPixel = new Pixel(pixel.getRGB());
        int nearRGB = getPixel(u, v, closest, depth - 1).getRGB();
        if (Math.abs(shine - 1.0) <= 0.0001) {
            newPixel = new Pixel(nearRGB);
        } else {
            newPixel = newPixel.mix(nearRGB, shine);
        }

        return newPixel;
    }

    private Pixel applyLight(Vector u, Vector p, Vector n, Traceable closest, Matter matter)
    {
        // ambient light
        Pixel pixel = new Pixel(100, 100, 100, 255);

        // set u = unit(v) for phong
        int phong = matter.getPhong();

        for (Light light : lights)
        {
            // l = l0 - p;
            // TODO: Hardcoded point light
            Vector l = ((PointLight) light).getPosition().subtract(p);

            if (!SHADOWS || !intersects(p, l, closest))
            {
                l = l.unit();

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

    /**
     * Returns whether the specified ray intersects any scene object, optionally ignoring a specified one.
     * <p>
     * The ray is specified by the line <b>p</b> = <b>u</b> + <b>v</b><i>t</i>.
     *
     * @param u
     *            the vector <b>u</b> of the ray
     * @param v
     *            the vector <b>v</b> of the ray
     * @param ignore
     *            the scene object to ignore, or {@code null} to consider all scene objects
     * @return {@code true} if the specified ray intersects a scene object, {@code false} otherwise
     */
    private boolean intersects(Vector u, Vector v, Traceable ignore)
    {
        for (Traceable object : objects)
        {
            if (object != ignore && !Double.isNaN(object.getIntersection(u, v)))
            {
                return true;
            }
        }

        return false;
    }
}
