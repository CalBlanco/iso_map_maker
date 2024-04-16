package com.isomapmaker.game.controls;

import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Tile;
import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.util.CursorSetter;

public class ModeController {
    private static ModeController instance; // the singleton instance
    private PaintModes paintMode; // the paint mode we are in (pencil, box, etc..)
    private TileType assetMode; // The asset type we are using (Floor, Wall, Object)
    private String activeFile; // The active atlas we are pulling from 
    private String activeRegion; // The active atlas region we want 
    private WallQuadrant activeQuad;

    public static ModeController getInstance(){
        if(ModeController.instance == null){
            ModeController.instance = new ModeController();
        }

        return ModeController.instance;
    }

    private ModeController(){
        paintMode = PaintModes.Pencil; // initialize to pencil mode
        assetMode = TileType.Floor;
        activeFile = TileAtlas.getInstance().getAssetsByType(assetMode).keys().get(0);
        activeRegion = TileAtlas.getInstance().getAssetsByType(assetMode).getRegionNames(activeFile).get(0);
        activeQuad = WallQuadrant.bottom;
    }

    public void setState(PaintModes newMode){
        paintMode = newMode;
        CursorSetter.getInstance().setCursor(newMode);
    }

    public PaintModes getState(){return paintMode;}

    public void setAssetState(TileType state){
        assetMode = state;
        activeFile = TileAtlas.getInstance().getAssetsByType(state).keys().get(0);
        activeRegion = TileAtlas.getInstance().getAssetsByType(state).getRegionNames(activeFile).get(0);
    }
    public TileType getAssetState(){return assetMode;}

    public void setActiveFile(String name){
        if(!TileAtlas.getInstance().getAssetsByType(assetMode).keys().contains(name)) return;
        activeFile = name;
        activeRegion = TileAtlas.getInstance().getAssetsByType(assetMode).getRegionNames(activeFile).get(0);
    }
    public String getActiveFile(){return activeFile;}

    public void setActiveRegion(String name){
        if(!TileAtlas.getInstance().getAssetsByType(assetMode).getRegionNames(activeFile).contains(name)) return;
        activeRegion = name;
    }
    public String getActiveRegion(){return activeRegion;}


    public Asset getActiveAsset(){
        return TileAtlas.getInstance().get(assetMode, activeFile, activeRegion);
    }

    public void setPlacementMode(TileType type, String name, String assetName){
        if(!TileAtlas.getInstance().getAssetsByType(type).keys().contains(name) || !TileAtlas.getInstance().getAssetsByType(type).getRegionNames(name).contains(assetName)) return;
        assetMode = type;
        activeFile = name;
        activeRegion = assetName;
        System.out.println("Active Asset: " + assetMode.toString() +", " + activeFile +", " + activeRegion);
    }

    public void setQuadrant(WallQuadrant quad){
        activeQuad = quad;
    }

    public WallQuadrant getQuadrant(){return activeQuad;}


    public void incrementQuadrant(){
        int quadIndex = activeQuad.ordinal();
        int next = (quadIndex + 1) % 4; // this should always be 4
        activeQuad = WallQuadrant.values()[next];
    }

}
