package com.isomapmaker.game.controls.commands;

import java.util.Vector;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.controls.PaintTools;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Tile;
import com.isomapmaker.game.map.Assets.TileDelta;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.map.TileMaps.TileMap;

/**
 * Draw a line out of assets 
 */
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
        Vector<Integer[]> l = PaintTools.line((int)clickPos.x, (int)clickPos.y, (int)endPos.x, (int)endPos.y);
        
        switch(ModeController.getInstance().getAssetState()){
            case Floor:
                
                for(int i = 0; i<l.size(); i++){
                    Tile oldTile = map.getTile(l.get(i)[0],l.get(i)[1]) == null ? null : new Tile(map.getTile(l.get(i)[0],l.get(i)[1]));
                    map.setFloor(l.get(i)[0], l.get(i)[1],floor);
                    Tile newTile = new Tile(map.getTile(l.get(i)[0], l.get(i)[1]));
                    TileDelta td = new TileDelta(l.get(i)[0],l.get(i)[1],oldTile, newTile);
                    this.deltas.add(td);
                }
                
            break;
            case Wall:
                for(int i = 0; i<l.size(); i++){
                    int x = l.get(i)[0];
                    int y = l.get(i)[1];

                    Tile oldTile = map.getTile(x,y) == null ? null : new Tile(map.getTile(x,y));
                    if(map.getWall(x-1,y, WallQuadrant.right) == null) map.setWall(x,y,WallQuadrant.left, ModeController.getInstance().getWallRegion(WallQuadrant.left));
                    if(map.getWall(x+1,y, WallQuadrant.left) == null) map.setWall(x,y,WallQuadrant.right, ModeController.getInstance().getWallRegion(WallQuadrant.right));
                    if(map.getWall(x,y+1, WallQuadrant.bottom) == null) map.setWall(x,y,WallQuadrant.top, ModeController.getInstance().getWallRegion(WallQuadrant.top));
                    if(map.getWall(x,y-1, WallQuadrant.top) == null) map.setWall(x,y,WallQuadrant.bottom, ModeController.getInstance().getWallRegion(WallQuadrant.bottom));
                    Tile newTile = new Tile(map.getTile(x, y));

                    TileDelta td = new TileDelta(x, y, oldTile, newTile);
                    this.deltas.add(td);

                }
                break;
            case Object:
                for(int i=0; i<l.size(); i++){
                    Tile oldTile = map.getTile(l.get(i)[0],l.get(i)[1]) == null ? null : new Tile(map.getTile(l.get(i)[0],l.get(i)[1]));
                    map.setObject(l.get(i)[0], l.get(i)[1], floor);
                    Tile newTile = new Tile(map.getTile(l.get(i)[0], l.get(i)[1]));
                    TileDelta td = new TileDelta(l.get(i)[0],l.get(i)[1],oldTile, newTile);
                    this.deltas.add(td);
                }
        }
        
        return true;
    }
    
}
