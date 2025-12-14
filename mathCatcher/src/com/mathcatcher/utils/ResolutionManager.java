package com.mathcatcher.utils;

import java.awt.Dimension;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ResolutionManager {
    private static final String CONFIG_FILE = "settings.properties";
    private static final List<Dimension> AVAILABLE_RESOLUTIONS = new ArrayList<>();
    private static Dimension currentResolution;
    private static final Dimension DEFAULT_RESOLUTION = new Dimension(800, 600);

    static {
        // Add common 4:3 resolutions
        AVAILABLE_RESOLUTIONS.add(new Dimension(800, 600));
        AVAILABLE_RESOLUTIONS.add(new Dimension(1024, 768));
        AVAILABLE_RESOLUTIONS.add(new Dimension(1280, 960));
        AVAILABLE_RESOLUTIONS.add(new Dimension(1400, 1050));

        // Add common 16:9 resolutions
        AVAILABLE_RESOLUTIONS.add(new Dimension(1280, 720));
        AVAILABLE_RESOLUTIONS.add(new Dimension(1366, 768));
        AVAILABLE_RESOLUTIONS.add(new Dimension(1600, 900));
        AVAILABLE_RESOLUTIONS.add(new Dimension(1920, 1080));

        // Load saved resolution or use default
        loadResolution();
    }

    public static void addCustomResolution(int width, int height) {
        if (width < 640 || height < 480) {
            throw new IllegalArgumentException("Minimum resolution is 640x480");
        }
        if (width > 3840 || height > 2160) {
            throw new IllegalArgumentException("Maximum resolution is 3840x2160");
        }

        Dimension custom = new Dimension(width, height);
        if (!AVAILABLE_RESOLUTIONS.contains(custom)) {
            AVAILABLE_RESOLUTIONS.add(custom);
            AVAILABLE_RESOLUTIONS.sort((d1, d2) -> {
                int area1 = d1.width * d1.height;
                int area2 = d2.width * d2.height;
                return Integer.compare(area1, area2);
            });
        }
    }

    public static void setResolution(Dimension resolution) {
        currentResolution = resolution;
        saveResolution();
    }

    public static void setResolution(int width, int height) {
        setResolution(new Dimension(width, height));
    }

    public static Dimension getCurrentResolution() {
        return currentResolution;
    }

    public static List<Dimension> getAvailableResolutions() {
        return new ArrayList<>(AVAILABLE_RESOLUTIONS);
    }

    public static double getScaleX() {
        return currentResolution.getWidth() / DEFAULT_RESOLUTION.getWidth();
    }

    public static double getScaleY() {
        return currentResolution.getHeight() / DEFAULT_RESOLUTION.getHeight();
    }

    public static double getScale() {
        // Use the minimum scale to maintain aspect ratio
        return Math.min(getScaleX(), getScaleY());
    }

    private static void loadResolution() {
        Properties props = new Properties();
        File configFile = new File(CONFIG_FILE);

        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
                int width = Integer.parseInt(props.getProperty("resolution.width", "800"));
                int height = Integer.parseInt(props.getProperty("resolution.height", "600"));
                currentResolution = new Dimension(width, height);

                // Add to available resolutions if it's custom
                if (!AVAILABLE_RESOLUTIONS.contains(currentResolution)) {
                    AVAILABLE_RESOLUTIONS.add(currentResolution);
                }
                return;
            } catch (IOException | NumberFormatException e) {
                System.err.println("Error loading resolution settings: " + e.getMessage());
            }
        }

        currentResolution = DEFAULT_RESOLUTION;
    }

    private static void saveResolution() {
        Properties props = new Properties();
        props.setProperty("resolution.width", String.valueOf(currentResolution.width));
        props.setProperty("resolution.height", String.valueOf(currentResolution.height));

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Math Catcher Settings");
        } catch (IOException e) {
            System.err.println("Error saving resolution settings: " + e.getMessage());
        }
    }

    public static String getResolutionString(Dimension d) {
        return d.width + " x " + d.height;
    }
}

