package com.isomapmaker.game.util;

import com.isomapmaker.game.map.Tiles.Tile;

public class MapCopy {
    public static int getdiff(Tile[][] m1, Tile[][] m2){
        int diff = 0;
        for(int i=0; i< m1.length; i++){
            for(int j=0; j< m1[i].length; j++){
                if(m1[i][j] != m2[i][j]) diff++;
            }
        }

        return diff;
    }

    /**Super deep copy */
    public static Tile[][] deepCopy(Tile[][] original){
        if(original == null) return null;
        Tile[][] copy = new Tile[original.length][];
        for(int i=0; i< original.length; i++){
            copy[i] = new Tile[original[i].length];
            for(int j=0; j<original[i].length; j++){
                copy[i][j] = new Tile(original[i][j]);
            }
        }

        return copy;
    }
}
