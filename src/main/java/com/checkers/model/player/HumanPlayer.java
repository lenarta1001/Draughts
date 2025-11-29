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
            game.updateCounters(move);
            move.execute(game);
            game.updateFen(move);
            game.getSupport().firePropertyChange("boardChange", null, null);
        }
    }
    
    public void onOpponentTurnCompleted(Game game) { 
        game.swapPlayers();
        game.getSupport().firePropertyChange("boardChange", null, null);
    }

    public void firstTurn(Game game) {
        // Amíg nincs klikk nem tudunk semmit sem csinálni
    }
}
