package ru.vsu.cs.p_p_v.object.material;

import java.awt.*;

public interface Material {
    Color getColor();

    double getOpacity();

    double getReflective();

    int getPhong();
}
