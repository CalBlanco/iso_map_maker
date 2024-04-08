package com.isomapmaker.game.controls;

import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.util.CursorSetter;

public class ModeController {
    private static ModeController instance;
    private PaintModes paintMode;
    private TileType assetMode;

    public static ModeController getInstance(){
        if(ModeController.instance == null){
            ModeController.instance = new ModeController();
        }

        return ModeController.instance;
    }

    private ModeController(){
        paintMode = PaintModes.Pencil; // initialize to pencil mode
        assetMode = TileType.Floor;
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
}
