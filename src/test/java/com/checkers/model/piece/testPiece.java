package com.checkers.model.piece;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;
import com.checkers.model.move.*;

class TestPiece {
    
    @Test
    void testGetColour() {
        Piece piece = new Checker(Colour.black);
        assertEquals(Colour.black, piece.getColour());
    }

    @Test
    void testToStringChecker() {
        Piece piece = new Checker(Colour.black);
        assertEquals("", piece.toString());
    }

    @Test
    void testToStringKing() {
        Piece piece = new King(Colour.black);
        assertEquals("K", piece.toString());
    }

    @Test
    void testValidNormalMovesBlackChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move expected = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(15));

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidNormalMovesWhiteChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(18));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));
        Move expected = new NormalMove(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(19));

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidCapturesBlackChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move expected = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18));

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidCapturesWhiteChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));
        Move expected = new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(13));

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidCaptureSequencesBlackChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(22));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18)));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27)));

        Move expected = new CaptureSequence(captures);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected))); 
    }

    @Test
    void testValidCaptureSequencesWhiteChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(18));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(10));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(13)));
        captures.add(new Capture(Board.pointFromSquareNumber(13), Board.pointFromSquareNumber(6)));

        Move expected = new CaptureSequence(captures);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidNormalMovesBlackKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(15)));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(6)));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(7)));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    void testValidNormalMovesWhiteKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.white), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(18));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(26)));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(27)));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(19)));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    void testValidCapturesBlackKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(7));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18)));
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(4)));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    void testValidCapturesWhiteKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.white), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(26));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(19));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(29)));
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(15)));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    void testValidCaptureSequencesBlackKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.black), Board.pointFromSquareNumber(19));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(19)).validMoves(board, Board.pointFromSquareNumber(19));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(19), Board.pointFromSquareNumber(10)));
        captures.add(new Capture(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(17)));

        Move expected = new CaptureSequence(captures);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected))); 
    }

    @Test
    void testValidCaptureSequencesWhiteKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.white), Board.pointFromSquareNumber(19));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.black), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(19)).validMoves(board, Board.pointFromSquareNumber(19));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(19), Board.pointFromSquareNumber(10)));
        captures.add(new Capture(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(17)));

        Move expected = new CaptureSequence(captures);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected))); 
    }
}
