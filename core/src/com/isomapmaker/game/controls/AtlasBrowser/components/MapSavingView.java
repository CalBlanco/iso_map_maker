package com.isomapmaker.game.controls.AtlasBrowser.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.isomapmaker.game.util.MapSaver;

public class MapSavingView extends Table {
    String curName;
    public MapSavingView(Skin skin){
        super(skin);
        curName = "";
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("atlas/UI/MenuItems.atlas"));

        // need 2 buttons for now 
        // save 
        // add in 2 buttons manually for undo and redo
        Image im = new Image(atlas.findRegion("save"));
        im.setScaling(Scaling.fill);
        ImageTextButton.ImageTextButtonStyle sty = new ImageTextButton.ImageTextButtonStyle();
        sty.up = im.getDrawable();
        sty.down = im.getDrawable();
        sty.over = im.getDrawable();
        sty.font = skin.getFont("title-font");
        ImageTextButton imText = new ImageTextButton("save", sty);
        imText.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MapSaver.getInstance().saveNewMap(curName);
            }
        });

        this.add(im).expand();
        // load 

        im = new Image(atlas.findRegion("load"));
        im.setScaling(Scaling.fill);
        sty = new ImageTextButton.ImageTextButtonStyle();
        sty.up = im.getDrawable();
        sty.down = im.getDrawable();
        sty.over = im.getDrawable();
        sty.font = skin.getFont("title-font");
        imText = new ImageTextButton("load", sty);
        imText.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MapSaver.getInstance().readMaps(curName);
            }
        });
        this.add(im).expand();

        //text field input for map name
        TextField tf = new TextField(curName, skin);
        tf.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO Auto-generated method stub
                curName = tf.getMessageText();

            }
        });
        this.add(tf).expand();
    }
}
