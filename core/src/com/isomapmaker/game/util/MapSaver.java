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
        ExecutorService exec = Executors.newFixedThreadPool(1);
        exec.submit(() -> saveMap(mapName, manager));

        exec.shutdown();
    }

    private void saveMap(String mapName, TileMapManager manager){
        try{
            boolean made = new File("maps/"+mapName).mkdir();
            for(int i=0; i<manager.maxLayer()+1; i++){
                updateCompletion(i, manager.maxLayer()+1);
                BufferedWriter writer = new BufferedWriter(new FileWriter("maps/"+mapName+"/"+i+".txt"));
                writer.write(manager.getLayer(i).saveMap());
                writer.close();
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    

    public void readMaps(String mapName, TileMapManager manager, TileLoader loader){
        ExecutorService exec = Executors.newFixedThreadPool(1);
        exec.submit(() -> readSavedMap(mapName, manager, loader));
        exec.shutdown();
    }

    public void readSavedMap(String mapName, TileMapManager manager, TileLoader loader){
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
                manager.getLayer(i).loadMap(read.lines().toArray(String[]::new), loader);
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
