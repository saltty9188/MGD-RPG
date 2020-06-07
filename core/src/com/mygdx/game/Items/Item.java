package com.mygdx.game.Items;

import java.util.ArrayList;

public abstract class Item {

    int qty;

    Item[] items;

    private String name;

    public void removeItem() {
        qty--;
    }

    public void addItems(int qty) {
        this.qty += qty;
    }

    public abstract void use();

    public abstract String getBattleMessage(String name);

    public int getQty() {
        return qty;
    }

    public String getName() {
        return name;
    }

}
