package fr.univlille;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Maze {
    private int tailleX;
    private int tailleY;
    private boolean[][] maze;

    public Maze(int tailleX, int tailleY) {
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        this.maze = new boolean[tailleY][tailleX];
        initializeMaze();
    }

    private void initializeMaze() {
        for (boolean[] row : maze) {
            Arrays.fill(row, true);
        }
    }

    public boolean[][] createMaze() {
        recursiveBacktrack(0, 0);
        return maze;
    }

    private void recursiveBacktrack(int currentX, int currentY) {
        List<int[]> directions = Arrays.asList(new int[]{0, -2}, new int[]{0, 2}, new int[]{-2, 0}, new int[]{2, 0});
        Collections.shuffle(directions);

        for (int[] direction : directions) {
            int newX = currentX + direction[0];
            int newY = currentY + direction[1];

            if (isValidCell(newX, newY) && maze[newY][newX]) {
                maze[currentY + direction[1] / 2][currentX + direction[0] / 2] = false; // Ouvre le mur
                maze[newY][newX] = false; // Marque la nouvelle case comme visitée
                recursiveBacktrack(newX, newY); // Appel récursif pour la nouvelle case
            }
        }
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < tailleX && y >= 0 && y < tailleY;
    }

    public boolean[][] getMaze() {
        return maze;
    }

    public Coordinate getDimensions() {
        return new Coordinate(tailleX, tailleY);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Maze " + tailleX + ", " + tailleY + "\n");
        for (int y = 0; y < tailleY; y++) {
            for (int x = 0; x < tailleX; x++) {
                if(maze[y][x]) {
                    str.append(' ');
                } else {
                    str.append('#');
                }
            }
            str.append('\n');
        }
        return str.toString();
    }
}
