package com.mygdx.game.framework.debug.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.util.GameUtility;

//import com.mygdx.game.BubbleBubble3;

public class GameOverScreen extends GameScreen {

    private Viewport viewport;
    private Stage stage;

    private Game game;
    private GameManagerAssets gameManagerAssetsInstance;

    public GameOverScreen(Game g, GameManagerAssets instance){
        this.game = g;
        this.gameManagerAssetsInstance = instance;

        viewport = new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((NameGame) game).batch);
        stage = new Stage(new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT ));


        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("Ups... did you fall to far... or let one enemy beat you down!!!", font);
        Label playAgainLabel = new Label("Touch the Screen, and try, try again.....", font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        stage.addActor(table);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.justTouched()) {

            //this we just gone call if the Player have No life left !!


            //game.setScreen(new MainGameScreen((MyGdxGame) game));
            //game.setScreen(new MainMenuScreen((BubbleBubble3) game));
            //game.setScreen(new MainGameScreen((MyGdxGame) game, GameManager.instance.getCurrentWorld()));
            game.setScreen(new MainMenuScreen((NameGame) game, gameManagerAssetsInstance.getCurrentWorld(), gameManagerAssetsInstance));
            dispose();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1); // black
        //Gdx.gl.glClearColor(0, 0, 1, 1); // blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

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
        //stage.clear();
        stage.dispose();
    }

}
