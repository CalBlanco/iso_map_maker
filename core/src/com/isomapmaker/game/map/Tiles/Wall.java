package com.isomapmaker.game.map.Tiles;

import java.util.Dictionary;
import java.util.Hashtable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wall implements SimpleTile {
    private TextureRegion texture;

    Dictionary<String, Boolean> flags;

    private String name;

    public Wall(TextureRegion texture, String name){
        this.texture = texture;
        this.flags = new Hashtable<String, Boolean>();
        this.name = name;
    }

    @Override
    public TextureRegion getTexture(){
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
