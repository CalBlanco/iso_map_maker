package com.isomapmaker.game.map.TileMaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.Tiles.Floor;
import com.isomapmaker.game.map.Tiles.Object;
import com.isomapmaker.game.map.Tiles.SimpleTile;
import com.isomapmaker.game.map.Tiles.Wall;
import com.isomapmaker.game.util.IsoUtil;
import com.isomapmaker.game.util.XmlParse;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Collections;

/**
 * Load in images to textures, to texture regions to allow for easy use for map making 
 * Handles assigning asset game information here as well
 */
public class TileLoader {

    public Dictionary<String, Vector<Floor>> floors;
    public Dictionary<String, Vector<Wall>> walls;
    public Dictionary<String, Vector<Object>> objects;
    public Dictionary<String, String[]> objectData;

    /**
     * Manages tile texture information so that game tiles can get correct graphical data
     * @param assetPath The asset file we want to use as configuration for our assets 
     */
    public TileLoader(String assetPath){
        
        floors = new Hashtable<String,Vector<Floor>>(); // {dirt: [Floor Class...].., grass: {}}
        walls = new Hashtable<String,Vector<Wall>>(); // NS-Wall: [Wall Class...]..,
        objects = new Hashtable<String,Vector<Object>>();

        objectData = new Hashtable<String, String[]>();

        loadXmlData(XmlParse.readXML(assetPath));
    }

    /**
     * Get all of our textures from the loaded xml data
     * @param tiles
     */
    public void loadXmlData(String[][] tiles){
        for(int i=0; i<tiles.length; i++){
            parseTileTexture(tiles[i]);
        }
    }

    /**
     * Main worker of this class, Loads in each texture to make regions for building out map later
     * @param rawTileData
     */
    public void parseTileTexture(String[] rawTileData){
        String name = rawTileData[0]; // points to a tile sheet, need to iterate over every box of the sheet to create the appropriate tile regions
        String file = rawTileData[1]; // load file as a texture, assign textures 
        String type = rawTileData[2];
        String flags = rawTileData[3];
        String size = rawTileData[4];

        System.out.println("Attempting to load asset:\n\tName: " + name + "\n\tFile: " +file + "\n\tType: " +type + "\n\tFlags: " + flags + "\n\tSize: " +size + "\n");

        objectData.put(name, new String[]{type,flags,size});

        switch (type) {
            case "f":
                parseFloors(name, file, flags);
                break;
            case "w":
                parseWalls(name, file, flags, size);
                break;
            case "o":
                parseObjects(name, file, flags, size);
                break;
        }


        
    }


    /**
     * Add into our floor 
     * @param name
     * @param file
     * @param flags
     */
    public void parseFloors(String name, String file, String flags){
        floors.put(name, new Vector<Floor>()); //store a new vector here
       
        
        Vector<TextureRegion> regions = getRegionsFromFile(file, "128x64"); // floor tiles are all(at least they should) this constant size
        
        for( int i=0; i< regions.size(); i++){
            
            Floor t = new Floor(regions.get(i), name+"_"+i);
            addFlags(t, flags);
            floors.get(name).add(t);
        }
    }

    /**
     * Parse walls from input string
     * @param name
     * @param file
     * @param flags
     * @param size_str
     */
    public void parseWalls(String name, String file, String flags, String size_str){
        walls.put(name, new Vector<Wall>()); //store a new vector here
        
        Vector<TextureRegion> regions = getRegionsFromFile(file, size_str); // floor tiles are all(at least they should) this constant size
        
        
        for( int i=0; i< regions.size(); i++){
            Wall t = new Wall(regions.get(i),name+"_"+i);
            addFlags(t, flags);
            walls.get(name).add(t);
        }
    }


    /**
     * Parse out our read objects into actual texture info for objects
     * @param name
     * @param file
     * @param flags
     * @param size_str
     */
    public void parseObjects(String name, String file, String flags, String size_str){
        objects.put(name, new Vector<Object>()); //store a new vector here
        
        Vector<TextureRegion> regions = getRegionsFromFile(file, size_str); // floor tiles are all(at least they should) this constant size
        for( int i=0; i< regions.size(); i++){
            Object t = new Object(regions.get(i), new Vector2(getSize(size_str)[0], getSize(size_str)[1]), name+"_"+i);
            addFlags(t, flags);
            objects.get(name).add(t);
        }
    }


    
    /**
     * Given a file we want textures from, and the size of textures within the file. Create texture regions for each item in the image
     * @param file : File path relative to the assets/ folder
     * @param size_str : size string for the objects 128x64, **by default [128,64] is returned by getSize if size string is not there 
     * @return A vector containing all the texture regions for the file
     */
    public Vector<TextureRegion> getRegionsFromFile(String file, String size_str){
        Vector<TextureRegion> regions = new Vector<TextureRegion>(); // allocate our vector of texture regions
        Texture texture = new Texture(Gdx.files.internal(file)); // get the texture into memory


        int[] size = getSize(size_str); // get our dimensions
        int numRegions = (texture.getWidth()*texture.getHeight()) / (size[0]*size[1]); // calculate number of regions we need to make
        // make the regions
        for(int i=0; i<numRegions; i++){
            int[] grabed = IsoUtil.convertTo2DPoint(i, texture.getWidth()/size[0]);
            regions.add(new TextureRegion(texture, size[0]*grabed[0], size[1]*grabed[1], size[0], size[1])); // add our new texture region
        }

        return regions;
    }

