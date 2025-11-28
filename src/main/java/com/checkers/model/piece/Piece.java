package com.checkers.model.piece;

import java.awt.Point;
import java.util.List;

import com.checkers.model.move.*;
import com.checkers.view.GamePanel;
import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;


/**
 * A bábuk abstract osztálya
 */
public abstract class Piece {
    private Colour colour;

    /**
     * A bábu konstruktora
     * @param colour a bábu színe
     */
    public Piece(Colour colour) {
        this.colour = colour;
    }

    /**
     * A babu színének gettere
     * @return a bábu színe
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * A Fen jelöléshez szükséges karakterlánccá alakító metódus
     */
    public String toString() {
        return "";
    }

    /**
     * Egy tábla adott pontjáról elvégezhető összes lépés a bábuval
     * @param table a tábla, amin vizsgáljuk a lépéseket
     * @param point a pont, ahonnan a lépeseket el akarjuk végezni
     * @return az elvégezhető összes lépés
     */
    public abstract List<Move> validMoves(Board table, Point point);

    public abstract void draw(GamePanel gp, int x, int y);  
}
