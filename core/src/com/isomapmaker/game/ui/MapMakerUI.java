package com.isomapmaker.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MapMakerUI  {
    private Stage stage;
    private Skin skin;
    
    public MapMakerUI(){
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        final TextButton button = new TextButton("Click Me", skin, "default");
        button.setWidth(200);
        button.setHeight(50);
        stage.addActor(button);
    }

    public void render(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
        skin.dispose();
    }
}
