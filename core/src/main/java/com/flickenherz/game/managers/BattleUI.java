package com.flickenherz.game.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Manager for rendering battle UI elements.
 * Handles drawing of message areas, bottom menu, and score display.
 * Uses StringBuilder for optimized string operations to reduce garbage collection.
 */
public class BattleUI {

    /** Font for regular text rendering */
    private final BitmapFont font;
    
    /** Font for larger text rendering */
    private final BitmapFont fontBig;
    
    /** StringBuilder for efficient string concatenation without allocations */
    private final StringBuilder stringBuilder = new StringBuilder(50);

    /**
     * Creates a new battle UI manager with the specified fonts.
     * 
     * @param font Regular bitmap font for standard text
     * @param fontBig Larger bitmap font for emphasized text
     */
    public BattleUI(BitmapFont font, BitmapFont fontBig) {
        this.font = font;
        this.fontBig = fontBig;
    }

    /**
     * Draws the message area displaying battle status and action text.
     * 
     * @param batch SpriteBatch to draw with
     * @param messageText Primary message to display
     * @param actionText Action description text
     * @param virtualHeight Virtual height of the screen for positioning
     */
    public void drawMessageArea(SpriteBatch batch, String messageText, String actionText, int virtualHeight) {
        font.getData().setScale(1.6f);
        font.draw(batch, messageText, 120, virtualHeight / 2f + 60);

        font.getData().setScale(1.3f);
        font.draw(batch, actionText, 120, virtualHeight / 2f - 10);
    }

    /**
     * Draws the bottom menu with selectable items.
     * Highlights the currently selected item when highlight is active.
     * 
     * @param batch SpriteBatch to draw with
     * @param menuItems Array of menu item strings to display
     * @param selectedIndex Index of currently selected item
     * @param highlightActive Whether to show selection highlight
     * @param virtualWidth Virtual width of the screen for centering
     */
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

    /**
     * Draws the player's score in the top-right corner.
     * 
     * @param batch SpriteBatch to draw with
     * @param score Current score value to display
     * @param virtualWidth Virtual width for positioning
     * @param virtualHeight Virtual height for positioning
     */
    public void drawScore(SpriteBatch batch, int score, int virtualWidth, int virtualHeight) {
        font.getData().setScale(1.5f);
        stringBuilder.setLength(0);
        stringBuilder.append("Score: ");
        stringBuilder.append(score);
        font.draw(batch, stringBuilder, virtualWidth - 300, virtualHeight - 80);
    }
}
