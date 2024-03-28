package com.isomapmaker.game.map.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Hashtable;
import java.util.Dictionary;

public class Floor implements SimpleTile {
    
    private TextureRegion texture;


    Dictionary<String, Boolean> flags;

    private String name;

    public Floor(TextureRegion texture, String name){
        this.texture = texture;
        this.flags = new Hashtable<String, Boolean>();
        this.name = name;
    }


    @Override
    public TextureRegion getTexture() {
        return texture;
    }

    @Override
    public void setFlag(String name, boolean value) {
        this.flags.put(name,value);
    }

    @Override
    public boolean getFlag(String name) {
        try{
            return this.flags.get(name);
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    
}
