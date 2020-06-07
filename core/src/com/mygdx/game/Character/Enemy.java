package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Attack;

import java.util.Random;



/**
 * The class that represents the Enemies the Player will face both in the overworld
 * and in the battle screens.
 */
public abstract class Enemy extends BattleCharacter {

    private int exp;
    protected int gold;

    private Random rand;
    private float walkDuration;
    private int direction;

    public Enemy(Texture spriteSheet, int width, int height, Texture battleSprite,
                 int maxHP, int strength, int defence, int speed, int level, String name, int exp) {
        super(spriteSheet, width, height, battleSprite, maxHP, strength, defence, speed, level, name);

        this.exp = exp;

        this.spriteSheet = spriteSheet;
        stateTimer = 0.0f;
        genAnimations();
        currentAni = idleAni;

        //Do attack stuff later
        setAttacks();

        rand = new Random();
        walkDuration = 0.5f;
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = currentAni.getKeyFrame(stateTimer, true);
        stateTimer += dt;
        return region;
    }

    protected abstract void genAnimations();

    public abstract void setAttacks();

    public void update(float delta, Rectangle roamZone) {
        setRegion(getFrame(delta));
        if(walkDuration <= 0) {
            direction = rand.nextInt(4);
            walkDuration = 0.5f;
        }

        Vector2 enemyDelta = new Vector2();

        int right = (int)Math.ceil(Math.max(
                getX() + getWidth(),
                getX() + getWidth() + enemyDelta.x
        ));
        int top = (int)Math.ceil(Math.max(
                getY() + getHeight(),
                getY() + getHeight() + enemyDelta.y
        ));
        int left = (int)Math.floor(Math.min(
                getX(),
                getX() + enemyDelta.x
        ));
        int bottom = (int)Math.floor(Math.min(
                getY(),
                getY() + enemyDelta.y
        ));

        switch (direction) {
            //Move up
            case 0:
                if(top < roamZone.y + roamZone.getHeight()) enemyDelta.y = 98 * delta;
                currentAni = walkUpAni;
                break;
            //Move right
            case 1:
                if(right < roamZone.x + roamZone.getWidth()) enemyDelta.x = 98 * delta;
                currentAni = walkRightAni;
                break;
            //Move down
            case 2:
                if(bottom > roamZone.y) enemyDelta.y = -98 * delta;
                currentAni = walkDownAni;
                break;
            //Move left
            case 3:
                if(left > roamZone.x) enemyDelta.x = -98 * delta;
                currentAni = walkLeftAni;
                break;
        }
        if(enemyDelta.len2() <= 0) {
            currentAni = idleAni;
        }

        translate(enemyDelta.x, enemyDelta.y);

        walkDuration -= delta;
    }

    /**
     * Performs a random Attack from the list of Attacks.
     * @return The amount of damage this attack will do to the Player.
     */
    public Attack attack() {
        Attack attack;
        do {
            attack = attacks[rand.nextInt(attacks.length)];
        } while(attack.getPP() == 0);
        attack.decrementPP();
        return attack;
    }

    public int getExp() {
        return exp;
    }

    public int getGold() {
        return gold;
    }
}
