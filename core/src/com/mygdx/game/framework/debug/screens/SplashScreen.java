package com.mygdx.game.framework.debug.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.util.GameUtility;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class SplashScreen extends GameScreen{

    private Stage stage;
    private NameGame gameName;
    private Viewport viewport;

    private Image splashImg_Small;

    private Texture splashImg_texture;

    private GameManagerAssets gameManagerAssetsInstance;


    public SplashScreen(NameGame game, String level, GameManagerAssets instance) {
        this.gameName = game;
        this.gameManagerAssetsInstance = instance;

        viewport = new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        //splashImg_TextureRegion = new TextureRegion( GameUtility.getTextureAsset(""));

        splashImg_texture = GameUtility.getTextureAsset("img/Skull_Logo_Small.png");
        splashImg_Small = new Image(splashImg_texture);

    }

    @Override
    public void show() {
        //System.out.println("Spalsh Screen Showing!");

        Runnable transitionRunnable = new Runnable() {
            @Override
            public void run() {

                gameName.setScreen(new MainMenuScreen((NameGame)gameName,
                        gameManagerAssetsInstance.getCurrentWorld(), gameManagerAssetsInstance ));

            }
        };


        //Texture spalshTexure_Small = GameUtility.assetManager.get("img/Skull_Logo_Small.png", Texture.class);

        //splashImg_Small = new Image(spalshTexure_Small);
        splashImg_Small.setOrigin(splashImg_Small.getImageWidth() / 2, splashImg_Small.getHeight() / 2);
        splashImg_Small.setPosition(GameUtility.V_WIDTH / 2 - 60, GameUtility.V_HEIGHT + 57 );
        splashImg_Small.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
                        moveTo(GameUtility.V_WIDTH / 2 - 60, GameUtility.V_HEIGHT / 2 - 57, 2f, Interpolation.swing)),
                delay(1.5f), fadeOut(3f), run(transitionRunnable)));

        stage.addActor(splashImg_Small);
    }

    @Override
    public void render(float dt) {
        //Gdx.gl.glClearColor(1f, 1f, 1f, 1f); // White
        Gdx.gl.glClearColor(0,0,0,1); // Black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(dt);

        stage.draw();


    }

    public void resize(int width, int height) { stage.getViewport().update(width, height, false); }


    public void update(float dt) {
        stage.act(dt);


    }

    public void hide() {

    }

    public void dispose() {
        stage.dispose();
    }

}
