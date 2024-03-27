package com.isomapmaker.game.map.Tiles;

import java.util.Dictionary;
import java.util.Hashtable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Object implements SimpleTile {
    private TextureRegion texture;
    Dictionary<String, Boolean> flags;
    
    public Vector2 size;
    

    public Object(TextureRegion texture, Vector2 size){
        this.texture = texture;
        this.size = size;
        this.flags = new Hashtable<String, Boolean>();
    }


    @Override
    public TextureRegion getTexture() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTexture'");
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
    
}
