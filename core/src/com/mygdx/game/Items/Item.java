package com.mygdx.game.Items;

import java.util.ArrayList;

public abstract class Item {
    ArrayList<Item> items;

    public abstract int getItems();

    public abstract void removeItems(Item item);

    public abstract void addItems(Item item);

}
