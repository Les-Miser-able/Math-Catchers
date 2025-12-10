package com.mathcatcher.entities;

import com.mathcatcher.game.DifficultySelect;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class FallingNumber {
    private int x, y;
    private int value;
    private double velocity;
    private boolean caught;
    private double rotation;
    private double rotationSpeed;
    private double glowPhase;
    private boolean isPaused;
    private static final int SIZE = 45;

    public FallingNumber(int x, int y, int value, int level) {
        this(x, y, value, level, DifficultySelect.Difficulty.MEDIUM);
    }

    public FallingNumber(int x, int y, int value, int level, DifficultySelect.Difficulty difficulty) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.rotation = Math.random() * 360;
        this.rotationSpeed = (Math.random() - 0.5) * 3; // Random rotation speed
        this.glowPhase = Math.random() * Math.PI * 2; // Random starting phase for glow
        this.isPaused = false;
        
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
        if (!isPaused) {
            y += velocity;
            rotation += rotationSpeed;
            glowPhase += 0.1; // Animate glow
        }
    }
    
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public void draw(Graphics2D g2d) {
        // Save original transform
        AffineTransform originalTransform = g2d.getTransform();
        
        // Translate to center of heart for rotation
        int centerX = x + SIZE / 2;
        int centerY = y + SIZE / 2;
        g2d.translate(centerX, centerY);
        g2d.rotate(Math.toRadians(rotation));
        g2d.translate(-centerX, -centerY);
        
        // Animated glow effect
        double glowIntensity = (Math.sin(glowPhase) + 1) / 2; // 0 to 1
        int glowAlpha = (int) (30 + glowIntensity * 40); // 30-70 alpha
        
        // Glow effect (multiple layers) - draw as hearts
        if (!isPaused) {
            for (int i = 3; i >= 1; i--) {
                int glowSize = SIZE + i * 8;
                int glowX = x - (glowSize - SIZE) / 2;
                int glowY = y - (glowSize - SIZE) / 2;
                g2d.setColor(new Color(255, 0, 0, glowAlpha / (i * 2)));
                drawHeartShape(g2d, glowX, glowY, glowSize);
            }
        }
        
        // Shadow (darker red on left side)
        g2d.setColor(new Color(150, 0, 0, 100));
        drawHeartShape(g2d, x + 2, y + 2, SIZE);

        // Main heart shape - change color when paused
        if (isPaused) {
            // Grayed out when paused
            g2d.setColor(new Color(150, 150, 150));
        } else {
            // Bright red heart
            g2d.setColor(new Color(255, 0, 0));
        }
        drawHeartShape(g2d, x, y, SIZE);
        
        // Darker red shadow on left side for depth (only when not paused)
        if (!isPaused) {
            g2d.setColor(new Color(200, 0, 0, 150));
            drawHeartShape(g2d, x - 2, y, SIZE);
        }

        // Number text
        g2d.setColor(isPaused ? new Color(80, 80, 80) : Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String numStr = String.valueOf(value);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (SIZE - fm.stringWidth(numStr)) / 2;
        int textY = y + (SIZE + fm.getAscent()) / 2 - 2;
        g2d.drawString(numStr, textX, textY);
        
        // Restore original transform
        g2d.setTransform(originalTransform);
    }
    
    private void drawHeartShape(Graphics2D g2d, int x, int y, int size) {
        GeneralPath heart = new GeneralPath();
        
        // Heart shape parameters - using a more accurate heart shape
        float width = size;
        float height = size;
        float centerX = x + width / 2;
        float topY = y;
        float bottomY = y + height;
        
        // Start from the bottom point (V shape)
        float bottomX = centerX;
        
        // Left side of heart
        // Bottom left curve
        heart.moveTo(bottomX, bottomY);
        heart.curveTo(
            bottomX - width * 0.1f, bottomY - height * 0.15f,  // Control point 1
            bottomX - width * 0.3f, bottomY - height * 0.3f,  // Control point 2
            bottomX - width * 0.35f, topY + height * 0.25f  // End point (left side)
        );
        
        // Left top lobe (circular part)
        heart.curveTo(
            bottomX - width * 0.35f, topY + height * 0.1f,  // Control point 1
            bottomX - width * 0.2f, topY,  // Control point 2
            bottomX, topY + height * 0.15f  // End point (center top)
        );
        
        // Right top lobe (circular part)
        heart.curveTo(
            bottomX + width * 0.2f, topY,  // Control point 1
            bottomX + width * 0.35f, topY + height * 0.1f,  // Control point 2
            bottomX + width * 0.35f, topY + height * 0.25f  // End point (right side)
        );
        
        // Right side of heart
        heart.curveTo(
            bottomX + width * 0.3f, bottomY - height * 0.3f,  // Control point 1
            bottomX + width * 0.1f, bottomY - height * 0.15f,  // Control point 2
            bottomX, bottomY  // End point (back to bottom)
        );
        
        heart.closePath();
        g2d.fill(heart);
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
    
    public boolean getIsPaused() {
        return isPaused;
    }
}