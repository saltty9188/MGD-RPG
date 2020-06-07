package com.mygdx.game.Items;

import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

public class Ether extends Item {

    private RPGGame game;
    private Player player;
    private int etherRestore;
    private String name;

    public Ether(String name, Player player) {
        this.name = name;
        this.player = player;
        etherRestore = 5;
    }

    public void use() {
        removeItem();
    }

    public int getPPRestored() {
        return etherRestore;
    }

    @Override
    public String getBattleMessage(String name) {
        return name + " used an " + getName();
    }

    public String getName() {
        return name;
    }
}
