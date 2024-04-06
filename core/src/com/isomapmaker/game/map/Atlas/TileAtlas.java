package com.isomapmaker.game.map.Atlas;

import java.util.HashMap;

import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.util.XmlParse;

public class TileAtlas {
    private static TileAtlas instance;

    private HashMap<TileType, AtlasContainer> assets;

    public static TileAtlas getInstance(){
        if (TileAtlas.instance == null){
            TileAtlas.instance = new TileAtlas();
        }

        return TileAtlas.instance;
    }

    private TileAtlas(){
        assets = XmlParse.getAssets("asset_atlas.xml");
    }

    public void updateAssets(HashMap<TileType, AtlasContainer> assets){
        this.assets = assets;
    }

    public AtlasContainer getAssetsByType(TileType type){
        return this.assets.get(type);
    }
}
