package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attack;

/**
 * The class that represents the Enemies the Player will face both in the overworld
 * and in the battle screens.
 */
public class Enemy extends Character {

    // Sprite used on the BattleScreen
    Texture battleSprite;

    // Enemy's stats
    private int maxHP;
    private int HP;
    private int strength;
    private int defence;
    private int speed;

    private String name;

    private Attack[] attacks;

    private boolean alive;

    public Enemy() {
        super(new Texture("placeholder.png"), 15, 23);
        battleSprite = new Texture("placeholder.png");
        alive = true;
        setRegion(spriteSheet);
    }

    public Enemy(Texture spriteSheet, int width, int height, Texture battleSprite, String name,
                 int maxHP, int strength, int defence, int speed, Attack... attacks) {
        super(spriteSheet, width, height);
        this.battleSprite = battleSprite;
        this.name = name;
        this.maxHP = maxHP;
        this.HP = maxHP;
        this.strength = strength;
        this.defence = defence;
        this.speed = speed;
        this.attacks = attacks;

        alive = true;

        setRegion(spriteSheet);
    }

    /**
     * Performs a random Attack from the list of Attacks.
     * @return The amount of damage this attack will do to the Player.
     */
    public int attack() {
        Attack attack = attacks[((int) Math.random()) % attacks.length];
        attack.decrementPP();
        //Change to battlescreen output when we do that stuff
        System.out.println(attack.battleMessage(name));
        System.out.println("It did " + strength + attack.getDamage() + " damage!");
        return strength + attack.getDamage();
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    public Texture getBattleSprite() {
        return battleSprite;
    }
}
