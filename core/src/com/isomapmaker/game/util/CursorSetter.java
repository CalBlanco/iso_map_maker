package com.isomapmaker.game.util;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.isomapmaker.game.controls.PaintModes;

/**
 * Class to manage what the cursor looks like on the screen depending on the tool (Singleton class so that keys and ui can change state )
 * 
 * I hate this because I need to have pixmaps for everything instead of being able to do something cool with a texture atlas 
 * atleast for right now 
 */
public class CursorSetter {
    Cursor[] cursors;
    
    private static CursorSetter instance;
    private HashMap<PaintModes, FileHandle> modeFileMap;

    /**
     * Get the active CursorSetter Instance 
     * @return
     */
    public static CursorSetter getInstance(){
        if(instance == null){
            CursorSetter.instance = new CursorSetter();
        }

        return CursorSetter.instance;
    }

    Pixmap Pencil, Circle, Box, Bucket, Line, Brush;
    private CursorSetter(){
        modeFileMap = new HashMap<PaintModes, FileHandle>();
        modeFileMap.put(PaintModes.Pencil, Gdx.files.internal("pencil.png"));
        modeFileMap.put(PaintModes.Circle, Gdx.files.internal("circle.png"));
        modeFileMap.put(PaintModes.Line, Gdx.files.internal("line.png"));
        modeFileMap.put(PaintModes.Box, Gdx.files.internal("box.png"));
        modeFileMap.put(PaintModes.Bucket, Gdx.files.internal("bucket.png"));
        modeFileMap.put(PaintModes.Brush, Gdx.files.internal("cursor.png"));
        
        cursors = new Cursor[6];

        Pencil = new Pixmap(modeFileMap.get(PaintModes.Pencil));
        Circle = new Pixmap(modeFileMap.get(PaintModes.Circle));
        Box = new Pixmap(modeFileMap.get(PaintModes.Box));
        Bucket = new Pixmap(modeFileMap.get(PaintModes.Bucket));
        Line = new Pixmap(modeFileMap.get(PaintModes.Line));
        Brush = new Pixmap(modeFileMap.get(PaintModes.Brush));

        cursors[PaintModes.Pencil.ordinal()] = Gdx.graphics.newCursor(Pencil,15,15);
        cursors[PaintModes.Circle.ordinal()] = Gdx.graphics.newCursor(Circle,15,15);
        cursors[PaintModes.Line.ordinal()] = Gdx.graphics.newCursor(Line, 15,15);
        cursors[PaintModes.Box.ordinal()] = Gdx.graphics.newCursor(Box, 15,15);
        cursors[PaintModes.Bucket.ordinal()] = Gdx.graphics.newCursor(Bucket, 15,15);
        cursors[PaintModes.Brush.ordinal()] = Gdx.graphics.newCursor(Brush, 15,15);
        
    }

    /**
     * Set the active cursor based on the PaintMode type 
     */
    public void setCursor(PaintModes mode){
        Gdx.graphics.setCursor(cursors[mode.ordinal()]);
    }

    /**
     * Get the files involved in the cursor setting 
     * @return
     */
    public Vector<FileHandle> getFiles(){
        return new Vector<FileHandle>(modeFileMap.values());
    }

    /**
     * Return the different paint modes we can have (idk why i even made this )
     * @return
     */
    public Vector<PaintModes> getModes(){return new Vector<PaintModes>(modeFileMap.keySet());}

    public void dispose(){
        Pencil.dispose();
        Circle.dispose();
        Box.dispose();
        Bucket.dispose();
        Line.dispose();
    }

}
