package com.isomapmaker.game.map.TileMaps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Floor;
import com.isomapmaker.game.map.Assets.Tile;
import com.isomapmaker.game.map.Assets.Wall;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.util.IsoUtil;
import com.isomapmaker.game.util.MapCopy;

/**
 * Class to control actual tiles that are active on the map 
 * 
 * Contains code to edit those tiles and the assets inside of them
 * 
 */
public class TileMap {

    private int size;
    
    private Tile[][] map;

    
    TextureRegion highlight;
    TextureRegion defaultTexture;

    public Vector2 tileOffset = new Vector2(0,0);


   


    /**
     * Create a new TileMap to be rendered and editable 
     * @param size The desired size (is passed from TileMapManager)
     * @param tileOffset The offset this map needs to be placed at (higher z's require larger offsets)
     * @param defaultFloor The default floor we want to be rendered
     */
    public TileMap(int size, Vector2 tileOffset, Floor defaultFloor){
        this.size = size;
        this.tileOffset = tileOffset;
        this.defaultTexture = null;
        //this.highlight = new TextureRegion(new Texture(Gdx.files.internal("highlight.png")));
        this.map = new Tile[size][size];
        reloadMap();
    }


    

    public Tile[][] getMapState(){return this.map;}
    public void setMapState(Tile[][] state){
        this.map = MapCopy.deepCopy(state);
    }
    public int getSize(){return size;}
    /**
     * Render this map based on its offset
     * @param b
     */

    private Vector2 renderTVector = new Vector2();
    private Vector2 outVector = new Vector2();
    public void render(SpriteBatch b){
        b.setColor(1f,1f,1f,1f);
        // iterate back to front
        for(int i=size-1; i>=0; i--){
            for(int j=size-1; j>=0; j--){
                // get world cordinates incorperating layer offset
                Vector2 wpos = IsoUtil.isometricToWorldPos(renderTVector.set(i,j).add(tileOffset.x, tileOffset.y), IsoUtil.FLOOR_SIZE, outVector);
                // render floor
                if(map[i][j] != null) map[i][j].render(b, wpos);
                
            }
        }

    }

/*
███████╗██╗      ██████╗  ██████╗ ██████╗ ███████╗
██╔════╝██║     ██╔═══██╗██╔═══██╗██╔══██╗██╔════╝
█████╗  ██║     ██║   ██║██║   ██║██████╔╝███████╗
██╔══╝  ██║     ██║   ██║██║   ██║██╔══██╗╚════██║
██║     ███████╗╚██████╔╝╚██████╔╝██║  ██║███████║
╚═╝     ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚══════╝
*/
    /**
     * Set the floor of the tile
     * @param x
     * @param y
     * @param f
     */
    public void setFloor(int x, int y, Asset f){
        if (!inBounds(x, y)) return;
        if (map[x][y] == null) map[x][y]= new Tile();
        map[x][y].setFloor(f);

    }

    /**
     * Get the floor for the tile
     * @param x
     * @param y
     * @return
     */
    public Floor getFloor(int x, int y){
        if (!hasTile(x, y)) return null;
        return map[x][y].getFloor();
    }
/*
██╗    ██╗ █████╗ ██╗     ██╗     ███████╗
██║    ██║██╔══██╗██║     ██║     ██╔════╝
██║ █╗ ██║███████║██║     ██║     ███████╗
██║███╗██║██╔══██║██║     ██║     ╚════██║
╚███╔███╔╝██║  ██║███████╗███████╗███████║
 ╚══╝╚══╝ ╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝
*/
    /**
     * Set the wall at a specified slot 
     * @param x
     * @param y
     * @param slot
     * @param w
     */
    public void setWall(int x, int y, WallQuadrant quad, Asset w){
        if(!inBounds(x, y)) return;
        if (map[x][y] == null) map[x][y] = new Tile();
        map[x][y].setWall(w, quad);
        
    }

    /**
     * Get a wall at a given position 
     * @param x X cordinate 
     * @param y Y cordinate 
     * @param slot Quadrant to place wall in 
     * @return
     */
    public Wall getWall(int x, int y, WallQuadrant slot){
        if (!hasTile(x, y)) return null;
        return map[x][y].getWall(slot);
    }

