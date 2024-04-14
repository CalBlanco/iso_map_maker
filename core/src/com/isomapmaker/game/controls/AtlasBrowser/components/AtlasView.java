package com.isomapmaker.game.controls.AtlasBrowser.components;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Atlas.AtlasContainer;
import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;

public class AtlasView extends Table {
    /**
     * Just figured out I can make an actually good change listener by making a class that extends the type of Listener
     * class GoodListener extends ChangeListener {
     *  public GoodListener(pass args here){
     *  set args here 
     * }
     * 
     *  Override
     *  void changed(...){
     *      event logic
     *      }
     *  }
     * 
     * 
     * Then just add this as a listener passing the args we want to and we avoid the annoying global problems with these lambdas
     * 
     */
    
    private class TypeView extends Table {
        
        public TypeView(TileType type, Skin skin){
            super(skin);
            

            Vector<String> fileNames = TileAtlas.getInstance().getAssetsByType(type).keys();
            this.add(new Label(type.name(),skin)).row();
            for(int i=0; i<fileNames.size(); i++){
                this.add(new FileView(type, fileNames.get(i), skin));
            }
        }
    }



    private class FileView extends Table {
        class AssetChangeListener extends ChangeListener{
            TileType type;
            String name, assetName;
            public AssetChangeListener(TileType type, String name, String assetName){
                this.type = type;
                this.name = name;
                this.assetName = assetName;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ModeController.getInstance().setPlacementMode(type, name, assetName);
            }
            
        }
        
        /**
         * Create a type view that will show 
         * @param type The Tile Type this view shows
         * @param name The name of the file it is from 
         * @param skin The skin for the ui
         */
        public FileView(TileType type, String name, Skin skin){
            super(skin);
            

            Label l = new Label(name,skin);
            this.add(l).row();
            Vector<String> names = TileAtlas.getInstance().getAssetsByType(type).getRegionNames(name);
            Image im = null;

            ImageTextButton imText = null;
            
            
            Asset r = null;
            
            Collections.sort(names);

            for(int i=0; i<names.size(); i++){
                if(i % 3 == 0) this.row();
                r = TileAtlas.getInstance().getAssetsByType(type).getAssetFromAtlas(name, names.get(i));
                if(r == null) continue;
                im = new Image(r.getRegion());
                im.setScaling(Scaling.contain);
                
                l = new Label(names.get(i), skin);
                ImageTextButton.ImageTextButtonStyle sty = new ImageTextButton.ImageTextButtonStyle();
                sty.up = im.getDrawable();
                sty.down = im.getDrawable();
                sty.over = im.getDrawable();
                sty.font = skin.getFont("default-font");
                imText = new ImageTextButton(names.get(i), sty);
                
                imText.addListener(new AssetChangeListener(type, name, names.get(i)));

                
                this.add(imText).pad(5);
                
            }
        }


    }

   
    Skin skin;
    public AtlasView(Skin skin, Stage stage){
        


        super(skin);
        this.skin = skin;
        SelectBox<TileType> selectbox = new SelectBox<TileType>(skin);
        selectbox.setItems(TileType.values());
        selectbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ModeController.getInstance().setAssetState(selectbox.getSelected());
                renderView();
            }
        });

        this.add(selectbox).row();
    
        Table t = new Table(skin);
        t.setName("TileTypeContainer");
        t.add(new TypeView(ModeController.getInstance().getAssetState(), skin));
        this.add(t);


        
        
        

    }

    public void renderView(){
        Table t = this.findActor("TileTypeContainer");
        t.clear();
        t.add(new TypeView(ModeController.getInstance().getAssetState(), skin)).grow();
    }
}
