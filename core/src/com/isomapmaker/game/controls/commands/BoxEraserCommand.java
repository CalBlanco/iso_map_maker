package com.isomapmaker.game.controls.commands;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.TileMaps.TileMap;

public class BoxEraserCommand extends Command {

    Vector2 start,end;
    public BoxEraserCommand(Vector2 start, Vector2 end, TileMap map) {
        super(map);
        this.start = start;
        this.end = end;

        //TODO Auto-generated constructor stub
    }

    private void BoxErase(){
        int lx = start.x < end.x ? (int) start.x : (int) end.x;
        int ly = start.y < end.y ? (int) start.y : (int) end.y;
        int dx = (int)Math.abs(start.x - end.x);
        int dy = (int)Math.abs(start.y - end.y);

        for(int x=lx; x<lx+dx+1; x++){
            for(int y=ly; y<ly+dy+1; y++){
                map.clearTile(x, y);
            }
        }

    }

    @Override
    public void executeCommand() {
        BoxErase();
    }
    
}
