package com.flickenherz.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TitleScreen implements Screen {

    private static final int VIRTUAL_WIDTH = 1920;
    private static final int VIRTUAL_HEIGHT = 1080;

    private final com.badlogic.gdx.Game game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private BitmapFont titleFont;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture titlescreenTexture;

    public TitleScreen(com.badlogic.gdx.Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        titleFont = new BitmapFont();
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
        
        titlescreenTexture = new Texture(Gdx.files.internal("Titlescreen.png"));
    }

    @Override
    public void render(float delta) {
        // Handle input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || 
            Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            game.setScreen(new BattleScreen(game));
            // Don't call dispose here - let LibGDX handle it
            return;
        }

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // Draw titlescreen background inside the main white box
        float outerMargin = 60f;
        float bgX = outerMargin;
        float bgY = outerMargin + 260f;
        float bgW = VIRTUAL_WIDTH - outerMargin * 2f;
        float bgH = VIRTUAL_HEIGHT - (outerMargin * 2f + 260f);

        if (titlescreenTexture != null) {
            float texW = titlescreenTexture.getWidth();
            float texH = titlescreenTexture.getHeight();

            // preserve aspect ratio (letterbox) and center inside the bg rect
            float scale = Math.min(bgW / texW, bgH / texH);
            float drawW = texW * scale;
            float drawH = texH * scale;
            float drawX = bgX + (bgW - drawW) / 2f;
            float drawY = bgY + (bgH - drawH) / 2f;

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            batch.draw(titlescreenTexture, drawX, drawY, drawW, drawH);
            batch.end();
        }

        // Draw boxes
        drawBoxes();

        // Draw text
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Instructions in the bottom menu area
        font.getData().setScale(2.5f);
        String instruction = "Press ENTER or Z to Start";
        float instructionWidth = font.getXHeight() * instruction.length() * 1.5f;
        font.draw(batch, instruction, VIRTUAL_WIDTH / 2f - instructionWidth / 2f, outerMargin + 130);

        batch.end();
    }

    private void drawBoxes() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);

        float outerMargin = 60f;

        // Main frame
        shapeRenderer.rect(
            outerMargin,
            outerMargin + 260,
            VIRTUAL_WIDTH - outerMargin * 2,
            VIRTUAL_HEIGHT - (outerMargin * 2 + 260)
        );

        // Bottom menu field
        float menuBoxHeight = 260f;
        shapeRenderer.rect(
            outerMargin,
            outerMargin,
            VIRTUAL_WIDTH - outerMargin * 2,
            menuBoxHeight
        );

        shapeRenderer.end();
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
        shapeRenderer.dispose();
        if (titlescreenTexture != null) {
            titlescreenTexture.dispose();
        }
    }
}
