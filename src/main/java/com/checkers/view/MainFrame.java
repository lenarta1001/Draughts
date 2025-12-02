package com.checkers.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
import com.checkers.model.game.ImportSwingWorker;
import com.checkers.model.player.ComputerPlayer;
import com.checkers.model.player.HumanPlayer;
import com.checkers.model.player.Player;

/**
 * A főablak osztálya
 */
public class MainFrame extends JFrame {
    
    private transient Game game;
    private GamePanel currentBoardPanel;

    /**
     * A fő ablak konstruktora
     */
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

    /**
     * A menü bar-t létrehozó metódus
     * @return a menübar
     */
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

    /**
     * Az új játék dialógust megeleníti és elindít egy új játékot, a dialógustól származó imformációk alapján
     */
    private void showNewGameDialog() {
        if (game != null && !confirmAction("Start a new game? Unsaved progress will be lost.")) {
            return;
        }

        NewGameDialog dialog = new NewGameDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            if (dialog.isImport()) {
                handleImport(dialog.getSelectedFile(), dialog.isBlack(), dialog.isVsComputer());
            } else {
                initNewGame(dialog.isBlack(), dialog.isVsComputer());
            }
        }
    }

    /**
     * Új játékot indít el az új játék dialógusban kiválasztott opciók alapján
     * @param isHumanBlack az ember játékos a fekete bábukkal játszik-e
     * @param isVsComputer PvE játékmódú játékot indítunk-e
     */
    private void initNewGame(boolean isHumanBlack, boolean isVsComputer) {
        List<Player> players = createPlayers(isHumanBlack, isVsComputer);
        startGame(new Game(players.get(0), players.get(1)));
    }

    /**
     * A játékhoz szükséges játékosokat létrehozását végzi el
     * @param isHumanBlack az ember játékos a fekete bábukkal játszik-e
     * @param isVsComputer PvE játékmódú játékot indítunk-e
     * @return a két játékos listája
     */
    private List<Player> createPlayers(boolean isHumanBlack, boolean isVsComputer) {
        Player p1;
        Player p2;
        if (isVsComputer) {
            if (isHumanBlack) {
                p1 = new HumanPlayer(Colour.BLACK);
                p2 = new ComputerPlayer(Colour.WHITE);
            } else {
                p1 = new ComputerPlayer(Colour.BLACK);
                p2 = new HumanPlayer(Colour.WHITE);
            }
        } else {
            p1 = new HumanPlayer(Colour.BLACK);
            p2 = new HumanPlayer(Colour.WHITE);
        }
        return List.of(p1, p2);
    }

    /**
     * Az importálást végzi el
     * @param file a fájl amiből importálunk
     * @param isHumanBlack az ember játékos a fekete bábukkal játszik-e
     * @param isVsComputer PvE játékmódú játékot indítunk-e
     */
    private void handleImport(File file, boolean isHumanBlack, boolean isVsComputer) {
        if (file == null || !file.exists()) {
             JOptionPane.showMessageDialog(this, "Invalid file selected.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        LoadingDialog loadingDialog = new LoadingDialog(this);

        List<Player> players = createPlayers(isHumanBlack, isVsComputer);
        ImportSwingWorker worker = new ImportSwingWorker(this, loadingDialog, players, file);

        worker.execute();
        loadingDialog.setVisible(true);
    }

    /**
     * Leszedi a korábban megjelenített játék panelt és megjeleníti az újat
     * @param newGame az új játék
     */
    public void startGame(Game newGame) {
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

    /**
     * A kilépés lebonyolítását végzi el
     */
    public void handleExit() {
        if (game != null) {
            if (confirmAction("Do you really want to exit? Unsaved progress will be lost.")) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    /**
     * Megjelenít egy megerősítő dialógust 
     * @param message az üzenet, amit meg akarunk jeleníteni
     * @return megerősítette-e a felhasználó
     */
    private boolean confirmAction(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirm", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Az export dialógust megeleníti és exportálja a játékot, a dialógustól származó imformációk alapján
     */
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
