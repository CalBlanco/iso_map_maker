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
    Wall[][][] walls; // wall positions (each tile can have 4 walls for now)
    Floor[][] floors; // floor positions
    Object[][][] objects; // object positions 

    private Vector2[] WALLOFFSET = new Vector2[]{new Vector2(-32,16), new Vector2(32,16), new Vector2(-32,-16), new Vector2(32,-16)};

    TextureRegion highlight;
    
    public Vector2 tileOffset = new Vector2(0,0);

    // eventually need a way to load in a map here
    // so lets make a class at some point to deal with reading/writing this information later
    public TileMap(int size, Vector2 tileOffset){
        this.size = size;
        this.tileOffset = tileOffset;
        this.highlight = new TextureRegion(new Texture(Gdx.files.internal("highlight.png")));
        init_maps();
    }

    public void init_maps(){
        this.walls = new Wall[size][size][4];
        this.floors = new Floor[size][size];
        this.objects = new Object[size][size][objectLimit];
        
    }

    public void render(SpriteBatch b){
        // iterate back to front
        for(int i=size-1; i>=0; i--){
            for(int j=size-1; j>=0; j--){
                // get world cordinates incorperating layer offset
                Vector2 wpos = IsoUtil.worldToIsometric(new Vector2(i,j).add(tileOffset.x, tileOffset.y), IsoUtil.FLOOR_SIZE);
                // render floor
                if (floors[i][j] != null) b.draw(floors[i][j].getTexture(), wpos.x, wpos.y);
                if (floors[i][j] == null) b.draw(this.highlight, wpos.x, wpos.y);
                // objects 
                renderObjects(b, wpos, objects[i][j]);
                //walls 
                renderWalls(b, wpos, walls[i][j]);
            }
        }
    }

    public void renderWalls(SpriteBatch b, Vector2 pos, Wall[] walls){
        for(int i=0; i<4; i++){
            if (walls[i] == null) continue;
            b.draw(walls[i].getTexture(),pos.x + WALLOFFSET[i].x,pos.y+ WALLOFFSET[i].y);
        }
    }

    public void renderObjects(SpriteBatch b, Vector2 pos, Object[] objs){
        for(int i=0; i< objectLimit; i++){
            if(objs[i] == null) continue;
            b.draw(objs[i].getTexture(), pos.x, pos.y);
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
        floors[x][y] = f;
    }

    public Floor getFloor(int x, int y){
        if (!inBounds(x, y)) return null;
        return floors[x][y];
    }
/*
██╗    ██╗ █████╗ ██╗     ██╗     ███████╗
██║    ██║██╔══██╗██║     ██║     ██╔════╝
██║ █╗ ██║███████║██║     ██║     ███████╗
██║███╗██║██╔══██║██║     ██║     ╚════██║
╚███╔███╔╝██║  ██║███████╗███████╗███████║
 ╚══╝╚══╝ ╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝
*/
    public void setWall(int x, int y, int slot, Wall w){
        if (slot < 0 ||  slot > 4) return;
        if(!inBounds(x, y)) return;
        walls[x][y][slot] = w;
    }

    public Wall getWall(int x, int y, int slot){
        if (slot < 0 ||  slot > 4) return null;
        if (!inBounds(x, y)) return null;
        return walls[x][y][slot];
    }
/*
 ██████╗ ██████╗      ██╗
██╔═══██╗██╔══██╗     ██║
██║   ██║██████╔╝     ██║
██║   ██║██╔══██╗██   ██║
╚██████╔╝██████╔╝╚█████╔╝
 ╚═════╝ ╚═════╝  ╚════╝     
*/

    public void setObject(int x, int y, int space, Object o){
        if(space < 0 || space > objectLimit) return;
        if(!inBounds(x, y)) return;
        objects[x][y][space] = o;
    }

    public Object getObject(int x, int y, int space){
        if (space < 0 || space > objectLimit) return null;
        if(!inBounds(x, y)) return null;
        return objects[x][y][space];
    }

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

}
