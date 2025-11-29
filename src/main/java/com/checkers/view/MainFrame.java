package com.checkers.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.checkers.control.GameController;
import com.checkers.control.MainWindowListener;
import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.player.ComputerPlayer;
import com.checkers.model.player.HumanPlayer;
import com.checkers.model.player.Player;

public class MainFrame extends JFrame {
    
    private transient Game game;
    private GamePanel currentBoardPanel;

    public MainFrame() {
        super("Checkers");
        
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new MainWindowListener(this));

        setLayout(new BorderLayout());
        setJMenuBar(createJMenuBar());

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(java.awt.Color.LIGHT_GRAY);
        add(emptyPanel, BorderLayout.CENTER);

        setSize(new Dimension(600, 600));
        setLocationRelativeTo(null);
    }

    private JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> showNewGameDialog());
        fileMenu.add(newGameItem);
        fileMenu.addSeparator();

        JMenuItem exportItem = new JMenuItem("Export");
        exportItem.addActionListener(e -> handleExport());
        fileMenu.add(exportItem);
        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> handleExit());
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Draughts v1.0."));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void showNewGameDialog() {
        if (game != null && !confirmAction("Start a new game? Unsaved progress will be lost.")) {
            return;
        }

        NewGameDialog dialog = new NewGameDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            if (dialog.isImport()) {
                handleImport(dialog.getSelectedFile());
            } else {
                initNewGame(dialog.isBlack(), dialog.isVsComputer());
            }
        }
    }

    private void initNewGame(boolean isHumanBlack, boolean isVsComputer) {
        Player p1;
        Player p2;

        if (isVsComputer) {
            if (isHumanBlack) {
                p1 = new HumanPlayer(Colour.black);
                p2 = new ComputerPlayer(Colour.white);
            } else {
                p1 = new ComputerPlayer(Colour.black);
                p2 = new HumanPlayer(Colour.white);
            }
        } else {
            p1 = new HumanPlayer(Colour.black);
            p2 = new HumanPlayer(Colour.white);
        }

        startGame(new Game(p1, p2));
    }

    private void handleImport(File file) {
        if (file == null || !file.exists()) {
             JOptionPane.showMessageDialog(this, "Invalid file selected.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        try {
            Game importedGame = new Game(new HumanPlayer(Colour.black), new HumanPlayer(Colour.white));
            importedGame.read(file.getAbsolutePath());
            startGame(importedGame);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startGame(Game newGame) {
        this.game = newGame;
        
        if (currentBoardPanel != null) {
            remove(currentBoardPanel);
        }

        if (getContentPane().getComponentCount() > 0) {
            remove(getContentPane().getComponent(0));
        }

        GameController controller = new GameController(game);
        currentBoardPanel = new GamePanel(game, controller);
        
        add(currentBoardPanel, BorderLayout.CENTER);

        game.start();
        revalidate();
        repaint();
    }

    public void handleExit() {
        if (game != null) {
            if (confirmAction("Do you really want to exit? Unsaved progress will be lost.")) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private boolean confirmAction(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirm", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    private void handleExport() {
        if (game == null) {
            JOptionPane.showMessageDialog(this, "No game to export!");
            return;
        }

        ExportDialog dialog = new ExportDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            File exportFile = dialog.getSelectedFile();

            if (exportFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(this, "File already exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            try {
                game.write(exportFile.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Game exported successfully to:\n" + exportFile.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
