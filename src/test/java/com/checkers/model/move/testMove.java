package com.checkers.model.move;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

import com.checkers.model.FileFormatException;
import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.piece.*;

public class testMove {

    @Test
    public void testNormalMoveToString() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14));
        assertEquals("11-14", move.toString());
    }

    @Test
    public void testCaptureString() {
        Board board = new Board();
        board.initBoard();
        board.setPiece(board.getPiece(Board.pointFromSquareNumber(22)), Board.pointFromSquareNumber(14));
        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18));
        assertEquals("11x18", move.toString());
    }

    @Test
    public void testCaptureSequenceString() {
        Board board = new Board();
        board.initBoard();
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(null, Board.pointFromSquareNumber(27));
        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18)));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27)));

        Move move = new CaptureSequence(captures);
        assertEquals("11x18x27", move.toString());
    }

    @Test
    public void testGetFrom() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14));
        assertEquals(Board.pointFromSquareNumber(11), move.getFrom());
    } 

    @Test
    public void testGetTo() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14));
        assertEquals(Board.pointFromSquareNumber(14), move.getTo());
    }

    @Test
    public void testIsPromotion() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(26));
        Move move = new NormalMove(Board.pointFromSquareNumber(26), Board.pointFromSquareNumber(29));
        assertTrue(move.isPromotion(board));
    }

    @Test
    public void testIsManadatoryTrue() {
        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18));
        assertTrue(move.isMandatory());
    }

    @Test
    public void testIsManadatoryFalse() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14));
        assertFalse(move.isMandatory());
    }

    @Test
    public void testExecuteNormalMoveNoPromotion() {
        Game game = new Game();
        game.initGame();
        Board board = game.getBoard();

        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14));
        move.execute(game);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertTrue(pieceAtToPoint instanceof Checker);
    }
    
    @Test
    public void testExecuteNormalMovePromotion() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(26));

        Move move = new NormalMove(Board.pointFromSquareNumber(26), Board.pointFromSquareNumber(29)); 
        move.execute(game);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertTrue(pieceAtToPoint instanceof King);
    }

    @Test
    public void testExecuteCaptureNoPromotion() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();

        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(18));

        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18));
        move.execute(game);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtJumpedPoint = board.getPiece(new Point((move.getFrom().x + move.getTo().x) / 2, (move.getFrom().y + move.getTo().y) / 2));
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertNull(pieceAtJumpedPoint);
        assertTrue(pieceAtToPoint instanceof Checker);
    }

    @Test
    public void testExecuteCapturePromotion() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();

        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(27));

        Move move = new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(31));
        move.execute(game);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtJumpedPoint = board.getPiece(new Point((move.getFrom().x + move.getTo().x) / 2, (move.getFrom().y + move.getTo().y) / 2));
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertNull(pieceAtJumpedPoint);
        assertTrue(pieceAtToPoint instanceof King);
    }

    @Test
    public void testExecuteCaptureSequenceNoPromotion() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();

        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(18));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(27));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18)));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27)));

        CaptureSequence captureSequence = new CaptureSequence(captures);
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
    public void testExecuteCaptureSequencePromotion() {
        Game game = new Game();
        game.cleanInitGame();
        Board board = game.getBoard();

        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(15));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(31));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(15), Board.pointFromSquareNumber(22)));
        captures.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(31)));

        CaptureSequence captureSequence = new CaptureSequence(captures);
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
    public void testNormalMoveFromStringNotInverted() throws FileFormatException {
        Board board = new Board();
        assertEquals(Move.moveFromString("11-14", board), new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14)));
    }

    @Test
    public void testCaptureFromStringNotInverted() throws FileFormatException {
        Board board = new Board();
        assertEquals(Move.moveFromString("11x18", board), new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18)));
    }

    @Test
    public void testCaptureSequenceFromStringNotInverted() throws FileFormatException {
        Board board = new Board();
        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18)));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27)));
        CaptureSequence captureSequence = new CaptureSequence(captures);
        assertEquals(Move.moveFromString("11x18x27", board), captureSequence);
    }

    @Test
    public void testNormalMoveFromStringNumberOutOfRange() throws IllegalArgumentException {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("33-1", board); }
        );
    }

    @Test
    public void testNormalMoveFromStringNotNumber() throws IllegalArgumentException {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("ab-1", board); }
        );
    }

    @Test
    public void testNormalMoveFromStringTooManySeparators() throws IllegalArgumentException {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("3-1-2", board); }
        );
    }

    @Test
    public void testCaptureFromStringNumberOutOfRange() throws IllegalArgumentException {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("3x0", board); }
        );
    }

    @Test
    public void testCaptureFromStringNotNumber() throws IllegalArgumentException {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("abx1", board); }
        );
    }

    @Test
    public void testMoveFromStringNoSeparator() throws IllegalArgumentException {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, 
            () -> { Move.moveFromString("1122", board); }
        );
    }
}
