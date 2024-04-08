package com.isomapmaker.game.controls.commands;

import java.util.Arrays;

import com.isomapmaker.game.map.Assets.Tile;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;

import com.isomapmaker.game.util.MapCopy;

/**
 * Generic class for commands
 * Handles saving the map state before command execution
 * and reverting map state if needed
 */
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
    public Command( TileMap map){
        this.map = map;
        this.state = MapCopy.deepCopy(map.getMapState());
    }

    

    /**
     * Execute wrapper to save state before execution
     */
    public void execute(){
        executeCommand(); // execute command
    }

    /**
     * Undo this operation by reverting the map state to the one stored before exectution
     */
    public void undo(){
        if(state != null) this.map.setMapState(state);
    }


}
