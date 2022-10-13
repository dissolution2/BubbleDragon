package com.mygdx.game.framework.debug.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.audio.AudioObserver;
import com.mygdx.game.framework.debug.controllers.Action;
import com.mygdx.game.framework.debug.controllers.Button;
import com.mygdx.game.framework.debug.controllers.ControllerJoyStickButtonStyle;
import com.mygdx.game.framework.debug.controllers.NavigationDrawerScreen;
import com.mygdx.game.framework.debug.hud.PlayerHUD;
import com.mygdx.game.framework.debug.managers.GameManagerAI;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.managers.RayCastManager;
import com.mygdx.game.framework.debug.screens.transitions.PortalMapTransition;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.BossEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.EnemyKnightDevil;
import com.mygdx.game.framework.debug.sprites.Enemies.MovingFallingEnemy.MovingFallingEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.SmallEnemyDef;
import com.mygdx.game.framework.debug.sprites.GraphicsAnimations.AnimationItemDef;
import com.mygdx.game.framework.debug.sprites.items.Portal;
import com.mygdx.game.framework.debug.sprites.items.PortalMapTransitionHidden;
import com.mygdx.game.framework.debug.util.FloatingText;
import com.mygdx.game.framework.debug.world.TravelSpawnPoint;
import com.mygdx.game.framework.debug.world.gameAiObjects.GameAIObject;
import com.mygdx.game.framework.debug.sprites.items.SavePoint;
import com.mygdx.game.framework.debug.util.FPSLogger;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.B2WorldCreator;
//import com.mygdx.game.framework.debug.world.TiledMapReader;
import com.mygdx.game.framework.debug.world.WorldContactListener;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectSwitchHidden;
import com.mygdx.game.framework.debug.world.gameObjects.ItemObjectDef;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;
import com.mygdx.game.framework.debug.world.gameObstacles.ObstacleDoor;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectSwitchDoor;
import com.mygdx.game.framework.debug.world.gamePfxObject.GamePfxObject;

/*
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
*/

import java.util.HashMap;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;

/**Networking*/
/*
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
*/



public class PlayScreen extends GameScreen {

/*
    private Socket socket;
    String id;
    HashMap<String, BubblePlayer> friendlyPlayers;
*/
    protected static final String TAG = null;
    private NameGame gameName;
    private String mapLevel;
    private String mapWorld;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private PlayerHUD hud;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //private TiledMapReader mapReader;
    private float backgroundTime;
    TiledMapRenderer tiledMapRenderer;

    //Box2d variables
    static final float STEEP_TIME = 1f / 60f;
    private float accumulator = 0;
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator worldB2VarsCreate;

    //TEST TODO updateAllSpawnLifeFromEnemy on remove instance of GameManagerAI
    private GameManagerAI gameManagerAI;


    //sprites
    private BubblePlayer bubble;
    private String playerActivePowerInUse;
    private BubblePlayer netWorkPlayerBubble;

    private RayHandler rayHandler;
    private PointLight myLight;
    private PointLight myLightTwo;

    private PointLight mapPointLight_TEST;
    private ConeLight mapConeLight_TEST, mapConeLight_TEST_01, mapConeLight_TEST_02;

    private DirectionalLight mapDirectionalLight;



    private ConeLight myLightCone, myLightCone2, myLightCone3, myLightCone4;

    private DirectionalLight myLightDriection;


    // Controller
    //private ControllerJoyStick controllerJoyStick;

    //private ControllerJoyStickButtonStyle controllerJoyStickButtonStyle;
    //TODO testing adding and removing button in game !!! cant use button_list.remove(i) on a fixed list !!!
    //private List<Button> main_action_bar_buttons_list = Arrays.asList(new Button("attack", 4, new Action()), new Button("heal", 10, new Action()));

    //DrawerNavigationprivate PointLight myLight, myLightTwo;
    //private static final float NAV_WIDTH = 280;//200F;
    //private static final float NAV_HEIGHT = 420;//1920F;
    NavigationDrawerScreen navigationDrawerScreen;
    ControllerJoyStickButtonStyle controllerJoyStickButtonStyle;
    //private List<Button> main_action_bar_buttons_list = Arrays.asList(new Button("heal", 0, new Action()), new Button("attack_one", 1, new Action()), new Button("power", 0, new Action()) );
    private ArrayList<Button> main_action_bar_buttons_list;  // now cool down showing // = new Button("heal", 0, new Action()), new Button("attack_one", 1, new Action()), new Button("power", 0, new Action()) );
    private ArrayList<Button> quick_bar_buttons_list; // long power with cool down showing.
    //private ArrayList<Button> buttons_list;
    private InputMultiplexer inputMultiplexer;

    /*Might put this in a CameraManager*/
    float statTime = 0;
    Vector3 lerpTarget;

    /*Might put this in a LevelManager*/
    int levelWidth = 0;
    int levelHeight = 0;



    private Vector2 startVector2, endVector2;
    private Vector2 vecPoint, vecNormal;
    private float dist;

    Vector2 rayDir = new Vector2();
    Vector2 rayEnd = new Vector2();
    Vector2 center = new Vector2();

    //private AI artificialIntelligence;

    //Debugging Line
    ShapeRenderer shapeRenderer;
    float[] aabb = new float[10];
    float[] angleLinetesting = new float[10];

    Array<Body> bodiesWithinArea = new Array<Body>();

    Array<BubblePlayer> testPlayerAABB = new Array<BubblePlayer>();


    Array<Body> bodyList = new Array<Body>();



    //SteeringEntityBoss aiEntity;
    //SteeringEntityBoss targetEntity;
    //Array<SteeringEntityBoss> arraySterringEntity;

    //B2dSteeringEntity aiEntity;
    //B2dSteeringEntity targetEntity;
    //Array<B2dSteeringEntity> arraySterringEntity;


    // Testing
    RayCastManager raycastManager;
    Vector2 enemyRayPointCenter = new Vector2();
    Vector2 enemyLosDegree40 = new Vector2();
    Vector2 enemyLosDegree25 = new Vector2();
    Vector2 enemyLosDegree0 = new Vector2();

    Vector2 enemyLosDegree75 = new Vector2();
    Vector2	enemyLosDegree90 = new Vector2();
    Vector2	enemyLosDegree125 = new Vector2();

    Vector2 collision = new Vector2(), normal = new Vector2();
    boolean directionFacingRight;




    int numRays = 10;
    float angle = 0;

    Vector2 vertxVectorP1 = new Vector2();
    Vector2 vertxVectorP2 = new Vector2();
    Vector2 vertxVectorP3 = new Vector2();
    Vector2 vertxVectorP4 = new Vector2();

    Vector2 enemyLOSlength = new Vector2(0.5f,0f);
    float lowerX , lowerY , upperX , upperY;


    //Array<Vector2> arrayShapeRenderEnemyPointVector;

    //Vector2 p1 = new Vector2();
    //Vector2 p2 = new Vector2();

    /*
    Draw text on to screen , for now just a updateAllSpawnLifeFromEnemy on gesture
     */
    private BitmapFont font;
    private ParallaxGameScreen parallaxGameScreen;


    /**
     *  Start testing Var's
     */
    GameMapScreen gameMapScreen;

    //Array<ExtraLifeGameItem> spawnExtraGameObjectFromEnemyDead;

    // not sure this should be init with inn update loop
    float startX; // = gamePort.getWorldWidth() / 2; //gamecam.viewportWidth / 2;
    float startY; // = gamePort.getWorldHeight() / 2; //gamecam.viewportHeight / 2;



    /** ParticleEffect testing */

    ParticleEffect effect;
    TextureAtlas note;

    //public ParticleEffect rain_Part_One_Particles = new ParticleEffect();
    //public ParticleEffect rain_Part_Two_Particles = new ParticleEffect();
    //public ParticleEffect rain_Part_Three_Particles = new ParticleEffect();

    //public ParticleEffect particleEffect = new ParticleEffect();


    private GameManagerAssets gameManagerAssetsInstance;

    private PlayScreen playScreen;

    FPSLogger fpsLogger;
    private int testVar;

    private int world_body_count;


