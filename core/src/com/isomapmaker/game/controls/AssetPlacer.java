package com.isomapmaker.game.controls;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.util.IsoUtil;

public class AssetPlacer implements InputProcessor {

    OrthographicCamera cam;
    AssetController ass;
    TileMap map;
    public AssetPlacer(OrthographicCamera cam, AssetController ass, TileMap tl){this.cam = cam; this.ass=ass; this.map = tl;}


    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
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
                if (ass.f == null) return false;
                map.setFloor((int)tilePos.x, (int)tilePos.y, ass.f);
                return true;
            case "Wall":
                if (ass.w == null) return false;
                map.setWall((int)tilePos.x, (int)tilePos.y, IsoUtil.getTileQuadrant(tilePos, IsoUtil.FLOOR_SIZE, new Vector2(wpos.x, wpos.y)),ass.w);
                return true;
            case "Object":
                if (ass.o == null) return false;
                map.setObject((int)tilePos.x, (int)tilePos.y, 0,ass.o);
                return true;
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
    
}
