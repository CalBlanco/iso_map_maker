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

    State paintState;

    Vector<Integer[]> tileSelection; // the currently selected tiles based on the tool 
    public AssetPlacer(OrthographicCamera cam, AssetController ass, TileMapManager manager, TileLoader loader){
        
        this.paintState = State.Pencil; 
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
                return pencilEraser();
            case Input.Keys.PAGE_UP: // go to next layer or make new layer above this one 
                if(layer+1 > manager.maxLayer()) manager.addNewLayer(); // make a new layer if there is not one
                map = manager.getLayer(layer+1); // get next layer
                layer +=1;
                return true;
            case Input.Keys.PAGE_DOWN:
                if(layer-1 < 0) layer = 0;
                map = manager.getLayer(layer);
                return true;
            case Input.Keys.DEL:
                if(manager.maxLayer() != 0) manager.popLayer();
                return true;
            case Input.Keys.L:
                setState(State.Line);
                return true;                
            case Input.Keys.P:
                setState(State.Pencil);
                return true;
            case Input.Keys.O:
                setState(State.Circle);
                return true;
            case Input.Keys.K:
                setState(State.Bucket);
                return true;
            case Input.Keys.B:
                setState(State.Box);
                return true;
        }
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


        switch(this.paintState){
            case Box:
                return box(endclick);
            case Circle:
                return circle(endclick);
            case Line:
                return line(endclick);
            case Pencil:
                return pencil();
            case Bucket:
                return bucket();
            default:
                break;
        }

        
        
        
        if(clickPos.dst(endclick) <= 2) return pencil();
        return false;
        // TODO Auto-generated method stub
      }

    

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        updatePlacementView();
        
        Vector3 wpos = cam.unproject(new Vector3(screenX,screenY,0));
        screenPos.set(wpos.x-IsoUtil.FLOOR_SIZE.x/2f, wpos.y-IsoUtil.FLOOR_SIZE.y/2f);
        tilePos = IsoUtil.isometricToWorld(new Vector2(wpos.x-IsoUtil.FLOOR_SIZE.x/4,wpos.y-IsoUtil.FLOOR_SIZE.y/8), IsoUtil.FLOOR_SIZE);
        quadrant = IsoUtil.getTileQuadrant(tilePos, new Vector2(screenPos.x, screenPos.y));
        
        ass.updateTileInfo(screenPos, tilePos, map.getTileString((int)tilePos.x, (int)tilePos.y), quadrant, layer);
        
        // TODO Auto-generated method stub
        return false;
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
                break;
            case Circle:
                pencilTileRender(b);
                break;
            case Line:
                pencilTileRender(b);
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
        tVector = IsoUtil.worldToIsometric(tilePos, IsoUtil.FLOOR_SIZE);
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
        b.draw(loader.floors.get(file).get(selection).getTexture(), screenPos.x, screenPos.y);
    }
        catch(Exception e){b.setColor(1f,1f,1f,1f); return;}
        b.setColor(1f,1f,1f,1f);
    }

    /*
██████╗  █████╗ ██╗███╗   ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     ███████╗
██╔══██╗██╔══██╗██║████╗  ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
██████╔╝███████║██║██╔██╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     ███████╗
██╔═══╝ ██╔══██║██║██║╚██╗██║   ██║          ██║   ██║   ██║██║   ██║██║     ╚════██║
██║     ██║  ██║██║██║ ╚████║   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗███████║
╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝   ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝                                                                               
     */

     /*
╔═╗┌─┐┌┐┌┌─┐┬┬  
╠═╝├┤ ││││  ││  
╩  └─┘┘└┘└─┘┴┴─┘
      */
    /**
     * Place a tile down in the hover tile position
     * @return boolean representing successful input
     */
    private boolean pencil(){
        System.out.println("Placing " + ass.mode + " at " + screenPos.toString() +", tile: " + tilePos.toString());
        switch(mode){
            case Floor:
                try{
                    map.setFloor((int)tilePos.x, (int)tilePos.y, loader.floors.get(file).get(selection));
                    return true;
                }
                catch(Exception e){return false;}
            case Wall:
                try{
                    map.setWall((int)tilePos.x, (int)tilePos.y, IsoUtil.getTileQuadrant(tilePos, new Vector2(screenPos.x, screenPos.y)),loader.walls.get(quadrant).get(selection));
                    return true;
                }
                catch(Exception e){return false;}
            case Object:
                return false;
        }
        return false;
    }

    private boolean pencilEraser(){
        switch (ass.mode) {
            case Floor:
                map.setFloor((int)tilePos.x, (int)tilePos.y, null);
                return true;
            case Wall:
                map.setWall((int)tilePos.x, (int)tilePos.y, quadrant, null);
                return true;
            case Object:
                return true;
            default:
                return false;
        }
    }

