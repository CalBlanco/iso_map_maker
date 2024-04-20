package com.isomapmaker.game;

import java.util.Collections;
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

import com.isomapmaker.game.controls.AssetPlacer;
import com.isomapmaker.game.controls.CamController;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.controls.AtlasBrowser.AtlasBrowser;
import com.isomapmaker.game.map.Atlas.TileAtlas;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;

import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.TileMaps.TileMapManager;
import com.isomapmaker.game.util.CursorSetter;
import com.isomapmaker.game.util.IsoUtil;
import com.isomapmaker.game.util.MapSaver;
import com.isomapmaker.game.util.XmlParse;



/**
 * Main Map making Game class 
 * Contains the outermose game code and orginization
 */
public class IsoMapMaker extends Game {
	

	SpriteBatch batch; // Main drawing batch
	SpriteBatch hudBatch; // HUD / UI drawing batch 

	OrthographicCamera cam; // Main game camera 
	

	InputMultiplexer ip; // Main game input multiplexer 


	AssetPlacer assetPlacer; // Class that handles placing assets into tiles 

	CamController cameraController; // Class that handles moving and zooming with the camera 

	TileMap map; // Reference for the TileMap 

	TileMapManager tileMapManager; // Manages tile maps for us / manges layers of tile maps 


	
	AtlasBrowser atlasBrowser; // Main UI code 



	/**
	 * Initialize all of our variables and things we need before game setup
	 */
	@Override
	public void create () {
		System.out.println("Starting Map Maker");
		
		

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // main camera

		tileMapManager = TileMapManager.getInstance(); // Manages tilemaps for multiple layers 

		
		batch = new SpriteBatch(); // Create the sprite batches
		hudBatch = new SpriteBatch();

		cameraController = new CamController(cam, 0.05f, 7f, 5f); // camera controls 
		assetPlacer = new AssetPlacer(cam, tileMapManager); // Asset placer that manages the map using the tile loader

		
		atlasBrowser = new AtlasBrowser(assetPlacer); // Give the reference to give us mouse information to the UI

		//Controls input flow (placement determines order of input [i believe last added is first])
		ip = new InputMultiplexer();

		ip.addProcessor(atlasBrowser); // UI first 
		ip.addProcessor(cameraController); // Camera controls 
		ip.addProcessor(assetPlacer); // Map placement input 
		
		Gdx.input.setInputProcessor(ip); 

		assetPlacer.setAtlasBrowser(atlasBrowser); // need this to update the ui idk libgx is confusing and annoying



	}

	@Override
	public void render () {

	

		assetPlacer.setState(ModeController.getInstance().getState());
		ScreenUtils.clear(0, 0, 0, 1);
		this.batch.setProjectionMatrix(cam.combined);
		cam.update();
		
		

		


		batch.begin(); // map batch
		batch.setColor(1f,1f,1f,1f);
		TileMapManager.getInstance().render(batch); // render the map
		//batch.draw(TileAtlas.getInstance().getAssetsByType(TileType.Object).getRegion("Containers(Dumpster)", "dmpsterpink_open-bottom"), 0,0);
		assetPlacer.activeTileRender(batch);
		cameraController.render(batch); // apply and camera movements
		
		batch.end();

		hudBatch.begin(); // don't think this gets used at all anymore
		//mh.render(hudBatch, cameraController, ml);
		
		hudBatch.end(); 

		//assetControler.render(); // render our ui last so it is always on top
		atlasBrowser.render();
		
	}
	
	

	/**
	 * Delete our TextureRegions (need to add in code to dispose of pixmaps as well)
	 */
	@Override
	public void dispose () {
		batch.dispose();
		hudBatch.dispose();
		TileAtlas.getInstance().dispose();
		CursorSetter.getInstance().dispose();
	}

	/**
	 * Iterate over all contained assets and place them down on the map in a some what organized manner 
	 */
	public void placeAllAssets(){
		int lastSize = 0; // Keep track of how many x tiles we have taken up 
		Vector<String> names = TileAtlas.getInstance().getAssetsByType(TileType.Floor).keys();

		// Floors
		for(int i=0; i<names.size(); i++){
			
			Vector<String> assets = TileAtlas.getInstance().getAssetsByType(TileType.Floor).getRegionNames(names.get(i));
			
			for(int j=0; j<assets.size(); j++){
				
				try{
					TileMapManager.getInstance().getLayer(0).setFloor(i, j, TileAtlas.getInstance().get(TileType.Floor, names.get(i), assets.get(j)));
				}
				catch(Exception e){
					//e.printStackTrace();
				}
			}
		}

		lastSize = names.size();
		
		// Walls 
		names = TileAtlas.getInstance().getAssetsByType(TileType.Wall).keys();
		Collections.sort(names);
		for(int i=0; i<names.size(); i++){
			Vector<String> assets = TileAtlas.getInstance().getAssetsByType(TileType.Wall).getRegionNames(names.get(i));
			for(int j=0; j<assets.size(); j++){
				WallQuadrant quad = WallQuadrant.valueOf(assets.get(j).split("-")[1]);
				try{
				TileMapManager.getInstance().getLayer(0).setWall(i+lastSize, j, quad, TileAtlas.getInstance().get(TileType.Wall, names.get(i), assets.get(j)));
				}
				catch(Exception e){}
			}
		}

		lastSize = lastSize + names.size();

		names = TileAtlas.getInstance().getAssetsByType(TileType.Object).keys();
		Collections.sort(names);
		
		// Objects 
		for(int i=0; i<names.size(); i++){
			Vector<String> assets = TileAtlas.getInstance().getAssetsByType(TileType.Object).getRegionNames(names.get(i));
			for(int j=0; j<assets.size(); j++){
				
				try{
				TileMapManager.getInstance().getLayer(0).setObject(i+lastSize, j,TileAtlas.getInstance().get(TileType.Object, names.get(i), assets.get(j)));
				}
				catch(Exception e){}
			}
		}
	}
}
