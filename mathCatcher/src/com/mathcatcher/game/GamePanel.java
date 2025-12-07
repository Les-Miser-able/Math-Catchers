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
    private DifficultySelect.Difficulty difficulty;
    private int lives;
    private int correctAnswersCount; // Track correct answers for progression
    private int wrongAnswersCount; // Track wrong answers for game over
    private boolean isPaused;
    private boolean isGameOver;
    private Runnable onGameOver;
    @SuppressWarnings("unused")
    private Runnable onQuitToMenu;

    public GamePanel() {
        this(DifficultySelect.Difficulty.MEDIUM); // Default to medium
    }

    public GamePanel(DifficultySelect.Difficulty difficulty) {
        this(difficulty, null, null);
    }

    public GamePanel(DifficultySelect.Difficulty difficulty, Runnable onGameOver) {
        this(difficulty, onGameOver, null);
    }

    public GamePanel(DifficultySelect.Difficulty difficulty, Runnable onGameOver, Runnable onQuitToMenu) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(135, 206, 250));
        setFocusable(true);
        this.difficulty = difficulty;
        this.onGameOver = onGameOver;
        this.onQuitToMenu = onQuitToMenu;

        initGame();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_P) {
                    togglePause();
                } else if (!isPaused && !isGameOver) {
                    player.keyPressed(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!isPaused && !isGameOver) {
                    player.keyReleased(e);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isGameOver) return;

                // Pause button click
                int btnX = WIDTH - 120;
                int btnY = 20;
                int btnWidth = 100;
                int btnHeight = 40;
                
                if (e.getX() >= btnX && e.getX() <= btnX + btnWidth &&
                    e.getY() >= btnY && e.getY() <= btnY + btnHeight) {
                    togglePause();
                    return;
                }

                // Pause menu button clicks
                if (isPaused) {
                    int menuX = WIDTH / 2 - 150;
                    int menuY = HEIGHT / 2 - 150;
                    int menuWidth = 300;
                    int btnY2 = menuY + 90;
                    int btnHeight2 = 45;
                    int btnSpacing = 55;

                    // Resume button
                    if (e.getX() >= menuX + 25 && e.getX() <= menuX + menuWidth - 25 &&
                        e.getY() >= btnY2 && e.getY() <= btnY2 + btnHeight2) {
                        togglePause();
                    }
                    // Quit button
                    else if (e.getX() >= menuX + 25 && e.getX() <= menuX + menuWidth - 25 &&
                             e.getY() >= btnY2 + btnSpacing && e.getY() <= btnY2 + btnSpacing + btnHeight2) {
                        if (onQuitToMenu != null) {
                            onQuitToMenu.run();
                        }
                    }
                }
            }
        });

        // Create pause button
        createPauseButton();

        gameTimer = new Timer(16, this); // ~60 FPS
        gameTimer.start();
    }

    private void initGame() {
        player = new Player(WIDTH / 2, HEIGHT - 80);
        fallingNumbers = new ArrayList<>();
        // Always start with Easy difficulty regardless of what was selected
        difficulty = DifficultySelect.Difficulty.EASY;
        currentEquation = new MathEquation(1, difficulty);
        rand = new Random();
        score = 0;
        level = 1;
        spawnCounter = 0;
        lives = 3;
        correctAnswersCount = 0;
        wrongAnswersCount = 0;
        isPaused = false;
        isGameOver = false;
    }

    private void createPauseButton() {
        setLayout(null); // Use absolute positioning for pause button
    }

    private void togglePause() {
        if (!isGameOver) {
            isPaused = !isPaused;
            if (isPaused) {
                gameTimer.stop();
            } else {
                gameTimer.start();
                requestFocus();
            }
            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        if (isPaused || isGameOver) {
            return;
        }

        player.update();

        // Spawn falling numbers (rate based on difficulty)
        spawnCounter++;
        int baseSpawnRate;
        switch (difficulty) {
            case EASY:
                baseSpawnRate = 40; // Slower spawn
                break;
            case MEDIUM:
                baseSpawnRate = 30;
                break;
            case HARD:
                baseSpawnRate = 20; // Faster spawn
                break;
            default:
                baseSpawnRate = 30;
        }
        int spawnRate = Math.max(baseSpawnRate - level * 2, 10);
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
                // Remove the caught number
                fallingNumbers.remove(i);
                continue; // Skip the off-screen check since we already removed it
            }

            // Remove off-screen numbers (only if they're not caught)
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

        fallingNumbers.add(new FallingNumber(x, 0, value, level, difficulty));
    }

    private void handleNumberCatch(int value) {
        if (value == currentEquation.getAnswer()) {
            // Correct answer: add points and increment count
            score += 10 * level;
            correctAnswersCount++;

            // Level up every 50 points
            int newLevel = (score / 50) + 1;
            if (newLevel > level) {
                level = newLevel;
            }

            // Progress through difficulties based on correct answers:
            // Easy: 20 correct answers -> Medium
            // Medium: 30 correct answers -> Hard
            // Hard: 40 correct answers -> Game ends
            if (difficulty == DifficultySelect.Difficulty.EASY && correctAnswersCount >= 20) {
                difficulty = DifficultySelect.Difficulty.MEDIUM;
                correctAnswersCount = 0; // Reset count for next difficulty
                // Keep wrong answers count - it doesn't reset
            } else if (difficulty == DifficultySelect.Difficulty.MEDIUM && correctAnswersCount >= 30) {
                difficulty = DifficultySelect.Difficulty.HARD;
                correctAnswersCount = 0; // Reset count for next difficulty
                // Keep wrong answers count - it doesn't reset
            } else if (difficulty == DifficultySelect.Difficulty.HARD && correctAnswersCount >= 40) {
                // Complete all difficulties - game ends
                endGame();
                return;
            }

            currentEquation = new MathEquation(level, difficulty);
        } else {
            // Wrong answer: increment wrong answers count
            wrongAnswersCount++;
            
            // Game over after 3 wrong answers
            if (wrongAnswersCount >= 3) {
                endGame();
            }
        }
    }

    private void endGame() {
        isGameOver = true;
        gameTimer.stop();
        if (onGameOver != null) {
            onGameOver.run();
        }
        repaint();
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void resume() {
        if (isPaused) {
            togglePause();
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
        
        // Draw hearts for lives (3 - wrongAnswersCount)
        drawHearts(g2d, 20, 100);

        // Pause button
        drawPauseButton(g2d);

        // Instructions
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("← → Arrow keys to move | P/Pause to pause | Catch the correct answer!", WIDTH / 2 - 280, HEIGHT - 15);

        // Draw pause overlay
        if (isPaused) {
            drawPauseOverlay(g2d);
        }
    }

    private void drawHearts(Graphics2D g2d, int x, int y) {
        int heartsRemaining = 3 - wrongAnswersCount;
        int heartSize = 24;
        int spacing = 30;
        
        for (int i = 0; i < 3; i++) {
            int heartX = x + (i * spacing);
            
            if (i < heartsRemaining) {
                // Full heart (red)
                g2d.setColor(new Color(255, 0, 0));
            } else {
                // Empty heart (gray)
                g2d.setColor(new Color(100, 100, 100));
            }
            
            // Draw heart shape
            drawHeart(g2d, heartX, y, heartSize);
        }
    }
    
    private void drawHeart(Graphics2D g2d, int x, int y, int size) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        
        // Simplified heart shape using a polygon
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        
        xPoints[0] = centerX;
        yPoints[0] = centerY + size / 4;
        
        xPoints[1] = centerX - size / 3;
        yPoints[1] = centerY - size / 6;
        
        xPoints[2] = centerX - size / 2;
        yPoints[2] = centerY - size / 3;
        
        xPoints[3] = centerX;
        yPoints[3] = centerY - size / 2;
        
        xPoints[4] = centerX + size / 2;
        yPoints[4] = centerY - size / 3;
        
        xPoints[5] = centerX + size / 3;
        yPoints[5] = centerY - size / 6;
        
        g2d.fillPolygon(xPoints, yPoints, 6);
        
        // Draw two circles for the top of the heart
        int circleSize = size / 2;
        g2d.fillOval(centerX - size / 2, centerY - size / 2, circleSize, circleSize);
        g2d.fillOval(centerX, centerY - size / 2, circleSize, circleSize);
    }

    private void drawPauseButton(Graphics2D g2d) {
        int btnX = WIDTH - 120;
        int btnY = 20;
        int btnWidth = 100;
        int btnHeight = 40;

        // Button background
        g2d.setColor(new Color(100, 116, 139, 200));
        g2d.fillRoundRect(btnX, btnY, btnWidth, btnHeight, 8, 8);
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(btnX, btnY, btnWidth, btnHeight, 8, 8);

        // Button text
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        String text = isPaused ? "Resume" : "Pause";
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, btnX + (btnWidth - textWidth) / 2, btnY + 28);
    }

    private void drawPauseOverlay(Graphics2D g2d) {
        // Semi-transparent overlay
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Pause menu box
        int menuX = WIDTH / 2 - 150;
        int menuY = HEIGHT / 2 - 150;
        int menuWidth = 300;
        int menuHeight = 250;

        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);

        // Title
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        String title = "PAUSED";
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, menuX + (menuWidth - titleWidth) / 2, menuY + 50);

        // Button areas (will be clickable)
        int btnY = menuY + 90;
        int btnHeight = 45;
        int btnSpacing = 55;

        // Resume button
        g2d.setColor(new Color(34, 197, 94, 200));
        g2d.fillRoundRect(menuX + 25, btnY, menuWidth - 50, btnHeight, 10, 10);
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(menuX + 25, btnY, menuWidth - 50, btnHeight, 10, 10);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(Color.WHITE);
        FontMetrics btnFm = g2d.getFontMetrics();
        String resumeText = "Resume (P/ESC)";
        int resumeWidth = btnFm.stringWidth(resumeText);
        g2d.drawString(resumeText, menuX + (menuWidth - resumeWidth) / 2, btnY + 30);

        // Quit button
        g2d.setColor(new Color(239, 68, 68, 200));
        g2d.fillRoundRect(menuX + 25, btnY + btnSpacing, menuWidth - 50, btnHeight, 10, 10);
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.drawRoundRect(menuX + 25, btnY + btnSpacing, menuWidth - 50, btnHeight, 10, 10);
        String quitText = "Quit to Menu";
        int quitWidth = btnFm.stringWidth(quitText);
        g2d.drawString(quitText, menuX + (menuWidth - quitWidth) / 2, btnY + btnSpacing + 30);
    }
}