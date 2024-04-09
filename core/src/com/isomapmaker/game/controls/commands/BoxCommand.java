package com.isomapmaker.game.controls.commands;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Floor;

import com.isomapmaker.game.map.TileMaps.TileMap;


public class BoxCommand extends Command {
    Asset floor;
    Vector2 tilePos, endpos;
    public BoxCommand(Vector2 tilePos, Vector2 endpos, Asset floor, TileMap map) {
        super(map);
        this.tilePos = tilePos;
        this.endpos = endpos;
        this.floor = floor;
       
    }

    @Override
    public void executeCommand() {
        box();
    }


    private boolean box(){
        int lx = tilePos.x < endpos.x ? (int) tilePos.x : (int) endpos.x;
        int ly = tilePos.y < endpos.y ? (int) tilePos.y : (int) endpos.y;
        int dx = (int)Math.abs(tilePos.x - endpos.x);
        int dy = (int)Math.abs(tilePos.y - endpos.y);

        for(int x=lx; x<lx+dx+1; x++){
            for(int y=ly; y<ly+dy+1; y++){
                if(x == tilePos.x || x == endpos.x || y == tilePos.y || y == endpos.y){
                    map.setFloor(x,y,floor);
                }
            }
        }

        return true;
    }
    
}
