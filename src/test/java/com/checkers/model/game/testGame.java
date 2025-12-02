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
import com.checkers.model.player.HumanPlayer;

class TestGame {
    @Test
    void testIsGameOver() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(14));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move capture = moves.stream().filter(move -> move instanceof Capture).findFirst().orElse(null);
        game.getPlayerToMove().handleTurn(game, capture);
        game.getPlayerNotToMove().onOpponentTurnCompleted(game);
        assertTrue(game.isGameOver());
    }

    @Test
    void testIsDrawRepetition() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new King(Colour.BLACK), Board.pointFromSquareNumber(5));
        board.setPiece(new King(Colour.WHITE), Board.pointFromSquareNumber(28));

        List<Move> movesOfBlack;
        List<Move> movesOfWhite;
        Move normalMoveOfBlack;
        Move normalMoveOfWhite;

        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                movesOfBlack = board.getPiece(Board.pointFromSquareNumber(5)).validMoves(board, Board.pointFromSquareNumber(5));
                normalMoveOfBlack = movesOfBlack.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(1))).findFirst().orElse(null);
                game.getPlayerToMove().handleTurn(game, normalMoveOfBlack);
                game.getPlayerNotToMove().onOpponentTurnCompleted(game);
                movesOfWhite = board.getPiece(Board.pointFromSquareNumber(28)).validMoves(board, Board.pointFromSquareNumber(28));
                normalMoveOfWhite = movesOfWhite.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(32))).findFirst().orElse(null);
                game.getPlayerToMove().handleTurn(game, normalMoveOfWhite);
                game.getPlayerNotToMove().onOpponentTurnCompleted(game);
            } else {
                movesOfBlack = board.getPiece(Board.pointFromSquareNumber(1)).validMoves(board, Board.pointFromSquareNumber(1));
                normalMoveOfBlack = movesOfBlack.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(5))).findFirst().orElse(null);
                game.getPlayerToMove().handleTurn(game, normalMoveOfBlack);
                game.getPlayerNotToMove().onOpponentTurnCompleted(game);
                movesOfWhite = board.getPiece(Board.pointFromSquareNumber(32)).validMoves(board, Board.pointFromSquareNumber(32));
                normalMoveOfWhite = movesOfWhite.stream().filter(move -> move.getTo().equals(Board.pointFromSquareNumber(28))).findFirst().orElse(null);
                game.getPlayerToMove().handleTurn(game, normalMoveOfWhite);
                game.getPlayerNotToMove().onOpponentTurnCompleted(game);
            }
        }
    
        assertTrue(game.isDraw());
    }

    @Test
    void testIsDrawNoPromotionCapture() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new King(Colour.BLACK), Board.pointFromSquareNumber(2));
        board.setPiece(new King(Colour.WHITE), Board.pointFromSquareNumber(9));

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
    void testValidMovesOfPlayerToMove() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(10));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(9));

        List<Move> moves = game.getPlayerToMove().validMoves(game);
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(9), Board.pointFromSquareNumber(13), board));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(14), board));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(13), board));

        assertEquals(expectedMoves, moves);
    }

    @Test
    void testValidMovesAtPlayerToMove() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(10));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(9));

        List<Move> moves = game.getPlayerToMove().validMovesAt(game, Board.pointFromSquareNumber(10));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(14), board));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(13), board));

        assertEquals(expectedMoves, moves);
    }

    @Test
    void testSerializationOneMove() throws InvalidMoveException, IOException {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
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
    void testSerializationWithComments() throws IOException, InvalidMoveException {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.initGame();
        Move blackMove = game.getPlayerToMove().validMoves(game).stream().filter(move -> move.getFrom().equals(Board.pointFromSquareNumber(9)) && move.getTo().equals(Board.pointFromSquareNumber(13))).findFirst().orElse(null);
        game.getPlayerToMove().handleTurn(game, blackMove);
        game.getPlayerNotToMove().onOpponentTurnCompleted(game);

        Move whiteMove = game.getPlayerToMove().validMoves(game).stream().filter(move -> move.getFrom().equals(Board.pointFromSquareNumber(24)) && move.getTo().equals(Board.pointFromSquareNumber(20))).findFirst().orElse(null);
        game.getPlayerToMove().handleTurn(game, whiteMove);
        game.getPlayerNotToMove().onOpponentTurnCompleted(game);

        Board board = game.getBoard();

        Path path = Paths.get("test-data", "game_with_comments.txt");
        game.read(path.toString());
        assertEquals(board.getFen(game.getPlayerToMove()), game.getBoard().getFen(game.getPlayerToMove()));   
    }

    @Test
    void testSerializationInvalidMove() throws IOException, InvalidMoveException {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        Path path = Paths.get("test-data", "game_with_invalid_move.txt");
        assertThrows(InvalidMoveException.class, 
            () -> { game.read(path.toString()); }
        );
    }

    @Test
    void testSerializationRoundNumberFormat() throws IOException, InvalidMoveException {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        Path path = Paths.get("test-data", "game_with_round_number_incorrect.txt");
        assertThrows(FileFormatException.class, 
            () -> { game.read(path.toString()); }
        );
    }

    @Test
    void testSerializationNumberFormat() throws IOException, InvalidMoveException {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        Path path = Paths.get("test-data", "game_with_round_number_invalid.txt");
        assertThrows(FileFormatException.class, 
            () -> { game.read(path.toString()); }
        );
    }

    @Test
    void testSerializationTooManyArguments() throws IOException, InvalidMoveException {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        Path path = Paths.get("test-data", "game_with_too_many_arguments.txt");
        assertThrows(FileFormatException.class, 
            () -> { game.read(path.toString()); }
        );
    }

    @Test
    void testSerializationLineAfterHalfLine() throws IOException, InvalidMoveException {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        Path path = Paths.get("test-data", "game_with_extra_line.txt");
        assertThrows(FileFormatException.class, 
            () -> { game.read(path.toString()); }
        );
    }
}
