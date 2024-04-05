package com.isomapmaker.game.util;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.isomapmaker.game.controls.PaintModes;

public class CursorSetter {
    Cursor[] cursors;
    
    private static CursorSetter instance;
    private HashMap<PaintModes, FileHandle> modeFileMap;

    public static CursorSetter getInstance(){
        if(instance == null){
            CursorSetter.instance = new CursorSetter();
        }

        return CursorSetter.instance;
    }

    private CursorSetter(){
        modeFileMap = new HashMap<PaintModes, FileHandle>();
        modeFileMap.put(PaintModes.Pencil, Gdx.files.internal("pencil.png"));
        modeFileMap.put(PaintModes.Circle, Gdx.files.internal("circle.png"));
        modeFileMap.put(PaintModes.Line, Gdx.files.internal("line.png"));
        modeFileMap.put(PaintModes.Box, Gdx.files.internal("box.png"));
        modeFileMap.put(PaintModes.Bucket, Gdx.files.internal("bucket.png"));
        cursors = new Cursor[6];

        cursors[PaintModes.Pencil.ordinal()] = Gdx.graphics.newCursor(new Pixmap(modeFileMap.get(PaintModes.Pencil)),15,15);
        cursors[PaintModes.Circle.ordinal()] = Gdx.graphics.newCursor(new Pixmap(modeFileMap.get(PaintModes.Circle)),15,15);
        cursors[PaintModes.Line.ordinal()] = Gdx.graphics.newCursor(new Pixmap(modeFileMap.get(PaintModes.Line)),15,15);
        cursors[PaintModes.Box.ordinal()] = Gdx.graphics.newCursor(new Pixmap(modeFileMap.get(PaintModes.Box)),15,15);
        cursors[PaintModes.Bucket.ordinal()] = Gdx.graphics.newCursor(new Pixmap(modeFileMap.get(PaintModes.Bucket)),15,15);

    }

    public void setCursor(PaintModes mode){
        Gdx.graphics.setCursor(cursors[mode.ordinal()]);
    }

    
    public Vector<FileHandle> getFiles(){
        return new Vector<FileHandle>(modeFileMap.values());
    }
    public Vector<PaintModes> getModes(){return new Vector<PaintModes>(modeFileMap.keySet());}

}
