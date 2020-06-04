package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RestlessLeaves extends Enemy {

    public RestlessLeaves() {
        super(new Texture("log.png"), 20, 23, new Texture("log-battle.png"),
                100, 10, 5, 5, 3, "Restless Leaves", 20);
    }

    protected void genAnimations() {
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
}
