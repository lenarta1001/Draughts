package com.checkers.model.player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.move.Move;

public abstract class Player {
    private Colour colour;

    protected Player(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

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

    public List<Move> validMovesAt(Game game, Point point) {
        Board board = game.getBoard();
        if (!Board.isInsideBoard(point) || board.isEmpty(point) || board.getPiece(point).getColour() != colour) {
            return new ArrayList<>();
        }
        List<Move> validMoves = validMoves(game); 
        List<Move> potentialMoves = board.getPiece(point).validMoves(board, point);
        return potentialMoves.stream().filter(validMoves::contains).toList();
    }

    public abstract void handleTurn(Game game, Move move);
    public abstract void onOpponentTurnCompleted(Game game);
}