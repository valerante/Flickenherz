package com.flickenherz.game.entities;

public class TeddyBear implements Enemy {
    public String name;
    public int hp;
    public int maxHp;

    public TeddyBear() {
        this.name = "Zerrissener Teddybär";
        this.maxHp = 80;
        this.hp = maxHp;
    }

    public int clawAttack() {
        return 8 + (int)(Math.random() * 7);
    }

    public int fearAttack() {
        return 4 + (int)(Math.random() * 5);
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
    public EnemyType getType() { return EnemyType.TEDDY; }

    @Override
    public String getDefeatMessage() { return "The teddy falls apart into stuffing."; }

    @Override
    public String getDefeatAction() { return "Something else in the room starts moving."; }
}
