package fr.univlille;

import java.util.ArrayList;
import java.util.Random;

import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.models.GameModel;

public class MonsterStrategy implements IMonsterStrategy {
    GameModel model;

    public MonsterStrategy(GameModel model) {
        this.model = model;
    }

    @Override
    public ICoordinate play() {
        ArrayList<ICoordinate> directions = new ArrayList<>();

        


        ICoordinate monsterPosition = model.getMonster().getPosition();

        Random random = new Random();
        if(!model.isWallAt(monsterPosition.getCol() + 1, monsterPosition.getRow())) directions.add(new Coordinate(1, 0));
        if(!model.isWallAt(monsterPosition.getCol() - 1, monsterPosition.getRow())) directions.add(new Coordinate(-1, 0));
        if(!model.isWallAt(monsterPosition.getCol(), monsterPosition.getRow() + 1)) directions.add(new Coordinate(0, 1));
        if(!model.isWallAt(monsterPosition.getCol(), monsterPosition.getRow() - 1)) directions.add(new Coordinate(0, -1));
        
        
        ICoordinate direction = directions.get(random.nextInt(directions.size()));

        return new Coordinate(
            monsterPosition.getCol() + direction.getCol(),
            monsterPosition.getRow() + direction.getRow()
        );
    }


    @Override
    public void initialize(boolean[][] arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }

    @Override
    public void update(ICellEvent arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}