    // if implement drawer fails - updateAllSpawnLifeFromEnemy making this super and invoke render etc in gameScreen to updateAllSpawnLifeFromEnemy with stage there!!!
    public PlayScreen(NameGame game, String mapW, String mapL, GameManagerAssets instance) {

/** network testing */
       // Removed the testing
/** network testing */
        this.gameManagerAssetsInstance = instance;

        //this.playerActivePowerInUse = this.gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePlayerPowerCrystalInUse();

        fpsLogger = new FPSLogger();

        //effect = new ParticleEffect();

        //note = new TextureAtlas();
        //note.addRegion("note",new TextureRegion(new Texture("note.png")));

        //effect.load( Gdx.files.internal("particleEffect/musician.p"), note ); // ,"BubbleA"
        //effect.load( Gdx.files.internal("musician.p"), MyGdxGame.textureAtlas);

        //effect.start();
        //effect.setPosition(200f, 150f);
        //effect.setPosition( 100f / 2 / GameUtility.PPM, 100f / 2 / GameUtility.PPM);



        //Gdx.input.setCatchBackKey(true); // deprecated
        Gdx.input.setCatchKey(Input.Keys.BACK, true);


        //System.out.println("PlayScreen Class: player CurrentPower is: " + this.playerActivePowerInUse);
        // tried with if test on power to change default buttons but if test would not work ???


        //ToDo: Might move this to GameManagerAssets as we have default powers 1 and if a save game is active
        //ToDo: the player power might be different - changing it her if test did not work !!! don't know why it failed.

        //ToDo: the problem is with save game active the power can be different from what actual power in use is !!! Visually

        /** Refactoring */
        //ToDo: moving to GAmeManagerAssets class -Test Ongoing!!
        this.main_action_bar_buttons_list = new ArrayList<Button>();
        this.main_action_bar_buttons_list = gameManagerAssetsInstance.getMain_action_bar_game_ui_list();


        main_action_bar_buttons_list.add(new Button("jump", 0, new Action()));
        main_action_bar_buttons_list.add(new Button("attack_one", 5f, new Action())); // 0.5f attack_two // 1.8f
        main_action_bar_buttons_list.add(new Button("power_one", 0, new Action())); // 1.5f
        //main_action_bar_buttons_list.add(new Button("power_two", 1.5f, new Action()) );
        main_action_bar_buttons_list.add(new Button("meny_power", 0, new Action()));
        main_action_bar_buttons_list.add(new Button("power_chgreen", 0, new Action())); // 1.5f

        this.quick_bar_buttons_list = new ArrayList<Button>();
        quick_bar_buttons_list.add(new Button("meny_power", 0, new Action()) ); // 1.8f


        /** font for Buttons Counter How many Bullets left and FSP debug Counter etc*/
        font = new BitmapFont(Gdx.files.internal("fonts/sans_serif_18.fnt"));
        //font = new BitmapFont();  // Default
        font.setColor(Color.YELLOW);





        this.gameName = game;
        this.mapWorld = mapW;
        this.mapLevel = mapL;

        //this.spawnExtraGameObjectFromEnemyDead = new Array<ExtraLifeGameItem>();


        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera(GameUtility.V_WIDTH,GameUtility.V_HEIGHT); // had be for ();
        lerpTarget = new Vector3(); // follow the player up close

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(GameUtility.V_WIDTH / GameUtility.PPM, GameUtility.V_HEIGHT / GameUtility.PPM, gamecam);

        gamePort.setScreenWidth(GameUtility.V_WIDTH);
        gamePort.setScreenHeight(GameUtility.V_HEIGHT);


        //gamePort.setWorldWidth(GameUtility.V_WIDTH);
        //gamePort.setWorldHeight(GameUtility.V_HEIGHT);

        map = gameManagerAssetsInstance.getCurrentMap(); // can just get the current map


        /** OrthogonalTiledMapRenderer(TiledMap map, float unitScale) */
        renderer = new OrthogonalTiledMapRenderer(map, 1  / GameUtility.PPM);

        MapProperties mapProps = map.getProperties();

        levelWidth = mapProps.get("width", Integer.class);
        levelHeight = mapProps.get("height", Integer.class);



        /**changed - we sett camera pos out from player spawn post*/
        //initially set our gamcam to be centered correctly at the start of of map
        //gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);


//********************************************************************

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep


        //if(GameManagerAssets.instance.getCurrentWorld().equals("4")){
        //    world = new World(new Vector2(0, 10), true); // updateAllSpawnLifeFromEnemy wrong side up!!
        //}else{
            world = new World(new Vector2(0, -10), true); // right side up
        //}
        //world = new World(new Vector2(0, 10), true); // updateAllSpawnLifeFromEnemy wrong side up!!
        //world = new World(new Vector2(0, -10), true); // right side up

        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();
        //shapeRenderer = new ShapeRenderer(); // Draw Line From SmallEnemyDef -LOS- Debug


        /** BOX2D LIGHT STUFF BEGIN */
        //RayHandler.setGammaCorrection(true);
        //RayHandler.useDiffuseLight(true);

        //rayHandler = new RayHandler(world);


        //rayHandler.setCulling(true);
        //rayHandler.diffuseBlendFunc.apply();
        //rayHandler.setAmbientLight(0.2f,0.2f,0.2f,1.0f);

        //rayHandler.setAmbientLight(.2f); // Value 0 - 1  with coneLight 0.2
        //rayHandler.setShadows(false);

       // rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 0.5f);

        //rayHandler.setBlurNum(1);


        // should be in AssetManager !!! testing
        //ToDo:: move to AssetManager etc
        //particleEffect.load(Gdx.files.internal("particles/fireflyGreen"), Gdx.files.internal("particles"));


/*
        rain_Part_One_Particles.load(Gdx.files.internal("particles/fireflyYellow"), Gdx.files.internal("particles"));
        rain_Part_One_Particles.setPosition(150f, 200f);
        rain_Part_One_Particles.start();

        rain_Part_Two_Particles.load(Gdx.files.internal("particles/rain3"), Gdx.files.internal("particles"));
        rain_Part_Two_Particles.setPosition(250f, 350f);
        rain_Part_Two_Particles.start();
*/
        //rain_Part_Three_Particles.load(Gdx.files.internal("particles/rain12.pfx"), Gdx.files.internal("particles"));
        //rain_Part_Three_Particles.setPosition(350f, 450f);
        //rain_Part_Three_Particles.start();

        // Here We get All enemy from tiledMap
        worldB2VarsCreate = new B2WorldCreator(this, gameManagerAssetsInstance); //, game, gamecam );
        //playScreen = this; ????

        // Not sure if we need !!!!
        /*
        if(worldB2VarsCreate.getCurrentMapInfoObject().size > 0){

            System.out.println("*** Map info ***");
            System.out.println("Map info World: " + worldB2VarsCreate.getCurrentMapInfoObject().get(0).getMapInfoCurrentWorld() );
            System.out.println("Map info Level: " + worldB2VarsCreate.getCurrentMapInfoObject().get(0).getMapInfoCurrentLevel() );
            System.out.println("Map info Save Point: " + worldB2VarsCreate.getCurrentMapInfoObject().get(0).getMapInfoCurrentSavePoint() );
            System.out.println("*** End ***");
        }
        */


        //ToDo : World and Level pos fix!!!

        /** this sets the spawn pos from the new map or old get it's spawn pos 0 */
        /** We have to keep the old pos from map world  if we die */
        /** We have to use the new pos if we travel a level */


        //System.out.println("PlayScreen be for savePoint & spawnPoint Array currWorld: " + GameManagerAssets.instance.getCurrentWorld() +
        //        " currLevel: " + GameManagerAssets.instance.getCurrentLevel());

        //System.out.println("PlayScreen be for savePoint & spawnPoint Array New currWorld: " + GameManagerAssets.instance.getNewCurrentWorld() +
        //        " New currLevel: " + GameManagerAssets.instance.getNewCurrentLevel());

        //ToDo : have to make this one better array inst with inn for loop
        /** creates the player in our game world */
        if(gameManagerAssetsInstance.gameManagerSaveFilePlayerExists()) {

            /** Debug - lin out */
            gameManagerAssetsInstance.readFromSaveGamePlayer();
            // get fixed string on last named spawn location ?
            // here player died , so integer 1 = life to have.
            if(!gameManagerAssetsInstance.getWorldMapChange() && !gameManagerAssetsInstance.getLevelMapChange() ) { // if false do

                System.out.println("PlayScreen Class Create bubble world Pos from SaveGame!! ");
                //System.out.println("PlayScreen Class maxHitperLife: " + GameManagerAssets.instance.getMaxLifeLostOnHitGameManagerAssets());

                // player died or new load game from menu
                gamecam.position.set(gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointPosition(),0);
// 2second this, - removed (its playScreen)
/** Player Created */
                bubble = new BubblePlayer(world,
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointPosition(),
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife(),
                        gameManagerAssetsInstance.getMaxLifeLostOnHitGameManagerAssets(),
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePlayerPowerCrystalInUse(),
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalGreen(),
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalBlack(),
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalBlue(),
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalRed(),
                        false, false, gameManagerAssetsInstance );
                gameManagerAssetsInstance.setPlayerToAssetManager(this.bubble);


            }else{
                /** WorldMapChange or LevelMapChange are true */

                /** World -MapChange is true */
                if(gameManagerAssetsInstance.getWorldMapChange()) {

                    System.out.println("PlayScreen Class Create bubble newWorld startPos!! ");
                    //System.out.println("PlayScreen Class Create SaveGame -life: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife());
                    //System.out.println("PlayScreen Class maxHitperLife: " + GameManagerAssets.instance.getMaxLifeLostOnHitGameManagerAssets());
                    //System.out.println("PlayScreen Class Create Player Class -life: " + bubble.updatePlayerLifeToHudAndSaveOnExit() ); bubble is not created duuuu
                    // last true set's mapTransfer World with inn Player defineBubble /


                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("DOWN")) {

                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("3")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }

                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("UP")) {


                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("6")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }


                    // travel right -> /
                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("RIGHT UP")) {
                        //System.out.println("PlayScreen Class - We travel right");


                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("1")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }

                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("RIGHT DOWN")) {
                        //System.out.println("PlayScreen Class - We travel right");


                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("2")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }

                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("LEFT UP")){
                        //System.out.println("PlayScreen Class - We travel left");

                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("5")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }

                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("LEFT DOWN")){
                        //System.out.println("PlayScreen Class - We travel left");

                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("4")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }
                    //System.out.println("old World: " + GameManagerAssets.instance.getOldCurrentWorld() );
                    //System.out.println("new World: " + GameManagerAssets.instance.getNewCurrentWorld() );

                    //System.out.println("old Level: " + GameManagerAssets.instance.getOldCurrentLevel() );
                    //System.out.println("new Level: " + GameManagerAssets.instance.getNewCurrentLevel() );
