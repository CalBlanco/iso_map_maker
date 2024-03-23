package com.isomapmaker.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.util.IsoUtil;

public class MapLoader {
    final static int MAX_SIZE = 400;
    private String[][] strMap; //str representation of the map
    private AssetLoader assets; // asset loading 
    private TextureData[][] tileMap; // actual map that gets rendered (ik this is 2x storage for strMap and tile map but )

    private TextureData loadedData;
    /**
     * Constructor for Map Loader
     * @param map : The starting map you would like to render
     * @param assets : The asset loader we are using
     */
    public MapLoader(String[][] map, AssetLoader assets){
        this.strMap = new String[MAX_SIZE][MAX_SIZE]; // save the map
        loadedData = null;
        for(int i=0; i< map.length; i++){ // copy the map into our allocated map
            for(int j=0; j<map[i].length; j++){
                strMap[i][j] = map[i][j];
            }
        }
        this.assets = assets;
        this.tileMap = new TextureData[MAX_SIZE][MAX_SIZE];
        //build init map
        buildTileMap();
    }
    

    /**
     * Create a texture data object from tile info
     * @param tileStr : the tile we are painting
     * @param row : the row to paint it at
     * @param col : the column
     * @return TextureData for that location
     */
    private TextureData parseTile(String tileStr, int row, int col){ // convert a tile like Grass_1:8 into a texture region
        if (tileStr == null) return null;
        String [] split = tileStr.split(":",2);
        String[] info = assets.get(split[0]);

        return new TextureData(assets.loadTextureRegion(split[0], Integer.parseInt(split[1])), split[0], info[1], row, col, Integer.parseInt(split[1]));
    }

    

    /**
     * Print the string map
     */
    public void printMap(){
        for(int i=0; i<strMap.length; i++){
            for(int j=0; j<strMap.length; j++){
                System.out.print(strMap[i][j]+ ", ");
            }
            System.out.println("");
        }
    }

    /**
     * Update the tile map from the string map
     */
    public void buildTileMap(){
        for(int i=0; i<strMap.length; i++){
            for(int j=0; j<strMap[i].length; j++){
                TextureData td = parseTile(strMap[i][j],i,j);
                if (td == null) continue;
                tileMap[i][j] = td;
            }
        }
    }

    public void saveTileMap(){
        for(int i=0; i<tileMap.length; i++){
            for(int j=0; j < tileMap[i].length; j++){
                loadedData = tileMap[i][j];
                if(loadedData != null) {
                    strMap[i][j] = loadedData.name+":"+loadedData.selection;
                    continue;
                }
                strMap[i][j] = "";
            }
        }
    }

    /**
     * Render the classes tile map
     * @param batch
     */
    public void drawTileMap(SpriteBatch batch){
        for(int i=0; i<strMap.length; i++){
            for(int j=0; j<strMap[i].length; j++){
                TextureData td = tileMap[i][j];
                if(td == null)  continue;
                batch.draw(td.tr,td.pos.x,td.pos.y);
            }
        }
    }

    public void render(SpriteBatch batch){
        drawTileMap(batch);
    }

    /**
     * Get Texture data at a certain tile or return null if unable to locate (this relies on the tile being present in the string map as the tile map is pre-allocated to a certain size)
     * @param i
     * @param j
     * @return
     */
    public TextureData getTextureData(float row, float column){
        int i,j;
        i = (int)Math.floor(row);
        j = (int)Math.floor(column);
        
        if (i < 0 || j < 0 || i > strMap.length-1 || j > strMap[i].length-1) return null;
        
        return tileMap[i][j];
    }


    public String textureDataString(float row, float column){
        int i,j;
        i = (int)Math.floor(row);
        j = (int)Math.floor(column);
        
        if (i < 0 || j < 0 || i > strMap.length-1 || j > strMap[i].length-1) return null;
        
        TextureData td = tileMap[i][j];
        if (td==null) return "NULL";
        return "Tile: " + td.name +"-"+ td.selection +"\nTile-Pos: ("+td.tilePos.x +", " +td.tilePos.y+")\nWorld-Pos: ("+td.pos.x+", "+td.pos.y+")";
    }

    // Tile wise are we in bounds
    public boolean isOutOfBounds(int x, int y){
        
        return (x < 0 | x > MAX_SIZE-1 | y < 0 | y > MAX_SIZE-1);
    }

    public void addTile(TextureData td){
        if(isOutOfBounds((int)td.tilePos.x, (int)td.tilePos.x)) return;
        tileMap[(int)td.tilePos.x][(int)td.tilePos.y] = td;
    }

    public void removeTile(int x, int y){
        if(isOutOfBounds(x,y)) return;
        tileMap[x][y] = null;
    }

    public void removeTile(float x, float y){
        removeTile((int)x, (int)y);
    }



}
