package com.isomapmaker.game.map.Atlas;

import java.util.HashMap;
import java.util.Vector;

import com.isomapmaker.game.map.Assets.Asset;

public class AssetContainer {
    HashMap<String, AssetAtlas> assets;

    public AssetContainer(){
        this.assets = new HashMap<String, AssetAtlas>();
    }


    public Vector<String> keys(){
        return new Vector<String>(this.assets.keySet());
    }

    public Vector<String> getRegionNames(String name){
        return this.assets.get(name).keys();
    }

    public void addAssetToAtlas(String name, String assetName, Asset asset){
        try{
            this.assets.get(name).setAsset(assetName,asset);
        }
        catch(Exception e){
            this.assets.put(name, new AssetAtlas());
            this.assets.get(name).setAsset(assetName, asset);
        }
    }

    public Asset getAssetFromAtlas(String name, String assetName){
        return this.assets.get(name).getAsset(assetName);
    }
    
}
