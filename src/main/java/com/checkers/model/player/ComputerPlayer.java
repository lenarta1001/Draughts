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
        game.swapPlayers(); // Az ütéssorozat megjelenítésénél ne forduljon meg a tábla
        game.updateCounters(move);
        move.execute(game);
        game.updateFenAndMoves(move);
    }
    
    public void onOpponentTurnCompleted(Game game) {
        game.swapPlayers();
        game.checkIsGameOverOrDraw();
        List<Move> moves = validMoves(game);
        if (moves.isEmpty()) {
            return;
        }
        Move chosen = moves.get(new Random().nextInt(moves.size()));
        handleTurn(game, chosen);
        game.getSupport().firePropertyChange("boardChange", null, null);
        game.checkIsGameOverOrDraw();
    }

    public void firstTurn(Game game) {
        game.swapPlayers();
        game.getSupport().firePropertyChange("boardChange", null, null);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                onOpponentTurnCompleted(game);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}   
