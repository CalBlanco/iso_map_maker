package com.isomapmaker.game.controls.UiComponents;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PaintPanel extends Table {
    public PaintPanel(Skin skin){
        super(skin);
        this.add(new Label("Test", skin));
        this.row();
    }
}
