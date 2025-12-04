package com.mathcatcher.game;

import com.mathcatcher.entities.Player;
import com.mathcatcher.entities.FallingNumber;
import com.mathcatcher.utils.MathEquation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Player player;
    private ArrayList<FallingNumber> fallingNumbers;
    private MathEquation currentEquation;
    private Timer gameTimer;
    private Random rand;
    private int score;
    private int level;
    private int spawnCounter;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(135, 206, 250));
        setFocusable(true);

        initGame();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                player.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }
        });

        gameTimer = new Timer(16, this); // ~60 FPS
        gameTimer.start();
    }

    private void initGame() {
        player = new Player(WIDTH / 2, HEIGHT - 80);
        fallingNumbers = new ArrayList<>();
        currentEquation = new MathEquation(1);
        rand = new Random();
        score = 0;
        level = 1;
        spawnCounter = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        player.update();

        // Spawn falling numbers
        spawnCounter++;
        int spawnRate = Math.max(30 - level * 2, 15);
        if (spawnCounter >= spawnRate) {
            spawnCounter = 0;
            spawnNumber();
        }

        // Update falling numbers
        for (int i = fallingNumbers.size() - 1; i >= 0; i--) {
            FallingNumber num = fallingNumbers.get(i);
            num.update();

            // Check collision
            if (num.getBounds().intersects(player.getBounds()) && !num.isCaught()) {
                num.setCaught(true);
                handleNumberCatch(num.getValue());
            }

            // Remove off-screen numbers
            if (num.isOffScreen(HEIGHT)) {
                fallingNumbers.remove(i);
            }
        }
    }

    private void spawnNumber() {
        int x = rand.nextInt(WIDTH - 50);
        int value;

        // 40% chance correct answer, 60% random numbers
        if (rand.nextDouble() < 0.4) {
            value = currentEquation.getAnswer();
        } else {
            value = rand.nextInt(50) + 1;
            // Avoid spawning the correct answer
            if (value == currentEquation.getAnswer()) {
                value += rand.nextInt(10) + 1;
            }
        }

        fallingNumbers.add(new FallingNumber(x, 0, value, level));
    }

    private void handleNumberCatch(int value) {
        if (value == currentEquation.getAnswer()) {
            score += 10 * level;
            fallingNumbers.clear();

            // Level up every 5 correct answers
            if (score % 50 == 0) {
                level++;
            }

            currentEquation = new MathEquation(level);
        } else {
            score = Math.max(0, score - 5);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2d);
        drawUI(g2d);

        // Draw entities
        for (FallingNumber num : fallingNumbers) {
            if (!num.isCaught()) {
                num.draw(g2d);
            }
        }

        player.draw(g2d);
    }

    private void drawBackground(Graphics2D g2d) {
        // Sky gradient
        GradientPaint sky = new GradientPaint(0, 0, new Color(135, 206, 250),
                0, HEIGHT, new Color(176, 224, 230));
        g2d.setPaint(sky);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Ground
        g2d.setColor(new Color(34, 139, 34));
        g2d.fillRect(0, HEIGHT - 50, WIDTH, 50);
    }

    private void drawUI(Graphics2D g2d) {
        // Equation box
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRoundRect(WIDTH / 2 - 150, 20, 300, 60, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(WIDTH / 2 - 150, 20, 300, 60, 15, 15);

        // Equation text
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        String equation = currentEquation.toString();
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(equation);
        g2d.drawString(equation, WIDTH / 2 - textWidth / 2, 60);

        // Score and level
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Score: " + score, 20, 40);
        g2d.drawString("Level: " + level, 20, 70);

        // Instructions
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("← → Arrow keys to move | Catch the correct answer!", WIDTH / 2 - 200, HEIGHT - 15);
    }
}