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

public class LoadSaveGameScreen extends GameScreen{


    private NameGame gameName;
    private TextButton buttonNewGame, buttonPlayLoadFromFile, buttonExit, buttonSettings;

    private Stage stage;
    private Skin skin;

    private ShapeRenderer shapeRenderer;
    private ParallaxGameScreen parallaxGameScreen;

    private GameManagerAssets gameManagerAssetsInstance;

    private String level;
    public LoadSaveGameScreen(NameGame game, String l, GameManagerAssets instance) {
        this.gameName = game;
        this.level = l;
        this.gameManagerAssetsInstance = instance;

        this.stage = new Stage(new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT )); //, app.camera));
        this.shapeRenderer = new ShapeRenderer();
        // Parallax testing
        parallaxGameScreen = new ParallaxGameScreen(this.gameName);
    }

    @Override
    public void show() {
        System.out.println("MAIN MENU");
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
        parallaxGameScreen.render(delta);

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
    }

    /**
     * BackGround (png) Image of Game and Game Image Name!!
     */
    private void initScreenVisualObjects() {



    }

    private void initButtons() {
        buttonNewGame = new TextButton("Remove Save", skin, "default");
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

                        //ToDo : save game from file to be loaded!!

                        gameManagerAssetsInstance.removeSaveGameFilePlayer();
                        gameManagerAssetsInstance.clearSaveGameObjectPlayer();

                        gameManagerAssetsInstance.removeSaveGameFileWorld();
                        gameManagerAssetsInstance.clearSaveGameObjectWorld();

                        System.out.println("LoadSaveGameScreen Class updateAllSpawnLifeFromEnemy button delete for now!! updateAllSpawnLifeFromEnemy");
                        gameName.setScreen( new MainMenuScreen((NameGame)gameName,
                                gameManagerAssetsInstance.getCurrentWorld(), gameManagerAssetsInstance ));
                    }
                };
                stage.addAction( parallel(fadeOut(.8f), delay(2f, run(transitionRunnable)) ));
            }
        });

        if(gameManagerAssetsInstance.gameManagerSaveFilePlayerExists()) {
            buttonNewGame.setVisible(true);//.setDisabled(true);
        }else{
            buttonNewGame.setVisible(false);//.setDisabled(true);
        }

        buttonExit = new TextButton("Back to Menu", skin, "default");
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

                        gameName.setScreen( new MainMenuScreen((NameGame)gameName,
                                gameManagerAssetsInstance.getCurrentWorld(), gameManagerAssetsInstance ));
                    }
                };
                stage.addAction( parallel(fadeOut(.8f), delay(2f, run(transitionRunnable)) ));

            }
        });

        stage.addActor(buttonNewGame);
        //stage.addActor(buttonPlayLoadFromFile);
        //stage.addActor(buttonSettings);
        stage.addActor(buttonExit);
    }
}