/** Player Created */
                    bubble = new BubblePlayer(world,gameManagerAssetsInstance.getMapSpawnStartPosition(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife(),
                            gameManagerAssetsInstance.getMaxLifeLostOnHitGameManagerAssets(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePlayerPowerCrystalInUse(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalGreen(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalBlack(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalBlue(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalRed(),
                            true, false, gameManagerAssetsInstance );
                    gameManagerAssetsInstance.setPlayerToAssetManager(this.bubble);
                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("LEFT")) {
                        bubble.setPlayerFacingDirection(false);
                    }
                    gameManagerAssetsInstance.setWorldMapChange(false);

                }

                /** Level -MapChange is true */
                if(gameManagerAssetsInstance.getLevelMapChange()) {

                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("DOWN")) {

                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("3")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }

                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("UP")) {


                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("6")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }


                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("RIGHT UP")) {
                        System.out.println("PlayScreen Class - We travel right");


                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if (useSpawnPointForTravelArray.size > 0) {

                            for (TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray) {
                                if (loopForIndex.mapId.equals("1")) {
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }

                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("RIGHT DOWN")) {
                        System.out.println("PlayScreen Class - We travel right");


                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if (useSpawnPointForTravelArray.size > 0) {

                            for (TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray) {
                                if (loopForIndex.mapId.equals("2")) {
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }


                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("LEFT UP")){
                        System.out.println("PlayScreen Class - We travel left");


                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("5")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }


                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("LEFT DOWN")){
                        System.out.println("PlayScreen Class - We travel left");


                        Array<TravelSpawnPoint> useSpawnPointForTravelArray;
                        useSpawnPointForTravelArray = worldB2VarsCreate.getGameTravelSpawnPointObjects();

                        if(useSpawnPointForTravelArray.size > 0) {

                            for(TravelSpawnPoint loopForIndex : useSpawnPointForTravelArray){
                                if(loopForIndex.mapId.equals("4")){
                                    gameManagerAssetsInstance.setMapSpawnStartPosition(
                                            loopForIndex.getSpawnPoint()
                                    );
                                }
                            }
                        }
                    }


                    // last false set's mapTransfer World with inn Player defineBubble /
                    System.out.println("PlayScreen Class Create bubble newLevel startPos!! ");

                    //System.out.println("old World: " + GameManagerAssets.instance.getOldCurrentWorld() );
                    //System.out.println("new World: " + GameManagerAssets.instance.getNewCurrentWorld() );

                    //System.out.println("old Level: " + GameManagerAssets.instance.getOldCurrentLevel() );
                    //System.out.println("new Level: " + GameManagerAssets.instance.getNewCurrentLevel() );

                    gamecam.position.set(gameManagerAssetsInstance.getMapSpawnStartPosition(),0);
/** Player Created */
                    bubble = new BubblePlayer(world,gameManagerAssetsInstance.getMapSpawnStartPosition(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife(),
                            gameManagerAssetsInstance.getMaxLifeLostOnHitGameManagerAssets(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePlayerPowerCrystalInUse(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalGreen(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalBlack(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalBlue(),
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPowerCrystalRed(),
                            false, true, gameManagerAssetsInstance);
                    gameManagerAssetsInstance.setPlayerToAssetManager(this.bubble);
                    if(gameManagerAssetsInstance.getPortalTravelDirection().equals("LEFT UP") || gameManagerAssetsInstance.getPortalTravelDirection().equals("LEFT DOWN")) {
                        bubble.setPlayerFacingDirection(false);
                    }
                    gameManagerAssetsInstance.setLevelMapChange(false);

                }

            }
        }else{
            /** Start of Game - No save Game - should start with low life = 1.*/

            System.out.println("Start of new Game, NameGame(Class) is setting world instance");

            //System.out.println("PlayScreen Class maxHitperLife: " + GameManagerAssets.instance.getMaxLifeLostOnHitGameManagerAssets());
            //System.out.println("PlayScreen Class Create bubble currentWorld startPos!! " + GameManagerAssets.instance.getMapSpawnStartPosition() );

            //GameManagerAssets.instance.getMapSpawnStartPosition()
            //worldB2VarsCreate.getSpawnPointsRight().get(0).positionSavePoint

            // ToDo: temp, but only one SavePoint per level loaded inn so should be good. !!!
            //if(worldB2VarsCreate.getCurrentMapInfoObject().size > 0) {
                //if (worldB2VarsCreate.getCurrentMapInfoObject().get(0).getMapInfoCurrentSavePoint().equals("true")) {
            gameManagerAssetsInstance.setMapSpawnStartPosition(
                            worldB2VarsCreate.getSavePoints().get(0).getPositionSavePoint()
                    );
                //}
            //}

            gamecam.position.set(gameManagerAssetsInstance.getMapSpawnStartPosition(),0);
/** Player Created */
            bubble = new BubblePlayer(world, gameManagerAssetsInstance.getMapSpawnStartPosition(), 1,
                    gameManagerAssetsInstance.getMaxLifeLostOnHitGameManagerAssets(),
                    "1",
                    0,
                    0,
                    0,
                    0,
                    false, false, gameManagerAssetsInstance);
            gameManagerAssetsInstance.setPlayerToAssetManager(this.bubble);
        }

        /** in bubble player init() -this was a virtual method Arraylist on null object ref - testing here !!!*/
        /** -check save game for powers- */
        if( gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPlayerPowerUpList().size() > 0 ){

            for(int a = 0; a < gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPlayerPowerUpList().size(); a++) {

                bubble.setPlayerPowerUp(
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getPlayerPowerUpList().get(a).getPowerName() );
            }


        }
        /** Debug line out */
        //gameManagerAssetsInstance.readFromSaveGamePlayer();


        //create our game HUD for scores/timers/level info
        hud = new PlayerHUD(gameName.batch, bubble.updatePlayerLifeToHudAndSaveOnExit(), gameManagerAssetsInstance ); // Moved after the player creation !!!

        /** Create updateAllSpawnLifeFromEnemy lights */

        //myLight = new PointLight(rayHandler, 100, Color.WHITE, 0.4f, (50 / GameUtility.PPM ), 400 / GameUtility.PPM);
        //myLightTwo = new PointLight(rayHandler, 100, Color.WHITE, 0.4f, (350 / GameUtility.PPM ), 250 / GameUtility.PPM);
/*
        myLight = new PointLight(rayHandler, 50, Color.WHITE, 0.4f, (400 / GameUtility.PPM ), 300 / GameUtility.PPM);
        myLight.setXray(true);
        myLight.setSoftnessLength(2.6f);
        myLight.setDistance(1.4f);
        //myLight.setColor(2,3,4,1);
        myLight.attachToBody(bubble.b2body, 0, 0); // .attachToBody(bubble.b2body,0,0);
*/


        //mapConeLight_TEST = new ConeLight(rayHandler, 100, Color.GOLDENROD, 0.4f, (400 / GameUtility.PPM ), (300 / GameUtility.PPM), 180f,  10f );

        //mapConeLight_TEST.setXray(false);
        //mapConeLight_TEST.setDirection(1f);
        //mapConeLight_TEST.setSoftnessLength(2.6f);
        //mapConeLight_TEST.setDistance(2.4f);

       // if(worldB2VarsCreate.getGameLightObjects().size > 0) {
            //for (int i=0; i < worldB2VarsCreate.getGameLightObjects().size; i++) {

                //mapConeLight_TEST = new ConeLight(rayHandler, 100, Color.DARK_GRAY, 0.4f, worldB2VarsCreate.getGameLightObjects().get(0).b2body.getPosition().x,
                //        worldB2VarsCreate.getGameLightObjects().get(0).b2body.getPosition().y, 180f, 5f);

            //mapConeLight_TEST_01 = new ConeLight(rayHandler, 100, Color.GOLDENROD, 0.4f, worldB2VarsCreate.getGameLightObjects().get(1).b2body.getPosition().x,
            //        worldB2VarsCreate.getGameLightObjects().get(1).b2body.getPosition().y, 180f, 5f);

            //mapConeLight_TEST_02 = new ConeLight(rayHandler, 100, Color.YELLOW, 0.4f, worldB2VarsCreate.getGameLightObjects().get(2).b2body.getPosition().x,
            //        worldB2VarsCreate.getGameLightObjects().get(2).b2body.getPosition().y, 180f, 2f);

                //mapConeLight_TEST.setPosition(gameLight.b2body.getPosition());
                //mapConeLight_TEST.attachToBody(gameLight.b2body,0,0, 180f);
            //}
            //mapConeLight_TEST.setXray(false);

            //mapConeLight_TEST.setSoftnessLength(1.6f);
            //mapConeLight_TEST.setDistance(1.4f);

            //mapConeLight_TEST_01.setXray(false);

            //mapConeLight_TEST_01.setSoftnessLength(3.2f);
            //mapConeLight_TEST_01.setDistance(3.2f);

            //mapConeLight_TEST_02.setXray(false);

            //mapConeLight_TEST_02.setSoftnessLength(3.6f);
            //mapConeLight_TEST_02.setDistance(3.4f);


        //}


/*
      // works
        mapPointLight_TEST = new PointLight(rayHandler, 100, Color.WHITE, 0.4f, (400 / GameUtility.PPM ), 300 / GameUtility.PPM);

        for(GameLightObject gameLight : worldB2VarsCreate.getGameLightObjects()){

            mapPointLight_TEST.setPosition(gameLight.b2body.getPosition());
            //mapPointLight_TEST.attachToBody(gameLight.b2body,0,0);
        }
        //mapPointLight_TEST.setXray(false);
        mapPointLight_TEST.setSoftnessLength(2.6f);
        mapPointLight_TEST.setDistance(2.4f);
*/



        // last pos is wide angle


        //myLightDriection = new DirectionalLight(rayHandler, 100, Color.BLACK, -45f);

        //myLightDriection.setSoftnessLength(0.4f);

/*
        myLightCone = new ConeLight(rayHandler, 100, Color.WHITE, 10.4f, (25 / GameUtility.PPM), (600 / GameUtility.PPM), -45f, 10f );
        myLightCone2 = new ConeLight(rayHandler, 100, Color.WHITE, 10.4f, (50 / GameUtility.PPM), (600 / GameUtility.PPM), -26.4f, 10f );
        //myLightCone3 = new ConeLight(rayHandler, 100, Color.WHITE, 10.4f, (250 / GameUtility.PPM), (600 / GameUtility.PPM), -45f, 10f );
        //myLightCone4 = new ConeLight(rayHandler, 100, Color.WHITE, 10.4f, (375 / GameUtility.PPM), (600 / GameUtility.PPM), -45f, 10f );
        //myLightCone2 = new ConeLight(rayHandler, 100, Color.WHITE, 2.4f, (350 / GameUtility.PPM), (250 / GameUtility.PPM), -90f, 20.2f );

        myLightCone.setSoft(true);
        myLightCone2.setSoft(true);
        //myLightCone3.setSoft(true);
        //myLightCone4.setSoft(true);

        myLightCone.setXray(true);
        myLightCone2.setXray(true);
        //myLightCone3.setXray(true);
        //myLightCone4.setXray(true);
*/

        /**  ///////////// test //////////////// */
        /** ///////////// end test //////////// */
        gameMapScreen = new GameMapScreen(this.gameName, bubble);

        /** Controllers menu etc */
        inputMultiplexer = new InputMultiplexer();
        navigationDrawerScreen = new NavigationDrawerScreen(gameName, 280 , 525, gameManagerAssetsInstance ); // 280 , 420
        /** Old */
        //controllerJoyStickButtonStyle = new ControllerJoyStickButtonStyle( gameName, main_action_bar_buttons_list);
        controllerJoyStickButtonStyle = new ControllerJoyStickButtonStyle( gameName, this.main_action_bar_buttons_list, this.quick_bar_buttons_list, bubble, map);

        /** AI - Manager*/
        gameManagerAI = new GameManagerAI(gameName, bubble, this.worldB2VarsCreate, mapWorld, gameManagerAssetsInstance, world, controllerJoyStickButtonStyle, gamecam, map);


        /** World - Set the contact listener*/
        world.setContactListener(new WorldContactListener(worldB2VarsCreate, gameManagerAssetsInstance, gameManagerAI));


        /** Parallax testing */ //ToDo: Change
        parallaxGameScreen = new ParallaxGameScreen(this.gameName);







//********************************************************************
/*
        gameMapScreen = new GameMapScreen(this.gameName, bubble);


        // Controllers menu etc
        inputMultiplexer = new InputMultiplexer();
        navigationDrawerScreen = new NavigationDrawerScreen(gameName, 280 , 525, gameManagerAssetsInstance ); // 280 , 420
        //controllerJoyStickButtonStyle = new ControllerJoyStickButtonStyle( gameName, main_action_bar_buttons_list);
        controllerJoyStickButtonStyle = new ControllerJoyStickButtonStyle( gameName, this.main_action_bar_buttons_list, this.quick_bar_buttons_list, bubble);
*/
        /** Could be moved to GameAssetManager -Sound Effect
         * Load Call Need to be loaded every time we jump play world/level ore when we die */
        /** Because playScreen  get disposed
         * */
        notify(AudioObserver.AudioCommand.SOUND_LOAD, AudioObserver.AudioTypeEvent.SOUND_JUMP);
        notify(AudioObserver.AudioCommand.SOUND_LOAD, AudioObserver.AudioTypeEvent.SOUND_DROP);

        /** Change To fade out - Volume decrease might work !! */
        notify(AudioObserver.AudioCommand.MUSIC_FADE, AudioObserver.AudioTypeEvent.MUSIC_TITLE);

        /** Move to GUI Main - !! need to be loaded directly from GameUtility or GameAssetManager !! */
        //notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_TITLE);
        //notify(AudioObserver.AudioCommand.MUSIC_PLAY_ONCE, AudioObserver.AudioTypeEvent.MUSIC_TITLE);


        inputMultiplexer.addProcessor(navigationDrawerScreen.getStage() );
        inputMultiplexer.addProcessor(controllerJoyStickButtonStyle.getStage()); // 1. in line // controllerJoyStickButtonStyle

        //inputMultiplexer.addProcessor(controllerJoyStickButtonStyle.getGestureDect() );

        Gdx.input.setInputProcessor(inputMultiplexer);

        System.out.println("PlayScreen State : " + gameManagerAssetsInstance.getGameState() );
        System.out.println("PlayScreen preState : " + gameManagerAssetsInstance.getPreGameState() );


        // only get the same cor as the player - camera follows on playscreen not on hud. etc. !!?? maybe way
        /*
        System.out.println("PlayScreen ViewPort: " +
                gamePort.getCamera().position + " " +
                gamePort.getCamera().viewportHeight + " " +
                gamePort.getCamera().viewportWidth );


        System.out.println("PlayScreen ViewPort ScreenW: " +
                gamePort.getScreenWidth() + " ScreenH: " +
                gamePort.getScreenHeight() + " WorldW: " +
                gamePort.getWorldWidth() + " WorldH: " +
                gamePort.getWorldHeight() );

         */
        world_body_count = world.getBodyCount();
        System.out.println("Start World Body's: " + world_body_count); //world.getBodyCount() );
    }






    @Override
    public void show() {

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK) ) {
             navigationDrawerScreen.setPauseMenuActive().showManually(true);
             //System.out.println("Pause the game is called");
        }
    }

    int soundJumpControllerNormalJump = 0;
    int soundJumpControllerNormalWallJump = 0;
    private void handleInput(float dt) {

        if(!bubble.isDead()) {


            if (Gdx.input.isTouched(0) && controllerJoyStickButtonStyle.getKnobPXLeft() < 0 || controllerJoyStickButtonStyle.getKnobPXLeft() > 0) {
//System.out.println("[Input Debug] isTouched(0)");
                if (controllerJoyStickButtonStyle.getKnobPXLeft() > 0 && bubble.b2body.getLinearVelocity().x <= 1.5f) {

                    bubble.b2body.applyLinearImpulse(new Vector2(0.1f, 0), bubble.b2body.getWorldCenter(), true);
                    bubble.setPlayerControllDirectionRunningRightTrue();

                } else if (controllerJoyStickButtonStyle.getKnobPXLeft() < 0 && bubble.b2body.getLinearVelocity().x >= -1.5f) {
                    bubble.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), bubble.b2body.getWorldCenter(), true);
                    bubble.setPlayerControllDirectionRunningRightFalse();
                }
            }

            if (Gdx.input.isTouched(1) && controllerJoyStickButtonStyle.getKnobPXLeft() < 0 || controllerJoyStickButtonStyle.getKnobPXLeft() > 0) {
//System.out.println("[Input Debug] isTouched(1)");
                if (controllerJoyStickButtonStyle.getKnobPXLeft() > 0 && bubble.b2body.getLinearVelocity().x <= 1.5f) {

                    bubble.b2body.applyLinearImpulse(new Vector2(0.1f, 0), bubble.b2body.getWorldCenter(), true);
                    bubble.setPlayerControllDirectionRunningRightTrue();

                } else if (controllerJoyStickButtonStyle.getKnobPXLeft() < 0 && bubble.b2body.getLinearVelocity().x >= -1.5f) {
                    bubble.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), bubble.b2body.getWorldCenter(), true);
                    bubble.setPlayerControllDirectionRunningRightFalse();
                }
            }

            if(controllerJoyStickButtonStyle.getShotButtonHudPressed()){
                System.out.println("PlayScreenClass Input: ControllerJoystick getShot: true" );
                notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, AudioObserver.AudioTypeEvent.SOUND_DROP);
                controllerJoyStickButtonStyle.setShotButtonHudPressedFalse();
            }

            /** Jump Sound Normal jump - need falling test -Or sound will play if a sound is stored and we fall down a platform */
            if(!bubble.getPlayerOnGroundNew() && !bubble.getPlayerWallJump() && !bubble.currentState.equals(BubblePlayer.PlayerState.FALLING)) {
                if (controllerJoyStickButtonStyle.getJumpButtonHudPressed() && soundJumpControllerNormalJump == 0) {
                    notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, AudioObserver.AudioTypeEvent.SOUND_JUMP);
                    controllerJoyStickButtonStyle.setJumpButtonIsPressedFalse();
                }
            }
            /** Jump Sound Controller Normal jump */
            if(bubble.getPlayerOnGroundNew() && !bubble.getPlayerWallJump() ){
                soundJumpControllerNormalJump = 0; // On
            }else{
                soundJumpControllerNormalJump = 1; // Off
            }


            /** Jump Sound Wall jump ? Never sett controllerSound to 1 ? */
            /*
            if(bubble.getPlayerWallJump() && bubble.getPlayerWallJump()) {
                if (controllerJoyStickButtonStyle.getJumpButtonHudPressed() && soundJumpControllerNormalWallJump == 0) {
                    notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, AudioObserver.AudioTypeEvent.SOUND_JUMP);
                    controllerJoyStickButtonStyle.setJumpButtonIsPressedFalse();
                }
            }
            */
            /** Jump Sound Controller Wall jump */
            /*
            if(bubble.getPlayerWallJump() && !bubble.getPlayerOnGroundNew() ){
                soundJumpControllerNormalWallJump = 0; // On
            }else{
                soundJumpControllerNormalWallJump = 1; // Off
            }
            */
            // testing new speed with char 1.5 to 2.5 ?? OK
            if(!bubble.isDead()) {//bubble.currentState != BubblePlayer.State.DEAD) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)  && bubble.getPlayerOnGroundNew() ||
                        Gdx.input.isKeyJustPressed(Input.Keys.UP) && bubble.getPlayerWallJump()) {

                    bubble.jumpDesktop();
                    //notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, AudioObserver.AudioTypeEvent.SOUND_JUMP);
                }

                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && bubble.b2body.getLinearVelocity().x <= 2.5f) {
                    bubble.b2body.applyLinearImpulse(new Vector2(0.1f, 0), bubble.b2body.getWorldCenter(), true);
                    //System.out.println("applyLinearImpulse: " + bubble.b2body.getLinearVelocity());
                }

                if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && bubble.b2body.getLinearVelocity().x >= -2.5f) {
                    bubble.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), bubble.b2body.getWorldCenter(), true);
                    //System.out.println("applyLinearImpulse: " + bubble.b2body.getLinearVelocity());
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    bubble.fire(Integer.parseInt(bubble.getPlayerActvieShootingPower())); // retrive the player Chose of power !!!
                    notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, AudioObserver.AudioTypeEvent.SOUND_DROP);
                }
            }
        }


    }

