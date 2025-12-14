package com.mathcatcher.game;

import com.mathcatcher.utils.ResolutionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsPanel extends JPanel {

    public SettingsPanel(Runnable onBack, Runnable onApply) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 70, 40));

        // Create main container panel with styled background
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Semi-transparent black background with shadow effect
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(4, 4, getWidth(), getHeight(), 32, 32);

                // Main background
                g2d.setColor(new Color(0, 0, 0, 76));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);

                // Border
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 32, 32);
            }
        };
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title Section
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Font titleFont = new Font("SansSerif", Font.BOLD, 48);
                String title = "Settings";

                FontMetrics fm = g2d.getFontMetrics(titleFont);
                int textWidth = fm.stringWidth(title);
                int textHeight = fm.getHeight();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() - textHeight) / 2 + fm.getAscent();

                // Gradient text
                Color cyan = new Color(34, 211, 238);
                Color blue = new Color(59, 130, 246);
                java.awt.LinearGradientPaint gradient = new java.awt.LinearGradientPaint(
                    x, 0, x + textWidth, 0,
                    new float[]{0.0f, 1.0f},
                    new Color[]{cyan, blue}
                );

                g2d.setPaint(gradient);
                g2d.setFont(titleFont);
                g2d.drawString(title, x, y);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(0, 100));

        // Settings Content
        JPanel settingsContent = new JPanel();
        settingsContent.setLayout(new BoxLayout(settingsContent, BoxLayout.Y_AXIS));
        settingsContent.setOpaque(false);
        settingsContent.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Resolution Selection
        JPanel resolutionPanel = createResolutionPanel();
        settingsContent.add(resolutionPanel);
        settingsContent.add(Box.createVerticalStrut(20));

        // Custom Resolution Input
        JPanel customResPanel = createCustomResolutionPanel();
        settingsContent.add(customResPanel);

        // Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        GradientButton applyButton = new GradientButton("Apply", new Color(34, 197, 94), new Color(22, 163, 74));
        applyButton.addActionListener(e -> {
            if (onApply != null) onApply.run();
        });

        GradientButton backButton = new GradientButton("Back to Menu", new Color(59, 130, 246), new Color(37, 99, 235));
        backButton.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });

        buttonPanel.add(applyButton);
        buttonPanel.add(backButton);

        container.add(titlePanel, BorderLayout.NORTH);
        container.add(settingsContent, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        add(container, BorderLayout.CENTER);
    }

    private JPanel createResolutionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setOpaque(false);

        JLabel label = new JLabel("Resolution: ");
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(Color.WHITE);

        JComboBox<String> resolutionCombo = new JComboBox<>();
        resolutionCombo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        resolutionCombo.setPreferredSize(new Dimension(200, 35));

        // Populate combo box
        for (Dimension d : ResolutionManager.getAvailableResolutions()) {
            resolutionCombo.addItem(ResolutionManager.getResolutionString(d));
        }

        // Select current resolution
        Dimension current = ResolutionManager.getCurrentResolution();
        resolutionCombo.setSelectedItem(ResolutionManager.getResolutionString(current));

        // Listen for changes
        resolutionCombo.addActionListener(e -> {
            String selected = (String) resolutionCombo.getSelectedItem();
            if (selected != null) {
                String[] parts = selected.split(" x ");
                if (parts.length == 2) {
                    try {
                        int width = Integer.parseInt(parts[0].trim());
                        int height = Integer.parseInt(parts[1].trim());
                        ResolutionManager.setResolution(width, height);
                    } catch (NumberFormatException ex) {
                        // Ignore
                    }
                }
            }
        });

        panel.add(label);
        panel.add(resolutionCombo);

        return panel;
    }

    private JPanel createCustomResolutionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setOpaque(false);

        JLabel label = new JLabel("Custom: ");
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(Color.WHITE);

        JTextField widthField = new JTextField("800", 6);
        widthField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        widthField.setHorizontalAlignment(JTextField.CENTER);

        JLabel xLabel = new JLabel(" x ");
        xLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        xLabel.setForeground(Color.WHITE);

        JTextField heightField = new JTextField("600", 6);
        heightField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        heightField.setHorizontalAlignment(JTextField.CENTER);

        JButton addButton = new JButton("Add");
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(80, 30));
        addButton.addActionListener(e -> {
            try {
                int width = Integer.parseInt(widthField.getText().trim());
                int height = Integer.parseInt(heightField.getText().trim());
                ResolutionManager.addCustomResolution(width, height);
                ResolutionManager.setResolution(width, height);

                JOptionPane.showMessageDialog(this,
                    "Custom resolution " + width + "x" + height + " added!\nClick Apply to use it.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for width and height.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Invalid Resolution",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(label);
        panel.add(widthField);
        panel.add(xLabel);
        panel.add(heightField);
        panel.add(addButton);

        return panel;
    }

    // Gradient Button Class
    static class GradientButton extends JButton {
        private Color startColor;
        private Color endColor;
        private Color hoverStartColor;
        private Color hoverEndColor;
        private boolean isHovered = false;

        public GradientButton(String text, Color startColor, Color endColor) {
            super(text);
            this.startColor = startColor;
            this.endColor = endColor;
            this.hoverStartColor = brighten(startColor);
            this.hoverEndColor = brighten(endColor);

            setFont(new Font("SansSerif", Font.BOLD, 20));
            setForeground(Color.WHITE);
            setPreferredSize(new Dimension(200, 50));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        private Color brighten(Color color) {
            int r = Math.min(255, color.getRed() + 30);
            int g = Math.min(255, color.getGreen() + 30);
            int b = Math.min(255, color.getBlue() + 30);
            return new Color(r, g, b);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color start = isHovered ? hoverStartColor : startColor;
            Color end = isHovered ? hoverEndColor : endColor;

            GradientPaint gradient = new GradientPaint(0, 0, start, 0, getHeight(), end);
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            super.paintComponent(g);
        }
    }
}

