package com.isomapmaker.game.controls.commands;

import java.util.Vector;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.controls.PaintTools;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.map.TileMaps.TileMap;


public class LineCommand extends Command{
    Vector2 clickPos, endPos;
    Asset floor;

    public LineCommand(Vector2 clickPos, Vector2 endPos, Asset floor , TileMap map) {
        super(map);
        this.clickPos = clickPos;
        this.endPos = endPos;
        this.floor = floor;
    }

    @Override
    public void executeCommand() {
        line();
    }

    private boolean line(){
        Vector<Integer[]> l = PaintTools.line(clickPos, endPos);
        
        switch(ModeController.getInstance().getAssetState()){
            case Floor:
                
                for(int i = 0; i<l.size(); i++){
                    
                    map.setFloor(l.get(i)[0], l.get(i)[1],floor);
                }
                
            break;
            case Wall:
                for(int i = 0; i<l.size(); i++){
                    int x = l.get(i)[0];
                    int y = l.get(i)[1];
                    if(map.getWall(x-1,y, WallQuadrant.right) == null) map.setWall(x,y,WallQuadrant.left, ModeController.getInstance().getWallRegion(WallQuadrant.left));
                    if(map.getWall(x+1,y, WallQuadrant.left) == null) map.setWall(x,y,WallQuadrant.right, ModeController.getInstance().getWallRegion(WallQuadrant.right));
                    if(map.getWall(x,y+1, WallQuadrant.bottom) == null) map.setWall(x,y,WallQuadrant.top, ModeController.getInstance().getWallRegion(WallQuadrant.top));
                    if(map.getWall(x,y-1, WallQuadrant.top) == null) map.setWall(x,y,WallQuadrant.bottom, ModeController.getInstance().getWallRegion(WallQuadrant.bottom));

                }
                break;
            case Object:
                for(int i=0; i<l.size(); i++){
                    map.setObject(l.get(i)[0], l.get(i)[1], floor);
                }
        }
        
        return true;
    }
    
}
