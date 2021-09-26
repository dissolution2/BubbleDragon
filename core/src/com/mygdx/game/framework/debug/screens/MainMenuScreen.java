package com.mygdx.game.framework.debug.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.audio.AudioObserver;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.util.GameUtility;



import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class MainMenuScreen extends GameScreen{


    private NameGame gameName;
    private TextButton buttonNewGame, buttonPlayLoadFromFile, buttonExit, buttonSettings;

    private Stage stage;
    private Skin skin;

    private ShapeRenderer shapeRenderer;
    private ParallaxGameScreen parallaxGameScreen;

    private String level;

    private GameManagerAssets gameManagerAssetsInstance;
    public MainMenuScreen(NameGame game, String l, GameManagerAssets instance) {
        this.gameName = game;
        this.gameManagerAssetsInstance = instance;
        this.level = l;


        this.stage = new Stage(new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT )); //, app.camera));
        this.shapeRenderer = new ShapeRenderer();


        /** Move to GUI Main - !! need to be loaded directly from GameUtility or GameAssetManager !! */
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_TITLE);
        notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);

        // Parallax testing
        parallaxGameScreen = new ParallaxGameScreen(this.gameName);
    }

    @Override
    public void show() {
        //System.out.println("MAIN MENU");
        //check();
        //notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);
        Gdx.input.setInputProcessor(stage);
        stage.clear();


        this.skin = new Skin();
        this.skin.addRegions(GameUtility.assetManager.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", gameName.font24);//app.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        initButtons();

    }


    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f); //
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
// TODO : All parallax Screen are background
        gameName.batch.setProjectionMatrix(parallaxGameScreen.getParallaxStage().getCamera().combined);
        //gameName.batch.disableBlending();

        parallaxGameScreen.render(delta);

        //gameName.batch.enableBlending();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();

        System.out.println("MainMenuScreen is disposed!!");
    }

    /**
     * BackGround (png) Image of Game and Game Image Name!!
     */
    private void initScreenVisualObjects() {



    }

    private void initButtons() {
        buttonNewGame = new TextButton("Play", skin, "default");
        //buttonNewGame.setPosition(110, 260);
        buttonNewGame.setPosition(GameUtility.V_WIDTH / 2 - 140, GameUtility.V_HEIGHT / 2 + 100);

        buttonNewGame.setSize(280, 60);
        buttonNewGame.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonNewGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //MainMenuScreen.this.notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);

                Runnable transitionRunnable = new Runnable() {
                    @Override
                    public void run() {

                        //gameName.setScreen(new PlayScreen(gameName, 1));
                        if(gameManagerAssetsInstance.gameManagerSaveFilePlayerExists()){
                            /*
                            System.out.println("MainMenu Class: playButton pressed");
                            System.out.println("MainMenu Class: world: " + GameManagerAssets.instance.getCurrentWorld() );
                            System.out.println("MainMenu Class: level: " + GameManagerAssets.instance.getCurrentLevel() );
                            System.out.println("MainMenu Class: new world: " + GameManagerAssets.instance.getNewCurrentWorld() );
                            System.out.println("MainMenu Class: new level: " + GameManagerAssets.instance.getNewCurrentLevel() );
                            System.out.println("MainMenu Class: old world: " + GameManagerAssets.instance.getOldCurrentWorld() );
                            System.out.println("MainMenu Class: old level: " + GameManagerAssets.instance.getOldCurrentLevel() );
                            System.out.println("MainMenu Class GameSave Exists print saveGame");
                            GameManagerAssets.instance.readFromSaveGamePlayer();
                            */
                            //GameManagerAssets.instance.loadMapFirstWorld();

/** - Removed this not sure */ //ToDo:: check if needed
                            gameManagerAssetsInstance.unloadMapWorldAndLevel(gameManagerAssetsInstance.getCurrentWorld(),
                                    gameManagerAssetsInstance.getCurrentLevel()
                                    );

                            //System.out.println("Unload level NewCurrentWorld and NewCurrentLevel");
                            //System.out.println("load level from saveGame");

                            gameManagerAssetsInstance.loadMapNewWorld(gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointWorld(),
                                    gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointLevel()
                                    );

                            gameManagerAssetsInstance.setGameState(GameManagerAssets.GameState.INITIATING_NEW_GAME);
                            gameName.setScreen(new PlayScreen((NameGame) gameName,
                                    gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointWorld(),
                                    gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointLevel(), gameManagerAssetsInstance ));

                                    //GameManagerAssets.instance.getCurrentWorld(), GameManagerAssets.instance.getCurrentLevel()));



                        }else {
                            gameManagerAssetsInstance.loadMapFirstWorld();
                            gameManagerAssetsInstance.setGameState(GameManagerAssets.GameState.INITIATING_NEW_GAME);
                            gameName.setScreen(new PlayScreen((NameGame) gameName,
                                    gameManagerAssetsInstance.getCurrentWorld(), gameManagerAssetsInstance.getCurrentLevel(),
                                    gameManagerAssetsInstance));
                        }
                        /*
                        if(!GameManagerAssets.instance.gameManagerSaveFilePlayerExists()){
                            System.out.println("MainMenuScreen SaveGame Don't Exists " + GameManagerAssets.instance.getCurrentWorld());

                            // getCurrentWold == null at some point !!???
                            if( !"1".equals( GameManagerAssets.instance.getCurrentWorld() ) ) {

                                GameManagerAssets.instance.loadMapFirstWorld();

                                GameManagerAssets.instance.setGameState(GameManagerAssets.GameState.INITIATING_NEW_GAME);
                                gameName.setScreen(new PlayScreen((NameGame) gameName,
                                        GameManagerAssets.instance.getCurrentWorld(), GameManagerAssets.instance.getCurrentLevel()));
                            }else {
                                GameManagerAssets.instance.setGameState(GameManagerAssets.GameState.INITIATING_NEW_GAME);
                                gameName.setScreen(new PlayScreen((NameGame) gameName,
                                        GameManagerAssets.instance.getCurrentWorld(), GameManagerAssets.instance.getCurrentLevel()));
                            }


                        }else {
                            GameManagerAssets.instance.setGameState(GameManagerAssets.GameState.INITIATING_NEW_GAME);
                            gameName.setScreen(new PlayScreen((NameGame) gameName,
                                    GameManagerAssets.instance.getCurrentWorld(), GameManagerAssets.instance.getCurrentLevel()));
                        }
                        */
                    }
                };
                stage.addAction( parallel(fadeOut(.8f), delay(2f, run(transitionRunnable)) ));
            }
        });

        buttonPlayLoadFromFile = new TextButton("Save Game", skin, "default");
        //buttonNewGame.setPosition(110, 260);
        buttonPlayLoadFromFile.setPosition(GameUtility.V_WIDTH / 2 - 140, GameUtility.V_HEIGHT / 2 + 20);

        buttonPlayLoadFromFile.setSize(280, 60);
        buttonPlayLoadFromFile.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));

        buttonPlayLoadFromFile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //MainMenuScreen.this.notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);

                Runnable transitionRunnable = new Runnable() {
                    @Override
                    public void run() {

                        //gameName.setScreen(new PlayScreen(gameName, 1));

                        gameName.setScreen( new LoadSaveGameScreen((NameGame)gameName,
                                gameManagerAssetsInstance.getCurrentWorld(), gameManagerAssetsInstance ));

                    }
                };
                stage.addAction( parallel(fadeOut(.8f), delay(2f, run(transitionRunnable)) ));
            }
        });

        buttonSettings = new TextButton("Settings", skin, "default");
        //buttonNewGame.setPosition(110, 260);
        buttonSettings.setPosition(GameUtility.V_WIDTH / 2 - 140, GameUtility.V_HEIGHT / 2 - 60);

        buttonSettings.setSize(280, 60);
        buttonSettings.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
