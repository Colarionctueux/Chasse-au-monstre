package fr.univlille;

import java.util.ArrayList;
import java.util.Random;


import fr.univlille.ihm.models.HunterModel;
import fr.univlille.ihm.models.MonsterModel;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

/**
 * <strong> </strong>
 * @author Gysemans Thomas
 * @author Leclercq Manon
 * @author Eckman Nicolas
 * @author Tourneur Aymeri
 * @author Belguebli Rayane
 */

public class Game{

    private int turn;
    private int mazeWidth;
    private int mazeHeight;
    private boolean[][] maze;
    private Vector2i exit;
    private HunterModel hunter;

    public HunterModel getHunter() {
        return hunter;
    }

    private MonsterModel monster;
    private ArrayList<ICellEvent> history;

    public boolean gameEnded;


    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public MonsterModel getMonster() {
        return monster;
    }

    public boolean isWallAt(int x, int y) {
        return maze[y][x];
    }

    public boolean monsterWon() {
        return getMonster().getPosition().equals(exit);
    }
    
    public boolean isWallAt(Vector2i vector2i) {
        return maze[vector2i.getRow()][vector2i.getCol()];
    }

    public Vector2i getMazeDimensions() {
        return new Vector2i(mazeWidth, mazeHeight);
    }

    public Vector2i getExit() {
        return exit;
    }

    public Vector2i randomPosition() {
        Random random = new Random();
        return new Vector2i(Math.floor(random.nextInt(mazeWidth / 2)) * 2 + 1, Math.floor(random.nextInt(mazeHeight / 2)) * 2 + 1);
    }

    public void generateMaze(int width, int height){
        this.mazeWidth = width;
        this.mazeHeight = height;

        this.hunter = new HunterModel();
        this.monster = new MonsterModel(randomPosition());

        this.turn = 1;
        this.history = new ArrayList<>();

        exit = randomPosition();
        while(exit.equals(getMonster().getPosition())) {
            exit = randomPosition();
        }

        maze = new boolean[mazeHeight][mazeWidth];
        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                if(x % 2 == 0 && y % 2 == 0 || x == 0 || x == mazeWidth - 1 || y == 0 || y == mazeHeight - 1) {
                    maze[y][x] = true;
                }
            }
        }
    }

    public int getTurn() {
        return turn;
    }

    public void incrementTurn() {
        this.turn += 1;
    }

    public ICellEvent play(Vector2i shootPosition) {
        CellInfo state = CellInfo.EMPTY;
        if(isWallAt(shootPosition)) {
            state = CellInfo.WALL;
        }
        for (ICellEvent cellEvent : history) {
            if(cellEvent.getCoord().equals(shootPosition)) {
                getHunter().shootsHistory.add(cellEvent);
                return cellEvent;
            }
        }
        CellEvent cellEvent = new CellEvent(shootPosition, state, turn);

        // remove other shoots history with the same position
        for (int i = getHunter().shootsHistory.size() - 1; i > 0; i--) {
            if(getHunter().shootsHistory.get(i).getCoord().equals(shootPosition)) {
                getHunter().shootsHistory.remove(i);
            }
        }
        getHunter().shootsHistory.add(cellEvent);
        return cellEvent;
    }

    public ArrayList<ICellEvent> getHistory() {
        return history;
    }

    public void addToHistory(ICellEvent cellEvent) {
        history.add(cellEvent);
    }
}