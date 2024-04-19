package com.isomapmaker.game.map.Atlas;

import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.AssetFactory;
import com.isomapmaker.game.map.Assets.Floor;
import com.isomapmaker.game.map.Assets.Wall;
import com.isomapmaker.game.map.Assets.Obj;
import com.isomapmaker.game.map.Atlas.enums.TileType;

public class AssetContainer {
    HashMap<String, AssetAtlas> assets;

    AssetFactory factory;
    public AssetContainer(){
        this.assets = new HashMap<String, AssetAtlas>();
        this.factory = new AssetFactory();
    }


    public Vector<String> keys(){
        return new Vector<String>(this.assets.keySet());
    }

    public Vector<String> getRegionNames(String name){
        System.out.println(assets.get(name).keys().toString());
        return this.assets.get(name).keys();
    }

    public void addAssetToAtlas(String name, String assetName, Asset asset, int id){
        try{
            this.assets.get(name).setAsset(assetName,asset);
        }
        catch(Exception e){
            this.assets.put(name, new AssetAtlas(id));
            this.assets.get(name).setAsset(assetName, asset);
        }
    }

    public Asset getAssetFromAtlas(String name, String assetName){
        return this.assets.get(name).getAsset(assetName);
    }

    /**
     * Add an entire Texture Atlas to the asset container instead of one by one 
     * @param name
     * @param id
     * @param textures
     */
    public void addTextureAtlas(String name, TileType type, String sid, TextureAtlas textures){
        Array<TextureAtlas.AtlasRegion> regions = textures.getRegions();
        int id = Integer.parseInt(sid);
        this.assets.put(name, new AssetAtlas(id));
        for(int i=0; i<regions.size; i++){
            String regionName = regions.get(i).name;
            System.out.println(regionName);
            TextureAtlas.AtlasRegion r =  regions.get(i);
            
            try{
                switch(type){
                    case Floor:
                        this.assets.get(name).setAsset(regionName, (Floor)factory.createAsset(regionName, TileType.Floor, r));
                        break;
                        
                    case Wall:
                        this.assets.get(name).setAsset(regionName, (Wall)factory.createAsset(regionName, TileType.Wall, r));
                        break;
                        
                    case Object:
                        this.assets.get(name).setAsset(regionName, (Obj)factory.createAsset(regionName, TileType.Object, r));
                        break;
                }
                
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    }
    
}
