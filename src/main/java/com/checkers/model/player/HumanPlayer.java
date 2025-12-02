package com.checkers.model.player;

import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.move.Move;

/**
 * Az ember játékos osztálya
 */
public class HumanPlayer extends Player {

    /**
     * Az ember játékos konstruktora
     * @param colour az ember játékos színe
     */
    public HumanPlayer(Colour colour) {
        super(colour);
    }

    /**
     * Az ember játékos egy lépését végzi el 
     * @param game a játék, amiben az ember játékos játszik
     * @param move a lépés amit az ember játékos választott
     */
    public void handleTurn(Game game, Move move) {
        move.execute(game);
        game.updateGameState(move);
        game.getSupport().firePropertyChange("boardChange", null, null);
    }
    
    /**
     * Az ember játékos ellenfele lépése utáni teendőket végzi el
     * @param game a játék, amiben az ember játékos játszik
     */
    public void onOpponentTurnCompleted(Game game) {
        game.swapPlayers();
        if (!game.checkIsGameOverOrDraw()) {
             game.setInverted(!game.getInverted());
             game.getSupport().firePropertyChange("boardChange", null, null);
        }
    }

    /**
     * A kezdő állapotban invertált legyen-e a játék táblája
     */
    public boolean getStartInverted() {
        return false;
    }

    /**
     * A játékos első lépése előtti teendőket végzi el
     * @param game a játék, amiben a játékos játszik
     */
    public void beforeFirstTurn(Game game) {
        // Első lépés előtt nem tudunk semmit csinálni csak várni a klikkelésig
    }    
}
