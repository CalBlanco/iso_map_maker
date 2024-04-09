package com.isomapmaker.game.controls.commands;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.ModeController;

import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
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
        System.out.println("Pencil: " + tilePos.toString() +", " + mode.toString() +", " + quad);
        switch(mode){
            case Floor:
                try{
                   map.setFloor((int)tilePos.x, (int)tilePos.y, ModeController.getInstance().getActiveAsset());
                }
                catch(Exception e){return false;}
            case Wall:
                try{
                    Wall w = (Wall)ModeController.getInstance().getActiveAsset();
                    map.setWall((int)tilePos.x, (int)tilePos.y, w.getQuadrant(), ModeController.getInstance().getActiveAsset());
                }
                catch(Exception e){
                    e.printStackTrace();
                    return false;}
            case Object:
            try{
                map.setObject((int)tilePos.x, (int)tilePos.y, ModeController.getInstance().getActiveAsset());
               
            }
            catch(Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public void executeCommand() {
        pencil();
    }
    
}
