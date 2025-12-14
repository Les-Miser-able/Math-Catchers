# Custom Resolution Feature - Usage Guide

## Overview
Math Catcher now supports custom resolutions! You can choose from preset resolutions or add your own custom screen sizes.

## How to Use

### Accessing Settings
1. Launch Math Catcher
2. Click the **⚙ Settings** button on the main menu

### Choosing a Preset Resolution
1. In the Settings panel, you'll see a dropdown menu labeled "Resolution:"
2. Select from available preset resolutions:
   - **4:3 Aspect Ratio**: 800x600, 1024x768, 1280x960, 1400x1050
   - **16:9 Aspect Ratio**: 1280x720, 1366x768, 1600x900, 1920x1080
3. Click **Apply** to restart the game with the new resolution
4. Your choice is automatically saved for future sessions

### Adding a Custom Resolution
1. In the Settings panel, locate the "Custom:" section
2. Enter your desired width (e.g., 1440)
3. Enter your desired height (e.g., 900)
4. Click the **Add** button
5. The custom resolution will be added to the dropdown and automatically selected
6. Click **Apply** to restart the game with your custom resolution

### Resolution Limits
- **Minimum**: 640 x 480
- **Maximum**: 3840 x 2160 (4K)

## Technical Details

### Files Created/Modified

#### New Files:
1. **ResolutionManager.java** (`src/com/mathcatcher/utils/ResolutionManager.java`)
   - Manages available resolutions
   - Saves/loads resolution preferences to `settings.properties`
   - Provides scaling factors for UI elements

2. **SettingsPanel.java** (`src/com/mathcatcher/game/SettingsPanel.java`)
   - User interface for resolution selection
   - Allows adding custom resolutions
   - Styled to match the game's visual theme

#### Modified Files:
1. **GameWindow.java**
   - Updated to use ResolutionManager for window sizing
   - Added Settings screen to card layout
   - Restarts window when resolution changes

2. **MainMenu.java**
   - Added Settings button to main menu
   - Updated constructor to accept settings callback

3. **GamePanel.java**
   - Updated to use dynamic resolution from ResolutionManager
   - Maintains 800x600 as base resolution for game logic

### Settings Storage
Resolution preferences are saved in `settings.properties` in the game's root directory:
```
resolution.width=1920
resolution.height=1080
```

## Tips
- The game will remember your last used resolution
- Custom resolutions are persisted even after closing the game
- If you experience any issues, delete `settings.properties` to reset to default (800x600)
- For best results, choose resolutions that match your screen's aspect ratio

## Troubleshooting
- **Resolution not applying**: Make sure you clicked the "Apply" button
- **Game appears too small/large**: Try a different preset resolution that better matches your screen
- **Custom resolution rejected**: Ensure your dimensions are within the allowed range (640x480 to 3840x2160)

