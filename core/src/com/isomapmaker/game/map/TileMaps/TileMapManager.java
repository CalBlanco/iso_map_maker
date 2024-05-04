package com.isomapmaker.game.map.TileMaps;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.util.IsoUtil;

/**
 * Manage layers of TileMaps (Singleton pattern)
 */
public class TileMapManager {
    Vector<TileMap> layers;
    private int size = 400; 
    private static TileMapManager instance;
    private OrthographicCamera cam;
    


    public static TileMapManager getInstance(){
        if(instance == null){
            synchronized (TileMapManager.class){
                if(instance == null){
                    instance = new TileMapManager(400);
                }
            }
           
        }
        
        return instance;
    }
    
    /**
     * Build a manager of a specified size
     * @param loader Loader for tile assets
     * @param size The size we want all maps made by this manager to be 
     */
    private TileMapManager(int size){
        
        layers = new Vector<TileMap>();
        TileMap base = new TileMap(size, new Vector2(0,0),null);
        TileMap first = new TileMap(size, new Vector2(1,1),null);
        TileMap second = new TileMap(size, new Vector2(2,2),null);
        TileMap fourth = new TileMap(size, new Vector2(3,3),null);
        
        layers.add(base); // add initial layer
        layers.add(first); // add initial layer
        layers.add(second); // add initial layer
        layers.add(fourth); // add initial layer

    }

    
    /**
     * Render based on render region (Not working as expected)
     * @param b
     */
    public void render(SpriteBatch b){
        //getRenderRegion(); // recalc the render region 
        for(int i = 0; i<layers.size(); i++){ // render higher layers first? probably not actually lmao
            //layers.get(i).render(b, (int)v[0].x, (int)v[1].x, (int)v[0].y, (int)v[1].y);
            layers.get(i).render(b);
        }
    }

    /**
     * Retrieve a specified layer
     * @param i the layer index to grab
     * @return The layer itself, returns null if unable to get layer
     */
    public TileMap getLayer(int i){
        try{
            return layers.get(i);
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * Add a new layer providing the tile map we want to add
     * @param l
     */
    public void addLayer(TileMap l){
        layers.add(l);
    }

    /**
     * Pop the highest layer off the manager
     */
    public void popLayer(){
        layers.remove(layers.size()-1);
    }

    /**Return the highest layer */
    public int maxLayer(){
        return layers.size()-1;
    }

    /**
     * Add a fresh layer to the next availble index
     */
    public void addNewLayer(){
        layers.add(new TileMap(size, layers.get(maxLayer()).tileOffset.add(new Vector2(1,1)), null));
    }

    

    /**
     * Load a layers map from a string
     * @param layer The layer we want to load
     * @param inMap the map we want the layer to now be 
     */
    public void setLayerMap(int layer, String[] inMap){
        try{
            TileMap t = layers.get(layer);
            t.loadMap(inMap);
        }   
        catch(Exception e){
            System.out.println("Failed to load map");
            System.out.println( e.toString());
        }
    }

    /**
     * Set the orthographic camera to ensure we only render what we need to
     * @param cam
     */
    public void setOrthoCamera(OrthographicCamera cam){
        this.cam = cam;
    }


   
     //Vars for setting the render region
    // Corner vectors
    Vector2 tlCorner = new Vector2();
    Vector2 trCorner = new Vector2();
    Vector2 blCorner = new Vector2();
    Vector2 brCorner = new Vector2();
    // temp math vectors
    Vector3 unprojection = new Vector3();
    Vector3 tVector0 = new Vector3();
    Vector2 tVector2_0 = new Vector2();

    //output arr
    Vector2[] v = new Vector2[]{new Vector2(), new Vector2()};
    
     /**
     * Calculate the active region we should be drawing
     * 
     *  Get corners,
     *  Unproject
     *  Convert to iso tiles
     *  Figure out render square
     *      
     * @return
     */
    private void getRenderRegion(){
        if (this.cam == null) return;

        //TL corner
        tVector0.set(0, 0, 0); // get the corner cords 
        unprojection = cam.unproject(tVector0); // unproject it 
        tVector2_0.set(unprojection.x, unprojection.y); // store the 2d vector results of unprojection 
        IsoUtil.worldPosToIsometric(tVector2_0, IsoUtil.FLOOR_SIZE, tlCorner); // convert to game cordinates

        //TR corner
        tVector0.set(Gdx.graphics.getWidth(), 0, 0);
        unprojection = cam.unproject(tVector0);
        tVector2_0.set(unprojection.x, unprojection.y);
        IsoUtil.worldPosToIsometric(tVector2_0, IsoUtil.FLOOR_SIZE, trCorner);

        // For some reason this is grabbing a little bit too far to the right 
        // BL corner
        tVector0.set(0, Gdx.graphics.getHeight(), 0);
        unprojection = cam.unproject(tVector0);
        tVector2_0.set(unprojection.x, unprojection.y);
        IsoUtil.worldPosToIsometric(tVector2_0, IsoUtil.FLOOR_SIZE, blCorner);
        

        //BR corner
        tVector0.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
        unprojection = cam.unproject(tVector0);
        tVector2_0.set(unprojection.x, unprojection.y);
        IsoUtil.worldPosToIsometric(tVector2_0, IsoUtil.FLOOR_SIZE, blCorner);

        //bound the vectors
        fixBounds(tlCorner);
        fixBounds(trCorner);
        fixBounds(blCorner);
        fixBounds(brCorner);

        

        v[0].set(blCorner.x, trCorner.x); // X Range 
        v[1].set(blCorner.y, tlCorner.y); // Y Range
        

       //System.out.println("Corners:\n\ttl: " + tlCorner.toString() +"\ttr: " + trCorner.toString()+"\n\tbl: " + blCorner.toString() +"\tbr: " + brCorner.toString()+"\n\tBounds\n\t\tX: " + v[0].toString()+"\n\t\tY: " + v[1].toString());

    }

    /**
     * Ensure the tiles we return are in bounds for the render
     * @param vec
     */
    private void fixBounds(Vector2 vec){
        if(vec.x < 0) vec.x = 0;
        if(vec.x > size-1) vec.x = size-1;
        if(vec.y < 0) vec.y = 0;
        if(vec.y > size-1) vec.y = size-1;
        

    }



}
