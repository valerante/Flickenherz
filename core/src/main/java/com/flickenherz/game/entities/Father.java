package com.flickenherz.game.entities;

// Final boss: Father
public class Father implements Enemy {
    public String name;
    public int hp;
    public int maxHp;

    public Father() {
        this.name = "Father";
        this.maxHp = 250;
        this.hp = maxHp;
    }

    public int clawAttack() {
        return 12 + (int)(Math.random() * 10);
    }

    public int fearAttack() {
        return 8 + (int)(Math.random() * 9);
    }

    public int shardSlash() {
        return 14 + (int)(Math.random() * 10);
    }

    public int lullabyWhisper() {
        return 12 + (int)(Math.random() * 10);
    }

    @Override
    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    @Override
    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getHp() { return hp; }

    @Override
    public int getMaxHp() { return maxHp; }

    @Override
    public EnemyType getType() { return EnemyType.OTHER; }

    @Override
    public String getDefeatMessage() { 
        return "The Father collapses. The nightmare is finally over."; 
    }

    @Override
    public String getDefeatAction() { 
        return "Press ENTER to wake up."; 
    }
}
