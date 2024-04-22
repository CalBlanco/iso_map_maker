package com.isomapmaker.game.controls.commands;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.ModeController;

import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.map.Assets.Tile;
import com.isomapmaker.game.map.Assets.TileDelta;
import com.isomapmaker.game.map.Assets.Wall;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.util.IsoUtil;

public class PencilCommand extends Command {
    
    String file;
    WallQuadrant quad;
    TileType mode;
    int selection;
    Vector2 tilePos;
    Vector2 screenPos;

    /**
     * Place the specified asset at the mouse position 
     * @param file
     * @param quadrant
     * @param selection
     * @param tilePos
     * @param screenPos
     * @param map
     */
    public PencilCommand(String file, WallQuadrant quadrant, int selection, Vector2 tilePos, Vector2 screenPos, TileMap map) {
        super( map);
        
        this.file = file;
        this.quad = quadrant;
        this.selection = selection;
        this.tilePos = tilePos;
        this.screenPos = screenPos;
        //TODO Auto-generated constructor stub
    }

    private boolean pencil(){
        TileType mode = ModeController.getInstance().getAssetState();
        Tile oldTile = map.getTile((int) tilePos.x, (int) tilePos.y) == null ? null : new Tile(map.getTile((int)tilePos.x, (int)tilePos.y)); // copy our old tile for the delta
        Tile newTile = null;
        //System.out.println("Pencil: " + tilePos.toString() +", " + mode.toString() +", " + quad);
        switch(mode){
            case Floor:
                try{
                   map.setFloor((int)tilePos.x, (int)tilePos.y, ModeController.getInstance().getActiveAsset());
                   newTile = new Tile(map.getTile((int)tilePos.x, (int)tilePos.y));
                   break;
                }
                catch(Exception e){return false;}
            case Wall:
                try{
                    Wall w = (Wall)ModeController.getInstance().getActiveAsset();
                    
                    map.setWall((int)tilePos.x, (int)tilePos.y, w.getQuadrant(), ModeController.getInstance().getActiveAsset());
                    newTile = new Tile(map.getTile((int)tilePos.x, (int)tilePos.y));
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                    return false;}
            case Object:
                try{
                    map.setObject((int)tilePos.x, (int)tilePos.y, ModeController.getInstance().getActiveAsset());
                    newTile = new Tile(map.getTile((int)tilePos.x, (int)tilePos.y));
                    break;
                
                }
                catch(Exception e){
                    e.printStackTrace();
                    return false;
                }
        }

        TileDelta delta = new TileDelta((int)tilePos.x, (int)tilePos.y, oldTile, newTile);
        this.deltas.add(delta);
        return false;
    }

    @Override
    public void executeCommand() {
        pencil();
    }
    
}
