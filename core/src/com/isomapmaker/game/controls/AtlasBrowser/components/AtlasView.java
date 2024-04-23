package com.isomapmaker.game.controls.AtlasBrowser.components;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.Batch;
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

import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;

/**
 * View to render the available assets based on ModeController state
 */
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

    private TileType mode;
    private String fileName, regionName;
    private WallQuadrant activeQuad;
    private class TypeView extends Table {
        
        public TypeView(TileType type, Skin skin){
            super(skin);
            

            Vector<String> fileNames = TileAtlas.getInstance().getAssetsByType(type).keys();
            this.add(new Label(type.name(),skin)).row();

            SelectBox<String> selectbox = new SelectBox<String>(skin);
            String[] arr = fileNames.toArray(new String[fileNames.size()]);
            selectbox.setItems(arr);
            selectbox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    ModeController.getInstance().setActiveFile(selectbox.getSelected());
                }
            });

            this.add(selectbox).row();
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

            boolean displayRotation = type == TileType.Object || type == TileType.Wall;

            for(int i=0; i<names.size(); i++){
                if(displayRotation && names.get(i).indexOf(ModeController.getInstance().getQuadrant().toString()) == -1) continue;
                if(i % 7 == 0) this.row();
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
                imText = new ImageTextButton(names.get(i).split("-")[0], sty);
                
                imText.addListener(new AssetChangeListener(type, name, names.get(i)));

                
                this.add(imText).pad(5);
                
            }

            this.add(new Label("Count: " + names.size(),skin));
        }


    }

   
    Skin skin;
    public AtlasView(Skin skin, Stage stage){
        


        super(skin);
        this.skin = skin;
        this.mode = TileType.Floor;
        this.fileName = "";
        this.regionName = "";
        this.activeQuad = WallQuadrant.bottom;
        SelectBox<TileType> selectbox = new SelectBox<TileType>(skin);
        selectbox.setItems(TileType.values());
        selectbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ModeController.getInstance().setAssetState(selectbox.getSelected());
                
            }
        });

        this.add(selectbox).row();
    

        Table t = new Table(skin);
        t.setName("TileTypeContainer");
        this.add(t).grow();

        this.background("default-window");

        
        
        

    }

    public void renderView(){
        Table t = this.findActor("TileTypeContainer");
        t.clear();
        TypeView tv = new TypeView(ModeController.getInstance().getAssetState(), skin);
        tv.setName("TypeViewRoot");
        Table r = new Table(skin);
        r.setName("TypeView");
        tv.add(r).row();
        t.add(tv).grow();
    }

    public void renderFile(){
        Table t = this.findActor("TypeView");
        t.clear();
        t.add(new FileView(ModeController.getInstance().getAssetState(), ModeController.getInstance().getActiveFile(), skin));

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO Auto-generated method stub
        super.draw(batch, parentAlpha);

        // Check if we need to update the ui 
        if(mode != ModeController.getInstance().getAssetState() || fileName != ModeController.getInstance().getActiveFile() || regionName != ModeController.getInstance().getActiveRegion() || activeQuad != ModeController.getInstance().getQuadrant()){
            mode = ModeController.getInstance().getAssetState();
            fileName = ModeController.getInstance().getActiveFile();
            regionName = ModeController.getInstance().getActiveRegion();
            activeQuad = ModeController.getInstance().getQuadrant();
            renderView();
            renderFile();
        }
        
    }
}
