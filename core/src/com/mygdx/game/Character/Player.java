package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attack;
import com.mygdx.game.InfiniteAttack;

import java.util.Random;

public class Player extends BattleCharacter {

    Animation<TextureRegion> itsAllAboutThePirouettes;

    private int currentExp;
    private int toNextLevel;

    private Random rand;

    public Player(){
        super(new Texture("character.png"), 15, 23, new Texture("character-battle2.png"),
                100, 20, 5, 12, 1, "Hero");
        HP = 1;

        currentExp = 0;
        toNextLevel = (int) (6.1 * Math.pow(level + 1, 2) + 1.4 * (level + 1) - 11.4);

        rand = new Random();

        stateTimer = 0.0f;
        genAnimations();
        currentAni = itsAllAboutThePirouettes;

        InfiniteAttack attack1 = new InfiniteAttack(0, "Attack");
        Attack attack2 = new Attack(5, 20, "Attack 2");
        Attack attack3 = new Attack(5, 20, "Attack 3");
        Attack attack4 = new Attack(5, 20, "Attack 4");

        setAttacks(attack1, attack2, attack3, attack4);
    }

    public Attack getAttack(int index) {
        return attacks[index];
    }

    /**
     * Gives the player exp from a fallen enemy after battle. Also returns whether or not the player will level up.
     * @param exp The amount of exp given to the player
     * @return True if the player will gain a level from this exp, false otherwise.
     */
    public boolean receiveExp(int exp) {
        currentExp += exp;
        if(currentExp >= toNextLevel) {
            currentExp -= toNextLevel;
            return true;
        }
        return false;
    }

    /**
     * Increase the player's level and sets the exp required for the next level.
     * Increases the player's stats by a variable amount as well.
     */
    public void levelUp() {
        level += 1;
        toNextLevel = (int) (6.1 * Math.pow(level + 1, 2) + 1.4 * (level + 1) - 11.4);

        int HPIncrease = rand.nextInt(10) + 5;

        maxHP += HPIncrease;
        HP += HPIncrease;
        strength += rand.nextInt(5) + 1;
        defence += rand.nextInt(5) + 1;
        speed += rand.nextInt(3) + 1;
    }

    public int getLevel() {
        return level;
    }

    public void setAnimation(int i) {
        super.setAnimation(i);
        switch(i) {
            case 9:
                this.setSize(32,32);
                currentAni = itsAllAboutThePirouettes;
                break;
        }
    }

    //character specific for sprite sheet dimensions
    private void genAnimations(){
        //Walk Down Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 5, 15, 23),
                new TextureRegion(spriteSheet, 17, 5, 15, 23),
                new TextureRegion(spriteSheet, 33, 5, 15, 23),
                new TextureRegion(spriteSheet, 49, 5, 15, 23)};
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Right Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 2, 38, 15,23),
                new TextureRegion(spriteSheet, 18,38, 15, 23),
                new TextureRegion(spriteSheet, 34, 38, 15, 23),
                new TextureRegion(spriteSheet, 50, 38, 15, 23)};
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Left Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 102, 15,23),
                new TextureRegion(spriteSheet, 17,102, 15, 23),
                new TextureRegion(spriteSheet, 33, 102, 15, 23),
                new TextureRegion(spriteSheet, 49, 102, 15, 23)};
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Up Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 0, 69, 15,23),
                new TextureRegion(spriteSheet, 16,69, 15, 23),
                new TextureRegion(spriteSheet, 32, 69, 15, 23),
                new TextureRegion(spriteSheet, 48, 69, 15, 23)};
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        //Idle Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 5, 15,23),
                new TextureRegion(spriteSheet, 81,5, 15, 23)};
        idleAni  = new Animation<TextureRegion>(0.5f, fFrames);
        //SpinnyBOY
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 32, 128, 32, 32),
                new TextureRegion(spriteSheet, 32,192, 32, 32),
                new TextureRegion(spriteSheet, 32, 160 , 32, 32),
                new TextureRegion(spriteSheet, 32, 224, 32, 32)};
        itsAllAboutThePirouettes = new Animation<TextureRegion>(AFS, fFrames);
    }
}
