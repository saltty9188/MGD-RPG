package com.mygdx.game.Items;

import java.util.ArrayList;

public abstract class Item {

    int qty;

    Item[] items;

    private String name;

    public abstract int getItems();

    public abstract void removeItems(Item item);

    public abstract void addItems(Item item);

    public abstract void use();

    public String toString(int qty) {
        return Integer.toString(qty);
    }

    public String getQtyString() {
        return toString(qty);
    }

    public int getQty() {
        return qty;
    }

    public String getName() {
        return name;
    }

}
