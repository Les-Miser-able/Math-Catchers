package com.mathcatcher.entities;

import java.awt.*;

public class FallingNumber {
    private int x, y;
    private int value;
    private double velocity;
    private boolean caught;
    private static final int SIZE = 45;

    public FallingNumber(int x, int y, int value, int level) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.velocity = 2 + (level * 0.5);
        this.caught = false;
    }

    public void update() {
        y += velocity;
    }

    public void draw(Graphics2D g2d) {
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(x + 3, y + 3, SIZE, SIZE);

        // Number circle
        g2d.setColor(new Color(255, 215, 0));
        g2d.fillOval(x, y, SIZE, SIZE);

        // Border
        g2d.setColor(new Color(218, 165, 32));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x, y, SIZE, SIZE);

        // Number text
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String numStr = String.valueOf(value);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (SIZE - fm.stringWidth(numStr)) / 2;
        int textY = y + (SIZE + fm.getAscent()) / 2 - 2;
        g2d.drawString(numStr, textX, textY);
    }

    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public int getValue() {
        return value;
    }

    public boolean isCaught() {
        return caught;
    }

    public void setCaught(boolean caught) {
        this.caught = caught;
    }
}