package com.flickenherz.game.controllers;

import java.util.ArrayList;
import java.util.List;

import com.flickenherz.game.entities.Enemy;
import com.flickenherz.game.entities.TeddyBear;
import com.flickenherz.game.entities.PorcelainDoll;
import com.flickenherz.game.entities.Cuddlefiend;
import com.flickenherz.game.entities.Father;

// Manages enemy encounter sequence
public class BattleController {
    private List<Enemy> enemies = new ArrayList<>();
    private int currentIndex = 0;

    public BattleController() {
        clearAndAddDefaults();
    }

    public void clearAndAddDefaults() {
        enemies.clear();
        enemies.add(new TeddyBear());
        enemies.add(new PorcelainDoll());
        enemies.add(new Cuddlefiend());
        enemies.add(new Father());
        currentIndex = 0;
    }

    public Enemy getCurrent() {
        if (currentIndex >= 0 && currentIndex < enemies.size()) {
            return enemies.get(currentIndex);
        }
        return null;
    }

    public boolean hasNext() {
        return (currentIndex + 1) < enemies.size();
    }

    public void advanceToNext() {
        if (hasNext()) {
            currentIndex++;
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        if (index >= 0 && index < enemies.size()) {
            currentIndex = index;
        }
    }

    public int getTotalEnemies() {
        return enemies.size();
    }
}
