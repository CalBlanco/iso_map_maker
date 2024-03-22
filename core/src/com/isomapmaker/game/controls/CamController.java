package com.isomapmaker.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.isomapmaker.game.util.IsoUtil;


public class CamController implements InputProcessor {
    final private static float MIN_ZOOM = 1.0f;
    final private static float MAX_ZOOM = 10.0f;
    private OrthographicCamera camera;

    private float zoomSpeed, zoomScrollMult, panSpeed, panMult;
    private float finalPanSpeed;
    private Vector2 TileClicked;

    public CamController(OrthographicCamera camera, float zoomSpeed, float panSpeed, float panMult){
        TileClicked = new Vector2();
        this.zoomSpeed = zoomSpeed;
        this.zoomScrollMult = 0.5f;
        this.panSpeed = panSpeed;
        this.finalPanSpeed = panSpeed;
        this.panMult = panMult;
        this.camera = camera;
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
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){
            
            camera.zoom += this.zoomSpeed;
            camera.zoom = camera.zoom > MAX_ZOOM ? MAX_ZOOM : camera.zoom;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.X)){
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
        if (button == Input.Buttons.LEFT){
            Vector3 tPos = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
            Vector3 worldPos = camera.unproject(tPos); 
            Vector2 adjPos = IsoUtil.isometricToWorld(new Vector2(worldPos.x, worldPos.y), new Vector2(128,64));
            TileClicked.set((int)adjPos.x, (int)adjPos.y);
            return true;
        }
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

    public Vector2 getTileClicked(){
        return TileClicked;
    }

}
