package com.mygdx.game.Items;

import com.badlogic.gdx.Game;
import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

public class Potion extends Item {
    private Player character;
    private int potionHealth;
    private String name;

    public Potion(String name, Player player) {
        this.name = name;
        potionHealth = 20;
        this.character = player;
    }

    public int getHealth() {
        return potionHealth;
    }

    public void use() {
        character.restoreHealth(getHealth());
        removeItem();
    }

    public String getName() {
        return name;
    }

    public String getBattleMessage(String name) {
        return name + " used a " + getName();
    }

}
