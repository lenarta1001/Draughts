package com.checkers.model.piece;

import com.checkers.model.move.*;
import com.checkers.view.GamePanel;
import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A dáma bábu osztálya
 */
public class King extends Piece {
    /**
     * Az dáma konstruktora
     * @param colour a bábu színe
     */
    public King(Colour colour) {
        super(colour);
    }

    /**
     * Egy tábla adott pontjáról elvégezhető ütések a dámával
     * @param board a tábla, amin vizsgáljuk az ütéseket
     * @param point a pont, ahonnan az ütéseket el akarjuk végezni
     * @param previosCaptures az előzőleg előállított ütéssorozat, az ütéssorozatok rekurzív előállításához szükséges paraméter
     * @return az elvégezhető ütések
     */
    public List<Capture> validCaptures(Board board, Point point, List<Capture> previosCaptures, Point previousDirection) {
        List<Capture> captures = new ArrayList<>();

        List<Point> diagonalDirections = new ArrayList<>(List.of(new Point(1, -1), new Point(-1, -1), new Point(1, 1), new Point(-1, 1)));
        diagonalDirections.removeIf(p -> p.equals(new Point(-1 * previousDirection.x, -1 * previousDirection.y)));

        for (Point direction : diagonalDirections) {    
            Point jumped = new Point(point.x + direction.x, point.y + direction.y);
            Point to = new Point(point.x + 2 * direction.x, point.y + 2 * direction.y);
            boolean canCapture = Board.isInsideBoard(jumped) && !board.isEmpty(jumped)
                            && Board.isInsideBoard(to) && board.isEmpty(to)
                            && board.getPiece(jumped).getColour() != this.getColour();

            if (canCapture) {
                List<Capture> newPreviousCaptures = new ArrayList<>();
                newPreviousCaptures.addAll(previosCaptures);
                newPreviousCaptures.add(new Capture(point, to, board));
                captures.addAll(validCaptures(board, to, newPreviousCaptures, direction));
            }
        }
                        
        if (captures.isEmpty()) {
            if (previosCaptures.size() > 1) {
                captures.add(new CaptureSequence(previosCaptures, board));
            } else if (previosCaptures.size() == 1) {
                captures.add(previosCaptures.getFirst());
            }
        }

        return captures;
    }

    /**
     * Egy tábla adott pontjáról elvégezhető egyszerű lépések a dámával
     * @param board a tábla, amin vizsgáljuk az egyszerű lépéseket
     * @param point a pont, ahonnan az egyszerű lépeseket el akarjuk végezni
     * @return az elvégezhető egyszerű lépések
     */
    public List<Move> validNormalMoves(Board board, Point point) {
        List<Move> moves = new ArrayList<>();

        List<Point> diagonalDirections = List.of(new Point(1, -1), new Point(-1, -1), new Point(1, 1), new Point(-1, 1));
        for (Point direction : diagonalDirections) {    
            Point to = new Point(point.x + direction.x, point.y + direction.y);
            
            if (Board.isInsideBoard(to) && board.isEmpty(to)) {
                moves.add(new NormalMove(point, to, board));
            }
        }
        return moves;
    }

    /**
     * Egy tábla adott pontjáról elvégezhető összes lépés a dámával
     * @param board a tábla, amin vizsgáljuk a lépéseket
     * @param point a pont, ahonnan a lépeseket el akarjuk végezni
     * @return az elvégezhető összes lépés
     */
    public List<Move> validMoves(Board board, Point point) {
        List<Move> normalMoves = validNormalMoves(board, point);
        List<Capture> captures = validCaptures(board, point, new ArrayList<>(), new Point(0, 0));

        List<Move> moves = new ArrayList<>();
        if (captures.isEmpty()) {
            moves.addAll(normalMoves);
        }
        moves.addAll(captures);


        return moves;
    }

    /**
     * Karakterlánccá alakítja a királyt (ld. specifikáció FEN)
     * @return a király krakterlánc formában
     */
    @Override
    public String toString() {
        return "K";
    }

    /**
     * Kirajzolja a játékpanelre a királyt (double dispatch)
     * @param gp a játék panel, amire ki akarjuk rajzolni a királyt
     * @param x a kirajzolás helyének x koordinátája
     * @param y a kirajzolás helyének y koordinátája
     */
    public void draw(GamePanel gp, int x, int y) {
        gp.drawKing(this, x, y);
    }
    
}
