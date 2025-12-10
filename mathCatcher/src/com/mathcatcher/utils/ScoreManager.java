package com.mathcatcher.utils;

import com.mathcatcher.game.DifficultySelect;
import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String SCORE_FILE = "scores.dat";
    private static final int MAX_SCORES_PER_DIFFICULTY = 10;
    
    private static List<ScoreEntry> scores = new ArrayList<>();
    
    static {
        loadScores();
    }
    
    public static class ScoreEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        private final int score;
        private final Date date;
        private final DifficultySelect.Difficulty difficulty;
        
        public ScoreEntry(int score, DifficultySelect.Difficulty difficulty) {
            this.score = score;
            this.date = new Date();
            this.difficulty = difficulty;
        }
        
        public int getScore() {
            return score;
        }
        
        public Date getDate() {
            return date;
        }
        
        public DifficultySelect.Difficulty getDifficulty() {
            return difficulty;
        }
    }
    
    public static void addScore(int score, DifficultySelect.Difficulty difficulty) {
        scores.add(new ScoreEntry(score, difficulty));
        
        // Maintain only top 10 scores per difficulty
        for (DifficultySelect.Difficulty diff : DifficultySelect.Difficulty.values()) {
            List<ScoreEntry> difficultyScores = new ArrayList<>();
            for (ScoreEntry entry : scores) {
                if (entry.getDifficulty() == diff) {
                    difficultyScores.add(entry);
                }
            }
            
            // Sort by score (highest first)
            difficultyScores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
            
            // Keep only top 10
            if (difficultyScores.size() > MAX_SCORES_PER_DIFFICULTY) {
                // Remove excess scores from main list
                List<ScoreEntry> toRemove = difficultyScores.subList(MAX_SCORES_PER_DIFFICULTY, difficultyScores.size());
                scores.removeAll(toRemove);
            }
        }
        
        saveScores();
    }
    
    public static List<ScoreEntry> getLeaderboard(DifficultySelect.Difficulty difficulty) {
        List<ScoreEntry> filtered = new ArrayList<>();
        
        // Filter by difficulty and sort by score (descending)
        for (ScoreEntry entry : scores) {
            if (entry.getDifficulty() == difficulty) {
                filtered.add(entry);
            }
        }
        
        // Sort by score (highest first)
        filtered.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        
        // Return top 10
        if (filtered.isEmpty()) {
            return new ArrayList<>();
        }
        int size = Math.min(filtered.size(), MAX_SCORES_PER_DIFFICULTY);
        return new ArrayList<>(filtered.subList(0, size));
    }
    
    @SuppressWarnings("unchecked")
    private static void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SCORE_FILE))) {
            scores = (List<ScoreEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty list
            scores = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading scores: " + e.getMessage());
            scores = new ArrayList<>();
        }
    }
    
    private static void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORE_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.err.println("Error saving scores: " + e.getMessage());
        }
    }
}

