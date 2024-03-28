package com.isomapmaker.game;

import java.util.Vector;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isomapmaker.game.controls.AssetController;
import com.isomapmaker.game.controls.AssetPlacer;
import com.isomapmaker.game.controls.CamController;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.TileMaps.TileMapManager;
import com.isomapmaker.game.map.Tiles.Floor;
import com.isomapmaker.game.map.Tiles.Wall;
import com.isomapmaker.game.util.IsoUtil;




public class IsoMapMaker extends Game {
	final static String[][][] StartMap = {{ {"Pattern_1:1"},{"Grass_1:1"},{"Grass_1:1"},{"Grass_1:1"},{"Grass_1:8"}, {"Dry_1:1"}},{{"Grass_0:1"}, {"Grass_1:3"},{"Grass_0:1"},{"Grass_1:1"},{"Grass_1:1"}}};
	final static String[][][] emptyMap = {{{}}};
	SpriteBatch batch;
	SpriteBatch hudBatch;

	OrthographicCamera cam;
	

	InputMultiplexer ip;

	//MapHud mh;
	

	TileLoader tileLoader;
	AssetController assetControler;
	AssetPlacer assetPlacer;

	CamController cameraController;
	TileMap tm;
	TileMapManager tileMapManager;

	@Override
	public void create () {
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		tileLoader = new TileLoader("assets.xml");
		tileMapManager = new TileMapManager(tileLoader);

		
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();

		cameraController = new CamController(cam, 2f, 5f, 5f);
		assetControler = new AssetController(tileLoader);
		assetPlacer = new AssetPlacer(cam, assetControler, tileMapManager, tileLoader);

		
		
		ip = new InputMultiplexer();

		ip.addProcessor(assetControler);
		ip.addProcessor(cameraController);
		ip.addProcessor(assetPlacer);
		Gdx.input.setInputProcessor(ip);

		
		String[] keys = tileLoader.getFloors();
		for(int i=0; i< keys.length; i++){
			Vector<Floor> fr = tileLoader.floors.get(keys[i]);
			for(int j=0; j<fr.size(); j++){
				tileMapManager.getLayer(0).setFloor(i, j, fr.get(j));
			}
		}

		/* String[] wallKeys = tileLoader.getWalls();
		for(int i=0; i< wallKeys.length; i++){
			Vector<Wall> fr = tileLoader.walls.get(wallKeys[i]);
			for(int j=0; j<fr.size(); j++){
				tm.setWall(j, i, i+2,fr.get(j));
			}
		} */
		
		

	
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		this.batch.setProjectionMatrix(cam.combined);
		cam.update();
		

		
		batch.begin(); // map batch
		tileMapManager.render(batch);
		cameraController.render(batch);
		assetPlacer.activeTileRender(batch);
		batch.end();

		hudBatch.begin();
		//mh.render(hudBatch, cameraController, ml);
		assetPlacer.renderSelectionTiles(hudBatch);
		hudBatch.end();

		assetControler.render();
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		hudBatch.dispose();
		

		
	}
}
