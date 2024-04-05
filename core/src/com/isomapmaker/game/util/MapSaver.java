package com.isomapmaker.game.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.isomapmaker.game.map.TileMaps.TileMapManager;
public class MapSaver {
    private static MapSaver instance;

    public static MapSaver getInstance()
    {
        if(MapSaver.instance == null){
            MapSaver.instance = new MapSaver();
        }

        return MapSaver.instance;
    }

    private MapSaver(){
        
        try{
            File f = new File("maps");
            boolean bool = f.mkdir();

            System.out.println("made? : " + bool);
        }     
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void saveNewMap(String mapName, TileMapManager manager){
        try{
            boolean made = new File("maps/"+mapName).mkdir();
            for(int i=0; i<manager.maxLayer(); i++){
                BufferedWriter writer = new BufferedWriter(new FileWriter("maps/"+mapName+"/"+i+".txt"));
                writer.write(manager.getLayer(i).saveMap());
                writer.close();
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
