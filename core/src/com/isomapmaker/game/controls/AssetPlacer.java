package com.isomapmaker.game.controls;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.TileMaps.TileMapManager;
import com.isomapmaker.game.map.Tiles.Floor;
import com.isomapmaker.game.map.Tiles.Wall;
import com.isomapmaker.game.util.IsoUtil;

public class AssetPlacer implements InputProcessor {

    final Vector2[] activeOffsets = new Vector2[]{new Vector2(-256,-64), new Vector2(0,0), new Vector2(256,-64)};

    OrthographicCamera cam;
    AssetController ass;
    TileMapManager manager;
    int selection = 0;
    int layer = 0;
    TileLoader loader;
    TileMap map;
    public AssetPlacer(OrthographicCamera cam, AssetController ass, TileMapManager manager, TileLoader loader){
        this.cam = cam; 
        this.ass= ass; 
        this.manager = manager;
        this.loader = loader;
        this.map = manager.getLayer(layer);
    }


    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        switch(keycode){
            case Input.Keys.LEFT:
                incrementSelection(-1);
                return true;
            case Input.Keys.RIGHT:
                incrementSelection(1);
                return true;
        }
        return false;
      }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
       }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 wpos = cam.unproject(new Vector3(screenX,screenY,0));
        Vector2 tilePos = IsoUtil.isometricToWorld(new Vector2(wpos.x,wpos.y), IsoUtil.FLOOR_SIZE);
        System.out.println("Placing " + ass.mode + " at " + wpos.toString() +", tile: " + tilePos.toString());
        switch(ass.mode){
            case "Floor":
                try{
                    map.setFloor((int)tilePos.x, (int)tilePos.y, loader.floors.get(ass.activeFile).get(selection));
                    return true;
                }
                catch(Exception e){return false;}
            case "Wall":
                try{
                    map.setWall((int)tilePos.x, (int)tilePos.y, IsoUtil.getTileQuadrant(tilePos, IsoUtil.FLOOR_SIZE, new Vector2(wpos.x, wpos.y)),loader.walls.get(ass.activeFile).get(selection));
                    return true;
                }
                catch(Exception e){return false;}
            case "Object":
                return false;
        }
        // TODO Auto-generated method stub
        return false;
        }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
      }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
        
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
        
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;

    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
       }
    
    public void incrementSelection(int i){
        if (selection + i < 0) {selection = loader.getNumRegions(ass.activeFile, ass.mode) -1; return;}
        if (selection + i >  loader.getNumRegions(ass.activeFile, ass.mode) -1){ selection = 0; return;}
        selection = selection + i;
    }

    public void renderSelectionTiles(SpriteBatch hudBatch){
        Vector<TextureRegion> regions = loader.getTextureRegions(ass.activeFile, ass.mode);
        int lower = (selection -1 > 0) ? selection-1 : loader.getNumRegions(ass.activeFile, ass.mode)-1;
        int upper = (selection + 1 < loader.getNumRegions(ass.activeFile, ass.mode)-1) ? selection+1 : 0;
        if(regions == null || regions.size() < 3){return;}
        TextureRegion[] active = new TextureRegion[]{regions.get(lower), regions.get(selection), regions.get(upper)};

        for(int i=0; i<3; i++){
            hudBatch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            if(active[i] == null) continue;
            if(i==1){
                hudBatch.setColor(1f, 1f, 1, 0.9f);
            }
            hudBatch.draw(active[i], Gdx.graphics.getWidth()/2 - 128/2 + activeOffsets[i].x, Gdx.graphics.getHeight()/4 - 64/2 + activeOffsets[i].y);
        }
    }
}
