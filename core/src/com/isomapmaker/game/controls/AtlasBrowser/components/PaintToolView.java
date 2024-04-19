package com.isomapmaker.game.controls.AtlasBrowser.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.controls.PaintModes;
import com.isomapmaker.game.controls.commands.Commander;

/**
 * Simple View for the paint tools currently supported in the game 
 */
public class PaintToolView extends Table {
    // Need to render a panel of paint tools on the right hand side of the screen in a vertical stack

    Skin skin;
    TextureAtlas uiItems;
    public PaintToolView(Skin skin){
        super(skin);
        this.skin = skin;
        uiItems = new TextureAtlas(Gdx.files.internal("atlas/UI/MenuItems.atlas"));

        Table t = new Table(skin);
        t.add(new PaintToolButton(skin, "pencil")).expand().row();
        t.add(new PaintToolButton(skin, "bucket")).expand().row();
        t.add(new PaintToolButton(skin, "line")).expand().row();
        t.add(new PaintToolButton(skin, "circle")).expand().row();
        t.add(new PaintToolButton(skin, "box")).expand().row();
        

        // add in 2 buttons manually for undo and redo
        Image im = new Image(uiItems.findRegion("undo"));
        im.setScaling(Scaling.fill);
        ImageTextButton.ImageTextButtonStyle sty = new ImageTextButton.ImageTextButtonStyle();
        sty.up = im.getDrawable();
        sty.down = im.getDrawable();
        sty.over = im.getDrawable();
        sty.font = skin.getFont("default-font");
        ImageTextButton imText = new ImageTextButton("undo", sty);
        imText.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Commander.getInstance().undo();
            }
        });
        t.add(imText).expand();

        im = new Image(uiItems.findRegion("redo"));
        im.setScaling(Scaling.fill);
        sty = new ImageTextButton.ImageTextButtonStyle();
        sty.up = im.getDrawable();
        sty.down = im.getDrawable();
        sty.over = im.getDrawable();
        sty.font = skin.getFont("default-font");
        imText = new ImageTextButton("redo", sty);
        imText.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Commander.getInstance().redo();
            }
        });

        t.add(imText).expand().row();


        this.add(t).grow();
        this.background("default-pane-noborder");

    }

    /**
     * ImageTextButton inside of a table for the image button we want to make 
     */
    public class PaintToolButton  extends Table {
        public PaintToolButton(Skin skin, String imageName){
            super(skin);
            Image im = new Image(uiItems.findRegion(imageName));
            im.setScaling(Scaling.fill);
            ImageTextButton.ImageTextButtonStyle sty = new ImageTextButton.ImageTextButtonStyle();
            sty.up = im.getDrawable();
            sty.down = im.getDrawable();
            sty.over = im.getDrawable();
            sty.font = skin.getFont("default-font");
            ImageTextButton  imText = new ImageTextButton(imageName, sty);
            
            // Set Paint state to image name (a little extra work is done here to make it line up with the enum for the state change)
            imText.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // TODO Auto-generated method stub
                    ModeController.getInstance().setState(PaintModes.valueOf(imageName.substring(0,1).toUpperCase() + imageName.substring((1))));     
                }
            });
            
            this.add(imText).grow();


        }
    }

}
