package com.isomapmaker.game.map.Assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.isomapmaker.game.map.Atlas.enums.TileType;

public abstract class Asset {
    TextureRegion region;
    TileType type;
    String name;

    public Asset(String name, TileType type, TextureRegion region){
        this.name = name;
        this.region = region;
        this.type = type;
    }

    public String getName(){return name;}
    public TextureRegion getRegion(){return region;}
    public TileType getType(){return type;}

}
