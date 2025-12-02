package com.checkers.model.game;

import java.util.List;
import java.util.Scanner;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.checkers.model.move.*;
import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;
import com.checkers.model.player.Player;
import com.checkers.model.*;

/**
 * A játék osztálya
 */
public class Game {
    private Board board;
    private Player playerToMove;
    private Player playerNotToMove;
    private List<Move> previousMoves;
    private HashMap<String, Integer> positionCounts;
    private int noPromotionCaptureCounter;
    private boolean inverted;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * A játék konstruktora
     * @param playerToMove a kezdő játékos
     * @param playerNotToMove a másik játékos
     */
    public Game(Player playerToMove, Player playerNotToMove) {
        this.playerToMove = playerToMove;
        this.playerNotToMove = playerNotToMove;
        initGame();
    }

    /**
     * @return az éppen következő játékos 
     */
    public Player getPlayerToMove() {
        return playerToMove;
    }

    /**
     * @return az éppen következő játékos után következő játékos
     */
    public Player getPlayerNotToMove() {
        return playerNotToMove;
    }

    /**
     * @return a játékhoz tartozó tábla
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return invertált-e a játék táblája
     */
    public boolean getInverted() {
        return inverted;
    }

    /**
     *Beállítja az invertáltságot
     * @param inverted az új invertált érték
     */
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    /**
     * @return nézet értesítéséhez szükséges PropertyChangeSupport
     */
    public PropertyChangeSupport getSupport() {
        return support;
    }
    
    /**
     * A nézet értesítéshez szükséges PropertyChangeListener ad hozzá a PropertyChangeSupport-hoz
     * @param pcl a PropertyChangeListener amit, hozzá akarunk adni
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * @return az éppen következő játékosnak van-e több lépése 
     */
    public boolean isGameOver() {
        return playerToMove.validMoves(this).isEmpty();
    }

    /**
     * @return az eredmény döntetlen-e
     */
    public boolean isDraw() {
        return isDrawRepetition() || isDrawNoPromotionCapture();
    }

    /**
     * Elkezd egy játékot
     */
    public void start() {
        playerToMove.beforeFirstTurn(this);
    }
    
    /**
     * A nézetet értesíti a győzelemről vagy a döntetlenről
     * @return valaki győzött-e vagy döntetlen-e az állás 
    */
   public boolean checkIsGameOverOrDraw() {
       if (isGameOver()) {
           getSupport().firePropertyChange("gameOver", null, null);
            return true;
        } else if (isDraw()) {
            getSupport().firePropertyChange("draw", null, null);
            return true;
        }
        return false;
    }
    
    /**
     * @return az eredmény döntetlen-e ismétlés miatt (ld. specifikáció)
    */
   public boolean isDrawRepetition() {
       return positionCounts.entrySet().stream().anyMatch(entry -> entry.getValue() >= 3);
    }
    
    /**
     * @return az eredmény döntetlen-e, mert nem volt 30 lépésig se dámává alakulás, se ütés
     */
    public boolean isDrawNoPromotionCapture() {
        return noPromotionCaptureCounter >= 30;
    }

    /**
     * A játék kezdetét állítja be
     */
    public void initGame() {
        this.board = new Board();
        this.board.initBoard();
        this.previousMoves = new ArrayList<>();
        this.positionCounts = new HashMap<>();
        this.noPromotionCaptureCounter = 0;
        if (playerToMove.getColour() != Colour.BLACK) {
            swapPlayers();
        }
        this.inverted = playerToMove.getStartInverted();
    }

    /**
     * Egy üres táblájú játék kezdetét állítja be
     */
    public void cleanInitGame() {
        this.board = new Board();
        this.previousMoves = new ArrayList<>();
        this.positionCounts = new HashMap<>();
        this.noPromotionCaptureCounter = 0;
        if (playerToMove.getColour() != Colour.BLACK) {
            swapPlayers();
        }
        this.inverted = playerToMove.getStartInverted();
    }

