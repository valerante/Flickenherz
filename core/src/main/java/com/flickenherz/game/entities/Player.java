package com.flickenherz.game.entities;

public class Player {
    public String name;
    public int hp;
    public int maxHp;
    public int sanity;
    public int maxSanity;
    public int score;

    public Player(String name) {
        this.name = name;
        this.maxHp = 100;
        this.hp = maxHp;
        this.maxSanity = 100;
        this.sanity = maxSanity;
        this.score = 0;
    }

    public int basicAttack() {
        return 10 + (int)(Math.random() * 9);
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    public void loseSanity(int amount) {
        sanity -= amount;
        if (sanity < 0) sanity = 0;
    }

    public boolean isDead() {
        return hp <= 0 || sanity <= 0;
    }

    public void addScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }
}
