package com.flickenherz.game;

import java.util.ArrayList;
import java.util.List;

public class BattleController {
    private final List<Enemy> enemies = new ArrayList<>();
    private int index = 0;

    public BattleController() {
        // default order: Teddy -> Doll
        enemies.add(new TeddyBear());
        enemies.add(new PorcelainDoll());
    }

    public Enemy getCurrent() {
        if (enemies.isEmpty()) return null;
        return enemies.get(index);
    }

    public boolean hasNext() {
        return index < enemies.size() - 1;
    }

    public boolean advanceToNext() {
        if (hasNext()) {
            index++;
            return true;
        }
        return false;
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public void reset() {
        index = 0;
    }

    public void clearAndAddDefaults() {
        enemies.clear();
        enemies.add(new TeddyBear());
        enemies.add(new PorcelainDoll());
        index = 0;
    }

    public List<Enemy> getEnemies() { return enemies; }
}
