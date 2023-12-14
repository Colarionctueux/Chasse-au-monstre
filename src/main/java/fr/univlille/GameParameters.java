package fr.univlille;

public class GameParameters {
    private int mazeWidth;
    private int mazeHeight;

    private int hunterShoots;
    private int hunterGrenades;

    private double wallsPercentage;

    private boolean fogOfWar;
    private int fogOfWarRadius = 3;
    
    public int getMazeWidth() {
        return mazeWidth;
    }
    public void setMazeWidth(int mazeWidth) {
        this.mazeWidth = mazeWidth;
    }
    public int getMazeHeight() {
        return mazeHeight;
    }
    public void setMazeHeight(int mazeHeight) {
        this.mazeHeight = mazeHeight;
    }
    public int getHunterShoots() {
        return hunterShoots;
    }
    public void setHunterShoots(int hunterShoots) {
        this.hunterShoots = hunterShoots;
    }
    public int getHunterGrenades() {
        return hunterGrenades;
    }
    public void setHunterGrenades(int hunterGrenades) {
        this.hunterGrenades = hunterGrenades;
    }
    public double getWallsPercentage() {
        return wallsPercentage;
    }
    public void setWallsPercentage(double wallsPercentage) {
        this.wallsPercentage = wallsPercentage;
    }
    public boolean isFogOfWar() {
        return fogOfWar;
    }
    public void setFogOfWar(boolean fogOfWar) {
        this.fogOfWar = fogOfWar;
    }
    public int getFogOfWarRadius() {
        return fogOfWarRadius;
    }
    public void setFogOfWarRadius(int fogOfWarRadius) {
        this.fogOfWarRadius = fogOfWarRadius;
    }
}
