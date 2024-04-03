package com.isomapmaker.game.controls;

import java.util.HashMap;
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

    Vector2 tilePos;
    Vector2 screenPos;
    String quadrant = "top";
    String mode = "Floor";
    String file = "Dry";


    Vector2 lowHighlightBound = new Vector2(0,0);
    Vector2 highHighlightBound = new Vector2(0,0);

    Vector2 clickPos = new Vector2(0,0);
    HashMap<String, TextureRegion> quadrantToHighlight = new HashMap<String,TextureRegion>();

    Vector2 tVector = new Vector2(0,0);
    public AssetPlacer(OrthographicCamera cam, AssetController ass, TileMapManager manager, TileLoader loader){
        this.cam = cam; 
        this.ass= ass; 
        this.manager = manager;
        this.loader = loader;
        this.map = manager.getLayer(layer);
        this.tilePos = new Vector2(0,0);
        this.screenPos = new Vector2(0,0);
        quadrantToHighlight.put("top", loader.floors.get("QuadrantHighlights").get(0).getTexture());
        quadrantToHighlight.put("right", loader.floors.get("QuadrantHighlights").get(1).getTexture());
        quadrantToHighlight.put("left", loader.floors.get("QuadrantHighlights").get(2).getTexture());
        quadrantToHighlight.put("bottom", loader.floors.get("QuadrantHighlights").get(3).getTexture());
    }


    public void activeTileRender(SpriteBatch b){
        tVector = IsoUtil.worldToIsometric(tilePos, IsoUtil.FLOOR_SIZE);
        b.setColor(1f, 1f, 1f, 0.7f);
        try{
        switch (mode) {
            case "Floor":
                b.draw(loader.floors.get(file).get(selection).getTexture(), tVector.x, tVector.y);
                break;
            case "Wall":
                b.draw(quadrantToHighlight.get(quadrant), tVector.x, tVector.y);
                b.draw(loader.walls.get(quadrant).get(selection).getTexture(), tVector.x, tVector.y);
                break;
            default:
                break;
        }}
        catch(Exception e){return;}
        b.setColor(1f,1f,1f,1f);

        
       
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        switch(keycode){
            case Input.Keys.Q:
                incrementSelection(-1);
                return true;
            case Input.Keys.E:
                incrementSelection(1);
                return true;
            case Input.Keys.C:
                switch (ass.mode) {
                    case "Floor":
                        map.setFloor((int)tilePos.x, (int)tilePos.y, null);
                        return true;
                    case "Wall":
                        map.setWall((int)tilePos.x, (int)tilePos.y, quadrant, null);
                        return true;
                    case "Object":
                        return true;
                    default:
                        break;
                }
            case Input.Keys.PAGE_UP: // go to next layer or make new layer above this one 
                if(layer+1 > manager.maxLayer()) manager.addNewLayer(); // make a new layer if there is not one
                map = manager.getLayer(layer+1); // get next layer
                layer +=1;
            case Input.Keys.PAGE_DOWN:
                if(layer-1 < 0) layer = 0;
                map = manager.getLayer(layer);
            case Input.Keys.DEL:
                if(manager.maxLayer() != 0) manager.popLayer();
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
        clickPos = tilePos;
        
        return false;
        }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // if click pos != tile pos we have moved the cursor while selecting
        // handle area selection
        Vector3 wpos = cam.unproject(new Vector3(screenX,screenY,0));
        Vector2 endclick = IsoUtil.isometricToWorld(new Vector2(wpos.x, wpos.y), IsoUtil.FLOOR_SIZE);
        System.out.println("Mouse Raised");
        if(clickPos.dst(endclick) <= 2) return placeTile();
        return false;
        // TODO Auto-generated method stub
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
        updatePlacementView();
        
        Vector3 wpos = cam.unproject(new Vector3(screenX,screenY,0));
        screenPos.set(wpos.x,wpos.y);
        tilePos = IsoUtil.isometricToWorld(new Vector2(wpos.x-IsoUtil.FLOOR_SIZE.x/4,wpos.y-IsoUtil.FLOOR_SIZE.y/8), IsoUtil.FLOOR_SIZE);
        quadrant = IsoUtil.getTileQuadrant(tilePos, new Vector2(screenPos.x, screenPos.y));
        
        ass.updateTileInfo(screenPos, tilePos, map.getTileString((int)tilePos.x, (int)tilePos.y), quadrant, layer);
        
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
       }
    
    
    /**
     * Increment the selected tile by a certain amount
     * @param i
     */
    public void incrementSelection(int i){
        if (selection + i < 0) {selection = loader.getNumRegions(file, mode) -1; return;}
        if (selection + i >  loader.getNumRegions(file, mode) -1){ selection = 0; return;}
        selection = selection + i;
    }

    /**
     * Render the available selection info to the ui
     * @param hudBatch
     */
    public void renderSelectionTiles(SpriteBatch hudBatch){
        Vector<TextureRegion> regions = mode != "Wall" ? loader.getTextureRegions(file, mode) : loader.getTextureRegions(quadrant, mode) ;
        int lower = (selection - 1 >= 0) ? selection-1 : loader.getNumRegions(file, mode) -1 ;
        int upper = (selection + 1 <= loader.getNumRegions(file, mode) -1 ) ? selection + 1 : 0;
        if(regions == null || regions.size() < 2){return;}
        TextureRegion[] active = new TextureRegion[]{regions.get(lower), regions.get(selection), regions.get(upper)};

        ass.updateTileBrowser(active);
    }

    /**
     * Place a tile down in the hover tile position
     * @return boolean representing successful input
     */
    private boolean placeTile(){
        System.out.println("Placing " + ass.mode + " at " + screenPos.toString() +", tile: " + tilePos.toString());
        switch(mode){
            case "Floor":
                try{
                    map.setFloor((int)tilePos.x, (int)tilePos.y, loader.floors.get(file).get(selection));
                    return true;
                }
                catch(Exception e){return false;}
            case "Wall":
                try{
                    map.setWall((int)tilePos.x, (int)tilePos.y, IsoUtil.getTileQuadrant(tilePos, new Vector2(screenPos.x, screenPos.y)),loader.walls.get(quadrant).get(selection));
                    return true;
                }
                catch(Exception e){return false;}
            case "Object":
                return false;
        }
        return false;
    }

    /**
     * Update the placement information for the asset highlighting mechanic
     */
    private void updatePlacementView(){
        if(mode != ass.mode) {mode = ass.mode ; selection=0;};
        if(file != ass.activeFile && mode != "Wall") {file = ass.activeFile; selection=0;}
        if(mode == "Wall"){file = quadrant;}
    }
    

}
