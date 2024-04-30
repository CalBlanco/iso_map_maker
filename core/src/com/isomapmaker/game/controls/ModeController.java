package com.isomapmaker.game.controls;

import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.util.CursorSetter;

/**
 * Class to manage important states across the map maker (Singleton pattern so that states are consistent across the game)
 */
public class ModeController {
    private static ModeController instance; // the singleton instance
    private PaintModes paintMode; // the paint mode we are in (pencil, box, etc..)
    private TileType assetMode; // The asset type we are using (Floor, Wall, Object)
    private String activeFile; // The active atlas we are pulling from 
    private String activeRegion; // The active atlas region we want 
    private WallQuadrant activeQuad;
    private int brushSize = 4;

    private boolean isSavingLoading = false;

    public static ModeController getInstance(){
        if(ModeController.instance == null){
            ModeController.instance = new ModeController();
        }

        return ModeController.instance;
    }

    private ModeController(){
        paintMode = PaintModes.Pencil; // initialize to pencil mode
        assetMode = TileType.Floor;
        while(TileAtlas.getInstance().isLoading == true){
            
        }
        activeFile = TileAtlas.getInstance().getAssetsByType(assetMode).keys().get(0);
        activeRegion = TileAtlas.getInstance().getAssetsByType(assetMode).getRegionNames(activeFile).get(0);
        activeQuad = WallQuadrant.bottom;
        brushSize = 2;
    }

    /**
     * Set the PaintMode of the MapMaker
     * @param newMode
     */
    public void setState(PaintModes newMode){
        paintMode = newMode;
        CursorSetter.getInstance().setCursor(newMode);
    }

    /**
     * Return the active paint mode 
     * @return
     */
    public PaintModes getState(){return paintMode;}

    /**
     * Set the TileType we are placing 
     * (this function also resets the active file and region to ensure we have a good reference to an asset based on our state)
     * @param state
     */
    public void setAssetState(TileType state){
        assetMode = state;
        activeFile = TileAtlas.getInstance().getAssetsByType(state).keys().get(0);
        activeRegion = TileAtlas.getInstance().getAssetsByType(state).getRegionNames(activeFile).get(0);
    }

    /**
     * Get the active TileType
     * @return
     */
    public TileType getAssetState(){return assetMode;}

    /**
     * Set the active file we are pulling assets from 
     * (resets the active region to ensure it is from this file)
     * @param name
     */
    public void setActiveFile(String name){
        if(!TileAtlas.getInstance().getAssetsByType(assetMode).keys().contains(name)) return;
        activeFile = name;
        activeRegion = TileAtlas.getInstance().getAssetsByType(assetMode).getRegionNames(activeFile).get(0);
    }

    /**
     * Return the active file we are pulling assets from
     * @return
     */
    public String getActiveFile(){return activeFile;}

    /**
     * Set the active texture region based on the asset name 
     * 
     * @param name
     */
    public void setActiveRegion(String name){
        if(!TileAtlas.getInstance().getAssetsByType(assetMode).getRegionNames(activeFile).contains(name)) return;
        activeRegion = name;
    }

    /**
     * Get the active region name 
     * @return
     */
    public String getActiveRegion(){return activeRegion;}

    /**
     * Get the asset that corresponds to our states 
     * @return
     */
    public Asset getActiveAsset(){
        return TileAtlas.getInstance().get(assetMode, activeFile, activeRegion);
    }

    /**
     * Get a different wall region based on the provided quadrant 
     * @param quad
     * @return
     */
    public Asset getWallRegion(WallQuadrant quad){
        if(assetMode != TileType.Wall) return null;
        return TileAtlas.getInstance().get(assetMode, activeFile, activeRegion.split("-")[0]+"-"+quad.toString());
    }

    /**
     * Ensure that our files are within our loaded information 
     * @param type
     * @param name
     * @param assetName
     */
    public void setPlacementMode(TileType type, String name, String assetName){
        if(!TileAtlas.getInstance().getAssetsByType(type).keys().contains(name) || !TileAtlas.getInstance().getAssetsByType(type).getRegionNames(name).contains(assetName)) return;
        assetMode = type;
        activeFile = name;
        activeRegion = assetName;
        //System.out.println("Active Asset: " + assetMode.toString() +", " + activeFile +", " + activeRegion);
    }

    /**
     * Set our active quadrant 
     * @param quad
     */
    public void setQuadrant(WallQuadrant quad){
        activeQuad = quad;
        
        setActiveRegion(activeRegion.split("-")[0]+"-"+quad.toString());
    }

    /**
     * Get the active quadrant 
     * @return
     */
    public WallQuadrant getQuadrant(){return activeQuad;}

    /**
     * increment the active quadrant 
     */
    public void incrementQuadrant(){
        int quadIndex = activeQuad.ordinal();
        int next = (quadIndex + 1) % 4; // this should always be 4
        setQuadrant(WallQuadrant.values()[next]);
    }

    public void decrementQuadrant(){
        int quadIndex = activeQuad.ordinal();
        int next = quadIndex - 1 < 0 ? 4 : quadIndex -1;
        setQuadrant(WallQuadrant.values()[next]);
    }

    /**
     * Set our save state (are we or aren't we saving)
     * @param state
     */
    public void setSavingLoading(boolean state){
        isSavingLoading = state;
    }

    public boolean getSavingLoading(){ return isSavingLoading;}


    public void setBrushSize(int size){
        if(size < 1 || size > 256) return;
        brushSize = size;
    }

    public void incrementBrushSize(int increment){
        if(brushSize + increment > 1 || brushSize + increment < 256){
            brushSize = brushSize + increment;
            System.out.println("Setting brush to size: " + brushSize);
        }
    }

    public int getBrushSize(){
        return brushSize;
    }


}
