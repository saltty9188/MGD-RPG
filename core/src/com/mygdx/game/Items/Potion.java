package com.mygdx.game.Items;

import com.badlogic.gdx.Game;
import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

public class Potion extends Item {
    private Player character;
    private int restoration;
    private String name;

    public Potion(String name, int restoration, Player player) {
        this.name = name;
        this.restoration = restoration;
        this.character = player;
    }

    public int getRestoration() {
        return restoration;
    }

    public void use() {
        character.restoreHealth(getRestoration());
        removeItem();
    }

    public String getName() {
        return name;
    }

    public String getBattleMessage(String name) {
        return name + " used a " + getName();
    }

}
