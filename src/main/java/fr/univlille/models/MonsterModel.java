package fr.univlille.models;

import fr.univlille.CellEvent;
import fr.univlille.Coordinate;
import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class MonsterModel implements IMonsterStrategy{

    Coordinate position;
    public GameModel model;

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
        if(model.getMonster().isMonsterMovementValid(movePosition)) {
            model.incrementTurn();
            model.getMonster().move(movePosition);
            model.addToHistory(new CellEvent(new Coordinate(movePosition.getCol(), movePosition.getRow()), CellInfo.MONSTER, model.getTurn()));
            return true;
        }
        return false;
    }

    @Override
    public ICoordinate play() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'play'");
    }

    @Override
    public void update(ICellEvent arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void initialize(boolean[][] arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }

        /**
     * Makes sure that the given movement is valid for the monster.
     * The monster cannot move outside of the maze.
     * It cannot move into a wall.
     * It cannot jump cells.
     * @param movement The desired movement of the monster.
     * @return `true` if the movement is valid, `false` otherwise.
     */
    public boolean isMonsterMovementValid(Coordinate movement) {
        Coordinate mazeDimensions = model.getMazeDimensions();

        // On vérifie déjà si le déplacement est dans la grille du jeu
        if(movement.getCol() < 0 || movement.getCol() > mazeDimensions.getCol() || movement.getRow() < 0 || movement.getRow() > mazeDimensions.getRow()) {
            return false;
        }
        
        double distance = model.getMonster().getPosition().distance(movement);
        if(distance > 1.0 || distance < 1.0) {
            return false;
        }

        // Et si le déplacement n'est pas dans un obstacle
        if(model.isWallAt(movement.getCol(), movement.getRow())) {
            return false;
        }
        return true;
    }
}
