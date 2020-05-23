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
public class Enemy extends BattleCharacter{

    private int exp;

    Random rand;
    float walkDuration;
    int direction;

    public Enemy() {
        this(new Texture("placeholder.png"), 15, 23, new Texture("placeholder.png"),
                100, 10, 5, 5, 3, "Uncle Tester", 20);


    }

    public Enemy(Texture spriteSheet, int width, int height, Texture battleSprite,
                 int maxHP, int strength, int defence, int speed, int level, String name, int exp) {
        super(spriteSheet, width, height, battleSprite, maxHP, strength, defence, speed, level, name);

        this.exp = exp;

        //Do attack stuff later
        Attack attack1 = new Attack(5, 20, "Attack 1");
        Attack attack2 = new Attack(5,5, "Attack 2");
        setAttacks(attack1, attack2);

        setRegion(spriteSheet);
        rand = new Random();
        walkDuration = 0.5f;
    }

    public void update(float delta, Rectangle roamZone) {
        if(walkDuration <= 0) {
            direction = rand.nextInt(4);
            walkDuration = 0.5f;
        }

        Vector2 enemyDelta = new Vector2(98*delta, 98*delta);

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
                if(top < roamZone.y + roamZone.getHeight()) translateY(enemyDelta.y);
                break;
            //Move right
            case 1:
                if(right < roamZone.x + roamZone.getWidth()) translateX(enemyDelta.x);
                break;
            //Move down
            case 2:
                if(bottom > roamZone.y) translateY(-(enemyDelta.y));
                break;
            //Move left
            case 3:
                if(left > roamZone.x) translateX(-(enemyDelta.x));
                break;
        }

        walkDuration -= delta;
    }

    /**
     * Performs a random Attack from the list of Attacks.
     * @return The amount of damage this attack will do to the Player.
     */
    public Attack attack() {
        Attack attack = attacks[rand.nextInt(attacks.length)];
        while(attack.getPP() == 0) {
            attack = attacks[rand.nextInt(attacks.length)];
        }
        attack.decrementPP();
        return attack;
    }

    public int getExp() {
        return exp;
    }
}
