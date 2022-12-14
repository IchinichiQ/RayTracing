package ru.vsu.cs.p_p_v;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends JFrame {
    public MainWindow() throws HeadlessException {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Ray Tracing");

        setSize(320, 256);

        this.add(new MainPanel(this));
    }
}
