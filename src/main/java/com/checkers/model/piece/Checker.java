package com.checkers.model.piece;


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.checkers.model.move.*;
import com.checkers.view.GamePanel;
import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;


/**
 * Az egyszerű bábu osztálya
 */
public class Checker extends Piece {
    /**
     * Az egyszerű bábu konstruktora
     * @param colour a bábu színe
     */
    public Checker(Colour colour) {
        super(colour);
    }

    /**
     * Egy tábla adott pontjáról elvégezhető ütések az egyszerű bábuval
     * @param board a tábla, amin vizsgáljuk az ütéseket
     * @param point a pont, ahonnan az ütéseket el akarjuk végezni
     * @param previosCaptures az előzőleg előállított ütéssorozat, az ütéssorozatok rekurzív előállításához szükséges paraméter
     * @return az elvégezhető ütések
     */
    private List<Capture> validCaptures(Board board, Point point, List<Capture> previosCaptures) {
        List<Capture> captures = new ArrayList<>();

        List<Point> diagonalDirections = List.of(new Point(direction.x + 1, direction.y), new Point(direction.x - 1, direction.y));
        for (Point direction : diagonalDirections) {    
            Point jumped = new Point(point.x + direction.x, point.y + direction.y);
            Point to = new Point(point.x + 2 * direction.x, point.y + 2 * direction.y);
            boolean canCapture = Board.isInsideBoard(jumped) && !board.isEmpty(jumped)
                            && Board.isInsideBoard(to) && board.isEmpty(to)
                            && board.getPiece(jumped).getColour() != this.getColour();

            if (canCapture) {
                List<Capture> newPreviousCaptures = new ArrayList<>();
                newPreviousCaptures.addAll(previosCaptures);
                newPreviousCaptures.add(new Capture(point, to));
                captures.addAll(validCaptures(board, to, newPreviousCaptures));
            }
        }
                        
        if (captures.isEmpty()) {
            if (previosCaptures.size() > 1) {
                captures.add(new CaptureSequence(previosCaptures));
            } else if (previosCaptures.size() == 1) {
                captures.add(previosCaptures.getFirst());
            }
        }

        return captures;
    }

    /**
     * Egy tábla adott pontjáról elvégezhető egyszerű lépések az egyszerű bábuval
     * @param board a tábla, amin vizsgáljuk az egyszerű lépéseket
     * @param point a pont, ahonnan az egyszerű lépeseket el akarjuk végezni
     * @return az elvégezhető egyszerű lépések
     */
    private List<Move> validNormalMoves(Board board, Point point) {
        List<Move> moves = new ArrayList<>();

        List<Point> diagonalDirections = List.of(new Point(direction.x + 1, direction.y), new Point(direction.x - 1, direction.y));
        for (Point direction : diagonalDirections) {    
            Point to = new Point(point.x + direction.x, point.y + direction.y);
            
            if (Board.isInsideBoard(to) && board.isEmpty(to)) {
                moves.add(new NormalMove(point, to));
            }
        }
        return moves;
    }

    /**
     * Egy tábla adott pontjáról elvégezhető összes lépés az egyszerű bábuval
     * @param board a tábla, amin vizsgáljuk a lépéseket
     * @param point a pont, ahonnan a lépeseket el akarjuk végezni
     * @return az elvégezhető összes lépés
     */
    public List<Move> validMoves(Board board, Point point) {
        List<Move> normalMoves = validNormalMoves(board, point);
        List<Capture> captures = validCaptures(board, point, new ArrayList<>());

        List<Move> moves = new ArrayList<>();
        if (captures.isEmpty()) {
            moves.addAll(normalMoves);
        }
        moves.addAll(captures);

        return moves;
    }

    public void draw(GamePanel gp, int x, int y) {
        gp.drawChecker(this, x, y);
    }
}
