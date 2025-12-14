package com.flickenherz.game.entities;

/**
 * First enemy: A torn teddy bear.
 * Represents the first threat in the nightmare sequence.
 */
public class TeddyBear implements Enemy {
    /** Enemy name */
    public String name;
    
    /** Current health points */
    public int hp;
    
    /** Maximum health points */
    public int maxHp;

    /**
     * Creates a new teddy bear enemy with 80 HP.
     */
    public TeddyBear() {
        this.name = "Zerrissener Teddybär";
        this.maxHp = 80;
        this.hp = maxHp;
    }

    /**
     * Claw attack dealing physical damage.
     * @return Damage dealt (8-14 HP)
     */
    public int clawAttack() {
        return 8 + (int)(Math.random() * 7);
    }

    /**
     * Fear attack dealing psychological damage.
     * @return Damage dealt (4-8 HP)
     */
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
