package com.mygdx.game.Items;

import com.badlogic.gdx.Game;
import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

public class Potion extends Item {
    private Player character;
    private RPGGame game;
    private int potionHealth;

    public Potion() {

    }

    public Potion(RPGGame game, Player character) {
        this.game = game;
        this.character = character;
        potionHealth = 20;
    }

    public int getItems() {
        return items.length;
    }

    public void addItems(Item item) {
        character.addItem(item);
    }

    public void removeItems(Item item) {
        //items.remove(item);
    }

    public int getHealth() {
        return potionHealth;
    }

    public void usePotion(Potion potion) {
        removeItems(potion);
        character.restoreHealth(potion.getHealth());
    }

}
