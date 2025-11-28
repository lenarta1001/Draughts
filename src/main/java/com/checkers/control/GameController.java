package com.checkers.control;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.checkers.model.game.Game;
import com.checkers.model.move.Move;
import com.checkers.view.GamePanel;


public class GameController extends MouseAdapter {
    private Game model;

    public GameController(Game model) {
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (model.isGameOver() || model.isDraw()) {
            return;
        }

        GamePanel panel = (GamePanel) e.getSource();

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
        if (panel.getSelectedPoint() == null) {
            if (!model.getBoard().isEmpty(p) && model.getBoard().getPiece(p).getColour() == model.getPlayerToMove().getColour()
                && !model.getPlayerToMove().validMovesAt(model, p).isEmpty()) {
                    panel.setSelectedPoint(p);
            }
        } else {
            Move moveToTake = model.getPlayerToMove().validMovesAt(model, panel.getSelectedPoint()).stream().filter(move -> move.getTo().equals(p)).findFirst().orElse(null);
            if (moveToTake == null) {
                Move otherMove = model.getPlayerToMove().validMoves(model).stream().filter(move -> move.getFrom().equals(p)).findFirst().orElse(null);
                if (otherMove != null) {
                    panel.setSelectedPoint(otherMove.getFrom());
                } else {
                    panel.setSelectedPoint(null);
                }
                return;
            }
            new Thread(() -> {
                model.getPlayerToMove().handleTurn(model, moveToTake);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
                model.getPlayerNotToMove().onOpponentTurnCompleted(model); 
            }).start();
            panel.setSelectedPoint(null);
        }   
    }
}
