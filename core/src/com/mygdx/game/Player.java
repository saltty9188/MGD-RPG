package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Sprite {

    Texture playerSheet;
    TextureRegion walkDown,walkRight,walkUp,walkLeft;

    public Player(){
        playerSheet = new Texture("character.png");

    }

}
