package com.mathcatcher.utils;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, Clip> soundClips = new HashMap<>();
    private static Clip backgroundMusic;
    private static boolean soundEnabled = true;
    private static boolean musicEnabled = true;
    private static float soundVolume = 0.7f; // 0.0 to 1.0
    private static float musicVolume = 0.5f; // 0.0 to 1.0

    // Sound effect types
    public enum Sound {
        CORRECT("correct.wav"),
        WRONG("wrong.wav"),
        CATCH("catch.wav"),
        LEVEL_UP("levelup.wav"),
        GAME_OVER("gameover.wav"),
        BUTTON_CLICK("click.wav"),
        TIMER_WARNING("warning.wav");

        private final String filename;

        Sound(String filename) {
            this.filename = filename;
        }

        public String getFilename() {
            return filename;
        }
    }

    /**
     * Initialize the sound system and preload sound effects
     */
    public static void init() {
        System.out.println("=== Sound Manager Initialization ===");
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        // Preload all sound effects
        for (Sound sound : Sound.values()) {
            loadSound(sound);
        }

        System.out.println("=== Sound Manager Initialized ===");
    }

    /**
     * Load a sound effect into memory
     */
    private static void loadSound(Sound sound) {
        try {
            InputStream audioSrc = null;

            // Try loading from file system first (sounds/ folder in working directory)
            File soundFile = new File("sounds/" + sound.getFilename());
            System.out.println("Checking for: " + soundFile.getAbsolutePath());

            if (soundFile.exists()) {
                audioSrc = new FileInputStream(soundFile);
                System.out.println("✓ Loaded sound: " + sound.getFilename());
            } else {
                // Try to load from resources as fallback
                audioSrc = SoundManager.class.getResourceAsStream("/sounds/" + sound.getFilename());
                if (audioSrc != null) {
                    System.out.println("✓ Loaded sound from resources: " + sound.getFilename());
                } else {
                    System.out.println("✗ Sound file not found: " + sound.getFilename() + " (using synthesized sound)");
                    return;
                }
            }

            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            soundClips.put(sound.name(), clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("✗ Error loading sound: " + sound.getFilename() + " - " + e.getMessage());
        }
    }

    /**
     * Play a sound effect
     */
    public static void playSound(Sound sound) {
        if (!soundEnabled) return;

        try {
            Clip clip = soundClips.get(sound.name());
            if (clip != null) {
                // Stop if already playing
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.setFramePosition(0);
                setVolume(clip, soundVolume);
                clip.start();
            } else {
                // Fallback: play system beep
                playBeep(sound);
            }
        } catch (Exception e) {
            System.err.println("Error playing sound: " + sound.name());
        }
    }

    /**
     * Fallback method to play simple beeps when audio files aren't available
     */
    private static void playBeep(Sound sound) {
        new Thread(() -> {
            try {
                java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
                switch (sound) {
                    case CORRECT:
                        // High pitch beep for correct answer
                        toolkit.beep();
                        break;
                    case WRONG:
                        // Double beep for wrong answer
                        toolkit.beep();
                        Thread.sleep(100);
                        toolkit.beep();
                        break;
                    case GAME_OVER:
                        // Triple descending beeps
                        toolkit.beep();
                        Thread.sleep(150);
                        toolkit.beep();
                        Thread.sleep(150);
                        toolkit.beep();
                        break;
                    default:
                        toolkit.beep();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Play background music (loops)
     */
    public static void playBackgroundMusic(String filename) {
        if (!musicEnabled) return;

        try {
            stopBackgroundMusic();

            InputStream audioSrc = null;

            // Try loading from file system first
            File musicFile = new File("sounds/" + filename);
            if (musicFile.exists()) {
                audioSrc = new FileInputStream(musicFile);
                System.out.println("Loaded music: " + filename);
            } else {
                // Try to load from resources as fallback
                audioSrc = SoundManager.class.getResourceAsStream("/sounds/" + filename);
                if (audioSrc == null) {
                    System.out.println("Music file not found: " + filename);
                    return;
                }
            }

            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            setVolume(backgroundMusic, musicVolume);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    /**
     * Stop background music
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    /**
     * Set volume for a clip (0.0 to 1.0)
     */
    private static void setVolume(Clip clip, float volume) {
        if (clip == null) return;

        try {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float gain = min + (max - min) * volume;
            volumeControl.setValue(gain);
        } catch (Exception e) {
            // Volume control not supported
        }
    }

    /**
     * Enable/disable sound effects
     */
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    /**
     * Enable/disable background music
     */
    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        if (!enabled && backgroundMusic != null) {
            stopBackgroundMusic();
        }
    }

    /**
     * Set sound effects volume (0.0 to 1.0)
     */
    public static void setSoundVolume(float volume) {
        soundVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    /**
     * Set music volume (0.0 to 1.0)
     */
    public static void setMusicVolume(float volume) {
        musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (backgroundMusic != null) {
            setVolume(backgroundMusic, musicVolume);
        }
    }

    /**
     * Check if sound is enabled
     */
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Check if music is enabled
     */
    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    /**
     * Clean up resources
     */
    public static void cleanup() {
        stopBackgroundMusic();
        for (Clip clip : soundClips.values()) {
            if (clip != null) {
                clip.close();
            }
        }
        soundClips.clear();
    }
}

