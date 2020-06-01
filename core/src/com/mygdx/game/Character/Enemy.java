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
        this(new Texture("log.png"), 20, 23, new Texture("placeholder.png"),
                100, 10, 5, 5, 3, "Uncle Tester", 20);

    }

    public Enemy(Texture spriteSheet, int width, int height, Texture battleSprite,
                 int maxHP, int strength, int defence, int speed, int level, String name, int exp) {
        super(spriteSheet, width, height, battleSprite, maxHP, strength, defence, speed, level, name);

        this.exp = exp;

        this.spriteSheet = spriteSheet;
        stateTimer = 0.0f;
        genAnimations();
        currentAni = idleAni;

        //Do attack stuff later
        Attack attack1 = new Attack(5, 20, "Attack 1");
        Attack attack2 = new Attack(5,5, "Attack 2");
        setAttacks(attack1, attack2);

        rand = new Random();
        walkDuration = 0.5f;
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = currentAni.getKeyFrame(stateTimer, true);
        stateTimer += dt;
        return region;
    }

    public void genAnimations() {
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 4, 7, 20, 23),
            new TextureRegion(spriteSheet, 37, 7, 20, 23),
            new TextureRegion(spriteSheet, 68, 7, 20, 23),
            new TextureRegion(spriteSheet, 101, 7, 20, 23)};
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 165, 11, 20, 23),
            new TextureRegion(spriteSheet, 163, 41, 20, 23),
            new TextureRegion(spriteSheet, 163, 72, 20, 23)};
        idleAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 7, 39, 20, 23),
            new TextureRegion(spriteSheet, 39, 40, 20, 23),
            new TextureRegion(spriteSheet, 71, 39, 20, 23),
            new TextureRegion(spriteSheet, 104, 40, 20, 23)};
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 10, 101, 20, 23),
                new TextureRegion(spriteSheet, 42, 102, 20, 23),
                new TextureRegion(spriteSheet, 74, 101, 20, 23),
                new TextureRegion(spriteSheet, 106, 102, 20, 23)};
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 10, 69, 20, 23),
                new TextureRegion(spriteSheet, 42, 70, 20, 23),
                new TextureRegion(spriteSheet, 73, 69, 20, 23),
                new TextureRegion(spriteSheet, 106, 70, 20, 23)};
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
    }

    public void update(float delta, Rectangle roamZone) {
        setRegion(getFrame(delta));
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
            default:
                currentAni = idleAni;
                break;
            case 0:
                if(top < roamZone.y + roamZone.getHeight()) translateY(enemyDelta.y);
                currentAni = walkUpAni;
                break;
            //Move right
            case 1:
                if(right < roamZone.x + roamZone.getWidth()) translateX(enemyDelta.x);
                currentAni = walkRightAni;
                break;
            //Move down
            case 2:
                if(bottom > roamZone.y) translateY(-(enemyDelta.y));
                currentAni = walkDownAni;
                break;
            //Move left
            case 3:
                if(left > roamZone.x) translateX(-(enemyDelta.x));
                currentAni = walkLeftAni;
                break;
        }
        if(enemyDelta.len2() <= 0) {
            currentAni = idleAni;
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

    public void dispose() {
       // spriteSheet.dispose();
    }
}
