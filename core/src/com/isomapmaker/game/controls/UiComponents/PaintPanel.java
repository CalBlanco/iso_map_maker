package com.isomapmaker.game.controls.UiComponents;

import java.io.File;
import java.util.Vector;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.controls.PaintModes;
import com.isomapmaker.game.util.CursorSetter;


/**
 * Control buttons / panel styles for interacting with AssetPlacer mode 
 * 
 */
public class PaintPanel extends Table {
   
    public PaintPanel(Skin skin){
        
        super(skin);
        Vector<FileHandle> files = CursorSetter.getInstance().getFiles();
        Vector<PaintModes> modes = CursorSetter.getInstance().getModes();
        // Why would you be able to do this programatically
        // When having to assign each one individually is way cooler and more better
        // I tried iterating over the different cursors and giving them a function of calling the mode change to their corresponding mode
        // Nope dont work (the index needed to be global and on top of that it freaks out and just sets them all to the same thing)
        // So the solution? Add every single tool one by one and set the function manually!!!!!
        // Even though its the exact same fucking thing
        // Love it here
        // Love it HERE
        this.add(new Label("Paint Tools", skin)).row();
        
        // idk
        ImageButton ib = new ImageButton(new TextureRegionDrawable(new Texture(files.get(0))));
        ib.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ModeController.getInstance().setState(modes.get(0));
            }
        });
        this.add(ib).expand().fill().pad(10);
        this.add(new Label("Line", skin)).row();

        ib = new ImageButton(new TextureRegionDrawable(new Texture(files.get(1))));
        ib.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ModeController.getInstance().setState(modes.get(1));
            }
        });
        this.add(ib).expand().fill().pad(10);
        this.add(new Label("Circle", skin)).row();

        ib = new ImageButton(new TextureRegionDrawable(new Texture(files.get(2))));
        ib.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ModeController.getInstance().setState(modes.get(2));
            }
        });
        this.add(ib).expand().fill().pad(10);
        this.add(new Label("Box", skin)).row();

        ib = new ImageButton(new TextureRegionDrawable(new Texture(files.get(3))));
        ib.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ModeController.getInstance().setState(modes.get(3));
            }
        });
        this.add(ib).expand().fill().pad(10);
        this.add(new Label("Pencil", skin)).row();

        ib = new ImageButton(new TextureRegionDrawable(new Texture(files.get(4))));
        ib.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ModeController.getInstance().setState(modes.get(4));
            }
        });
        this.add(ib).expand().fill().pad(10);
        this.add(new Label("Bucket", skin)).row();
       
        this.row();
        
        
    }
}
