package com.isomapmaker.game.map.Assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;

/**
 * Wall asset containing any wall related logic 
 */
public class Wall extends Asset {
    WallQuadrant quadrant;

    public Wall(String name, TileType type, TextureRegion region, String id){
        super(name,type,region, id);
        quadrant = WallQuadrant.bottom;
    }
    public Wall(String name, TileType type, TextureRegion region, String id, WallQuadrant quadrant) {
        super(name, type, region, id);
        this.quadrant = quadrant;
        
    }

    public WallQuadrant getQuadrant(){return quadrant;}
    public void setQuadrant(WallQuadrant quadrant){this.quadrant = quadrant;}

    
}
