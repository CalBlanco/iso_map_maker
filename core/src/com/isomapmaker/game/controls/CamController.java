package com.isomapmaker.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.isomapmaker.game.map.AssetLoader;
import com.isomapmaker.game.ui.OnScreenText;
import com.isomapmaker.game.util.IsoUtil;


public class CamController implements InputProcessor {
    final private static float MIN_ZOOM = .5f;
    final private static float MAX_ZOOM = 12.0f;
    final private static Vector2 HOVER_OFFSET = new Vector2(-32f,-16f);
    final private static Vector2 FLOOR_SIZE = new Vector2(128,64);
    private OrthographicCamera camera;

    private float zoomSpeed, zoomScrollMult, panSpeed, panMult;
    private float finalPanSpeed;
    
    Texture nullTexture;

    public Vector2 hoverWorldPos;
    public Vector2 hoverTile;


    private Vector2 loadVector;
  


    
    public CamController(OrthographicCamera camera, float zoomSpeed, float panSpeed, float panMult){
        
        this.zoomSpeed = zoomSpeed;
        this.zoomScrollMult = 0.5f;
        this.panSpeed = panSpeed;
        this.finalPanSpeed = panSpeed;
        this.panMult = panMult;
        this.camera = camera;
        this.loadVector = new Vector2();
        this.nullTexture = new Texture(Gdx.files.internal("my_iso_assets/floor_highlight_128x64.png"));
        this.hoverWorldPos = new Vector2();
        this.hoverTile = new Vector2();

    }

    public void render(SpriteBatch batch){
        panCamera();
        

        
    }


    private void panCamera(){
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.position.x -= this.finalPanSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            camera.position.x += this.finalPanSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.position.y += this.finalPanSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.position.y -= this.finalPanSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            
            camera.zoom += this.zoomSpeed;
            camera.zoom = camera.zoom > MAX_ZOOM ? MAX_ZOOM : camera.zoom;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            camera.zoom -= this.zoomSpeed;
            camera.zoom = camera.zoom < MIN_ZOOM ? MIN_ZOOM : camera.zoom;
        }
       
    }


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SHIFT_LEFT:
                this.finalPanSpeed = this.panSpeed * this.panMult;
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.SHIFT_LEFT:
                this.finalPanSpeed = this.panSpeed;
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 v = camera.unproject(new Vector3(screenX,screenY,0));
        Vector2 world = new Vector2(v.x-FLOOR_SIZE.x/2,v.y-FLOOR_SIZE.y/2);
        hoverTile = IsoUtil.isometricToWorld(world, FLOOR_SIZE);
        hoverWorldPos.set(world.x,world.y);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float inc = camera.zoom + amountY*this.zoomScrollMult;
        if (inc > MAX_ZOOM){
            camera.zoom = MAX_ZOOM;
            return true;
        }
        if (inc < MIN_ZOOM){
            camera.zoom = MIN_ZOOM;
            return true;
        }

        camera.zoom += amountY*this.zoomScrollMult;
        
        return true;
    }

    

    public Vector2 getHoverTile(){
        return this.hoverTile;
    }
    


    
}
