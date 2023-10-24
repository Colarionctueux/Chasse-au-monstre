package fr.univlille;

import java.util.ArrayList;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent;

/**
 * <strong> </strong>
 * @author Gysemans Thomas
 * @author Leclercq Manon
 * @author Eckman Nicolas
 * @author Tourneur Aymeri
 * @author Belguebli Rayane
 */

public class Game{

    private int mazeWidth;
    private int mazeHeight;
    private boolean[][] maze;
    private Vector2i exit;
    private HunterModel hunter;
    private MonsterModel monster;
    private ArrayList<ICellEvent> history;

    public void generateMaze(){
        
    }
}