package com.isomapmaker.game.map.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wall implements SimpleTile {
    private TextureRegion texture;

    public boolean isSolid; // is this wall passable
    public boolean isNorthSouth; // is this wall north / south facing 

    public Wall(TextureRegion texture, boolean isSolid, boolean isNorthSouth){
        this.texture = texture;
        this.isSolid = isSolid;
        this.isNorthSouth = isNorthSouth;
    }

    @Override
    public TextureRegion getTexture(){
        return texture;
    }

    
}
