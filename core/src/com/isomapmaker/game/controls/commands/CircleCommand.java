package com.isomapmaker.game.controls.commands;

import java.util.Vector;



import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.controls.PaintTools;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Floor;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.map.TileMaps.TileMap;

/**
 * Draw a cirlce out of assets 
 */
public class CircleCommand extends Command{
    int x0, y0, r;
    Asset floor;

    public CircleCommand(int x0, int y0, int r, Asset floor, TileMap map) {
        super(map);
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        this.floor = floor;
        
    }

    @Override
    public void executeCommand() {
        circle();
    }
    

    private boolean circle(){
        Vector<Integer[]> c = PaintTools.circle(x0,y0,r);
       

        switch(ModeController.getInstance().getAssetState()){
            case Wall:
                for(int i = 0; i<c.size(); i++){
                    int x = c.get(i)[0];
                    int y = c.get(i)[1];
                    
                    if(map.getWall(x-1,y, WallQuadrant.right) == null) map.setWall(x,y,WallQuadrant.left, ModeController.getInstance().getWallRegion(WallQuadrant.left));
                    if(map.getWall(x+1,y, WallQuadrant.left) == null) map.setWall(x,y,WallQuadrant.right, ModeController.getInstance().getWallRegion(WallQuadrant.right));
                    if(map.getWall(x,y+1, WallQuadrant.bottom) == null) map.setWall(x,y,WallQuadrant.top, ModeController.getInstance().getWallRegion(WallQuadrant.top));
                    if(map.getWall(x,y-1, WallQuadrant.top) == null) map.setWall(x,y,WallQuadrant.bottom, ModeController.getInstance().getWallRegion(WallQuadrant.bottom));


                }
            break;
            case Floor:
                for(int i = 0; i<c.size(); i++){
                    map.setFloor(c.get(i)[0], c.get(i)[1], (Asset) floor);
                }
            break;
            case Object:
                for(int i = 0; i<c.size(); i++){
                    map.setObject(c.get(i)[0], c.get(i)[1], (Asset) floor);
                }
            break;
        }
        
        return true;
    }


    private void setEndCap(int x, int y){

    }

}
