package fr.univlille;


import java.util.Random;

import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.models.GameModel;

public class MonsterStrategy implements IMonsterStrategy{
    ICoordinate monsterPosition;

    ICoordinate exit;

    int mazeHeight;
    int mazeWidth;

    int fog; // distance of fog around the monster, he's only supposed to know what is "brouillard" number of blocks around.
    
    boolean[][] knownMaze;
    
    public MonsterStrategy(GameModel game){
        this.monsterPosition = game.getMonster().getPosition();
        this.exit = game.getExit();

        mazeHeight = game.getHeight();
        mazeWidth = game.getWidth();

        knownMaze = new boolean[mazeHeight][mazeWidth];
    }

    @Override
    public ICoordinate play() {
        System.out.println(knownMaze);
        /*
         * Very simple implementation, goes to the direction of the exit if there are no walls where we want to go
         * If exit is above and right of the monster, the monster will go on the block above, to get closer to the exit, assuming no wall is there
         */
        
        Coordinate vel = new Coordinate(monsterPosition.getCol(), monsterPosition.getRow());
        Random random = new Random();
        switch (random.nextInt(4)) {
            case 0:
                vel.setCol(vel.getCol() + 1);
                break;
                case 1:
                vel.setCol(vel.getCol() - 1);
                break;
                case 2:
                vel.setRow(vel.getRow() + 1);
                break;
                case 3:
                vel.setRow(vel.getRow() + 1);
                break;
            default:
                break;
        }
        
        return vel;
    }

    @Override
    public void update(ICellEvent event) { //sets a newly discovered walkable block to "true"
        if (event.getState() == CellInfo.EMPTY) {
            knownMaze[event.getCoord().getRow()][event.getCoord().getCol()] = true;
        }
    }

    @Override
    public void initialize(boolean[][] arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
    
}