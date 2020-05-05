package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Character {

    float stateTimer;
    float AFS = 0.15f;
    Texture playerTextures;
    TextureRegion temp0, temp1, temp2, temp3;
    TextureRegion[] fFrames;
    Animation<TextureRegion> currentAni, idleAni, walkDownAni, walkRightAni, walkLeftAni, walkUpAni
            , itsAllAboutThePirouettes;

    public Player(){
        playerTextures = new Texture("character.png");
        stateTimer = 0.0f; genAnimations(); currentAni = itsAllAboutThePirouettes;
    }

    public void update(float delta){
        setRegion(getFrame(delta));
    }

    public void setAnimation(int i){
        switch (i){
            case 0: currentAni = idleAni; break;
            case 1: currentAni = walkUpAni; break;
            case 2: currentAni = walkLeftAni; break;
            case 3: currentAni = walkDownAni; break;
            case 4: currentAni = walkRightAni; break;
            case 9: currentAni = itsAllAboutThePirouettes; break;
        }
    }

    private TextureRegion getFrame(float delta){
        TextureRegion region;
        region = currentAni.getKeyFrame(stateTimer, true);
        stateTimer += delta;
        return region;
    }

    private void genAnimations(){
        //Walk Down Animation
        temp0 = new TextureRegion(playerTextures, 0, 0, 16,32);
        temp1 = new TextureRegion(playerTextures, 16,0, 16, 32);
        temp2 = new TextureRegion(playerTextures, 32, 0, 16, 32);
        temp3 = new TextureRegion(playerTextures, 48, 0, 16, 32);
        fFrames = new TextureRegion[4*1];
        fFrames[0] = temp0;fFrames[1] = temp1;fFrames[2] = temp2;fFrames[3] = temp3;
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Right Animation
        temp0 = new TextureRegion(playerTextures, 0, 32, 16,32);
        temp1 = new TextureRegion(playerTextures, 16,32, 16, 32);
        temp2 = new TextureRegion(playerTextures, 32, 32, 16, 32);
        temp3 = new TextureRegion(playerTextures, 48, 32, 16, 32);
        fFrames = new TextureRegion[4*1];
        fFrames[0] = temp0;fFrames[1] = temp1;fFrames[2] = temp2;fFrames[3] = temp3;
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Left Animation
        temp0 = new TextureRegion(playerTextures, 0, 96, 16,32);
        temp1 = new TextureRegion(playerTextures, 16,96, 16, 32);
        temp2 = new TextureRegion(playerTextures, 32, 96, 16, 32);
        temp3 = new TextureRegion(playerTextures, 48, 96, 16, 32);
        fFrames = new TextureRegion[4*1];
        fFrames[0] = temp0;fFrames[1] = temp1;fFrames[2] = temp2;fFrames[3] = temp3;
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Up Animation
        temp0 = new TextureRegion(playerTextures, 0, 64, 16,32);
        temp1 = new TextureRegion(playerTextures, 16,64, 16, 32);
        temp2 = new TextureRegion(playerTextures, 32, 64, 16, 32);
        temp3 = new TextureRegion(playerTextures, 48, 64, 16, 32);
        fFrames = new TextureRegion[4*1];
        fFrames[0] = temp0;fFrames[1] = temp1;fFrames[2] = temp2;fFrames[3] = temp3;
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        //Idle Animation
        temp0 = new TextureRegion(playerTextures, 0, 0, 16,32);
        temp1 = new TextureRegion(playerTextures, 80,0, 16, 32);
        fFrames = new TextureRegion[2*1];
        fFrames[0] = temp0;fFrames[1] = temp1;
        idleAni  = new Animation<TextureRegion>(0.5f, fFrames);
        //SpinnyBOY
        temp0 = new TextureRegion(playerTextures, 32, 128, 32, 32);
        temp1 = new TextureRegion(playerTextures, 32,192, 32, 32);
        temp2 = new TextureRegion(playerTextures, 32, 160 , 32, 32);
        temp3 = new TextureRegion(playerTextures, 32, 224, 32, 32);
        fFrames = new TextureRegion[4*1];
        fFrames[0] = temp0;fFrames[1] = temp1;fFrames[2] = temp2;fFrames[3] = temp3;
        itsAllAboutThePirouettes = new Animation<TextureRegion>(AFS, fFrames);
    }
}
