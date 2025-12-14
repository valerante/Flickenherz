package com.flickenherz.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.flickenherz.game.MyGdxGame;

/**
 * Desktop launcher for the Flickenherz game.
 * This class initializes and configures the LWJGL3 application for desktop platforms.
 */
public class DesktopLauncher {
    /**
     * Main entry point for the desktop application.
     * Creates and configures the game window with specified dimensions and settings.
     * 
     * @param arg Command line arguments (not used)
     */
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Flickenherz");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        
        new Lwjgl3Application(new MyGdxGame(), config);
    }
}
