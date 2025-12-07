package com.mathcatcher.game;

import com.mathcatcher.utils.ScoreManager;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private DifficultySelect.Difficulty selectedDifficulty;
    private JPanel scoresPanel;
    private JPanel container;

    public LeaderboardPanel(Runnable onBack) {
        this.selectedDifficulty = DifficultySelect.Difficulty.EASY;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Create main container panel with styled background
        container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Semi-transparent black background with shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(4, 4, getWidth(), getHeight(), 40, 40);
                
                g2d.setColor(new Color(0, 0, 0, 90));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // rounded-2xl

                // Enhanced border
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 40, 40);
            }
        };
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // ===== Back Button =====
        GradientButton backButton = new GradientButton("← Back",
            new Color(100, 116, 139), new Color(71, 85, 105), // Slate gray gradient (same as difficulty)
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

                // Draw gradient text "Leaderboard" (same style as "Math Catchers")
                Font titleFont = new Font("SansSerif", Font.BOLD, 64);
                String title = "Leaderboard";
                
                // Create TextLayout for gradient text rendering
                java.awt.font.TextLayout textLayout = new java.awt.font.TextLayout(
                    title, titleFont, g2d.getFontRenderContext()
                );
                
                FontMetrics fm = g2d.getFontMetrics(titleFont);
                int textWidth = fm.stringWidth(title);
                int textHeight = fm.getHeight();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() - textHeight) / 2 + fm.getAscent() - 20;

                // Create gradient: yellow-400 -> pink-500 -> purple-500 (same as Math Catchers)
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

                // Draw subtitle "Top 10"
                Font subtitleFont = new Font("SansSerif", Font.PLAIN, 16);
                g2d.setFont(subtitleFont);
                g2d.setColor(new Color(209, 213, 219)); // gray-300
                String subtitle = "Top 10";
                FontMetrics subFm = g2d.getFontMetrics();
                int subX = (getWidth() - subFm.stringWidth(subtitle)) / 2;
                int subY = y + 25;
                g2d.drawString(subtitle, subX, subY);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(0, 140));
        titlePanel.setMinimumSize(new Dimension(0, 140));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0)); // Minimal bottom padding to bring buttons very close

        // ===== Difficulty Filter Buttons =====
        JPanel filterPanel = new JPanel(new GridLayout(1, 3, 8, 0));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 24, 0)); // Very small top padding to be close to "Top 10"
        filterPanel.setPreferredSize(new Dimension(0, 58)); // Fixed height to ensure visibility (updated for bigger buttons)
        filterPanel.setMinimumSize(new Dimension(0, 58));
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        DifficultyFilterButton easyButton = new DifficultyFilterButton("Easy", 
            selectedDifficulty == DifficultySelect.Difficulty.EASY);
        easyButton.addActionListener(e -> {
            selectedDifficulty = DifficultySelect.Difficulty.EASY;
            updateDifficultyButtons(filterPanel);
            refreshScores();
        });

        DifficultyFilterButton mediumButton = new DifficultyFilterButton("Medium",
            selectedDifficulty == DifficultySelect.Difficulty.MEDIUM);
        mediumButton.addActionListener(e -> {
            selectedDifficulty = DifficultySelect.Difficulty.MEDIUM;
            updateDifficultyButtons(filterPanel);
            refreshScores();
        });

        DifficultyFilterButton hardButton = new DifficultyFilterButton("Hard",
            selectedDifficulty == DifficultySelect.Difficulty.HARD);
        hardButton.addActionListener(e -> {
            selectedDifficulty = DifficultySelect.Difficulty.HARD;
            updateDifficultyButtons(filterPanel);
            refreshScores();
        });

        filterPanel.add(easyButton);
        filterPanel.add(mediumButton);
        filterPanel.add(hardButton);

        // ===== Scores Panel =====
        scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(scoresPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(255, 255, 255, 100);
                this.trackColor = new Color(0, 0, 0, 0);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        // Add components to container
        // Use BorderLayout with title centered, back button in separate layer
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Title panel takes full width and centers text
        topPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Back button in separate panel that doesn't affect centering
        JPanel backButtonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backButtonWrapper.setOpaque(false);
        backButtonWrapper.add(backButton);
        topPanel.add(backButtonWrapper, BorderLayout.WEST);

        // Create content panel to hold filter and scores
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(filterPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        container.add(topPanel, BorderLayout.NORTH);
        container.add(contentPanel, BorderLayout.CENTER);

        // Add container to main panel
        add(container, BorderLayout.CENTER);

        // Initial load
        refreshScores();
    }

    private void updateDifficultyButtons(JPanel filterPanel) {
        Component[] components = filterPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof DifficultyFilterButton) {
                DifficultyFilterButton btn = (DifficultyFilterButton) components[i];
                DifficultySelect.Difficulty btnDifficulty = DifficultySelect.Difficulty.values()[i];
                btn.setSelected(selectedDifficulty == btnDifficulty);
            }
        }
    }

    private void refreshScores() {
        scoresPanel.removeAll();
        
        List<ScoreManager.ScoreEntry> scores = ScoreManager.getLeaderboard(selectedDifficulty);
        
        if (scores.isEmpty()) {
            // Empty state
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setOpaque(false);
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(48, 0, 48, 0));
            
            JLabel trophyIcon = new JLabel("🏆");
            trophyIcon.setFont(new Font("SansSerif", Font.PLAIN, 64));
            trophyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            trophyIcon.setForeground(new Color(255, 255, 255, 51)); // opacity-20
            
            JLabel emptyText = new JLabel("No scores yet. Be the first to play!");
            emptyText.setFont(new Font("SansSerif", Font.PLAIN, 16));
            emptyText.setForeground(new Color(156, 163, 175)); // gray-400
            emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyText.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
            
            emptyPanel.add(trophyIcon);
            emptyPanel.add(emptyText);
            scoresPanel.add(emptyPanel);
        } else {
            // Score entries
            for (int i = 0; i < scores.size(); i++) {
                ScoreManager.ScoreEntry entry = scores.get(i);
                ScoreEntryPanel entryPanel = new ScoreEntryPanel(entry, i);
                scoresPanel.add(entryPanel);
                scoresPanel.add(Box.createVerticalStrut(16)); // More spacing between entries
            }
        }
        
        scoresPanel.revalidate();
        scoresPanel.repaint();
    }

    // Difficulty filter button
    private static class DifficultyFilterButton extends JButton {
        private boolean isSelected;

        public DifficultyFilterButton(String text, boolean selected) {
            super(text);
            this.isSelected = selected;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 16)); // Slightly bigger font
            setPreferredSize(new Dimension(0, 48)); // Slightly bigger filter buttons
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            setMinimumSize(new Dimension(0, 48));
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            if (isSelected) {
                // Selected: gradient purple to pink
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(168, 85, 247), // purple-500
                    width, height, new Color(219, 39, 119) // pink-600
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, width, height, 12, 12); // Smaller rounded corners
                g2d.setColor(Color.WHITE);
            } else {
                // Not selected: white/10 background
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.fillRoundRect(0, 0, width, height, 12, 12); // Smaller rounded corners
                g2d.setColor(new Color(209, 213, 219)); // gray-300
            }

            // Draw text
            g2d.setFont(getFont());
            FontMetrics fm = g2d.getFontMetrics();
            String text = getText();
            int textX = (width - fm.stringWidth(text)) / 2;
            int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, textX, textY);
        }
    }

    // Score entry panel
    private static class ScoreEntryPanel extends JPanel {
        private final ScoreManager.ScoreEntry entry;
        private final int rank;

        public ScoreEntryPanel(ScoreManager.ScoreEntry entry, int rank) {
            this.entry = entry;
            this.rank = rank;
            setOpaque(false);
            setPreferredSize(new Dimension(0, 75)); // Score entries
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Background (top 3 get gradient, others get simple background)
            if (rank < 3) {
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 51), // white/20
                    width, height, new Color(255, 255, 255, 25) // white/10
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, width, height, 12, 12); // More rounded
                
                // Border
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.drawRoundRect(0, 0, width - 1, height - 1, 12, 12);
            } else {
                g2d.setColor(new Color(255, 255, 255, 13)); // white/5
                g2d.fillRoundRect(0, 0, width, height, 12, 12);
            }

            // Rank icon/text
            int iconX = 16;
            int iconY = height / 2;
            String rankText;
            Color rankColor;

            if (rank == 0) {
                rankText = "🏆";
                rankColor = new Color(251, 191, 36); // yellow-400
            } else if (rank == 1) {
                rankText = "🥈";
                rankColor = new Color(209, 213, 219); // gray-300
            } else if (rank == 2) {
                rankText = "🥉";
                rankColor = new Color(251, 146, 60); // orange-400
            } else {
                rankText = "#" + (rank + 1);
                rankColor = new Color(156, 163, 175); // gray-400
            }

            if (rank < 3) {
                // Icon for top 3 - larger
                Font iconFont = new Font("SansSerif", Font.PLAIN, 32);
                g2d.setFont(iconFont);
                FontMetrics iconFm = g2d.getFontMetrics();
                int iconTextY = iconY + iconFm.getAscent() / 2;
                g2d.drawString(rankText, iconX, iconTextY);
            } else {
                // Number for others
                g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2d.setColor(rankColor);
                FontMetrics fm = g2d.getFontMetrics();
                int textY = iconY + fm.getAscent() / 2;
                g2d.drawString(rankText, iconX, textY);
            }

            // Date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String dateStr = dateFormat.format(entry.getDate());
            String timeStr = timeFormat.format(entry.getDate());

            int textX = iconX + 40;
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2d.setColor(Color.WHITE);
            FontMetrics fm = g2d.getFontMetrics();
            int dateY = iconY - 8;
            g2d.drawString(dateStr, textX, dateY);

            g2d.setColor(new Color(156, 163, 175)); // gray-400
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
            fm = g2d.getFontMetrics();
            int timeY = iconY + fm.getAscent() + 4;
            g2d.drawString(timeStr, textX, timeY);

            // Score and difficulty - larger
            int scoreX = width - 140;
            g2d.setFont(new Font("SansSerif", Font.BOLD, 28)); // Larger score
            g2d.setColor(new Color(251, 191, 36)); // yellow-400
            fm = g2d.getFontMetrics();
            String scoreStr = String.valueOf(entry.getScore());
            int scoreTextX = scoreX - fm.stringWidth(scoreStr);
            g2d.drawString(scoreStr, scoreTextX, dateY);

            g2d.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Larger difficulty text
            g2d.setColor(new Color(156, 163, 175)); // gray-400
            fm = g2d.getFontMetrics();
            String diffStr = entry.getDifficulty().name().toLowerCase();
            diffStr = diffStr.substring(0, 1).toUpperCase() + diffStr.substring(1);
            int diffTextX = scoreX - fm.stringWidth(diffStr);
            g2d.drawString(diffStr, diffTextX, timeY);
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

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
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

