package com.isomapmaker.game.map.TileMaps;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Manage layers of TileMaps (Singleton pattern)
 */
public class TileMapManager {
    Vector<TileMap> layers;
    private int size = 400; 
    private static TileMapManager instance;
    


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

   
    // render
    public void render(SpriteBatch b){
        for(int i = 0; i<layers.size(); i++){ // render higher layers first? probably not actually lmao
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


}