    /**
     * A játék kiírását végzi el
     * @param fileName a fájl neve, ahova ki akarjuk íratni a játékot
     * @throws IOException ha a fájl nem létezik
     */
    public void write(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
         
            for (int round = 0; round < previousMoves.size() / 2; round++) {
                bw.write((round + 1) + ". " + previousMoves.get(round * 2) + " " + previousMoves.get(round * 2 + 1));
                bw.newLine();
            }
            if (previousMoves.size() % 2 == 1) {
                bw.write((previousMoves.size() / 2 + 1) + ". " + previousMoves.getLast());
            }
        }
    }

    /**
     * A játékosok cserélyét végzi el
     */
    public void swapPlayers() {
        Player tempPlayer = playerToMove;
        playerToMove = playerNotToMove;
        playerNotToMove = tempPlayer;
    }

    /**
     * Egy lépés elvégzése után frissíti a játékállapotot 
     * @param move a lépés amit elvégeztünk
     */
    public void updateGameState(Move move) {
        if (move.isMandatory() || move.isPromotion()) {
            noPromotionCaptureCounter = 0;
            positionCounts.clear();
        } else {
            noPromotionCaptureCounter++;
        }
        String fen = board.getFen(playerToMove);
        positionCounts.merge(fen, 1, Integer::sum);
        previousMoves.add(move);
    }

    /**
     * A játék beolvasását végzi el
     * @param fileName 
     * @throws FileNotFoundException ha nem létetik ilyen fájl
     * @throws FileFormatException ha a fájl formátuma hibás
     * @throws InvalidMoveException ha a fájl olyan lépést olvas be, ami nem érvényes a szabályok szerint
    */
   public void read(String fileName) throws FileNotFoundException, FileFormatException, InvalidMoveException {
       initGame();
       File file = new File(fileName);
       int roundNumber = 0;
       try (Scanner sc = new Scanner(file)) {
           while (sc.hasNextLine()) {
               String line = sc.nextLine();
               line = line.trim();
               if (line.isEmpty() || line.charAt(0) == '[') {
                   continue;
                }
                roundNumber++;
                String[] roundAndMoveStrings = line.split(" ");

                try {
                    int readRoundNumber = Integer.parseInt(roundAndMoveStrings[0].replace(".", ""));
                    if (readRoundNumber != roundNumber) {
                        throw new FileFormatException("Cannot parse file. A round number is not correct!");
                    }
                } catch (NumberFormatException e) {
                    throw new FileFormatException("Cannot parse file. A round number is not correct!", e);
                }

                if (roundAndMoveStrings.length == 3) {

                    Move blackMove;
                    try {
                        blackMove = Move.moveFromString(roundAndMoveStrings[1], board);
                    } catch (IllegalArgumentException e) {
                        throw new FileFormatException("Cannot parse file. A move is not correct!", e);
                    }
                    if (playerToMove.validMovesAt(this, blackMove.getFrom()).stream().anyMatch(move -> move.equals(blackMove))) { 
                        blackMove.execute(board);
                        updateGameState(blackMove);
                        swapPlayers();
                    } else {
                        throw new InvalidMoveException("There is an invalid move in the file in the " + roundNumber + ". round!");
                    }

                    if (isGameOver() || isDraw()) {
                        break;
                    }

                    Move whiteMove;
                    try {
                        whiteMove = Move.moveFromString(roundAndMoveStrings[2], board);
                    } catch (IllegalArgumentException e) {
                        throw new FileFormatException("Cannot parse file. A move is not correct!", e);
                    }
                    if (playerToMove.validMoves(this).stream().anyMatch(move -> move.equals(whiteMove))) {
                        whiteMove.execute(board);
                        updateGameState(whiteMove);

                        swapPlayers();
                    } else {
                        throw new InvalidMoveException("There is an invalid move in the file in the " + roundNumber + ". round!");
                    }

                    if (isGameOver() || isDraw()) {
                        break;
                    }

                } else if (roundAndMoveStrings.length == 2) {
                    Move blackMove = Move.moveFromString(roundAndMoveStrings[1], board);
                    if (playerToMove.validMoves(this).stream().anyMatch(move -> move.equals(blackMove))) { 
                        blackMove.execute(board);
                        updateGameState(blackMove);
                        swapPlayers();
                    } else {
                        throw new InvalidMoveException("There is an invalid move in the file in the " + roundNumber + ". round!");
                    }
                    break;
                } else {
                    throw new FileFormatException("Cannot parse file!");
                }
            }
            if (sc.hasNextLine()) {
                throw new FileFormatException("Cannot parse file! There are more lines");
            }
        }
        support.firePropertyChange("boardChange", null, null); 
    }

    /**
     * Az ütés végrehajtását végzi el (double dispatch)
     * @param capture az ütés, amit végre akarunk hajtani
     */
    public void executeCapture(Capture capture) {
        capture.execute(board);
    }

    /**
     * Az ütéssorozat végrehajtását végzi el (double dispatch)
     * @param capture az ütéssorozat, amit végre akarunk hajtani
     */
    public void executeCaptureSequence(CaptureSequence captureSequence) {
        for (Capture capture : captureSequence.getCaptures()) {
            capture.execute(this);
            support.firePropertyChange("boardChange", null, null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * A normál lépés végrehajtását végzi el (double dispatch)
     * @param capture az egyszerű lépés, amit végre akarunk hajtani
     */
    public void executeNormalMove(NormalMove normalMove) {
        normalMove.execute(board);
    }
}
