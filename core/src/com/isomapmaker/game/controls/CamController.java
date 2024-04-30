package com.isomapmaker.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.isomapmaker.game.util.IsoUtil;

/**
 * Controls the user's camera
 * Min/Max zoom are set as final static variables
 * When instantiating this class provide a zoom speed, pan speed and pan multiplier as well as the camera it is intended to control 
 * 
 * Scroll in / out controls zoom (as well as up and down arrows)
 * WASD control panning
 * Shift applies the pan multiplier 
 */
public class CamController implements InputProcessor {
    final private static float MIN_ZOOM = .25f;
    final private static float MAX_ZOOM = 32.0f;
    final private static Vector2 FLOOR_SIZE = new Vector2(128,64);
    private OrthographicCamera camera;

    private float zoomSpeed, zoomScrollMult, panSpeed, panMult;
    private float finalPanSpeed;
    
    
    public Vector2 hoverWorldPos;
    public Vector2 hoverTile;

  


    /**
     * Class to manage the camera movements 
     * @param camera : Camera to control 
     * @param zoomSpeed : Speed which the mouse zooms 
     * @param panSpeed : Speed at which WASD move the camera 
     * @param panMult : amount to multiply speed by when SHIFT is pressed 
     */
    public CamController(OrthographicCamera camera, float zoomSpeed, float panSpeed, float panMult){
        
        this.zoomSpeed = zoomSpeed;
        this.zoomScrollMult = 0.5f;
        this.panSpeed = panSpeed;
        this.finalPanSpeed = panSpeed;
        this.panMult = panMult;
        this.camera = camera;
        this.hoverWorldPos = new Vector2();
        this.hoverTile = new Vector2();

    }

    public void render(SpriteBatch batch){
        panCamera();
        

        
    }

    /**
     * Move the camera based on user inputs 
     * (This is implemented outside of the input processor I want it to move while the key is down not on every press and it was annoying doing extra work when I already had what I wanted working)
     */
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
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            
            camera.zoom += this.zoomSpeed;
            camera.zoom = camera.zoom > MAX_ZOOM ? MAX_ZOOM : camera.zoom;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            camera.zoom -= this.zoomSpeed;
            camera.zoom = camera.zoom < MIN_ZOOM ? MIN_ZOOM : camera.zoom;
        }
       
    }

    /**
     * Check for the speed multiplier and apply it 
     */
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

    /**
     * Reset the speed multiplier when the SHIFT key is released
     */
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

    private Vector2 tVector = new Vector2();
    private Vector3 tVector3 = new Vector3();
    private Vector2 tVector2 = new Vector2();
    // This is just remenants from when the camera controller was more tightly linked to the asset placement lmao
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 v = camera.unproject(tVector3.set(screenX,screenY,0));
        Vector2 world = tVector.set(v.x-FLOOR_SIZE.x/2,v.y-FLOOR_SIZE.y/2);
        hoverTile = IsoUtil.worldPosToIsometric(world, FLOOR_SIZE, tVector2);
        hoverWorldPos.set(world.x,world.y);
        return false;
    }


    /**
     * Mouse zoom control
     */
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
