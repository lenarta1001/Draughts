package com.checkers.model.game;

import java.util.List;
import java.util.Scanner;
import java.awt.Point;
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
import com.checkers.model.piece.*;
import com.checkers.model.player.HumanPlayer;
import com.checkers.model.player.Player;
import com.checkers.model.*;

public class Game {
    private Board board;
    private Player playerToMove;
    private Player playerNotToMove;
    private List<Move> previousMoves;
    private HashMap<String, Integer> positionCounts;
    private int noPromotionCaptureCounter;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public Game(Player playerToMove, Player playerNotToMove) {
        this.playerToMove = playerToMove;
        this.playerNotToMove = playerNotToMove; 
    }

    public Game() {
        this(new HumanPlayer(Colour.black), new HumanPlayer(Colour.white));
    }

    public Player getPlayerToMove() {
        return playerToMove;
    }

    public Player getPlayerNotToMove() {
        return playerNotToMove;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isGameOver() {
        return playerToMove.validMoves(this).isEmpty();
    }

    public boolean isDraw() {
        boolean repetition = positionCounts.entrySet().stream().anyMatch(entry -> entry.getValue() >= 3);
        return repetition || noPromotionCaptureCounter >= 30;
    }

    
    public void initGame() {
        this.board = new Board();
        this.board.initBoard();
        this.previousMoves = new ArrayList<>();
        this.positionCounts = new HashMap<>();
        this.noPromotionCaptureCounter = 0;
        if (playerToMove.getColour() != Colour.black) {
            swapPlayers();
        }
    }

    public void cleanInitGame() {
        this.board = new Board();
        this.previousMoves = new ArrayList<>();
        this.positionCounts = new HashMap<>();
        this.noPromotionCaptureCounter = 0;
        if (playerToMove.getColour() != Colour.black) {
            swapPlayers();
        }
    }

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

    public void swapPlayers() {
        Player tempPlayer = playerToMove;
        playerToMove = playerNotToMove;
        playerNotToMove = tempPlayer;
    }

    public void finalizeMoveState(Move move) {
        if (move.isMandatory() || move.isPromotion(board)) {
            noPromotionCaptureCounter = 0;
            positionCounts.clear();
        } else {
            noPromotionCaptureCounter++;
        }
        String fen = board.getFen(playerToMove);
        positionCounts.merge(fen, 1, Integer::sum);
        previousMoves.add(move);
        support.firePropertyChange("boardChange", null, null);
    }
    
    
    public void read(String fileName) throws FileNotFoundException, FileFormatException, InvalidMoveException {
        initGame();
        File file = new File(fileName);
        int roundNumber = 1;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                line = line.trim();
                if (line.isEmpty() || line.charAt(0) == '[') {
                    continue;
                }
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
                    if (playerToMove.validMoves(this).stream().anyMatch(move -> move.equals(blackMove))) { 
                        playerToMove.handleTurn(this, blackMove);
                    } else {
                        throw new InvalidMoveException("There is an invalid move in the file according to the rules!");
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
                        playerToMove.handleTurn(this, whiteMove);
                    } else {
                        throw new InvalidMoveException("There is an invalid move in the file according to the rules!");
                    }

                    if (isGameOver() || isDraw()) {
                        break;
                    }

                } else if (roundAndMoveStrings.length == 2) {
                    Move blackMove = Move.moveFromString(roundAndMoveStrings[1], board);
                    if (playerToMove.validMoves(this).stream().anyMatch(move -> move.equals(blackMove))) { 
                        playerToMove.handleTurn(this, blackMove);
                    } else {
                        throw new InvalidMoveException("There is an invalid move in the file according to the rules");
                    }
                    break;
                } else {
                    throw new FileFormatException("Cannot parse file!");
                }
                roundNumber++;
            }
            if (sc.hasNextLine()) {
                throw new FileFormatException("Cannot parse file! There are more lines");
            }
        }    
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void executeCapture(Capture capture) {
        if (!capture.isPromotion(board)) {
            board.setPiece(board.getPiece(capture.getFrom()), capture.getTo());
        } else {
            Colour colour = board.getPiece(capture.getFrom()).getColour();
            board.setPiece(new King(colour), capture.getTo());
        }
        board.setPiece(null, capture.getFrom());
        Point capturedPoint = new Point((capture.getFrom().x + capture.getTo().x) / 2, (capture.getFrom().y + capture.getTo().y) / 2);
        board.setPiece(null, capturedPoint);
    }

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

    public void executeNormalMove(NormalMove normalMove) {
        if (!normalMove.isPromotion(board)) {
            board.setPiece(board.getPiece(normalMove.getFrom()), normalMove.getTo());
        } else {
            Colour colour = board.getPiece(normalMove.getFrom()).getColour();
            board.setPiece(new King(colour), normalMove.getTo());
        }
        board.setPiece(null, normalMove.getFrom());
    }
}
