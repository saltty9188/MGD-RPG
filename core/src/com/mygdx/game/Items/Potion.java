package com.mygdx.game.Items;

import com.badlogic.gdx.Game;
import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

public class Potion extends Item {
    private Player character;
    protected int restoration;

    public Potion(Player player) {
        this.name = "Potion";
        this.restoration = 20;
        this.character = player;

        value = 10;
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
