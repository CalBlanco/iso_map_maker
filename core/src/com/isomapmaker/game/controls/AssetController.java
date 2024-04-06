package com.isomapmaker.game.controls;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import com.isomapmaker.game.controls.UiComponents.PaintPanel;
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
    
    PlacementModes mode = PlacementModes.Floor;
    String activeFile = "Highlights";

    int fullySmart = 0;

    public Floor f;
    public Wall w;
    public Object o;
    

    public AssetController(TileLoader tl){
        skin = new Skin(Gdx.files.internal("skins/uiskin/uiskin.json"));
        this.tl = tl;
        mode = PlacementModes.Floor;
        activeFile = "Highlights";
        // initialize our root table and add it to the scene 
        root = new Table();
        root.setFillParent(true);
        this.addActor(root); 

        // 3 panels

        //Left Panel
        Table left = new Table();

        Label leftPanel = new Label("Left_Panel", skin);
        
        int[] selections = new int[]{4,0,1};
        

        Table mouseInfo = new Table(skin);
        Label screenPos = new Label("Screen: ()", skin);
        Label tilePos = new Label("Tile: ()", skin);
        Label quadrantInfo = new Label("Quad: ", skin);
        Label hoverTile = new Label("Hovered Tile:", skin);
        Label hoverLayer = new Label("Layer: ", skin);
        
        screenPos.setName("screenPos");
        tilePos.setName("tilePos");
        quadrantInfo.setName("quadrantInfo");
        hoverTile.setName("hoverTile");
        hoverLayer.setName("hoverLayer");

        mouseInfo.add(screenPos).left().expandX().row();
        mouseInfo.add(tilePos).left().expandX().row();
        mouseInfo.add(quadrantInfo).left().expandX().row();
        mouseInfo.add(hoverLayer).left().expandX().row();
        mouseInfo.add(hoverTile).left().expandX().row();

        
        left.row().grow();
        mouseInfo.left().bottom();
        left.add(mouseInfo);


        //Middle Panel
        Table middle = new Table(skin);

        Label middlePanel = new Label("", skin);
        middlePanel.setName("middleHeader");
        middle.add(middlePanel).top();

        // asset browsing 
        Table assetBrowser = new Table(skin);
        Label typeLabel = new Label("Available Tiles ", skin);
        SelectBox<String> typeSelect = new SelectBox<String>(skin);
        typeSelect.setItems(new String[]{"Floor","Wall", "Object"});
        typeSelect.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent e, Actor a){
            switch(typeSelect.getSelected()){
                case "Floor":
                    mode = PlacementModes.Floor;
                    break;
                case "Wall":
                    mode = PlacementModes.Wall;
                    break;
                case "Object":
                    mode = PlacementModes.Object;
                    break;
            }
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

        //tile browser?
        Table tileBrowser = new Table(skin);
        tileBrowser.background("default-pane-noborder");
        tileBrowser.setName("tileBrowserPanel");
        
        middle.add(new Label(" ", skin)).grow().row();
        middle.add(tileBrowser).row();
        middle.add(assetBrowser.background("default-window")).fill().bottom();
        
        

        //Right Panel
        Table right = new Table();
        Label rightPanel = new Label(" ", skin);
        PaintPanel panel = new PaintPanel(skin);
        right.add(rightPanel).top();
        right.add(panel).grow().colspan(1);
        //header
        Table topT = new Table(skin);
        topT.background("default-pane");
        topT.add(new Label("Iso Map Maker", skin, "title-font", Color.WHITE)).expand().center();
        root.add(topT).growX().colspan(12).row();

        // left panel
        root.add(left).grow().colspan(2);
        //middle panel
        root.add(middle).grow().colspan(8);
        // right panel
        root.add(right).grow().colspan(2);
        root.row();

        

        


        // footer space
        root.add(new Label(" ", skin)).colspan(12).row();
        
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
        String[] fileKeys = tl.getFiles(mode.toString());
        if(mode == PlacementModes.Wall) {activeFile = "left"; return;}
        box.setItems(fileKeys);
        box.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a){
             activeFile = box.getSelected();
            } 
         });
        
    }
   
    public void updateTileInfo(Vector2 screenPos, Vector2 tilePos, String tileInfo, String quadrant, int Layer){
        screenPos.set((int)screenPos.x, (int)screenPos.y);
        Label l = root.findActor("screenPos");
        l.setText("Screen: " + screenPos.toString());
        l = root.findActor("tilePos");
        l.setText("Tile: " + tilePos.toString());
        l = root.findActor("hoverTile");
        l.setText(tileInfo);
        l = root.findActor("quadrantInfo");
        l.setText("Quad: " + quadrant);
        l = root.findActor("hoverLayer");
        l.setText("Layer: " + Layer);
    }


    public void updateTileBrowser(TextureRegion[] regions){
        if(regions == null || regions.length < 2){return;}
        Table t = root.findActor("tileBrowserPanel");
        t.clear();

        //left selection
        Table temp = new Table(skin);
        
        Image i = new Image(regions[0]);
        i.setScaling(Scaling.fit);
        temp.add(new Label(" ", skin)).grow().row();
        temp.add(i);
        t.add(temp).left();

        // active selection
        temp = new Table(skin);
        temp.background("default-round");
        i = new Image(regions[1]);
        i.setAlign(Align.center);
        i.setScaling(Scaling.fit);
        temp.add(i).grow();
        t.add(temp).grow();

        // right selection
        temp = new Table(skin);
        i = new Image(regions[2]);
        i.setScaling(Scaling.fit);
        
        temp.add(new Label(" ", skin)).grow().row();
        temp.add(i);
        t.add(temp).right();

    }
 

    



}
