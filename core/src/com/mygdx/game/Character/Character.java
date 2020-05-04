package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * The base class for all the Characters of the game.
 */
public class Character extends Sprite {

    // The sprite sheet holding the Character's sprite(s).
    private Texture spriteSheet;

    public Character(Texture spriteSheet) {
        this.spriteSheet = spriteSheet;
    }
}