/*
    public void setParticleEffectPosition_test(){
        if(worldB2VarsCreate.getGamePfxObjects().size != 0) {
            for (GamePfxObject gamePfxObject : worldB2VarsCreate.getGamePfxObjects()) {

                particleEffect.setPosition(gamePfxObject.b2body.getPosition().x * 100f, gamePfxObject.b2body.getPosition().y * 100f);

            }
            //particleEffect.setPosition( 273.33f, 273.33f);

            //particleEffect.getBoundingBox().
        }
    }
*/
    //ToDo: test world bodyCount if we need to scale the body count down!!
    public void update(float dt) {
        //System.out.println("Start Var of WorldBodyCount is = " + this.world_body_count); // Fixed :)

        if(gameManagerAssetsInstance.getGameState() != GameManagerAssets.GameState.GAME_PAUSED ) {
            handleInput(dt);

            /** prints out the world Body count when it increase or decrease in world */
            if(this.world_body_count != this.world.getBodyCount() ){
                System.out.println("update World Body's: " + world.getBodyCount() );
                this.world_body_count = this.world.getBodyCount(); /** Reset the world body count to the check value*/
            }


/*
            System.out.println("PlayScreen Update ViewPort ScreenW: " +
                    gamePort.getScreenWidth() + " ScreenH: " +
                    gamePort.getScreenHeight() + " WorldW: " +
                    gamePort.getWorldWidth() + " WorldH: " +
                    gamePort.getWorldHeight() );
*/
            //System.out.println("World body's crated: " + world.getBodyCount() );
            //System.out.println("JavaHeap: " + Gdx.app.getJavaHeap() );
            //System.out.println("JavaNativeHeap : " + Gdx.app.getNativeHeap() );




            accumulator += Math.min(dt, 0.25f);
            while (accumulator >= STEEP_TIME){
                accumulator -= STEEP_TIME;

                //rayHandler.update();

                world.step(STEEP_TIME, 6, 2);
            }


            /** Updates gui buttons cool down timer */
            GameTime.updateCurrentTime(dt);


            gameManagerAI.update(dt);
            bubble.update(dt);



            //particleEffect.update(dt);


            //rain_Part_One_Particles.update(dt);
            //rain_Part_Two_Particles.update(dt);
            //rain_Part_Three_Particles.update(dt);

//ToDo:: testing!!!
            //effect.setPosition(bubble.b2body.getPosition().x / GameUtility.PPM, bubble.b2body.getPosition().y / GameUtility.PPM);
            //effect.setPosition( Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            //effect.update(dt);

            //effect.start();
            //effect.setPosition( bubble.b2body.getPosition().x, bubble.b2body.getPosition().y + 150  );
            //effect.setDuration(10);


            /** Door Switches */
            for( GameObjectSwitchDoor gameObjectSwitch : worldB2VarsCreate.getGameObjectSwitches()){

                if(gameObjectSwitch.getRunDoors()){
                   gameObjectSwitch.active(worldB2VarsCreate.activableObstacles);
                }
            }

            for(GameObjectSwitchDoor gameObjectSwitch : worldB2VarsCreate.getGameObjectSwitches()) {

                gameObjectSwitch.update(dt);
            }

            /** Hidden Switches */
            for(GameObjectSwitchHidden gameObjectSwitchHidden : worldB2VarsCreate.getGameObjectSwitchesHidden()) {

                if(gameObjectSwitchHidden.getRunDoors()) {
                    gameObjectSwitchHidden.active(worldB2VarsCreate.activableObstacles);
                }
            }

            for(GameObjectSwitchHidden gameObjectSwitchHidden : worldB2VarsCreate.getGameObjectSwitchesHidden()) {

                gameObjectSwitchHidden.update(dt);
            }


            //spawnExtraGameObjectFromEnemyDead = gameManagerAI.getExtraLifeGameItemsToSpawnInGameArrayToDraw();
            //System.out.println("spawnExtraGameObjectFromEnemyDead.Size: " + spawnExtraGameObjectFromEnemyDead.size );

            /**  The Door's */
            for (Obstacle obstacle : worldB2VarsCreate.getObstacles()) {
                ((ObstacleDoor)obstacle).update(dt);
            }
            /** activable obstacle */
            for(Obstacle obstacle : worldB2VarsCreate.getActiveAbleObstacles()) {

                obstacle.update(dt);
            }


            /**  SavePoint's
            get savePoint number, go through the list, Update all points not active, change texture to false!!
            */
            for(SavePoint savePoint : worldB2VarsCreate.getSavePoints()) {

                savePoint.update(dt);
                /*
                if(savePoint.getSavePoint().equals(GameManagerAssets.instance.getUpdateAllSavePointTexture() )){
                        savePoint.onGameObjectHitChangeTexture(false);
                        savePoint.setSavePointTextureOff();
                }else{
                    savePoint.onGameObjectHitChangeTexture(true);
                    savePoint.setSavePointTextureOn();
                }
                */
            }

            /** Boss Spawn Point AI - Marker */
            for(GameAIObject gameAIObject : worldB2VarsCreate.getGameAIBossSpawnObjects()) {
                gameAIObject.update(dt);
            }

            /** All the games Portals -Level & New Worlds- */
            for(Portal portal : worldB2VarsCreate.getPortals()){
                portal.update(dt);
            }
            /** All the games Hidden transition Portals -Level & New Worlds- No texture */
            for(PortalMapTransitionHidden hiddenTransition : worldB2VarsCreate.getPortalsHiddenMapTransition()){
                hiddenTransition.update(dt);
            }

            /*
            for(ExtraLifeGameItem exlife : worldB2VarsCreate.getExtraLife() ){
                exlife.update(dt);
            }
            */

            /** Extra life, extraDragonEgg, extraTreausreChest, playerPowerUP etc */
            for(ItemObjectDef itemObjectDef : worldB2VarsCreate.getItemGameObjects()) {
                itemObjectDef.update(dt);
            }

            /** Update graphics Grass etc */
            for(AnimationItemDef itemDef : worldB2VarsCreate.getGameObjectGraphicsAnimationItemForGround()) {
                itemDef.update(dt);
            }

            /** Update graphics Grass etc */
            for(AnimationItemDef itemDef : worldB2VarsCreate.getGameObjectGraphicsAnimationItemBackGround()) {
                itemDef.update(dt);
            }


            /** Moving Falling Enemy Stalgmites */
            // moved to GameManagerAI
            /*
            for(MovingFallingEnemyDef itemObject : worldB2VarsCreate.getEnemyMovingFalling()) {
                itemObject.update(dt);
            }
            */

            /** Enemy spikes Update */
            //for(EnemyGraphicSensor spikes : worldB2VarsCreate.getEnemyGraphicSensors()) {
            //    spikes.update(dt);
            //}

            //for(EnemyVineThorns vines : worldB2VarsCreate.getTrapVines()) {
            //    vines.update(dt);
            //}
/*
            for(ItemObjectDef itemObjectDef : worldB2VarsCreate.getItemGameToSpawnFromDeadEnemy()) {
                itemObjectDef.b2body.setActive(true);
                //((ExtraLifeGameItem)itemObjectDef).setDrawItemBool(true);
                itemObjectDef.update(dt);
            }
*/
            //hud update
            hud.update(dt, bubble.updatePlayerLifeToHudAndSaveOnExit());
            controllerJoyStickButtonStyle.update();


            //attach our gamecam to our players.x coordinate

            float startX2 = gamePort.getWorldWidth() / 2; //gamecam.viewportWidth / 2;
            float startY2 = gamePort.getWorldHeight() / 2; //gamecam.viewportHeight / 2;
            //System.out.println("startX gamecam.viewportW: " + gamecam.viewportWidth );

            // last used !!
            startX = gamecam.viewportWidth / 2; //gamePort.getWorldWidth() / 2; //gamecam.viewportWidth / 2;
            startY = gamecam.viewportHeight / 2; //gamePort.getWorldHeight() / 2; //gamecam.viewportHeight / 2;
            float posisX = gamecam.viewportWidth / 2;
            float posisY = gamecam.viewportHeight / 2;




/*
            if(!(bubble.b2body.getPosition().x > gamecam.viewportWidth + 0.8f)  ) {

                gamecam.position.x = bubble.b2body.getPosition().x;
            }
*/

            /** Tak away Black bars -Screen- */
            //boundary(gamecam,
            // (gamecam.viewportWidth / 2),
            // (gamecam.viewportHeight / 2),
            //levelWidth(Map Prop = 20 Tiles) * 64 - startX * 2,
            //levelHeight(Map Prop = 10 Tiles) * 64 - startY * 2);

            // The Tiles = 64 bit Large.



            /** Make the Camera not stutter with use of boundary */
            gamecam.position.x = bubble.b2body.getPosition().x;

            /** for Seen where player is falling a long time !!!    */
            /*
            if(cutFallSceenRunning) {
                gamecam.position.y = bubble.b2body.getPosition().y;
            }else{
                gamecam.position.x = bubble.b2body.getPosition().x;
            }
            */

            //boundary(gamecam, startX, startY, levelWidth * 64 - startX * 2, levelHeight * 64 - startY * 2); //old Code
            //boundary(gamecam, startX, startY, 12.8f * 2 - startX * 2, 6.4f * 2 - startY * 2); calculated x,y

            // startX = 4 (8/2)
            // width = 40 * 0.64f - 4 * 2 // 40 = map width
            // width = 25.6 - 8
            // width = 17.6f


            /** this works over all - but on map Size small or lager the textManager wont work */
            boundary(gamecam, startX, startY, levelWidth * 0.64f - startX * 2, levelHeight * 0.64f - startY * 2); // New Formula

            /**
             *
             * if we use gamePort.getWorldWidth() / 2 or
             * if we use gamecam.viewportWidth() / 2
             *
             * and 0.0 on startY && startX the text is good and camera to left up/down is good but not right up/down
             *
             * map 40 - 20.
             *
             * test on map 20 - 10 || here the text is out of sync but not far off the player height && width !!
             *
             * change now startX && Y from 0.0 to a number !! se again
             *
             * */


            //System.out.println("startX " + startX + " startY " + startY );
            //boundary(gamecam, posisX, posisY, levelWidth * 0.64f - startX * 2, levelHeight * 0.64f - startY * 2 ); // mixing gamePort.W With gamcam.view...

            // forgot to change startX2 and startY2 where startX * 2 etc.... ??? but where good !!!!
            //System.out.println("startX " + startX + " startY " + startY );
            //boundary(gamecam, startX2, startY2, levelWidth * 0.64f - startX * 2, levelHeight * 0.64f - startY * 2); //


            //boundaryTwo(OrthographicCamera camera, float pX, float pY, FitViewport viewport, float worldW, float worldH )
            //boundaryTwo(gamecam, bubble.b2body.getPosition().x, bubble.b2body.getPosition().y, gamePort, levelWidth * 0.64f, levelHeight * 0.64f);


            /** update our game camera with correct coordinates after changes */
            gamecam.update();



            /** RayHandler lights etc */
            //rayHandler.useCustomViewport(gamePort.getScreenX(),
            //        gamePort.getScreenY(), gamePort.getScreenWidth(), gamePort.getScreenHeight());

            //rayHandler.diffuseBlendFunc.set(
            //        GL20.GL_DST_COLOR, GL20.GL_SRC_COLOR);
            //myLight.setColor(Color.BLACK);
            //myLight.update();
            renderer.setView(gamecam);
        }
    }




    @Override
    public void render(float dt) {

        update(dt);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f); // Black
        //Gdx.gl.glClearColor(1f, 1f, 1f, 1f); // Don't will have white bars at sides !!!
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK) ) {
            System.out.println("back key is pressed");

            navigationDrawerScreen.setPauseMenuActive().showManually(true);
            gameManagerAssetsInstance.setGameState(GameManagerAssets.GameState.GAME_PAUSED);
        }
        /**
         * Internal Clock for CoolTime on UI Buttons
         */
        if(gameManagerAssetsInstance.getGameState() != GameManagerAssets.GameState.GAME_PAUSED) {

            GameTime.updateCurrentTime(Gdx.graphics.getDeltaTime());
        }

        if(gameManagerAssetsInstance.getGameState() == GameManagerAssets.GameState.GAME_PAUSED) {

            inputMultiplexer.removeProcessor(controllerJoyStickButtonStyle.getStage());
            //inputMultiplexer.removeProcessor(controllerJoyStickButtonStyle.getGestureDect());
        }

        if(gameManagerAssetsInstance.getGameState() == GameManagerAssets.GameState.GAME_RESUMED &&
                gameManagerAssetsInstance.getPreGameState() == GameManagerAssets.GameState.GAME_PAUSED ) {

            gameManagerAssetsInstance.setGameState(GameManagerAssets.GameState.GAME_RUNNING);
            inputMultiplexer.addProcessor(controllerJoyStickButtonStyle.getStage());
            //inputMultiplexer.addProcessor(controllerJoyStickButtonStyle.getGestureDect());
        }

        if(!bubble.isDead()) {
            gamecam.position.lerp( lerpTarget.set(bubble.b2body.getPosition().x, bubble.b2body.getPosition().y, 0), 3.5f * dt);
        }




