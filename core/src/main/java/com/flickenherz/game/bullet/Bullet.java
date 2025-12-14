package com.flickenherz.game.bullet;

// Kugel-Projektil mit optionaler Wellenbewegung
public class Bullet {
    public float x, y;
    public float vx, vy;
    public float radius;
    public boolean alive = true;
    
    // Wellenbewegungsparameter
    public float baseY;
    public float waveAmplitude = 0f;
    public float waveFrequency = 0f;
    public float travelDistance = 0f;
    
    // Gecacht für schnellere Kollisionserkennung
    public float radiusSquared;

    public Bullet() {}

    public Bullet(float x, float y, float vx, float vy, float radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.alive = true;
        this.baseY = y;
    }
    
    // Kugel aus Objektpool wiederverwenden
    public void reset(float x, float y, float vx, float vy, float radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.alive = true;
        this.baseY = y;
        this.waveAmplitude = 0f;
        this.waveFrequency = 0f;
        this.travelDistance = 0f;
    }
}
