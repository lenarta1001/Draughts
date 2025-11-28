package com.checkers.model.move;

import java.util.List;

import com.checkers.model.board.*;
import com.checkers.model.game.Game;
import com.checkers.view.GamePanel;

public class CaptureSequence extends Capture {
    protected List<Capture> captures;

    public CaptureSequence(List<Capture> captures) {
        super(captures.getFirst().from, captures.getLast().to);
        this.captures = captures;
    }

    public List<Capture> getCaptures() {
        return captures;
    }

    @Override
    public void execute(Game game) {
        game.executeCaptureSequence(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Capture capture : captures) {
            sb.append(Board.squareNumberFromPoint(capture.from));
            sb.append("x");
        }
        sb.append(Board.squareNumberFromPoint(captures.getLast().to));
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
        return captures.equals(other.captures);
    }

    @Override
    public void draw(GamePanel gp) {
        gp.drawCaptureSequence(this);
    }
}
