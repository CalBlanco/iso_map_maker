package com.isomapmaker.game.map.TileMaps;

import java.util.Arrays;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

// The purpose of this class is to take advantage of the SimpleTile interfaces (floors, walls, and objects)
// Allow the parsing of a string into tile map
// CRUD on floors
// CRUD on walls
// CRUD on objects 


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
        this.highlight = new TextureRegion(new Texture(Gdx.files.internal("highlight.png")));
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
    public void render(SpriteBatch b){
        b.setColor(1f,1f,1f,1f);
        // iterate back to front
        for(int i=size-1; i>=0; i--){
            for(int j=size-1; j>=0; j--){
                // get world cordinates incorperating layer offset
                Vector2 wpos = IsoUtil.isometricToWorldPos(new Vector2(i,j).add(tileOffset.x, tileOffset.y), IsoUtil.FLOOR_SIZE);
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
        if (!inBounds(x, y)) return null;
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
        if (map[x][y] == null) map[x][y]= new Tile();
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
        return map[x][y].toString();
    }



    /**
     * Iterating over all tiles create appropriate strings for saving
     * @return a string containing tile information for map loading
     */
    public String saveMap(){
        String out = "";
        for(int i=0; i< size; i++){
            for(int j=0; j<size; j++){
                if(map[i][j] != null) out+= map[i][j].saveString() +",";
            }
            out += "\n";
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
        reloadMap(); // wipe everything and load from this

        /* for(int line=0; line < inMap.length && line < size; line++){ // ensure our provided map is not bigger than allowed map 
            // load a tile one by one here
            String[] tiles = inMap[line].split(",", size);
            for(int tile=0; tile<tiles.length && tile < size; tile++){ // ensure we only place tiles that would fit in size
                map[line][tile].parseString(tiles[tile], loader );
            }
        } */
    }

    

    /**
     * Re-Assign each tile to be a fresh tile
     */
    public void reloadMap(){
        for(int i=0; i< size; i++){
            for(int j=0; j<size; j++){
                map[i][j] = new Tile();
            }
        }
    }


}
