package com.isomapmaker.game.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.TileMaps.TileMapManager;
public class MapSaver {
    private static MapSaver instance;
    private static float completionP;

    public static MapSaver getInstance()
    {
        if(MapSaver.instance == null){
            MapSaver.instance = new MapSaver();
        }

        return MapSaver.instance;
    }

    private MapSaver(){
        completionP = 0f;
        try{
            File f = new File("maps");
            boolean bool = f.mkdir();

            System.out.println("made? : " + bool);
        }     
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateCompletion(int at, int max){
        completionP = at/max;
    }


    public void saveNewMap(String mapName, TileMapManager manager){
        ExecutorService exec = Executors.newFixedThreadPool(4);
        exec.submit(() -> saveMap(mapName, 0,  manager.getLayer(0)));
        exec.submit(() -> saveMap(mapName, 1,  manager.getLayer(1)));
        exec.submit(() -> saveMap(mapName, 2,  manager.getLayer(2)));
        exec.submit(() -> saveMap(mapName, 3,  manager.getLayer(3)));

        exec.shutdown();
    }

    private void saveMap(String mapName, int layer,TileMap map){
        try{
            boolean made = new File("maps/"+mapName).mkdir();
            
                
                BufferedWriter writer = new BufferedWriter(new FileWriter("maps/"+mapName+"/"+layer+".txt"));
                writer.write(map.saveMap());
                writer.close();
            
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    

    public void readMaps(String mapName, TileMapManager manager){
        ExecutorService exec = Executors.newFixedThreadPool(1);
        exec.submit(() -> readSavedMap(mapName, manager));
        exec.shutdown();
    }

    public void readSavedMap(String mapName, TileMapManager manager){
        File dir = new File("maps/"+mapName);
        try{
        
            File[] matches = dir.listFiles(new FilenameFilter(){
                @Override
                public boolean accept(File di, String name){
                    return name.matches("\\d.*\\.txt");
                }
            });

            BufferedReader read = null;
            for(int i=0; i< matches.length; i++){
                if( i > manager.maxLayer()) break;
                updateCompletion(i, matches.length);
                read = new BufferedReader(new FileReader(matches[i]));
                manager.getLayer(i).loadMap(read.lines().toArray(String[]::new));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public float getCompletion(){
        return completionP;
    }
}
