package ru.vsu.cs.p_p_v;

import ru.vsu.cs.p_p_v.light.PointLight;
import ru.vsu.cs.p_p_v.object.Plane;
import ru.vsu.cs.p_p_v.object.Sphere;
import ru.vsu.cs.p_p_v.object.matter.ColorMatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.MemoryImageSource;
import java.util.concurrent.TimeUnit;

import static java.awt.event.KeyEvent.*;

public class MainPanel extends JPanel {
    private final RayTracer tracer;
    Vector cameraPosition;
    Matrix3X3 cameraRotation;

    private Image image = null;
    private long renderTimeMs = 0;

    public MainPanel() {
        this.setFocusable(true);
        this.requestFocusInWindow();

        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent event)
            {
                image = null;
            }
        });

        Scene scene = new Scene();
        cameraPosition = new Vector(0, 1, 0);

        cameraRotation = new Matrix3X3(new double[][] {new double[] {1, 0, 0},
                new double[] {0, 1, 0},
                new double[] {0, 0, 1}});

        Plane ground = new Plane(new Vector(), new Vector(0, 1, 0), new ColorMatter(Color.GREEN, 0, 0, 0));
        scene.addObject(ground);

        Sphere s1 = new Sphere(new Vector(0, 3, 3), 1, new ColorMatter(Color.RED, 50, 0.1, 50));
        scene.addObject(s1);

        Sphere s2 = new Sphere(new Vector(0, 300, 2000), 100, new ColorMatter(Color.YELLOW, 50, 1.0, 0));
        //scene.addObject(s2);

        Sphere s3 = new Sphere(new Vector(2, 2, 2), 0.5, new ColorMatter(Color.BLUE, 50, 0.5, 50));
        scene.addObject(s3);

        PointLight l1 = new PointLight(new Vector(0, 5, 0), 1.0, Color.DARK_GRAY);
        scene.addLight(l1);

        PointLight l2 = new PointLight(new Vector(0, 8, 0), 1.0, Color.darkGray);
        scene.addLight(l2);

        tracer = new RayTracer(scene);

        double delta = 0.25;
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case VK_W -> {changeCameraPosition(0, 0, delta);}
                    case VK_S -> {changeCameraPosition(0, 0, -delta);}
                    case VK_A -> {changeCameraPosition(-delta, 0, 0);}
                    case VK_D -> {changeCameraPosition(delta, 0, 0);}
                    case VK_Q -> {changeCameraPosition(0, delta, 0);}
                    case VK_E -> {changeCameraPosition(0, -delta, 0);}
                }
            }
        });
    }

    private void changeCameraPosition(double deltaX, double deltaY, double deltaZ) {
        cameraPosition = cameraPosition.add(new Vector(deltaX, deltaY, deltaZ));
        createImage();
        repaint();
    }

    private void createImage() {
        Dimension size = getSize();
        Canvas canvas = new Canvas(size.width, size.height);

        long start = System.nanoTime();
        tracer.trace(canvas, cameraPosition, cameraRotation);
        long end = System.nanoTime();
        renderTimeMs = TimeUnit.NANOSECONDS.toMillis(end - start);

        MemoryImageSource imageSource = new MemoryImageSource(size.width, size.height, canvas.getPixels(), 0, size.width);

        image = createImage(imageSource);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image == null)
            createImage();

        g.drawImage(image, 0, 0, null);

        String performanceText = String.format("%d x %d @ %d ms (%.1f fps)", image.getWidth(null), image.getHeight(null), renderTimeMs, 1000.0 / renderTimeMs);
        String positionText = String.format("x: %.1f y: %.1f z: %.1f", cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ());

        g.setColor(Color.WHITE);
        g.drawString(performanceText, 0, 12);
        g.drawString(positionText, 0, 24);
    }
}
