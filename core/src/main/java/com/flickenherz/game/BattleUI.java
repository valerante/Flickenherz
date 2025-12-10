package com.flickenherz.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BattleUI {

    private final BitmapFont font;
    private final BitmapFont fontBig;

    public BattleUI(BitmapFont font, BitmapFont fontBig) {
        this.font = font;
        this.fontBig = fontBig;
    }

    public void drawMessageArea(SpriteBatch batch, String messageText, String actionText, int virtualHeight) {
        font.getData().setScale(1.6f);
        font.draw(batch, messageText, 120, virtualHeight / 2f + 60);

        font.getData().setScale(1.3f);
        font.draw(batch, actionText, 120, virtualHeight / 2f - 10);
    }

    public void drawBottomMenu(SpriteBatch batch, String[] menuItems, int selectedIndex, boolean highlightActive, int virtualWidth) {
        float outerMargin = 60f;
        float baseY = outerMargin + 80;
        float centerX = virtualWidth / 2f;

        fontBig.getData().setScale(2f);

        float spacing = 260f;
        float startX = centerX - spacing * 1.5f;

        for (int i = 0; i < menuItems.length; i++) {
            String label = menuItems[i];
            float x = startX + i * spacing;

            if (i == selectedIndex && highlightActive) {
                fontBig.draw(batch, ">" + label, x, baseY);
            } else {
                fontBig.draw(batch, label, x, baseY);
            }
        }
    }
}
