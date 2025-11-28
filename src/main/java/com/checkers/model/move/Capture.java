package com.checkers.model.move;

import java.awt.Point;

import com.checkers.model.board.*;
import com.checkers.model.game.Game;
import com.checkers.view.GamePanel;

public class Capture extends Move {
    public Capture(Point from, Point to) {
        super(from, to);
    }

    public String toString() {
        return Board.squareNumberFromPoint(from) + "x" + Board.squareNumberFromPoint(to);
    }

    public boolean isMandatory() {
        return true;
    }
    
    public void execute(Game game) {
        game.executeCapture(this);
    }

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

    public void draw(GamePanel gp) {
        gp.drawCapture(this);
    }
    
}
