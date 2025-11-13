package com.checkers.model.move;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import com.checkers.model.board.*;


public abstract class Move {
    protected Point from;
    protected Point to;
    protected boolean onInvertedBoard;

    public Move(Point from, Point to, boolean onInvertedBoard) {
        this.from = from;
        this.to = to;
        this.onInvertedBoard = onInvertedBoard;
    }

    public Point getFrom() {
        return from;
    } 
    
    public Point getTo() {
        return to;
    }
    
    public boolean getOnInvertedBoard() {
        return onInvertedBoard;
    }
    
    public boolean isPromotion() {
        return to.y == 0;
    }
    
    public Iterator<Move> iterator() {
        return List.of(this).iterator();
    }
    
    public abstract void execute(Board board);
    public abstract boolean isMandatory();
    public abstract String toString();

}
