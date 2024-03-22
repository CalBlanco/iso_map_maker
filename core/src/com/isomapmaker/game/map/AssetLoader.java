package com.isomapmaker.game.map;
import java.io.*;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
    Dictionary<String,String[]> fileAssetMap; //contain mapping information for assets
    Dictionary<String,Texture> loadedTextures;

    public AssetLoader(){
        fileAssetMap = new Hashtable<>();
        loadedTextures = new Hashtable<>();

        loadedTextures.put("NULL", new Texture(Gdx.files.internal("my_iso_assets/floor_highlight_128x64.png")));
        readAssetConfig();
    }

    private void readAssetConfig(){
        try (BufferedReader reader = new BufferedReader(new FileReader("asset_config.txt"))) {
            String line; // iterate over lines
            while((line = reader.readLine()) != null){ // assign and check
                parseLine(line);
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLine(String line){
        String[] split = line.split(":",3);
        fileAssetMap.put(split[0],new String[]{ split[1], split[2]});
    }

    public String[] get(String textureName){
        return fileAssetMap.get(textureName);
    }

    public Texture loadTexture(String textureName){
        /**
         * Load the texture either by retrieving it from loadedTextures, or loading it and adding it to loadedTextures
         */
        String[] info = fileAssetMap.get(textureName);
        if (loadedTextures.get(textureName) != null) return loadedTextures.get(textureName);
        Texture t = new Texture(Gdx.files.internal(info[0]));
        if (t != null) loadedTextures.put(textureName, t);
        
        return t;
    }

    /**
     * Get Dimensions based on a string size WidthxHeight
     * @param size_str
     * @return
     */
    private int[] getDims(String size_str){
        String[] spl  = size_str.split("x");
        return new int[]{ Integer.parseInt(spl[0]), Integer.parseInt(spl[1])};
    }

    /**
     * Convert the index into a 2d coord we can use for the tile map 
     * @param index
     * @param width
     * @return
     */
    private int[] convertTo2DPoint(int index, int width) {
        int[] point = new int[2];
        point[0] = index % width; // x-coordinate
        point[1] = index / width; // y-coordinate
        return point;
    }

    /**
     * Load a texture region based on a texture name and the item chocie. returns null if unable to find the texture in the loaded texture 
     * @param textureName
     * @param areaChoice
     * @return
     */
    public TextureRegion loadTextureRegion(String textureName, int areaChoice){
        int area, tX, tY, tArea;
        int dim[];
        Texture loaded = loadedTextures.get(textureName);

        if(loaded == null) {
            loaded = loadTexture(textureName);
            if (loaded == null) return new TextureRegion(loadedTextures.get("NULL"),128,64);
        } // reject if nothing available

        dim = getDims(fileAssetMap.get(textureName)[1]); // get dimensions
        area = dim[0]*dim[1];
        
        tX = loaded.getWidth(); //get texture data
        tY = loaded.getHeight();
        tArea = tX*tY;

        int availableTextures = tArea / area;
        if (areaChoice < 0 | areaChoice > availableTextures) return null; // reject if area choice greater than allowed

        int[] grabRegion = convertTo2DPoint(areaChoice, tX/dim[0]);
        if (grabRegion.length != 2) return null; // unable to grab region
        //System.out.println("Attempting to grab:\n\tchoice: "+ areaChoice +"\n\tavailable: " + availableTextures +"\n\tgrabRegion:\n\t\tx: " + grabRegion[0] + ",\n\t\ty: "+ grabRegion[1]+"\n\n\tselected:\n\t\tx: " + dim[0]*grabRegion[0] +"\n\t\ty: " + dim[1]*grabRegion[1]);

        return new TextureRegion(loadedTextures.get(textureName),dim[0]*grabRegion[0], dim[1]*grabRegion[1], dim[0], dim[1]);

    }

    // Call dispose on all loaded textures
    public void dispose(){
        for (Enumeration e = this.loadedTextures.keys(); e.hasMoreElements();){
            this.loadedTextures.get(e).dispose();
        }
    }
}
