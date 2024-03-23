package com.isomapmaker.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.AssetLoader;
import com.isomapmaker.game.map.MapLoader;
import com.isomapmaker.game.map.TextureData;
import com.isomapmaker.game.ui.OnScreenText;

public class MenuController implements InputProcessor {
    final private static Vector2 MenuPos = new Vector2(20, Gdx.graphics.getHeight()-20);
    final private static Vector2 InfoPos = new Vector2(20,100);
    final static int MAX_SIZE = 400;
    private MapLoader map;
    private AssetLoader assets;
    private CamController ccont;
    
    private OnScreenText tileSelection;
    private OnScreenText tileInformation;
    private TextureData loadedData;


    private boolean controlModifier;
  
    public MenuController(MapLoader map, AssetLoader assets, CamController ccont){
        this.map = map;
        this.controlModifier = false;
        this.assets = assets;
        this.ccont = ccont;
        this.tileSelection = new OnScreenText("Press UP or DOWN to change tile category\nPress LEFT or RIGHT to change tile selection", MenuPos, "fonts/badd_mono.fnt");
        this.loadedData = null;
        this.tileInformation = new OnScreenText("tile_data", InfoPos, "default.fnt");
        
    }

    public void render(SpriteBatch b){
        tileSelection.render(b);
        tileInformation.render(b);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                assets.incrementSelection(-1);
                break;
            case Input.Keys.RIGHT:
                assets.incrementSelection(1);
                break;
            case Input.Keys.UP:
                assets.incrementTexture(-1);
                break;
            case Input.Keys.DOWN:
                assets.incrementTexture(1);
                break;
            case Input.Keys.S:
                if(controlModifier) map.saveTileMap();
                break;
            case Input.Keys.C:
                map.removeTile(ccont.hoverTile.x, ccont.hoverTile.y);
            default:
                
        }

        if (keycode == Input.Keys.CONTROL_LEFT){
            controlModifier = true;
        }

        tileSelection.setText(assets.getActiveTextureName() + "\n" + (assets.getActiveSelection()+1)+"/"+assets.getAvailableSelection());
        return true;
        
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        if (keycode == Input.Keys.CONTROL_LEFT){
            controlModifier = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }


    // Tile wise are we in bounds
    public boolean isOutOfBounds(int x, int y){
        return (x < 0 | x > MAX_SIZE-1 | y < 0 | y > MAX_SIZE-1);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        
        if(button == Input.Buttons.LEFT){
            if(isOutOfBounds((int)ccont.hoverTile.x, (int)ccont.hoverTile.y)) return false;
            TextureData td = assets.getActiveTextureData((int)ccont.hoverTile.x, (int)ccont.hoverTile.y);
            
            map.addTile(td);
        }
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
        loadedData = map.getTextureData(ccont.hoverTile.x, ccont.hoverTile.y);
        
        tileInformation.setText((loadedData == null ? "" : "Tile-Name: " + loadedData.name +"\nSize: " +loadedData.size )+"\nTile-Pos: ("+ccont.hoverTile.x+", "+ccont.hoverTile.y+")\n"+"World-Pos: (" + ccont.hoverWorldPos.x +", "+ ccont.hoverWorldPos.y +")" );
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
    }

    
    
}
