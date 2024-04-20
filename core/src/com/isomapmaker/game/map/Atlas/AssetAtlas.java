package com.isomapmaker.game.map.Atlas;

import java.util.HashMap;
import java.util.Vector;

import com.isomapmaker.game.map.Assets.Asset;

/**
 * Provides the assets from a asset file 
 * 
 * The asset containers are made up from all of the asset files for a given tile type
 */
public class AssetAtlas {
    HashMap<String, AssetPack> atlas;
    HashMap<Integer, String> idMap;
    
    private int id;
    public int getId(){return id;}

    /**
     * Simple HashMap for asset names and the assets themselves
     * @param id an id for this map 
     */
    public AssetAtlas(int id){
        this.atlas = new HashMap<String, AssetPack>();
        this.idMap = new HashMap<Integer, String>();
        this.id = id;
    }

    public Asset getAsset(String name){ return atlas.get(name).asset;} 
    /**
     * Set a new asset into the map 
     * @param name Name of the asset
     * @param asset The actual asset 
     * @param id an id for the asset 
     */
    public void setAsset(String name, Asset asset, int id){ 
        this.atlas.put(name, new AssetPack(name, asset, id));
        this.idMap.put(id, name);
    }

    /**
     * Get a list of keys for the assets contained in this map
     * @return
     */

    Vector<String> keyCollection = new Vector<String>();
    public Vector<String> keys(){
        keyCollection.clear();
        keyCollection.addAll(atlas.keySet());
        return keyCollection;
    }

    /**
     * Get the id of an asset 
     * @param name
     * @return
     */
    public int getAssetId(String name){
        return this.atlas.get(name).id;
    }

    /**
     * Get an asset based on the id
     * @param id
     * @return
     */
    public Asset getAsset(int id){
        return atlas.get(idMap.get(id)).asset;
    }

    public class AssetPack{
        public String name;
        public int id;
        public Asset asset;
        public AssetPack(String name, Asset asset, int id){
            this.name = name;
            this.asset = asset;
            this.id = id;
        }


    }
}
