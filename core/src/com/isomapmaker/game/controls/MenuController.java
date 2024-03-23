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
    final private static Vector2 MenuPos = new Vector2(Gdx.graphics.getWidth()-100, Gdx.graphics.getHeight()-20);
    private MapLoader map;
    private AssetLoader assets;
    private CamController ccont;
    private OnScreenText fileBrowser;
    private OnScreenText tileSelection;

  
    public MenuController(MapLoader map, AssetLoader assets, CamController ccont){
        this.map = map;
        this.assets = assets;
        this.ccont = ccont;
        this.fileBrowser = new OnScreenText("file_1", MenuPos, "default.fnt");
        this.tileSelection = new OnScreenText("1", MenuPos.add(-40, 0), "default.fnt");

        
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
            default:
                
        }

        fileBrowser.setText(assets.getActiveTextureName());
        tileSelection.setText((assets.getActiveSelection()+1)+"/"+assets.getAvailableSelection());

        return true;
        
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
        // TODO Auto-generated method stub
        
        if(button == Input.Buttons.LEFT){
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
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
    }

    public void render(SpriteBatch b){
        fileBrowser.render(b);
        tileSelection.render(b);
    }
    
}
