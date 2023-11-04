package fr.univlille.ihm.models;
import java.util.ArrayList;
import java.util.Scanner;

import fr.univlille.Coordinate;
import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class HunterModel implements IHunterStrategy{
    public ArrayList<ICellEvent> shootsHistory;

    public HunterModel() {
        shootsHistory = new ArrayList<>();
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
    public void initialize(int arg0, int arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
}
