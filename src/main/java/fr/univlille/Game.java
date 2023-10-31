package fr.univlille;

import java.util.ArrayList;

import fr.univlille.ihm.models.HunterModel;
import fr.univlille.ihm.models.MonsterModel;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;

/**
 * <strong> </strong>
 * @author Gysemans Thomas
 * @author Leclercq Manon
 * @author Eckman Nicolas
 * @author Tourneur Aymeri
 * @author Belguebli Rayane
 */

public class Game{

    private int mazeWidth;
    private int mazeHeight;
    private boolean[][] maze;
    private Vector2i exit;
    private HunterModel hunter;
    private MonsterModel monster;
    private ArrayList<ICellEvent> history;


    public MonsterModel getMonster() {
        return monster;
    }

    public boolean isWallAt(int x, int y) {
        return maze[y][x];
    }

    public Vector2i getMazeDimensions() {
        return new Vector2i(mazeWidth, mazeHeight);
    }

    public Vector2i getExit() {
        return exit;
    }

    public void generateMaze(int width, int height){

        this.hunter = new HunterModel(new Vector2i(1, 1));
        this.monster = new MonsterModel(new Vector2i(1, 1));
        this.history = new ArrayList<>();

        this.mazeWidth = width;
        this.mazeHeight = height;

        exit = new Vector2i(this.mazeWidth - 2, this.mazeHeight - 2);

        maze = new boolean[mazeHeight][mazeWidth];
        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                if(x % 2 == 0 && y % 2 == 0 || x == 0 || x == mazeWidth - 1 || y == 0 || y == mazeHeight - 1) {
                    maze[y][x] = true;
                }
            }
        }
    }
}