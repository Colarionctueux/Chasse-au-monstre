package fr.univlille;

import java.util.Random;

public class Labyrinthe {

    public final static int N = 1;
    public final static int S = 2;
    public final static int E = 4;
    public final static int W = 8;

    private int width = 10;
    private int height = 10;

    public boolean[][] creer_labyrinthe(int l_width, int l_height) {
        width = l_width;
        height = l_height;
        boolean[][] map = new boolean[height * 2][width * 2];

        int[][] grid = new int[height][width];
        carvePassagesFrom(0, 0, grid);
        MazeTile[][] t = show(grid);

        for(int y = 0; y < height; y += 1) {
            for(int x = 0; x < width; x += 1) {
                for(int iy = 0; iy < 2; iy += 1) {
                    for(int ix = 0; ix < 2; ix += 1) {
                        int gx = x * 2;
                        int gy = y * 2;
                        


                        map[gy][gx] = true; // 1
                        
                        map[gy + 1][gx + 1] = false; // 2

                        map[gy + iy][gx] = !t[y][x].south;


                        map[gy][gx + ix] = !t[y][x].east;
                    }
                }
            }
        }
        return map;
    }

    MazeTile[][] show(int[][] grid) {
        MazeTile[][] tableau = new MazeTile[height][width];
        Random random = new Random();
        for(int y = 0; y < height; y += 1) {
            for(int x = 0; x < width; x += 1) {
                MazeTile t = new MazeTile();
                
                t.north = (N & grid[y][x]) != 0;
                t.south = (S & grid[y][x]) != 0;
                t.east = (E & grid[y][x]) != 0;
                t.west = (W & grid[y][x]) != 0;
                if(x > 0 && x < width - 1 || y > 0 && y < height - 1) {
                    if(!t.north) {
                        t.north = random.nextDouble() > 0.8;
                    }
                    if(!t.south) {
                        t.south = random.nextDouble() > 0.8;
                    }
                    if(!t.east) {
                        t.east = random.nextDouble() > 0.8;
                    }
                    if(!t.west) {
                        t.west = random.nextDouble() > 0.8;
                    }
                    
                }
                tableau[y][x] = t;
            }
        }
        return tableau;
    }

    int[] random_directions() {
        int[] directions = new int[]{N, S, E, W};
        int[] r_dir = new int[4];
        Random random = new Random();
        for(int i = 0; i < 4; i += 1) {
            int entier = (int) (random.nextDouble() * (double) directions.length);
            r_dir[i] = directions[entier];

            int nbr = 0;
            int[] tampon = directions;
            directions = new int[4 - (i + 1)];
            for(int j = 0; j <= 4 - (i + 1); j += 1) {
                if(tampon[entier] != tampon[j]) {
                    directions[nbr] = tampon[j];
                    nbr += 1;
                }
            }

        }
        return r_dir;
    }

    void carvePassagesFrom(int cx, int cy, int[][] grid) {
        int[] directions = random_directions();
        
        for(int i = 0; i < directions.length; i += 1) {
            int direction = directions[i];
            int nx = cx + dx(direction);
            int ny = cy + dy(direction);
            if(between(ny, 0, height - 1) && between(nx, 0, width - 1) && grid[ny][nx] == 0) {
                grid[cy][cx] = grid[cy][cx] | direction;
                grid[ny][nx] = grid[ny][nx] | opposite(direction);
                carvePassagesFrom(nx, ny, grid);
            }
        }
    }

    boolean between(int val, int min, int max) {
        return val >= min && val <= max;
    }

    int dx(int direction) {
        int valeur = 0;
        if(direction == E) {
            valeur = 1;
        } else if(direction == W) {
            valeur = -1;
        }
        return valeur;
    }
    
    int dy(int direction) {
        int valeur = 0;
        if(direction == S) {
            valeur = 1;
        } else if(direction == N) {
            valeur = -1;
        }
        return valeur;
    }

    int opposite(int direction) {
        int valeur = 0;
        if(direction == S) {
            valeur = N;
        } else if(direction == N) {
            valeur = S;
        } else if(direction == E) {
            valeur = W;
        } else if(direction == W) {
            valeur = E;
        }
        return valeur;
    }
}


class MazeTile {
    public MazeTile() {
    }
    public boolean north = false;
    public boolean south = false;
    public boolean east = false;
    public boolean west = false;
}