package com.checkers.model.game;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
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
        boolean includesMandatory = moves.stream().anyMatch(Move::isMandatory);

        if (includesMandatory) {
            moves.removeIf(m -> !m.isMandatory());
        }

        return moves;
    }

    public List<Move> validMovesAt(Point point) {
        return validMovesOfPlayerToMove().stream().filter(move -> move.getFrom().equals(point)).collect(Collectors.toList());
    }
    
    public void initialize() {
        this.playerToMove = Colour.black;
        this.board = Board.initBoard();
        this.moves = new ArrayList<>();
        this.positionCounts = new HashMap<>();
        this.noPromotionCaptureCounter = 0;
    }

    public void cleanInitialize() {
        this.playerToMove = Colour.black;
        this.board = new Board();
        this.moves = new ArrayList<Move>();
        this.positionCounts = new HashMap<>();
        this.noPromotionCaptureCounter = 0;
    }

    public boolean endRound(Move move) {
        if (move.isMandatory() || move.isPromotion()) {
            noPromotionCaptureCounter = 0;
            positionCounts.clear();
        }

        Integer count = positionCounts.get(board.getFen(playerToMove));
        if (count == null) {
            positionCounts.put(board.getFen(playerToMove),1);
        } else {
            positionCounts.put(board.getFen(playerToMove), count + 1);
        }

        playerToMove = Colour.opposite(playerToMove);
        board.invert();
        return isGameOver();
    }

    public void write(String fileName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName + ".txt"));
         
        for (int round = 0; round < moves.size() / 2; round++) {
            bw.write((round + 1) + ". " + moves.get(round * 2) + moves.get(round * 2 + 1));
            bw.newLine();
        }
        if (moves.size() % 2 == 1) {
            bw.write((moves.size() / 2 + 1) + ". " + moves.getLast());
        }

        bw.close();
    }


    public void read(String fileName) throws FileNotFoundException, FileFormatException {
        cleanInitialize();
        File file = new File(fileName);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                line = line.trim();
                if (line.charAt(0) == '[') {
                    continue;
                }
                String[] roundAndMoveStrings = line.split(" ");
                if (roundAndMoveStrings.length == 3) {
                    Move blackMove = moveFromString(roundAndMoveStrings[1], Colour.black);
                    Move whiteMove = moveFromString(roundAndMoveStrings[2], Colour.white);
                    blackMove.execute(board);
                    endRound(blackMove);
                    whiteMove.execute(board);
                    endRound(whiteMove);
                } else if (roundAndMoveStrings.length == 2) {
                    Move blackMove = moveFromString(roundAndMoveStrings[1], Colour.black);
                    blackMove.execute(board);
                    endRound(blackMove);
                    break;
                } else {
                    throw new FileFormatException("Cannot parse file!");
                }
            }

            if (sc.hasNextLine()) {
                throw new FileFormatException("Cannot parse file!");
            }
        }
        

    }
     
    private Move moveFromString(String moveString, Colour player) throws FileFormatException {
        Move move;

        if (moveString.contains("-")) {

            String[] postions = moveString.split("-");

            if (postions.length != 2) {
                throw new FileFormatException("Cannot parse file!");
            }

            int fromNumber = Integer.parseInt(postions[0]);
            int toNumber = Integer.parseInt(postions[1]);
            boolean onInvertedBoard = player == Colour.white;
            Point from = onInvertedBoard ? Board.invertPoint(Board.pointFromSquareNumber(fromNumber)) : Board.pointFromSquareNumber(fromNumber);
            Point to = onInvertedBoard ? Board.invertPoint(Board.pointFromSquareNumber(toNumber)) : Board.pointFromSquareNumber(toNumber);

            move = new NormalMove(from, to, onInvertedBoard);

        } else if (moveString.contains("x")) {

            String[] postions = moveString.split("x");

            if (postions.length >= 2) {

                boolean onInvertedBoard = player == Colour.white;
                List<Capture> captures = new ArrayList<>();

                for (int i = 0; i < postions.length - 1; i++) {

                    int fromNumber = Integer.parseInt(postions[i]);
                    int toNumber = Integer.parseInt(postions[i + 1]);

                    Point from = onInvertedBoard ? Board.invertPoint(Board.pointFromSquareNumber(fromNumber)) : Board.pointFromSquareNumber(fromNumber);
                    Point to = onInvertedBoard ? Board.invertPoint(Board.pointFromSquareNumber(toNumber)) : Board.pointFromSquareNumber(toNumber);

                    captures.add(new Capture(from, to, onInvertedBoard));

                }

                if (captures.size() == 1) {
                    move = captures.getFirst();
                } else {
                    move = new CaptureSequence(captures, onInvertedBoard);
                }
            } else {
                throw new FileFormatException("Cannot parse file!");
            }
        } else {
            throw new FileFormatException("Cannot parse file!");
        }

        return move;
    }


}