    /**
     * Parse a size str : WxH into int[]{W, H}
     * @param size_str
     * @return returns int[]{W,H} if unable to parse returns int[]{128x64} for floor size
     */
    public int[] getSize(String size_str){
        try{
            String[] spl = size_str.split("x");
            return new int[]{Integer.parseInt(spl[0]), Integer.parseInt(spl[1])};
        }
        catch(Exception e){
            return new int[]{128,64}; // default size
        }
    }

    /**
     * Add flags to our object by parsing the string of provided flags flag1=boolean,flag2=boolean,... and calling the SimpleTile setFlag
     * @param tile
     * @param flags
     */
    public void addFlags(SimpleTile tile, String flags){
        String[] split_flags = flags.split(",");
        for(int i=0; i< split_flags.length; i++){
            String[] args = split_flags[i].split("=");
            if (args.length != 2) continue;
            tile.setFlag(args[0], Boolean.valueOf(args[1]));
        }        
    }

    /**
     * Get the floor specified
     * @param textureName
     * @param choice
     * @return
     */
    public Floor getFloor(String textureName, int choice){
        try{
            return floors.get(textureName).get(choice);
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * Get the wall Specified
     * @param textureName
     * @param choice
     * @return
     */
    public Wall getWall(String textureName, int choice){
        try{
            return walls.get(textureName).get(choice);
        }
        catch(Exception e){
            return null;
        }
    }


    /**
     * Get the object specified 
     * @param textureName
     * @param choice
     * @return
     */
    public Object getObject(String textureName, int choice){
        try{
            return objects.get(textureName).get(choice);
        }
        catch(Exception e){
            return null;
        }
    }


    /**
     * Internal function to retrieve the enumeration of keys for the desired dictionary as a String[]
     * @param d Dictionary<String, ?>
     * @return An array of all possible keys for the given dictionary
     */
    private String[] getKeys(Dictionary<String, ?> d){
        String[] keys = new String[d.size()];
        int i =0;
        for(Enumeration<String> e = d.keys(); e.hasMoreElements();){
            keys[i] = e.nextElement();
            i++;
        }
        return keys;
    }

    /**
     * Get a list of available files for a particular type 
     * @param mode "Floor", "Wall", or "Object"
     * @return
     */
    public String[] getFiles(String mode){
        switch(mode){
            case "Floor":
                return getFloors();
            case "Wall":
                return getWalls();
            case "Object":
                return getObjects();
        }
        return new String[]{"No Files found"};
    }

    /**
     * Return all possible keys for floors
     * @return
     */
    public String[] getFloors(){
        return getKeys(floors);
    }

    /**
     * Return all possible keys for walls
     * @return
     */
    public String[] getWalls(){
        return getKeys(walls);
    }

    /**
     * Return all possible keys availble for objects
     * @return
     */
    public String[] getObjects(){
        return getKeys(objects);
    }


    // Get the total available regions associated with the texture name, providing a particular type
    /**
     * Get the total number of regions available for this type of file 
     * @param name
     * @param type
     * @return
     */ 
    public int getNumRegions(String name, String type){
        try{
            switch(type){
                case "Floor":
                    return floors.get(name).size();
                case "Wall":
                    return walls.get(name).size();
                case "Object":
                    return objects.get(name).size();
            }

            return 1;
        }
        catch(Exception e){
            return 1;
        }
    }

    /**
     * File meta information for a particular file
     * @param name The name of the file we want meta info from
     * @return [type, flags, size]
     */
    public String[] fileData(String name){
        System.out.println("Getting data for " + name);
        try{
            return objectData.get(name);
        }
        catch(Exception e){
            System.out.println("Couldnt get data");
            return new String[]{"","",""};
        }
    }


    /**
     * Get a Vector of all the texture regions of an associated name and mode 
     * @param name Name of file 
     * @param mode Floor gets data from the floor textures, Wall does walls, and Object does objects
     * @return A Vector containing texture data for a given file
     */
    public Vector<TextureRegion> getTextureRegions(String name, String mode){
        try{
            Vector<TextureRegion> tr = new Vector<TextureRegion>();
            switch(mode){
                case "Floor":
                    for(int i=0; i< floors.get(name).size(); i++){
                        tr.add(floors.get(name).get(i).getTexture());
                    }
                    break;
                case "Wall":
                    for(int i=0; i< walls.get(name).size(); i++){
                        tr.add(walls.get(name).get(i).getTexture());
                    }
                    break;
                case "Object":
                    for(int i=0; i< objects.get(name).size(); i++){
                        tr.add(objects.get(name).get(i).getTexture());
                    }
                    break;
            }

            return tr;
        }
        catch(Exception e){
            return null;
        }
    }
    
}
