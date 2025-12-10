package com.flickenherz.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.flickenherz.game.MyGdxGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Flickenherz");
        config.setWindowedMode(1920, 1080);
        config.useVsync(true);

        new Lwjgl3Application(new MyGdxGame(), config);
    }
}
