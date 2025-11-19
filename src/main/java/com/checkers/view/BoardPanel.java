package com.checkers.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.Color;

import javax.swing.JPanel;

import com.checkers.control.GameController;
import com.checkers.model.board.Board;
import com.checkers.model.game.Game;

public class BoardPanel extends JPanel implements PropertyChangeListener {
    private transient Game model;

    public BoardPanel(Game model, GameController gameController) {
        this.model = model;
        addMouseListener(gameController);
        model.addPropertyChangeListener(this);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);
        int squareSize = size / 8; 

        int xOffset = (w - (squareSize * 8)) / 2;
        int yOffset = (h - (squareSize * 8)) / 2;

        g2.translate(xOffset, yOffset);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 1) {
                    g2.setColor(new Color(139, 69, 19));
                    g2.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
                } else {
                    g2.setColor(new Color(245, 222, 179));
                    g2.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
                }
            }
        }

        for (int i = 1; i <= 32; i++) {
            Point p = Board.pointFromSquareNumber(i);
            if (!model.getBoard().isEmpty(p)) {
                PieceDrawer.drawPiece(g2, p.x * squareSize, p.y * squareSize, squareSize, model.getBoard().getPiece(p));
            }
            if (model.getSelectedPoint() != null) {
                boolean canMoveHere = model.validMovesAt(model.getSelectedPoint()).stream().anyMatch(move -> move.getTo().equals(p));
                if (canMoveHere) {
                    g2.setColor(new Color(50, 205, 50, 150)); 

                    int dotSize = squareSize / 3; 

                    int dotX = (p.x * squareSize) + (squareSize - dotSize) / 2;
                    int dotY = (p.y * squareSize) + (squareSize - dotSize) / 2;
  
                    g2.fillOval(dotX, dotY, dotSize, dotSize);
                }
            }
        }

        g2.translate(-xOffset, -yOffset);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("boardChange")) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                this.repaint();
            });
        }
    }
}
