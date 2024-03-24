package com.isomapmaker.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.AssetLoader;
import com.isomapmaker.game.map.LayerManager;
import com.isomapmaker.game.map.TextureData;
import com.isomapmaker.game.map.TileLayer;
import com.isomapmaker.game.ui.OnScreenText;

public class MenuController implements InputProcessor {
    final private static Vector2 MenuPos = new Vector2(20, Gdx.graphics.getHeight()-20);
    final private static Vector2 InfoPos = new Vector2(20,100);
    final private static Vector2 layerInfoPos = new Vector2(Gdx.graphics.getWidth()-20,100);
    final static int MAX_SIZE = 400;
    private TileLayer map;
    private LayerManager lm;
    private AssetLoader assets;
    private CamController ccont;
    
    private OnScreenText tileSelection;
    private OnScreenText tileInformation;
    private OnScreenText layerInformation;
    private TextureData loadedData;

    private int layerSelection=0;

    private boolean controlModifier;

    private String helpText = "Press UP or DOWN to change tile category\nPress LEFT or RIGHT to change tile selection\nPress C to remove the highlighted tile\nPress H to display this message again";
  
    public MenuController(LayerManager lm, AssetLoader assets, CamController ccont){
        this.lm = lm;
        this.map = lm.getLayer(0);
        this.controlModifier = false;
        this.assets = assets;
        this.ccont = ccont;
        this.tileSelection = new OnScreenText(helpText, MenuPos, "fonts/badd_mono.fnt");
        this.loadedData = null;
        this.tileInformation = new OnScreenText("Tile Info", InfoPos, "default.fnt");
        this.layerInformation = new OnScreenText("Layer Info", InfoPos, "default.fnt");
    }

    public void render(SpriteBatch b){
        tileSelection.render(b);
        tileInformation.render(b);
        layerInformation.render(b);
    }

    private void incrementIndex(int i){
        int next = layerSelection + i ;
        if(next < 0) next = lm.maxLayer()-1;
        if(next > lm.maxLayer()-1) next = 0;
        this.layerSelection = next;
    }

    private void incrementLayer(int i){
        incrementIndex(i);
        map = lm.getLayer(layerSelection);
        System.out.println(layerSelection);
        layerInformation.setText("Layer: ("+map.layerName+", " + layerSelection +")");
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                assets.incrementSelection(-1);
                tileSelection.setText(assets.getActiveTextureName() + "\n" + (assets.getActiveSelection()+1)+"/"+assets.getAvailableSelection());
                break;
            case Input.Keys.RIGHT:
                assets.incrementSelection(1);
                tileSelection.setText(assets.getActiveTextureName() + "\n" + (assets.getActiveSelection()+1)+"/"+assets.getAvailableSelection());
                break;
            case Input.Keys.UP:
                assets.incrementTexture(-1);
                tileSelection.setText(assets.getActiveTextureName() + "\n" + (assets.getActiveSelection()+1)+"/"+assets.getAvailableSelection());
                break;
            case Input.Keys.DOWN:
                assets.incrementTexture(1);
                tileSelection.setText(assets.getActiveTextureName() + "\n" + (assets.getActiveSelection()+1)+"/"+assets.getAvailableSelection());
                break;
            case Input.Keys.NUM_2:
                incrementLayer(1);
                break;
            case Input.Keys.NUM_1:
                incrementLayer(-1);
                break;
            case Input.Keys.S:
                if(controlModifier) map.saveMap();
                break;
            case Input.Keys.C:
                if (controlModifier) map.removeWall((int)ccont.hoverTile.x-map.getOffset()[0], (int)ccont.hoverTile.y-map.getOffset()[1]);
                else map.removeTile(ccont.hoverTile.x-map.getOffset()[0], ccont.hoverTile.y-map.getOffset()[1]);
                break;
            case Input.Keys.H:
                tileSelection.setText(helpText);
            case Input.Keys.P:
                int[] off = {lm.getLayer(lm.maxLayer()-1).getOffset()[0]+1,lm.getLayer(lm.maxLayer()-1).getOffset()[1]+1};
                lm.addLayer(new TileLayer(assets, new String[][][]{{{}}}, off));
                break;
                default:
                
        }

        if (keycode == Input.Keys.CONTROL_LEFT){
            controlModifier = true;
        }

        
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
            
            if (controlModifier) map.addWall(td);
            else map.addTile(td);
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
        if(map == null) return false;
        loadedData = map.getTile(ccont.hoverTile.x, ccont.hoverTile.y);
        if(loadedData == null) return false;
        tileInformation.setText("\nTile-Pos: ("+ccont.hoverTile.x+", "+ccont.hoverTile.y+")\n"+"World-Pos: (" + ccont.hoverWorldPos.x +", "+ ccont.hoverWorldPos.y +")\n"+ "Layer: (" + layerSelection +", " + map.layerName+")\n" +(loadedData == null ? "" : "Tile-Name: " + loadedData.name));
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
    }

    
    
}
