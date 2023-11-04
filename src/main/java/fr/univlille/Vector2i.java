package fr.univlille;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

/**
 * Defines coordinates and allows us to calculate distances between two positions.
 * The name is inspired by the Godot Engine.
 * A `Vector2` holds two doubles: X and Y.
 * A `Vector2i` holds two integers: X and Y.
 * It represents coordinates in a game.
 * 
 * As it implements "ICoordinate",
 * to get the X coordinate, use "getCol()",
 * and to get the Y coordinate, use "getRow()".
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
