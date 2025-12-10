package com.mathcatcher.utils;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;

public class SoundManager {
    private static boolean soundEnabled = true;
    
    // Generate simple beep sounds programmatically
    public static void playSound(SoundType type) {
        if (!soundEnabled) return;
        
        new Thread(() -> {
            try {
                byte[] audioData = generateSound(type);
                AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
                AudioInputStream audioStream = new AudioInputStream(
                    new ByteArrayInputStream(audioData),
                    format,
                    audioData.length / format.getFrameSize()
                );
                
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } catch (Exception e) {
                // Silently fail if sound can't be played
            }
        }).start();
    }
    
    private static byte[] generateSound(SoundType type) {
        int sampleRate = 44100;
        double duration = 0.1; // 100ms
        int numSamples = (int) (sampleRate * duration);
        byte[] audioData = new byte[numSamples * 2];
        
        double frequency;
        double volume = 0.3;
        
        switch (type) {
            case CATCH:
                frequency = 800; // High pitch for success
                break;
            case WRONG:
                frequency = 200; // Low pitch for wrong
                break;
            case LIFE_LOST:
                frequency = 150; // Very low pitch for life lost
                duration = 0.3; // Longer sound
                numSamples = (int) (sampleRate * duration);
                audioData = new byte[numSamples * 2];
                break;
            case LEVEL_UP:
                frequency = 1000; // Very high pitch
                break;
            default:
                frequency = 440;
        }
        
        for (int i = 0; i < numSamples; i++) {
            double time = (double) i / sampleRate;
            double wave;
            
            if (type == SoundType.LIFE_LOST) {
                // Descending tone for life lost
                double freq = frequency * (1.0 - time * 0.5);
                wave = Math.sin(2 * Math.PI * freq * time);
                // Add fade out
                wave *= (1.0 - time / duration);
            } else if (type == SoundType.CATCH) {
                // Quick beep with slight vibrato
                wave = Math.sin(2 * Math.PI * frequency * time * (1 + 0.1 * Math.sin(10 * time)));
            } else {
                wave = Math.sin(2 * Math.PI * frequency * time);
            }
            
            short sample = (short) (wave * volume * Short.MAX_VALUE);
            audioData[i * 2] = (byte) (sample & 0xFF);
            audioData[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        
        return audioData;
    }
    
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }
    
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    public enum SoundType {
        CATCH,      // When catching correct number
        WRONG,      // When catching wrong number
        LIFE_LOST,  // When losing a life
        LEVEL_UP    // When leveling up
    }
}

