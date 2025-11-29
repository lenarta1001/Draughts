package com.checkers.model.player;

import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.move.Move;

public class HumanPlayer extends Player {

    public HumanPlayer(Colour colour) {
        super(colour);
    }

    public void handleTurn(Game game, Move move) {
        game.updateCounters(move);
        move.execute(game);
        game.updateFenAndMoves(move);
        game.getSupport().firePropertyChange("boardChange", null, null);
    }
    
    public void onOpponentTurnCompleted(Game game) { 
        game.swapPlayers();
        game.getSupport().firePropertyChange("boardChange", null, null);
        game.checkIsGameOverOrDraw();
    }

    public void firstTurn(Game game) {
        game.getSupport().firePropertyChange("boardChange", null, null);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                game.checkIsGameOverOrDraw();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
