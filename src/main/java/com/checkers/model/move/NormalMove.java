package com.checkers.model.move;

import java.awt.Point;

import com.checkers.model.board.*;
import com.checkers.model.piece.*;

public class NormalMove extends Move {
    public NormalMove(Point from, Point to, boolean onInvertedBoard) {
        super(from, to, onInvertedBoard);
    }

    public boolean isMandatory() {
        return false;
    }

    public void execute(Board table) {
        if (!isPromotion()) {
            table.setPiece(table.getPiece(from), to);
        } else {
            Colour colour = table.getPiece(from).getColour();
            table.setPiece(new King(colour), to);
        }
        table.setPiece(null, from);
    }
}
