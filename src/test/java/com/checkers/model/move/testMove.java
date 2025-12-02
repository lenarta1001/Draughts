package com.checkers.model.move;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.piece.*;
import com.checkers.model.player.HumanPlayer;

class TestMove {

    @Test
    void testNormalMoveToString() {
        Board board = new Board();
        board.initBoard();
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), board);
        assertEquals("11-14", move.toString());
    }

    @Test
    void testCaptureString() {
        Board board = new Board();
        board.initBoard();
        board.setPiece(board.getPiece(Board.pointFromSquareNumber(22)), Board.pointFromSquareNumber(14));
        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board);
        assertEquals("11x18", move.toString());
    }

    @Test
    void testCaptureSequenceString() {
        Board board = new Board();
        board.initBoard();
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(14));
        board.setPiece(null, Board.pointFromSquareNumber(27));
        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), board));

        Move move = new CaptureSequence(captures, board);
        assertEquals("11x18x27", move.toString());
    }

    @Test
    void testGetFrom() {
        Board board = new Board();
        board.initBoard();
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), board);
        assertEquals(Board.pointFromSquareNumber(11), move.getFrom());
    } 

    @Test
    void testGetTo() {
        Board board = new Board();
        board.initBoard();
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), board);
        assertEquals(Board.pointFromSquareNumber(14), move.getTo());
    }

    @Test
    void testIsPromotionNormalMove() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(26));
        Move move = new NormalMove(Board.pointFromSquareNumber(26), Board.pointFromSquareNumber(29), board);
        assertTrue(move.isPromotion());
    }

    @Test
    void testIsPromotionCaptureSequence() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(15));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(19));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(27));
        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(15), Board.pointFromSquareNumber(22), board));
        captures.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(31), board));

        Move move = new CaptureSequence(captures, board);
        assertTrue(move.isPromotion());
    }

    @Test
    void testIsManadatoryTrue() {
        Board board = new Board();
        board.initBoard();
        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board);
        assertTrue(move.isMandatory());
    }

    @Test
    void testIsManadatoryFalse() {
        Board board = new Board();
        board.initBoard();
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), board);
        assertFalse(move.isMandatory());
    }

    @Test
    void testExecuteNormalMoveNoPromotion() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.initGame();
        Board board = game.getBoard();

        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), board);
        move.execute(game);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertTrue(pieceAtToPoint instanceof Checker);
    }
    
    @Test
    void testExecuteNormalMovePromotion() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(26));

        Move move = new NormalMove(Board.pointFromSquareNumber(26), Board.pointFromSquareNumber(29), board); 
        move.execute(game);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertTrue(pieceAtToPoint instanceof King);
    }

    @Test
    void testExecuteCaptureNoPromotion() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();

        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(18));

        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board);
        move.execute(game);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtJumpedPoint = board.getPiece(new Point((move.getFrom().x + move.getTo().x) / 2, (move.getFrom().y + move.getTo().y) / 2));
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertNull(pieceAtJumpedPoint);
        assertTrue(pieceAtToPoint instanceof Checker);
    }

    @Test
    void testExecuteCapturePromotion() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();

        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(27));

        Move move = new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(31), board);
        move.execute(game);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtJumpedPoint = board.getPiece(new Point((move.getFrom().x + move.getTo().x) / 2, (move.getFrom().y + move.getTo().y) / 2));
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertNull(pieceAtJumpedPoint);
        assertTrue(pieceAtToPoint instanceof King);
    }

    @Test
    void testExecuteCaptureSequenceNoPromotion() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();

        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(18));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(27));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), board));

        CaptureSequence captureSequence = new CaptureSequence(captures, board);
        captureSequence.execute(game);
        Piece pieceAtFromPoint = board.getPiece(captureSequence.getFrom());
        Piece pieceAtToPoint = board.getPiece(captureSequence.getTo());

        assertNull(pieceAtFromPoint);
        for (Capture capture : captureSequence.getCaptures()) {
            Piece pieceAtJumpedPoint = board.getPiece(new Point((capture.getFrom().x + capture.getTo().x) / 2, (capture.getFrom().y + capture.getTo().y) / 2));
            assertNull(pieceAtJumpedPoint);
        }
        assertTrue(pieceAtToPoint instanceof Checker);
    }

    @Test
    void testExecuteCaptureSequencePromotion() {
        Game game = new Game(new HumanPlayer(Colour.BLACK), new HumanPlayer(Colour.WHITE));
        game.cleanInitGame();
        Board board = game.getBoard();

        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(15));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(31));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(15), Board.pointFromSquareNumber(22), board));
        captures.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(31), board));

        CaptureSequence captureSequence = new CaptureSequence(captures, board);
        captureSequence.execute(game);
        Piece pieceAtFromPoint = board.getPiece(captureSequence.getFrom());
        Piece pieceAtToPoint = board.getPiece(captureSequence.getTo());

        assertNull(pieceAtFromPoint);
        for (Capture capture : captureSequence.getCaptures()) {
            Piece pieceAtJumpedPoint = board.getPiece(new Point((capture.getFrom().x + capture.getTo().x) / 2, (capture.getFrom().y + capture.getTo().y) / 2));
            assertNull(pieceAtJumpedPoint);
        }
        assertTrue(pieceAtToPoint instanceof King);
    }

    @Test
    void testNormalMoveFromString() {
        Board board = new Board();
        board.initBoard();
        assertEquals(Move.moveFromString("11-14", board), new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), board));
    }

    @Test
    void testCaptureFromString() {
        Board board = new Board();
        board.initBoard();
        assertEquals(Move.moveFromString("11x18", board), new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board));
    }

    @Test
    void testCaptureSequenceFromString() {
        Board board = new Board();
        board.initBoard();
        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), board));
        CaptureSequence captureSequence = new CaptureSequence(captures, board);
        assertEquals(Move.moveFromString("11x18x27", board), captureSequence);
    }

    @Test
    void testNormalMoveFromStringNumberOutOfRange() throws IllegalArgumentException {
        Board board = new Board();
        board.initBoard();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("33-1", board); }
        );
    }

    @Test
    void testNormalMoveFromStringNotNumber() throws IllegalArgumentException {
        Board board = new Board();
        board.initBoard();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("ab-1", board); }
        );
    }

    @Test
    void testNormalMoveFromStringTooManySeparators() throws IllegalArgumentException {
        Board board = new Board();
        board.initBoard();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("3-1-2", board); }
        );
    }

    @Test
    void testCaptureFromStringNumberOutOfRange() throws IllegalArgumentException {
        Board board = new Board();
        board.initBoard();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("3x0", board); }
        );
    }

    @Test
    void testCaptureFromStringNotNumber() throws IllegalArgumentException {
        Board board = new Board();
        board.initBoard();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("abx1", board); }
        );
    }

    @Test
    void testMoveFromStringNoSeparator() throws IllegalArgumentException {
        Board board = new Board();
        board.initBoard();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("1122", board); }
        );
    }
}