// Render Debug rayCast lines


        // Debugging for RayCasting steering GameAIObject
        //shapeRenderer.setProjectionMatrix(gamecam.combined);
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // TODO : All parallax Screen are background -> we want all so som in front of player !!!???


        /** don' know if needed, and take away parallax as bad with particle effect */
        //gameName.batch.setProjectionMatrix(parallaxGameScreen.getParallaxStage().getCamera().combined);

        if(bubble.getPlayerFacingDirection()) {
            if( bubble.b2body.getLinearVelocity().x != 0 || bubble.b2body.getLinearVelocity().y != 0 ) {
                //parallaxGameScreen.setSpeedAndDirection(1);
            }else {
                //parallaxGameScreen.setSpeedAndDirection(0);
            }
        }else {
           if( bubble.b2body.getLinearVelocity().x != 0 || bubble.b2body.getLinearVelocity().y != 0 ) {
                //parallaxGameScreen.setSpeedAndDirection(-1);
            }else {
                //parallaxGameScreen.setSpeedAndDirection(0);
            }
        }

        //gameName.batch.setProjectionMatrix( ((ParallaxGameScreen.ParallaxCamera)parallaxGameScreen.getParallaxStage().getCamera()).calculateParallaxMatrix(0,0) );
        //gameName.batch.setProjectionMatrix(((ParallaxGameScreen.ParallaxCamera)parallaxGameScreen.getParallaxStage().getCamera()).parallaxCombined );
