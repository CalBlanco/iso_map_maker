package com.isomapmaker.game.controls;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
import com.isomapmaker.game.controls.AtlasBrowser.AtlasBrowser;
import com.isomapmaker.game.controls.commands.BoxCommand;
import com.isomapmaker.game.controls.commands.BoxEraserCommand;
import com.isomapmaker.game.controls.commands.BucketCommand;
import com.isomapmaker.game.controls.commands.CircleCommand;
import com.isomapmaker.game.controls.commands.Command;
import com.isomapmaker.game.controls.commands.Commander;
import com.isomapmaker.game.controls.commands.FullEraser;
import com.isomapmaker.game.controls.commands.LineCommand;
import com.isomapmaker.game.controls.commands.PaintBrushCommand;
import com.isomapmaker.game.controls.commands.PencilCommand;
import com.isomapmaker.game.controls.commands.PencilEraserCommand;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.TileMaps.TileMapManager;
import com.isomapmaker.game.util.MapSaver;
import com.isomapmaker.game.util.IsoUtil;

/**
 * Class that places assets down on the TileMap's specifically it interacts with the TileMapManager to get the active layer's TileMap and then interacts with that map
 */
public class AssetPlacer implements InputProcessor {
    final Vector2[] activeOffsets = new Vector2[]{new Vector2(-256,-64), new Vector2(0,0), new Vector2(256,-64)};
    
 
    OrthographicCamera cam;
    TileMapManager manager;
    int selection = 0;
    int layer = 0;
    TileMap map;

    public Vector2 tilePos;
    public Vector2 screenPos;
    WallQuadrant quadrant = WallQuadrant.top;
    TileType mode = TileType.Floor;
    String file = "Dry";


    Vector2 lowHighlightBound = new Vector2(0,0);
    Vector2 highHighlightBound = new Vector2(0,0);

    Vector2 clickPos = new Vector2(0,0);
    HashMap<WallQuadrant, TextureRegion> quadrantToHighlight = new HashMap<WallQuadrant, TextureRegion>();

    Vector2 tVector = new Vector2(0,0);

    PaintModes paintState;

    Vector<Integer[]> tileSelection; // the currently selected tiles based on the tool 
    Pixmap pencil,pm;

    AtlasBrowser atlasBrowser = null;

    boolean controlModifier = false;

