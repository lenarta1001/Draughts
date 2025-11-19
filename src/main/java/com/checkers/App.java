package com.checkers;

import com.checkers.control.GameController;
import com.checkers.model.game.Game;
import com.checkers.view.BoardPanel;
import com.checkers.view.MainFrame;

public class App {
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(
            () -> {
                Game game = new Game();
                game.initGame();
                new MainFrame(new BoardPanel(game, new GameController(game)));
            }
        );
    }
}