/*
        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	//MainMenuScreen.this.notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);

            	Runnable transitionRunnable = new Runnable() {
                    @Override
                    public void run() {

                    	System.out.println("have to implement new Screen Settings and back to Main Menu!!" );
                    }
                };
                stage.addAction( parallel(fadeOut(.8f), delay(2f, run(transitionRunnable)) ));
            }
        });
*/
        buttonExit = new TextButton("Exit", skin, "default");
        //buttonExit.setPosition(110, 190);
        buttonExit.setPosition(GameUtility.V_WIDTH / 2 - 140, GameUtility.V_HEIGHT / 2 - 150 );
        buttonExit.setSize(280, 60);
        buttonExit.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Runnable transitionRunnable = new Runnable() {
                    @Override
                    public void run() {

                        Gdx.app.exit(); //Close app
                        //System.exit(0);
                        System.exit(-1); // -1/0 force close app in phone task manager - because if run/pause menu/ resume / pause/menu/exit and then run again - the game error's
                    }
                };
                stage.addAction( parallel(fadeOut(.8f), delay(2f, run(transitionRunnable)) ));

            }
        });

        stage.addActor(buttonNewGame);
        stage.addActor(buttonPlayLoadFromFile);
        stage.addActor(buttonSettings);
        stage.addActor(buttonExit);
    }

}

