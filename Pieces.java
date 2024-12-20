// I certify, that this computer program submitted by me is all of my own work. Signed: Elisha Bjerkeset
//Bjerkeset CSC 322 Final Project

/*
 * Features:
 * Serilization
 * Overriding the equals() method
 */

import java.awt.*;
import java.io.Serializable;

public class Pieces implements Serializable {
    private String side;
    private Point piecePoint;
    private boolean king;

    public Pieces(String side, Point piecesPoint) {
        this.side = side;
        this.piecePoint = piecesPoint;
        this.king = false;
    }

    public String getSide() {
        return side;
    }

    public Point getPiecePoint() {
        return piecePoint;
    }

    public boolean getKing() {
        return this.king;
    }

    public void setKing(boolean label) {
        this.king = label;
    }

    public void setPiecePoint(Point newPoint) {
        piecePoint = newPoint;
    }

    //Seeing if the mouse click was in the piece's box
    public boolean contains(Point point) {
        int x = (int)piecePoint.getX();
        int y = (int)piecePoint.getY();
        return (point.getX() >= x && point.getX() <= x + 100 && point.getY() >= y && point.getY() <= y + 100);
    }

    public void move(int deltaX, int deltaY) {
        piecePoint.translate(deltaX, deltaY);
    }

    @Override
    public boolean equals(Object o) {
        if(this.side.equals(((Pieces) o).getSide())) {
        }
        else {
            return false;
        }
        if(this.piecePoint.equals(((Pieces) o).getPiecePoint())) {
        }
        else {
            return false;
        }
        if(this.king == ((Pieces) o).getKing()) {
        }
        else {
            return false;
        }
        return true;
    }
}