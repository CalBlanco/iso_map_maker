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
    /**
     * Constructor for Map Loader
     * @param map : The starting map you would like to render
     * @param assets : The asset loader we are using
     */
    public MapLoader(String[][] map, AssetLoader assets){
        this.strMap = map; // save the map
        this.assets = assets;
        this.tileMap = new TextureData[MAX_SIZE][MAX_SIZE];
        //build init map
        buildTileMap();
    }
    /**
     * Struct for managing texture data
     */
    public class TextureData{
        public TextureRegion tr;
        public String name;
        public Vector2 size;
        public Vector2 pos;
        public Vector2 tilePos;
        public int selection;
        public TextureData(TextureRegion tr, String name, String size_str, int row, int col, int sel){
            this.tr = tr;
            this.name = name;
            int width,height;
            this.selection = sel;
            width = Integer.parseInt(size_str.split("x",2)[0]);
            height = Integer.parseInt(size_str.split("x",2)[1]);
            tilePos = new Vector2(row,col);
            this.size = new Vector2(width,height);
            pos = IsoUtil.worldToIsometric(tilePos, size);
        }

               
    }

    /**
     * Create a texture data object from tile info
     * @param tileStr : the tile we are painting
     * @param row : the row to paint it at
     * @param col : the column
     * @return TextureData for that location
     */
    private TextureData parseTile(String tileStr, int row, int col){ // convert a tile like Grass_1:8 into a texture region
        
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
                tileMap[i][j] = td;
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
        Vector2 hoverTile = IsoUtil.isometricToWorld(new Vector2(i,j), new Vector2(128,64));
        return "Type: " + td.name +", \nSelection: " + td.selection +"\nTile: ("+hoverTile.x +", " +hoverTile.y+")\nWorld: ("+td.pos.x+", "+td.pos.y+")";
    }



}
