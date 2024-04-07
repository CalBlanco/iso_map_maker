package com.isomapmaker.game.map.Atlas;

import java.util.HashMap;
import java.util.Vector;

import com.isomapmaker.game.map.Assets.Asset;

public class AssetAtlas {
    HashMap<String, Asset> atlas;
    
    public AssetAtlas(){
        this.atlas = new HashMap<String, Asset>();

    }

    public Asset getAsset(String name){ return atlas.get(name);} 
    public void setAsset(String name, Asset asset){ this.atlas.put(name, asset);}

    public Vector<String> keys(){
        return new Vector<String>(atlas.keySet());
    }
}
