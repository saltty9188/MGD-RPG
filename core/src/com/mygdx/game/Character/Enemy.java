package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;

/**
 * The class that represents the Enemies the Player will face both in the overworld
 * and in the battle screens.
 */
public class Enemy extends Character {

    // Sprite used on the BattleScreen
    private Texture battleSprite;

    // Enemy's stats
    private int maxHP;
    private int HP;
    private int strength;
    private int defence;
    private int speed;

    private String name;

    public Enemy(Texture spriteSheet, Texture battleSprite, String name, int maxHP, int strength, int defence, int speed) {

        super(spriteSheet);
        this.battleSprite = battleSprite;
        this.name = name;
        this.maxHP = maxHP;
        this.HP = maxHP;
        this.strength = strength;
        this.defence = defence;
        this.speed = speed;
    }


}
