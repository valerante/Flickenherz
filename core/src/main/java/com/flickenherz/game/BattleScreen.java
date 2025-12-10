package com.flickenherz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BattleScreen implements Screen {

    // Auflösung
    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final BitmapFont fontBig;

    private Player girl;
    private BattleController battleController;
    private BattleState battleState;

    private enum MenuState {
        MAIN_MENU, ENEMY_TURN, WIN, LOSE
    }

    private MenuState menuState;

    private final String[] menuItems = {"FIGHT", "ACT", "ITEM", "MERCY"};
    private int selectedIndex = 0;

    private String messageText = "It's so quiet here...";
    private String actionText = "";

    // ===== Bullet-Hell Arena =====
    private final Arena arena;

    private final Array<Bullet> bullets = new Array<>();

    private BulletPattern currentPattern = BulletPattern.HORIZONTAL_WAVE;

    // Rotation für Circle-Burst
    private float circleBurstAngleOffset = 0f;

    // ===== Sprites =====
    private final Texture teddyTexture;
    private final Texture dollTexture;
    private final Texture girlTexture;

    private final float teddyX;
    private final float teddyY;
    private final float teddyWidth;
    private final float teddyHeight;
    private final float dollX;
    private final float dollY;
    private final float dollWidth;
    private final float dollHeight;
    private final float girlX;
    private final float girlY;
    private final float girlWidth;
    private final float girlHeight;

    // ===== MUSIK =====
    private final Music battleMusic;

    // ===== HIT-EFFEKTE =====
    private static final float HIT_DURATION = 0.15f;

    private boolean teddyHitActive = false;
    private float teddyHitTimer = 0f;

    private boolean girlHitActive = false;   // für die Seele in der Bullet-Hell
    private float girlHitTimer = 0f;

    // ===== AUDIO & UI HELPERS =====
    private final AudioManager audio;
    private final BattleUI ui;

    public BattleScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        fontBig = new BitmapFont();

        girl = new Player("Mädchen");
        battleController = new BattleController();

        battleState = BattleState.PLAYER_TURN;
        menuState = MenuState.MAIN_MENU;

        // Arena sitzt über dem unteren Menü
        arena = new Arena(VIRTUAL_WIDTH, 360f);
        arena.resetSoul();

        // Sprites laden (liegen im Projektroot unter assets/)
        teddyTexture = new Texture(Gdx.files.internal("teddy.png"));
        girlTexture  = new Texture(Gdx.files.internal("girl.png"));
        dollTexture  = new Texture(Gdx.files.internal("doll.png"));

        // Größen & Positionen
        float teddyScale = 0.35f;
        teddyWidth = teddyTexture.getWidth() * teddyScale;
        teddyHeight = teddyTexture.getHeight() * teddyScale;
        teddyX = VIRTUAL_WIDTH * 0.70f - teddyWidth / 2f;
        teddyY = arena.y + Arena.HEIGHT + 10f;

        float dollScale = 0.35f;
        dollWidth = dollTexture.getWidth() * dollScale;
        dollHeight = dollTexture.getHeight() * dollScale;
        dollX = VIRTUAL_WIDTH * 0.70f - dollWidth / 2f;
        dollY = arena.y + Arena.HEIGHT + 10f;

        float girlScale = 0.35f;
        girlWidth = girlTexture.getWidth() * girlScale;
        girlHeight = girlTexture.getHeight() * girlScale;
        girlX = arena.x + Arena.WIDTH * 0.25f - girlWidth / 2f;
        girlY = arena.y + Arena.HEIGHT * 0.5f - girlHeight / 2f;

        // Musik starten (Datei muss in assets/ liegen)
        battleMusic = Gdx.audio.newMusic(Gdx.files.internal("battle_theme.ogg"));
        battleMusic.setLooping(true);
        battleMusic.setVolume(0.05f);
        battleMusic.play();

        // Audio manager & UI helper
        audio = new AudioManager();
        ui = new BattleUI(font, fontBig);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        updateBulletHell(delta);
        updateHitEffects(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        drawBoxes();

        if (arena.inBulletHell) {
            drawBulletHell();
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawEnemyArea();
        drawCharacters();
        ui.drawMessageArea(batch, messageText, actionText, VIRTUAL_HEIGHT);
        boolean highlightActive = (menuState == MenuState.MAIN_MENU && battleState == BattleState.PLAYER_TURN && !arena.inBulletHell);
        ui.drawBottomMenu(batch, menuItems, selectedIndex, highlightActive, VIRTUAL_WIDTH);

        batch.end();
    }

    // ==== kleine Hit-Timer updaten ====
    private void updateHitEffects(float delta) {
        if (teddyHitActive) {
            teddyHitTimer -= delta;
            if (teddyHitTimer <= 0f) {
                teddyHitActive = false;
            }
        }

        if (girlHitActive) {
            girlHitTimer -= delta;
            if (girlHitTimer <= 0f) {
                girlHitActive = false;
            }
        }
    }

    // ==== Rahmen ====
    private void drawBoxes() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);

        float outerMargin = 60f;

        // Großer Rahmen
        shapeRenderer.rect(
            outerMargin,
            outerMargin + 260,
            VIRTUAL_WIDTH - outerMargin * 2,
            VIRTUAL_HEIGHT - (outerMargin * 2 + 260)
        );

        // Unteres Menü-Feld
        float menuBoxHeight = 260f;
        shapeRenderer.rect(
            outerMargin,
            outerMargin,
            VIRTUAL_WIDTH - outerMargin * 2,
            menuBoxHeight
        );

        // Bullet-Hell-Box NUR wenn Angriff läuft
        if (arena.inBulletHell) {
            shapeRenderer.rect(
                arena.x,
                arena.y,
                Arena.WIDTH,
                Arena.HEIGHT
            );
        }

        shapeRenderer.end();
    }

    // ==== Bullet-Hell zeichnen ====
    private void drawBulletHell() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Treffer-Blitz hinter der Seele
        if (girlHitActive) {
            shapeRenderer.setColor(1, 0.6f, 0.6f, 1);
            shapeRenderer.circle(arena.soulX, arena.soulY, arena.soulRadius + 6f);
        }

        // Spieler-Seele
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.circle(arena.soulX, arena.soulY, arena.soulRadius);

        // Bullets
        shapeRenderer.setColor(1, 1, 1, 1);
        for (Bullet b : bullets) {
            if (b.alive) {
                shapeRenderer.circle(b.x, b.y, b.radius);
            }
        }

        shapeRenderer.end();
    }

    // ==== Enemy-Area & Status ====
    private void drawEnemyArea() {
        fontBig.getData().setScale(2f);

        Enemy enemy = battleController.getCurrent();
        String enemyName = enemy != null ? enemy.getName() : "Unknown";
        int hp = enemy != null ? enemy.getHp() : 0;
        int maxHp = enemy != null ? enemy.getMaxHp() : 0;
        String enemyInfo = enemyName + "   HP: " + hp + "/" + maxHp;
        fontBig.draw(batch, enemyInfo, 120, VIRTUAL_HEIGHT - 80);

        font.getData().setScale(1.5f);
        String playerInfo = girl.name + "   HP: " + girl.hp + "/" + girl.maxHp +
            "   SAN: " + girl.sanity + "/" + girl.maxSanity;
        font.draw(batch, playerInfo, 120, 360);
    }

    // ==== Sprites zeichnen (mit Hit-Animation) ====
    private void drawCharacters() {

        Enemy enemy = battleController.getCurrent();
        // jeweils der aktuelle Gegner
        float enemyScaleFactor = teddyHitActive ? 1.08f : 1f;

        if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            float drawW = teddyWidth * enemyScaleFactor;
            float drawH = teddyHeight * enemyScaleFactor;
            float offX = (drawW - teddyWidth) / 2f;
            float offY = (drawH - teddyHeight) / 2f;

            if (teddyHitActive) {
                batch.setColor(1f, 0.6f, 0.6f, 1f);
            } else {
                batch.setColor(1f, 1f, 1f, 1f);
            }

            batch.draw(teddyTexture, teddyX - offX, teddyY - offY, drawW, drawH);
        } else {
            float drawW = dollWidth * enemyScaleFactor;
            float drawH = dollHeight * enemyScaleFactor;
            float offX = (drawW - dollWidth) / 2f;
            float offY = (drawH - dollHeight) / 2f;

            if (teddyHitActive) {
                batch.setColor(1f, 0.6f, 0.6f, 1f);
            } else {
                batch.setColor(1f, 1f, 1f, 1f);
            }

            batch.draw(dollTexture, dollX - offX, dollY - offY, drawW, drawH);
        }

        // Mädchen nur, wenn nicht in Bullet-Hell
        if (!arena.inBulletHell) {
            float girlScaleFactor = girlHitActive ? 1.05f : 1f;
            float drawGirlWidth = girlWidth * girlScaleFactor;
            float drawGirlHeight = girlHeight * girlScaleFactor;
            float girlOffsetX = (drawGirlWidth - girlWidth) / 2f;
            float girlOffsetY = (drawGirlHeight - girlHeight) / 2f;

            if (girlHitActive) {
                batch.setColor(1f, 0.6f, 0.6f, 1f);
            } else {
                batch.setColor(1f, 1f, 1f, 1f);
            }

            batch.draw(
                girlTexture,
                girlX - girlOffsetX,
                girlY - girlOffsetY,
                drawGirlWidth,
                drawGirlHeight
            );
        }

        // Farbe wieder normal
        batch.setColor(1f, 1f, 1f, 1f);
    }

    private void drawMessageArea() {
        font.getData().setScale(1.6f);
        font.draw(batch, messageText, 120, VIRTUAL_HEIGHT / 2f + 60);

        font.getData().setScale(1.3f);
        font.draw(batch, actionText, 120, VIRTUAL_HEIGHT / 2f - 10);
    }

    private void drawBottomMenu() {
        float outerMargin = 60f;
        float baseY = outerMargin + 80;
        float centerX = VIRTUAL_WIDTH / 2f;

        fontBig.getData().setScale(2f);

        float spacing = 260f;
        float startX = centerX - spacing * 1.5f;

        for (int i = 0; i < menuItems.length; i++) {
            String label = menuItems[i];
            float x = startX + i * spacing;

            if (i == selectedIndex &&
                menuState == MenuState.MAIN_MENU &&
                battleState == BattleState.PLAYER_TURN &&
                !arena.inBulletHell) {

                fontBig.draw(batch, ">" + label, x, baseY);
            } else {
                fontBig.draw(batch, label, x, baseY);
            }
        }
    }

    // ==== Input ====
    private void handleInput(float delta) {
        // Während Bullet-Hell: nur Movement
        if (arena.inBulletHell) {
            arena.handleMovement(delta);
            return;
        }

        if (battleState == BattleState.PLAYER_TURN && menuState == MenuState.MAIN_MENU) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                navigateMenuLeft();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                navigateMenuRight();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
                Gdx.input.isKeyJustPressed(Input.Keys.Z)) {

                selectMenu();
            }

        } else if ((battleState == BattleState.WIN || battleState == BattleState.LOSE)
            && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            resetBattle();
        }
    }

    

    // ==== Menüaktionen ====
    private void handleMenuSelection() {
        String choice = menuItems[selectedIndex];

        if ("FIGHT".equals(choice)) {
            performFight();
        } else if ("ACT".equals(choice)) {
            performAct();
        } else if ("ITEM".equals(choice)) {
            performItem();
        } else if ("MERCY".equals(choice)) {
            performMercy();
        }
    }

    // ==== Menu navigation helpers (extracted) ====
    private void navigateMenuLeft() {
        selectedIndex = (selectedIndex - 1 + menuItems.length) % menuItems.length;
        audio.playMenuMove();
    }

    private void navigateMenuRight() {
        selectedIndex = (selectedIndex + 1) % menuItems.length;
        audio.playMenuMove();
    }

    private void selectMenu() {
        audio.playMenuSelect();
        handleMenuSelection();
    }

    private void performFight() {
        int dmg = girl.basicAttack();
        Enemy enemy = battleController.getCurrent();
        if (enemy == null) return;

        // Flavor text depending on enemy type
        if (enemy.getType() == Enemy.EnemyType.TEDDY) {
            actionText = "You attack the torn teddy. -" + dmg + " HP";
            messageText = "The air feels heavier.";
        } else if (enemy.getType() == Enemy.EnemyType.DOLL) {
            actionText = "You strike the porcelain doll. -" + dmg + " HP";
            messageText = "New cracks spread across the porcelain.";
        } else {
            actionText = "You attack. -" + dmg + " HP";
            messageText = "The air feels heavier.";
        }

        // Apply damage
        enemy.takeDamage(dmg);

        // Hit-Effekt für aktuellen Gegner
        teddyHitActive = true;
        teddyHitTimer = HIT_DURATION;

        audio.playPlayerAttack();

        if (enemy.isDead()) {
            // If there is another enemy waiting, advance to it
            String defeatMsg = enemy.getDefeatMessage();
            String defeatAct = enemy.getDefeatAction();
            if (battleController.hasNext()) {
                messageText = defeatMsg;
                actionText = defeatAct;
                battleController.advanceToNext();
                // stay in player turn; next enemy will be shown on next render
            } else {
                battleState = BattleState.WIN;
                menuState = MenuState.WIN;
                messageText = defeatMsg;
                actionText = defeatAct;
            }
        } else {
            // enemy survived — they will start their attack
            if (enemy.getType() == Enemy.EnemyType.TEDDY) {
                startEnemyAttack("The teddy prepares an attack...", "Something moves in the dark.");
            } else if (enemy.getType() == Enemy.EnemyType.DOLL) {
                startEnemyAttack("The doll's glass eyes flare.", "Shards of porcelain whirl around you.");
            } else {
                startEnemyAttack("The enemy prepares an attack...", "You feel something approach.");
            }
        }
    }

    private void performAct() {
        Enemy enemy = battleController.getCurrent();
        girl.loseSanity(5);

        if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            actionText = "You whisper your name to the teddy.";
            messageText = "Something inside you cracks a little.";
        } else {
            actionText = "You speak softly to the enemy.";
            messageText = "Its painted mouth doesn't move, but you feel it listening.";
        }

        if (girl.isDead()) {
            battleState = BattleState.LOSE;
            menuState = MenuState.LOSE;
            messageText = "You can't hold your mind together anymore.";
            actionText = "";
        } else {
            if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
                startEnemyAttack("The teddy tilts its head.", "You feel eyes on you from every direction.");
            } else {
                startEnemyAttack("The enemy tilts its head.", "It creeps closer.");
            }
        }
    }

    private void performItem() {
        Enemy enemy = battleController.getCurrent();
        if (girl.hp < girl.maxHp) {
            girl.hp = Math.min(girl.maxHp, girl.hp + 10);
            actionText = "You use a bandage. +10 HP";
            messageText = "The bandage smells faintly of dust.";
        } else {
            actionText = "Your pockets are empty.";
            messageText = "You feel around, but find nothing.";
        }

        if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            startEnemyAttack("The teddy doesn't move.", "But the room starts to close in.");
        } else {
            startEnemyAttack("The enemy watches silently.", "The shadows swell and close around you.");
        }
    }

    private void performMercy() {
        Enemy enemy = battleController.getCurrent();
        actionText = "You look at the enemy with pity.";

        if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            messageText = "For a moment, its button eye seems softer.";

            if (Math.random() < 0.2) {
                // kompletter Sieg without later enemies
                battleState = BattleState.WIN;
                menuState = MenuState.WIN;
                messageText = "The teddy drops its arms. The fight is over.";
                actionText = "You feel another gaze waiting in the dark.";
            } else {
                startEnemyAttack("The teddy stares back.", "Your mercy is not enough.");
            }
        } else {
            messageText = "The enemy's painted smile doesn't change.";
            startEnemyAttack("The enemy's shadow stretches.", "Cold air cuts across your skin.");
        }
    }

    // ==== Enemy Bullet-Hell ====
    private void startEnemyAttack(String msg, String act) {
        battleState = BattleState.ENEMY_TURN;
        menuState = MenuState.ENEMY_TURN;

        messageText = msg;
        actionText = act;

        arena.inBulletHell = true;
        arena.attackTimer = 0f;
        arena.spawnTimer = 0f;
        arena.iFrameTimer = 0f;
        bullets.clear();

        // Standard: erstmal Mitte
        arena.resetSoul();

        // Schwierigkeit je nach aktuellem Enemy
        Enemy enemy = battleController.getCurrent();
        if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            arena.attackDuration = 4f;
            arena.spawnInterval = 0.25f;
        } else {
            arena.attackDuration = 6f;
            arena.spawnInterval = 0.16f; // schneller = schwerer
        }

        // Teddy: nur die ersten 3 Pattern, andere Gegner können alle Patterns nutzen
        int maxIndex = (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) ? 3 : BulletPattern.values().length;
        int patternIndex = (int) (Math.random() * maxIndex);
        currentPattern = BulletPattern.values()[patternIndex];

        // Für Circle-Burst: Soul unten spawnen & Winkel-Offset resetten
        if (currentPattern == BulletPattern.CIRCLE_BURST) {
            circleBurstAngleOffset = 0f;
            arena.soulX = arena.x + Arena.WIDTH / 2f;
            arena.soulY = arena.y + arena.soulRadius + 8f; // knapp über dem unteren Rand
        }

        audio.playEnemyAttackStart();
    }

    private void updateBulletHell(float delta) {
        if (!arena.inBulletHell) return;

        arena.attackTimer += delta;
        arena.spawnTimer -= delta;
        if (arena.iFrameTimer > 0f) {
            arena.iFrameTimer -= delta;
        }

        // neue Bullets spawnen
        if (arena.spawnTimer <= 0f) {
            arena.spawnTimer = arena.spawnInterval;
            spawnBulletPattern();
        }

        // Bullets bewegen & Kollision prüfen
        for (Bullet b : bullets) {
            if (!b.alive) continue;

            b.x += b.vx * delta;
            b.y += b.vy * delta;

            if (b.x < arena.x - 50 || b.x > arena.x + Arena.WIDTH + 50 ||
                b.y < arena.y - 50 || b.y > arena.y + Arena.HEIGHT + 50) {
                b.alive = false;
                continue;
            }

            float dx = b.x - arena.soulX;
            float dy = b.y - arena.soulY;
            float r = b.radius + arena.soulRadius;
            if (dx * dx + dy * dy <= r * r && arena.iFrameTimer <= 0f) {
                girl.takeDamage(5);
                arena.iFrameTimer = 0.5f;
                b.alive = false;
                actionText = "You are hit! -5 HP";

                // Hit-Effekt für Mädchen-Seele
                girlHitActive = true;
                girlHitTimer = HIT_DURATION;

                audio.playPlayerHit();

                if (girl.isDead()) {
                    endEnemyAttack(true);
                    return;
                }
            }
        }

        if (arena.attackTimer >= arena.attackDuration) {
            endEnemyAttack(false);
        }
    }

    // mehrere Bullet-Patterns
    private void spawnBulletPattern() {
        switch (currentPattern) {
            case HORIZONTAL_WAVE:
                spawnHorizontalWave();
                break;
            case VERTICAL_RAIN:
                spawnVerticalRain();
                break;
            case DIAGONAL_FAN:
                spawnDiagonalFan();
                break;
            case CIRCLE_BURST:
                spawnCircleBurst();
                break;
        }
    }

    // 1) wie dein altes Pattern: Schüsse von links/rechts
    private void spawnHorizontalWave() {
        BulletSpawner.spawnHorizontalWave(bullets, arena, 260f);
    }

    // 2) „Regen“ von oben
    private void spawnVerticalRain() {
        BulletSpawner.spawnVerticalRain(bullets, arena, 260f);
    }

    // 3) Diagonaler Fächer aus einer Seite
    private void spawnDiagonalFan() {
        BulletSpawner.spawnDiagonalFan(bullets, arena, 220f, 80f);
    }

    // 4) Kreis-Burst aus der Arena-Mitte – nur Doll, mit Rotation
    private void spawnCircleBurst() {
        circleBurstAngleOffset = BulletSpawner.spawnCircleBurst(bullets, arena, 220f, circleBurstAngleOffset, 10);
    }

    private void endEnemyAttack(boolean playerDied) {
        arena.inBulletHell = false;
        bullets.clear();

        if (playerDied || girl.isDead()) {
            battleState = BattleState.LOSE;
            menuState = MenuState.LOSE;
            messageText = "You can't tell what's real anymore.";
            actionText = "";
        } else {
            battleState = BattleState.PLAYER_TURN;
            menuState = MenuState.MAIN_MENU;
            messageText = "The room stops moving for a moment.";
            actionText = "";
        }
    }

    private void resetBattle() {
        girl = new Player("Mädchen");
        battleController.clearAndAddDefaults();

        battleState = BattleState.PLAYER_TURN;
        menuState = MenuState.MAIN_MENU;
        messageText = "You wake up in the same room again.";
        actionText = "";
        selectedIndex = 0;
        arena.inBulletHell = false;
        bullets.clear();
        teddyHitActive = false;
        girlHitActive = false;
        circleBurstAngleOffset = 0f;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        fontBig.dispose();
        teddyTexture.dispose();
        dollTexture.dispose();
        girlTexture.dispose();
        if (battleMusic != null) {
            battleMusic.dispose();
        }
        if (audio != null) audio.dispose();
    }
}
