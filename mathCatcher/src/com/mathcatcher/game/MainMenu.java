package com.mathcatcher.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel {

    public MainMenu(Runnable onStartGame, Runnable onQuitGame, Runnable onViewLeaderboard, Runnable onSettings) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 70, 40)); // More bottom padding to avoid green ground

        // Create main container panel with styled background
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Semi-transparent black background (bg-black/30) with shadow effect
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(4, 4, getWidth(), getHeight(), 32, 32);
                
                // Main background
                g2d.setColor(new Color(0, 0, 0, 76)); // ~30% opacity
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32); // rounded-2xl

                // Enhanced border (border-white/10)
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 32, 32);
            }
        };
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // ===== Title Section =====
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Draw gradient text "Math Catchers"
                Font titleFont = new Font("SansSerif", Font.BOLD, 64); // Normal size
                String title = "Math Catchers";
                
                // Create TextLayout for gradient text rendering
                java.awt.font.TextLayout textLayout = new java.awt.font.TextLayout(
                    title, titleFont, g2d.getFontRenderContext()
                );
                
                FontMetrics fm = g2d.getFontMetrics(titleFont);
                int textWidth = fm.stringWidth(title);
                int textHeight = fm.getHeight();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() - textHeight) / 2 + fm.getAscent() - 20;

                // Create gradient: yellow-400 -> pink-500 -> purple-500
                Color yellow = new Color(255, 230, 0);    // yellow-400
                Color pink = new Color(236, 72, 153);     // pink-500
                Color purple = new Color(168, 85, 247);   // purple-500
                
                // Use LinearGradientPaint for smooth gradient
                java.awt.LinearGradientPaint gradient = new java.awt.LinearGradientPaint(
                    x, 0, x + textWidth, 0,
                    new float[]{0.0f, 0.5f, 1.0f},
                    new Color[]{yellow, pink, purple}
                );
                
                // Draw text as shape with gradient fill
                java.awt.Shape textShape = textLayout.getOutline(
                    java.awt.geom.AffineTransform.getTranslateInstance(x, y)
                );
                g2d.setPaint(gradient);
                g2d.fill(textShape);

                // Draw subtitle
                Font subtitleFont = new Font("SansSerif", Font.PLAIN, 16);
                g2d.setFont(subtitleFont);
                g2d.setColor(new Color(209, 213, 219)); // gray-300
                String subtitle = "Catch Numbers, Solve Equations!";
                FontMetrics subFm = g2d.getFontMetrics();
                int subX = (getWidth() - subFm.stringWidth(subtitle)) / 2;
                int subY = y + 25;
                g2d.drawString(subtitle, subX, subY);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(0, 140));
        titlePanel.setMinimumSize(new Dimension(0, 140));

        // ===== Buttons Section =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Start Game Button
        GradientButton startButton = new GradientButton("▶ Start Game",
            new Color(34, 197, 94), new Color(5, 150, 105), // green-500 to emerald-600
            new Color(22, 163, 74), new Color(5, 122, 85)); // hover colors
        startButton.addActionListener(e -> onStartGame.run());

        // Leaderboard Button
        GradientButton leaderboardButton = new GradientButton("🏆 Leaderboard",
            new Color(234, 179, 8), new Color(234, 88, 12), // yellow-500 to orange-600
            new Color(202, 138, 4), new Color(194, 65, 12)); // hover colors
        leaderboardButton.addActionListener(e -> onViewLeaderboard.run());

        // Settings Button
        GradientButton settingsButton = new GradientButton("⚙ Settings",
            new Color(59, 130, 246), new Color(37, 99, 235), // blue-500 to blue-600
            new Color(29, 78, 216), new Color(30, 64, 175)); // hover colors
        settingsButton.addActionListener(e -> onSettings.run());

        // Quit Game Button
        GradientButton quitButton = new GradientButton("✖ Quit Game",
            new Color(239, 68, 68), new Color(219, 39, 119), // red-500 to pink-600
            new Color(220, 38, 38), new Color(190, 24, 93)); // hover colors
        quitButton.addActionListener(e -> onQuitGame.run());

        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(12)); // Normal spacing
        buttonPanel.add(leaderboardButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(quitButton);

        // ===== Footer =====
        JLabel footer = new JLabel("Catch the correct numbers to solve equations!") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        footer.setForeground(new Color(156, 163, 175)); // gray-400
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        // Add components to container
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(buttonPanel, BorderLayout.CENTER);
        container.add(footer, BorderLayout.SOUTH);

        // Add container to main panel
        add(container, BorderLayout.CENTER);
    }

    // Custom gradient button with hover effects
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
            setFont(new Font("SansSerif", Font.BOLD, 18)); // Normal font size
            setPreferredSize(new Dimension(0, 55)); // Normal button height
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
            setMinimumSize(new Dimension(0, 55));

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

            int width = getWidth();
            int height = getHeight();
            
            // Draw gradient background
            Color c1 = isHovered ? hoverColor1 : color1;
            Color c2 = isHovered ? hoverColor2 : color2;
            GradientPaint gradient = new GradientPaint(
                0, 0, c1,
                width, height, c2
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, width, height, 12, 12); // Normal rounded corners

            // Draw shadow/glow on hover
            if (isHovered) {
                // Simple glow effect
                for (int i = 1; i <= 2; i++) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f / i));
                    g2d.setStroke(new BasicStroke(2 * i));
                    g2d.setColor(c1);
                    g2d.drawRoundRect(i, i, width - 2 * i, height - 2 * i, 12, 12);
                }
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }

            // Draw text
            g2d.setColor(Color.WHITE);
            g2d.setFont(getFont());
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            FontMetrics fm = g2d.getFontMetrics();
            String text = getText();
            int textX = (width - fm.stringWidth(text)) / 2;
            int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, textX, textY);
        }
    }
}
