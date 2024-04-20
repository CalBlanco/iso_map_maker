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
import com.isomapmaker.game.util.IsoUtil;
import com.isomapmaker.game.util.MapSaver;
import com.isomapmaker.game.util.XmlParse;




public class IsoMapMaker extends Game {
	
	final static String[] startMap = new String[]{"Grass_1-top_0:right_1:left_2:bottom_3:-,Grass_1-e::e:e-e", "Grass_1-e-e"};

	SpriteBatch batch;
	SpriteBatch hudBatch;

	OrthographicCamera cam;
	

	InputMultiplexer ip;



	//MapHud mh;
	
	AssetPlacer assetPlacer;

	CamController cameraController;
	TileMap map;
	TileMapManager tileMapManager;

	// Atlas Refactor
	AtlasBrowser atlasBrowser;



	@Override
	public void create () {
		System.out.println("Starting Map Maker");
		
		

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // main camera

		tileMapManager = TileMapManager.getInstance(); // Manages tilemaps for multiple layers 

		// Sprite batches to render what we need
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();

		cameraController = new CamController(cam, 0.05f, 5f, 5f); // camera controls 
		assetPlacer = new AssetPlacer(cam, tileMapManager); // Asset placer that manages the map using the tile loader

		
		atlasBrowser = new AtlasBrowser(assetPlacer);

		//Controls input flow (placement determines order of input [i believe last added is first])
		ip = new InputMultiplexer();

		ip.addProcessor(atlasBrowser);
		ip.addProcessor(cameraController);
		ip.addProcessor(assetPlacer);
		
		Gdx.input.setInputProcessor(ip);

		assetPlacer.setAtlasBrowser(atlasBrowser);

		int lastSize = 0;
		Vector<String> names = TileAtlas.getInstance().getAssetsByType(TileType.Floor).keys();
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

	@Override
	public void render () {

	
		

		assetPlacer.setState(ModeController.getInstance().getState());
		ScreenUtils.clear(0, 0, 0, 1);
		this.batch.setProjectionMatrix(cam.combined);
		cam.update();
		
		

		


		batch.begin(); // map batch
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
	
	

	@Override
	public void dispose () {
		batch.dispose();
		hudBatch.dispose();
		

		
	}
}
