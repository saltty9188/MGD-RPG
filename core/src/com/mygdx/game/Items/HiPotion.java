package com.mygdx.game.Items;

import com.mygdx.game.Character.Player;

public class HiPotion extends Potion {

    public HiPotion( Player player) {
        super(player);
        this.name = "Hi-Potion";
        this.restoration = 50;

        description = "Restores 50 HP.";
        value = 30;
    }
}
