package com.mathcatcher.game;

import com.mathcatcher.utils.ScoreManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOverPanel extends JPanel {
    private int finalScore;
    private DifficultySelect.Difficulty difficulty;
    private Runnable onContinue;
    private Runnable onBackToMenu;

    public GameOverPanel(int score, DifficultySelect.Difficulty difficulty, 
                        Runnable onContinue, Runnable onBackToMenu) {
        this.finalScore = score;
        this.difficulty = difficulty;
        this.onContinue = onContinue;
        this.onBackToMenu = onBackToMenu;
        
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Save score
        ScoreManager.addScore(score, difficulty);
        
        createGameOverScreen();
    }

    private void createGameOverScreen() {
        // Main container with styled background
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Semi-transparent black background with shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(4, 4, getWidth(), getHeight(), 32, 32);
                
                // Main background
                g2d.setColor(new Color(0, 0, 0, 180)); // Darker for game over
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);

                // Enhanced border
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 32, 32);
            }
        };
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title Panel
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Draw gradient text "Game Over"
                Font titleFont = new Font("SansSerif", Font.BOLD, 64);
                String title = "Game Over";
                
                java.awt.font.TextLayout textLayout = new java.awt.font.TextLayout(
                    title, titleFont, g2d.getFontRenderContext()
                );
                
                FontMetrics fm = g2d.getFontMetrics(titleFont);
                int textWidth = fm.stringWidth(title);
                int textHeight = fm.getHeight();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() - textHeight) / 2 + fm.getAscent() - 20;

                // Red gradient for game over
                Color red1 = new Color(239, 68, 68);    // red-500
                Color red2 = new Color(220, 38, 38);    // red-600
                
                java.awt.LinearGradientPaint gradient = new java.awt.LinearGradientPaint(
                    x, y - textHeight, x, y,
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{red1, red2, red1}
                );
                
                g2d.setPaint(gradient);
                textLayout.draw(g2d, x, y);

                // Subtitle
                Font subtitleFont = new Font("SansSerif", Font.PLAIN, 18);
                g2d.setFont(subtitleFont);
                g2d.setColor(new Color(255, 255, 255, 200));
                String subtitle = "You ran out of lives!";
                FontMetrics subFm = g2d.getFontMetrics();
                int subX = (getWidth() - subFm.stringWidth(subtitle)) / 2;
                g2d.drawString(subtitle, subX, y + 40);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(0, 140));
        titlePanel.setMinimumSize(new Dimension(0, 140));

        // Score display panel
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setOpaque(false);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        JLabel scoreLabel = new JLabel("Final Score: " + finalScore);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel difficultyLabel = new JLabel("Difficulty: " + difficulty.name());
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        difficultyLabel.setForeground(new Color(200, 200, 200));

        scorePanel.add(scoreLabel);
        scorePanel.add(difficultyLabel);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Continue button
        GradientButton continueButton = new GradientButton("Continue",
            new Color(34, 197, 94), new Color(22, 163, 74), // green-500 to green-600
            new Color(22, 163, 74), new Color(21, 128, 61)); // darker on hover
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        continueButton.setPreferredSize(new Dimension(0, 55));
        continueButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        continueButton.setMinimumSize(new Dimension(0, 55));
        continueButton.addActionListener(e -> onContinue.run());

        // Back to menu button
        GradientButton menuButton = new GradientButton("Back to Main Menu",
            new Color(100, 116, 139), new Color(71, 85, 105), // slate gray
            new Color(71, 85, 105), new Color(51, 65, 85)); // darker on hover
        menuButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        menuButton.setPreferredSize(new Dimension(0, 55));
        menuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        menuButton.setMinimumSize(new Dimension(0, 55));
        menuButton.addActionListener(e -> onBackToMenu.run());

        buttonPanel.add(continueButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(menuButton);

        // Assemble components
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(scorePanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        add(container, BorderLayout.CENTER);
    }

    // Simple gradient button (reusing style from other panels)
    private static class GradientButton extends JButton {
        private Color color1, color2;
        private Color hoverColor1, hoverColor2;
        private boolean isHovered = false;

        public GradientButton(String text, Color c1, Color c2, Color h1, Color h2) {
            super(text);
            this.color1 = c1;
            this.color2 = c2;
            this.hoverColor1 = h1;
            this.hoverColor2 = h2;

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setMargin(new Insets(0, 0, 0, 0));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Shadow
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillRoundRect(2, 2, width, height, 12, 12);

            // Background
            Color c1 = isHovered ? hoverColor1 : this.color1;
            Color c2 = isHovered ? hoverColor2 : this.color2;
            if (c1.equals(c2)) {
                g2d.setColor(c1);
            } else {
                GradientPaint gradient = new GradientPaint(0, 0, c1, width, height, c2);
                g2d.setPaint(gradient);
            }
            g2d.fillRoundRect(0, 0, width, height, 12, 12);

            // Border
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.drawRoundRect(0, 0, width - 1, height - 1, 12, 12);

            // Text
            g2d.setColor(Color.WHITE);
            g2d.setFont(getFont());
            FontMetrics fm = g2d.getFontMetrics();
            String text = getText();
            int textX = (width - fm.stringWidth(text)) / 2;
            int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, textX, textY);
        }
    }
}



