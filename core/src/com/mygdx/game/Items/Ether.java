package com.mygdx.game.Items;

import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

public class Ether extends Item {

    private int restoration;
    private String name;

    public Ether(String name, int restoration) {
        this.name = name;
        this.restoration = restoration;
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
