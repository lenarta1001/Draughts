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
        Piece piece = new Checker(Colour.BLACK);
        assertEquals(Colour.BLACK, piece.getColour());
    }

    @Test
    void testToStringChecker() {
        Piece piece = new Checker(Colour.BLACK);
        assertEquals("", piece.toString());
    }

    @Test
    void testToStringKing() {
        Piece piece = new King(Colour.BLACK);
        assertEquals("K", piece.toString());
    }

    @Test
    void testValidNormalMovesBlackChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move expected = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(15), board);

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidNormalMovesWhiteChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(18));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));
        Move expected = new NormalMove(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(19), board);

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidCapturesBlackChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(14));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        Move expected = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board);

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidCapturesWhiteChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));
        Move expected = new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(13), board);

        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidCaptureSequencesBlackChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(22));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), board));

        Move expected = new CaptureSequence(captures, board);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected))); 
    }

    @Test
    void testValidCaptureSequencesWhiteChecker() {
        Board board = new Board();
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(18));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(10));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(13), board));
        captures.add(new Capture(Board.pointFromSquareNumber(13), Board.pointFromSquareNumber(6), board));

        Move expected = new CaptureSequence(captures, board);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
    }

    @Test
    void testValidNormalMovesBlackKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.BLACK), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(18));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(15), board));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(6), board));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(7), board));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    void testValidNormalMovesWhiteKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.WHITE), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(18));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(26), board));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(27), board));
        expectedMoves.add(new NormalMove(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(19), board));
        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    void testValidCapturesBlackKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.BLACK), Board.pointFromSquareNumber(11));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(7));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(11)).validMoves(board, Board.pointFromSquareNumber(11));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), board));
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(4), board));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    void testValidCapturesWhiteKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.WHITE), Board.pointFromSquareNumber(22));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(26));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(19));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(22)).validMoves(board, Board.pointFromSquareNumber(22));
        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(29), board));
        expectedMoves.add(new Capture(Board.pointFromSquareNumber(22), Board.pointFromSquareNumber(15), board));

        for (Move expected : expectedMoves) {
            assertTrue(moves.stream().anyMatch(move -> move.equals(expected)));
        }
    }

    @Test
    void testValidCaptureSequencesBlackKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.BLACK), Board.pointFromSquareNumber(19));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.WHITE), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(19)).validMoves(board, Board.pointFromSquareNumber(19));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(19), Board.pointFromSquareNumber(10), board));
        captures.add(new Capture(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(17), board));

        Move expected = new CaptureSequence(captures, board);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected))); 
    }

    @Test
    void testValidCaptureSequencesWhiteKing() {
        Board board = new Board();
        board.setPiece(new King(Colour.WHITE), Board.pointFromSquareNumber(19));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(14));
        board.setPiece(new Checker(Colour.BLACK), Board.pointFromSquareNumber(13));

        List<Move> moves = board.getPiece(Board.pointFromSquareNumber(19)).validMoves(board, Board.pointFromSquareNumber(19));

        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(19), Board.pointFromSquareNumber(10), board));
        captures.add(new Capture(Board.pointFromSquareNumber(10), Board.pointFromSquareNumber(17), board));

        Move expected = new CaptureSequence(captures, board);
        assertTrue(moves.stream().anyMatch(move -> move.equals(expected))); 
    }
}
