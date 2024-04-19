package com.isomapmaker.game.map.Assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.isomapmaker.game.map.Atlas.enums.TileType;

public abstract class Asset {
    TextureRegion region;
    TileType type;
    String name;

    public Asset(String name, TileType type, TextureRegion region, String fullId){
        this.name = name;
        this.region = region;
        this.type = type;
        this.fullId = fullId;
        System.out.println("[ADDED ASSET] : " + name + ", " + type.toString() + ", " + fullId);
    }

    // Save the full id of the asset for saving
    String fullId;
    /**
     * Setter for full id
     * @param fullId
     */
    public void setFullId(String fullId){
        this.fullId = fullId;
    }

    /**
     * getter for full id
     * @return
     */
    public String getFullId(){return fullId;}

    public String getName(){return name;}
    public TextureRegion getRegion(){return region;}
    public TileType getType(){return type;}


}
