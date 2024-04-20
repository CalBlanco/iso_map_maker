package com.isomapmaker.game.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.isomapmaker.game.controls.ModeController;
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

    //broke
    private void updateCompletion(int at, int max){
        completionP = at/max;
    }

    /**
     * Save a map (creates a thread per layer to save faster lmao) [Broken]
     * @param mapName
     */
    public void saveNewMap(String mapName){
        try {
            Files.createDirectories(Paths.get("maps", mapName));
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
        System.out.println("Called Save Wrapper");
        long startTime = System.currentTimeMillis();
        ModeController.getInstance().setSavingLoading(true);
        TileMapManager manager = TileMapManager.getInstance();
        ExecutorService exec = Executors.newFixedThreadPool(4);
        exec.submit(() -> saveMap(mapName, 0,  manager.getLayer(0)));
        exec.submit(() -> saveMap(mapName, 1,  manager.getLayer(1)));
        exec.submit(() -> saveMap(mapName, 2,  manager.getLayer(2)));
        exec.submit(() -> saveMap(mapName, 3,  manager.getLayer(3)));

        exec.shutdown();

        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Wait for all tasks to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ModeController.getInstance().setSavingLoading(false);
        long endTime = System.currentTimeMillis();

        System.out.println("Took " + ((endTime - startTime)) + " to save the map");
    }

    private void saveMap(String mapName, int layer,TileMap map){
        try{
            
            long startTime = System.currentTimeMillis();
            
            FileWriter fw = new FileWriter("maps/"+mapName+"/"+layer+".txt");
            BufferedWriter writer = new BufferedWriter(fw);
            try
            {
                writer.write(map.saveMap());
                long endTime = System.currentTimeMillis();
                System.out.println("\tTook " + ((endTime - startTime)) + " to save layer " + layer);
            }
            catch(Exception e){e.printStackTrace();}
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    

    /**
     * Public method to call thread load [Broken]
     * @param mapName
     */
    public void readMaps(String mapName){
        ModeController.getInstance().setSavingLoading(true);
        System.out.println("Called Load Wrapper");

        long startTime = System.currentTimeMillis();
        ExecutorService exec = Executors.newFixedThreadPool(1);
        exec.submit(() -> readSavedMap(mapName));
        exec.shutdown();

        
        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Wait for all tasks to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ModeController.getInstance().setSavingLoading(false);

        long endTime = System.currentTimeMillis();
        System.out.println("Took " + ((endTime - startTime)) + " to load the map");
    }


    private void readSavedMap(String mapName){
        File dir = new File("maps/"+mapName);
        TileMapManager manager = TileMapManager.getInstance();
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
