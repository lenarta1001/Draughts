package com.checkers.model.move;

import java.util.List;

import com.checkers.model.board.*;
import com.checkers.model.game.Game;
import com.checkers.view.GamePanel;

/**
 * Az ütéssorozat osztálya
 */
public class CaptureSequence extends Capture {
    private List<Capture> captures;

    /**
     * Az ütéssorozat konstruktora
     * @param captures az ütéssorozat ütései
     */
    public CaptureSequence(List<Capture> captures, Board board) {
        super(captures.getFirst().from, captures.getLast().to, board);
        captures.getLast().promotion = promotion;
        this.captures = captures;
    }

    /**
     * @return az ütéssorozat ütései
     */
    public List<Capture> getCaptures() {
        return captures;
    }

    
    /**
     * Az karakterlánccá konvertálja az ütéssorozatot (ld. specifikáció Portable Draughts Notation)
     * @return az ütéssorozat karakterlánc formátumban
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Capture capture : captures) {
            sb.append(Board.squareNumberFromPoint(capture.from));
            sb.append("x");
        }
        sb.append(Board.squareNumberFromPoint(captures.getLast().to));
        return sb.toString();
    }

    /**
     * Elvégzi az ütéssorozatot a játék tábláján
     * @param game a játék aminek a tábláján az ütést végezzük
     */
    @Override
    public void execute(Game game) {
        game.executeCaptureSequence(this);
    }

    /**
     * Elvégzi az ütést lépést a táblán
     * @param board a tábla, amin az ütést végezzük
     */
    @Override
    public void execute(Board board) {
        for (Capture capture : captures) {
            capture.execute(board);
        }
    }

    /**
     * Eldönti, hogy egy másik objektummal tartalmilag azonos-e az ütéssorozat
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

        CaptureSequence other = (CaptureSequence) o;
        return captures.equals(other.captures);
    }

    /** 
     * Kirajzolja az ütéssorozat kiemelését a táblán (double dispatch)
     * @param gp a játék panel, ahol meg akarjuk jeleníteni az ütéssorozatot
     */
    @Override
    public void draw(GamePanel gp) {
        gp.drawCaptureSequence(this);
    }
}
