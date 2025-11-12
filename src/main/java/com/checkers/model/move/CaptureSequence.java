package com.checkers.model.move;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.checkers.model.board.*;

public class CaptureSequence extends Capture {
    protected List<Capture> captureSequence;

    public CaptureSequence(List<Capture> captureSequence, boolean onInvertedBoard) {
        super(captureSequence.getFirst().from, captureSequence.getLast().to, onInvertedBoard);
        this.captureSequence = captureSequence;
    }

    public void execute(Board board) {
        for (Capture capture : captureSequence) {
            capture.execute(board);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Capture capture : captureSequence) {
            Point absoluteFrom = onInvertedBoard ? Board.invertPoint(capture.from) : capture.from;
            sb.append(Board.squareNumberFromPoint(absoluteFrom));
            sb.append("x");
        }
        Point lastAbsoluteTo = onInvertedBoard ? Board.invertPoint(captureSequence.getLast().to) : captureSequence.getLast().to;
        sb.append(Board.squareNumberFromPoint(lastAbsoluteTo));
        return sb.toString();
    }

    public boolean isMandatory() {
        return true;
    }

    public Iterator<Move> iterator() {
        List<Move> moves = new ArrayList<>();
        moves.addAll(captureSequence);
        return moves.iterator();
	}
}
