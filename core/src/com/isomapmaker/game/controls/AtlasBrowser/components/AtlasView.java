package com.isomapmaker.game.controls.AtlasBrowser.components;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
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
        TileType type;
        public TypeView(TileType type, Skin skin){
            super(skin);
            this.type = type;

            Vector<String> fileNames = TileAtlas.getInstance().getAssetsByType(type).keys();
            this.add(new Label(type.name(),skin)).row();
            for(int i=0; i<fileNames.size(); i++){
                this.add(new FileView(type, fileNames.get(i), skin));
            }
        }
    }



    private class FileView extends Table {
        TileType type;
        String name;
        Skin skin;
        /**
         * Create a type view that will show 
         * @param type The Tile Type this view shows
         * @param name The name of the file it is from 
         * @param skin The skin for the ui
         */
        public FileView(TileType type, String name, Skin skin){
            super(skin);
            this.skin = skin;
            this.type = type;
            this.name = name;

            Label l = new Label(name,skin);
            this.add(l).row();
            Vector<String> names = TileAtlas.getInstance().getAssetsByType(type).getRegionNames(name);
            Image im = null;
            
            Asset r = null;
            
            for(int i=0; i<names.size(); i++){
                if(i % 7 == 0) this.row();
                r = TileAtlas.getInstance().getAssetsByType(type).getAssetFromAtlas(name, names.get(i));
                if(r == null) continue;
                im = new Image(r.getRegion());
                im.setScaling(Scaling.contain);
                l = new Label(names.get(i), skin);
                this.add(l).pad(5);
                this.add(im).pad(5);
            }
        }


    }

    class DWindowOpener extends ChangeListener {
        Stage s;
        Table t;
        Dialog a;
        public DWindowOpener(Stage s, Table t, String name){
            this.s=s;
            this.t = t;
            a = (Dialog) t.findActor(name);
            
        }
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            a.show(s);
        }
        
    }

    public AtlasView(Skin skin, Stage stage){
        


        super(skin);

        Dialog dWin = new Dialog("Floors", skin);
        dWin.add(new TypeView(TileType.Floor, skin));
        dWin.setName("FloorDialog");
        dWin.hide();
        this.add(dWin);

        dWin = new Dialog("Walls", skin);
        dWin.add(new TypeView(TileType.Wall, skin));
        dWin.setName("WallDialog");
        dWin.hide();
        this.add(dWin);

        dWin = new Dialog("Object", skin);
        dWin.add(new TypeView(TileType.Object, skin));
        dWin.setName("ObjectDialog");
        dWin.hide();
        this.add(dWin);


        TextButton btn = new TextButton("Floor Dialog", skin);
        btn.addListener(new DWindowOpener(stage, this, "FloorDialog"));

        this.add(btn);

        btn = new TextButton("Wall Dialog", skin);
        btn.addListener(new DWindowOpener(stage, this, "WallDailog"));
        this.add(btn);

        btn = new TextButton("Object Dialog", skin);
        btn.addListener(new DWindowOpener(stage, this, "ObjectDialog"));
        this.add(btn);




        
        
        

    }
}