//        parallaxGameScreen.render(dt);




        //render our game map
        //renderer.getBatch().enableBlending();
        //renderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // TODO : render layer ???

        //renderer.getBatch().begin();
        //renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("GroundLowLayerGraphic"));
        //renderer.getBatch().end();

        renderer.render();

        //renderer.setView(gamecam);

        gameName.batch.setProjectionMatrix(gamecam.combined); // this is Main Cam to render!!!



        /** this draws very close to screen eye effect rain on phone screen sort of  */
        //gameName.batch.begin();
        //rain_Part_One_Particles.draw(gameName.batch); // fire fly
        //rain_Part_Two_Particles.draw(gameName.batch); // rain
        //rain_Part_Three_Particles.draw(gameName.batch);
        //gameName.batch.end();



        /**  Debug Line Renderer */
b2dr.render(world, gamecam.combined);
        /**  Debug Line Renderer */




        gameName.batch.begin();




        /**  Save point (texture update)  NB!! have to be drawn be for the Player */
        for(SavePoint savePoint : worldB2VarsCreate.getSavePoints()) {
            savePoint.draw(gameName.batch);
        }

        /** Game Doors Texture update when moving */
        for(GameObjectSwitchDoor gameObjectSwitch : worldB2VarsCreate.getGameObjectSwitches()) {
            gameObjectSwitch.draw(gameName.batch);
        }

        /** Game Doors Texture update when moving Boss is dead Open door again!! */
        //for(GameObjectSwitchHidden gameObjectSwitch : worldB2VarsCreate.getGameObjectSwitchesBossDead()) {
        //    gameObjectSwitch.draw(gameName.batch);
        //}



        /**  Only need this if we wan't hidden Switch with Texture */
        /*
        for(GameObjectSwitchHidden gameObjectSwitchHidden : worldB2VarsCreate.getGameObjectSwitchesHidden()) {

           gameObjectSwitchHidden.draw(gameName.batch);
        }
        */

        /** Render enemy  debug - comment out - to se hit box only */
        for (SmallEnemyDef smallEnemyDef : worldB2VarsCreate.getEnemies()) {


            //smallEnemyDef.draw(gameName.batch);

            /**Only Draw what player can se and is active*/
            if(smallEnemyDef.b2body.isActive() ) {//&& !((EnemyA)smallEnemyDef).isDead() ) {
                smallEnemyDef.draw(gameName.batch);
            }
        }

        for (BossEnemyDef bossEnemyDef : worldB2VarsCreate.getEnemiesBoss()) {

            if(bossEnemyDef.b2body.isActive() && !( (EnemyKnightDevil)bossEnemyDef).isDead() ){
                bossEnemyDef.draw(gameName.batch);
            }

        }

        /** Moving Falling Enemy Stalgmites */
        for(MovingFallingEnemyDef itemObject : worldB2VarsCreate.getEnemyMovingFalling()) {
            itemObject.draw(gameName.batch);
        }

        /** Render ExtraLife Dropped from Dead EnemyA*/
        for(ItemObjectDef itemObjectDef : gameManagerAI.getExtraLifeSpawnFromDeadToDraw() ) {
                itemObjectDef.draw(gameName.batch);
        }

        /** Render DragonEgg Dropped from Dead SmallEnemyDef */
        for(ItemObjectDef itemObjectDef : gameManagerAI.getDragonEggToDraw() ) {
            itemObjectDef.draw(gameName.batch);
        }


        // texture draw portals
        for(Portal portal : worldB2VarsCreate.getPortals()){
            portal.draw(gameName.batch);
        }


        // for now only Extra_life
        for(ItemObjectDef itemObjectDef : worldB2VarsCreate.getItemGameObjects()) {
            itemObjectDef.draw(gameName.batch);
        }



        for (Obstacle obstacle : worldB2VarsCreate.getObstacles()) {
                ((ObstacleDoor)obstacle).draw(gameName.batch);
        }

        // activable obstacle !!! This got drawn!!! not the other but need both to move the doors!!!
        for(Obstacle obstacle : worldB2VarsCreate.getActiveAbleObstacles()) {

            obstacle.draw(gameName.batch);
        }

        /** Enemy spikes Update - don't need this if no animation ??? */
        //for(EnemyGraphicSensor spikes : worldB2VarsCreate.getEnemyGraphicSensors()) {
            //spikes.draw(gameName.batch);
        //}

        //for(EnemyVineThorns vines : worldB2VarsCreate.getTrapVines()) {
        //    vines.draw(gameName.batch);
        //}


        /* Need this only if one wants texture
        for(GameAIObject mark : worldB2VarsCreate.getMarkers() ) {
            mark.draw(gameName.batch);
        }
        */



        //player /- Check on Power - bubbles draw in front or back of enemy etc...
        bubble.draw(gameName.batch);
        //effect.draw(gameName.batch);


        /** Render Visual Graphics Animation Grass etc - Behind visual Graphics*/
        for(AnimationItemDef itemDef : worldB2VarsCreate.getGameObjectGraphicsAnimationItemBackGround()) {
            itemDef.draw(gameName.batch);
        }
        gameName.batch.end();

        renderer.getBatch().begin();
        renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("PlayerForGroundLayerGraphic")); // Render Over Player
        renderer.getBatch().end();



        gameName.batch.begin();
        /** Render Visual Graphics Animation Grass etc - Behind visual Graphics*/
        for(AnimationItemDef itemDef : worldB2VarsCreate.getGameObjectGraphicsAnimationItemForGround()) {
            itemDef.draw(gameName.batch);
        }
        gameName.batch.end();



        //ToDo:: RayHandler render
        //gameName.batch.setProjectionMatrix(rayHandler.setCombinedMatrix(gamecam));
        //rayHandler.setCombinedMatrix(gamecam);
        //rayHandler.updateAndRender();
        //rayHandler.setCombinedMatrix(gamecam);
        //rayHandler.updateAndRender();

