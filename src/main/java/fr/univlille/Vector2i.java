package fr.univlille;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class Vector2i implements ICoordinate {
    private int x;
    private int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
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

    
}
