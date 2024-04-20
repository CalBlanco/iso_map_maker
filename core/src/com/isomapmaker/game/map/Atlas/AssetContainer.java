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

/**
 * Contains the hashmaps for 
 */
public class AssetContainer {
    HashMap<String, AssetAtlas> assets;
    HashMap<Integer, String> idMap;
    Vector<TextureAtlas> atlases;
    AssetFactory factory;

    
    public AssetContainer(){
        this.atlases = new Vector<TextureAtlas>();
        this.assets = new HashMap<String, AssetAtlas>();
        this.idMap = new HashMap<Integer, String>();
        this.factory = new AssetFactory();
    }


    public Vector<String> keys(){
        return new Vector<String>(this.assets.keySet());
    }

    public Vector<String> getRegionNames(String name){
        
        return this.assets.get(name).keys();
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
        this.atlases.add(textures);
        Array<TextureAtlas.AtlasRegion> regions = textures.getRegions();
        int id = Integer.parseInt(sid);
        this.assets.put(name, new AssetAtlas(id));
        this.idMap.put(id,name);
        for(int i=0; i<regions.size; i++){
            String regionName = regions.get(i).name;
            
            TextureAtlas.AtlasRegion r =  regions.get(i);
            
            try{
                switch(type){
                    case Floor:
                        this.assets.get(name).setAsset(regionName, (Floor)factory.createAsset(regionName, TileType.Floor, r, id+"_"+i),i);
                        break;
                        
                    case Wall:
                        this.assets.get(name).setAsset(regionName, (Wall)factory.createAsset(regionName, TileType.Wall, r, id+"_"+i),i);
                        break;
                        
                    case Object:
                        this.assets.get(name).setAsset(regionName, (Obj)factory.createAsset(regionName, TileType.Object, r, id+"_"+i),i);
                        break;
                }
                
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }

        
    }


    /**
     * Return an asset based on the id of the atlas and the id of the asset, returns null if unable to locate by id
     * @param atlasId
     * @param assetId
     * @return
     */
    public Asset getAssetById(int atlasId, int assetId){
        try{
            return this.assets.get(idMap.get(atlasId)).getAsset(assetId);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Trys to get the atlas' id, returns -1 if fails 
     * @param name
     * @return
     */
    public int getAtlasId(String name){
        try{
            return this.assets.get(name).getId();
        }
        catch(Exception e){
            return -1;
        }
    }


    /**
     * Get the Atlas' ID and Asset ID in a tuple (int[2]{atlasId, assetId})
     * @param name
     * @param assetName
     * @return
     */
    public int[] getIds(String name, String assetName){
        try{
            int atlasId = this.assets.get(name).getId();
            int assetId = this.assets.get(name).getAssetId(assetName);

            return new int[]{atlasId,  assetId};
        }
        catch(Exception e){
            return null;
        }
    }

    public void dispose(){
        for(int i=0; i<atlases.size(); i++){
            atlases.get(i).dispose();
        }
    }


    
}
