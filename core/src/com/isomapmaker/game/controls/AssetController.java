package com.isomapmaker.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.isomapmaker.game.map.TileMaps.TileLoader;

// Controll the TileLoader class 
// Allow user to navigate between floors, walls and objects
// Within those allow user to navigate different textures
// and within those display the "currently active" TextureRegion as well as the TextureRegion above, and below 
// want to be able to output what ever we are using to our map building controller 
public class AssetController extends Stage {
    
    Skin skin;
    TileLoader tl;
    Table root;
    

    public AssetController(TileLoader tl){
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.tl = tl;
        
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
        middle.add(middlePanel).top();

        



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
        root.debugAll();
        

    }

    public void render(){
        
        
        super.act();
        super.draw();
       
        
    }

    public void dispose(){
        super.dispose();
    }

 


}
