package com.isomapmaker.game.controls.AtlasBrowser.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.isomapmaker.game.controls.AssetPlacer;
import com.isomapmaker.game.map.TileMaps.TileMapManager;

public class MouseInfoView extends Table {

    String cordInfo = "";
    AssetPlacer ap;
    Vector2 lastPos = new Vector2();
    Label temp = null;
    TileMapManager manager;
    float elapsed = 0f;
    public MouseInfoView(Skin skin, AssetPlacer ap){
        super(skin);
        temp = new Label("", skin);
        temp.setName("mouseInfo");
        this.ap = ap;
        this.add(temp).expand().center().row();
        this.manager = TileMapManager.getInstance();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO Auto-generated method stub
        super.draw(batch, parentAlpha);
        elapsed += Gdx.graphics.getDeltaTime();

        if(elapsed < 1) return; 
        elapsed = 0;
        if(lastPos.x != ap.tilePos.x || lastPos.y != ap.tilePos.y){
            lastPos.set(ap.tilePos);
            temp = this.findActor("mouseInfo");
        
            temp.setText("Screen: " + ap.screenPos.toString() +"\n" +"Tile: " + ap.tilePos.toString() ); // + "," + manager.getLayer(0).getTileString((int)lastPos.x, (int)lastPos.y)
        }
        

    }
    
}
