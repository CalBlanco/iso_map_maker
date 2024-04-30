package com.isomapmaker.game.map.Atlas;

import java.util.HashMap;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.util.XmlParse;

/**
 * Provides all the assets to the game for placement and other things (Singleton Pattern so that only the required amount of texture atlas's are loaded)
 */
public class TileAtlas {

    private static TileAtlas instance; 

    private HashMap<TileType, AssetContainer> assets;

    public boolean isLoading;

    /**
     *  Return the active TileAtlas 
     * @return
     */
    public static TileAtlas getInstance(){
        if (TileAtlas.instance == null){
            TileAtlas.instance = new TileAtlas();
        }

        return TileAtlas.instance;
    }

    private TileAtlas(){
        isLoading = true;
        assets = XmlParse.getAssets("asset_atlas.xml"); // Load in the the assets 
        isLoading = false; 
        
    }

   
    
    /**
     * Return the asset container associated with the provided TileType
     * @param type
     * @return
     */
    public AssetContainer getAssetsByType(TileType type){return this.assets.get(type);}

    /**
     * Get an asset based on the TileType, file name, and asset region name 
     * @param type
     * @param name
     * @param assetName
     * @return
     */
    public Asset get(TileType type, String name, String assetName){
        return assets.get(type).getAssetFromAtlas(name, assetName);
    }

    /**
     * Get the asset based on the Atlas' id and the assets' id 
     * @param type
     * @param atlasId
     * @param assetId
     * @return
     */
    public Asset get(TileType type, int atlasId, int assetId){
        return assets.get(type).getAssetById(atlasId, assetId);
    }

    
    public void dispose(){
        TileType[] tpes = TileType.values();
        for(int i=0; i<tpes.length; i++){
            assets.get(tpes[i]).dispose();
        }
    }
    

}
