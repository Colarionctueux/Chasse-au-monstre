package fr.univlille.ihm.models;
import java.util.Scanner;

import fr.univlille.Coordinate;
import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class MonsterModel implements IMonsterStrategy{

    Coordinate position;

    public MonsterModel(Coordinate startPosition) {
        position = startPosition;
    }

    public Coordinate getPosition() {
        return position;
    }


    public ICoordinate play(Coordinate movement) {
        position.setX(movement.getCol());
        position.setY(movement.getRow());
        return position;
    }

    @Override
    public ICoordinate play() {
        int taille = 0;
        int x = 0;
        int y = 0;
        String coordo = " ";
        while(taille != 2){
            Scanner sc = new Scanner(System.in);
            coordo = sc.toString();
            taille = coordo.length();
            sc.close();
        }
        x = ((int) coordo.charAt(0)) - 'A';
        y = ((int) coordo.charAt(1));
        return (ICoordinate) new Coordinate(x, y);
    }

    @Override
    public void update(ICellEvent event) {
        if(event.getState().equals(CellInfo.EMPTY)){
            System.out.println("case vide");
        }
        else if(event.getState().equals(CellInfo.EXIT)){
            System.out.println("case sortie");
        }
        else if(event.getState().equals(CellInfo.HUNTER)){
            System.out.println("case du chasseur");
        }
        else if(event.getState().equals(CellInfo.MONSTER)){
            System.out.println("case du monstre");
        }
        else if(event.getState().equals(CellInfo.WALL)){
            System.out.println("case d'un mur");
        }
    }

    @Override
    public void initialize(boolean[][] arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
}
