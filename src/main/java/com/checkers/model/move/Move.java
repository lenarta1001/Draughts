package com.checkers.model.move;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.checkers.model.board.*;
import com.checkers.model.game.Game;
import com.checkers.model.piece.*;
import com.checkers.view.GamePanel;

/**
 * A lépés abstract osztálya
 */
public abstract class Move {
    protected Point from;
    protected Point to;
    protected boolean promotion;

    /**
     * A lépés konstruktora
     * @param from a kezdő pozíció
     * @param to a végpozíció
     */
    protected Move(Point from, Point to, Board board) {
        this.from = from;
        this.to = to;
        this.promotion = (to.y == 0 || to.y == 7) && board.getPiece(from) instanceof Checker;
    }

    /**
     * @return kezdőpozíció
     */
    public Point getFrom() {
        return from;
    } 
    
    /**
     * @return a végpozíció
     */
    public Point getTo() {
        return to;
    }
    
    /**
     * Eldönti, hogy a lépés dámává való átalakulás-e
     * @param board a tábla 
     * @return a lépés dámává való átalakulás-e
     */
    public boolean isPromotion() {
        return promotion;
    }

    /**
     * Egy karakterláncból egy lépést hoz létre (ld. specifikáció Portable Draughts Notation)
     * @param moveString a karakterlánc, amiből egy lépést akarunk létrehozni
     * @return a karakterlánchoz tartozó lépés
     * @throws IllegalArgumentException ha a karakterlánc érvénytelen
     */
    public static Move moveFromString(String moveString, Board board) throws IllegalArgumentException {
        Move move;

        if (moveString.contains("-")) {

            String[] postions = moveString.split("-");

            if (postions.length != 2) {
                throw new IllegalArgumentException();
            }

            int fromNumber;
            int toNumber;
            try {
                fromNumber = Integer.parseInt(postions[0]);
                toNumber = Integer.parseInt(postions[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }

            if (fromNumber > 32 || fromNumber < 1 || toNumber > 32 || toNumber < 1) {
                throw new IllegalArgumentException();
            }
            Point from = Board.pointFromSquareNumber(fromNumber);
            Point to = Board.pointFromSquareNumber(toNumber);

            move = new NormalMove(from, to, board);

        } else if (moveString.contains("x")) {

            String[] postions = moveString.split("x");
            List<Capture> captures = new ArrayList<>();

            for (int i = 0; i < postions.length - 1; i++) {

                int fromNumber;
                int toNumber;
                try {
                    fromNumber = Integer.parseInt(postions[i]);
                    toNumber = Integer.parseInt(postions[i + 1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(e);
                }

                if (fromNumber > 32 || fromNumber < 1 || toNumber > 32 || toNumber < 1) {
                    throw new IllegalArgumentException();
                }

                Point from = Board.pointFromSquareNumber(fromNumber);
                Point to = Board.pointFromSquareNumber(toNumber);

                captures.add(new Capture(from, to, board));

            }

            if (captures.size() == 1) {
                move = captures.getFirst();
            } else {
                move = new CaptureSequence(captures, board);
            }
        } else {
            throw new IllegalArgumentException();
        }

        return move;
    }
    
    /**
     * Elvégzi a normál lépést a játék tábláján
     * @param game a játék aminek a tábláján a lépést végezzük
     */
    public abstract void execute(Game game);

    /**
     * Elvégzi a lépést a táblán, itt nincs késleltetés ütéssorozat esetén
     * @param board a tábla, amin a lépést végezzük
     */
    public abstract void execute(Board board);

    /**
     * @return a lépést kötelező-e elvégezni
     */
    public abstract boolean isMandatory();

    /**
     * A lépés karakterlánccá konvertálja (ld. specifikáció Portable Draughts Notation)
     * @return a lépés karakterlánc formátumban
     */
    public abstract String toString();

    /**
     * Eldönti, hogy egy másik objektummal tartalmilag azonos-e a lépés
     * @param o az objektum, amivel összehasonlítjuk
     * @return tartalmilag azonos-e az objektummal
     */
    public abstract boolean equals(Object o);

    /** 
     * Kirajzolja a lépés kiemelését a táblán 
     * @param gp a játék panel ahol meg akarjuk jeleníteni a lépést
     */
    public abstract void draw(GamePanel gp);
}
