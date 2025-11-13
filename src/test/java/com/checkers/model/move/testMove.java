package com.checkers.model.move;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.*;

import com.checkers.model.board.Board;
import com.checkers.model.piece.*;

public class testMove {

    @Test
    public void testNormalMoveToString() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), false);
        assertEquals("11-14", move.toString());
    }

    @Test
    public void testNormalMoveToStringOnInvertedTable() {
        Board board = Board.initBoard();
        board.invert();
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), true);
        assertEquals("22-19", move.toString());
    }

    @Test
    public void testCaptureString() {
        Board board = Board.initBoard();
        board.setPiece(board.getPiece(Board.pointFromSquareNumber(22)), Board.pointFromSquareNumber(14));
        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), false);
        assertEquals("11x18", move.toString());
    }

    @Test
    public void testCaptureToStringOnInvertedTable() {
        Board board = Board.initBoard();
        board.setPiece(board.getPiece(Board.pointFromSquareNumber(22)), Board.pointFromSquareNumber(14));
        board.invert();
        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), true);
        assertEquals("22x15", move.toString());
    }

    @Test
    public void testCaptureSequenceString() {
        Board board = Board.initBoard();
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(null, Board.pointFromSquareNumber(27));
        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), false));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), false));

        Move move = new CaptureSequence(captures, false);
        assertEquals("11x18x27", move.toString());
    }

    @Test
    public void testCaptureSequenceToStringOnInvertedTable() {
        Board board = Board.initBoard();
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(null, Board.pointFromSquareNumber(27));
        board.invert();
        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), true));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), true));

        Move move = new CaptureSequence(captures, true);
        assertEquals("22x15x6", move.toString());
    }

    @Test
    public void testGetFrom() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), false);
        assertEquals(Board.pointFromSquareNumber(11), move.getFrom());
    } 

    @Test
    public void testGetTo() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), false);
        assertEquals(Board.pointFromSquareNumber(14), move.getTo());
    }

    @Test
    public void testGetOnInvertedBoard() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), false);
        assertFalse(move.getOnInvertedBoard());
    }

    @Test
    public void testIsPromotion() {
        Move move = new NormalMove(Board.pointFromSquareNumber(26), Board.pointFromSquareNumber(29), false);
        assertTrue(move.isPromotion());
    }

    @Test
    public void testIsManadatoryTrue() {
        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), true);
        assertTrue(move.isMandatory());
    }

    @Test
    public void testIsManadatoryFalse() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), true);
        assertFalse(move.isMandatory());
    }

    @Test
    public void testExecuteNormalMoveNoPromotion() {
        Board board = Board.initBoard();
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), true);
        move.execute(board);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertEquals("", pieceAtToPoint.toString());
    }
    
    @Test
    public void testExecuteNormalMovePromotion() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(26));

        Move move = new NormalMove(Board.pointFromSquareNumber(26), Board.pointFromSquareNumber(29), false); 
        move.execute(board);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertEquals("K", pieceAtToPoint.toString());
    }

    @Test
    public void testExecuteCaptureNoPromotion() {
        Board board = new Board();

        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(18));

        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), false);
        move.execute(board);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtJumpedPoint = board.getPiece(new Point((move.getFrom().x + move.getTo().x) / 2, (move.getFrom().y + move.getTo().y) / 2));
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertNull(pieceAtJumpedPoint);
        assertEquals("", pieceAtToPoint.toString());
    }

    @Test
    public void testExecuteCapturePromotion() {
        Board board = new Board();

        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(27));

        Move move = new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(31), false);
        move.execute(board);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtJumpedPoint = board.getPiece(new Point((move.getFrom().x + move.getTo().x) / 2, (move.getFrom().y + move.getTo().y) / 2));
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        assertNull(pieceAtJumpedPoint);
        assertEquals("K", pieceAtToPoint.toString());
    }

    @Test
    public void testExecuteCaptureSequenceNoPromotion() {
        Board board = new Board();

        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(18));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(27));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), false));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), false));

        Move move = new CaptureSequence(captures, false);
        move.execute(board);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        Iterator<Move> moveIterator = move.iterator();
        while (moveIterator.hasNext()) {
            Move subMove = moveIterator.next();
            if (moveIterator.hasNext()) {
                assertNull(board.getPiece(subMove.getTo()));
            }
        }
        assertEquals("", pieceAtToPoint.toString());
    }

    @Test
    public void testExecuteCaptureSequencePromotion() {
        Board board = new Board();

        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(15));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(31));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(15), Board.pointFromSquareNumber(22), false));
        captures.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(31), false));

        Move move = new CaptureSequence(captures, false);
        move.execute(board);
        Piece pieceAtFromPoint = board.getPiece(move.getFrom());
        Piece pieceAtToPoint = board.getPiece(move.getTo());

        assertNull(pieceAtFromPoint);
        Iterator<Move> moveIterator = move.iterator();
        while (moveIterator.hasNext()) {
            Move subMove = moveIterator.next();
            if (moveIterator.hasNext()) {
                assertNull(board.getPiece(subMove.getTo()));
            }
        }
        assertEquals("K", pieceAtToPoint.toString());
    }
}
