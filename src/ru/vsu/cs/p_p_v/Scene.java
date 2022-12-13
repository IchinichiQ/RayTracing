package ru.vsu.cs.p_p_v;

import ru.vsu.cs.p_p_v.light.Light;
import ru.vsu.cs.p_p_v.object.Traceable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Scene {
    private final List<Traceable> objects;
    private final List<Light> lights;

    public Scene() {
        this.objects = new ArrayList<>();
        this.lights = new ArrayList<>();
    }

    public boolean addObject(Traceable object) {
        return objects.add(object);
    }

    public boolean removeObject(Traceable object) {
        return objects.remove(object);
    }

    public List<Traceable> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    public boolean addLight(Light light) {
        return lights.add(light);
    }

    public boolean removeLight(Light light) {
        return lights.remove(light);
    }

    public List<Light> getLights() {
        return Collections.unmodifiableList(lights);
    }
}
