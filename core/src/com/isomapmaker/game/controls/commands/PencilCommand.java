package com.isomapmaker.game.controls.commands;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.PlacementModes;
import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.util.IsoUtil;

public class PencilCommand extends Command {
    PlacementModes mode;
    String file, quadrant;
    WallQuadrant quad;
    int selection;
    Vector2 tilePos;
    Vector2 screenPos;

    public PencilCommand(PlacementModes mode, String file, WallQuadrant quadrant, int selection, Vector2 tilePos, Vector2 screenPos, TileLoader loader, TileMap map) {
        super(loader, map);
        this.mode = mode;
        this.file = file;
        this.quad = quadrant;
        this.selection = selection;
        this.tilePos = tilePos;
        this.screenPos = screenPos;
        //TODO Auto-generated constructor stub
    }

    private boolean pencil(){
        
        switch(mode){
            case Floor:
                try{
                   
                }
                catch(Exception e){return false;}
            case Wall:
                try{
                    
                }
                catch(Exception e){return false;}
            case Object:
                return false;
        }
        return false;
    }

    @Override
    public void executeCommand() {
        pencil();
    }
    
}
