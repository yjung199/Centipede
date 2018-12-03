import javax.swing.*;
import java.awt.*;

// TODO
// Mushroom placement fix
// Dont make Diagonally placed
// Dont place along the wall


public class TileGenerator{

    public int tile[][];
    public int mushCount = 0;


    public TileGenerator(int row, int col, double shroomNum) {
        tile = new int [row][col];

        for (int i = 1; i < tile.length-1; i++) {           // i range set from 1 to tile.length-1 to avoid mushrooms to get placed along the wall.
            for (int j = 1; j < tile[0].length; j++) {      // j range set from 1 to tile[0].length to empty first row.
                if(Math.random() < shroomNum) {
                    tile[i][j] = 3;
//                    mushCount++;
                } else {
                    tile[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[0].length; j++) {
                if(i > 0 && i < tile.length-1 && j > 0 && j < tile[0].length)
                {
                    if(tile[i-1][j-1]>0)
                    {
                        tile[i][j] = 0;
                    }
                    else if (tile[i+1][j-1]>0)
                    {
                        tile[i][j] = 0;
                    }
                    if(j == 1)
                    {
                        tile[0][j] = 0;
                        tile[tile.length-1][j] = 0;
                    }
                }

                if(i == 0 || i == tile.length-1)
                {
                    if(j > 1)
                    {
                        if(tile[i][j-1] > 0 || tile[i][j-2] > 0)
                        {
                            tile[i][j] = 0;
                        }
                        if (i == 0)
                        {
                            if(tile[i+1][j-1] > 0)
                            {
                                tile[i][j] = 0;
                            }
                        }
                        else if (i == tile.length-1)
                        {
                            if (tile[i-1][j-1] > 0)
                            {
                                tile[i][j] = 0;
                            }

                        }
                    }
                }

                if(tile[i][j] > 0)
                {
                    mushCount ++;
                }

            }
        }
    }

    public void draw(Graphics2D g){

        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[0].length; j++) {
                if (tile[i][j] > 0) {
                    Mushroom mush = new Mushroom(tile[i][j]);
                    g.drawImage(mush.mushImage(), i*32, j*32, null);

                }
            }
        }
    }

    public void gotHit(int i, int j){
        tile[i][j]--;


        if(tile[i][j] <= 0) {
            mushCount--;
        }

    }
}
