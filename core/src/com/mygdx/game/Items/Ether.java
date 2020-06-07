package com.mygdx.game.Items;

import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

public class Ether extends Item {

    protected int restoration;

    public Ether() {
        this.name = "Ether";
        this.restoration = 5;

        value = 30;
    }

    public void use() {
        removeItem();
    }

    public int getRestoration() {
        return restoration;
    }

    @Override
    public String getBattleMessage(String name) {
        // test if the ether name starts with a vowel
        String vowels = "aeiou";
        String middleString;
        if(vowels.indexOf(Character.toLowerCase(getName().charAt(0))) != -1) {
            middleString = " used an ";
        } else {
            middleString = " used a ";
        }

        return name + middleString + getName();
    }

    public String getName() {
        return name;
    }
}
