package fr.univlille.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        try{
            Scanner scanner = new Scanner(new File("src/main/resources/maze/maze.csv"));
            int lignes = 0;
            int colonnes = 0;

            while(scanner.hasNextLine()) {
                lignes ++;
                String ligne = scanner.nextLine();
                String[] valeurs = ligne.split(",");
                colonnes = valeurs.length;
            }
            scanner = new Scanner(new File("src/main/resources/maze/maze.csv"));
            boolean[][] maze = new boolean[lignes][colonnes];

            for(int i = 0; i< lignes; i++){
                String ligne = scanner.nextLine();
                String[] valeurs = ligne.split(","); 

                for(int j = 0; j<colonnes; j++){
                    if(Integer.parseInt(valeurs[j]) == 2){
                        maze[i][j] = true;
                    }
                    if(Integer.parseInt(valeurs[j]) == 3){
                        maze[i][j] = true;
                    }
                    if(Integer.parseInt(valeurs[j]) == 0){
                        maze[i][j] = true;
                    }
                    else{
                        maze[i][j] = false;
                    }
                }
            }
            for(int k = 0; k< lignes; k++){
                for(int l = 0; l< lignes; l++){
                System.out.println( ""+ maze[k][l]);
                }
                System.out.println("\n");
            }
            scanner.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

            
    }
}
