package com.isomapmaker.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileLayer {
    /**
     *  Tile Layer represents a Z cordinate layer of the drawn map
     */
    
    
    TextureData loadedTexture = null; // temp texture for usage throughout the game
    AssetLoader assets; // assetloader to get textures from
    int[] tileOffset = new int[]{0,0}; // the offset to apply to the tile (my thinking is that something +1 z up will need to be drawn a tile away etc..)
    int maxSize = 400; // var to store max size (i think i might not need the static final and just set the default up here?)

    TextureData[][] layerMap = new TextureData[maxSize][maxSize]; // actual map

    // Layer meta data? just gonna make it all public so it can be changed whenever by whoever lmao its JUST meta data(words will most definetly not be eaten here idk what you are talking about)
    public String layerName = "layer";

    /**
     * Create a generic TileLayer providing an AssetLoader. This will not have a map and not render anything
     * @param assets The AssetLoader we are pulling textures from
     */
    public TileLayer(AssetLoader assets){
        this.assets = assets;
    }

    /**
     * Create a TileLayer with an initial map loaded
     * @param assets AssetLoader for textures
     * @param startMap initial map to draw
     */
    public TileLayer(AssetLoader assets, String[][] startMap){
        this.assets = assets;
        loadMap(startMap);
    }
    
    /**
     * Create a TileLayer with an initial map and offset applied to tiles
     * @param assets AssetLoader for textures
     * @param startMap Initial map to draw
     * @param tileOffset Array to offset tiles (should be 2 long  i.e [x,y])
     */
    public TileLayer(AssetLoader assets, String[][] startMap, int[] tileOffset){
        this.assets = assets;
        this.tileOffset = tileOffset;
        loadMap(startMap);
    }

    /**
     * Load a String map into this TileLayer's layerMap  
     * @param loadMap The string map to convert into our layerMap 
     */
    public void loadMap(String[][] startMap){
        this.layerMap = new TextureData[this.maxSize][this.maxSize];
        for(int i=0; i <startMap.length; i++){
            for(int j=0; j < startMap[i].length; j++){
                loadedTexture = parseTile(startMap[i][j], i, j);
                if(loadedTexture == null) continue;
                layerMap[i][j] = loadedTexture;
            }
        }
    }

    /**
     * Save this TileLayer's layerMap into a String[][] array and return that array
     * @return The layerMap string representation
     */
    public String[][] saveMap(){
        String[][] saveMap = new String[this.maxSize][this.maxSize];
        for(int i=0; i<this.maxSize; i++){
            for(int j=0; j<this.maxSize; j++){
                loadedTexture = layerMap[i][j];
                if (loadedTexture == null) continue;
                saveMap[i][j] = loadedTexture.name+":"+loadedTexture.selection;
            }
        }

        return saveMap;
    }
    

    // Actually draw the layer
    public void render(SpriteBatch batch){
        for(int i=0; i<this.maxSize; i++){
            for(int j=0; j<this.maxSize; j++){
                loadedTexture = layerMap[i][j];
                if(loadedTexture == null) continue;
                batch.draw(loadedTexture.tr,loadedTexture.pos.x,loadedTexture.pos.y);
            }
        }
    }
    
    /**
     * Create a TextureData item based on an input string and position
     * @param tileStr the tile we are parsing
     * @param row the x cordinate of the tile
     * @param col the y cordinate of the tile 
     * @return A TextureData representing the asset, if there is a tileOffset for this layer it is applied here
     */
    private TextureData parseTile(String tileStr, int row, int col){ // convert a tile like Grass_1:8 into a texture region
        if (tileStr == null) return null;
        String [] split = tileStr.split(":",2);
        String[] info = assets.get(split[0]);

        return new TextureData(assets.loadTextureRegion(split[0], Integer.parseInt(split[1])), split[0], info[1], row+tileOffset[0], col+tileOffset[1], Integer.parseInt(split[1]), false);
    }

    /**
     * Check if the tile would be placed in bounds
     * @param x
     * @param y
     * @return True if in bounds, False if out of bounds
     */
    public boolean inBounds(int x, int y){
        
        return (x >= 0 && x < this.maxSize && y >= 0 && y < this.maxSize);
    }

    /**
     * Override that calls the int version of inBounds after casting arguments
     * @param x
     * @param y
     * @return
     */
    public boolean inBounds(float x, float y){
        return inBounds((int)x, (int)y);
    }

    //add tile
    public void addTile(TextureData td){
        if( inBounds(td.tilePos.x, td.tilePos.y)) layerMap[(int)td.tilePos.x][(int)td.tilePos.y] = td;
        return;
    }

    //remove a tile based on the x and y position
    public void removeTile(int x, int y){
        if( inBounds(x, y)) layerMap[x][y] = null;
        return;
    }

    //get tile
    public TextureData getTile(int x, int y){
        if( inBounds(x, y) ) return layerMap[x][y];
        return null; 
    }

    public TextureData getTile(float x, float y){return getTile((int)x, (int)y);}

    public void removeTile(float x, float y){
        removeTile((int)x, (int)y);
    }




}
