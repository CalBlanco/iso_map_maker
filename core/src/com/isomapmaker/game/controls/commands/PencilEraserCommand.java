package com.isomapmaker.game.controls.commands;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.PlacementModes;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;

public class PencilEraserCommand extends Command {
    PlacementModes mode;
    Vector2 tilePos;
    WallQuadrant quad;
    public PencilEraserCommand(PlacementModes mode, Vector2 tilePos, WallQuadrant quad, TileMap map) {
        super(map);
        this.mode = mode;
        this.tilePos = tilePos;
        this.quad = quad;
        //TODO Auto-generated constructor stub
    }

    @Override
    public void executeCommand() {
        pencilEraser();
    }

    private boolean pencilEraser(){
        switch (mode) {
            case Floor:
                map.setFloor((int)tilePos.x, (int)tilePos.y, null);
                return true;
            case Wall:
                map.setWall((int)tilePos.x, (int)tilePos.y, quad, null);
                return true;
            case Object:
                return true;
            default:
                return false;
        }
    }
    
}
