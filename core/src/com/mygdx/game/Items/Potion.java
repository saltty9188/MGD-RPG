package com.mygdx.game.Items;

import com.mygdx.game.Character.Player;

public class Potion extends Item {
    private Player character;
    protected int restoration;

    public Potion(Player player) {
        this.name = "Potion";
        this.restoration = 20;
        this.character = player;

        description = "Restores 20 HP.";
        value = 10;
    }

    public int getRestoration() {
        return restoration;
    }

    public void use() {
        character.restoreHealth(getRestoration());
        removeItems(1);
    }

    public String getName() {
        return name;
    }

    public String getBattleMessage(String name) {
        return name + " used a " + getName();
    }

}
