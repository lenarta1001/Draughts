package com.checkers.model.move;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.checkers.model.board.*;
import com.checkers.model.piece.*;


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
    
    public boolean isPromotion(Board board) {
        return to.y == 0 && board.getPiece(from) instanceof Checker;
    }
    
    public Iterator<Move> iterator() {
        return List.of(this).iterator();
    }

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

            if (fromNumber > 32 || fromNumber < 1 || toNumber > 32 || fromNumber < 1) {
                throw new IllegalArgumentException();
            }
            boolean onInvertedBoard = board.isInvertedBoard();
            Point from = onInvertedBoard ? Board.invertPoint(Board.pointFromSquareNumber(fromNumber)) : Board.pointFromSquareNumber(fromNumber);
            Point to = onInvertedBoard ? Board.invertPoint(Board.pointFromSquareNumber(toNumber)) : Board.pointFromSquareNumber(toNumber);

            move = new NormalMove(from, to, onInvertedBoard);

        } else if (moveString.contains("x")) {

            String[] postions = moveString.split("x");
            boolean onInvertedBoard = board.isInvertedBoard();
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

                if (fromNumber > 32 || fromNumber < 1 || toNumber > 32 || fromNumber < 1) {
                    throw new IllegalArgumentException();
                }

                Point from = onInvertedBoard ? Board.invertPoint(Board.pointFromSquareNumber(fromNumber)) : Board.pointFromSquareNumber(fromNumber);
                Point to = onInvertedBoard ? Board.invertPoint(Board.pointFromSquareNumber(toNumber)) : Board.pointFromSquareNumber(toNumber);

                captures.add(new Capture(from, to, onInvertedBoard));

            }

            if (captures.size() == 1) {
                move = captures.getFirst();
            } else {
                move = new CaptureSequence(captures, onInvertedBoard);
            }
        } else {
            throw new IllegalArgumentException();
        }

        return move;
    }
    
    public abstract void execute(Board board);
    public abstract boolean isMandatory();
    public abstract String toString();
    public abstract boolean equals(Object o);

}
