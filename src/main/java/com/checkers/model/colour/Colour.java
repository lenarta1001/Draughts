package com.checkers.model.colour;

/**
 * A színhez felsorolt típus 
 */
public enum Colour {
    black,
    white;

    /**
     * Egy szín ellentetjét képzi
     * @param colour a szin, aminek az ellentetjét akarjuk képezni 
     * @return a szin ellentetje
     */
    public static Colour opposite(Colour colour) {
        return colour == black ? white : black;
    }
}
