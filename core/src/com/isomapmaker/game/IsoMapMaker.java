package com.isomapmaker.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isomapmaker.game.controls.CamController;
import com.isomapmaker.game.controls.MenuController;
import com.isomapmaker.game.map.AssetLoader;
import com.isomapmaker.game.map.LayerManager;
import com.isomapmaker.game.map.TileLayer;




public class IsoMapMaker extends Game {
	final static String[][] StartMap = {{"Pattern_1:1","Grass_1:1","Grass_1:1","Grass_1:1","Grass_1:8", "Dry_1:1"},{"Grass_0:1", "Grass_1:3","Grass_0:1","Grass_1:1","Grass_1:1"}};
	final static String[][] emptyMap = {{}};
	SpriteBatch batch;
	AssetLoader assets;
	TextureRegion tr;


	TileLayer groundLayer;
	TileLayer wallLayer;
	TileLayer wallLayer2;

	LayerManager lm;

	OrthographicCamera cam;
	CamController ccont;
	MenuController menu;

	InputMultiplexer ip;

	//MapHud mh;
	SpriteBatch hudBatch;

	@Override
	public void create () {
		assets = new AssetLoader();
		batch = new SpriteBatch();
		tr = assets.loadTextureRegion("Thick_72x100_23", 5);
		
		//mh = new MapHud(assets);
		hudBatch = new SpriteBatch();

		groundLayer = new TileLayer(assets,StartMap);
		groundLayer.layerName = "Ground";
		wallLayer = new TileLayer(assets,emptyMap);
		wallLayer.layerName = "Walls";
		wallLayer2 = new TileLayer(assets, emptyMap, new int[]{1,1});
		wallLayer2.layerName = "Walls 2";

		lm = new LayerManager();
		lm.addLayer(groundLayer);
		lm.addLayer(wallLayer);
		lm.addLayer(wallLayer2);

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ccont = new CamController(cam, 0.05f, 5f, 2f, assets);

		menu = new MenuController(lm, assets, ccont);

		ip = new InputMultiplexer();
		ip.addProcessor(ccont);
		ip.addProcessor(menu);
		Gdx.input.setInputProcessor(ip);

		if(tr==null){
			System.out.println("Not able to get texture region");
		}
		new AssetLoader();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		this.batch.setProjectionMatrix(cam.combined);
		cam.update();

		batch.begin(); // map batch
		lm.render(batch);
		ccont.render(batch);
		batch.end();

		hudBatch.begin();
		//mh.render(hudBatch, ccont, ml);
		menu.render(hudBatch);
		hudBatch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		hudBatch.dispose();
		assets.dispose();

		
	}
}
