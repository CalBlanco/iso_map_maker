package com.isomapmaker.game.map.Assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;

/**
 * Object container 
 */
public class Obj extends Asset {

    boolean isSolid;
    WallQuadrant rotation;

    public Obj(String name, TileType type, TextureRegion region, String id){
        super(name,type,region, id);

    }
    public Obj(String name, TileType type, TextureRegion region, String id,boolean isSolid) {
        super(name, type, region, id);
        this.isSolid = isSolid;
        //TODO Auto-generated constructor stub
    }
    
    public boolean isSolid(){return isSolid;}
    public void setSolid(boolean isSolid){this.isSolid = isSolid;}
    public void setRotation(WallQuadrant rotation){this.rotation = rotation;}
    public WallQuadrant getRotation(){return this.rotation;}
    
}
