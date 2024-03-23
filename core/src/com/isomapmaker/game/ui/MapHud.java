package com.isomapmaker.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.CamController;
import com.isomapmaker.game.map.AssetLoader;
import com.isomapmaker.game.map.MapLoader;

public class MapHud {
    /**
     * Display tile information in bottom left corner
     * Display menu items in top right? lol idk if this will be clickable (make it all controlled, arrows move directories or options)
     */
     final static Vector2 o = new Vector2();
     private OnScreenText tileInfo;

     AssetLoader assets;
     public MapHud(AssetLoader assets){
        tileInfo = new OnScreenText("null", o, "default.fnt");
        this.assets = assets;
     }


     public void render(SpriteBatch batch, CamController ccont, MapLoader ml){
        tileInfo.setPos(new Vector2(0, 0+tileInfo.getBound().y));
        String dataString = ml.textureDataString(ccont.getHoverTile().x, ccont.getHoverTile().y);
        if (dataString != null) tileInfo.setText(dataString);
        tileInfo.render(batch);
     }
}
