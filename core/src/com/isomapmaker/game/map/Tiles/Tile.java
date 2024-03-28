package com.isomapmaker.game.map.Tiles;


import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tile {
    Floor floor;
    HashMap<String, Wall> walls;
    Vector<Object> objects;

    public Tile(){
        floor = null;

        walls = new HashMap<String, Wall>();
        walls.put("top", null);
        walls.put("bottom", null);
        walls.put("left", null);
        walls.put("right", null);

        objects = new Vector<Object>();
    }

    public void setFloor(Floor f){
        this.floor = f;
    }

    public Floor getFloor(){ return this.floor;}

    public void setWall(Wall w, String slot){
        try{
            walls.replace(slot, w);
        }
        catch(Exception e){
            System.out.println("Unable to place wall in slot " + slot);
            return;
        }
    }

    public Wall getWall(String slot){
        try{
            return walls.get(slot);
        }
        catch(Exception e){
            return null;
        }
    }

    public void addObject(Object o){
        return;
    }

    public Object getObject(int index){
        return null;
    }

    //Render this tile by rendering the following in order
    // a) render floor
    // b) render walls
    //      i) top
    //      ii) right
    //      iii) left
    //      iv)  bottom
    // c) Objects
    public void render(SpriteBatch b, Vector2 pos){
        try{if(this.floor != null) b.draw(this.floor.getTexture(), pos.x, pos.y);}catch(Exception e){return;}
        try{if(this.walls.get("top") != null) b.draw(this.walls.get("top").getTexture(), pos.x, pos.y);}catch(Exception e){return;}
        try{if(this.walls.get("right") != null) b.draw(this.walls.get("right").getTexture(), pos.x, pos.y);}catch(Exception e){return;}
        try{if(this.walls.get("left") != null) b.draw(this.walls.get("top").getTexture(), pos.x, pos.y);}catch(Exception e){return;}
        try{if(this.walls.get("bottom") != null) b.draw(this.walls.get("top").getTexture(), pos.x, pos.y);}catch(Exception e){return;}

        try{
            for(int i=0; i<this.objects.size(); i++){
                if(objects.get(i) != null) b.draw(objects.get(i).getTexture(), pos.x, pos.y);
            }
        }   
        catch(Exception e){return ;}
    }
}
