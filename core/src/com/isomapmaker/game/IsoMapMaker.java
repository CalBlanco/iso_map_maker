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
import com.isomapmaker.game.controls.MenuController;
import com.isomapmaker.game.map.AssetLoader;
import com.isomapmaker.game.map.LayerManager;
import com.isomapmaker.game.map.TileLayer;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
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
	

	TileLoader tl;
	AssetController aCont;
	AssetPlacer ap;

	CamController ccont;
	TileMap tm;

	@Override
	public void create () {
		
		tl = new TileLoader("assets.xml");
		aCont = new AssetController(tl);
		
		tm = new TileMap(400, new Vector2(0,0));
		batch = new SpriteBatch();
		
		//mh = new MapHud(assets);
		hudBatch = new SpriteBatch();

		

		

	

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ccont = new CamController(cam, 5f, 5f, 2f);
		ap = new AssetPlacer(cam, aCont, tm);
		ip = new InputMultiplexer();

		ip.addProcessor(aCont);
		ip.addProcessor(ccont);
		ip.addProcessor(ap);
		Gdx.input.setInputProcessor(ip);

		
		String[] keys = tl.getFloors();
		for(int i=0; i< keys.length; i++){
			Vector<Floor> fr = tl.floors.get(keys[i]);
			for(int j=0; j<fr.size(); j++){
				tm.setFloor(i, j, fr.get(j));
			}
		}

		/* String[] wallKeys = tl.getWalls();
		for(int i=0; i< wallKeys.length; i++){
			Vector<Wall> fr = tl.walls.get(wallKeys[i]);
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
		tm.render(batch);
		ccont.render(batch);
		
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
