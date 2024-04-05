package com.isomapmaker.game.map.Tiles;


import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.TileMaps.TileLoader;

public class Tile {
    Floor floor;
    HashMap<String, Wall> walls;
    Vector<Object> objects;
    TextureRegion defaultRegion;
    
    public Tile(Tile copy){
        this.floor = copy.floor;
        this.walls = copy.walls;
        this.objects = copy.objects;
        this.defaultRegion = copy.defaultRegion;
    }

    /**
     * Constructor requires a default texture to draw for this tile if no floor is present (this should be the grid highlight texture)
     * @param defaultRegion
     */
    public Tile(TextureRegion defaultRegion){
        
        this.defaultRegion = defaultRegion;
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

    /**
     * Get floor from ths tile
     * @return The Floor this tile is using 
     */
    public Floor getFloor(){ return this.floor;}


    /**
     * Set the wall for a specific slot/quadrant
     * @param w The wall to place
     * @param slot The slot we want to place the wall in
     */
    public void setWall(Wall w, String slot){
        try{
            walls.replace(slot, w);
        }
        catch(Exception e){
            System.out.println("Unable to place wall in slot " + slot);
            return;
        }
    }


    /**
     * Get wall information on a specified slot from this tile
     * @param slot : should be "top", "right", "left", or "bottom"
     * @return The wall in that slot 
     */
    public Wall getWall(String slot){
        try{
            return walls.get(slot);
        }
        catch(Exception e){
            return null;
        }
    }


    /**
     * Add a new object to the tile (UNIMPLEMENTED)
     * @param o
     */
    public void addObject(Object o){
        return;
    }

    /**
     * Get the object at the specified object index (UNIMPLEMENTED RN)
     * @param index
     * @return
     */
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
        //Floor
        try{if(this.floor != null){ b.draw(this.floor.getTexture(), pos.x, pos.y);}else{
            b.setColor(1, 1, 1, 0.05f);
            b.draw(this.defaultRegion, pos.x, pos.y);
            b.setColor(1,1,1,1);
        }}catch(Exception e){return;} 
        //Walls
        try{if(this.walls.get("top") != null) b.draw(this.walls.get("top").getTexture(), pos.x, pos.y);}catch(Exception e){return;}
        try{if(this.walls.get("right") != null) b.draw(this.walls.get("right").getTexture(), pos.x, pos.y);}catch(Exception e){return;}
        try{if(this.walls.get("left") != null) b.draw(this.walls.get("left").getTexture(), pos.x, pos.y);}catch(Exception e){return;}
        try{if(this.walls.get("bottom") != null) b.draw(this.walls.get("bottom").getTexture(), pos.x, pos.y);}catch(Exception e){return;}
        //Objects
        try{
            for(int i=0; i<this.objects.size(); i++){
                if(objects.get(i) != null) b.draw(objects.get(i).getTexture(), pos.x, pos.y);
            }
        }   
        catch(Exception e){return ;}
    }


    /**
     * Pretty string representation for visual display of tile info
     */
    public String toString(){
        String out ="Floor: ";
        out += floor == null ? "...\nWalls:\n\tLeft: " : floor.getName()+"\nWalls:\n\tLeft: ";
        out += walls.get("top") == null ? "...\n\tRight: " : walls.get("top").getName() +"\n\tRight: "; 
        out += walls.get("right") == null ? "...\n\tTop: " : walls.get("right").getName() +"\n\tTop: "; 
        out += walls.get("left") == null ? "...\n\tBottom: " : walls.get("left").getName() +"\n\tBottom: "; 
        out += walls.get("bottom") == null ? "...\n\t" : walls.get("bottom").getName() +"\nObjects:\n\t"; 
        for(int i=0;i<objects.size();i++){
            out += i +": "+ objects.get(i).getName() +"\n\t";
            if(i==objects.size()-1) out+="\n";
        }
        return out;
    }


    /**
     * Generate a string to make saving the tile easy
     * @return Returns a string representing the tile information floor-top:right:left:bottom-objects
     * Places an "e" if texture info is null
     */
    public String saveString(){
        String out = "";
        out += floor != null ? floor.getName() +"-" : "e-";
        out += walls.get("top") != null ? walls.get("top").getName() +":" : "e:";
        out += walls.get("right") != null ? walls.get("right").getName() +":" : "e:";
        out += walls.get("left") != null ? walls.get("left").getName() +":" : "e:";
        out += walls.get("bottom") != null ? walls.get("bottom").getName() +"-" : "e-";

        for(int i=0; i<objects.size(); i++){
            out += objects.get(i) != null ? objects.get(i).getName() +":" : "";
        }
        

        return out;
    }


    /**
     * Parse an input string representing a tile into an actual tile
     * @param tileString should look like "floor-top:right:left:bottom-objects..."
     * @param loader The loader grabs assets based on the tile description and assigns them to this tile
     */
    public void parseString(String tileString, TileLoader loader){
        String[] mainSplit = tileString.split("-",3);
        if(mainSplit.length != 3) return;
        String floorString = mainSplit[0];
        String wallString = mainSplit[1];
        String objectString = mainSplit[2];



        // load floor or default if null
        String[] floorParts = new String[2];
        if(floorString != "e") floorParts = floorString.split("_",2);
        else floorParts = new String[]{"Highlight", "4"};
        

        if(floorParts.length != 2) return;
        floor = loader.getFloor(floorParts[0], Integer.parseInt(floorParts[1]));


        // now load walls 
        String[] wallSplits = wallString.split(":");
        if(wallSplits.length != 4) return;
        String topWall = wallSplits[0];
        String rightWall = wallSplits[1];
        String leftWall = wallSplits[2];
        String bottomWall = wallSplits[3];

        
        if(!topWall.equals("e")) walls.put("top", loader.getWall("top", Integer.parseInt(topWall.split("_")[1])));
        if(!rightWall.equals("e")) walls.put("right", loader.getWall("right", Integer.parseInt(rightWall.split("_")[1])));
        if(!leftWall.equals("e")) walls.put("left", loader.getWall("left", Integer.parseInt(leftWall.split("_")[1])));
        if(!bottomWall.equals("e")) walls.put("bottom", loader.getWall("bottom", Integer.parseInt(bottomWall.split("_")[1])));


        String[] objectsplit = objectString.split(":");
        for(int i=0; i<objectsplit.length; i++){
            String[] obj = objectsplit[i].split("_");
            if(obj.length != 2) continue;
            objects.add(i, loader.getObject(obj[0], Integer.parseInt(obj[1])));
        }

    }
}
