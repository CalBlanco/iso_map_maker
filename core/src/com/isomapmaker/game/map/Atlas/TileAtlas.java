package com.isomapmaker.game.map.Atlas;

import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.AssetFactory;
import com.isomapmaker.game.map.Assets.Floor;
import com.isomapmaker.game.map.Assets.Obj;
import com.isomapmaker.game.map.Assets.Wall;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.util.XmlParse;

public class TileAtlas {
    private static TileAtlas instance;

    private HashMap<TileType, AssetContainer> assets;

    public boolean isLoading;
    public static TileAtlas getInstance(){
        if (TileAtlas.instance == null){
            TileAtlas.instance = new TileAtlas();
        }

        return TileAtlas.instance;
    }

    private TileAtlas(){
        isLoading = true;
        assets = XmlParse.getAssets("asset_atlas.xml"); 
        isLoading = false;
        
    }

   
    

    public AssetContainer getAssetsByType(TileType type){return this.assets.get(type);}

    public Asset get(TileType type, String name, String assetName){
        return assets.get(type).getAssetFromAtlas(name, assetName);
    }

    public Asset get(TileType type, int atlasId, int assetId){
        return assets.get(type).getAssetById(atlasId, assetId);
    }
    

}
