package com.isomapmaker.game.controls;

import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.isomapmaker.game.controls.commands.BoxCommand;
import com.isomapmaker.game.controls.commands.BucketCommand;
import com.isomapmaker.game.controls.commands.CircleCommand;
import com.isomapmaker.game.controls.commands.Command;
import com.isomapmaker.game.controls.commands.Commander;
import com.isomapmaker.game.controls.commands.LineCommand;
import com.isomapmaker.game.controls.commands.PencilCommand;
import com.isomapmaker.game.controls.commands.PencilEraserCommand;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.TileMaps.TileMapManager;
import com.isomapmaker.game.map.Tiles.Floor;
import com.isomapmaker.game.map.Tiles.Wall;
import com.isomapmaker.game.util.MapSaver;
import com.isomapmaker.game.util.CursorSetter;
import com.isomapmaker.game.util.IsoUtil;

public class AssetPlacer implements InputProcessor {
    private enum State {Line, Box, Circle, Pencil, Bucket};
    final Vector2[] activeOffsets = new Vector2[]{new Vector2(-256,-64), new Vector2(0,0), new Vector2(256,-64)};
    private static final int[] bucket_row = { -1, 0, 1, 0 };
    private static final int[] bucket_col = { 0, 1, 0, -1};
 
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
    PlacementModes mode = PlacementModes.Floor;
    String file = "Dry";


    Vector2 lowHighlightBound = new Vector2(0,0);
    Vector2 highHighlightBound = new Vector2(0,0);

    Vector2 clickPos = new Vector2(0,0);
    HashMap<String, TextureRegion> quadrantToHighlight = new HashMap<String,TextureRegion>();

    Vector2 tVector = new Vector2(0,0);

    PaintModes paintState;

    Vector<Integer[]> tileSelection; // the currently selected tiles based on the tool 
    Pixmap pencil,pm;

    public AssetPlacer(OrthographicCamera cam, AssetController ass, TileMapManager manager, TileLoader loader){
        this.paintState = ModeController.getInstance().getState(); 
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

        pm = new Pixmap(Gdx.files.internal("cursor.png"));
        pencil = new Pixmap(Gdx.files.internal("pencil.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 15, 15));
		
    }
   

    // Only going to be useful in pencil mode for now needs to be expanded to brushes and selections
    

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
                PencilEraserCommand peraser = new PencilEraserCommand(mode, tilePos, quadrant, loader, map);
                Commander.getInstance().run(peraser);
                return true;
            case Input.Keys.PAGE_UP: // go to next layer or make new layer above this one 
                System.out.println("Layers: " + manager.maxLayer());
                if(layer+1 > manager.maxLayer()) return false; // make a new layer if there is not one
                layer +=1;
                map = manager.getLayer(layer); // get next layer
                return true;
            case Input.Keys.PAGE_DOWN:
                if(layer-1 < 0) return false;
                layer -= 1;
                map = manager.getLayer(layer);
                return true;
            case Input.Keys.DEL:
                if(manager.maxLayer() != 0) manager.popLayer();
                return true;
            case Input.Keys.L:
                setState(PaintModes.Line);
                return true;                
            case Input.Keys.P:
                setState(PaintModes.Pencil);
                return true;
            case Input.Keys.O:
                setState(PaintModes.Circle);
                return true;
            case Input.Keys.K:
                setState(PaintModes.Bucket);
                return true;
            case Input.Keys.B:
                setState(PaintModes.Box);
                return true;
            case Input.Keys.Z:
                Commander.getInstance().undo();
                return true;
            case Input.Keys.V:
                Commander.getInstance().redo();
                return true;
            case Input.Keys.NUM_9:
                MapSaver.getInstance().saveNewMap("MyMap", manager);
                break;
            case Input.Keys.NUM_0:
                MapSaver.getInstance().readMaps("MyMap", manager, loader);
            
        }
        return false;
      }

   

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        clickPos = tilePos;
        
