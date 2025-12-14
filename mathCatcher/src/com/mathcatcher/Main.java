package com.mathcatcher;

import com.mathcatcher.game.GameWindow;
import com.mathcatcher.utils.SoundManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize sound system
        SoundManager.init();

        SwingUtilities.invokeLater(() -> {
            new GameWindow();
        });
    }
}