/** Testing running it in front */
        //parallaxGameScreen.render(dt);

        //Set our batch to now draw what the Hud camera sees.
        gameName.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.render(dt);



        /** could this work ???  copy hud way of doing it ???*/
        //gameName.batch.setProjectionMatrix(particles.stage.getCamera().combbined);
        //particles.render(dt);

        /** This works with out parallax dull's it out !!?? */



            //particleEffect.draw(gameName.batch);

            for (GamePfxObject gamePfxObject : worldB2VarsCreate.getGamePfxObjects()) {
                gameName.batch.begin();
                //gamePfxObject.drawParticleEffect(gameName.batch, dt);
                gameName.batch.end();

            }



/*
        for (GamePfxObject gamePfxObject : worldB2VarsCreate.getGamePfxObjects()) {
            gamePfxObject.setParticleEffectOn(true);
            gamePfxObject.drawParticleEffect(gameName.batch);
        }
*/

            //rain_Part_One_Particles.draw(gameName.batch, dt);
            //rain_Part_Two_Particles.draw(gameName.batch, dt);
            //rain_Part_Three_Particles.draw(gameName.batch, dt);




/*
        // Controller's
        gameName.batch.setProjectionMatrix(controllerJoyStickButtonStyle.getStage().getCamera().combined);
        //gameName.batch.setProjectionMatrix(controllerJoyStickButtonStyle.getStage().getCamera().unproject());
        controllerJoyStickButtonStyle.render(dt);
        controllerJoyStickButtonStyle.update(); // need to update buttons on cooldown

        gameName.batch.setProjectionMatrix(navigationDrawerScreen.getStage().getCamera().combined);
        navigationDrawerScreen.render(dt);
        //navigationDrawerScreen.update(dt); // diden't look like i needed this one ?
*/

        //TODO font testing with fling!!
        //TODO font fpsLogger
// comment out this is debug!! ToDo: Comment out!!


        //gameName.batch.begin();
        // fx -Pick up Message Line ?? test ?? */ // controllerJoyStickButtonStyle.getMessage()
        //font.draw(gameName.batch, "Power Up",(GameUtility.V_WIDTH / 2) + 150 + 1 * 90, 190); //bubble.b2body.getPosition().x, bubble.b2body.getPosition().y + 150);
        //font.draw(gameName.batch, "20",(GameUtility.V_WIDTH / 2) + 150 + 1 * 90, 250); //bubble.b2body.getPosition().x, bubble.b2body.getPosition().y + 150);
        // Debug line //
        //font.draw(gameName.batch, fpsLogger.logg(), GameUtility.V_WIDTH / GameUtility.PPM  +5 , GameUtility.V_HEIGHT / GameUtility.PPM +10 ); // Down Left Corner
        //gameName.batch.end();




        // Begin Controller's
        gameName.batch.setProjectionMatrix(controllerJoyStickButtonStyle.getStage().getCamera().combined);
        controllerJoyStickButtonStyle.render(dt);
        controllerJoyStickButtonStyle.update(); // need to update buttons on cooldown

        // moved down one step
        //gameName.batch.setProjectionMatrix(navigationDrawerScreen.getStage().getCamera().combined);
        //navigationDrawerScreen.render(dt);
        // End Controller's

        /** Counter left to shoot and info about buttons TEST*/
        gameName.batch.begin();
        /** fx -Pick up Message Line ?? test ?? */ // controllerJoyStickButtonStyle.getMessage()
        //font.draw(gameName.batch, "Dash",(GameUtility.V_WIDTH / 2) + 170 + 1 * 90, 210); //bubble.b2body.getPosition().x, bubble.b2body.getPosition().y + 150);
        //font.draw(gameName.batch, "20",(GameUtility.V_WIDTH / 2) + 176 + 1 * 90, 290); //bubble.b2body.getPosition().x, bubble.b2body.getPosition().y + 150);

        //font.draw(gameName.batch, "Poison",(GameUtility.V_WIDTH / 2) + 160 + 1 * 90, 84);

        if(bubble.getBallooneBulletGreen() > 9){
            //font.draw(gameName.batch, Integer.toString(bubble.getBallooneBulletGreen()),(GameUtility.V_WIDTH / 2) + 176 + 1 * 90, 170);

            /** Crystal power Left side info*/
            font.draw(gameName.batch, Integer.toString(bubble.getBallooneBulletRed()),(GameUtility.V_WIDTH / 2) - 290 - 1 * 90, 493); // top
            font.draw(gameName.batch, Integer.toString(bubble.getBallooneBulletBlue()),(GameUtility.V_WIDTH / 2) - 290 - 1 * 90, 474);
            font.draw(gameName.batch, Integer.toString(bubble.getBallooneBulletBlack()),(GameUtility.V_WIDTH / 2) - 290 - 1 * 90, 455);
            //font.draw(gameName.batch, Integer.toString(bubble.getBallooneBulletBlack()),(GameUtility.V_WIDTH / 2) - 290 - 1 * 90, 436);
        }else {
            //font.draw(gameName.batch, "0" + Integer.toString(bubble.getBallooneBulletGreen()), (GameUtility.V_WIDTH / 2) + 176 + 1 * 90, 170);

            /** Crystal power Left side info*/
            font.draw(gameName.batch, "0" + Integer.toString(bubble.getBallooneBulletRed()),(GameUtility.V_WIDTH / 2) - 290 - 1 * 90, 493); // top
            font.draw(gameName.batch, "0" + Integer.toString(bubble.getBallooneBulletBlue()),(GameUtility.V_WIDTH / 2) - 290 - 1 * 90, 474);
            font.draw(gameName.batch, "0" + Integer.toString(bubble.getBallooneBulletBlack()),(GameUtility.V_WIDTH / 2) - 290 - 1 * 90, 455);
            //font.draw(gameName.batch, "0" + Integer.toString(bubble.getBallooneBulletBlack()),(GameUtility.V_WIDTH / 2) - 290 - 1 * 90, 436);

        }

        //font.draw(gameName.batch, "Jump",(GameUtility.V_WIDTH / 2) + 75 + 1 * 90, 84);
        gameName.batch.end();



        if( controllerJoyStickButtonStyle.getGameMapShow() ) {

            //gameMapScreen.show();

            //gameMapScreen = new GameMapScreen(this.gameName);
            //gameMapScreen.show();
            //gameMapScreen.getStage().setDebugAll(true);

            gameMapScreen.draw();
            gameMapScreen.show();
            inputMultiplexer.addProcessor(this.gameMapScreen.getStage());
            inputMultiplexer.removeProcessor(controllerJoyStickButtonStyle.getStage());
            //System.out.println("we draw gameMapScreen!!!");
        }

        if(gameMapScreen.getExitWindow()){
            inputMultiplexer.removeProcessor(this.gameMapScreen.getStage());
            gameMapScreen.hide();
            controllerJoyStickButtonStyle.hideGameMapShow();
            inputMultiplexer.addProcessor(controllerJoyStickButtonStyle.getStage());
            //System.out.println("Close & hide Window, happend one time!!");
        }

        gameName.batch.setProjectionMatrix(navigationDrawerScreen.getStage().getCamera().combined);
        navigationDrawerScreen.render(dt);
        // End Controller's


        /** Debug line */
        gameName.batch.begin();
        font.draw(gameName.batch, fpsLogger.logg(), GameUtility.V_WIDTH / GameUtility.PPM  +5 , GameUtility.V_HEIGHT / GameUtility.PPM +10 ); // Down Left Corner
        gameName.batch.end();

        /** RayHandler light Render & update */
        //rayHandler.setCombinedMatrix(gamecam);
        //rayHandler.updateAndRender();

        /**
         * Testing : just to show it for now, this is of course very wrong how to sett it... etc.
         * ???!!! Map internal box view
         */
