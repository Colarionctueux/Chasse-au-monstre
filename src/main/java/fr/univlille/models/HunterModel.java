package fr.univlille.models;
import java.util.ArrayList;

import fr.univlille.CellEvent;
import fr.univlille.Coordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.utils.Subject;

public class HunterModel extends Subject {
    public ArrayList<ICellEvent> shootsHistory;
    public GameModel gameModel;

    public int shootLeft = 1;
    public int grenadeLeft = 1;
    public boolean grenade = false;

    public HunterModel(GameModel gameModel) {
        this.gameModel = gameModel;
        shootsHistory = new ArrayList<>();
    }

    public void turnBegin() {
        shootLeft = 1;
    }

    /**
     * Makes sure that the given hunter's target is valid.
     * The hunter cannot shoot outside of the maze and cannot shoot the borders.
     * @param shoot The target's position.
     * @return `true` if the target's position is valid, `false` otherwise.
     */
    public boolean isHunterShootValid(Coordinate shoot) {
        Coordinate mazeDimensions = gameModel.getMazeDimensions();
        return shoot.getCol() >= 0 && shoot.getCol() < mazeDimensions.getCol() && shoot.getRow() >= 0 && shoot.getRow() < mazeDimensions.getRow();
    }

    /**
     * Gets information about the cell that the hunter is targeting.
     * @param shootPosition The coordinates of the hunter's target.
     * @return The type of cell that the hunter has shot.
     */
    public void shoot(Coordinate shootPosition) {
        CellInfo state = CellInfo.EMPTY;
        if (gameModel.isWallAt(shootPosition)) {
            state = CellInfo.WALL;
        }

        for (ICellEvent cellEvent : gameModel.getHistory()) {
            if (cellEvent.getCoord().equals(shootPosition)) {
                shootsHistory.add(cellEvent);
                notifyObservers(cellEvent);
                return;
            }
        }
        CellEvent cellEvent = new CellEvent(shootPosition, state, gameModel.getTurn());

        // remove other shoots history with the same position
        for (int i = shootsHistory.size() - 1; i > 0; i--) {
            if (shootsHistory.get(i).getCoord().equals(shootPosition)) {
                shootsHistory.remove(i);
            }
        }
        shootsHistory.add(cellEvent);
        shootLeft -= 1;
        notifyObservers(cellEvent);
    }

    public boolean isHunterGrenadeValid(Coordinate grenade) {
        boolean res = true;
        Coordinate mazeDimensions = gameModel.getMazeDimensions();
        if(isHunterShootValid(grenade) == false){
            res = false;
        }
        if(grenade.getCol()+1 <= 0 && grenade.getCol()+1 < mazeDimensions.getCol() && grenade.getRow() >= 0 && grenade.getRow() < mazeDimensions.getRow()){
            res = false;
        }
        if(grenade.getCol()-1 <= 0 && grenade.getCol()-1 < mazeDimensions.getCol() && grenade.getRow() >= 0 && grenade.getRow() < mazeDimensions.getRow()){
            res = false;
        }
        if(grenade.getCol() >= 0 && grenade.getCol() < mazeDimensions.getCol() && grenade.getRow()+1 >= 0 && grenade.getRow()+1 < mazeDimensions.getRow()){
            res = false;
        }
        if(grenade.getCol() >= 0 && grenade.getCol() < mazeDimensions.getCol() && grenade.getRow()-1 >= 0 && grenade.getRow()-1 < mazeDimensions.getRow()){
            res = false;
        }
        return res;
    }

    public void grenade(Coordinate greandePosition){
        shoot(greandePosition);
        shoot(new Coordinate(greandePosition.getCol()+1, greandePosition.getRow()));
        shoot(new Coordinate(greandePosition.getCol()-1, greandePosition.getRow()));
        shoot(new Coordinate(greandePosition.getCol(), greandePosition.getRow()+1));
        shoot(new Coordinate(greandePosition.getCol(), greandePosition.getRow()-1));
    }
}
