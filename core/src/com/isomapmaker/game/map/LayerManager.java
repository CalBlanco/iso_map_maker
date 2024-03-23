package com.isomapmaker.game.map;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LayerManager {
    Vector<TileLayer> layers;

    public LayerManager(){
        this.layers = new Vector<TileLayer>();
    }


    public void render(SpriteBatch b){
        for(TileLayer t: layers){
            t.render(b);
        }
    }

    public TileLayer getLayer(int i){
        try{
            return layers.get(i);
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    // add a new layer
    public void addLayer(TileLayer l){
        layers.add(l);
    }

    // remove the topmost layer
    public void popLayer(){
        layers.remove(layers.size()-1);
    }

    public int maxLayer(){
        return layers.size();
    }

}
