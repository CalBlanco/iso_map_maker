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

    private HashMap<TileType, AtlasContainer> atlas;
    private HashMap<TileType, AssetContainer> assets;


    public static TileAtlas getInstance(){
        if (TileAtlas.instance == null){
            TileAtlas.instance = new TileAtlas();
        }

        return TileAtlas.instance;
    }

    private TileAtlas(){
        atlas = XmlParse.getAssets("asset_atlas.xml"); 
        
        assets = new HashMap<TileType, AssetContainer>();
        
        convertAtlasToAssets();

    }

    private void convertAtlasToAssets(){
         
        TileType[] types = TileType.values();
        AssetFactory assetFac = new AssetFactory();

        // Convert the AtlasContainer into a AssetContainer, 3 deep 
        for(int typeIndex = 0; typeIndex<types.length; typeIndex++){
            assets.put(types[typeIndex], new AssetContainer());
            Vector<String> assetNames = atlas.get(types[typeIndex]).getAssetNames(); // Get the names of the atlas contained by this AtlasContainer 
            
            for(int assetIndex = 0; assetIndex<assetNames.size(); assetIndex++){ // loop over all the asset names we have
                Vector<String> regionNames = atlas.get(types[typeIndex]).getRegionNames(assetNames.get(assetIndex));

                for(int regionIndex = 0; regionIndex<regionNames.size(); regionIndex++){
                    String name = regionNames.get(regionIndex);
                    String assetName = assetNames.get(assetIndex);
                    TextureRegion region = atlas.get(types[typeIndex]).getRegion(assetName, name);
                    System.out.println(name +" , " + assetName);
                    switch(types[typeIndex]){
                        case Floor:
                            Floor f = (Floor)assetFac.createAsset(name, types[typeIndex], region );
                            assets.get(types[typeIndex]).addAssetToAtlas(assetName, name, f);
                            break;
                        case Wall:
                            Wall w = (Wall)assetFac.createAsset(name, types[typeIndex], region);
                            String[] wString = name.split("-");
                            WallQuadrant quad = WallQuadrant.valueOf(wString[1]);
                            w.setQuadrant(quad);
                            assets.get(types[typeIndex]).addAssetToAtlas(assetName, name, w);
                            break;
                        case Object:
                            Obj o = (Obj)assetFac.createAsset(name, types[typeIndex], region);
                            String[] oString = name.split("-");
                            WallQuadrant rot = WallQuadrant.valueOf(oString[1]);
                            o.setRotation(rot);
                            assets.get(types[typeIndex]).addAssetToAtlas(assetName, name, o);
                            break;
                    }
                }
            }
        }
    }

    public AssetContainer getAssetsByType(TileType type){return this.assets.get(type);}

    public Asset get(TileType type, String name, String assetName){
        return assets.get(type).getAssetFromAtlas(name, assetName);
    }

    public void dispose(){
        
    }

    /**
     * TileMap: {
     *  "Floor": {
     *      "Grass":
     *          {
     *          "yellow_grass": Floor(yellow_grass, Floor, TextureRegion)
     *      }
     *  }
     * }
     * 
     */
}
