package com.isomapmaker.game.controls;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.Tiles.Floor;
import com.isomapmaker.game.map.Tiles.SimpleTile;
import com.isomapmaker.game.map.Tiles.Wall;
import com.isomapmaker.game.map.Tiles.Object;
import com.isomapmaker.game.util.IsoUtil;

// Controll the TileLoader class 
// Allow user to navigate between floors, walls and objects
// Within those allow user to navigate different textures
// and within those display the "currently active" TextureRegion as well as the TextureRegion above, and below 
// want to be able to output what ever we are using to our map building controller 
public class AssetController extends Stage {
    
    Skin skin;
    TileLoader tl;
    Table root;
    
    String mode = "Floor";
    String activeFile = "Highlights";

    int fullySmart = 0;

    public Floor f;
    public Wall w;
    public Object o;

    public AssetController(TileLoader tl){
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.tl = tl;
        mode = "Floor";
        // initialize our root table and add it to the scene 
        root = new Table();
        root.setFillParent(true);
        this.addActor(root); 

        // 3 panels

        //Left Panel
        Table left = new Table();

        Label leftPanel = new Label("Left_Panel", skin);
        
        

        Table mouseInfo = new Table(skin);
        Label mouseInfoLabel = new Label("--Mouse info--",skin);
        Label screenPos = new Label("Screen: ()", skin);
        Label tilePos = new Label("Tile: ()", skin);
        Label hoverTile = new Label("Hovered Tile:", skin);
        
        screenPos.setName("screenPos");
        tilePos.setName("tilePos");
        hoverTile.setName("hoverTile");

        mouseInfo.add(mouseInfoLabel).center().row();
        mouseInfo.add(screenPos).left().row();
        mouseInfo.add(tilePos).left().row();
        mouseInfo.add(hoverTile).left().row();

        
        left.row().grow();
        mouseInfo.left().bottom();
        left.add(mouseInfo);


        //Middle Panel
        Table middle = new Table();

        Label middlePanel = new Label("", skin);
        middlePanel.setName("middleHeader");
        middle.add(middlePanel).top();

        // asset browsing 
        Table assetBrowser = new Table();
        Label typeLabel = new Label("Available Tiles ", skin);
        SelectBox<String> typeSelect = new SelectBox<String>(skin);
        typeSelect.setItems(new String[]{"Floor","Wall", "Object"});
        typeSelect.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent e, Actor a){
            mode = typeSelect.getSelected();
            updateFileSelection();
           } 
        });

        SelectBox<String> fileSelect =  new SelectBox<String>(skin);
        fileSelect.setName("fileSelect");
       
 


        assetBrowser.row();
        assetBrowser.add(typeLabel).pad(10);
        assetBrowser.add(typeSelect).pad(10);
        assetBrowser.add(fileSelect).pad(10).bottom();
        
        Table typeBrowser = new Table();
        
    

        middle.row().grow();
        middle.add(new Label(" ",skin)).row();
        middle.add(assetBrowser).bottom();
        

        //Right Panel
        Table right = new Table();
        Label rightPanel = new Label("Right_Panel", skin);

        right.add(rightPanel).top();

        //header
        root.add(new Label("Iso Map Maker", skin)).colspan(12).row();

        // left panel
        root.add(left).grow().colspan(2);
        //middle panel
        root.add(middle).grow().colspan(8);
        // right panel
        root.add(right).grow().colspan(2);
        root.row();

        

        


        // footer space
        root.add(new Label("footer", skin)).colspan(12).row();
        
        //root.debugAll();
    }

    public void render(){
        super.act();
        super.draw();
    }

    public void dispose(){
        super.dispose();
    }


    public void updateFileSelection(){
        SelectBox<String> box = root.findActor("fileSelect");
        String[] fileKeys = tl.getFiles(mode);
        box.setItems(fileKeys);
        box.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a){
             activeFile = box.getSelected();
            } 
         });
        
    }
   
 


}
