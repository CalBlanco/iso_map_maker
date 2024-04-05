package com.isomapmaker.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.isomapmaker.game.controls.PaintModes;

public class CursorSetter {
    Pixmap[] cursors;
    private static CursorSetter instance;

    public static CursorSetter getInstance(){
        if(instance == null){
            CursorSetter.instance = new CursorSetter();
        }

        return CursorSetter.instance;
    }

    private CursorSetter(){
        cursors = new Pixmap[6];
        cursors[PaintModes.Pencil.ordinal()] = new Pixmap(Gdx.files.internal("pencil.png"));
        cursors[PaintModes.Circle.ordinal()] = new Pixmap(Gdx.files.internal("circle.png"));
        cursors[PaintModes.Line.ordinal()] = new Pixmap(Gdx.files.internal("cursor.png"));
        cursors[PaintModes.Box.ordinal()] = new Pixmap(Gdx.files.internal("cursor.png"));
        cursors[PaintModes.Bucket.ordinal()] = new Pixmap(Gdx.files.internal("cursor.png"));
    }

    public void setCursor(PaintModes mode){
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursors[mode.ordinal()], 15, 15));
    }


}
