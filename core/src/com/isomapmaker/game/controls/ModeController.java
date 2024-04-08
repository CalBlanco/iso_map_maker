package com.isomapmaker.game.controls;

import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.util.CursorSetter;

public class ModeController {
    private static ModeController instance;
    private PaintModes paintMode;
    private TileType assetMode;
    private String activeFile;
    private String activeRegion;

    public static ModeController getInstance(){
        if(ModeController.instance == null){
            ModeController.instance = new ModeController();
        }

        return ModeController.instance;
    }

    private ModeController(){
        paintMode = PaintModes.Pencil; // initialize to pencil mode
        assetMode = TileType.Wall;
        activeFile = TileAtlas.getInstance().getAssetsByType(assetMode).keys().get(0);
        activeRegion = TileAtlas.getInstance().getAssetsByType(assetMode).getRegionNames(activeFile).get(0);
    }

    public void setState(PaintModes newMode){
        paintMode = newMode;
        CursorSetter.getInstance().setCursor(newMode);
    }

    public PaintModes getState(){return paintMode;}

    public void setAssetState(TileType state){
        assetMode = state;
    }
    public TileType getAssetState(){return assetMode;}

    public void setActiveFile(String name){
        if(!TileAtlas.getInstance().getAssetsByType(assetMode).keys().contains(name)) return;
        activeFile = name;
    }
    public String getActiveFile(){return activeFile;}

    public void setActiveRegion(String name){
        if(!TileAtlas.getInstance().getAssetsByType(assetMode).getRegionNames(activeFile).contains(name)) return;
        activeRegion = name;
    }
    public String getActiveRegion(){return activeRegion;}

}
