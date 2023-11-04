package fr.univlille.ihm.views;

import fr.univlille.Coordinate;
import javafx.scene.canvas.GraphicsContext;

public class ViewUtils {
    public static void drawSimpleTexture(GraphicsContext gc, Coordinate spritesheetPosition, Coordinate gamePosition) {
        gc.drawImage(
            GameView.spritesheet, spritesheetPosition.getCol(), spritesheetPosition.getRow(), 64, 64,
            gamePosition.getCol() * GameView.TILE_SIZE,
            gamePosition.getRow() * GameView.TILE_SIZE,
            GameView.TILE_SIZE, GameView.TILE_SIZE
        );
    }

    public static void drawSimpleTexture(GraphicsContext gc, int x, int y, Coordinate gamePosition) {
        gc.drawImage(
            GameView.spritesheet, x, y, 64, 64,
            gamePosition.getCol() * GameView.TILE_SIZE,
            gamePosition.getRow() * GameView.TILE_SIZE,
            GameView.TILE_SIZE, GameView.TILE_SIZE
        );
    }
    
    public static void drawSimpleTexture(GraphicsContext gc, Coordinate spritesheetPosition, int x, int y) {
        gc.drawImage(
            GameView.spritesheet, spritesheetPosition.getCol(), spritesheetPosition.getRow(), 64, 64,
            x * GameView.TILE_SIZE,
            y * GameView.TILE_SIZE,
            GameView.TILE_SIZE, GameView.TILE_SIZE
        );
    }

    public static void drawSimpleTexture(GraphicsContext gc, int sx, int sy, int gx, int gy) {
        gc.drawImage(
            GameView.spritesheet, sx, sy, 64, 64,
            gx * GameView.TILE_SIZE,
            gy * GameView.TILE_SIZE,
            GameView.TILE_SIZE, GameView.TILE_SIZE
        );
    }   
}
