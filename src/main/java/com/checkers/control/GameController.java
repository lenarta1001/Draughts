package com.checkers.control;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.checkers.model.game.Game;
import com.checkers.model.move.Move;


public class GameController extends MouseAdapter {
    private Game model;

    public GameController(Game model) {
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JPanel panel = (JPanel) e.getSource();
        
        int w = panel.getWidth();
        int h = panel.getHeight();
        int size = Math.min(w, h);
        int squareSize = size / 8;

        int xOffset = (w - (squareSize * 8)) / 2;
        int yOffset = (h - (squareSize * 8)) / 2;


        int mouseX = e.getX() - xOffset;
        int mouseY = e.getY() - yOffset;

        if (mouseX < 0 || mouseX >= (squareSize * 8) || mouseY < 0 || mouseY >= (squareSize * 8)) {
            return;
        }

        int col = mouseX / squareSize;
        int row = mouseY / squareSize;

        Point p = new Point(col, row);
        if (model.getSelectedPoint() == null) {
            if (!model.getBoard().isEmpty(p) && model.getBoard().getPiece(p).getColour() == model.getPlayerToMove()
                && !model.validMovesAt(p).isEmpty()) {
                    model.setSelectedPoint(p);
            }
        } else {
            Move moveToTake = model.validMovesAt(model.getSelectedPoint()).stream().filter(move -> move.getTo().equals(p)).findFirst().orElse(null);
            if (moveToTake == null) {
                model.setSelectedPoint(null);
                return;
            }
            new Thread(() -> {
                model.execute(moveToTake); 
            }).start();
            model.setSelectedPoint(null);
        }   
    }
}
