package com.checkers.model.board;

import com.checkers.model.colour.Colour;
import com.checkers.model.piece.*;
import com.checkers.model.player.Player;

import java.awt.Point;

public class Board {
    private Piece[][] board;

    public Board() {
        board = new Piece[8][8];
    }

    public boolean isEmpty(Point p) { 
        return board[p.y][p.x] == null;
    }

    public static boolean isInsideBoard(Point p) {
        return p.x >= 0 && p.x < 8 && p.y >= 0 && p.y < 8;
    }

    public void setPiece(Piece piece, Point p) {
        board[p.y][p.x] = piece;
    }

    public Piece getPiece(Point p) {
        return board[p.y][p.x];
    }

    public static Point invertPoint(Point p) {
        Point invertedPoint = new Point();
        invertedPoint.x = 7 - (int)p.getX();
        invertedPoint.y = 7 - (int)p.getY();
        return invertedPoint;
    }

    public void initBoard() {
        for (int i = 1; i <= 32; i++) {
            Point p = pointFromSquareNumber(i);
            if (p.y <= 2) {
                setPiece(new Checker(Colour.white), p);
            } else if (p.y >= 5) {
                setPiece(new Checker(Colour.black), p);
            }
        }
    }
    
    public static int squareNumberFromPoint(Point p) throws IllegalArgumentException {
        if ((p.x + p.y) % 2 == 0) {
            throw new IllegalArgumentException("The position is invalid!");
        }
        int numOfBlackSquaresInLowerLines = (7 - p.y) * 4;
        int numOfBlackSquaresInThisLine = p.x / 2 + 1;
        return numOfBlackSquaresInLowerLines + numOfBlackSquaresInThisLine;
    }

    public static Point pointFromSquareNumber(int squareNumber) throws IllegalArgumentException {
        if (squareNumber > 32 || squareNumber < 1) {
            throw new IllegalArgumentException("The position square number invalid!");
        }
        Point p = new Point();
        int y = 7 - (squareNumber - 1) / 4;

        int x = (squareNumber - 1) % 4 * 2 + (y % 2 == 0 ? 1 : 0);
        p.setLocation(x, y);
        return p;
    }
    

    public String getFen(Player playerToMove) {
        StringBuilder fen = new StringBuilder();
        if (playerToMove.getColour() == Colour.black) {
            fen.append("B");
        } else {
            fen.append("W");
        }

        StringBuilder blacks = new StringBuilder();
        StringBuilder whites = new StringBuilder();
        blacks.append(":B");
        whites.append(":W");

        for (int i = 1; i <= 32; i++) {
            Point p = pointFromSquareNumber(i);

            if (!isEmpty(p)) {
                Piece piece = getPiece(p);
                if (piece.getColour() == Colour.black) {
                    if (blacks.length() != 2) {
                        blacks.append(",");
                    }
                    blacks.append(i);
                    blacks.append(piece.toString());
                } else {
                    if (whites.length() != 2) {
                        whites.append(",");
                    }
                    whites.append(i);
                    whites.append(piece.toString());
                }
            }
        }
        fen.append(whites.toString());
        fen.append(blacks.toString());
        return fen.toString();
    }
}
