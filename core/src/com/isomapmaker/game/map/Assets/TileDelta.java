package com.isomapmaker.game.map.Assets;

import com.isomapmaker.game.map.TileMaps.TileMap;

/**
 * Track the changes made to a tile (supposed to lighten the load of the command stacks)
 */
public class TileDelta {
    int x,y; // position of the tile 
    Tile oldTile, newTile; // copy of the old tile, copy of the new tile 

    /**
     * Base line constructor, provide position, old tile, and new tile 
     * @param x
     * @param y
     * @param oldTile
     * @param newTile
     */
    public TileDelta(int x, int y, Tile oldTile, Tile newTile){
        this.x = x;
        this.y = y;
        this.oldTile = oldTile;
        this.newTile = newTile;
    }

    /*
     * Apply the delta to a given map 
     */
    public void applyDelta(TileMap map){
        map.setTile(x, y, newTile);
    }

    /**
     * Undo the delta from the given map
     * @param map
     */
    public void undoDelta(TileMap map){
        if (oldTile == null) map.clearTile(x, y);
        else  map.setTile(x, y, oldTile);
    }


    
}
