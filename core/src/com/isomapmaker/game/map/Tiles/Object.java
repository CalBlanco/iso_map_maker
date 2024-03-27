package com.isomapmaker.game.map.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Object implements SimpleTile {
    private TextureRegion texture;

    
    public Vector2 size;
    

    public Object(TextureRegion texture, Vector2 size){
        this.texture = texture;
        this.size = size;
    }


    @Override
    public TextureRegion getTexture() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTexture'");
    }
    
}