    /**
     * Determine if the tile provided and the wall quadrant provided share a wall with a neighboring tile
     * @param x
     * @param y
     * @param slot
     * @return
     */
    public boolean hasSharedWall(int x, int y, WallQuadrant slot){
        switch(slot){
            case right:
                if( getWall(x+1,y, WallQuadrant.left) != null ) return true;
            case left:
                if( getWall(x-1, y, WallQuadrant.right) != null ) return true;
            case top:
                if( getWall(x,y+1, WallQuadrant.bottom) != null ) return true;
            case bottom:
                if( getWall(x, y-1, WallQuadrant.top) != null ) return true;
        }

        return false;
    }
/*
 ██████╗ ██████╗      ██╗
██╔═══██╗██╔══██╗     ██║
██║   ██║██████╔╝     ██║
██║   ██║██╔══██╗██   ██║
╚██████╔╝██████╔╝╚█████╔╝
 ╚═════╝ ╚═════╝  ╚════╝     
*/

public void setObject(int x, int y, Asset o){
    if(!inBounds(x, y)) return;
    if(map[x][y] == null) map[x][y] = new Tile();
    map[x][y].setObject(o);
}



public void clearTile(int x, int y){
    if(!hasTile(x, y)) return;
    map[x][y].clearTile();
}

/*
██╗   ██╗████████╗██╗██╗     
██║   ██║╚══██╔══╝██║██║     
██║   ██║   ██║   ██║██║     
██║   ██║   ██║   ██║██║     
╚██████╔╝   ██║   ██║███████╗
 ╚═════╝    ╚═╝   ╚═╝╚══════╝
 */
    /**
     * Check if the given tile is within the bounds of our map
     * @param x
     * @param y
     * @return
     */
    public boolean inBounds(int x, int y){
        if(x < 0 || x > size-1 || y < 0 || y > size-1) return false;
        return true;
    }

    /**
     * Check if a given tile has a tile to place information on (No longer needed because all maps now start initilaized)
     * @param x
     * @param y
     * @return
     */
    public boolean hasTile(int x, int y){
        if(!inBounds(x, y)) return false;
        if(map[x][y] == null) return false;
        return true;
    }


    /**
     * Return pretty tile data
     * @param x X-cordinate of tile
     * @param y Y-coridnate of tile
     * @return Pretty string
     */
    public String getTileString(int x, int y){
        if(!inBounds(x, y)) return "";
        if(map[x][y] == null) return "";
        return map[x][y].toString();
    }



    /**
     * Iterating over all tiles create appropriate strings for saving
     * @return a string containing tile information for map loading
     */
    public String saveMap(){
        String out = ""; // final output
        String chunk = ""; // chunk / line 
        for(int i=0; i< size; i++){

            for(int j=0; j<size; j++){
                if(map[i][j] != null) chunk += map[i][j].saveString() +",";
            }

            
            if(chunk.length() == size) chunk = "=";

            out += chunk+"\n";
            chunk = ""; // reset the chunk
        }

        return out;
    }
    

    

    /**
     * Load in a map providing the tile loader for the tile to get information from 
     * Provide an array of lines from the map file 
     * @param inMap
     * @param loader
     */
    public void loadMap(String[] inMap ){
        

        for(int line=0; line < inMap.length && line < size; line++){ // ensure our provided map is not bigger than allowed map 
            // load a tile one by one here
            String[] tiles = inMap[line].split(",", size);
            if(inMap[line].equals("=")) continue;
            for(int tile=0; tile<tiles.length && tile < size; tile++){ // ensure we only place tiles that would fit in size
                if(map[line][tile] == null) map[line][tile] = new Tile();
                map[line][tile].parseString(tiles[tile]);
            }
        } 
    }

    

    /**
     * Re-Assign each tile to be a fresh tile
     */
    public void reloadMap(){
        for(int i=0; i< size; i++){
            for(int j=0; j<size; j++){
                map[i][j] = null;
            }
        }
    }


    /**
     * Set an entire tile
     * @param x
     * @param y
     * @param t
     */
    public void setTile(int x, int y, Tile t){
        if(!inBounds(x, y)) return;
        map[x][y] = t;
    }

    /**
     * Get a whole tile
     * @param x
     * @param y
     * @return
     */
    public Tile getTile(int x, int y){
        if(!inBounds(x, y)) return null;
        return map[x][y];
    }


}
