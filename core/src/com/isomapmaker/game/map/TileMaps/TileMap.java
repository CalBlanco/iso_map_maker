package com.isomapmaker.game.map.TileMaps;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.Tiles.Floor;
import com.isomapmaker.game.map.Tiles.Object;
import com.isomapmaker.game.map.Tiles.Tile;
import com.isomapmaker.game.map.Tiles.Wall;
import com.isomapmaker.game.util.IsoUtil;

// The purpose of this class is to take advantage of the SimpleTile interfaces (floors, walls, and objects)
// Allow the parsing of a string into tile map
// CRUD on floors
// CRUD on walls
// CRUD on objects 


public class TileMap {

    private int size;
    private int objectLimit = 3;
    
    private Tile[][] map;

    private Vector2[] WALLOFFSET = new Vector2[]{new Vector2(-32,16), new Vector2(32,16), new Vector2(-32,-16), new Vector2(32,-16)};

    TextureRegion highlight;
    TextureRegion defaultTexture;

    public Vector2 tileOffset = new Vector2(0,0);
    

    // eventually need a way to load in a map here
    // so lets make a class at some point to deal with reading/writing this information later
    public TileMap(int size, Vector2 tileOffset, Floor defaultFloor){
        this.size = size;
        this.tileOffset = tileOffset;
        this.defaultTexture = defaultFloor.getTexture();
        this.highlight = new TextureRegion(new Texture(Gdx.files.internal("highlight.png")));
        this.map = new Tile[size][size];
        for(int i=0; i< size; i++){
            for(int j=0; j<size; j++){
                map[i][j] = new Tile(this.defaultTexture);
                
            }
        }
    }

    public void init_maps(){
        
    }

    public void render(SpriteBatch b){
        // iterate back to front
        for(int i=size-1; i>=0; i--){
            for(int j=size-1; j>=0; j--){
                // get world cordinates incorperating layer offset
                Vector2 wpos = IsoUtil.worldToIsometric(new Vector2(i,j).add(tileOffset.x, tileOffset.y), IsoUtil.FLOOR_SIZE);
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
    public void setFloor(int x, int y, Floor f){
        if (!inBounds(x, y)) return;
        if (map[x][y] == null) map[x][y]= new Tile(this.defaultTexture);
        map[x][y].setFloor(f);

    }

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
    public void setWall(int x, int y, String slot, Wall w){
        if(!inBounds(x, y)) return;
        if (map[x][y] == null) map[x][y]= new Tile(this.defaultTexture);
        map[x][y].setWall(w, slot);
    }

    public Wall getWall(int x, int y, String slot){
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


/*
██╗   ██╗████████╗██╗██╗     
██║   ██║╚══██╔══╝██║██║     
██║   ██║   ██║   ██║██║     
██║   ██║   ██║   ██║██║     
╚██████╔╝   ██║   ██║███████╗
 ╚═════╝    ╚═╝   ╚═╝╚══════╝
 */
    public boolean inBounds(int x, int y){
        if(x < 0 || x > size-1 || y < 0 || y > size-1) return false;
        return true;
    }

    public boolean hasTile(int x, int y){
        if(!inBounds(x, y)) return false;
        if(map[x][y] == null) return false;
        return true;
    }


    public String getTileString(int x, int y){
        if(!inBounds(x, y)) return "";
        return map[x][y].toString();
    }
}
