package com.flickenherz.game.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Rendert Kampf-UI-Elemente
public class BattleUI {

    private final BitmapFont font;
    private final BitmapFont fontBig;
    
    // StringBuilder für effiziente String-Operationen
    private final StringBuilder stringBuilder = new StringBuilder(50);

    public BattleUI(BitmapFont font, BitmapFont fontBig) {
        this.font = font;
        this.fontBig = fontBig;
    }

    // Zeichne Nachrichtenbereich mit Kampfstatus
    public void drawMessageArea(SpriteBatch batch, String messageText, String actionText, int virtualHeight) {
        font.getData().setScale(1.6f);
        font.draw(batch, messageText, 120, virtualHeight / 2f + 60);

        font.getData().setScale(1.3f);
        font.draw(batch, actionText, 120, virtualHeight / 2f - 10);
    }

    // Zeichne unteres Menü mit hervorgehobener Auswahl
    public void drawBottomMenu(SpriteBatch batch, String[] menuItems, int selectedIndex, boolean highlightActive, int virtualWidth) {
        float outerMargin = 60f;
        float baseY = outerMargin + 80;
        float centerX = virtualWidth / 2f;

        fontBig.getData().setScale(2f);

        float spacing = 260f;
        float totalSpacing = (menuItems.length - 1) * spacing;
        float startX = centerX - totalSpacing / 2f;

        for (int i = 0; i < menuItems.length; i++) {
            String label = menuItems[i];
            float x = startX + i * spacing;

            if (i == selectedIndex && highlightActive) {
                stringBuilder.setLength(0);
                stringBuilder.append(">");
                stringBuilder.append(label);
                fontBig.draw(batch, stringBuilder, x, baseY);
            } else {
                fontBig.draw(batch, label, x, baseY);
            }
        }
    }

    // Zeichne Punktzahl in der oberen rechten Ecke
    public void drawScore(SpriteBatch batch, int score, int virtualWidth, int virtualHeight) {
        font.getData().setScale(1.5f);
        stringBuilder.setLength(0);
        stringBuilder.append("Score: ");
        stringBuilder.append(score);
        font.draw(batch, stringBuilder, virtualWidth - 300, virtualHeight - 80);
    }
}
