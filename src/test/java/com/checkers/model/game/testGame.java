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
import com.checkers.model.colour.Colour;
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
        game.getPlayerToMove().handleTurn(game, capture);
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
                game.getPlayerToMove().handleTurn(game, normalMoveOfBlack);
                movesOfWhite = board.getPiece(Board.pointFromSquareNumber(28)).validMoves(board, Board.pointFromSquareNumber(28));
                normalMoveOfWhite = movesOfWhite.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(32))).findFirst().orElse(null);
                game.getPlayerToMove().handleTurn(game, normalMoveOfWhite);
            } else {
                movesOfBlack = board.getPiece(Board.pointFromSquareNumber(1)).validMoves(board, Board.pointFromSquareNumber(1));
                normalMoveOfBlack = movesOfBlack.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(5))).findFirst().orElse(null);
                game.getPlayerToMove().handleTurn(game, normalMoveOfBlack);
                movesOfWhite = board.getPiece(Board.pointFromSquareNumber(32)).validMoves(board, Board.pointFromSquareNumber(32));
                normalMoveOfWhite = movesOfWhite.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(28))).findFirst().orElse(null);
                game.getPlayerToMove().handleTurn(game, normalMoveOfWhite);
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
            game.getPlayerToMove().handleTurn(game, normalMoveOfBlack);

            List<Move> movesOfWhite = board.getPiece(Board.pointFromSquareNumber(squareNumbersOfWhiteCircle.get(i % whiteCircleSize))).validMoves(board, Board.pointFromSquareNumber(squareNumbersOfWhiteCircle.get(i % whiteCircleSize)));
            Point nextPointWhite = Board.pointFromSquareNumber(squareNumbersOfWhiteCircle.get((i + 1) % whiteCircleSize));
            Move normalMoveOfWhite = movesOfWhite.stream().filter(move -> move.getTo().equals(nextPointWhite)).findFirst().orElse(null);
            game.getPlayerToMove().handleTurn(game, normalMoveOfWhite);
        }
    
        assertTrue(game.isDraw());
    }

    @Test
    public void testValidMovesOfPlayerToMove() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(10));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(9));

        List<Move> moves = game.getPlayerToMove().validMoves(game);
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(9), Board.pointFromSquareNumber(13)));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(14)));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(13)));

        assertEquals(expectedMoves, moves);
    }

    @Test
    public void testValidMovesAtPlayerToMove() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(10));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(9));

        List<Move> moves = game.getPlayerToMove().validMovesAt(game, Board.pointFromSquareNumber(10));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(14)));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(13)));

        assertEquals(expectedMoves, moves);
    }

    @Test
    public void testSerializationOneMove() throws InvalidMoveException, IOException {
        Game game = new Game();
        game.initGame();
        Move move = game.getPlayerToMove().validMoves(game).stream().filter(validMove -> validMove.getFrom().equals(Board.pointFromSquareNumber(9))).findFirst().orElse(null);
        game.getPlayerToMove().handleTurn(game, move);

        Board board = game.getBoard();
        Path path = Paths.get("test-data", "game.txt");
        game.write(path.toString());
        game.read(path.toString());
        assertEquals(board.getFen(game.getPlayerToMove()), game.getBoard().getFen(game.getPlayerToMove()));   
    }

    
    @Test
    public void testSerializationWithComments() throws IOException, InvalidMoveException {
        Game game = new Game();
        game.initGame();
        Move blackMove = game.getPlayerToMove().validMoves(game).stream().filter(move -> move.getFrom().equals(Board.pointFromSquareNumber(9)) && move.getTo().equals(Board.pointFromSquareNumber(13))).findFirst().orElse(null);
        game.getPlayerToMove().handleTurn(game, blackMove);

        Move whiteMove = game.getPlayerToMove().validMoves(game).stream().filter(move -> move.getFrom().equals(Board.pointFromSquareNumber(24)) && move.getTo().equals(Board.pointFromSquareNumber(20))).findFirst().orElse(null);
        game.getPlayerToMove().handleTurn(game, whiteMove);

        Board board = game.getBoard();

        Path path = Paths.get("test-data", "game_with_comments.txt");
        game.read(path.toString());
        assertEquals(board.getFen(game.getPlayerToMove()), game.getBoard().getFen(game.getPlayerToMove()));   
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
