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

public class PaintToolView extends Table {
    // Need to render a panel of paint tools on the right hand side of the screen in a vertical stack

    Skin skin;
    TextureAtlas uiItems;
    public PaintToolView(Skin skin){
        super(skin);
        this.skin = skin;
        uiItems = new TextureAtlas(Gdx.files.internal("atlas/UI/MenuItems.atlas"));

        Table t = new Table(skin);
        t.add(new PaintToolButton(skin, "pencil")).grow().row();
        t.add(new PaintToolButton(skin, "bucket")).grow().row();
        t.add(new PaintToolButton(skin, "line")).grow().row();
        t.add(new PaintToolButton(skin, "circle")).grow().row();
        t.add(new PaintToolButton(skin, "box")).grow().row();
        this.add(t).grow();
        this.background("default-pane-noborder");

    }

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
