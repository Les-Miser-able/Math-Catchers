package com.mathcatcher.entities;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    private double x, y;
    private double velocityX;
    private int screenWidth; // Dynamic screen width for boundary checking
    private static final int WIDTH = 50;
    private static final int HEIGHT = 60;
    private static final double ACCELERATION = 1.5;
    private static final double MAX_SPEED = 8;
    private static final double FRICTION = 0.85;

    private boolean leftPressed, rightPressed;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.velocityX = 0;
        this.screenWidth = 800; // Default
    }

    public void setScreenWidth(int width) {
        this.screenWidth = width;
    }

    public void update() {
        // Handle movement
        if (leftPressed) {
            velocityX -= ACCELERATION;
        }
        if (rightPressed) {
            velocityX += ACCELERATION;
        }

        // Apply friction
        if (!leftPressed && !rightPressed) {
            velocityX *= FRICTION;
        }

        // Clamp velocity
        velocityX = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, velocityX));

        // Update position
        x += velocityX;

        // Boundary check
        if (x < 0) {
            x = 0;
            velocityX = 0;
        }
        if (x > screenWidth - WIDTH) {
            x = screenWidth - WIDTH;
            velocityX = 0;
        }
    }

    public void draw(Graphics2D g2d) {
        // Body
        g2d.setColor(new Color(255, 100, 100));
        g2d.fillRect((int)x, (int)y + 25, WIDTH, 25);

        // Head
        g2d.setColor(new Color(255, 180, 180));
        g2d.fillOval((int)x + 10, (int)y, 30, 30);

        // Arms (catching position)
        g2d.setColor(new Color(255, 150, 150));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine((int)x, (int)y + 30, (int)x - 15, (int)y + 15);
        g2d.drawLine((int)x + WIDTH, (int)y + 30, (int)x + WIDTH + 15, (int)y + 15);

        // Legs
        g2d.setColor(new Color(200, 80, 80));
        g2d.fillRect((int)x + 5, (int)y + 50, 15, 10);
        g2d.fillRect((int)x + 30, (int)y + 50, 15, 10);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, WIDTH, HEIGHT);
    }
}