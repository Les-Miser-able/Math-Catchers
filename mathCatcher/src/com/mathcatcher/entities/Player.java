package com.mathcatcher.entities;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    private double x, y;
    private double velocityX;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 60;
    private static final double ACCELERATION = 1.5;
    private static final double MAX_SPEED = 8;
    private static final double FRICTION = 0.85;

    private boolean leftPressed, rightPressed;
    private double walkAnimationPhase;
    private double catchAnimationPhase;
    private boolean isCatching;
    private int catchAnimationTimer;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.velocityX = 0;
        this.walkAnimationPhase = 0;
        this.catchAnimationPhase = 0;
        this.isCatching = false;
        this.catchAnimationTimer = 0;
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
        if (x > 800 - WIDTH) {
            x = 800 - WIDTH;
            velocityX = 0;
        }
        
        // Update walk animation
        if (Math.abs(velocityX) > 0.1) {
            walkAnimationPhase += Math.abs(velocityX) * 0.3;
        } else {
            walkAnimationPhase *= 0.9; // Slow down animation when stopping
        }
        
        // Update catch animation
        if (isCatching) {
            catchAnimationTimer++;
            catchAnimationPhase = Math.sin(catchAnimationTimer * 0.3) * 0.5;
            if (catchAnimationTimer > 20) {
                isCatching = false;
                catchAnimationTimer = 0;
                catchAnimationPhase = 0;
            }
        }
    }
    
    public void triggerCatchAnimation() {
        isCatching = true;
        catchAnimationTimer = 0;
    }

    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int drawX = (int)x;
        int drawY = (int)y;
        
        // Calculate leg positions for walking animation
        double legOffset = Math.sin(walkAnimationPhase) * 3;
        double armSwing = Math.sin(walkAnimationPhase) * 8;
        
        // Catch animation - slight bounce
        if (isCatching) {
            drawY += (int)(catchAnimationPhase * 5);
        }
        
        // Body
        g2d.setColor(new Color(255, 100, 100));
        g2d.fillRoundRect(drawX, drawY + 25, WIDTH, 25, 5, 5);

        // Head
        g2d.setColor(new Color(255, 180, 180));
        g2d.fillOval(drawX + 10, drawY, 30, 30);
        
        // Eyes
        g2d.setColor(Color.BLACK);
        g2d.fillOval(drawX + 18, drawY + 8, 4, 4);
        g2d.fillOval(drawX + 28, drawY + 8, 4, 4);

        // Arms (animated catching position)
        g2d.setColor(new Color(255, 150, 150));
        g2d.setStroke(new BasicStroke(5));
        
        // Left arm
        int leftArmX = drawX - 15;
        int leftArmY = drawY + 15;
        if (isCatching) {
            leftArmX += (int)(catchAnimationPhase * 10);
            leftArmY -= (int)(catchAnimationPhase * 5);
        } else {
            leftArmX += (int)armSwing;
        }
        g2d.drawLine(drawX, drawY + 30, leftArmX, leftArmY);
        
        // Right arm
        int rightArmX = drawX + WIDTH + 15;
        int rightArmY = drawY + 15;
        if (isCatching) {
            rightArmX -= (int)(catchAnimationPhase * 10);
            rightArmY -= (int)(catchAnimationPhase * 5);
        } else {
            rightArmX -= (int)armSwing;
        }
        g2d.drawLine(drawX + WIDTH, drawY + 30, rightArmX, rightArmY);

        // Legs (walking animation)
        g2d.setColor(new Color(200, 80, 80));
        g2d.fillRoundRect(drawX + 5, drawY + 50, 15, 10, 3, 3);
        g2d.fillRoundRect(drawX + 30, drawY + 50, 15, 10, 3, 3);
        
        // Animate legs
        if (Math.abs(velocityX) > 0.1) {
            g2d.fillRoundRect(drawX + 5, drawY + 50 + (int)legOffset, 15, 10, 3, 3);
            g2d.fillRoundRect(drawX + 30, drawY + 50 - (int)legOffset, 15, 10, 3, 3);
        }
        
        // Catch effect glow
        if (isCatching) {
            int glowSize = WIDTH + 20;
            int glowX = drawX - 10;
            int glowY = drawY - 10;
            float alpha = (float)(0.5 * (1 - Math.abs(catchAnimationPhase)));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(new Color(255, 255, 0, 150));
            g2d.fillOval(glowX, glowY, glowSize, glowSize);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
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