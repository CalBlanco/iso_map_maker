package com.isomapmaker.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isomapmaker.game.controls.AssetController;
import com.isomapmaker.game.controls.CamController;
import com.isomapmaker.game.controls.MenuController;
import com.isomapmaker.game.map.AssetLoader;
import com.isomapmaker.game.map.LayerManager;
import com.isomapmaker.game.map.TileLayer;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;




public class IsoMapMaker extends Game {
	final static String[][][] StartMap = {{ {"Pattern_1:1"},{"Grass_1:1"},{"Grass_1:1"},{"Grass_1:1"},{"Grass_1:8"}, {"Dry_1:1"}},{{"Grass_0:1"}, {"Grass_1:3"},{"Grass_0:1"},{"Grass_1:1"},{"Grass_1:1"}}};
	final static String[][][] emptyMap = {{{}}};
	SpriteBatch batch;
	SpriteBatch hudBatch;

	OrthographicCamera cam;
	

	InputMultiplexer ip;

	//MapHud mh;
	

	TileLoader tl;
	AssetController aCont;

	TileMap tm;

	@Override
	public void create () {
		
		tl = new TileLoader("assets.xml");
		aCont = new AssetController(tl);

		tm = new TileMap(30, new Vector2(0,0));
		batch = new SpriteBatch();
		
		//mh = new MapHud(assets);
		hudBatch = new SpriteBatch();

		

	

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		

		ip = new InputMultiplexer();
		ip.addProcessor(aCont);
		Gdx.input.setInputProcessor(ip);

		tm.setFloor(0, 0, tl.getFloor("Dirt", 0));
		tm.setWall(0,0,0, tl.getWall("EW Walls", 0));
		tm.setWall(0,0,1, tl.getWall("NS Walls", 0));
		

	
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		this.batch.setProjectionMatrix(cam.combined);
		cam.update();
		

		
		batch.begin(); // map batch
		tm.render(batch);
		
		batch.end();

		hudBatch.begin();
		//mh.render(hudBatch, ccont, ml);
		
		hudBatch.end();

		aCont.render();
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		hudBatch.dispose();
		

		
	}
}
