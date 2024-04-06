package com.isomapmaker.game.controls.AtlasBrowser.components;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.isomapmaker.game.map.Atlas.AtlasContainer;
import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;

public class AtlasView extends Table {
    

    public AtlasView(Skin skin){
        super(skin);

        AtlasContainer floors = TileAtlas.getInstance().getAssetsByType(TileType.Floor);
        AtlasContainer walls = TileAtlas.getInstance().getAssetsByType(TileType.Wall);
        AtlasContainer objects = TileAtlas.getInstance().getAssetsByType(TileType.Object);

        
        Label l = new Label("Floors", skin);
        this.add(l).row();
        Vector<String> names = objects.getAssetNames();
        Vector<String> regions = new Vector<String>();
        TextureRegion r = null;
        
        l = new Label("Objects", skin);
        this.add(l).row();
        names = objects.getAssetNames();
        for(int i=0; i<names.size(); i++){
            this.add(l).padLeft(2).row();
            regions = objects.getRegionNames(names.get(i));
            if(regions == null || regions.size() < 1) continue;
            for(int j=0;j<regions.size(); j++){
                l = new Label(regions.get(j),skin);
                r = objects.getRegion(names.get(i), regions.get(j));
                this.add(l).padLeft(10);
                if(r != null) this.add(new Image(new TextureRegionDrawable(r), Scaling.contain));
                if(((i+1)*j + 1) % 7 == 0) this.row();
            }

        }

    }
}
