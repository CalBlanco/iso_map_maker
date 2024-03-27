package com.isomapmaker.game.map.TileMaps;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class TileMapManager {
    Vector<TileMap> layers;
    private int size = 400; 

    public TileMapManager(){
        layers = new Vector<TileMap>();
        TileMap base = new TileMap(size, new Vector2(0,0));
        addLayer(base); // add initial layer
    }

    public TileMapManager(int size){
        layers = new Vector<TileMap>();
        TileMap base = new TileMap(size, new Vector2(0,0));
        addLayer(base); // add initial layer
    }

   
    // render
    public void render(SpriteBatch b){
        for(int i= layers.size()-1; i>=0; i--){ // render higher layers first? probably not actually lmao
            layers.get(i).render(b);
        }
    }

    public TileMap getLayer(int i){
        try{
            return layers.get(i);
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    // add a new layer
    public void addLayer(TileMap l){
        layers.add(l);
    }

    // remove the topmost layer
    public void popLayer(){
        layers.remove(layers.size()-1);
    }

    public int maxLayer(){
        return layers.size()-1;
    }
}