    public AssetPlacer(OrthographicCamera cam, TileMapManager manager){

        
        this.paintState = ModeController.getInstance().getState();
        this.cam = cam; 
        this.manager = manager;
        this.map = manager.getLayer(layer);
        this.tilePos = new Vector2(0,0);
        this.screenPos = new Vector2(0,0);
        

        pm = new Pixmap(Gdx.files.internal("cursor.png"));
        pencil = new Pixmap(Gdx.files.internal("pencil.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 15, 15));

        
		
    }
   

    // Only going to be useful in pencil mode for now needs to be expanded to brushes and selections
    /*
$$\   $$\                          $$$$$$$\                                          
$$ | $$  |                         $$  __$$\                                         
$$ |$$  / $$$$$$\  $$\   $$\       $$ |  $$ | $$$$$$\   $$$$$$\   $$$$$$$\  $$$$$$$\ 
$$$$$  / $$  __$$\ $$ |  $$ |      $$$$$$$  |$$  __$$\ $$  __$$\ $$  _____|$$  _____|
$$  $$<  $$$$$$$$ |$$ |  $$ |      $$  ____/ $$ |  \__|$$$$$$$$ |\$$$$$$\  \$$$$$$\  
$$ |\$$\ $$   ____|$$ |  $$ |      $$ |      $$ |      $$   ____| \____$$\  \____$$\ 
$$ | \$$\\$$$$$$$\ \$$$$$$$ |      $$ |      $$ |      \$$$$$$$\ $$$$$$$  |$$$$$$$  |
\__|  \__|\_______| \____$$ |      \__|      \__|       \_______|\_______/ \_______/ 
                   $$\   $$ |                                                        
                   \$$$$$$  |                                                        
                    \______/                                                         
     */

    @Override
    public boolean keyDown(int keycode) {
        resetFocus();
        if(controlModifier){
            switch(keycode){
                case Input.Keys.C: // full tile clear 
                    FullEraser eraser = new FullEraser(tilePos, map);
                    Commander.getInstance().run(eraser);
                break;
                case Input.Keys.Q:
                    
                    ModeController.getInstance().incrementBrushSize(-2);
                break;
                case Input.Keys.R:
                    
                    ModeController.getInstance().incrementBrushSize(2);
                break;

            }
        }
        else{
            switch(keycode){
                case Input.Keys.CONTROL_LEFT:
                    controlModifier = true;
                    break;
                case Input.Keys.C: // Eraser tool 
                    PencilEraserCommand peraser = new PencilEraserCommand(tilePos, ModeController.getInstance().getQuadrant(), map);
                    Commander.getInstance().run(peraser);
                    return true;
                case Input.Keys.PAGE_UP: // shift up a layer
                    if(layer+1 > manager.maxLayer()) return false; // make a new layer if there is not one
                    layer +=1;
                    map = manager.getLayer(layer); // get next layer
                    return true;
                case Input.Keys.PAGE_DOWN: // Shift down a layer 
                    if(layer-1 < 0) return false;
                    layer -= 1;
                    map = manager.getLayer(layer);
                    return true;
                case Input.Keys.L: // Change edit mode to line 
                    setState(PaintModes.Line);
                    return true;                
                case Input.Keys.P: // Change edit mode to pencil 
                    setState(PaintModes.Pencil);
                    return true;
                case Input.Keys.I:
                    setState(PaintModes.Brush);
                    return true;
                case Input.Keys.O: // Change edit mode to cirlce 
                    setState(PaintModes.Circle);
                    return true;
                case Input.Keys.K: // Change edit mode to bucket
                    setState(PaintModes.Bucket);
                    return true;
                case Input.Keys.B: // Change edit mode to box 
                    setState(PaintModes.Box);
                    return true;
                case Input.Keys.Z: // Undo last performed command 
                    Commander.getInstance().undo();
                    return true;
                case Input.Keys.V: // Redo last undone command 
                    Commander.getInstance().redo();
                    return true;
                case Input.Keys.NUM_9: // Save Map 
                    MapSaver.getInstance().saveNewMap("MyMap");
                    break;
                case Input.Keys.NUM_0: // Load Map 
                    MapSaver.getInstance().readMaps("MyMap");
                    break;
                case Input.Keys.R: // Change Rotation 
                    ModeController.getInstance().incrementQuadrant();
                    break;
                case Input.Keys.Q:
                    ModeController.getInstance().decrementQuadrant();
                    break;
                
                
            }
        }
        return false;
      }


    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.CONTROL_LEFT){
            controlModifier = false;
            return true;
        }
        return false;
    }

   
    /*
 __       __                                        
|  \     /  \                                        
| $$\   /  $$  ______   __    __   _______   ______  
| $$$\ /  $$$ /      \ |  \  |  \ /       \ /      \ 
| $$$$\  $$$$|  $$$$$$\| $$  | $$|  $$$$$$$|  $$$$$$\
| $$\$$ $$ $$| $$  | $$| $$  | $$ \$$    \ | $$    $$
| $$ \$$$| $$| $$__/ $$| $$__/ $$ _\$$$$$$\| $$$$$$$$
| $$  \$ | $$ \$$    $$ \$$    $$|       $$ \$$     \
 \$$      \$$  \$$$$$$   \$$$$$$  \$$$$$$$   \$$$$$$$
     */

    boolean mouseHeldDown = false;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        resetFocus();
        clickPos = tilePos;

        
        mouseHeldDown = true;
        
