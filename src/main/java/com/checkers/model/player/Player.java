package com.checkers.model.player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.move.Move;

/**
 * A játékos abstract osztálya
 */
public abstract class Player {
    private Colour colour;

    /**
     * A játékos konstruktora
     * @param colour a játékos színe
     */
    protected Player(Colour colour) {
        this.colour = colour;
    }

    /**
     * @return a játékos színe
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * A játékos összes érvényes lépése a játék egy adott állapotában
     * @param game a játék amiben a játékos játszik
     * @return a játékos érvényes lépései
     */
    public List<Move> validMoves(Game game) {
        List<Move> validMoves = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            Point p = Board.pointFromSquareNumber(i);
            Board board = game.getBoard();
            if (!board.isEmpty(p) && board.getPiece(p).getColour() == colour) {      
                validMoves.addAll(board.getPiece(p).validMoves(board, p));
            }
        }
        if (validMoves.stream().anyMatch(Move::isMandatory)) {
            validMoves = validMoves.stream().filter(Move::isMandatory).toList();
        }
        return validMoves;
    }

    /**
     * A játékos adott pontról elvégezhető érvényes lépései a játék egy adott állapotában
     * @param game a játék, amiben a játékos játszik
     * @param point a pont, amelyről az elvégezhető lépéseket vizsgáljuk
     * @return a játékos adott pontról elvégezhető érvényes lépései
     */
    public List<Move> validMovesAt(Game game, Point point) {
        Board board = game.getBoard();
        if (!Board.isInsideBoard(point) || board.isEmpty(point) || board.getPiece(point).getColour() != colour) {
            return new ArrayList<>();
        }
        List<Move> validMoves = validMoves(game); 
        List<Move> potentialMoves = board.getPiece(point).validMoves(board, point);
        return potentialMoves.stream().filter(validMoves::contains).toList();
    }

    /**
     * A játékos egy lépését végzi el 
     * @param game a játék, amiben a játékos játszik
     * @param move a lépés amit a játékos választott
     */
    public abstract void handleTurn(Game game, Move move);

    /**
     * A játékos ellenfele lépése utáni teendőket végzi el
     * @param game a játék, amiben a játékos játszik
     */
    public abstract void onOpponentTurnCompleted(Game game);

    /**
     * A kezdő állapotban invertált legyen-e a játék táblája
     */
    public abstract boolean getStartInverted();

    /**
     * A játékos első lépése előtti teendőket végzi el
     * @param game a játék, amiben a játékos játszik
     */
    public abstract void beforeFirstTurn(Game game);
}