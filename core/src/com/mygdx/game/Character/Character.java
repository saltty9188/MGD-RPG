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

    private int baseWidth;
    private int baseHeight;

    public Character() {
        currentAni = idleAni;
        stateTimer = 0.0f;
    }

    /**
     * Initialises the Character with a sprite sheet and a base sprite for the superclass (Sprite) to be
     * initialised with.
     * @param spriteSheet The sprite sheet holding the animation frames for this Character.
     * @param width       The width of the Character.
     * @param height      The height of the Character.
     */
    public Character(Texture spriteSheet, int width, int height) {
        this.spriteSheet = spriteSheet;
        baseWidth = width;
        baseHeight = height;
        setSize(width, height);
    }

    public void update(float delta){
        setRegion(getFrame(delta));
    }

    public void setAnimation(int i){
        this.setSize(baseWidth, baseHeight);
        switch (i){
            case 0:
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
