package fr.univlille;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

/**
 * <strong> </strong>
 * @author Gysemans Thomas
 * @author Leclercq Manon
 * @author Eckman Nicolas
 * @author Tourneur Aymeri
 * @author Belguebli Rayane
 */

public class Vector2i implements ICoordinate {
    private int x;
    private int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2i(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public int getCol() {
        return this.x;
    }


    @Override
    public int getRow() {
        return this.y;
    }


    public double distance(Vector2i other) {
        return Math.sqrt(Math.pow((getCol() - other.getCol()), 2) + Math.pow((getRow() - other.getRow()), 2));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        Vector2i other = (Vector2i) obj;
        return other.getCol() == getCol() && other.getRow() == getRow();
    }
    
    @Override
    public String toString() {
        return "(" + getCol() + ", " + getRow() + ")";
    }
}
