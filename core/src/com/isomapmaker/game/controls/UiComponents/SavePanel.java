package com.isomapmaker.game.controls.UiComponents;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMapManager;
import com.isomapmaker.game.util.MapSaver;

public class SavePanel extends Table {
    String name="myMap";

    public SavePanel(Skin skin, TileMapManager manager, TileLoader loader){
        super(skin);

        

        TextField mapName = new TextField("myMap", skin);
        mapName.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                name = mapName.getText();
                mapName.next(true);
            }
        });
        this.add(mapName).grow().top().center().row();




        ImageButton ib = new ImageButton(new TextureRegionDrawable(new Texture("save.png")));
        ib.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Saving map as: " + name);
                MapSaver.getInstance().saveNewMap(name, manager);
            }
        });

        this.add(ib).grow();

        ib = new ImageButton(new TextureRegionDrawable(new Texture("load.png")));
        ib.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Attempting to load map: " + name);
                MapSaver.getInstance().readMaps(name, manager);
            }
        });

        this.add(ib).grow();

    
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        
        super.draw(batch, parentAlpha);
    }
    


}
