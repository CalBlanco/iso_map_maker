package com.isomapmaker.game.map.Assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.isomapmaker.game.map.Atlas.enums.TileType;

public class AssetFactory {
    public AssetFactory(){}

    public Asset createAsset(String name,TileType type, TextureRegion region, String id){
        switch(type){
            case Floor:
                return new Floor(name, type, region, id);
            case Wall:
                return new Wall(name, type, region, id);
            default:
                return new Obj(name, type, region, id);
        }
    }


}
