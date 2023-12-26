package fr.univlille;
import java.util.HashMap;

import fr.univlille.models.GameModel;

public class Save {
    

    private HashMap<String,GameModel> saveParties = new HashMap<>();

    public void addSave(String name, GameModel model){
        saveParties.put(name, model);
    }

    public GameModel getSave(String name){
        return saveParties.get(name);
    }
}
