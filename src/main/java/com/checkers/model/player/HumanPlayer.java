package com.checkers.model.player;

import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.move.Move;

public class HumanPlayer extends Player {

    public HumanPlayer(Colour colour) {
        super(colour);
    }

    public void handleTurn(Game game, Move move) {
        if (this == game.getPlayerToMove()) {
            move.execute(game);
            game.finalizeMoveState(move);
            game.swapPlayers();
        }
    }

    public void onOpponentTurnCompleted(Game game) { 
        // A HumanPlayer az ellenfele köre után közvetlenül nem csinál semmit
    }
}
