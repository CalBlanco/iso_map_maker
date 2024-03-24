package com.isomapmaker.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.util.IsoUtil;

public class TileLayer {
    /**
     *  Tile Layer represents a Z cordinate layer of the drawn map
     */
    
    TextureData loadedTexture = null; // temp texture for usage throughout the game
    AssetLoader assets; // assetloader to get textures from
    int[] tileOffset = new int[]{0,0}; // the offset to apply to the tile (my thinking is that something +1 z up will need to be drawn a tile away etc..)
    int maxSize = 400; // var to store max size (i think i might not need the static final and just set the default up here?)

    Vector2 worldOffset = new Vector2(0,0);
    TextureData[][][] layerMap = new TextureData[maxSize][maxSize][2]; // actual map
    Vector2 loadVector = new Vector2();
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
    public TileLayer(AssetLoader assets, String[][][] startMap){
        this.assets = assets;
        loadMap(startMap);
    }
    
    /**
     * Create a TileLayer with an initial map and offset applied to tiles
     * @param assets AssetLoader for textures
     * @param startMap Initial map to draw
     * @param tileOffset Array to offset tiles (should be 2 long  i.e [x,y])
     */
    public TileLayer(AssetLoader assets, String[][][] startMap, int[] tileOffset){
        this.assets = assets;
        this.tileOffset = tileOffset;
        this.worldOffset = IsoUtil.worldToIsometric(new Vector2(tileOffset[0], tileOffset[1]), IsoUtil.FLOOR_SIZE);
        loadMap(startMap);
    }

    /**
     * Load a String map into this TileLayer's layerMap  
     * @param loadMap The string map to convert into our layerMap 
     */
    public void loadMap(String[][][] startMap){
        this.layerMap = new TextureData[this.maxSize][this.maxSize][2];
        for(int i=0; i <startMap.length; i++){
            for(int j=0; j < startMap[i].length; j++){

                try{
                    loadedTexture = parseTile(startMap[i][j][0], i, j);
                }
                catch(Exception e){
                    loadedTexture = null;
                }
                layerMap[i][j][0] = loadedTexture;

                try{
                    loadedTexture = parseTile(startMap[i][j][1], i, j);
                }
                catch(Exception e){
                    loadedTexture = null;
                }
                layerMap[i][j][1] = loadedTexture;
            }
        }
    }

    /**
     * Save this TileLayer's layerMap into a String[][] array and return that array
     * @return The layerMap string representation
     */
    public String[][][] saveMap(){
        String[][][] saveMap = new String[this.maxSize][this.maxSize][2];
        for(int i=0; i<this.maxSize; i++){
            for(int j=0; j<this.maxSize; j++){
                loadedTexture = layerMap[i][j][0];
                if (loadedTexture == null) continue;
                saveMap[i][j][0] = loadedTexture.name+":"+loadedTexture.selection;
            }
        }

        return saveMap;
    }
    

    // Actually draw the layer
    public void render(SpriteBatch batch){
        for(int i=this.maxSize-1; i >=0; i--){
            for(int j=this.maxSize-1; j>=0; j--){
                
                loadedTexture = layerMap[i][j][0];
                try{
                    loadVector = loadedTexture.getPos();
                    batch.draw(loadedTexture.tr,loadVector.x+this.worldOffset.x,loadVector.y+this.worldOffset.y);
                }
                catch(Exception e){}

                loadedTexture = layerMap[i][j][1];
                try{
                    loadVector = loadedTexture.getPos();
                    batch.draw(loadedTexture.tr,loadVector.x+this.worldOffset.x,loadVector.y+this.worldOffset.y);
                }
                catch(Exception e){}

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
        return new TextureData(assets.loadTextureRegion(split[0], Integer.parseInt(split[1])), split[0], info[1], row, col, Integer.parseInt(split[1]), false);
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
        if( inBounds(td.tilePos.x, td.tilePos.y)) layerMap[(int)td.tilePos.x][(int)td.tilePos.y][0] = td;
        return;
    }

    public void addWall(TextureData td){
        if( inBounds(td.tilePos.x, td.tilePos.y)) layerMap[(int)td.tilePos.x][(int)td.tilePos.y][1] = td;
        return;
    }

    //remove a tile based on the x and y position
    public void removeTile(int x, int y){
        if( inBounds(x, y)) layerMap[x][y][0] = null;
        return;
    }

    //remove a tile based on the x and y position
    public void removeWall(int x, int y){
        if( inBounds(x, y)) layerMap[x][y][1] = null;
        return;
    }


    //get tile
    public TextureData getTile(int x, int y){
        if( inBounds(x, y) ) return layerMap[x][y][0];
        return null; 
    }

     //get tile
     public TextureData getWall(int x, int y){
        if( inBounds(x, y) ) return layerMap[x][y][1];
        return null; 
    }

    public TextureData getTile(float x, float y){return getTile((int)x, (int)y);}
    public TextureData getWall(float x, float y){ return getWall((int)x, (int)y);}

    public void removeTile(float x, float y){ removeTile((int)x, (int)y);}

    public int[] getOffset(){return tileOffset;}
    

}
