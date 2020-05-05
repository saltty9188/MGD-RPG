package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Character {

    Animation<TextureRegion> itsAllAboutThePirouettes;

    public Player(){
        super(new Texture("character.png"), new TextureRegion(new Texture("character.png"), 0, 0, 16,32));
        stateTimer = 0.0f; genAnimations(); currentAni = itsAllAboutThePirouettes;
    }

    public void setAnimation(int i) {
        super.setAnimation(i);
        switch(i) {
            case 9: currentAni = itsAllAboutThePirouettes;
                    break;
        }
    }

    //character specific for sprite sheet dimensions
    private void genAnimations(){
        //Walk Down Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 0, 0, 16, 32),
                new TextureRegion(spriteSheet, 16, 0, 16, 32),
                new TextureRegion(spriteSheet, 32, 0, 16, 32),
                new TextureRegion(spriteSheet, 48, 0, 16, 32)};
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Right Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 0, 32, 16,32),
                new TextureRegion(spriteSheet, 16,32, 16, 32),
                new TextureRegion(spriteSheet, 32, 32, 16, 32),
                new TextureRegion(spriteSheet, 48, 32, 16, 32)};
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Left Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 0, 96, 16,32),
                new TextureRegion(spriteSheet, 16,96, 16, 32),
                new TextureRegion(spriteSheet, 32, 96, 16, 32),
                new TextureRegion(spriteSheet, 48, 96, 16, 32)};
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Up Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 0, 64, 16,32),
                new TextureRegion(spriteSheet, 16,64, 16, 32),
                new TextureRegion(spriteSheet, 32, 64, 16, 32),
                new TextureRegion(spriteSheet, 48, 64, 16, 32)};
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        //Idle Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 0, 0, 16,32),
                new TextureRegion(spriteSheet, 80,0, 16, 32)};
        idleAni  = new Animation<TextureRegion>(0.5f, fFrames);
        //SpinnyBOY
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 32, 128, 32, 32),
                new TextureRegion(spriteSheet, 32,192, 32, 32),
                new TextureRegion(spriteSheet, 32, 160 , 32, 32),
                new TextureRegion(spriteSheet, 32, 224, 32, 32)};
        itsAllAboutThePirouettes = new Animation<TextureRegion>(AFS, fFrames);
    }
}
