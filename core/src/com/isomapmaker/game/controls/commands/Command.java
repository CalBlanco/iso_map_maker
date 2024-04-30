package com.isomapmaker.game.controls.commands;

import java.util.Vector;
import com.isomapmaker.game.map.Assets.Tile;
import com.isomapmaker.game.map.Assets.TileDelta;
import com.isomapmaker.game.map.TileMaps.TileMap;

/**
 * Generic class for commands
 * Handles saving the map state before command execution
 * and reverting map state if needed
 */
public abstract class Command {
    TileMap map;
    Tile[][] state;
    Vector<TileDelta> deltas;
    
    
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
       //this.state = MapCopy.deepCopy(map.getMapState());
        this.deltas = new Vector<TileDelta>();
    }

    

    /**
     * Execute wrapper to save state before execution
     */
    public void execute(){
        executeCommand(); // execute command
    }

    /**
     * Undo this operation by reverting the map state to the one stored before exectution
     *  -- Undo by undoing the delta
     */
    public void undo(){
        //if(state != null) this.map.setMapState(state);
        System.out.println("Size of deltas: " + deltas.size());
        for(int i=0; i<deltas.size(); i++){
            deltas.get(i).undoDelta(map);
        }
    }

    
    /**
     * Redo Operation by re-applying the delta
     */
    public void redo(){
        System.out.println("Size of deltas: " + deltas.size());
        for(int i=0; i<deltas.size(); i++){
            deltas.get(i).applyDelta(map);
        }
    }


    public int deltaSize(){
        return this.deltas.size();
    }


    


}
