package com.isomapmaker.game.controls;

import com.isomapmaker.game.util.CursorSetter;

public class ModeController {
    private static ModeController instance;
    private PaintModes paintMode;

    public static ModeController getInstance(){
        if(ModeController.instance == null){
            ModeController.instance = new ModeController();
        }

        return ModeController.instance;
    }

    private ModeController(){
        paintMode = PaintModes.Pencil; // initialize to pencil mode
    }

    public void setState(PaintModes newMode){
        paintMode = newMode;
        CursorSetter.getInstance().setCursor(newMode);
    }

    public PaintModes getState(){return paintMode;}
}
