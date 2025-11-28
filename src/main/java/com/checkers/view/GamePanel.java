package com.checkers.view;

import java.awt.*;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.checkers.control.GameController;
import com.checkers.model.board.Board;
import com.checkers.model.colour.Colour;
import com.checkers.model.game.Game;
import com.checkers.model.move.Capture;
import com.checkers.model.move.CaptureSequence;
import com.checkers.model.move.Move;
import com.checkers.model.move.NormalMove;
import com.checkers.model.piece.*;

public class GamePanel extends JPanel implements PropertyChangeListener {
    private transient Game model;
    private Point selectedPoint;
    private Graphics2D g2;
    private int size;
    private int squareSize;

    public GamePanel(Game model, GameController gameController) {
        this.model = model;
        addMouseListener(gameController);
        model.addPropertyChangeListener(this);
    }

    public Point getSelectedPoint() {
        return selectedPoint;
    }
    
    public void setSelectedPoint(Point selectedPoint) {
        this.selectedPoint = selectedPoint;
        javax.swing.SwingUtilities.invokeLater(() -> {
            this.repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        
        int w = getWidth();
        int h = getHeight();
        size = Math.min(w, h);
        squareSize = size / 8; 

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
                model.getBoard().getPiece(p).draw(this, p.x, p.y);
            }
            
            if (selectedPoint != null) {
                Move moveToTake = model.getPlayerToMove().validMovesAt(model, selectedPoint).stream().filter(move -> move.getTo().equals(p)).findFirst().orElse(null);
                if (moveToTake != null) {
                    moveToTake.draw(this);
                }
            }
        }

        g2.translate(-xOffset, -yOffset);
    }

    public void drawChecker(Checker checker, int x, int y) {
        boolean isBlack = checker.getColour() == Colour.black;
        Color mainColor = isBlack ? new Color(40, 40, 40) : new Color(220, 220, 220);
        Color highlight = isBlack ? new Color(80, 80, 80) : new Color(255, 255, 255);
        Color border = isBlack ? Color.BLACK : Color.GRAY;

        int padding = squareSize / 10;
        int pieceSize = squareSize - (2 * padding);
        int pieceX = x * squareSize + padding;
        int pieceY = y * squareSize + padding;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(border);
        g2.fillOval(pieceX, pieceY, pieceSize, pieceSize);

        GradientPaint gp = new GradientPaint(pieceX, pieceY, highlight, pieceX + pieceSize, pieceY + pieceSize, mainColor);
        g2.setPaint(gp);
        g2.fillOval(pieceX + 2, pieceY + 2, pieceSize - 4, pieceSize - 4);

        g2.setColor(border);
        g2.drawOval(pieceX + (pieceSize/4), pieceY + (pieceSize/4), pieceSize/2, pieceSize/2);
    }

    public void drawKing(King king, int x, int y) {
        boolean isBlack = king.getColour() == Colour.black;

        Color mainColor = isBlack ? new Color(40, 40, 40) : new Color(220, 220, 220);
        Color highlight = isBlack ? new Color(80, 80, 80) : new Color(255, 255, 255);
        Color border = isBlack ? Color.BLACK : Color.GRAY;

        int padding = squareSize / 10;
        int pieceSize = squareSize - (2 * padding);
        int pieceX = x * squareSize + padding;
        int pieceY = y * squareSize + padding;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(border);
        g2.fillOval(pieceX, pieceY, pieceSize, pieceSize);

        GradientPaint gp = new GradientPaint(pieceX, pieceY, highlight, pieceX + pieceSize, pieceY + pieceSize, mainColor);
        g2.setPaint(gp);
        g2.fillOval(pieceX + 2, pieceY + 2, pieceSize - 4, pieceSize - 4);

        g2.setColor(border);
        g2.drawOval(pieceX + (pieceSize/4), pieceY + (pieceSize/4), pieceSize/2, pieceSize/2);

        g2.setColor(isBlack ? new Color(255, 215, 0) : new Color(200, 0, 0));
        
        int cx = x + squareSize / 2;
        int cy = y + squareSize / 2;
        int crownWidth = squareSize / 3;
        int crownHeight = squareSize / 4;

        Path2D.Double crown = new Path2D.Double();
        crown.moveTo(cx - crownWidth/2.0, cy + crownHeight/4.0);
        crown.lineTo(cx - crownWidth/2.0, cy - crownHeight/2.0);
        crown.lineTo(cx - crownWidth/6.0, cy - crownHeight/4.0);
        crown.lineTo(cx, cy - crownHeight/1.5);
        crown.lineTo(cx + crownWidth/6.0, cy - crownHeight/4.0);
        crown.lineTo(cx + crownWidth/2.0, cy - crownHeight/2.0);
        crown.lineTo(cx + crownWidth/2.0, cy + crownHeight/4.0);
        crown.closePath();

        g2.fill(crown);
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("boardChange")) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                this.repaint();
            });
        }
    }

    public void drawCaptureSequence(CaptureSequence captureSequence) {
        for (int i = 1; i < captureSequence.getCaptureSequence().size(); i++) {
            Capture capture = captureSequence.getCaptureSequence().get(i);
                    
            g2.setColor(new Color(40, 40, 40, 100));
            g2.setStroke(new BasicStroke(3));

            int startX = capture.getFrom().x * squareSize + squareSize / 4;
            int startY = capture.getFrom().y * squareSize + squareSize / 4;
            int endX = capture.getFrom().x * squareSize + squareSize * 3 / 4;
            int endY = capture.getFrom().y * squareSize + squareSize * 3 / 4;

            g2.drawLine(startX, startY, endX, endY);
            g2.drawLine(startX, endY, endX, startY);
        }

        g2.setColor(new Color(40, 40, 40, 100)); 

        Capture lastCapture = captureSequence.getCaptureSequence().getLast();

        int dotSize = squareSize / 3; 

        int dotX = (lastCapture.getTo().x * squareSize) + (squareSize - dotSize) / 2;
        int dotY = (lastCapture.getTo().y * squareSize) + (squareSize - dotSize) / 2;

        g2.fillOval(dotX, dotY, dotSize, dotSize);
    }

    public void drawNormalMove(NormalMove normalMove) {
        g2.setColor(new Color(40, 40, 40, 100)); 

        int dotSize = squareSize / 3; 

        int dotX = (normalMove.getTo().x * squareSize) + (squareSize - dotSize) / 2;
        int dotY = (normalMove.getTo().y * squareSize) + (squareSize - dotSize) / 2;

        g2.fillOval(dotX, dotY, dotSize, dotSize);
    }

    public void drawCapture(Capture capture) {
        g2.setColor(new Color(40, 40, 40, 100)); 

        int dotSize = squareSize / 3; 

        int dotX = (capture.getTo().x * squareSize) + (squareSize - dotSize) / 2;
        int dotY = (capture.getTo().y * squareSize) + (squareSize - dotSize) / 2;

        g2.fillOval(dotX, dotY, dotSize, dotSize);
    }
}
