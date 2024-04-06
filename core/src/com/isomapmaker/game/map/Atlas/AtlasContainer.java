package com.isomapmaker.game.map.Atlas;

import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AtlasContainer {
    HashMap<String, TextureAtlas> atMap;
    
    public AtlasContainer(){this.atMap = new HashMap<String ,TextureAtlas>();}
    public AtlasContainer(HashMap<String, TextureAtlas> atMap){ this.atMap = atMap;}

    public void addAtlas(String name, TextureAtlas atlas){
        atMap.put(name, atlas);
    }
    public TextureRegion getRegion(String name, String region){
        try{
            return atMap.get(name).findRegion(region);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Vector<String> getRegionNames(String name){
        try{
            Array<TextureAtlas.AtlasRegion> regions = atMap.get(name).getRegions();
            Vector<String> regionNames = new Vector<String>();
            for(int i=0; i<regions.size; i++){
                regionNames.add(regions.get(i).name);
            }

            return regionNames;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
