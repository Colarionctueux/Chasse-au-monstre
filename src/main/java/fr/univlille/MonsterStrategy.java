package fr.univlille;


import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.models.GameModel;

public class MonsterStrategy implements IMonsterStrategy{
    int monsterCol;
    int monsterRow;

    int exitCol;
    int exitRow;

    int mazeHeight;
    int mazeWidth;

    int fog; // distance of fog around the monster, he's only supposed to know what is "brouillard" number of blocks around.
    
    boolean[][] knownMaze;
    
    public MonsterStrategy(GameModel game){
        this.monsterRow = game.getMonster().getPosition().getRow();
        this.monsterCol = game.getMonster().getPosition().getCol();

        mazeHeight = game.getHeight();
        mazeWidth = game.getWidth();

        initialize(knownMaze);

        this.exitRow = game.getExit().getRow();
        this.exitCol = game.getExit().getCol();
    }

    @Override
    public ICoordinate play() {
        /*
         * Very simple implementation, goes to the direction of the exit if there are no walls where we want to go
         * If exit is above and right of the monster, the monster will go on the block above, to get closer to the exit, assuming no wall is there
         */
        
        ICoordinate toPlay = null;

        if (exitCol<monsterCol && knownMaze[monsterRow][monsterCol-1]){ 
            toPlay = new Coordinate(monsterRow,monsterCol-1);
        }

        if (exitCol>monsterCol && knownMaze[monsterRow][monsterCol+1]){
            toPlay = new Coordinate(monsterRow,monsterCol+1);
        }

        if (exitRow<monsterRow && knownMaze[monsterRow-1][monsterCol]){
            toPlay = new Coordinate(monsterRow-1,monsterCol);
        }
        
        if (exitCol>monsterCol && knownMaze[monsterRow+1][monsterCol]){
            toPlay = new Coordinate(monsterRow+1,monsterCol);
        }
        
        return toPlay;
    }

    @Override
    public void update(ICellEvent event) { //sets a newly discovered walkable block to "true"
        if (event.getState() == CellInfo.EMPTY) {
            knownMaze[event.getCoord().getRow()][event.getCoord().getCol()] = true;
        }
    }
    
    @Override
    public void initialize(boolean[][] maze) { //initializes knownMaze with only false
        maze = new boolean[mazeHeight][mazeWidth];
    }
}