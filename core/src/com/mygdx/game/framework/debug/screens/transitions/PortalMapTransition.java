package com.mygdx.game.framework.debug.screens.transitions;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.managers.GameManagerAITimer;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.GameScreen;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;



public class PortalMapTransition  extends GameScreen {

    private Viewport viewport;
    private Stage stage;
    private Game game;
    private String mapWorld, mapLevel;
    private ShapeRenderer shapeRenderer;
    private float progress;
    private GameManagerAITimer delayOurBetweenLoadingScreen;

    private GameManagerAssets gameManagerAssetsInstance;

    public PortalMapTransition(Game game, String w, String l, GameManagerAssets instance) {
        this.game = game;
        this.mapWorld = w;
        this.mapLevel = l;
        this.gameManagerAssetsInstance = instance;

        this.progress = 0f;

        delayOurBetweenLoadingScreen = new GameManagerAITimer();
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((NameGame) game).batch);
    }


    public void update(float dt) {

        delayOurBetweenLoadingScreen.updateCurrentTime(dt);
        //System.out.println("delayOurBetweenLoadingScreen: " + delayOurBetweenLoadingScreen.getCurrentTime());
        progress = MathUtils.lerp(progress, GameUtility.loadCompleted(), .1f); //Utility.assetManager.getProgress(), .1f);

        //System.out.println(progress + " " + GameUtility.loadCompleted() ); //Utility.assetManager.getProgress());



        if(delayOurBetweenLoadingScreen.getCurrentTime() > 0.4f && GameUtility.loadCompleted() == 1 ) {
            game.setScreen(new PlayScreen((NameGame) game, gameManagerAssetsInstance.getCurrentWorld()
                    , gameManagerAssetsInstance.getCurrentLevel(), gameManagerAssetsInstance ));
            dispose();
        }
        /*
            // the loading bare is so fast that we just see a blip on the screen!!!!
            if (GameUtility.assetManager.update() && progress <= GameUtility.loadCompleted() - 0.01f ) { //&& delayOurBetweenLoadingScreen.getCurrentTime() > 0.39f ) {
                System.out.println("new screen should load!!!");
                game.setScreen(new PlayScreen((NameGame) game, GameManagerAssets.instance.getCurrentLevel()
                        , GameManagerAssets.instance.getCurrentLevel()));
                dispose();
            }
        */
    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
/*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(32,  this.viewport.getScreenHeight() / 2 - 6, this.viewport.getScreenWidth() - 64, 16);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(32,  this.viewport.getScreenHeight() / 2 - 6, progress * (this.viewport.getScreenWidth() - 64), 16);
        shapeRenderer.end();
*/

        // Now we can us this as a LoadingScreen and TransitionScreen Effects ?
//      game.setScreen(new MainGameScreen((BubbleBubble3) game, this.level));


        //game.setScreen(new MainGameScreen((BubbleBubble3) game, GameManager.instance.getCurrentLevel() ));
        //dispose();


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        //Gdx.input.setInputProcessor(null);
        //dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
