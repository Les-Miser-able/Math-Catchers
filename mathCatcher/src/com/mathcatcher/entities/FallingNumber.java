package com.mathcatcher.entities;

import com.mathcatcher.game.DifficultySelect;
import java.awt.*;

public class FallingNumber {
    private int x, y;
    private int value;
    private double velocity;
    private boolean caught;
    private static final int SIZE = 45;

    public FallingNumber(int x, int y, int value, int level) {
        this(x, y, value, level, DifficultySelect.Difficulty.MEDIUM);
    }

    public FallingNumber(int x, int y, int value, int level, DifficultySelect.Difficulty difficulty) {
        this.x = x;
        this.y = y;
        this.value = value;
        
        // Base velocity based on difficulty
        double baseSpeed;
        switch (difficulty) {
            case EASY:
                baseSpeed = 1.5; // Slower
                break;
            case MEDIUM:
                baseSpeed = 2.0;
                break;
            case HARD:
                baseSpeed = 3.0; // Faster
                break;
            default:
                baseSpeed = 2.0;
        }
        this.velocity = baseSpeed + (level * 0.5);
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