        return false;
        }

    Vector2 endClickTVector = new Vector2();
    Vector3 endClickCamTVector3 = new Vector3();
    Vector2 endclick = new Vector2();
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        resetFocus();
        // if click pos != tile pos we have moved the cursor while selecting
        // handle area selection
        Vector3 wpos = cam.unproject(endClickCamTVector3.set(screenX,screenY,0));
        endclick = IsoUtil.worldPosToIsometric(endClickTVector.set(wpos.x, wpos.y), IsoUtil.FLOOR_SIZE, endclick);
        

        //
        switch(this.paintState){
            case Box:
                Command c = null;
                if(controlModifier){
                    c = new BoxEraserCommand(clickPos, endclick, map);
                }
                else{
                    c = new BoxCommand(clickPos, endclick, ModeController.getInstance().getActiveAsset(), map);
                }
                Commander.getInstance().run(c);
                break;
            case Circle:
                CircleCommand circ = new CircleCommand((int)clickPos.x, (int)clickPos.y, (int)clickPos.dst(endclick), ModeController.getInstance().getActiveAsset(), map);
                Commander.getInstance().run(circ);
                break;
            case Line:
                LineCommand li = new LineCommand(clickPos, endclick, ModeController.getInstance().getActiveAsset(), map);
                Commander.getInstance().run(li);
                break;
            case Pencil:
                PencilCommand pen = new PencilCommand(file, ModeController.getInstance().getQuadrant(), selection, endclick, screenPos, map);
                Commander.getInstance().run(pen);
                break;
            case Bucket:
                BucketCommand buk = new BucketCommand((int)endclick.x, (int)endclick.y, ModeController.getInstance().getActiveAsset(), map);
                Commander.getInstance().run(buk);
                break;
            case Brush:
                PaintBrushCommand brush = new PaintBrushCommand(map, brushSelection);
                Commander.getInstance().run(brush);
                break;
            default:
                mouseHeldDown = false;
                return false;
        }
        clickPos = null;
        mouseHeldDown = false; // reset mouse hold 

        brushSelection.clear(); // clear the paintbrush

        return true;
      }

    
    Vector3 camTVector = new Vector3();
    Vector2 tileMathTVector = new Vector2();
    Set<String> brushSelection = new HashSet<String>();
    

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        
        
        Vector3 wpos = cam.unproject(camTVector.set(screenX,screenY,0));
        screenPos.set(wpos.x-IsoUtil.FLOOR_SIZE.x/4f, wpos.y-IsoUtil.FLOOR_SIZE.y/4f);
        tilePos = IsoUtil.worldPosToIsometric(tileMathTVector.set(wpos.x-IsoUtil.FLOOR_SIZE.x/2f,wpos.y-IsoUtil.FLOOR_SIZE.y/8), IsoUtil.FLOOR_SIZE, tilePos);
        //quadrant = IsoUtil.getTileQuadrant(tilePos, new Vector2(screenPos.x, screenPos.y));

        
        
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
    
    
    

    


    
    /*
███████╗████████╗ █████╗ ████████╗███████╗    ██████╗ ███████╗███╗   ██╗██████╗ ███████╗██████╗ 
██╔════╝╚══██╔══╝██╔══██╗╚══██╔══╝██╔════╝    ██╔══██╗██╔════╝████╗  ██║██╔══██╗██╔════╝██╔══██╗
███████╗   ██║   ███████║   ██║   █████╗      ██████╔╝█████╗  ██╔██╗ ██║██║  ██║█████╗  ██████╔╝
╚════██║   ██║   ██╔══██║   ██║   ██╔══╝      ██╔══██╗██╔══╝  ██║╚██╗██║██║  ██║██╔══╝  ██╔══██╗
███████║   ██║   ██║  ██║   ██║   ███████╗    ██║  ██║███████╗██║ ╚████║██████╔╝███████╗██║  ██║
╚══════╝   ╚═╝   ╚═╝  ╚═╝   ╚═╝   ╚══════╝    ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═════╝ ╚══════╝╚═╝  ╚═╝
     */

    /**
     * Render the appropriate highlights based on the PaintMode
     * @param b
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
            case Brush:
                brushRender(b);
                break;
            default:
                break;

        }
       
    }

    
    private Vector2 outVector = new Vector2();
    /**
     * Render the pencil tile tool availability
     */
    private void pencilTileRender(SpriteBatch b){
        tVector = IsoUtil.isometricToWorldPos(lineTVector20.set(1,1).scl(layer).add(tilePos), IsoUtil.FLOOR_SIZE, tVector);
        b.setColor(1f, 1f, 1f, 0.7f);
        try{
        switch (mode) {
            case Floor:
                b.draw(ModeController.getInstance().getActiveAsset().getRegion(), tVector.x, tVector.y);
                break;
            case Wall:
                b.draw(quadrantToHighlight.get(this.quadrant), tVector.x, tVector.y);
                b.draw(ModeController.getInstance().getActiveAsset().getRegion(), tVector.x, tVector.y);
                break;
            default:
                break;
        }
        
    }
        catch(Exception e){b.setColor(1f,1f,1f,1f); return;}
        b.setColor(1f,1f,1f,1f);
    }

    private Vector3 lineTVector30 = new Vector3();
    private Vector2 lineTVector20 = new Vector2();
    private Vector2 lineTVector21 = new Vector2();
    private Vector2 lineTVector22 = new Vector2();
    Vector2 ht = new Vector2();
    
    /**
     * Render the line tool 
     * @param b
     */
    private void lineRender(SpriteBatch b){
        
        Vector3 hpos = cam.unproject(lineTVector30.set(Gdx.input.getX(), Gdx.input.getY(), 0)); // unproject mouse position from screen to world
        ht = IsoUtil.worldPosToIsometric(lineTVector20.set(hpos.x,hpos.y), IsoUtil.FLOOR_SIZE, ht); // convert to isometric cordinates 
        Vector2 v = clickPos != null ? lineTVector20.set(1,1).scl(layer).add(clickPos) : lineTVector20.set(1,1).scl(layer).add(ht); // get the click pos

        Vector2 t = lineTVector22.set(1,1).scl(layer).add(ht);
        Vector<Integer[]> linePoints = PaintTools.line((int)v.x, (int)v.y, (int)t.x, (int)t.y); 
        
        
        for(int i=0; i<linePoints.size(); i++){
            
            tVector = IsoUtil.isometricToWorldPos(lineTVector20.set(1,1).scl(layer).add(lineTVector21.set(linePoints.get(i)[0],linePoints.get(i)[1])), IsoUtil.FLOOR_SIZE, tVector);
            b.draw(ModeController.getInstance().getActiveAsset().getRegion(), tVector.x, tVector.y);
        }
       
        b.setColor(1,1,1,1);

    }
    
    /**
     * Render the cirlce tool 
     * @param b
     */
    private void circleRender(SpriteBatch b){
        
        Vector3 hpos = cam.unproject(lineTVector30.set(Gdx.input.getX(), Gdx.input.getY(), 0)); // unproject mouse position from screen to world
        ht = IsoUtil.worldPosToIsometric(lineTVector20.set(hpos.x,hpos.y), IsoUtil.FLOOR_SIZE, ht); // convert to isometric cordinates 
        Vector2 v = clickPos != null ? lineTVector20.set(1,1).scl(layer).add(clickPos) : lineTVector20.set(1,1).scl(layer).add(ht); // get the click pos

        
        Vector<Integer[]> linePoints = PaintTools.circle(v,(int) ht.dst(v));
        

        b.setColor(1f,1f,1f,0.7f);
        tVector = IsoUtil.isometricToWorldPos(v, IsoUtil.FLOOR_SIZE, outVector);
        b.draw(ModeController.getInstance().getActiveAsset().getRegion(), tVector.x, tVector.y);


        try{
        for(int i=0; i<linePoints.size(); i++){
            tVector = IsoUtil.isometricToWorldPos(lineTVector20.set(1,1).scl(layer).add(linePoints.get(i)[0],linePoints.get(i)[1]), IsoUtil.FLOOR_SIZE, tVector);
            b.draw(ModeController.getInstance().getActiveAsset().getRegion(), tVector.x, tVector.y);
        }
        }
        catch(Exception e){e.printStackTrace();}
        b.setColor(1,1,1,1);
    }

    /**
     * Render the box tool 
     * @param b
     */
    private void boxRender(SpriteBatch b){
       
        Vector3 hpos = cam.unproject(lineTVector30.set(Gdx.input.getX(), Gdx.input.getY(), 0)); // unproject mouse position from screen to world
        ht = IsoUtil.worldPosToIsometric(lineTVector20.set(hpos.x,hpos.y), IsoUtil.FLOOR_SIZE, ht); // convert to isometric cordinates 
        Vector2 v = clickPos != null ? lineTVector20.set(1,1).scl(layer).add(clickPos) : lineTVector20.set(1,1).scl(layer).add(ht); // get the click pos

        
        Vector<Vector2> bSel = PaintTools.box(v, ht);

       

        b.setColor(1f,1f,1f,0.7f);
        tVector = IsoUtil.isometricToWorldPos(v, IsoUtil.FLOOR_SIZE, outVector);
        b.draw(ModeController.getInstance().getActiveAsset().getRegion(), tVector.x, tVector.y);
        for(int i=0;i<bSel.size(); i++){
            tVector = IsoUtil.isometricToWorldPos(bSel.get(i).add(lineTVector20.set(1,1).scl(layer)), IsoUtil.FLOOR_SIZE, tVector);
            b.draw(ModeController.getInstance().getActiveAsset().getRegion(), tVector.x, tVector.y);
        }
        b.setColor(1,1,1,1);
    }


    // Holy fuck this is gnarly lmaoooooo 
    /**Render paint brush selections */
    Vector2 brushVector = new Vector2(0,0);
    String[] brushPointSplit = new String[2];
    Vector2 brushDrawVector1 = new Vector2(0,0);
    Vector2 brushDrawVector2 = new Vector2(0,0);
    Vector2 brushDrawVector3 = new Vector2(0,0);
    Vector2 brushDrawVector4 = new Vector2(0,0);
    private void brushRender(SpriteBatch b){
        // Paint Brush specific (save points we have visited)
        Vector3 hpos = cam.unproject(lineTVector30.set(Gdx.input.getX(), Gdx.input.getY(), 0)); // unproject mouse position from screen to world
        ht = IsoUtil.worldPosToIsometric(lineTVector20.set(hpos.x,hpos.y), IsoUtil.FLOOR_SIZE, ht); // convert to isometric cordinates 

        String pointString = ht.x +"x" + ht.y;
        if(ModeController.getInstance().getState() == PaintModes.Brush && !brushSelection.contains(pointString) && mouseHeldDown){
            brushSelection.add(pointString);
            System.out.println("Added: " + pointString);
        }

        if(brushSelection.size() < 0) return;

        b.setColor(1f,1f,1f,0.7f);
        for(String s: brushSelection){
            brushPointSplit = s.split("x",2);
            brushVector.set(Float.parseFloat(brushPointSplit[0]), Float.parseFloat(brushPointSplit[1]));

            //generate circle at point based on brush size
            Vector<Integer[]> circlePoints = PaintTools.circle(brushVector, ModeController.getInstance().getBrushSize());
            int uniquePoints = circlePoints.size() / 4; // get unique number of points 
            for(int i=0; i< uniquePoints; i++){
                
                // want a line from points [(0+4*i)] to 1+4*i and 2+4*i to 3+4*i to fill circle
                int p1 = 0+4*i;
                int p2 = 1+4*i;
                int p3 = 2+4*i;
                int p4 = 3+4*i;
                Vector<Integer[]> topLine = PaintTools.line(circlePoints.get(p1)[0], circlePoints.get(p1)[1], circlePoints.get(p2)[0], circlePoints.get(p2)[1]);
                Vector<Integer[]> bottomLine = PaintTools.line(circlePoints.get(p3)[0], circlePoints.get(p3)[1], circlePoints.get(p4)[0], circlePoints.get(p4)[1]);

                for(int linePoint = 0; linePoint < topLine.size(); linePoint++){
                    brushDrawVector3.set(topLine.get(linePoint)[0],topLine.get(linePoint)[1]); // set the isometric cordinates into temp vectors
                    brushDrawVector4.set(bottomLine.get(linePoint)[0],bottomLine.get(linePoint)[1]);
                    
                    // get the world pos
                    IsoUtil.isometricToWorldPos(brushDrawVector3, IsoUtil.FLOOR_SIZE, brushDrawVector1); 
                    IsoUtil.isometricToWorldPos(brushDrawVector4, IsoUtil.FLOOR_SIZE, brushDrawVector2); 
                    b.draw(ModeController.getInstance().getActiveAsset().getRegion(), brushDrawVector1.x, brushDrawVector1.y);
                    b.draw(ModeController.getInstance().getActiveAsset().getRegion(), brushDrawVector2.x, brushDrawVector2.y);
                }
            }

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

 /**
  * Change the paint mode 
  * @param newState
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
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
        
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
        
    }


    /**
     * Setter for atlas browser
     * (Annoying requirement to clear the ui, I don't make the rules )
     * @param atlasBrowser
     */
    public void setAtlasBrowser(AtlasBrowser atlasBrowser){this.atlasBrowser = atlasBrowser;}

    /**
     * Idk why I even have this getter but whateva 
     * @return
     */
    public AtlasBrowser getAtlasBrowser(){return this.atlasBrowser;}
    
    /**
     * Clear the focus of the ui when the game is interacted with 
     */
    public void resetFocus(){
        if(this.atlasBrowser == null) return;
        this.atlasBrowser.unfocusAll();
    }

}
