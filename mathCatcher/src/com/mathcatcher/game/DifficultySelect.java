package com.mathcatcher.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DifficultySelect extends JPanel {
    
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public DifficultySelect(Runnable onBack, DifficultyCallback onSelectDifficulty) {
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

                // Semi-transparent black background with shadow
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

        // ===== Back Button =====
        GradientButton backButton = new GradientButton("← Back",
            new Color(100, 116, 139), new Color(71, 85, 105), // Slate gray gradient
            new Color(71, 85, 105), new Color(51, 65, 85)); // Darker on hover
        backButton.addActionListener(e -> onBack.run());

        // ===== Title Section =====
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Draw gradient text "Select Difficulty"
                Font titleFont = new Font("SansSerif", Font.BOLD, 64); // Same size as main menu
                String title = "Select Difficulty";
                
                // Create TextLayout for gradient text rendering
                java.awt.font.TextLayout textLayout = new java.awt.font.TextLayout(
                    title, titleFont, g2d.getFontRenderContext()
                );
                
                FontMetrics fm = g2d.getFontMetrics(titleFont);
                int textWidth = fm.stringWidth(title);
                int textHeight = fm.getHeight();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() - textHeight) / 2 + fm.getAscent() - 20;

                // Create gradient: yellow-400 -> pink-500 -> purple-500 (same as main menu)
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
                String subtitle = "Choose your challenge level";
                FontMetrics subFm = g2d.getFontMetrics();
                int subX = (getWidth() - subFm.stringWidth(subtitle)) / 2;
                int subY = y + 25;
                g2d.drawString(subtitle, subX, subY);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(0, 140)); // Same height as main menu title
        titlePanel.setMinimumSize(new Dimension(0, 140));

        // ===== Difficulty Buttons =====
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new BoxLayout(difficultyPanel, BoxLayout.Y_AXIS));
        difficultyPanel.setOpaque(false);
        difficultyPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Easy Difficulty
        DifficultyButton easyButton = new DifficultyButton(
            "⚡ Easy",
            "Addition & subtraction (1-10)",
            new Color(34, 197, 94), new Color(5, 150, 105), // green-500 to emerald-600
            new Color(22, 163, 74), new Color(5, 122, 85)  // hover colors
        );
        easyButton.addActionListener(e -> onSelectDifficulty.onDifficultySelected(Difficulty.EASY));

        // Medium Difficulty
        DifficultyButton mediumButton = new DifficultyButton(
            "🔥 Medium",
            "Includes multiplication (1-20)",
            new Color(234, 179, 8), new Color(234, 88, 12), // yellow-500 to orange-600
            new Color(202, 138, 4), new Color(194, 65, 12)  // hover colors
        );
        mediumButton.addActionListener(e -> onSelectDifficulty.onDifficultySelected(Difficulty.MEDIUM));

        // Hard Difficulty
        DifficultyButton hardButton = new DifficultyButton(
            "☠ Hard",
            "All operations, faster speed (1-30)",
            new Color(239, 68, 68), new Color(219, 39, 119), // red-500 to pink-600
            new Color(220, 38, 38), new Color(190, 24, 93)  // hover colors
        );
        hardButton.addActionListener(e -> onSelectDifficulty.onDifficultySelected(Difficulty.HARD));

        difficultyPanel.add(easyButton);
        difficultyPanel.add(Box.createVerticalStrut(12)); // Same spacing as main menu
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(Box.createVerticalStrut(12));
        difficultyPanel.add(hardButton);

        // Add components to container
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        // Wrap back button in a panel to prevent stretching
        JPanel backButtonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backButtonWrapper.setOpaque(false);
        backButtonWrapper.add(backButton);
        topPanel.add(backButtonWrapper, BorderLayout.WEST);
        topPanel.add(titlePanel, BorderLayout.CENTER);

        container.add(topPanel, BorderLayout.NORTH);
        container.add(difficultyPanel, BorderLayout.CENTER);

        // Add container to main panel
        add(container, BorderLayout.CENTER);
    }

    // Interface for difficulty selection callback
    public interface DifficultyCallback {
        void onDifficultySelected(Difficulty difficulty);
    }

    // Custom difficulty button with icon and description
    private static class DifficultyButton extends JButton {
        private Color color1, color2;
        private Color hoverColor1, hoverColor2;
        private boolean isHovered = false;
        private String name;
        private String description;

        public DifficultyButton(String name, String description, Color c1, Color c2, Color h1, Color h2) {
            super();
            this.name = name;
            this.description = description;
            this.color1 = c1;
            this.color2 = c2;
            this.hoverColor1 = h1;
            this.hoverColor2 = h2;

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setPreferredSize(new Dimension(0, 70)); // Slightly taller to fit description
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
            setMinimumSize(new Dimension(0, 70));

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

            // Draw gradient background
            Color c1 = isHovered ? hoverColor1 : this.color1;
            Color c2 = isHovered ? hoverColor2 : this.color2;
            GradientPaint gradient = new GradientPaint(
                0, 0, c1,
                width, height, c2
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, width, height, 12, 12); // Same rounded corners as main menu

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

            // Draw icon container (bg-white/20) - smaller to fit
            int iconSize = 40;
            int iconX = 20;
            int iconY = (height - iconSize) / 2;
            g2d.setColor(new Color(255, 255, 255, 51)); // white/20
            g2d.fillRoundRect(iconX, iconY, iconSize, iconSize, 8, 8);

            // Draw icon (extract emoji from name)
            String icon = name.substring(0, name.indexOf(' '));
            Font iconFont = new Font("SansSerif", Font.PLAIN, 20); // Smaller icon
            g2d.setFont(iconFont);
            FontMetrics iconFm = g2d.getFontMetrics();
            int iconTextX = iconX + (iconSize - iconFm.stringWidth(icon)) / 2;
            int iconTextY = iconY + (iconSize + iconFm.getAscent()) / 2 - 2;
            g2d.setColor(Color.WHITE);
            g2d.drawString(icon, iconTextX, iconTextY);

            // Draw name and description
            int textX = iconX + iconSize + 20;
            int textY = height / 2;

            // Draw name
            Font nameFont = new Font("SansSerif", Font.BOLD, 18);
            g2d.setFont(nameFont);
            String nameText = name.substring(name.indexOf(' ') + 1);
            g2d.setColor(Color.WHITE);
            int nameY = textY - 8;
            g2d.drawString(nameText, textX, nameY);

            // Draw description
            Font descFont = new Font("SansSerif", Font.PLAIN, 13);
            g2d.setFont(descFont);
            g2d.setColor(new Color(255, 255, 255, 204)); // white/80
            FontMetrics descFm = g2d.getFontMetrics();
            int descY = textY + descFm.getAscent() + 4;
            g2d.drawString(description, textX, descY);
        }
    }

    // Simple gradient button for back button
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
            setFont(new Font("SansSerif", Font.BOLD, 12)); // Smaller font for smaller button
            setMargin(new Insets(0, 0, 0, 0)); // Remove default button margins

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
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            String text = getText();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            // Small padding: 8px left/right, 4px top/bottom
            Dimension size = new Dimension(textWidth + 16, textHeight + 8);
            setMaximumSize(size);
            setMinimumSize(size);
            return size;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Draw background
            Color c1 = isHovered ? hoverColor1 : this.color1;
            Color c2 = isHovered ? hoverColor2 : this.color2;
            if (c1.equals(c2)) {
                g2d.setColor(c1);
            } else {
                GradientPaint gradient = new GradientPaint(0, 0, c1, width, height, c2);
                g2d.setPaint(gradient);
            }
            g2d.fillRoundRect(0, 0, width, height, 8, 8); // rounded-lg

            // Draw text
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