        return true;
        }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // if click pos != tile pos we have moved the cursor while selecting
        // handle area selection
        Vector3 wpos = cam.unproject(new Vector3(screenX,screenY,0));
        Vector2 endclick = IsoUtil.worldPosToIsometric(new Vector2(wpos.x, wpos.y), IsoUtil.FLOOR_SIZE);
        System.out.println("Mouse Raised");


        switch(this.paintState){
            case Box:
                BoxCommand box = new BoxCommand(clickPos, endclick, loader.getFloor(file, selection), loader, map);
                Commander.getInstance().run(box);
                break;
            case Circle:
                CircleCommand circ = new CircleCommand((int)clickPos.x, (int)clickPos.y, (int)clickPos.dst(endclick), loader.getFloor(file, selection), loader, map);
                Commander.getInstance().run(circ);
                break;
            case Line:
                LineCommand li = new LineCommand(clickPos, endclick, loader.getFloor(file, selection), loader, map);
                Commander.getInstance().run(li);
                break;
            case Pencil:
                PencilCommand pen = new PencilCommand(mode, file, quadrant, selection, endclick, screenPos, loader, map);
                Commander.getInstance().run(pen);
                break;
            case Bucket:
                BucketCommand buk = new BucketCommand((int)endclick.x, (int)endclick.y, loader.floors.get(file).get(selection), loader, map);
                Commander.getInstance().run(buk);
                break;
            default:
                return false;
        }
        clickPos = null;
        return true;
    
        // TODO Auto-generated method stub
      }

    

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        updatePlacementView();
        
        Vector3 wpos = cam.unproject(new Vector3(screenX,screenY,0));
        screenPos.set(wpos.x-IsoUtil.FLOOR_SIZE.x/4f, wpos.y-IsoUtil.FLOOR_SIZE.y/4f);
        tilePos = IsoUtil.worldPosToIsometric(new Vector2(wpos.x-IsoUtil.FLOOR_SIZE.x/2f,wpos.y-IsoUtil.FLOOR_SIZE.y/8), IsoUtil.FLOOR_SIZE);
        quadrant = IsoUtil.getTileQuadrant(tilePos, new Vector2(screenPos.x, screenPos.y));
        
        ass.updateTileInfo(screenPos, tilePos, map.getTileString((int)tilePos.x, (int)tilePos.y), quadrant, layer);
        
        
        return true;
    }

    /*
██╗   ██╗   ██╗
██║   ██║   ██║
██║   ██║   ██║
██║   ██║   ██║
╚██████╔╝██╗██║
 ╚═════╝ ╚═╝╚═╝               
     */
    
    
    /**
     * Increment the selected tile by a certain amount
     * @param i
     */
    public void incrementSelection(int i){
        if (selection + i < 0) {selection = loader.getNumRegions(file, mode.toString()) -1; return;}
        if (selection + i >  loader.getNumRegions(file, mode.toString()) -1){ selection = 0; return;}
        selection = selection + i;
    }

    /**
     * Render the available selection info to the ui
     * @param hudBatch
     */
    public void renderSelectionTiles(SpriteBatch hudBatch){
        Vector<TextureRegion> regions = mode != PlacementModes.Wall ? loader.getTextureRegions(file, mode.toString()) : loader.getTextureRegions(quadrant, mode.toString()) ;
        int lower = (selection - 1 >= 0) ? selection-1 : loader.getNumRegions(file, mode.toString()) -1 ;
        int upper = (selection + 1 <= loader.getNumRegions(file, mode.toString()) -1 ) ? selection + 1 : 0;
        if(regions == null || regions.size() < 3){return ;}
        TextureRegion[] active = new TextureRegion[]{regions.get(lower), regions.get(selection), regions.get(upper)};

        ass.updateTileBrowser(active);
    }

    

    /**
     * Update the placement information for the asset highlighting mechanic
     */
    private void updatePlacementView(){
        if(mode != ass.mode) {mode = ass.mode ; selection=0;};
        if(file != ass.activeFile && mode != PlacementModes.Wall) {file = ass.activeFile; selection=0;}
        if(mode == PlacementModes.Wall){file = quadrant;}
    }
    
    /*
███████╗████████╗ █████╗ ████████╗███████╗    ██████╗ ███████╗███╗   ██╗██████╗ ███████╗██████╗ 
██╔════╝╚══██╔══╝██╔══██╗╚══██╔══╝██╔════╝    ██╔══██╗██╔════╝████╗  ██║██╔══██╗██╔════╝██╔══██╗
███████╗   ██║   ███████║   ██║   █████╗      ██████╔╝█████╗  ██╔██╗ ██║██║  ██║█████╗  ██████╔╝
╚════██║   ██║   ██╔══██║   ██║   ██╔══╝      ██╔══██╗██╔══╝  ██║╚██╗██║██║  ██║██╔══╝  ██╔══██╗
███████║   ██║   ██║  ██║   ██║   ███████╗    ██║  ██║███████╗██║ ╚████║██████╔╝███████╗██║  ██║
╚══════╝   ╚═╝   ╚═╝  ╚═╝   ╚═╝   ╚══════╝    ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═════╝ ╚══════╝╚═╝  ╚═╝
     */
    public void activeTileRender(SpriteBatch b){
        switch(this.paintState){
            case Pencil:
                pencilTileRender(b);
                break;
            case Box:
                boxRender(b);
                break;
            case Circle:
                circleRender(b);
                break;
            case Line:
                lineRender(b);
                break;
            case Bucket:
                pencilTileRender(b);
                break;
            default:
                break;

        }
       
    }

    /*
     * Render the pencil tile tool availability
     */
    private void pencilTileRender(SpriteBatch b){
        tVector = IsoUtil.isometricToWorldPos(new Vector2(1,1).scl(layer).add(tilePos), IsoUtil.FLOOR_SIZE);
        b.setColor(1f, 1f, 1f, 0.7f);
        try{
        switch (mode) {
            case Floor:
                b.draw(loader.floors.get(file).get(selection).getTexture(), tVector.x, tVector.y);
                break;
            case Wall:
                b.draw(quadrantToHighlight.get(quadrant), tVector.x, tVector.y);
                b.draw(loader.walls.get(quadrant).get(selection).getTexture(), tVector.x, tVector.y);
                break;
            default:
                break;
        }
        
    }
        catch(Exception e){b.setColor(1f,1f,1f,1f); return;}
        b.setColor(1f,1f,1f,1f);
    }

    private void lineRender(SpriteBatch b){
        if (mode != PlacementModes.Floor) return; // remove this after implementing some wall code
        Vector3 hpos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 ht = IsoUtil.worldPosToIsometric(new Vector2(hpos.x,hpos.y), IsoUtil.FLOOR_SIZE);
        Vector2 v = clickPos != null ? new Vector2(1,1).scl(layer).add(clickPos) : new Vector2(1,1).scl(layer).add(tilePos);
        b.setColor(1f,1f,1f,0.7f);
        
        Vector<Integer[]> linePoints = PaintTools.line(v, new Vector2(1,1).scl(layer).add(ht));
        for(int i=0; i<linePoints.size(); i++){
            tVector = IsoUtil.isometricToWorldPos(new Vector2(1,1).scl(layer).add(new Vector2(linePoints.get(i)[0],linePoints.get(i)[1])), IsoUtil.FLOOR_SIZE);
            b.draw(loader.floors.get(file).get(selection).getTexture(), tVector.x, tVector.y);
        }
        b.setColor(1,1,1,1);

    }
    
    private void circleRender(SpriteBatch b){
        if (mode != PlacementModes.Floor) return; // remove this after implementing some wall code
        Vector3 hpos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 ht = IsoUtil.worldPosToIsometric(new Vector2(hpos.x,hpos.y), IsoUtil.FLOOR_SIZE);
        Vector2 v = clickPos != null ? new Vector2(1,1).scl(layer).add(clickPos) : new Vector2(1,1).scl(layer).add(tilePos);
        b.setColor(1f,1f,1f,0.7f);
        
        Vector<Integer[]> linePoints = PaintTools.circle(v,(int) new Vector2(1,1).scl(layer).add(ht).dst(v));
        try{
        for(int i=0; i<linePoints.size(); i++){
            tVector = IsoUtil.isometricToWorldPos(new Vector2(1,1).scl(layer).add(new Vector2(linePoints.get(i)[0],linePoints.get(i)[1])), IsoUtil.FLOOR_SIZE);
            b.draw(loader.floors.get(file).get(selection).getTexture(), tVector.x, tVector.y);
        }
        }
        catch(Exception e){e.printStackTrace();}
        b.setColor(1,1,1,1);
    }

    private void boxRender(SpriteBatch b){
        if (mode != PlacementModes.Floor) return; // remove this after implementing some wall code
        Vector3 hpos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 ht = IsoUtil.worldPosToIsometric(new Vector2(hpos.x,hpos.y), IsoUtil.FLOOR_SIZE);
        Vector2 v = clickPos != null ? new Vector2(1,1).scl(layer).add(clickPos) : new Vector2(1,1).scl(layer).add(tilePos);
        Vector<Vector2> bSel = PaintTools.box(v, ht);
        b.setColor(1f,1f,1f,0.7f);
        tVector = IsoUtil.isometricToWorldPos(v, IsoUtil.FLOOR_SIZE);
        b.draw(loader.getFloor(file,selection).getTexture(), tVector.x, tVector.y);
        for(int i=0;i<bSel.size(); i++){
            tVector = IsoUtil.isometricToWorldPos(bSel.get(i).add(new Vector2(1,1).scl(layer)), IsoUtil.FLOOR_SIZE);
            b.draw(loader.getFloor(file, selection).getTexture(), tVector.x, tVector.y);
        }
        b.setColor(1,1,1,1);
    }

/*
██╗   ██╗████████╗██╗██╗     
██║   ██║╚══██╔══╝██║██║     
██║   ██║   ██║   ██║██║     
██║   ██║   ██║   ██║██║     
╚██████╔╝   ██║   ██║███████╗
 ╚═════╝    ╚═╝   ╚═╝╚══════╝
 */

 public void setState(PaintModes newState){
    ModeController.getInstance().setState(newState);
    this.paintState = ModeController.getInstance().getState();
 }



/*
██╗███╗   ██╗████████╗███████╗
██║████╗  ██║╚══██╔══╝██╔════╝
██║██╔██╗ ██║   ██║   █████╗  
██║██║╚██╗██║   ██║   ██╔══╝  
██║██║ ╚████║   ██║   ██║     
╚═╝╚═╝  ╚═══╝   ╚═╝   ╚═╝     
        Unimplemented interface methods                       
 */
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
    public boolean scrolled(float amountX, float amountY) {
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
    
}