/*
╦  ┬┌┐┌┌─┐
║  ││││├┤ 
╩═╝┴┘└┘└─┘
 */

    private boolean line(Vector2 endPos){
        Vector<Integer[]> l = PaintTools.line(clickPos, endPos);
        map.setSelection(l);

        for(int i = 0; i<l.size(); i++){
            map.setFloor(l.get(i)[0], l.get(i)[1],loader.floors.get(file).get(selection) );
        }
        return true;
    }

/*
╔═╗┬┬─┐┌─┐┬  ┌─┐
║  │├┬┘│  │  ├┤ 
╚═╝┴┴└─└─┘┴─┘└─┘
 */

    private boolean circle(Vector2 endPos){
        Vector<Integer[]> c = PaintTools.circle(clickPos, (int)clickPos.dst(endPos));

        for(int i = 0; i<c.size(); i++){
            map.setFloor(c.get(i)[0], c.get(i)[1],loader.floors.get(file).get(selection) );
        }
        return true;
    }

/*
╔╗ ┬ ┬┌─┐┬┌─┌─┐┌┬┐
╠╩╗│ ││  ├┴┐├┤  │ 
╚═╝└─┘└─┘┴ ┴└─┘ ┴ 
 */
    private boolean isBuckatable(int x, int y, Floor oldFloor, Floor newFloor){
        if (map.getFloor(x,y) != null && map.getFloor(x,y).getName() == newFloor.getName()) return false;
        if (map.inBounds(x, y) && (map.getFloor(x, y) == null || (oldFloor != null && oldFloor.getName() == map.getFloor(x,y).getName())) ) return true;
        return false;
    }

    private boolean bucket(){
        if (mode != PlacementModes.Floor) return false;
        

        Vector<Integer[]> queue = new Vector<Integer[]>(); // queue for points
        
        queue.add(new Integer[]{(int)tilePos.x, (int)tilePos.y}); // add our first point 
        Floor oldFloor = map.getFloor((int)tilePos.x, (int)tilePos.y); 
        Floor newFloor = loader.floors.get(file).get(selection);
        while(queue.size() > 0){
            Integer[] p = queue.get(queue.size()-1);
            System.out.println(p[0]+","+p[1]);  
            queue.remove(queue.size()-1);

            map.setFloor(p[0],p[1], newFloor);

            for(int k=0; k<bucket_row.length; k++){
                if(isBuckatable(p[0]+bucket_row[k], p[1]+bucket_col[k], oldFloor, newFloor)){
                    queue.add(new Integer[]{p[0]+bucket_row[k], p[1]+bucket_col[k]});
                }
            }
        }

        return true;
    }

    private boolean box(Vector2 endpos){
        int lx = tilePos.x < endpos.x ? (int) tilePos.x : (int) endpos.x;
        int ly = tilePos.y < endpos.y ? (int) tilePos.y : (int) endpos.y;
        int dx = (int)Math.abs(tilePos.x - endpos.x);
        int dy = (int)Math.abs(tilePos.y - endpos.y);

        for(int x=lx; x<lx+dx+1; x++){
            for(int y=ly; y<ly+dy+1; y++){
                if(x == tilePos.x || x == endpos.x || y == tilePos.y || y == endpos.y){
                    map.setFloor(x,y,loader.floors.get(file).get(selection));
                }
            }
        }

        return true;
    }
/*
██╗   ██╗████████╗██╗██╗     
██║   ██║╚══██╔══╝██║██║     
██║   ██║   ██║   ██║██║     
██║   ██║   ██║   ██║██║     
╚██████╔╝   ██║   ██║███████╗
 ╚═════╝    ╚═╝   ╚═╝╚══════╝
 */

 private void setState(State newState){
    this.paintState = newState;
    map.setSelection(null);
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
