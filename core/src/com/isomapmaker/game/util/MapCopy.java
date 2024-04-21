package com.isomapmaker.game.util;

import com.isomapmaker.game.map.Assets.Tile;

/**
 * Utility class to help deep copy Tile information for undo/redo 
 */
public class MapCopy {
    
    /**Super deep copy */
    public static Tile[][] deepCopy(Tile[][] original){
        if(original == null) return null;
        Tile[][] copy = new Tile[original.length][];
        for(int i=0; i< original.length; i++){
            copy[i] = new Tile[original[i].length];
            for(int j=0; j<original[i].length; j++){
                if(original[i][j] == null) continue; 
                copy[i][j] = new Tile(original[i][j]);
            }
        }

        return copy;
    }
}
