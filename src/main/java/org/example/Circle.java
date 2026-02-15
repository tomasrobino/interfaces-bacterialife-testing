package org.example;

import javax.swing.*;
import java.awt.*;

import static org.example.BacteriaLifeUI.BACTERIA_SIZE;

public class Circle extends JButton {
    private Color color;
    private final int diameter;

    public Circle(Color color) {
        this.color = color;
        this.diameter = BACTERIA_SIZE;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setEnabled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(color);
        g.fillOval(0, 0, diameter, diameter); // use diameter instead of getWidth()/getHeight() if you want consistent circles
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(diameter, diameter);
    }

    public void setCircleColor(Color c) {
        this.color = c;
        repaint();
    }

    public Color getColor() {
        return color;
    }
}