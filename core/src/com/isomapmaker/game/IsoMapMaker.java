package com.isomapmaker.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isomapmaker.game.controls.CamController;
import com.isomapmaker.game.map.AssetLoader;
import com.isomapmaker.game.map.MapLoader;
import com.isomapmaker.game.ui.MapMakerUI;



public class IsoMapMaker extends Game {
	final static String[][] StartMap = {{"Pattern_1:1","Grass_1:1","Grass_1:1","Grass_1:1","Grass_1:8", "Dry_1:1"},{"Grass_0:1", "Grass_1:3","Grass_0:1","Grass_1:1","Grass_1:1"}};

	SpriteBatch batch;
	AssetLoader assets;
	TextureRegion tr;
	MapMakerUI mui;
	MapLoader ml;

	OrthographicCamera cam;
	CamController ccont;

	@Override
	public void create () {
		assets = new AssetLoader();
		batch = new SpriteBatch();
		tr = assets.loadTextureRegion("Thick_72x100_23", 5);
		
		ml = new MapLoader(StartMap, assets);
		ml.printMap();

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ccont = new CamController(cam, 0.05f, 2f, 2f);
		Gdx.input.setInputProcessor(ccont);

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

		batch.begin();
		
		ml.render(batch);
		ccont.render(batch);
		//batch.draw(tr, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assets.dispose();
		mui.dispose();
	}
}
