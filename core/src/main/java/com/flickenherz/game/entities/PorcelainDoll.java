package com.flickenherz.game.entities;

public class PorcelainDoll implements Enemy {

    public String name = "Porzellanpuppe";
    public int maxHp = 120;
    public int hp = maxHp;

    @Override
    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public void takeDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getHp() { return hp; }

    @Override
    public int getMaxHp() { return maxHp; }

    @Override
    public EnemyType getType() { return EnemyType.DOLL; }

    @Override
    public String getDefeatMessage() { return "The doll shatters to pieces. The room is silent."; }

    @Override
    public String getDefeatAction() { return "Press ENTER to wake up."; }

    // körperlicher Angriff (z.B. mit Scherben)
    public int shardSlash() {
        return 10 + (int)(Math.random() * 6); // 10–15 Schaden
    }

    // psychischer Angriff (Sanity-Schaden, falls du das nutzen willst)
    public int lullabyWhisper() {
        return 8 + (int)(Math.random() * 6); // 8–13 Schaden
    }
}
