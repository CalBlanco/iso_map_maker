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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
    
    String mode;
    String activeFile = "";

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

        Label middlePanel = new Label("Middle_Panel", skin);
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
            displayMode();
           } 
        });

        assetBrowser.add(typeLabel).pad(10);
        assetBrowser.add(typeSelect);
        
        Table typeBrowser = new Table();
        typeBrowser.setName("typeBrowser");

        Table typeBrowserContainer = new Table();
        typeBrowserContainer.row().grow();
        typeBrowserContainer.add(typeBrowser).expand();
        typeBrowser.row().grow();

        

        typeBrowser.setWidth(getWidth()/12);
        typeBrowser.setHeight(getHeight()/12);

        assetBrowser.row();
        assetBrowser.add(typeBrowserContainer).expandX();
        
        assetBrowser.bottom();



        middle.row().grow();
        middle.add(assetBrowser);


        //Right Panel
        Table right = new Table();
        Label rightPanel = new Label("Right_Panel", skin);

        right.add(rightPanel).top();

        root.add(new Label("Iso Map Maker", skin)).colspan(12).row();
        root.add(left).grow().colspan(2);
        root.add(middle).grow().colspan(8);
        root.add(right).grow().colspan(2);
        root.row();
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

    public void displayMode(){
        Table t = root.findActor("typeBrowser");
        t.clear();
        String[] keys;

        switch(mode){
            case "Wall":
                keys = tl.getWalls();
                break;
            case "Object":
                keys = tl.getObjects();
                break;
            default:
                keys = tl.getFloors();
                break;
        }

        List<String> list = new List(skin);
        list.setItems(keys);
        list.setTypeToSelect(true);
        list.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            activeFile = list.getSelected();
            
            drawTextures();
            }
        });
        t.row();
        t.add(list).pad(20);
        this.setKeyboardFocus(list);

        Table textureViewer = new Table();
        textureViewer.setName("textureView");
        t.add(textureViewer);
        textureViewer.debugAll();
    }

    public void drawTextures(){
        Table textureViewer = root.findActor("textureView");
        textureViewer.clear();
        if(activeFile == "") return;
        System.out.println("Trying to draw regions");
        
        Vector<TextureRegion> regions = tl.getTextureRegions(activeFile, mode);
        Image[] imgs = new Image[regions.size()];
        System.out.println(regions.size());
        List<Image> list = new List<Image>(skin);

        for(int i=0; i<regions.size(); i++){
            imgs[i] = new Image(regions.get(i));
            imgs[i].setScaling(Scaling.fit);
            imgs[i].setScale(1f);
        }
        
        list.setItems(imgs);


        System.out.println("Drew regions");
        textureViewer.bottom();
        
    }

    public void setActiveTile(int i){
        if(mode == "Floor"){
            Floor t = tl.floors.get(activeFile).get(i);
            f = t;
        }
        if(mode == "Wall"){
            Wall t = tl.walls.get(activeFile).get(i);
            w = t;
        }
        if(mode == "Object"){
            Object t = tl.objects.get(activeFile).get(i);
            o = t;
        }
    }
 


}
