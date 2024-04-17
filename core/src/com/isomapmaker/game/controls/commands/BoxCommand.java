package com.isomapmaker.game.controls.commands;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Floor;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
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

        TileType type = ModeController.getInstance().getAssetState();
        
        for(int x=lx; x<lx+dx+1; x++){
            for(int y=ly; y<ly+dy+1; y++){
                if(x == tilePos.x || x == endpos.x || y == tilePos.y || y == endpos.y){
                    switch(type){
                        case Floor:
                        map.setFloor(x,y,floor);
                        break;
                        case Wall:
                            map.setWall(x,y,WallQuadrant.left,ModeController.getInstance().getWallRegion(WallQuadrant.left));
                            map.setWall(x,y, WallQuadrant.right, ModeController.getInstance().getWallRegion(WallQuadrant.right));
                            map.setWall(x,y, WallQuadrant.top, ModeController.getInstance().getWallRegion(WallQuadrant.top));
                            map.setWall(x,y, WallQuadrant.bottom, ModeController.getInstance().getWallRegion(WallQuadrant.bottom));
                        break;
                        case Object:
                            map.setObject(x, y, floor);
                        break;
                    }
                    
                }
            }
        }

        return true;
    }
    
}
