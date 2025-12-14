package com.flickenherz.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DeathScreen implements Screen {

    private static final int VIRTUAL_WIDTH = 1280;
    private static final int VIRTUAL_HEIGHT = 720;

    private final com.badlogic.gdx.Game game;
    private final int finalScore;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont titleFont;
    private OrthographicCamera camera;
    private Viewport viewport;

    public DeathScreen(com.badlogic.gdx.Game game, int finalScore) {
        this.game = game;
        this.finalScore = finalScore;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        titleFont = new BitmapFont();
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        // Handle input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || 
            Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            game.setScreen(new TitleScreen(game));
            // Don't call dispose here - let LibGDX handle it
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || 
            Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            Gdx.app.exit();
            return;
        }

        // Clear screen
        Gdx.gl.glClearColor(0.15f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Game Over text
        titleFont.getData().setScale(4f);
        String gameOver = "GAME OVER";
        float gameOverWidth = titleFont.getXHeight() * gameOver.length() * 2.5f;
        titleFont.draw(batch, gameOver, VIRTUAL_WIDTH / 2f - gameOverWidth / 2f, VIRTUAL_HEIGHT / 2f + 100);

        // Message
        font.getData().setScale(2f);
        String message = "You can't tell what's real anymore.";
        float messageWidth = font.getXHeight() * message.length() * 1.2f;
        font.draw(batch, message, VIRTUAL_WIDTH / 2f - messageWidth / 2f, VIRTUAL_HEIGHT / 2f);

        // Options
        String tryAgain = "Press ENTER or Z to Try Again";
        float tryAgainWidth = font.getXHeight() * tryAgain.length() * 1.2f;
        font.draw(batch, tryAgain, VIRTUAL_WIDTH / 2f - tryAgainWidth / 2f, VIRTUAL_HEIGHT / 2f - 80);

        String quit = "Press ESCAPE or X to Quit";
        float quitWidth = font.getXHeight() * quit.length() * 1.2f;
        font.draw(batch, quit, VIRTUAL_WIDTH / 2f - quitWidth / 2f, VIRTUAL_HEIGHT / 2f - 130);

        // Final Score (centered at bottom)
        String scoreText = "Final Score: " + finalScore;
        float scoreWidth = font.getXHeight() * scoreText.length() * 1.2f;
        font.draw(batch, scoreText, VIRTUAL_WIDTH / 2f - scoreWidth / 2f, 100);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        titleFont.dispose();
    }
}
