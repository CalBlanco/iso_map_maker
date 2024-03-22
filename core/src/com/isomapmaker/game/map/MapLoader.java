package com.isomapmaker.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.util.IsoUtil;

public class MapLoader {
    private String[][] strMap;
    private AssetLoader assets;

    private class TextureData{
        public TextureRegion tr;
        public String name;
        public Vector2 size;

        public TextureData(TextureRegion tr, String name, String size_str){
            this.tr = tr;
            this.name = name;
            int x,y;
            x = Integer.parseInt(size_str.split("x",2)[0]);
            y = Integer.parseInt(size_str.split("x",2)[1]);
            this.size = new Vector2(x,y);
        }

               
    }


    private TextureData parseTile(String tileStr){ // convert a tile like Grass_1:8 into a texture region
        
        String [] split = tileStr.split(":",2);
        String[] info = assets.get(split[0]);

        return new TextureData(assets.loadTextureRegion(split[0], Integer.parseInt(split[1])), split[0], info[1]);
    }

    public MapLoader(String[][] map, AssetLoader assets){
        this.strMap = map; // save the map
        this.assets = assets;
    }

    public void printMap(){
        for(int i=0; i<strMap.length; i++){
            for(int j=0; j<strMap.length; j++){
                System.out.print(strMap[i][j]+ ", ");
            }
            System.out.println("");
        }
    }

    public void buildMap(SpriteBatch batch){
        
        for(int i=0; i<strMap.length; i++){
            for(int j=0; j<strMap[i].length; j++){
                TextureData td = parseTile(strMap[i][j]);
                Vector2 iso = IsoUtil.worldToIsometric(new Vector2(i,j), td.size);
                batch.draw(td.tr,iso.x,iso.y);
            }
        }
    }

    public void render(SpriteBatch batch){
        buildMap(batch);
    }



}
