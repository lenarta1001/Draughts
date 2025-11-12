package com.checkers.model.move;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

import com.checkers.model.board.Board;
import com.checkers.model.piece.Colour;
import com.checkers.model.piece.*;

public class testMove {
    Board board;

    @BeforeEach
    public void init() {
        board = Board.initBoard();
    }

    @Test
    public void testNormalMoveToString() {
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), false);
        assertEquals("11-14", move.toString());
    }

    @Test
    public void testNormalMoveToStringOnInvertedTable() {
        board.invert();
        Move move = new NormalMove(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(14), true);
        assertEquals("22-19", move.toString());
    }

    @Test
    public void testCaptureString() {
        board.setPiece(board.getPiece(Board.pointFromSquareNumber(22)), Board.pointFromSquareNumber(14));
        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), false);
        assertEquals("11x18", move.toString());
    }

    @Test
    public void testCaptureToStringOnInvertedTable() {
        board.setPiece(board.getPiece(Board.pointFromSquareNumber(22)), Board.pointFromSquareNumber(14));
        board.invert();
        Move move = new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), true);
        assertEquals("22x15", move.toString());
    }

    @Test
    public void testCaptureSequenceString() {
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
        board.setPiece(new Checker(Colour.white), Board.pointFromSquareNumber(14));
        board.setPiece(null, Board.pointFromSquareNumber(27));
        board.invert();
        List<Capture> captures = new ArrayList<>();
        captures.add(new Capture(Board.pointFromSquareNumber(11), Board.pointFromSquareNumber(18), true));
        captures.add(new Capture(Board.pointFromSquareNumber(18), Board.pointFromSquareNumber(27), true));

        Move move = new CaptureSequence(captures, true);
        assertEquals("22x15x6", move.toString());
    }
}
