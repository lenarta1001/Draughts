package com.checkers.model.piece;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;
import com.checkers.model.move.*;

public class testPiece {
    
    @Test
    public void testGetColour() {
        Piece piece = new Checker(Colour.black);
        assertEquals(Colour.black, piece.getColour());
    }

    @Test
    public void testToStringChecker() {
        Piece piece = new Checker(Colour.black);
        assertEquals("", piece.toString());
    }

    @Test
    public void testToStringKing() {
        Piece piece = new King(Colour.black);
        assertEquals("K", piece.toString());
    }

    @Test
    public void testValidNormalMovesBlackChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move expected = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(15), false);

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    public void testValidNormalMovesWhiteChecker() {
        Board board = new Board();
        board.invert();
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move expected = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(15), true);

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    public void testValidCapturesBlackChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move expected = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), false);

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    public void testValidCapturesWhiteChecker() {
        Board board = new Board();
        board.invert();
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(14));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move expected = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), true);

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    public void testValidCaptureSequencesBlackChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(22));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), false));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), false));

        Move expected = new CaptureSequence(captures, false);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected))); 
    }

    @Test
    public void testValidCaptureSequencesWhiteChecker() {
        Board board = new Board();
        board.invert();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(22));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), true));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), true));

        Move expected = new CaptureSequence(captures, true);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    public void testValidNormalMovesBlackKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(15), false));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(6), false));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(7), false));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    public void testValidNormalMovesWhiteKing() {
        Board board = new Board();
        board.invert();
        board.setPiece(new King(Colour.white), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(15), true));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(6), true));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(7), true));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    public void testValidCapturesBlackKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(7));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), false));
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(4),false));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    public void testValidCapturesWhiteKing() {
        Board board = new Board();
        board.invert();
        board.setPiece(new King(Colour.white), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(7));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), true));
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(4),true));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    public void testValidCaptureSequencesBlackKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(19));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(19)).validMoves(board, Board.pointFromSquareNumber(19));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(19), Board.pointFromSquareNumber(10), false));
        captures.add(new Capture(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(17), false));

        Move expected = new CaptureSequence(captures, false);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected))); 
    }

    @Test
    public void testValidCaptureSequencesWhiteKing() {
        Board board = new Board();
        board.invert();
        board.setPiece(new King(Colour.white), Board.pointFromSquareNumber(19));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(19)).validMoves(board, Board.pointFromSquareNumber(19));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(19), Board.pointFromSquareNumber(10), true));
        captures.add(new Capture(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(17), true));

        Move expected = new CaptureSequence(captures, true);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected))); 
    }
}
