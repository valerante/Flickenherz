package com.flickenherz.game;

import com.badlogic.gdx.Game;
import com.flickenherz.game.screens.TitleScreen;

public class MyGdxGame extends Game {

    @Override
    public void create() {
        setScreen(new TitleScreen(this));
    }
}