/*
        if(navigationDrawerScreen.getGameMapShow()) {
            gameMapScreen = new GameMapScreen(this.gameName);
            gameMapScreen.draw();
        }
*/

        /**
         * Testing : just to show it for now, this is of course very wrong how to sett it... etc.
         * ???!!! Map internal box view
         */

        // ToDo : change this to Last savePoint with 1 life bar!!
        if(gameOver()){

             /** if one have more then one life left and fall to far spawn at last save point else back to menu!!
             * and from there Play -> last spawn point */
            //if( bubble.updatePlayerLifeToHudAndSaveOnExit() > 1 ) {
            bubble.removeLifeFallToDeath();

            gameManagerAssetsInstance.setSaveGamePlayerStatsWithoutPosition();

            //System.out.println("Game Over");
            //System.out.println("save game world : " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
            //        " save game level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() );

            /** load the last save game, gets world, level from saveGame */
            gameManagerAssetsInstance.loadMapNewWorld(gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointWorld()
            ,gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointLevel());

            // this is redundant take away input when sure!!!
                gameName.setScreen(new PlayScreen((NameGame) gameName,
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointWorld(),
                        gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointLevel(), gameManagerAssetsInstance ));
            dispose();

        }
/** currentWorld currentLevel | newCurrentWorld newCurrentLevel
 *  are set by GameManagerAsset on call to onPortalTravelHit*/

        if(gameWorldMapChange()) {

           //System.out.println("Change the Map World, we do not save position");

            gameManagerAssetsInstance.setSaveGamePlayerStatsWithoutPosition();
            gameManagerAssetsInstance.loadMapNewWorld();

           gameName.setScreen(new PortalMapTransition(gameName,
                   gameManagerAssetsInstance.getNewCurrentWorld(),
                   gameManagerAssetsInstance.getNewCurrentLevel(), gameManagerAssetsInstance )
           );
            dispose();

        }

        if(gameLevelMapChange()) {

            //System.out.println("Change the Map level, we do not save position");

            gameManagerAssetsInstance.setSaveGamePlayerStatsWithoutPosition();

            gameManagerAssetsInstance.loadMapNewWorld();

            gameName.setScreen(new PortalMapTransition(gameName,
                    gameManagerAssetsInstance.getNewCurrentWorld(),
                    gameManagerAssetsInstance.getNewCurrentLevel(), gameManagerAssetsInstance )
            );
            dispose();

        }


    }
    /** this starts World Map change - player get's it from onPortalHit */
    public boolean gameWorldMapChange(){
        if(bubble.changeWorldMap()){
            return true;
        }
        return false;
    }

    public boolean gameLevelMapChange(){
        if(bubble.changeLevelMap()){
            return true;
        }
        return false;
    }


    public boolean gameOver(){

        //System.out.println("Ypos : " + player.b2body.getPosition().y);

        // StateTimer is not fully in use i think - look to Mario and might be use full on Harder Level time level
        // add && bubble.getLife != 0
        if(bubble.currentState == BubblePlayer.PlayerState.DEAD && bubble.getStateTimer() > 3 || bubble.b2body.getPosition().y < -2){
            return true;
        }

        return false;
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    @Override
    public void resize(int width, int height) {
            gamePort.update(width, height);
    }

    @Override
    public void pause() {

        //System.out.println("pause is Used");
        navigationDrawerScreen.setPauseMenuActive().showManually(true);
        gameManagerAssetsInstance.setGameState(GameManagerAssets.GameState.GAME_PAUSED);

    }


    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }


    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        System.out.println("PlayScreen dispose");

        //bubble.world.dispose(); // new to dispose
        //navigationDrawerScreen.dispose(); // new to dispose
        //controllerJoyStickButtonStyle.dispose(); // new to dispose

        map.dispose();
        renderer.dispose();
        //rayHandler.dispose();
        world.dispose();
        b2dr.dispose();

        gameManagerAssetsInstance.disposeGameIsRunning();


        // TODO check this : not working!! after restart of game!!
        //GameManagerAI.instance.dispose();
        gameManagerAI.dispose();

        //controllerDpad.dispose();
        //controllerJoyStick.dispose();
        //ControllerJoyStickButtonStyle.dispose(); static ??!! change
        hud.dispose();


    }



    //startX = gamecam.viewportWidth / 2;//gamePort.getWorldWidth() / 2; //gamecam.viewportWidth / 2;
    //startY = gamecam.viewportHeight / 2; //gamePort.getWorldHeight() / 2; //gamecam.viewportHeight / 2;
    //boundary(gamecam, startX, startY, levelWidth * 64 - startX * 2, levelHeight * 64 - startY * 2);
    // takes away black bars at each ends of the view game!!?
    public static void boundary(OrthographicCamera camera, float startX, float startY, float with, float height ) {
        Vector3 position = camera.position;

        if(position.x < startX) {
            position.x = startX;
        }
        if(position.y < startY) {
            position.y = startY;
        }

        if(position.x > startX + with ) {
            position.x = startX + with;
        }

        if(position.y > startY + height) {
            position.y = startY + height;
        }

        camera.position.set(position);
        camera.update();
    }



    public Vector3 boundaryOfPlayer(Vector3 position, float startX, float startY, float with, float height ) {
  /*
        System.out.println("boundary Start Value");
        System.out.println("cameraPos: " + position);
        System.out.println("StartX Pos: " + startX);
        System.out.println("StartY Pos: " + startY);
        System.out.println("width Pos: " + with);
        System.out.println("height Pos: " + height);
*/
        //Vector3 position = camera.position;

        if(position.x < startX) {
            position.x = startX;
        }
        if(position.y < startY) {
            position.y = startY;
        }

        if(position.x > startX + with ) {
            position.x = startX + with;
        }

        if(position.y > startY + height) {
            position.y = startY + height;
        }

/*
        System.out.println("boundary Value");
        System.out.println("cameraPos: " + position);
        System.out.println("StartX Pos: " + startX);
        System.out.println("StartY Pos: " + startY);
        System.out.println("width Pos: " + with);
        System.out.println("height Pos: " + height);
*/
        return position;
    }
/*
    public void lerpToTarget(Camera camera, Vector2 target){

        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * 0.1f;
        position.y = camera.position.y - (target.y - camera.position.y) * 0.1f;
        camera.position.set(position);
        camera.update();
    }
*/
    public Vector2 rotatePoint(Vector2 center, Vector2 point, float angle) {

        angle = angle * MathUtils.degreesToRadians;
        float rotatedX = MathUtils.cos(angle) * (point.x - center.x) - MathUtils.sin(angle) * (point.y - center.y) + center.x;
        float rotatedY = MathUtils.sin(angle) * (point.x - center.x) + MathUtils.cos(angle) * ( point.y - center.y ) + center.y;

        return new Vector2(rotatedX, rotatedY);
    }


}
