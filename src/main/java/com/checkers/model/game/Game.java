package com.checkers.model.game;

import java.util.List;
import java.util.Scanner;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.checkers.model.move.*;
import com.checkers.model.board.Board;
import com.checkers.model.piece.*;
import com.checkers.model.*;

public class Game {
    private Colour playerToMove;
    private Board board;
    private List<Move> moves;
    private HashMap<String, Integer> positionCounts;
    private int noPromotionCaptureCounter;

    Board getBoard() {
        return board;
    }

    Colour getPlayerToMove() {
        return playerToMove;
    }

    public boolean isGameOver() {
        return validMovesOfPlayerToMove().isEmpty();
    }

    public boolean isDraw() {
        boolean repetition = positionCounts.entrySet().stream().anyMatch(entry -> entry.getValue() >= 3);
        return repetition || noPromotionCaptureCounter >= 30;
    }

    public List<Move> validMovesOfPlayerToMove() {
        List<Move> moves = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            Point p = Board.pointFromSquareNumber(i);

            if (!board.isEmpty(p) && board.getPiece(p).getColour() == playerToMove) {
                moves.addAll(board.getPiece(p).validMoves(board, p));
            }
        }

        return moves;
    }

    public List<Move> validMovesAt(Point point) {
        if (!Board.isInsideBoard(point) || board.isEmpty(point) || board.getPiece(point).getColour() != playerToMove) {
            return new ArrayList<>();
        }
        return board.getPiece(point).validMoves(board, point);
    }
    
    public void initGame() {
        this.playerToMove = Colour.black;
        this.board = Board.initBoard();
        this.moves = new ArrayList<>();
        this.positionCounts = new HashMap<>();
        this.noPromotionCaptureCounter = 0;
    }

    public void cleanInitGame() {
        this.playerToMove = Colour.black;
        this.board = new Board();
        this.moves = new ArrayList<Move>();
        this.positionCounts = new HashMap<>();
        this.noPromotionCaptureCounter = 0;
    }

    public void endRound(Move move) {
        if (move.isMandatory() || move.isPromotion(board)) {
            noPromotionCaptureCounter = 0;
            positionCounts.clear();
        } else {
            noPromotionCaptureCounter++;
        }
        String fen = board.getFen(playerToMove);
        Integer count = positionCounts.get(fen);
        if (count == null) {
            positionCounts.put(fen, 1);
        } else {
            positionCounts.put(fen, count + 1);
        }
        moves.add(move);
        playerToMove = Colour.opposite(playerToMove);
        board.invert();
    }

    public void write(String fileName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
         
        for (int round = 0; round < moves.size() / 2; round++) {
            bw.write((round + 1) + ". " + moves.get(round * 2) + " " + moves.get(round * 2 + 1));
            bw.newLine();
        }
        if (moves.size() % 2 == 1) {
            bw.write((moves.size() / 2 + 1) + ". " + moves.getLast());
        }

        bw.close();
    }


    public void read(String fileName) throws FileNotFoundException, FileFormatException, InvalidMoveException {
        initGame();
        File file = new File(fileName);
        int roundNumber = 1;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                line = line.trim();
                if (line.length() == 0 || line.charAt(0) == '[') {
                    continue;
                }
                String[] roundAndMoveStrings = line.split(" ");

                try {
                    int readRoundNumber = Integer.parseInt(roundAndMoveStrings[0].replace(".", ""));
                    if (readRoundNumber != roundNumber) {
                        throw new FileFormatException("Cannot parse file. A round number is not correct!");
                    }
                } catch (NumberFormatException e) {
                    throw new FileFormatException("Cannot parse file. A round number is not correct!");
                }

                if (roundAndMoveStrings.length == 3) {

                    Move blackMove = Move.moveFromString(roundAndMoveStrings[1], board);
                    if (validMovesOfPlayerToMove().stream().anyMatch(move -> move.equals(blackMove))) { 
                        blackMove.execute(board);
                        endRound(blackMove);
                    } else {
                        throw new InvalidMoveException("There is an invalid move in the file according to the rules");
                    }

                    Move whiteMove = Move.moveFromString(roundAndMoveStrings[2], board);
                    if (validMovesOfPlayerToMove().stream().anyMatch(move -> move.equals(whiteMove))) {
                        whiteMove.execute(board);
                        endRound(whiteMove);
                    } else {
                        throw new InvalidMoveException("There is an invalid move in the file according to the rules");
                    }
                } else if (roundAndMoveStrings.length == 2) {
                    Move blackMove = Move.moveFromString(roundAndMoveStrings[1], board);
                    if (validMovesOfPlayerToMove().stream().anyMatch(move -> move.equals(blackMove))) { 
                        blackMove.execute(board);
                        endRound(blackMove);
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
}
