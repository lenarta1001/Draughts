package com.checkers.model.game;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.checkers.model.player.Player;
import com.checkers.view.LoadingDialog;
import com.checkers.view.MainFrame;

public class ImportSwingWorker extends SwingWorker<Game, Void> {
    private MainFrame mainFrame;
    private LoadingDialog loadingDialog;
    private List<Player> players;
    private File file;

    public ImportSwingWorker(MainFrame mainFrame, LoadingDialog loadingDialog, List<Player> players, File file) {
        this.mainFrame = mainFrame;
        this.loadingDialog = loadingDialog;
        this.players = players;
        this.file = file;
    }

    @Override
    protected Game doInBackground() throws Exception {
        Game game = new Game(players.get(0), players.get(1));
        game.read(file.getAbsolutePath());
        return game;
    }

    @Override
    protected void done() {
        loadingDialog.dispose();
        try {
            Game game = get();
            mainFrame.startGame(game);
        }  catch (ExecutionException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error unexpected interrupt", "Error", JOptionPane.ERROR_MESSAGE);
            Thread.currentThread().interrupt();
        }
    }
    
}
