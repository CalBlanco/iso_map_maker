package com.isomapmaker.game.map.Assets;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;

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
        walls = new HashMap<WallQuadrant, Wall>();

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
        try{b.draw(this.floor.getRegion(), pos.x, pos.y);}catch(Exception e){};
        
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
        out += floor == null ? "...\nWalls:\n\tLeft: " : floor.getName()+"\nWalls:\n\tLeft: ";
        out += walls.get(WallQuadrant.top) == null ? "...\n\tRight: " : walls.get(WallQuadrant.top).getName() +"\n\tRight: "; 
        out += walls.get(WallQuadrant.right) == null ? "...\n\tTop: " : walls.get(WallQuadrant.right).getName() +"\n\tTop: "; 
        out += walls.get(WallQuadrant.left) == null ? "...\n\tBottom: " : walls.get(WallQuadrant.left).getName() +"\n\tBottom: "; 
        out += walls.get(WallQuadrant.bottom) == null ? "...\n\t" : walls.get(WallQuadrant.bottom).getName() +"\nObjects:\n\t"; 
        out += object == null ? "Object: \n" : "Object: " + object.getName() +"\n";
        return out;
    }

    public String saveString(){
        String out = "";
        out += floor != null ? floor.getName() +"-" : "e-";
        out += walls.get(WallQuadrant.top) != null ? walls.get(WallQuadrant.top).getName() +":" : "e:";
        out += walls.get(WallQuadrant.right) != null ? walls.get(WallQuadrant.right).getName() +":" : "e:";
        out += walls.get(WallQuadrant.left) != null ? walls.get(WallQuadrant.left).getName() +":" : "e:";
        out += walls.get(WallQuadrant.bottom) != null ? walls.get(WallQuadrant.bottom).getName() +"-" : "e-";
        out += object != null ? object.getName() +":" : "";
        
        return out;
    }


    public Floor getFloor(){return this.floor;}
    public Wall getWall(WallQuadrant quad){return this.walls.get(quad);}
    public Obj getObject(){return this.object;}


    
}
