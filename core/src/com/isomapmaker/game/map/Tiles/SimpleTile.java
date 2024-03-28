package com.isomapmaker.game.map.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * SimpleTile
 */
public interface SimpleTile {
    public TextureRegion getTexture();
    public void setFlag(String name, boolean value);
    public boolean getFlag(String name);
    public String getName();
}