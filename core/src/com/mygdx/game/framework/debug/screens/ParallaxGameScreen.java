package com.mygdx.game.framework.debug.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.framework.debug.screens.parallax.ParallaxBackground;

public class ParallaxGameScreen implements Screen {

    private Stage stage;
    private Game game;
    private OrthographicCamera camera;
    private ParallaxCamera parallaxCamera;

    ParallaxBackground parallaxBackground;

    class ParallaxCamera extends OrthographicCamera {
        Matrix4 parallaxView = new Matrix4();
        Matrix4 parallaxCombined = new Matrix4();
        Vector3 tmp = new Vector3();
        Vector3 tmp2 = new Vector3();

        public ParallaxCamera (float viewportWidth, float viewportHeight) {
            super(viewportWidth, viewportHeight);
        }

        public Matrix4 calculateParallaxMatrix (float parallaxX, float parallaxY) {
            update();
            tmp.set(position);
            tmp.x *= parallaxX;
            tmp.y *= parallaxY;

            parallaxView.setToLookAt(tmp, tmp2.set(tmp).add(direction), up);
            parallaxCombined.set(projection);
            Matrix4.mul(parallaxCombined.val, parallaxView.val);
            return parallaxCombined;
        }
    }

    public ParallaxGameScreen(Game aGame) {
        game = aGame;
        /*
        viewport = new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, gameName.batch);
         */

        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera) stage.getViewport().getCamera();
        //stage = new Stage(new ScreenViewport(new ParallaxCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()) ) );
        //camera = (ParallaxCamera) stage.getViewport().getCamera();



        Array<Texture> textures = new Array<Texture>();
        for(int i = 1; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("parallax/img"+i+".png")));
            //textures.add(new Texture(Gdx.files.internal("parallax/img"+6+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        //ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(1); // default 1 ->
        stage.addActor(parallaxBackground);

    }

    // if player facing

    public void setSpeedAndDirection(int value){this.parallaxBackground.setSpeed(value);}


    public Stage getParallaxStage(){
        return this.stage;
    }

    @Override
    public void show() {
        //Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}
