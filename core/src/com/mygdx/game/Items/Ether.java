package com.mygdx.game.Items;

import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

public class Ether extends Item {

    private RPGGame game;
    private Player player;
    private int etherRestore;
    private String name;

    public Ether(String name) {
        this.name = name;
    }

    public Ether(RPGGame game, Player player) {
        this.game = game;
        etherRestore = 10;
        this.player = player;
    }

    public int getItems() {
        return items.length;
    }

    public void removeItems(Item item) {
        player.removeItem(item);
    }

    public void addItems(Item item) {
        player.addItem(item);
    }

    public int getEtherRestore() {
        return etherRestore;
    }

    public void use() {
        removeItems(this);
    }

    public String getName() {
        return name;
    }
}
