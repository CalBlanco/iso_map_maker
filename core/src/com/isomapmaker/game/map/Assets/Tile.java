package com.isomapmaker.game.map.Assets;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;

/**
 * Contains logic for rendering the required assets (Floor, Walls, Objects )
 */
public class Tile {
    HashMap<WallQuadrant, Wall> walls;
    Floor floor;
    Obj object;

    Wall[] _walls = new Wall[4];
    
    /**
     * Copy Constructor
     * @param copy
     */
    public Tile(Tile copy){

        if(copy == null) return;
        
        
        this._walls[WallQuadrant.top.ordinal()] = copy._walls[WallQuadrant.top.ordinal()];
        this._walls[WallQuadrant.bottom.ordinal()] = copy._walls[WallQuadrant.bottom.ordinal()];
        this._walls[WallQuadrant.left.ordinal()] = copy._walls[WallQuadrant.left.ordinal()];
        this._walls[WallQuadrant.right.ordinal()] = copy._walls[WallQuadrant.right.ordinal()];
        
        this.floor = copy.floor;
        this.object = copy.object;
    }


    /**
     * Null Constructor
     */
    public Tile(){
        WallQuadrant[] quads = WallQuadrant.values();
        for(int i=0; i<quads.length; i++){
            this._walls[quads[i].ordinal()] = null;
        }
        floor = null;
        object = null;
    }


    public void setFloor(Asset floor){
        this.floor = (Floor) floor;
    }

    public void setWall(Asset wall, WallQuadrant quad){
        this._walls[quad.ordinal()] = (Wall) wall;
    }

    public void setObject(Asset object){
        this.object = (Obj) object;
    }

    public void render(SpriteBatch b, Vector2 pos){
        try{b.draw(this.floor.getRegion(), pos.x, pos.y); }catch(Exception e){};
        
        //upper walls
        try{b.draw(this._walls[WallQuadrant.right.ordinal()].getRegion(), pos.x, pos.y);}catch(Exception e){};
        try{b.draw(this._walls[WallQuadrant.top.ordinal()].getRegion(), pos.x, pos.y);}catch(Exception e){};

        //Object
        try{b.draw(this.object.getRegion(), pos.x,pos.y);}catch(Exception e){};

        //lower walls 
        try{b.draw(this._walls[WallQuadrant.left.ordinal()].getRegion(), pos.x, pos.y);}catch(Exception e){};
        try{b.draw(this._walls[WallQuadrant.bottom.ordinal()].getRegion(), pos.x, pos.y);}catch(Exception e){};

       
    }


    public String toString(){
        String out ="Floor: ";
        out += floor == null ? "..., Walls:\tLeft: " : floor.getName() +"("+floor.getFullId()+")" +", Walls:\tLeft: ";
        out += _walls[WallQuadrant.left.ordinal()] == null ? "...\tRight: " : _walls[WallQuadrant.left.ordinal()].getName() +"("+_walls[WallQuadrant.left.ordinal()].getFullId()+")" +"\tRight: "; 
        out += _walls[WallQuadrant.right.ordinal()] == null ? "...\tTop: " : _walls[WallQuadrant.right.ordinal()].getName() +"("+_walls[WallQuadrant.right.ordinal()].getFullId()+")" +"\tTop: "; 
        out += _walls[WallQuadrant.top.ordinal()] == null ? "...\tBottom: " : _walls[WallQuadrant.top.ordinal()].getName() +"("+_walls[WallQuadrant.top.ordinal()].getFullId()+")" +"\tBottom: "; 
        out += _walls[WallQuadrant.bottom.ordinal()] == null ? "...\t" : _walls[WallQuadrant.bottom.ordinal()].getName() +"("+_walls[WallQuadrant.bottom.ordinal()].getFullId()+")"  +"Objects:\n\t"; 
        out += object == null ? "Object:" : "Object: " + object.getName() +"("+object.getFullId()+")";
        return out;
    }

    public boolean hasAnAsset(){
        return (floor != null || _walls[WallQuadrant.top.ordinal()] != null || _walls[WallQuadrant.right.ordinal()] != null || _walls[WallQuadrant.left.ordinal()] != null || _walls[WallQuadrant.bottom.ordinal()] != null || object != null);
    }
    
    public String saveString(){
        String out = "";
        out += floor != null ? floor.getFullId() +"-" : "e-";
        out += _walls[WallQuadrant.top.ordinal()] != null ? _walls[WallQuadrant.top.ordinal()].getFullId() +":" : "e:";
        out += _walls[WallQuadrant.right.ordinal()] != null ? _walls[WallQuadrant.right.ordinal()].getFullId() +":" : "e:";
        out += _walls[WallQuadrant.left.ordinal()] != null ? _walls[WallQuadrant.left.ordinal()].getFullId() +":" : "e:";
        out += _walls[WallQuadrant.bottom.ordinal()] != null ? _walls[WallQuadrant.bottom.ordinal()].getFullId() +"-" : "e-";
        out += object != null ? object.getFullId() : "e";
        if(out.trim().equals("e-e:e:e:e-e")) out = ""; // fully empty tile no point in saving everything
        return out;
    }


    public Floor getFloor(){return this.floor;}
    public Wall getWall(WallQuadrant quad){return this._walls[quad.ordinal()];}
    public Obj getObject(){return this.object;}

    static final String[] quadNames = {"top", "right", "left", "bottom"};
   
    /**
     * Parse the provided string into a tile 
     * @param tile
     */
    int[] ids = new int[2];
    public void parseString(String tile){
        
        String[] items = tile.split("-"); // split the tile string 
        if(items.length <= 1) return;
        

        if(!items[0].equals("e") && !items[0].equals("e,")) { // if our floor tile is saved 
            parseId(items[0], ids);            
            floor = (Floor)TileAtlas.getInstance().get(TileType.Floor, ids[0], ids[1]);
        }

        String[] wallString = items[1].split(":");
        for(int i=0; i<wallString.length; i++){
            if(wallString[i].equals("e")) continue;
            parseId(wallString[i], ids);
            _walls[WallQuadrant.valueOf(quadNames[i]).ordinal()] =  (Wall)TileAtlas.getInstance().get(TileType.Wall, ids[0], ids[1]);
        }

        if(!items[2].equals("e") && !items[2].equals("e,")){
            parseId(items[2], ids);
            object = (Obj) TileAtlas.getInstance().get(TileType.Object, ids[0], ids[1]);
        }
    }



    private int[] parseId(String idString, int[] setId){
        String[] spl = idString.split("_");
        setId[0] = Integer.parseInt(spl[0]);
        setId[1] = Integer.parseInt(spl[1]);
        return setId;
    }

    /**
     * Remove all the assets on the tile by assigned them all to null
     */
    public void clearTile(){
        this.floor = null;
        this._walls[WallQuadrant.top.ordinal()] =  null;
        this._walls[WallQuadrant.left.ordinal()] =  null;
        this._walls[WallQuadrant.right.ordinal()] =  null;
        this._walls[WallQuadrant.bottom.ordinal()] =  null;
        this.object = null;
    }

    public boolean equals(Tile tile){
        if(tile == null) return false;

        if(tile.floor == this.floor && tile._walls[0] == this._walls[0] && tile._walls[1] == this._walls[1] && tile._walls[2] == this._walls[2] && tile._walls[3] == this._walls[3] && tile.object == this.object ) return true;

        return false;
    }
    
}
