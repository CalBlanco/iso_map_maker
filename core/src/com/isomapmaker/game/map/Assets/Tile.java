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
    

    public Tile(Tile copy){
        this.walls = copy.walls;
        this.floor = copy.floor;
        this.object = copy.object;
    }


    public Tile(){
        walls = new HashMap<WallQuadrant, Wall>(4);

        WallQuadrant[] quads = WallQuadrant.values();
        for(int i=0; i<quads.length; i++){
            walls.put(quads[i],null);
        }
        floor = null;
        object = null;
    }

    public void setFloor(Asset floor){
        this.floor = (Floor) floor;
    }

    public void setWall(Asset wall, WallQuadrant quad){
        walls.replace(quad, (Wall) wall);
    }

    public void setObject(Asset object){
        this.object = (Obj) object;
    }

    public void render(SpriteBatch b, Vector2 pos){
        try{b.draw(this.floor.getRegion(), pos.x, pos.y); }catch(Exception e){};
        
        //upper walls
        try{b.draw(this.walls.get(WallQuadrant.right).getRegion(), pos.x, pos.y);}catch(Exception e){};
        try{b.draw(this.walls.get(WallQuadrant.top).getRegion(), pos.x, pos.y);}catch(Exception e){};

        //Object
        try{b.draw(this.object.getRegion(), pos.x,pos.y);}catch(Exception e){};

        //lower walls 
        try{b.draw(this.walls.get(WallQuadrant.left).getRegion(), pos.x, pos.y);}catch(Exception e){};
        try{b.draw(this.walls.get(WallQuadrant.bottom).getRegion(), pos.x, pos.y);}catch(Exception e){};

       
    }


    public String toString(){
        String out ="Floor: ";
        out += floor == null ? "...\nWalls:\n\tLeft: " : floor.getName() +"("+floor.getFullId()+")" +"\nWalls:\n\tLeft: ";
        out += walls.get(WallQuadrant.left) == null ? "...\n\tRight: " : walls.get(WallQuadrant.left).getName() +"("+walls.get(WallQuadrant.left).getFullId()+")" +"\n\tRight: "; 
        out += walls.get(WallQuadrant.right) == null ? "...\n\tTop: " : walls.get(WallQuadrant.right).getName() +"("+walls.get(WallQuadrant.right).getFullId()+")" +"\n\tTop: "; 
        out += walls.get(WallQuadrant.top) == null ? "...\n\tBottom: " : walls.get(WallQuadrant.top).getName() +"("+walls.get(WallQuadrant.top).getFullId()+")" +"\n\tBottom: "; 
        out += walls.get(WallQuadrant.bottom) == null ? "...\n\t" : walls.get(WallQuadrant.bottom).getName() +"("+walls.get(WallQuadrant.bottom).getFullId()+")"  +"\nObjects:\n\t"; 
        out += object == null ? "Object: \n" : "Object: " + object.getName() +"("+floor.getFullId()+")" +"\n";
        return out;
    }

    public String toString2(){
        boolean hasFloor = floor != null;
        boolean hasTopWall = walls.get(WallQuadrant.top) != null;
        boolean hasLeftWall = walls.get(WallQuadrant.left) != null;
        boolean hasRightWall = walls.get(WallQuadrant.right) != null;
        boolean hasBottomWall = walls.get(WallQuadrant.bottom) != null;
        boolean hasObject = object != null;

        String o = "";
        if(hasFloor) o+= floor.getName() + floor.getFullId() +"\n";
        if(hasTopWall) o+= walls.get(WallQuadrant.top).getName() + walls.get(WallQuadrant.top).getFullId()+"\n";
        if(hasLeftWall) o+= walls.get(WallQuadrant.left).getName() + walls.get(WallQuadrant.left).getFullId()+"\n";
        if(hasRightWall) o+= walls.get(WallQuadrant.right).getName() + walls.get(WallQuadrant.right).getFullId()+"\n";
        if(hasBottomWall) o+= walls.get(WallQuadrant.bottom).getName() + walls.get(WallQuadrant.bottom).getFullId()+"\n";
        if(hasObject) o+= object.getName() + object.getFullId()+"\n";
        return o;
    }

    public boolean hasAnAsset(){
        return (floor != null || walls.get(WallQuadrant.top) != null || walls.get(WallQuadrant.right) != null || walls.get(WallQuadrant.left) != null || walls.get(WallQuadrant.bottom) != null || object != null);
    }
    
    public String saveString(){
        String out = "";
        out += floor != null ? floor.getFullId() +"-" : "e-";
        out += walls.get(WallQuadrant.top) != null ? walls.get(WallQuadrant.top).getFullId() +":" : "e:";
        out += walls.get(WallQuadrant.right) != null ? walls.get(WallQuadrant.right).getFullId() +":" : "e:";
        out += walls.get(WallQuadrant.left) != null ? walls.get(WallQuadrant.left).getFullId() +":" : "e:";
        out += walls.get(WallQuadrant.bottom) != null ? walls.get(WallQuadrant.bottom).getFullId() +"-" : "e-";
        out += object != null ? object.getFullId() : "e";
        if(out.trim().equals("e-e:e:e:e-e")) out = ""; // fully empty tile no point in saving everything
        return out;
    }


    public Floor getFloor(){return this.floor;}
    public Wall getWall(WallQuadrant quad){return this.walls.get(quad);}
    public Obj getObject(){return this.object;}

    static final String[] quadNames = {"top", "right", "left", "bottom"};
   
    /**
     * Parse the provided string into a tile 
     * @param tile
     */
    public void parseString(String tile){
        
        String[] items = tile.split("-"); // split the tile string 
        if(items.length <= 1) return;
        int[] ids = new int[2]; // var for saving ids 

        if(!items[0].equals("e") && !items[0].equals("e,")) { // if our floor tile is saved 
            ids = parseId(items[0]);            
            floor = (Floor)TileAtlas.getInstance().get(TileType.Floor, ids[0], ids[1]);
        }

        String[] wallString = items[1].split(":");
        for(int i=0; i<wallString.length; i++){
            if(wallString[i].equals("e")) continue;
            ids = parseId(wallString[i]);
            walls.replace(WallQuadrant.valueOf(quadNames[i]), (Wall)TileAtlas.getInstance().get(TileType.Wall, ids[0], ids[1]));
        }

        if(!items[2].equals("e") && !items[2].equals("e,")){
            ids = parseId(items[2]);
            object = (Obj) TileAtlas.getInstance().get(TileType.Object, ids[0], ids[1]);
        }
    }

    private int[] parseId(String idString){
        String[] spl = idString.split("_");
        return new int[]{Integer.parseInt(spl[0]), Integer.parseInt(spl[1])};
    }


    public void clearTile(){
        this.floor = null;
        this.walls.replace(WallQuadrant.top, null);
        this.walls.replace(WallQuadrant.left, null);
        this.walls.replace(WallQuadrant.right, null);
        this.walls.replace(WallQuadrant.bottom, null);
        this.object = null;
    }
    
}
