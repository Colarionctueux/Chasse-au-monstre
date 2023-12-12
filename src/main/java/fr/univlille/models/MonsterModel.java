package fr.univlille.models;

import fr.univlille.CellEvent;
import fr.univlille.Coordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.utils.Subject;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class MonsterModel extends Subject {

    Coordinate position;
    public GameModel model;
    public int superJumpLeft = 1;
    public boolean superJump = false;

    public boolean[][] fogOfWar;


    public MonsterModel(GameModel model, Coordinate startPosition) {
        this.model = model;
        position = startPosition;
    }

    public Coordinate getPosition() {
        return position;
    }

    public ICoordinate move(Coordinate movement) {
        position.setCol(movement.getCol());
        position.setRow(movement.getRow());
        return position;
    }

    public boolean play(Coordinate movePosition) {
        if(superJump == true && superJumpLeft > 0){
            if(isMonsterMovementValid(movePosition, 2.0)) {
            superJumpLeft -= 1;
            changePosition(movePosition);
            return true;
            }
        }
        else if(isMonsterMovementValid(movePosition, 1.0)) {
            changePosition(movePosition);
            return true;
        }
        return false;
    }

    private void changePosition(Coordinate movePosition) {
        model.incrementTurn();
        move(movePosition);
        model.addToHistory(new CellEvent(new Coordinate(movePosition.getCol(), movePosition.getRow()), CellInfo.MONSTER, model.getTurn()));
    }


        /**
     * Makes sure that the given movement is valid for the monster.
     * The monster cannot move outside of the maze.
     * It cannot move into a wall.
     * It cannot jump cells.
     * @param movement The desired movement of the monster.
     * @return `true` if the movement is valid, `false` otherwise.
     */
    public boolean isMonsterMovementValid(Coordinate movement, double max) {
        Coordinate mazeDimensions = model.getMazeDimensions();

        // On vérifie déjà si le déplacement est dans la grille du jeu
        if(movement.getCol() < 0 || movement.getCol() > mazeDimensions.getCol() || movement.getRow() < 0 || movement.getRow() > mazeDimensions.getRow()) {
            return false;
        }
        
        double distance = model.getMonster().getPosition().distance(movement);
        if(distance > max || distance < max) {
            return false;
        }

        // Et si le déplacement n'est pas dans un obstacle
        if(model.isWallAt(movement.getCol(), movement.getRow())) {
            return false;
        }
        return true;
    }

    
}
