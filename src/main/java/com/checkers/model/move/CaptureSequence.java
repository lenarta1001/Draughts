package com.checkers.model.move;

import java.awt.Point;
import java.util.List;

import com.checkers.model.board.*;
import com.checkers.model.game.Game;
import com.checkers.view.GamePanel;

public class CaptureSequence extends Capture {
    protected List<Capture> captureSequence;

    public CaptureSequence(List<Capture> captureSequence, boolean onInvertedBoard) {
        super(captureSequence.getFirst().from, captureSequence.getLast().to, onInvertedBoard);
        this.captureSequence = captureSequence;
    }

    public List<Capture> getCaptureSequence() {
        return captureSequence;
    }

    public void execute(Game game) {
        game.executeCaptureSequence(this);
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

    public boolean equals(Object o) {
        if (this == o) { 
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaptureSequence other = (CaptureSequence) o;
        return from.equals(other.from) && to.equals(other.to) 
            && onInvertedBoard == other.onInvertedBoard && captureSequence.equals(other.captureSequence);
    }

    public void draw(GamePanel gp) {
        gp.drawCaptureSequence(this);
    }
}
