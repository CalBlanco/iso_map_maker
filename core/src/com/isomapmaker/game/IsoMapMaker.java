package com.isomapmaker.game;

import java.util.Vector;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isomapmaker.game.controls.AssetController;
import com.isomapmaker.game.controls.AssetPlacer;
import com.isomapmaker.game.controls.CamController;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.TileMaps.TileMapManager;
import com.isomapmaker.game.map.Tiles.Floor;
import com.isomapmaker.game.map.Tiles.Wall;
import com.isomapmaker.game.util.IsoUtil;
import com.isomapmaker.game.util.MapSaver;




public class IsoMapMaker extends Game {
	
	final static String[] startMap = new String[]{"Grass_1-top_0:right_1:left_2:bottom_3:-,Grass_1-e::e:e-e", "Grass_1-e-e"};

	SpriteBatch batch;
	SpriteBatch hudBatch;

	OrthographicCamera cam;
	

	InputMultiplexer ip;

	//MapHud mh;
	

	TileLoader tileLoader;
	AssetController assetControler;
	AssetPlacer assetPlacer;

	CamController cameraController;
	TileMap map;
	TileMapManager tileMapManager;



	@Override
	public void create () {
		
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // main camera

		tileLoader = new TileLoader("assets.xml"); // Loader is responsible for getting assets into the game
		tileMapManager = new TileMapManager(tileLoader, 300); // Manages tilemaps for multiple layers 

		// Sprite batches to render what we need
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();

		cameraController = new CamController(cam, 2f, 5f, 5f); // camera controls 
		assetControler = new AssetController(tileLoader); // UI for assets 
		assetPlacer = new AssetPlacer(cam, assetControler, tileMapManager, tileLoader); // Asset placer that manages the map using the tile loader

		
		//Controls input flow (placement determines order of input [i believe last added is first])
		ip = new InputMultiplexer();

		ip.addProcessor(assetControler);
		ip.addProcessor(cameraController);
		ip.addProcessor(assetPlacer);
		Gdx.input.setInputProcessor(ip);

		// places down all floor tiles that we have
		String[] keys = tileLoader.getFloors();
		for(int i=0; i< keys.length; i++){
			Vector<Floor> fr = tileLoader.floors.get(keys[i]);
			for(int j=0; j<fr.size(); j++){
				tileMapManager.getLayer(0).setFloor(i, j, fr.get(j));
			}
		}


	}

	@Override
	public void render () {
		assetPlacer.setState(ModeController.getInstance().getState());
		ScreenUtils.clear(0, 0, 0, 1);
		this.batch.setProjectionMatrix(cam.combined);
		cam.update();
		
		if(MapSaver.getInstance().getCompletion() > 0f){
			System.out.println((MapSaver.getInstance().getCompletion()*100) + "%");
		}

		
		batch.begin(); // map batch
		tileMapManager.render(batch); // render the map
		cameraController.render(batch); // apply and camera movements
		assetPlacer.activeTileRender(batch); // highlight editing tiles 
		batch.end();

		hudBatch.begin(); // don't think this gets used at all anymore
		//mh.render(hudBatch, cameraController, ml);
		assetPlacer.renderSelectionTiles(hudBatch);
		hudBatch.end(); 

		assetControler.render(); // render our ui last so it is always on top
		
	}
	
	@Override
    public void resize(int width, int height) {
        assetControler.getViewport().update(width, height, true);
    }

	@Override
	public void dispose () {
		batch.dispose();
		hudBatch.dispose();
		

		
	}
}
