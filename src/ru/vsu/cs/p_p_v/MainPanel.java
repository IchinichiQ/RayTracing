package ru.vsu.cs.p_p_v;

import ru.vsu.cs.p_p_v.light.AmbientLight;
import ru.vsu.cs.p_p_v.light.PointLight;
import ru.vsu.cs.p_p_v.object.Plane;
import ru.vsu.cs.p_p_v.object.Sphere;
import ru.vsu.cs.p_p_v.object.material.ColorMaterial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.concurrent.TimeUnit;

import static java.awt.event.KeyEvent.*;

public class MainPanel extends JPanel {
    private final JFrame parentFrame;

    private final RayTracer tracer;

    private Camera camera;

    private Image image = null;
    private long renderTimeMs = 0;

    private int reflectionDepth = 3;

    public MainPanel(JFrame parent) {
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.parentFrame = parent;

        this.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent event)
            {
                image = null;
            }
        });

        Scene scene = new Scene();
        camera = new Camera(new Vector(1, 2.5, -1));

        Plane ground = new Plane(new Vector(), new Vector(0, 1, 0), new ColorMaterial(new Color(100, 230, 100), 0.0, 0, 0));
        scene.addObject(ground);

        Sphere s1 = new Sphere(new Vector(0, 3, 3), 1, new ColorMaterial(Color.WHITE, 0.0, 0.0, 50));
        scene.addObject(s1);

        Sphere s2 = new Sphere(new Vector(1, 3, 1), 0.5, new ColorMaterial(Color.WHITE, 0.5, 0.0, 50));
        scene.addObject(s2);

        Sphere s3 = new Sphere(new Vector(2, 2, 2), 0.5, new ColorMaterial(Color.WHITE, 0.0, 1.0, 50));
        scene.addObject(s3);

        Sphere s4 = new Sphere(new Vector(3, 3, 2), 0.5, new ColorMaterial(Color.WHITE, 0.0, 1.0, 50));
        scene.addObject(s4);

        PointLight l1 = new PointLight(new Vector(0, 4, 1), 1.0, Color.RED);
        scene.addLight(l1);

        PointLight l2 = new PointLight(new Vector(0, 8, 0), 1.0, Color.GREEN);
        scene.addLight(l2);

        AmbientLight l3 = new AmbientLight(0.2, Color.WHITE);
        scene.addLight(l3);

        tracer = new RayTracer(scene);

        double delta = 0.1;
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case VK_W -> {changeCameraPosition(new Vector(0, 0, delta), true);}
                    case VK_S -> {changeCameraPosition(new Vector(0, 0, -delta), true);}
                    case VK_A -> {changeCameraPosition(new Vector(-delta, 0, 0), true);}
                    case VK_D -> {changeCameraPosition(new Vector(delta, 0, 0), true);}
                    case VK_SPACE -> {changeCameraPosition(new Vector(0, delta, 0), false);}
                    case VK_SHIFT -> {changeCameraPosition(new Vector(0, -delta, 0), false);}
                    case VK_X -> {changeReflectionDepth(1);}
                    case VK_Z -> {changeReflectionDepth(-1);}
                    case VK_R -> {
                        createImage();
                        repaint();
                    }
                }
            }
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {
            private boolean mouseCaptured = false;
            private final double mouseSensitivity = 0.3;
            private int windowCenterX, windowCenterY;
            private int prevX, prevY;

            private void moveCursorToScreenCenter() {
                windowCenterX = parentFrame.getLocationOnScreen().x + parentFrame.getWidth() / 2;
                windowCenterY = parentFrame.getLocationOnScreen().y + parentFrame.getHeight() / 2;

                moveCursorToPoint(windowCenterX, windowCenterY);
            }

            private void moveCursorToPoint(int x, int y) {
                GraphicsDevice device = parentFrame.getGraphicsConfiguration().getDevice();
                Robot robot;
                try {
                    robot = new Robot(device);
                } catch (AWTException ex) {
                    throw new RuntimeException(ex);
                }

                robot.mouseMove(x, y);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!mouseCaptured) {
                    prevX = e.getXOnScreen();
                    prevY = e.getYOnScreen();

                    parentFrame.setCursor( parentFrame.getToolkit().createCustomCursor(
                            new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),
                            new Point(),
                            null ) );
                    moveCursorToScreenCenter();

                    mouseCaptured = true;
                } else {
                    moveCursorToPoint(prevX, prevY);

                    parentFrame.setCursor(Cursor.DEFAULT_CURSOR);

                    mouseCaptured = false;
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (mouseCaptured) {
                    int deltaX = e.getXOnScreen() - windowCenterX;
                    int deltaY = e.getYOnScreen() - windowCenterY;

                    // x - yaw
                    // y - pitch

                    double deltaYaw = deltaX * mouseSensitivity;
                    double deltaPitch = deltaY * mouseSensitivity;

                    //System.out.printf("Yaw: %.2f, Pitch: %.2f\n", deltaYaw, deltaPitch);

                    camera.addYaw(deltaYaw);
                    camera.addPitch(deltaPitch);

                    moveCursorToScreenCenter();

                    createImage();
                    repaint();
                }
            }
        };

        this.addMouseMotionListener(mouseAdapter);
        this.addMouseListener(mouseAdapter);
    }

    private void changeCameraPosition(Vector delta, boolean relativeToCamera) {
        if (relativeToCamera)
            delta = delta.rotateYP(camera.getYaw(), camera.getPitch());

        camera.setPosition(camera.getPosition().add(delta));

        createImage();
        repaint();
    }

    private void changeReflectionDepth(int delta) {
        reflectionDepth = Math.max(0, reflectionDepth + delta);

        createImage();
        repaint();
    }

    private void createImage() {
        Dimension size = getSize();
        Canvas canvas = new Canvas(size.width, size.height);

        long start = System.nanoTime();
        tracer.trace(canvas, camera, reflectionDepth);
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

        Vector cameraPosition = camera.getPosition();

        String positionInfo = String.format("x: %.1f y: %.1f z: %.1f", cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ());
        String rotationInfo = String.format("Yaw: %.1f Pitch: %.1f", camera.getYaw(), camera.getPitch());
        String performanceInfo = String.format("%d x %d @ %d ms (%.1f fps)", image.getWidth(null), image.getHeight(null), renderTimeMs, 1000.0 / renderTimeMs);
        String renderInfo = String.format("Reflection depth: %d", reflectionDepth);

        g.setColor(Color.WHITE);
        g.drawString(performanceInfo, 0, 12);
        g.drawString(positionInfo, 0, 24);
        g.drawString(rotationInfo, 0, 36);
        g.drawString(renderInfo, 0, 48);
    }
}
