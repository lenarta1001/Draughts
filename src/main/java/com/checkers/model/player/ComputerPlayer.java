package com.checkers.model.player;


import java.util.List;
import java.util.Random;

import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.move.Move;

public class ComputerPlayer extends Player {

    public ComputerPlayer(Colour colour) {
        super(colour);
    }

    public void handleTurn(Game game, Move move) {
        if (this == game.getPlayerToMove()) {
            move.execute(game);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            game.finalizeMoveState(move);
            game.swapPlayers();
            game.getBoard().invert();
        }
    }

    public void onOpponentTurnCompleted(Game game) { 
        List<Move> moves = validMoves(game);
        Move chosen = moves.get(new Random().nextInt(moves.size()));
        handleTurn(game, chosen);
    }
}
