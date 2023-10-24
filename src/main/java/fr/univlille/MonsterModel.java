package fr.univlille;
import java.util.Scanner;

import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

/**
 * <strong> </strong>
 * @author Gysemans Thomas
 * @author Leclercq Manon
 * @author Eckman Nicolas
 * @author Tourneur Aymeri
 * @author Belguebli Rayane
 */

public class MonsterModel implements IMonsterStrategy{

    Vector2i position;

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
        return (ICoordinate) new Vector2i(x, y);
    }

    @Override
    public void update(ICellEvent arg0) {
        if(arg0.equals(CellInfo.EMPTY)){
            System.out.println("case vide");
        }
        else if(arg0.equals(CellInfo.EXIT)){
            System.out.println("case sortie");
        }
        else if(arg0.equals(CellInfo.HUNTER)){
            System.out.println("case du chasseur");
        }
        else if(arg0.equals(CellInfo.MONSTER)){
            System.out.println("case du monstre");
        }
        else if(arg0.equals(CellInfo.WALL)){
            System.out.println("case d'un mur");
        }
    }

    @Override
    public void initialize(boolean[][] arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
}
