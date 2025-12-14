package com.flickenherz.game;

import com.badlogic.gdx.Game;
import com.flickenherz.game.screens.TitleScreen;

/**
 * Main game class for Flickenherz.
 * Extends LibGDX Game class to manage different game screens.
 * This is the core entry point that initializes the game flow.
 */
public class MyGdxGame extends Game {

    /**
     * Called when the game is first created.
     * Initializes the game by setting the title screen as the initial screen.
     */
    @Override
    public void create() {
        setScreen(new TitleScreen(this));
    }
}
