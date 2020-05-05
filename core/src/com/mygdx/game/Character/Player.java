package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Character {

    Animation<TextureRegion> itsAllAboutThePirouettes;

    private Texture spriteSheet;

    public Player(){
        super(new Texture("character.png"),
                new TextureRegion(new Texture("character.png"),
                        1, 5, 15, 23));
        spriteSheet = new Texture("character.png");
        stateTimer = 0.0f;
        genAnimations();
        currentAni = itsAllAboutThePirouettes;
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
