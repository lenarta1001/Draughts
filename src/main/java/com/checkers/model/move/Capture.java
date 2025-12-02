package com.checkers.model.move;

import java.awt.Point;

import com.checkers.model.board.*;
import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.piece.King;
import com.checkers.view.GamePanel;

/**
 * Az ütés osztálya
 */
public class Capture extends Move {
    /**
     * Az ütés konstruktora
     * @param from a kezdő pozíció
     * @param to a végpozíció
     */
    public Capture(Point from, Point to, Board board) {
        super(from, to, board);
    }

    /**
     * Az karakterlánccá konvertálja az ütést (ld. specifikáció Portable Draughts Notation)
     * @return az ütés karakterlánc formátumban
     */
    public String toString() {
        return Board.squareNumberFromPoint(from) + "x" + Board.squareNumberFromPoint(to);
    }

    /**
     * @return az ütést kötelező-e elvégezni
     */
    public boolean isMandatory() {
        return true;
    }
    
    /**
     * Elvégzi az ütést a játék tábláján
     * @param game a játék aminek a tábláján az ütést végezzük
     */
    public void execute(Game game) {
        game.executeCapture(this);
    }

    /**
     * Elvégzi az ütést lépést a táblán
     * @param board a tábla, amin az ütést végezzük
     */
    public void execute(Board board) {
        if (!isPromotion()) {
            board.setPiece(board.getPiece(getFrom()), getTo());
        } else {
            Colour colour = board.getPiece(getFrom()).getColour();
            board.setPiece(new King(colour), getTo());
        }
        board.setPiece(null, getFrom());
        Point capturedPoint = new Point((getFrom().x + getTo().x) / 2, (getFrom().y + getTo().y) / 2);
        board.setPiece(null, capturedPoint);
    }

    /**
     * Eldönti, hogy egy másik objektummal tartalmilag azonos-e az ütés
     * @param o az objektum, amivel összehasonlítjuk
     * @return tartalmilag azonos-e az objektummal
     */
    public boolean equals(Object o) {
        if (this == o) { 
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Capture other = (Capture) o;
        return from.equals(other.from) && to.equals(other.to);
    }

    /** 
     * Kirajzolja az ütés kiemelését a táblán (double dispatch)
     * @param gp a játék panel ahol meg akarjuk jeleníteni az ütést
     */
    public void draw(GamePanel gp) {
        gp.drawCapture(this);
    }
    
}
