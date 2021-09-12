package com.mygdx.game.framework.debug.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.util.GameUtility;

public class LoadingScreen extends GameScreen{

    private NameGame gameName;
    private ShapeRenderer shapeRenderer;
    private float progress;

    private Skin skin;
    private Label labelInfo; // Info label

    private OrthographicCamera camera;
    private Stage stage;

    private int level;

    private BitmapFont font;
    //private Array<String> assetsNamesArrayString;
    //private Boolean assetLoadedInAgain = false;
    //private Array<String> assetLoadedinAgain;


    private GameManagerAssets gameManagerAssetsInstance;

    //Array<String> stringArray;
    public LoadingScreen(NameGame game, GameManagerAssets instance ) {  //, int lev) {
        this.gameName = game;
        this.gameManagerAssetsInstance = instance;
        //this.level = lev;
        //assetLoadedinAgain = new Array<String>();
        font = new BitmapFont();

        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT, camera )); //, app.camera));)

        //camera.setToOrtho(false, Utility.V_WIDTH, Utility.V_HEIGHT); // false !!?
        this.shapeRenderer = new ShapeRenderer();
        this.progress = 0f;
        //assetsNamesArrayString = new Array<String>();
        //assetsNamesArrayString = GameUtility.getAssetNames();

        //queueAssets();
        //initInfoLabel();
        //this.skin = new Skin();
        //this.skin.addRegions(GameUtility.assetManager.get("ui/uiskin.atlas", TextureAtlas.class));
        //this.skin.add("default-font", gameName.font24);
        //this.skin.load(Gdx.files.internal("ui/uiskin.json"));


        //labelInfo = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        //labelInfo.setWrap(true);

        //labelInfo = new Label("Loading inn Game Assets....!!", skin, "default");
        //labelInfo.setPosition(150, 60);
        //labelInfo.getColor().set(Color.WHITE);
        //labelInfo.setAlignment(Align.bottomLeft);
        //labelInfo.addAction(fadeOut(.1f));

        //Table table = new Table();
        //table.debug();
        //Top-Align table
        //table.top();
        //table.left();
        //make the table fill the entire stage
        //table.setFillParent(true);
        //add our labels to our table, padding the top, and giving them all equal width with expandX

        //table.add().size(14);
        //table.add(labelInfo).padTop(10);
        //table.add().size(14);


        //stage.addActor(table);


        //stringArray = new Array<String>();

        //stringArray = GameUtility.assetManager.getAssetNames();
    }

    // TESTING!!!! not good!!!
    // sett in GameManager - Can delete or reuse
    private void queueAssets() {
/*
        GameUtility.assetManager.clear();

        gameManagerAssetsInstance.unloadMapWorldAndLevel(
                gameManagerAssetsInstance.getCurrentWorld(),
                gameManagerAssetsInstance.getCurrentLevel()
        );

        gameManagerAssetsInstance.loadMapWorldAndLevel("1","1");

        if(gameManagerAssetsInstance.isLoadedScreenMenuAsset()){
            //assetLoadedInAgain = true;
            assetLoadedinAgain.add("isLoadedScreenMenuAsset");
        }
        gameManagerAssetsInstance.isLoadedGameUIAssets();

        if(gameManagerAssetsInstance.isLoadedSplashScreenAsset()) {
            //assetLoadedInAgain = true;
            assetLoadedinAgain.add("LoadedSplashScreenAsset!!");
        }
        gameManagerAssetsInstance.isLoadedGameAssetsGameObjects();
        gameManagerAssetsInstance.isLoadedPauseMenuDrawerAsset();
        gameManagerAssetsInstance.isLoadedJoyStickControllerButtonStyle();

        //gameManagerAssetsInstance.loadingGameAssets();

        System.out.println("queueAssets for loading Done!!");

        //return assetLoadedInAgain;

 */
    }

    @Override
    public void show() {



        Gdx.input.setInputProcessor(stage);
        shapeRenderer.setProjectionMatrix(camera.combined);
        this.progress = 0f;


        //System.out.println("Diagnostics: " + GameUtility.assetManager.getDiagnostics() );

/*
        this.skin = new Skin();
        this.skin.addRegions(GameUtility.assetManager.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", gameName.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));
        //initInfoLabel();
*/

    }

    public void update(float dt) {
        stage.act(dt);



        //labelInfo.setText("testing on text!!");


        //System.out.println("stringArray Size: " + stringArray.size );
    //for(int i = 0; i < stringArray.size; i++){
        //labelInfo.setText( GameUtility.assetManager.getDiagnostics() );//"\n" + stringArray.get(i).toString() + "\n" ) ;//GameUtility.assetManager.getDiagnostics() );
    //}



        // a + (b - a) * lerp
        progress = MathUtils.lerp(progress,  GameUtility.assetManager.getProgress(), .1f);
        //Debug
        //System.out.println("assetManager Progress: " + progress);
        // looks ok now, but might add : && GameUtility.assetManager.isLoaded("ui/uiskin.atlas", TextureAtlas.class
        if( GameUtility.assetManager.update() && progress >= GameUtility.assetManager.getProgress() - .001 ) {

            //System.out.println("Activate splash Screen!!");
            if(gameManagerAssetsInstance.isLoadedSplashScreenAsset()) {
                gameName.setScreen(new SplashScreen((NameGame) gameName,
                        gameManagerAssetsInstance.getCurrentWorld(), gameManagerAssetsInstance ));
            }
        }
    }

    @Override
    public void render(float dt) {
        //Gdx.gl.glClearColor(1f, 1f, 1f, 1f); //
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(dt);

        stage.draw();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(32, this.camera.viewportHeight / 2 - 8, this.camera.viewportWidth - 64, 16);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(32, this.camera.viewportHeight / 2 - 8, progress * (this.camera.viewportWidth - 64), 16);
        shapeRenderer.end();


        // Debug
        /*
        gameName.batch.begin();
        if(assetLoadedinAgain.size > 0) {

            for (int i = 0; i < assetLoadedinAgain.size; i++) {
                font.draw(gameName.batch, assetLoadedinAgain.get(i), 150, i * 40);
                //assetLoadedinAgain.removeIndex(i);
            }
        }
        gameName.batch.end();
        */
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
        shapeRenderer.dispose();
    }


    // Initialize the info label
    private void initInfoLabel() {
        labelInfo = new Label("Loading inn Game Assets....!!", skin, "default");
        labelInfo.setPosition(35, 320);
        labelInfo.setAlignment(Align.bottomLeft);
        //labelInfo.addAction(fadeOut(.1f));
        stage.addActor(labelInfo);
    }
}
