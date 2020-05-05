package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The base class for all the Characters of the game.
 */
public class Character extends Sprite {

    // The sprite sheet holding the Character's sprite(s).
    protected Texture spriteSheet;
    protected float stateTimer;
    public static final float AFS = 0.15f;
    protected TextureRegion[] fFrames;
    protected Animation<TextureRegion> currentAni, idleAni, walkDownAni, walkRightAni, walkLeftAni,
            walkUpAni;

    public Character() {
        currentAni = idleAni;
        stateTimer = 0.0f;
    }

    public Character(Texture spriteSheet, TextureRegion baseSprite) {
        super(baseSprite);
        this.spriteSheet = spriteSheet;
    }

    public void update(float delta){
        setRegion(getFrame(delta));
    }

    public void setAnimation(int i){
        switch (i){
            case 0:
                this.setSize(15,23);
                currentAni = idleAni;
                break;
            case 1:
                currentAni = walkUpAni;
                break;
            case 2:
                currentAni = walkLeftAni;
                break;
            case 3:
                currentAni = walkDownAni;
                break;
            case 4:
                currentAni = walkRightAni;
                break;
        }
    }

    protected TextureRegion getFrame(float delta){
        TextureRegion region;
        region = currentAni.getKeyFrame(stateTimer, true);
        stateTimer += delta;
        return region;
    }
}
