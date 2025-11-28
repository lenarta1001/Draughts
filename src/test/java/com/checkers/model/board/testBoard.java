package com.checkers.model.board;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.*;

import com.checkers.model.colour.Colour;
import com.checkers.model.piece.King;
import com.checkers.model.player.HumanPlayer;


public class testBoard {
    
    @Test
    public void testSquareNumberFromPointNormal() {
        assertEquals(5, Board.squareNumberFromPoint(new Point(1, 6)));
    }

    @Test
    public void testSquareNumberFromPointThrow() {
        assertThrows(IllegalArgumentException.class, 
            () -> { Board.squareNumberFromPoint(new Point(0, 0)); }
        );
    }

    @Test
    public void testPointFromSquareNumberNormal() {
        assertEquals(new Point(3, 0), Board.pointFromSquareNumber(30));
    }

    @Test
    public void testPointFromSquareNumberThrow() {
        assertEquals(new Point(3, 0), Board.pointFromSquareNumber(30));
    }

    @Test
    void testIsInsideBoardTrue() {
        assertTrue(Board.isInsideBoard(new Point(7, 7)));
    }


    @Test
    void testIsInsideBoardFalse() {
        assertFalse(Board.isInsideBoard(new Point(0, 8)));
    }

    @Test
    void testIsEmptyFalse() {
        Board board = new Board();
        board.initBoard();
        assertFalse(board.isEmpty(new Point(1, 0)));
    }

    @Test
    void testIsEmptyTrue() {
        Board board = new Board();
        board.initBoard();
        assertTrue(board.isEmpty(new Point(1, 7)));
    }
    
    @Test
    public void testInitBoard() {
        Board board = new Board();
        board.initBoard();
        for (int i = 1; i <= 32; i++) {
            if (i <= 12) {
                assertFalse(board.isEmpty(Board.pointFromSquareNumber(i)));
                assertEquals(board.getPiece(Board.pointFromSquareNumber(i)).getColour(), Colour.black);
            } else if (i >= 21) {
                assertFalse(board.isEmpty(Board.pointFromSquareNumber(i)));
                assertEquals(board.getPiece(Board.pointFromSquareNumber(i)).getColour(), Colour.white);
            } else {
                assertTrue(board.isEmpty(Board.pointFromSquareNumber(i)));
            }
        }
    }

    @Test
    public void testFen() {
        Board board = new Board();
        board.initBoard();
        board.setPiece(new King(Colour.white), new Point(1, 0));
        assertEquals("B:W21,22,23,24,25,26,27,28,29K,30,31,32:B1,2,3,4,5,6,7,8,9,10,11,12", board.getFen(new HumanPlayer(Colour.black)));
    }
}
