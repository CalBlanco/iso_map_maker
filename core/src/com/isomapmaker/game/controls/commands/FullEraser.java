package com.isomapmaker.game.controls.commands;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.TileMaps.TileMap;

public class FullEraser extends Command {
    Vector2 tilePos;
    public FullEraser(Vector2 tilePos, TileMap map){
        super(map);
        this.tilePos = tilePos;
    }

    private boolean FullErase(){
        this.map.clearTile((int)tilePos.x, (int)tilePos.y);
        return true;
    }

    @Override
    public void executeCommand() {
        FullErase();
    }
    
}
