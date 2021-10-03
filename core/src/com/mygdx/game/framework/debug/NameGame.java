package com.mygdx.game.framework.debug;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.framework.debug.audio.AudioObserver;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.LoadingScreen;
import com.mygdx.game.framework.debug.screens.MainMenuScreen;
import com.mygdx.game.framework.debug.util.GameUtility;

public class NameGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font24;

	public GameManagerAssets gameManagerAssetsInstance;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		batch = new SpriteBatch();
		initFonts();

		//gameManagerAssetsInstance = new GameManagerAssets();
		gameManagerAssetsInstance = GameManagerAssets.getInstance();
		
		System.out.println("[AssetManager Cleared]");
		GameUtility.assetManager.clear();


		gameManagerAssetsInstance.setCurrentWorld("1");
		gameManagerAssetsInstance.setCurrentLevel("1");
		gameManagerAssetsInstance.setNewCurrentWorld("0");
		gameManagerAssetsInstance.setNewCurrentLevel("0");

		gameManagerAssetsInstance.setOldCurrentWorld("0");
		gameManagerAssetsInstance.setOldCurrentLevel("0");



		gameManagerAssetsInstance.init(); //("1", "1");
		/** Clear's Asset manager for asset's and loads default assets*/
		gameManagerAssetsInstance.loadDefaultGameAssets();

/*
		GameManagerAssets.instance.setCurrentWorld("1");
		GameManagerAssets.instance.setCurrentLevel("1");
		GameManagerAssets.instance.setNewCurrentWorld("0");
		GameManagerAssets.instance.setNewCurrentLevel("0");

		GameManagerAssets.instance.setOldCurrentWorld("0");
		GameManagerAssets.instance.setOldCurrentLevel("0");



		GameManagerAssets.instance.init(); //("1", "1");
		GameManagerAssets.instance.loadDefaultGameAssets();
*/

//ToDo:: Need to change or use !!!
		if( GameUtility.assetManager.update()) {

			if(gameManagerAssetsInstance.isLoadedScreenMenuAsset()) {
				setScreen(new LoadingScreen(this, gameManagerAssetsInstance ));
			}else {
				System.out.println("can't load somethings wrong!!!");
			}
		}

	}

	@Override
	public void dispose(){
		super.dispose();

		// updateAllSpawnLifeFromEnemy on reloading all asset, clear them first!!!
		//GameUtility.assetManager.clear();
		batch.dispose();
		//this.dispose(); // don't know !!
		System.out.println("nameGame is disposed!!");

	}

	@Override
	public void render() {
		super.render();
	}

	private void initFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params.size = 24;
		params.color = Color.BLACK;
		font24 = generator.generateFont(params);
	}
}
