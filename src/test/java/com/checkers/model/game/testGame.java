package com.checkers.model.game;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

import com.checkers.model.FileFormatException;
import com.checkers.model.InvalidMoveException;
import com.checkers.model.board.*;
import com.checkers.model.move.*;
import com.checkers.model.piece.*;

public class testGame {
    @Test
    public void testIsGameOver() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move capture = moves.stream().filter(move -> move instanceof Capture).findFirst().orElse(null);
        capture.execute(board);
        game.endRound(capture);
        assertTrue(game.isGameOver());
    }

    @Test
    public void testIsDrawRepetition() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(5));
        board.setPiece(new King(Colour.white), Board.pointFromSquareNumber(28));

        List<Move> movesOfBlack;
        List<Move> movesOfWhite;
        Move normalMoveOfBlack;
        Move normalMoveOfWhite;

        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                movesOfBlack = board.getPiece(Board.pointFromSquareNumber(5)).validMoves(board, Board.pointFromSquareNumber(5));
                normalMoveOfBlack = movesOfBlack.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(1))).findFirst().orElse(null);
                normalMoveOfBlack.execute(board);
                game.endRound(normalMoveOfBlack);
                movesOfWhite = board.getPiece(Board.pointFromSquareNumber(5)).validMoves(board, Board.pointFromSquareNumber(5));
                normalMoveOfWhite = movesOfWhite.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(1))).findFirst().orElse(null);
                normalMoveOfWhite.execute(board);
                game.endRound(normalMoveOfWhite);
            } else {
                movesOfBlack = board.getPiece(Board.pointFromSquareNumber(1)).validMoves(board, Board.pointFromSquareNumber(1));
                normalMoveOfBlack = movesOfBlack.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(5))).findFirst().orElse(null);
                normalMoveOfBlack.execute(board);
                game.endRound(normalMoveOfBlack);
                movesOfWhite = board.getPiece(Board.pointFromSquareNumber(1)).validMoves(board, Board.pointFromSquareNumber(1));
                normalMoveOfWhite = movesOfWhite.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(5))).findFirst().orElse(null);
                normalMoveOfWhite.execute(board);
                game.endRound(normalMoveOfWhite);
            }
        }
    
        assertTrue(game.isDraw());
    }

    @Test
    public void testIsDrawNoPromotionCapture() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(2));
        board.setPiece(new King(Colour.white), Board.pointFromSquareNumber(9));

        List<Integer> squareNumbersOfBlackCircle = List.of(9, 13, 18, 22, 27, 31, 28, 24, 20, 15, 11, 6, 2, 5);
        List<Integer> squareNumbersOfWhiteCircle = List.of(2, 5, 9, 13, 18, 22, 27, 31, 28, 24, 20, 15, 11, 6);
        int blackCircleSize = squareNumbersOfBlackCircle.size();
        int whiteCircleSize = squareNumbersOfWhiteCircle.size();

        for (int i = 0; i < 30; i++) {
            List<Move> movesOfBlack = board.getPiece(Board.pointFromSquareNumber(squareNumbersOfBlackCircle.get(i % blackCircleSize))).validMoves(board, Board.pointFromSquareNumber(squareNumbersOfBlackCircle.get(i % blackCircleSize)));
            Point nextPointBlack = Board.pointFromSquareNumber(squareNumbersOfBlackCircle.get((i + 1) % blackCircleSize));
            Move normalMoveOfBlack = movesOfBlack.stream().filter(move -> move.getTo().equals(nextPointBlack)).findFirst().orElse(null);
            normalMoveOfBlack.execute(board);
            game.endRound(normalMoveOfBlack);

            List<Move> movesOfWhite = board.getPiece(Board.invertPoint(Board.pointFromSquareNumber(squareNumbersOfWhiteCircle.get(i % whiteCircleSize)))).validMoves(board, Board.invertPoint(Board.pointFromSquareNumber(squareNumbersOfWhiteCircle.get(i % whiteCircleSize))));
            Point nextPointWhite = Board.invertPoint(Board.pointFromSquareNumber(squareNumbersOfWhiteCircle.get((i + 1) % whiteCircleSize)));
            Move normalMoveOfWhite = movesOfWhite.stream().filter(move -> move.getTo().equals(nextPointWhite)).findFirst().orElse(null);
            normalMoveOfWhite.execute(board);
            game.endRound(normalMoveOfWhite);
        }
    
        assertTrue(game.isDraw());
    }

    @Test
    public void testValidMovesOfPlayerToMove() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(10));
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(9));

        List<Move> moves = game.validMovesOfPlayerToMove();
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(14), false));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(13), false));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(9), Board.pointFromSquareNumber(13), false));

        moves.equals(expectedMoves);
    }

    @Test
    public void testValidMovesAt() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(10));
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(9));

        List<Move> moves = game.validMovesAt(Board.pointFromSquareNumber(10));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(14), false));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(13), false));

        moves.equals(expectedMoves);
    }

    @Test
    public void testSerializationOneMove() throws InvalidMoveException, IOException {
        Game game = new Game();
        game.initGame();
        Move move = game.validMovesOfPlayerToMove().getFirst();
        move.execute(game.getBoard());
        game.endRound(move);

        Board board = game.getBoard();
        Path path = Paths.get("test-data", "game.txt");
        game.write(path.toString());
        game.read(path.toString());
        assertEquals(board.getFen(Colour.white), game.getBoard().getFen(game.getPlayerToMove()));   
    }

    
    @Test
    public void testSerializationWithComments() throws IOException, InvalidMoveException {
        Game game = new Game();
        game.initGame();
        Move blackMove = game.validMovesOfPlayerToMove().stream().filter(move -> move.getFrom().equals(Board.pointFromSquareNumber(9)) && move.getTo().equals(Board.pointFromSquareNumber(13))).findFirst().orElse(null);
        blackMove.execute(game.getBoard());
        game.endRound(blackMove);

        Move whiteMove = game.validMovesOfPlayerToMove().stream().filter(move -> move.getFrom().equals(Board.pointFromSquareNumber(9)) && move.getTo().equals(Board.pointFromSquareNumber(13))).findFirst().orElse(null);
        whiteMove.execute(game.getBoard());
        game.endRound(whiteMove);

        Board board = game.getBoard();

        Path path = Paths.get("test-data", "game_with_comments.txt");
        game.read(path.toString());
        assertEquals(board.getFen(Colour.black), game.getBoard().getFen(game.getPlayerToMove()));   
    }

    @Test
    public void testSerializationInvalidMove() throws IOException, InvalidMoveException {
        Game game = new Game();
        Path path = Paths.get("test-data", "game_with_invalid_move.txt");
        assertThrows(InvalidMoveException.class, 
            () -> { game.read(path.toString()); }
        );
    }

    @Test
    public void testSerializationRoundNumberFormat() throws IOException, InvalidMoveException {
        Game game = new Game();
        Path path = Paths.get("test-data", "game_with_round_number_incorrect.txt");
        assertThrows(FileFormatException.class, 
            () -> { game.read(path.toString()); }
        );
    }

    @Test
    public void testSerializationNumberFormat() throws IOException, InvalidMoveException {
        Game game = new Game();
        Path path = Paths.get("test-data", "game_with_round_number_invalid.txt");
        assertThrows(FileFormatException.class, 
            () -> { game.read(path.toString()); }
        );
    }

    @Test
    public void testSerializationTooManyArguments() throws IOException, InvalidMoveException {
        Game game = new Game();
        Path path = Paths.get("test-data", "game_with_too_many_arguments.txt");
        assertThrows(FileFormatException.class, 
            () -> { game.read(path.toString()); }
        );
    }

    @Test
    public void testSerializationLineAfterHalfLine() throws IOException, InvalidMoveException {
        Game game = new Game();
        Path path = Paths.get("test-data", "game_with_extra_line.txt");
        assertThrows(FileFormatException.class, 
            () -> { game.read(path.toString()); }
        );
    }
}
