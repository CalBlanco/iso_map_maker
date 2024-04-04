package com.isomapmaker.game.controls.commands;

import java.util.Arrays;

import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.Tiles.Tile;
import com.isomapmaker.game.util.MapCopy;

public abstract class Command {
    TileLoader loader;
    TileMap map;
    Tile[][] state;
    
    
    /**
     * Subclass implmentation required, perform operation
     */
    public abstract void executeCommand();

    /**
     * Pass in a asset loader and map to edit super(loader,map) within subclass
     * @param loader
     * @param map
     */
    public Command(TileLoader loader, TileMap map){
        this.loader = loader;
        this.map = map;
        this.state = MapCopy.deepCopy(map.getMapState());
    }

    

    /**
     * Execute wrapper to save state before execution
     */
    public void execute(){
        System.out.println("Executing Command (Changing map state)");

        executeCommand(); // execute command
    }

    /**
     * Undo this operation by reverting the map state to the one stored before exectution
     */
    public void undo(){
        System.out.println("Undoing Command");
        int diff = MapCopy.getdiff(state, map.getMapState());
        System.out.println("Different Tiles: " + diff);
        if(state != null) this.map.setMapState(state);
        state = null;
    }


}
