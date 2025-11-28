package com.checkers.model.move;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.checkers.model.board.*;
import com.checkers.model.game.Game;
import com.checkers.model.piece.*;
import com.checkers.view.GamePanel;


public abstract class Move {
    protected Point from;
    protected Point to;

    protected Move(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public Point getFrom() {
        return from;
    } 
    
    public Point getTo() {
        return to;
    }
    
    public boolean isPromotion(Board board) {
        return to.y == 0 && board.getPiece(from) instanceof Checker;
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

            if (fromNumber > 32 || fromNumber < 1 || toNumber > 32 || toNumber < 1) {
                throw new IllegalArgumentException();
            }
            Point from = Board.pointFromSquareNumber(fromNumber);
            Point to = Board.pointFromSquareNumber(toNumber);

            move = new NormalMove(from, to);

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

                captures.add(new Capture(from, to));

            }

            if (captures.size() == 1) {
                move = captures.getFirst();
            } else {
                move = new CaptureSequence(captures);
            }
        } else {
            throw new IllegalArgumentException();
        }

        return move;
    }
    
    public abstract void execute(Game game);
    public abstract boolean isMandatory();
    public abstract String toString();
    public abstract boolean equals(Object o);
    public abstract void draw(GamePanel gp);

}
