package fr.univlille;

import java.util.ArrayList;
import java.util.Random;

import fr.univlille.ihm.models.HunterModel;
import fr.univlille.ihm.models.MonsterModel;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class Game {
    /**
     * The current turn of the game.
     * Initialized at 1 when creating the maze.
     */
    private int turn;

    /**
     * The dimensions of the maze.
     * It defines the "maze" array.
     */
    private int mazeWidth;
    private int mazeHeight;

    /**
     * 1 represents a wall,
     * 0 represents an empty cell.
     * There is no need for other types of cells as they're contained in other variables or in "history".
     */
    private boolean[][] maze;

    /**
     * The coordinates of the exit.
     */
    private ICoordinate exit;

    private HunterModel hunter;
    private MonsterModel monster;

    /**
     * A list containing all the moves of the hunter and the monster.
     * As it stores instances of `ICellEvent` it remembers at which turn one particular move was done,
     * so it's thanks to this variable that we can know at which turn the monster was on a particular cell.
     */
    private ArrayList<ICellEvent> history = new ArrayList<>();

    /**
     * A boolean that stores whether or not the game has finished.
     */
    public boolean gameEnded;

    public HunterModel getHunter() {
        return hunter;
    }

    public MonsterModel getMonster() {
        return monster;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public int getTurn() {
        return turn;
    }

    public void incrementTurn() {
        this.turn += 1;
    }


    /**
     * Checks if a particular cell is a wall or empty.
     * @param x The X coordinate of the given cell.
     * @param y The Y coordinate of the given cell.
     * @return `true` if this cell is a wall, `false` if it's empty.
     */
    public boolean isWallAt(int x, int y) {
        return maze[y][x];
    }

    /**
     * Checks if a particular cell is a wall or empty.
     * @param coordinate The coordinates of the given cell.
     * @return `true` if this cell is a wall, `false` if it's empty.
     */
    public boolean isWallAt(Coordinate coordinate) {
        return isWallAt(coordinate.getRow(), coordinate.getCol());
    }

    /**
     * Checks if the position of the monster matches the position of the exit.
     * @return `true` if the monster has reached the exit, `false` otherwise.
     */
    public boolean monsterWon() {
        return monster.getPosition().equals(exit);
    }
    
    /**
     * Gets the width and height of the maze as an instance of `Coordinate`.
     * @return An instance of `Coordinate` where `x` is the width of the maze and `y` the height.
     */
    public Coordinate getMazeDimensions() {
        return new Coordinate(mazeWidth, mazeHeight);
    }

    /**
     * Gets the position of the exit.
     * @return The exact coordinates of the exit.
     */
    public ICoordinate getExit() {
        return exit;
    }

    /**
     * Gets a random position within the maze.
     * For now, it gives a random position that is not a wall.
     * @return A random position in the maze.
     */
    public Coordinate randomPosition() {
        Random random = new Random();
        return new Coordinate(random.nextInt(mazeWidth / 2) * 2 + 1, random.nextInt(mazeHeight / 2) * 2 + 1);
    }

    /**
     * Generates the maze.
     * It initializes the hunter and monster models.
     * It gives a random position to the monster.
     * The turns start at 1 and the history is cleared.
     * The exit is also randomized.
     * @param width The desired width of the maze.
     * @param height The desired height of the maze.
     */
    public void generateMaze(int width, int height) {
        this.mazeWidth = width;
        this.mazeHeight = height;

        this.hunter = new HunterModel();
        this.monster = new MonsterModel(randomPosition());

        this.turn = 1;
        this.history.clear();

        exit = randomPosition();
        while(exit.equals(getMonster().getPosition())) {
            exit = randomPosition();
        }

        maze = new boolean[mazeHeight][mazeWidth];
        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                // THIS IS SCARY BUT TEMPORARY
                // for now we want the borders of the maze to be walls
                // and we also want every other square to be a wall (exluding the borders).
                if(x % 2 == 0 && y % 2 == 0 || x == 0 || x == mazeWidth - 1 || y == 0 || y == mazeHeight - 1) {
                    maze[y][x] = true;
                }
            }
        }
    }
    
    /**
     * Gets information about the cell that the hunter is targeting.
     * @param shootPosition The coordinates of the hunter's target.
     * @return The type of cell that the hunter has shot.
     */
    public ICellEvent play(Coordinate shootPosition) {
        CellInfo state = CellInfo.EMPTY;
        if (isWallAt(shootPosition)) {
            state = CellInfo.WALL;
        }

        for (ICellEvent cellEvent : history) {
            if (cellEvent.getCoord().equals(shootPosition)) {
                hunter.shootsHistory.add(cellEvent);
                return cellEvent;
            }
        }
        CellEvent cellEvent = new CellEvent(shootPosition, state, turn);

        // remove other shoots history with the same position
        for (int i = hunter.shootsHistory.size() - 1; i > 0; i--) {
            if (hunter.shootsHistory.get(i).getCoord().equals(shootPosition)) {
                hunter.shootsHistory.remove(i);
            }
        }
        hunter.shootsHistory.add(cellEvent);
        return cellEvent;
    }

    public ArrayList<ICellEvent> getHistory() {
        return history;
    }

    public void addToHistory(ICellEvent cellEvent) {
        history.add(cellEvent);
    }
}