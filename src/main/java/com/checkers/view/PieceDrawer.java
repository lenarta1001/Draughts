package com.checkers.view;

import java.awt.*;
import java.awt.geom.Path2D;

import com.checkers.model.colour.Colour;
import com.checkers.model.piece.*;

public class PieceDrawer {
    private Graphics2D g2;
    private int x;
    private int y;
    private int size;

    public PieceDrawer(Graphics2D g2) {
        this.g2 = g2;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void drawPiece(Piece piece) {
        boolean isBlack = piece.getColour() == Colour.black;
        Color mainColor = isBlack ? new Color(40, 40, 40) : new Color(220, 220, 220);
        Color highlight = isBlack ? new Color(80, 80, 80) : new Color(255, 255, 255);
        Color border = isBlack ? Color.BLACK : Color.GRAY;

        int padding = size / 10;
        int pieceSize = size - (2 * padding);
        int pieceX = x + padding;
        int pieceY = y + padding;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(border);
        g2.fillOval(pieceX, pieceY, pieceSize, pieceSize);

        GradientPaint gp = new GradientPaint(pieceX, pieceY, highlight, pieceX + pieceSize, pieceY + pieceSize, mainColor);
        g2.setPaint(gp);
        g2.fillOval(pieceX + 2, pieceY + 2, pieceSize - 4, pieceSize - 4);

        g2.setColor(border);
        g2.drawOval(pieceX + (pieceSize/4), pieceY + (pieceSize/4), pieceSize/2, pieceSize/2);
    }

    public void drawKing(King king) {
        drawPiece(king);
        boolean isBlack = king.getColour() == Colour.black;

        g2.setColor(isBlack ? new Color(255, 215, 0) : new Color(200, 0, 0));
        
        int cx = x + size / 2;
        int cy = y + size / 2;
        int crownWidth = size / 3;
        int crownHeight = size / 4;

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

}
