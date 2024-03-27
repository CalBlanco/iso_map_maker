package com.isomapmaker.game.map.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Floor implements SimpleTile {
    
    private TextureRegion texture;

    public boolean isTraversable;
    public float traverseDamage;

    public Floor(TextureRegion texture){
        this.texture = texture;
        this.isTraversable = true;
        this.traverseDamage = 0f;
    }

    public Floor(TextureRegion texture, boolean isTraversable, float traverseDamage){
        this.texture = texture;
        this.isTraversable = isTraversable;
        this.traverseDamage = traverseDamage;
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }
}
