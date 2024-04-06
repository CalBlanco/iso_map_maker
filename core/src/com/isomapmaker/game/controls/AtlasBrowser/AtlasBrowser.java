package com.isomapmaker.game.controls.AtlasBrowser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.isomapmaker.game.controls.AtlasBrowser.components.AtlasView;

public class AtlasBrowser extends Stage{
    
    Skin skin;
    Table root;
    public AtlasBrowser(){
        this.skin = new Skin(Gdx.files.internal("skins/uiskin/uiskin.json"));
        this.root = new Table();
        this.root.setFillParent(true);
        this.addActor(root);
        this.root.add(new Label("Carl wuz here",skin)).row();
        
        this.root.add(new AtlasView(skin));
        this.root.debugAll();

        
    }


    public void render(){
        super.act();
        super.draw();
    }

    public void dispose(){
        super.dispose();
    }
}
