package fr.univlille.models;
import java.util.ArrayList;

import fr.univlille.CellEvent;
import fr.univlille.Coordinate;
import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class HunterModel implements IHunterStrategy{
    public ArrayList<ICellEvent> shootsHistory;
    public GameModel gameModel;

    public HunterModel(GameModel gameModel) {
        this.gameModel = gameModel;
        shootsHistory = new ArrayList<>();
    }

    /**
     * Makes sure that the given hunter's target is valid.
     * The hunter cannot shoot outside of the maze and cannot shoot the borders.
     * @param shoot The target's position.
     * @return `true` if the target's position is valid, `false` otherwise.
     */
    public boolean isHunterShootValid(Coordinate shoot) {
        Coordinate mazeDimensions = gameModel.getMazeDimensions();
        return shoot.getCol() > 0 && shoot.getCol() < mazeDimensions.getCol() - 1 && shoot.getRow() > 0 && shoot.getRow() < mazeDimensions.getRow() - 1;
    }

    /**
     * Gets information about the cell that the hunter is targeting.
     * @param shootPosition The coordinates of the hunter's target.
     * @return The type of cell that the hunter has shot.
     */
    public ICellEvent shoot(Coordinate shootPosition) {
        CellInfo state = CellInfo.EMPTY;
        if (gameModel.isWallAt(shootPosition)) {
            state = CellInfo.WALL;
        }

        for (ICellEvent cellEvent : gameModel.getHistory()) {
            if (cellEvent.getCoord().equals(shootPosition)) {
                shootsHistory.add(cellEvent);
                return cellEvent;
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
        return cellEvent;
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
    public void initialize(int arg0, int arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
}
