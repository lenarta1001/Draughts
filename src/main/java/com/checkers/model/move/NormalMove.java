package com.checkers.model.move;

import java.awt.Point;

import com.checkers.model.board.*;
import com.checkers.model.piece.*;

public class NormalMove extends Move {
    public NormalMove(Point from, Point to, boolean onInvertedBoard) {
        super(from, to, onInvertedBoard);
    }

    public String toString() {
        Point absoluteFrom = onInvertedBoard ? Board.invertPoint(from) : from;
        Point absoluteTo = onInvertedBoard ? Board.invertPoint(to) : to;
        return Board.squareNumberFromPoint(absoluteFrom) + "-" + Board.squareNumberFromPoint(absoluteTo);
    }

    public boolean isMandatory() {
        return false;
    }

    public void execute(Board board) {
        if (!isPromotion(board)) {
            board.setPiece(board.getPiece(from), to);
        } else {
            Colour colour = board.getPiece(from).getColour();
            board.setPiece(new King(colour), to);
        }
        board.setPiece(null, from);
    }

    public boolean equals(Object o) {
        if (this == o) { 
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NormalMove other = (NormalMove) o;
        return from.equals(other.from) && to.equals(other.to) && onInvertedBoard == other.onInvertedBoard;
    }
}
