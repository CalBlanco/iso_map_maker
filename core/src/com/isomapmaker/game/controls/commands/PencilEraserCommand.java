package com.isomapmaker.game.controls.commands;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.map.Assets.Tile;
import com.isomapmaker.game.map.Assets.TileDelta;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;

import com.isomapmaker.game.map.TileMaps.TileMap;

/**
 * Erase the asset type under our cursor 
 */
public class PencilEraserCommand extends Command {
    TileType mode;
    Vector2 tilePos;
    WallQuadrant quad;
    public PencilEraserCommand(Vector2 tilePos, WallQuadrant quad, TileMap map) {
        super(map);
        this.mode = ModeController.getInstance().getAssetState();
        this.tilePos = tilePos;
        this.quad = quad;
        //TODO Auto-generated constructor stub
    }

    @Override
    public void executeCommand() {
        pencilEraser();
    }

    private boolean pencilEraser(){
        Tile oldTile = map.getTile((int)tilePos.x, (int)tilePos.y) == null ? null : new Tile(map.getTile((int)tilePos.x, (int)tilePos.y));
        switch (mode) {
            case Floor:
                map.setFloor((int)tilePos.x, (int)tilePos.y, null);
                break;
            case Wall:
                map.setWall((int)tilePos.x, (int)tilePos.y, quad, null);
                break;
            case Object:
                map.setObject((int)tilePos.x, (int)tilePos.y, null);
                break;
        }

        Tile newTile = new Tile(map.getTile((int)tilePos.x, (int)tilePos.y));
        TileDelta td = new TileDelta((int)tilePos.x, (int)tilePos.y, oldTile, newTile);
        this.deltas.add(td);

        return true;
    }
    
}
