package com.mathcatcher.game;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private GamePanel gamePanel;
    private JPanel menuContainer;
    private JPanel difficultyContainer;
    private JPanel leaderboardContainer;
    private JPanel gameOverContainer;
    private DifficultySelect.Difficulty currentDifficulty;

    public GameWindow() {
        setTitle("Math Catcher - 2D Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create menu with background
        createMenuView();
        
        // Create difficulty selection screen
        createDifficultyView();
        
        // Create leaderboard screen
        createLeaderboardView();
        
        // Create game over screen
        createGameOverView();

        // Add menu view to card layout (game panel will be created when needed)
        cardPanel.add(menuContainer, "MENU");
        cardPanel.add(difficultyContainer, "DIFFICULTY");
        cardPanel.add(leaderboardContainer, "LEADERBOARD");
        cardPanel.add(gameOverContainer, "GAMEOVER");

        add(cardPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createMenuView() {
        // Create a background panel with game-like visuals
        menuContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sky gradient background (similar to game)
                GradientPaint sky = new GradientPaint(0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(176, 224, 230));
                g2d.setPaint(sky);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Ground
                g2d.setColor(new Color(34, 139, 34));
                g2d.fillRect(0, getHeight() - 50, getWidth(), 50);
            }
        };
        menuContainer.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));

        // Create and add menu
        MainMenu menu = new MainMenu(
                this::showDifficultyScreen,
                this::quitGame,
                this::viewLeaderboard
        );
        menuContainer.add(menu, BorderLayout.CENTER);
    }

    private void createDifficultyView() {
        // Create a background panel with game-like visuals
        difficultyContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sky gradient background (similar to game)
                GradientPaint sky = new GradientPaint(0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(176, 224, 230));
                g2d.setPaint(sky);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Ground
                g2d.setColor(new Color(34, 139, 34));
                g2d.fillRect(0, getHeight() - 50, getWidth(), 50);
            }
        };
        difficultyContainer.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));

        // Create and add difficulty selection
        DifficultySelect difficultySelect = new DifficultySelect(
                this::showMainMenu,
                this::startGameWithDifficulty
        );
        difficultyContainer.add(difficultySelect, BorderLayout.CENTER);
    }

    private void showDifficultyScreen() {
        cardLayout.show(cardPanel, "DIFFICULTY");
    }

    private void showMainMenu() {
        cardLayout.show(cardPanel, "MENU");
    }

    private void startGameWithDifficulty(DifficultySelect.Difficulty difficulty) {
        // Reset game panel if it already exists
        if (gamePanel != null) {
            cardPanel.remove(gamePanel);
        }
        currentDifficulty = difficulty;
        gamePanel = new GamePanel(difficulty, this::showGameOver, this::showMainMenu);
        cardPanel.add(gamePanel, "GAME");
        cardLayout.show(cardPanel, "GAME");
        gamePanel.requestFocus();
    }

    private void showGameOver() {
        if (gamePanel != null && currentDifficulty != null) {
            // Update game over screen with current score
            gameOverContainer.removeAll();
            GameOverPanel gameOverPanel = new GameOverPanel(
                gamePanel.getScore(),
                currentDifficulty,
                this::restartGame,
                this::showMainMenu
            );
            gameOverContainer.add(gameOverPanel, BorderLayout.CENTER);
            gameOverContainer.revalidate();
            gameOverContainer.repaint();
        }
        cardLayout.show(cardPanel, "GAMEOVER");
    }

    private void restartGame() {
        if (currentDifficulty != null) {
            startGameWithDifficulty(currentDifficulty);
        }
    }

    private void quitGame() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit?",
                "Quit Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void createLeaderboardView() {
        // Create a background panel with game-like visuals
        leaderboardContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sky gradient background (similar to game)
                GradientPaint sky = new GradientPaint(0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(176, 224, 230));
                g2d.setPaint(sky);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Ground
                g2d.setColor(new Color(34, 139, 34));
                g2d.fillRect(0, getHeight() - 50, getWidth(), 50);
            }
        };
        leaderboardContainer.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));

        // Create and add leaderboard
        LeaderboardPanel leaderboard = new LeaderboardPanel(this::showMainMenu);
        leaderboardContainer.add(leaderboard, BorderLayout.CENTER);
    }

    private void viewLeaderboard() {
        cardLayout.show(cardPanel, "LEADERBOARD");
    }

    private void createGameOverView() {
        // Create a background panel with game-like visuals
        gameOverContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sky gradient background (similar to game)
                GradientPaint sky = new GradientPaint(0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(176, 224, 230));
                g2d.setPaint(sky);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Ground
                g2d.setColor(new Color(34, 139, 34));
                g2d.fillRect(0, getHeight() - 50, getWidth(), 50);
            }
        };
        gameOverContainer.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
    }
}
