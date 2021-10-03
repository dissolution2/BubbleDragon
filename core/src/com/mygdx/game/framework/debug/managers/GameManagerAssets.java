package com.mygdx.game.framework.debug.managers;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.SaveGamePlayerDataHolderClass;
import com.mygdx.game.framework.debug.SaveGameWorldDataHolderClass;
import com.mygdx.game.framework.debug.controllers.Button;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.B2WorldCreator;

/** testing if we get better response in changing gui action bars pre load then inn PlayScreen Class */
import com.mygdx.game.framework.debug.controllers.Action;
import com.mygdx.game.framework.debug.controllers.Button;


public class GameManagerAssets {

    //public static final GameManagerAssets instance = new GameManagerAssets();
    //public GameManagerAssets instance = new GameManagerAssets();

    private final static String DRAWER_UI_TEXTURE_ATLAS_PATH = "drawerUi/menu_ui.atlas";
    public static TextureAtlas DRAWER_UI_TEXTUREATLAS = new TextureAtlas(DRAWER_UI_TEXTURE_ATLAS_PATH);

    public static final float EPSILON = 0.00001f; // ?? Cool timer ??

    public static final String TEXTURE_ATLAS_HUD = "controllerUi/BubbleGameUI2.atlas"; //hud.pack";
    public static final String TEXTURE_ATLAS_HUD_BUTTON_UP_SUFFIX = "_up";
    public static final String TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX = "_down";

    /**
     * Class we save all GameData - Player Spawn point , Lives , Scores , powers etc.
     */
    public static SaveGamePlayerDataHolderClass saveGamePlayerDataHolderClass;
    public static SaveGameWorldDataHolderClass saveGameWorldDataHolderClass;

    public Vector2 playerPosition;
    public int maxLifeLostOnHitGameManagerAssets;

    private static FileHandle fileHandle;

    //Tiled map variables
    private TiledMap map;
    private String usingWorldMap;
    private String usingLevelMap;
    private String haveUsingMapSavePoint;


    private GameState gameState;
    private GameState preGameState;

    private boolean gameIsRunning;

    public enum GameState{
        INITIATING_NEW_GAME,
        INITIATING_LOADING_GAME,
        INITIATING_SAVING_GAME,
        GAME_LOADING_ASSETS,
        GAME_RUNNING,
        GAME_PAUSED,
        GAME_RESUMED,
    }

    private Vector2 playerSpawnStartPosition; // = new Vector2();
    private String updatedSavePointUsed; // ="";

    /** where to save to and load from */
    //private FileHandle file = Gdx.files.local("SaveGamePlayerDataHolderClass.json");
    private boolean isCurrentBossDefeated;

    private String currentWorld;
    private String currentLevel;
    private String newCurrentWorld;
    private String newCurrentLevel;

    private String oldCurrentWorld;
    private String oldCurrentLevel;

    private String mapTraveldDirection;
    private String mapTraveldSensorDirection;

    private boolean worldMapChange;
    private boolean levelMapChange;


    private String foWorldSave_World;
    private String foWorldSave_Level;
    private String foWorldSave_Boss;


    private Vector2 foPlayerPosition;
    private String foSavePointWorld;
    private String foSavePointLevel;
    private String foSavePointMarker;
    private float foSaveHighScores;
    private int foPlayerMainLife;
    private BubblePlayer bubblePlayer;

    private String foSavePowerCrystalInUse;

    private int foSaveGreenPowerCrystal;
    private int foSaveBlackPowerCrystal;
    private int foSaveBluePowerCrystal;
    private int foSaveRedPowerCrystal;

    private ArrayList<Button> main_action_bar_game_ui_list;


    private B2WorldCreator worldB2VarsCreate;
    private World world;
    //private TiledMap tiledMap;
    Array<String> gameAssetsGameReqList;
    Array<String> gameAssetGameReqListWorld01, gameAssetGameReqListWorld02, gameAssetGameReqListWorld03;

    public static GameManagerAssets getInstance() {
        if (instance == null) {
            instance = new GameManagerAssets();
        }

        return instance;
    }

    public void init() { // String w, String l) {

        preGameState = gameState = GameState.GAME_LOADING_ASSETS;

        main_action_bar_game_ui_list = new ArrayList<Button>();




        // Set list of asset, will change depending on level debug list!!!
        gameAssetsGameReqList = new Array<String>();
        gameAssetGameReqListWorld01 = new Array<String>();
        gameAssetGameReqListWorld02 = new Array<String>();
        gameAssetGameReqListWorld03 = new Array<String>();
        gameAssetsGameReqList();

        this.gameIsRunning = true;
        this.worldMapChange = false;
        this.levelMapChange = false;

        this.playerSpawnStartPosition = new Vector2();
        this.updatedSavePointUsed ="";

        this.foPlayerPosition = new Vector2();
        this.foSavePointWorld ="";
        this.foSavePointLevel ="";
        this.foSavePointMarker ="";
        this.foSaveHighScores = 0;
        this.foPlayerMainLife = 0;

        this.foSaveGreenPowerCrystal = 0;
        this.foSaveBlackPowerCrystal = 0;
        this.foSaveBluePowerCrystal = 0;
        this.foSaveRedPowerCrystal = 0;

        this.bubblePlayer = null;
        this.maxLifeLostOnHitGameManagerAssets = 1;

        this.isCurrentBossDefeated = false;

        //loadingScreenMenuAsset();
        //loadingSplashScreenAsset();
        //loadingPauseMenuDrawerAsset();
        //loadingJoyStickControllerButtonStyle();
        //loadingGameUIAssets(); // Player health bar etc
         // SmallEnemyDef, Player, Powers etc


        // Debug comment out!!


        //GameUtility.assetManager.finishLoading();
        //listAssetLoadedDebug();
        //isLoadedJoyStickControllerButtonStyle();
        System.out.println("***Begin GameManagerAsset Init***" );
        //System.out.println("Current World: " + GameManagerAssets.instance.getCurrentWorld() ); // not correct
        //System.out.println("Current Level: " + GameManagerAssets.instance.getCurrentLevel() );
        //System.out.println("info -:");
        //System.out.println("Steering Might flipp the SmallEnemyDef!! look at Debug Legg's in the air" );
        //System.out.println("***End GAmeManagerAsset Init***");

        saveGameWorldDataHolderClass = null;
        saveGamePlayerDataHolderClass = new SaveGamePlayerDataHolderClass(); // makes the class
        saveGamePlayerDataHolderClass.init(); // init the class.

        //readFromSaveGamePlayer(); // Debug
        saveGameWorldDataHolderClass = null;
        saveGameWorldDataHolderClass = new SaveGameWorldDataHolderClass();
        saveGameWorldDataHolderClass.init();

        //System.out.println("************Read World save game be for a Save*******************");
        //readFromSaveGameWorld(); // Debug


        // world test Save Game
        //setDataToSaveGameWorld();

        if(gameManagerSaveFilePlayerExists() ) { // Check if there is a saveGame - Position then will be had from SaveGame - Read!!
            /** Reading saveGamePlayerDataHolderClass | Saving player.dat */

            gameManagerPlayerDataReadWriteSave();
            //System.out.println("GameManager Class - readFrom saveGame levelUse - loadMapWorldAndLevel() ");
            //readFromSaveGamePlayer();

            loadMapWorldAndLevel(saveGamePlayerDataHolderClass.getSavePointWorld(), saveGamePlayerDataHolderClass.getSavePointLevel());

            loadingGameAssets(saveGamePlayerDataHolderClass.getSavePointWorld(), saveGamePlayerDataHolderClass.getSavePointLevel());
            //GameUtility.assetManager.finishLoading();
            //System.out.println("GameManagerAsset class(init) - Map level loaded from save Game!!");

        }else{
            //this.currentWorld = w;
            //this.currentLevel = l;
            //System.out.println("GameManagerAsset Class(init) - Save don't Exists we are inn init - currentWorld: "
            //        + this.currentWorld +
            //        " currentLevel: " + this.currentLevel );
            System.out.println("Loading CurrentWorld, saveGame don't ex!!");
            loadMapWorldAndLevel(currentWorld, currentLevel);
            loadingGameAssets(currentWorld, currentLevel); //currentWorld
            //GameUtility.assetManager.finishLoading();

            //System.out.println("GameManagerAsset class(init) - Map level loaded World 1 Level 1 God Luck !!");
            //gameManagerPlayerDataReadWriteSave();
        }
        //Array<String>assetNames = new Array<String>();
        //assetNames = GameUtility.assetManager.getAssetNames();

        //System.out.println("Diagnostics: " + GameUtility.assetManager.getDiagnostics() );
/*
        for(int a = 0; a < gameAssetsGameReqList.size; a++){

            if(!GameUtility.assetManager.contains(gameAssetsGameReqList.get(a))){
                System.out.println("[AssetLoaded Not Loaded!!]: " + gameAssetsGameReqList.get(a));
            }else{
                System.out.println("[AssetLoaded]: " + gameAssetsGameReqList.get(a));
            }
        }
 */


        if(gameManagerSaveFilePlayerExists()){

            readFromSaveGamePlayer();

            if(getSaveGamePlayerDataHolderClass().getSavePlayerPowerCrystalInUse() != null) {
                if (getSaveGamePlayerDataHolderClass().getSavePlayerPowerCrystalInUse() == "1") {

                    /** We sett the default action bare buttons for the player in gui */

                    /**
                     * Metod: getMainActionBarGui - get from instance with in PlayerScreen Class etc.
                     * */

                    main_action_bar_game_ui_list.add(new Button("jump", 0, new Action()));
                    main_action_bar_game_ui_list.add(new Button("attack_one", 1.8f, new Action())); // 0.5f attack_two
                    main_action_bar_game_ui_list.add(new Button("power_one", 1.5f, new Action()));
                    //main_action_bar_game_ui_list.add(new Button("power_two", 1.5f, new Action()) );
                    main_action_bar_game_ui_list.add(new Button("meny_power", 0, new Action()));
                    main_action_bar_game_ui_list.add(new Button("power_chgreen", 0, new Action()));
                }else{
                    main_action_bar_game_ui_list.add(new Button("jump", 0, new Action()));
                    main_action_bar_game_ui_list.add(new Button("attack_two", 1.8f, new Action())); // 0.5f attack_two
                    main_action_bar_game_ui_list.add(new Button("power_one", 1.5f, new Action()));
                    //main_action_bar_game_ui_list.add(new Button("power_two", 1.5f, new Action()) );
                    main_action_bar_game_ui_list.add(new Button("meny_power", 0, new Action()));
                    main_action_bar_game_ui_list.add(new Button("power_chblue", 0, new Action()));
                }
            }
        }

    }

    public ArrayList<Button> getMain_action_bar_game_ui_list(){
       return this.main_action_bar_game_ui_list;
    }

    //public GameManagerAssets getGameManagerAssetsInstance(){ return this.instance; }

    //working on
    /** set's and get's Used by GameManagerAI and B2WarsCreator */
    public boolean getIsCurrentBossDefeated(){return this.isCurrentBossDefeated; }
    public void setIsCurrentBossDefeated(boolean value){ this.isCurrentBossDefeated = value; }


    public void setWorldSaveStat(boolean run) {

        // set -> setWorldSaveState( getIsCurrentBossDefeated() ) { etc.
        // if( getIsCurrentBossDefeated() ) {
        //      get world map and level we are using and in
        //
        //      we write to -> worldSave stat world nr and level nr / boss is defeated
        //
        //      b2 world creator  -> we set under boos Creation if( getIsCurrentBossDefeated()  NB from world save NB )
        //    }
        if(run){

        }


    }

    public void loadDefaultGameAssets(){
        System.out.println("[Loading default asset's] for the game... -LOCKED-");
        /** assetManager.finishLoading() lock this is the last to load default asset's -becouse of locked thread!!*/
        //GameUtility.assetManager.clear(); // testing new not sure

        loadingScreenMenuAsset();
        loadingSplashScreenAsset();

        loadingPauseMenuDrawerAsset();
        loadingJoyStickControllerButtonStyle();
        loadingGameUIAssets();

        loadingGameMapScreenAssets();

        //loadingGameAssets(); // testing new
        GameUtility.assetManager.finishLoading();

        for(int a = 0; a < gameAssetsGameReqList.size; a++){

            if(!GameUtility.assetManager.contains(gameAssetsGameReqList.get(a))){
                System.out.println("[StartUp AssetLoaded, Not Loaded!!]: " + gameAssetsGameReqList.get(a));
            }else{
                //System.out.println("[AssetLoaded]: " + gameAssetsGameReqList.get(a));
            }

            //System.out.println("gameAssetsGameReqList: " + gameAssetsGameReqList.size + " a: " + a);
            if((a+1) == gameAssetsGameReqList.size ){
                System.out.println("[All StartUp Asset is Loaded]");
            }
        }

    }

    public void setPlayerToAssetManager(BubblePlayer b) { this.bubblePlayer = b;}
    public int getMaxLifeLostOnHitGameManagerAssets(){ return this.maxLifeLostOnHitGameManagerAssets; }

    /** Debug */
    public void readFromSaveGamePlayer() {
        System.out.println("GameManagerAsset Class : Read from SaveGamePlayer Class :" ) ;

        System.out.println("\tSavePoint " + saveGamePlayerDataHolderClass.getSavePointPosition() );
        System.out.println("\tWorld " + saveGamePlayerDataHolderClass.getSavePointWorld() );
        System.out.println("\tlevel " + saveGamePlayerDataHolderClass.getSavePointLevel() );
        System.out.println("\tSavePointMarker: " + saveGamePlayerDataHolderClass.getSavePointMarker() );
        System.out.println("\tMainLife " + saveGamePlayerDataHolderClass.getSavePlayerMainLife() );
        System.out.println("\tHighScores " + saveGamePlayerDataHolderClass.getSaveHighScores() );
        System.out.println("\tPower In Use " + saveGamePlayerDataHolderClass.getSavePlayerPowerCrystalInUse() );

        System.out.println("\tPower Crystals ");
        System.out.println("\t\tGreen " + saveGamePlayerDataHolderClass.getPowerCrystalGreen() );
        System.out.println("\t\tBlack " + saveGamePlayerDataHolderClass.getPowerCrystalBlack() );
        System.out.println("\t\tBlue " + saveGamePlayerDataHolderClass.getPowerCrystalBlue() );
        System.out.println("\t\tRed " + saveGamePlayerDataHolderClass.getPowerCrystalRed() );

      System.out.println("Player Power's: " );

      for(int a = 0; a < saveGamePlayerDataHolderClass.getPlayerPowerUpList().size(); a++ ){

          System.out.println("\tName: " + saveGamePlayerDataHolderClass.getPlayerPowerUpList().get(a).getPowerName()
                  );
      }





    }

    /** Debug */
    public void readFromSaveGameWorld() {
        System.out.println("GameManagerAsset Class : Read from SaveGameWorld Class :");
        for(int a = 0; a < saveGameWorldDataHolderClass.getKnownWorldsList().size(); a++ ) {
            System.out.println("World: " +
                    saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectWorld() );


            for(int b = 0; b < saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().size(); b++ ) {
                System.out.println(" Level: " +
                saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getLevelObjectLevel() + " HaveBoss: " +
                        saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getLevelObjectHaveBoss() + " BossIsDead: " +
                        saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getLevelObjectBossDead());


                for (int d = 0; d < saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getKnownDoorSwitch().size(); d++ ){
                    System.out.println(" \tDoor inn World: " +
                            saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getKnownDoorSwitch().get(d).getDoorSwichInnWorld() +
                            " Level: " + saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getKnownDoorSwitch().get(d).getDoorSwichInnLevel() +
                            " Switch id: " + saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getKnownDoorSwitch().get(d).getDoorSwichId() +
                            " Door Status: " + saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getKnownDoorSwitch().get(d).getKeyDoorStatus() +
                            " Key Needed: " + saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getKnownDoorSwitch().get(d).getKeyTypeNeeded()
                    );
                }

                for(int c = 0; c < saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getItemSaveObjectsHolderClassArray().size(); c++ ){
                    System.out.println(" \tItem: " +
                            saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getItemSaveObjectsHolderClassArray().get(c).getItemName() +
                            " id: " + saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getItemSaveObjectsHolderClassArray().get(c).getItemID() +
                            " isUsed: " + saveGameWorldDataHolderClass.getKnownWorldsList().get(a).getWorldObjectListLevels().get(b).getItemSaveObjectsHolderClassArray().get(c).getItemUsed()
                    );
                }
            }
        }
/*
        for(int key = 0; key < saveGameWorldDataHolderClass.getKnownDoorSwitch().size(); key++){

            System.out.println("World Door's:");
            System.out.println("\t\tWorld " + saveGameWorldDataHolderClass.getKnownDoorSwitch().get(key).getDoorSwichInnWorld());
            System.out.println("\t\tLevel " + saveGameWorldDataHolderClass.getKnownDoorSwitch().get(key).getDoorSwichInnLevel());
            System.out.println("\t\tSwitch Id " + saveGameWorldDataHolderClass.getKnownDoorSwitch().get(key).getDoorSwichId());
            System.out.println("\t\tDoor Status " + saveGameWorldDataHolderClass.getKnownDoorSwitch().get(key).getKeyDoorStatus());
            System.out.println("\t\tKey Type Needed " + saveGameWorldDataHolderClass.getKnownDoorSwitch().get(key).getKeyTypeNeeded());
        }
*/
    }

    public void setGameState(GameState state) {
        if(!gameState.equals(state)) {
            this.preGameState = gameState;
            this.gameState = state;
        }
    }

    public GameState getGameState() { return this.gameState; }

    public GameState getPreGameState(){ return this.preGameState; }

    public void isLoadedJoyStickControllerButtonStyle() {

        //GameUtility.isAssetLoaded("controllerUi/hud.pack"); //) isLoaded("controllerUi/hud.pack", TextureAtlas.class);
        if( !GameUtility.assetManager.isLoaded("controllerUi/hud.pack", TextureAtlas.class) ) {
            GameUtility.assetManager.load("controllerUi/hud.pack", TextureAtlas.class);
            //System.out.println("Controller Loading inn..... to memory!!");
        }else{
            //System.out.println("Controller JoyStickControllerButtonStyle is loaded into memory");
        }

    }

    private void loadingGameMapScreenAssets(){
        GameUtility.assetManager.load("controllerUi/BubbleGameUI.atlas", TextureAtlas.class);
        GameUtility.assetManager.load("gameMapScreen/GameMapScreenAssets.atlas", TextureAtlas.class);

    }

    private void loadingJoyStickControllerButtonStyle() {
        GameUtility.assetManager.load("controllerUi/hud.pack", TextureAtlas.class);
        //Utility.assetManager.finishLoading();

    }

    public boolean isLoadedGameUIAssets(){
        if(!GameUtility.assetManager.isLoaded("gameUI/GameUI2.atlas", TextureAtlas.class)){

            GameUtility.loadTextureAtlas("gameUI/GameUI2.atlas");
            //GameUtility.assetManager.finishLoading();
            /**Asset Atlas had to be loaded*/
            return true;
        }else {
            /**Asset Atlas was loaded*/
            return true;
        }
    }

    public void loadingGameUIAssets() {
        //GameUtility.assetManager.load("gameUI/GameUI.atlas", TextureAtlas.class);
        GameUtility.loadTextureAtlas("gameUI/GameUI2.atlas");


    }

    public boolean isLoadedSplashScreenAsset(){

        if( !GameUtility.assetManager.isLoaded("img/Skull_Logo_Small.png", Texture.class) ) {

            //GameUtility.assetManager.load("img/Skull_Logo_Small.png", Texture.class);
            GameUtility.loadTextureAsset("img/Skull_Logo_Small.png");
            //GameUtility.assetManager.finishLoading();

            /**Asset had to be loaded*/
            return true;
        }else{
            /** Asset was loaded*/
            return true;
        }

    }


    public void loadingSplashScreenAsset() {
        //GameUtility.assetManager.load("img/Skull_Logo_Small.png", Texture.class);
        GameUtility.loadTextureAsset("img/Skull_Logo_Small.png");
        //Utility.assetManager.finishLoading();

    }
    /** Used with NameGame check */
    public  boolean isLoadedScreenMenuAsset(){
        if( !GameUtility.assetManager.isLoaded("ui/uiskin.atlas", TextureAtlas.class) ) {
            GameUtility.assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
            return true;
        }else {
            //GameUtility.assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
            return true;
        }
    }

    public void loadingScreenMenuAsset(){
        GameUtility.assetManager.load("ui/uiskin.atlas", TextureAtlas.class);

    }





    public void isLoadedPauseMenuDrawerAsset(){
        if(!GameUtility.assetManager.isLoaded("drawerUi/menu_ui.atlas", TextureAtlas.class))
            GameUtility.assetManager.load("drawerUi/menu_ui.atlas", TextureAtlas.class); // drawer Own atlas
        if(!GameUtility.assetManager.isLoaded("drawerUi/ButtonPauseUI.atlas", TextureAtlas.class))
            GameUtility.assetManager.load("drawerUi/ButtonPauseUI.atlas", TextureAtlas.class); // My only Pause atlas
    }


    public void loadingPauseMenuDrawerAsset() {
        GameUtility.assetManager.load("drawerUi/menu_ui.atlas", TextureAtlas.class); // drawer Own atlas
        GameUtility.assetManager.load("drawerUi/ButtonPauseUI.atlas", TextureAtlas.class); // My only Pause atlas
        //Utility.assetManager.finishLoading();

    }



    public void isLoadedGameAssetsGameObjects(){

        //GameItem Atlas
        if( !GameUtility.assetManager.isLoaded("spriteAtlas/gameObjects/ItemAtlas.atlas", TextureAtlas.class))
            GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas.atlas", TextureAtlas.class);
        if( !GameUtility.assetManager.isLoaded("spriteAtlas/gameObjects/ItemAtlas2.atlas", TextureAtlas.class))
            GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas2.atlas", TextureAtlas.class);
        if( !GameUtility.assetManager.isLoaded("spriteAtlas/gameObjects/ItemAtlas3.atlas", TextureAtlas.class))
            GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas3.atlas", TextureAtlas.class);
        if( !GameUtility.assetManager.isLoaded("spriteAtlas/gameObjects/ItemAtlas4.atlas", TextureAtlas.class))
            GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas4.atlas", TextureAtlas.class);
    }

    //ToDo:: this will be loaded per level on what enemy and maps we use!!! later!!

    public void debugGameAssetListPrint(){

        System.out.println("AssetList:\n");
        for( int q = 0; q < gameAssetsGameReqList.size; q++){

            System.out.println(gameAssetsGameReqList.get(q));
        }
    }
//ToDo: ReqAssetList . WorldReqAssetList01, WorldReqAssetList02 etc
    public void gameAssetsGameReqList(){
        //loadingScreenMenuAsset();
        gameAssetsGameReqList.add("ui/uiskin.atlas");

        //loadingSplashScreenAsset();
        gameAssetsGameReqList.add("img/Skull_Logo_Small.png");

        //loadingPauseMenuDrawerAsset();
        gameAssetsGameReqList.add("drawerUi/menu_ui.atlas");
        gameAssetsGameReqList.add("drawerUi/ButtonPauseUI.atlas");
        //loadingJoyStickControllerButtonStyle();
        gameAssetsGameReqList.add("controllerUi/hud.pack");
        //loadingGameUIAssets();
        gameAssetsGameReqList.add("gameUI/GameUI2.atlas");

        //loadingGameMapScreenAssets();
        gameAssetsGameReqList.add("controllerUi/BubbleGameUI.atlas");
        gameAssetsGameReqList.add("gameMapScreen/GameMapScreenAssets.atlas");

        //World 01
        gameAssetGameReqListWorld01.add("spriteAtlas/BubbleEnemyAtlas.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/BubbleEnemyAtlasHit.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/BubbleEnemyFlyingAtlas.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/enemyRegular/enemyWindUpRobotAtlas.atlas");

        gameAssetGameReqListWorld01.add("spriteAtlas/FallingStalg.atlas");

        gameAssetGameReqListWorld01.add("spriteAtlas/NewKnight.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/enemyBoss/SpringRobotBoss.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/NewKnightRunningRight.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/NewKnightRunningLeft.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/NewKnightHit.atlas");

        gameAssetGameReqListWorld01.add("spriteAtlas/player/BubblePlayerAtlas.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/player/BubblePlayerHitAtlas.atlas");

        // test !!
        gameAssetGameReqListWorld01.add("spriteAtlas/player/newPlayerX2Atlas.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/player/newPlayer2.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/player/newPlayer0.atlas");

        gameAssetGameReqListWorld01.add("spriteAtlas/player/powers/BubblePowerAtlas.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/player/powers/BubblePowerAtlas2.atlas");

        gameAssetGameReqListWorld01.add("spriteAtlas/gameObjects/ItemAtlas.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/gameObjects/ItemAtlas2.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/gameObjects/ItemAtlas3.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/gameObjects/ItemAtlas4.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/gameObjects/ItemAtlas5.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/gameObjects/ItemAtlas6.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/gameObjects/portalBlueAtlas.atlas");

        gameAssetGameReqListWorld01.add("spriteAtlas/items/ItemHeartAtlas.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/items/ItemPowerUpWJAtlas.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/items/Item_SkullAtlas.atlas");

        gameAssetGameReqListWorld01.add("spriteAtlas/items/ItemAnimationGrassA.atlas");

        //gameAssetGameReqListWorld01.add("spriteAtlas/items/ItemDragonEgg.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/items/Item_Amo.atlas");
        gameAssetGameReqListWorld01.add("spriteAtlas/items/ItemChest.atlas");

        gameAssetGameReqListWorld01.add("particleEffect/note.png");
        gameAssetGameReqListWorld01.add("note.png");


        //test Todo:: remove
        //debugGameAssetListPrint(); // prints list
    }

    public void loadingGameAssets(String world, String level) {

        System.out.println("[Debug AssetManager] LoadingGameAssets for World: " + world + " Level: " + level );

        switch (world){
            case "1":
                System.out.println("Asset for World 1 loading...");
                // SmallEnemyDef Atlas
                GameUtility.assetManager.load("spriteAtlas/BubbleEnemyAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/BubbleEnemyAtlasHit.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/BubbleEnemyFlyingAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/enemyRegular/enemyWindUpRobotAtlas.atlas", TextureAtlas.class);

                // MovingFallingEnemyDef Atlas
                GameUtility.assetManager.load("spriteAtlas/FallingStalg.atlas", TextureAtlas.class);


                // Boss Enemy's
                //GameUtility.assetManager.load("spriteAtlas/knight.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/NewKnight.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/enemyBoss/SpringRobotBoss.atlas", TextureAtlas.class);

                GameUtility.assetManager.load("spriteAtlas/NewKnightRunningRight.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/NewKnightRunningLeft.atlas", TextureAtlas.class);

                GameUtility.assetManager.load("spriteAtlas/NewKnightHit.atlas", TextureAtlas.class);
                //GameUtility.assetManager.load("spriteAtlas/SpringRobot.atlas", TextureAtlas.class); // deleted - to large


                // Player Atlas
                GameUtility.assetManager.load("spriteAtlas/player/BubblePlayerAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/player/BubblePlayerHitAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/player/newPlayer2.atlas", TextureAtlas.class);

                // test !!
                GameUtility.assetManager.load("spriteAtlas/player/newPlayerX2Atlas.atlas", TextureAtlas.class);
                //GameUtility.assetManager.load("spriteAtlas/DinoDragoon.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/player/newPlayer0.atlas", TextureAtlas.class);

                // Player Powers
                GameUtility.assetManager.load("spriteAtlas/player/powers/BubblePowerAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/player/powers/BubblePowerAtlas2.atlas", TextureAtlas.class);


                //GameObjects Atlas
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas2.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas3.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas4.atlas", TextureAtlas.class); // normal door
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas5.atlas", TextureAtlas.class); // large door
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas6.atlas", TextureAtlas.class); // medium door
                GameUtility.assetManager.load("spriteAtlas/gameObjects/portalBlueAtlas.atlas", TextureAtlas.class);

                //GameItems
                GameUtility.assetManager.load("spriteAtlas/items/ItemHeartAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/ItemPowerUpWJAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/Item_SkullAtlas.atlas", TextureAtlas.class);

                //GameUtility.assetManager.load("spriteAtlas/items/ItemDragonEgg.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/Item_Amo.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/ItemChest.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/ItemAnimationGrassA.atlas", TextureAtlas.class);

                GameUtility.assetManager.load("particleEffect/note.png", Texture.class);
                GameUtility.assetManager.load("note.png", Texture.class);
                break;
            case "2":
                System.out.println("World is 2");
                System.out.println("Asset for World 1 loading...");
                // SmallEnemyDef Atlas
                GameUtility.assetManager.load("spriteAtlas/BubbleEnemyAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/BubbleEnemyAtlasHit.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/BubbleEnemyFlyingAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/enemyRegular/enemyWindUpRobotAtlas.atlas", TextureAtlas.class);

                // MovingFallingEnemyDef Atlas
                GameUtility.assetManager.load("spriteAtlas/FallingStalg.atlas", TextureAtlas.class);


                // Boss Enemy's
                //GameUtility.assetManager.load("spriteAtlas/knight.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/NewKnight.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/enemyBoss/SpringRobotBoss.atlas", TextureAtlas.class);

                GameUtility.assetManager.load("spriteAtlas/NewKnightRunningRight.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/NewKnightRunningLeft.atlas", TextureAtlas.class);

                GameUtility.assetManager.load("spriteAtlas/NewKnightHit.atlas", TextureAtlas.class);
                //GameUtility.assetManager.load("spriteAtlas/SpringRobot.atlas", TextureAtlas.class); // deleted - to large


                // Player Atlas
                GameUtility.assetManager.load("spriteAtlas/player/BubblePlayerAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/player/BubblePlayerHitAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/player/newPlayer2.atlas", TextureAtlas.class);

                // test !!
                GameUtility.assetManager.load("spriteAtlas/player/newPlayerX2Atlas.atlas", TextureAtlas.class);
                //GameUtility.assetManager.load("spriteAtlas/DinoDragoon.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/player/newPlayer0.atlas", TextureAtlas.class);

                // Player Powers
                GameUtility.assetManager.load("spriteAtlas/player/powers/BubblePowerAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/player/powers/BubblePowerAtlas2.atlas", TextureAtlas.class);


                //GameObjects Atlas
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas2.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas3.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas4.atlas", TextureAtlas.class); // normal door
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas5.atlas", TextureAtlas.class); // large door
                GameUtility.assetManager.load("spriteAtlas/gameObjects/ItemAtlas6.atlas", TextureAtlas.class); // medium door
                GameUtility.assetManager.load("spriteAtlas/gameObjects/portalBlueAtlas.atlas", TextureAtlas.class);

                //GameItems
                GameUtility.assetManager.load("spriteAtlas/items/ItemHeartAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/ItemPowerUpWJAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/Item_SkullAtlas.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/ItemAnimationGrassA.atlas", TextureAtlas.class);

                //GameUtility.assetManager.load("spriteAtlas/items/ItemDragonEgg.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/Item_Amo.atlas", TextureAtlas.class);
                GameUtility.assetManager.load("spriteAtlas/items/ItemChest.atlas", TextureAtlas.class);


                GameUtility.assetManager.load("particleEffect/note.png", Texture.class);
                GameUtility.assetManager.load("note.png", Texture.class);
                break;
        }

        if(world.equals("1")) {
            for (int a = 0; a < gameAssetGameReqListWorld01.size; a++) {

                if (!GameUtility.assetManager.contains(gameAssetGameReqListWorld01.get(a))) {
                    System.out.println("[AssetLoaded for World 1 is Not Loaded!!]: " + gameAssetGameReqListWorld01.get(a));
                } else {
                    //System.out.println("[AssetLoaded]: " + gameAssetsGameReqList.get(a));
                }

                //System.out.println("gameAssetsGameReqList: " + gameAssetsGameReqList.size + " a: " + a);
                if ((a + 1) == gameAssetGameReqListWorld01.size) {
                    System.out.println("[All World 1 Asset is Loaded]");
                }
            }
        }





        /** Stationary Enemies */


        // moved to main map atlas
        //GameUtility.assetManager.load("spriteAtlas/enemyStationary/EnemySpikesAtlas.atlas", TextureAtlas.class);
        //GameUtility.assetManager.load("spriteAtlas/enemyStationary/EnemyVinesAtlas.atlas", TextureAtlas.class);






    }

    public void onPortalTravelHit(String travelType,
         String theMapW, String theMapL, String mapTravelW,String mapTravelL, String mapTravelDirection ){

        if(travelType.equals("WORLD")){
            worldMapChange = true;

            //System.out.println("GameManagerAsset Class - World travel to new worldMap: " + mapTravelTo);
            this.oldCurrentWorld = theMapW;
            this.oldCurrentLevel = theMapL;
            this.newCurrentWorld = mapTravelW;
            this.newCurrentLevel = mapTravelL;
            //this.mapTraveldDirection = mapTravelDirection;

        }else if(travelType.equals("LEVEL")){

            levelMapChange = true;

            this.oldCurrentWorld = theMapW;
            this.oldCurrentLevel = theMapL;

            this.newCurrentLevel = mapTravelL;
            this.newCurrentWorld = mapTravelW; // same world we left
            //this.mapTraveldDirection = mapTravelDirection;

            //System.out.println("Level Travel: curW: " + currentWorld + " NewW: " + newCurrentWorld +
            //        " curLev: " + currentLevel + " newL: " + newCurrentLevel);
            //System.out.println("GameManagerAsset Class - Level travel to new location: " + mapTravelTo);
            // get portal position
            // move player to that location
        }
    }

    // Format : BMapW1L1.tmx
    public void loadMapWorldAndLevel(String wor, String lev) {

        GameUtility.loadMapAsset("map/world0" + wor + "/BMapW" + wor + "L" + lev + ".tmx"); // mapFilenamePath

        map = GameUtility.getMapAsset("map/world0" + wor + "/BMapW" + wor + "L" + lev + ".tmx"); // mapFilenamePath
        //GameUtility.assetManager.finishLoading();
/*
        if(map.getProperties().containsKey("WORLD") &&
                map.getProperties().containsKey("LEVEL") &&
                map.getProperties().containsKey("SAVE POINT")){

            usingWorldMap = map.getProperties().get("WORLD").toString();
            usingLevelMap = map.getProperties().get("LEVEL").toString();
            haveUsingMapSavePoint = map.getProperties().get("SAVE POINT").toString();
        }
*/
    }

    //public String getUsingWorldMap(){return this.usingWorldMap;}
    //public String getUsingLevelMap(){return this.usingLevelMap;}
    //public String getHaveUsingMapSavePoint(){return this.haveUsingMapSavePoint;}

    public void setPortalTravelDirection(String value){ this.mapTraveldDirection = value;}
    public String getPortalTravelDirection(){ return this.mapTraveldDirection;}

    public void setMapTraveldSensorDirection(String value){this.mapTraveldSensorDirection = value;}
    public String getMapTraveldSensorDirection(){return this.mapTraveldSensorDirection; }


    public void unloadMapWorldAndLevel(String wor, String lev) {
        GameUtility.unloadAsset("map/world0" + wor + "/BMapW" + wor + "L" + lev + ".tmx");
    }

    public void loadMapFirstWorld(){

        unloadMapWorldAndLevel(this.currentWorld, this.currentLevel);
        loadMapWorldAndLevel("1","1");

        setNewCurrentWorld("1");
        setNewCurrentLevel("1");

        setCurrentWorld("1");
        setCurrentLevel("1");
    }

    public void loadMapNewWorld(String mapW, String mapL){
        unloadMapWorldAndLevel(this.currentWorld, this.currentLevel);
        loadMapWorldAndLevel(mapW, mapL);
        setOldCurrentWorld(this.currentWorld);
        setOldCurrentLevel(this.currentLevel);
        setCurrentWorld(mapW);
        setCurrentLevel(mapL);


        setNewCurrentWorld("0");
        setNewCurrentLevel("0");
    }

    public void loadMapNewLevel(){
        unloadMapWorldAndLevel(this.oldCurrentWorld, this.oldCurrentLevel);
        loadMapWorldAndLevel(this.newCurrentWorld, this.newCurrentLevel);


    }

    public void loadMapNewWorld(){ //String mapW, String mapL){

        unloadMapWorldAndLevel(this.oldCurrentWorld, this.oldCurrentLevel);

        // do this in onPortalTravelHit
        //setOldCurrentWorld(this.currentWorld);
        //setOldCurrentLevel(this.currentLevel);
/*
        System.out.println("GamaManagerAsset loadMapNewWorld() begin");
        System.out.println("GamaManagerAsset loadMapWorld - currentWorld: " + this.currentWorld );
        System.out.println("GamaManagerAsset loadMapLevel - currentLevel: " + this.currentLevel );

        System.out.println("GamaManagerAsset loadMapNewWorld - newCurrentWorld: " + this.newCurrentWorld );
        System.out.println("GamaManagerAsset loadMapNewLevel - newCurrentLevel: " + this.newCurrentLevel );

        System.out.println("GamaManagerAsset loadMap old - World: " + this.oldCurrentWorld );
        System.out.println("GamaManagerAsset loadMap old - Level: " + this.oldCurrentLevel );
        System.out.println("GamaManagerAsset loadMapNewWorld() end");
*/
        loadMapWorldAndLevel(this.newCurrentWorld, this.newCurrentLevel);  //"1");
        //setCurrentWorld(this.newCurrentWorld);
        //setCurrentLevel(this.newCurrentLevel);


        //setNewCurrentWorld(this.newCurrentWorld);
        //setNewCurrentLevel(this.newCurrentLevel); //"1");



    }



    public void setCurrentWorld(String value) { this.currentWorld = value;}
    public String getCurrentWorld(){ return this.currentWorld; }

    public void setCurrentLevel(String value) { this.currentLevel = value; }
    public String getCurrentLevel(){ return this.currentLevel; }



    public void setNewCurrentWorld(String value){ this.newCurrentWorld = value; }
    public String getNewCurrentWorld(){ return this.newCurrentWorld; }

    public void setNewCurrentLevel(String value) { this.newCurrentLevel = value; }
    public String getNewCurrentLevel(){ return this.newCurrentLevel; }


    public void setOldCurrentWorld(String value){ this.oldCurrentWorld = value; }
    public void setOldCurrentLevel(String value){ this.oldCurrentLevel = value; }

    public String getOldCurrentWorld(){ return  this.oldCurrentWorld; }
    public String getOldCurrentLevel(){ return  this.oldCurrentLevel; }

    public TiledMap getCurrentMap() { return this.map; }

    public String getPlayerCurrentActivePower(){


        if(this.getSaveGamePlayerDataHolderClass().getSavePlayerPowerCrystalInUse() != ""){
            return this.getSaveGamePlayerDataHolderClass().getSavePlayerPowerCrystalInUse();
        }else{
            return "1";
        }
        //return this.bubblePlayer.getPlayerActvieShootingPower(); player might not hava been made /- ups :)
    }

    public void listAssetLoadedDebug() {

        for (int i=0; i < GameUtility.assetManager.getAssetNames().size; i++) {
            System.out.println("AssetsList : " + GameUtility.assetManager.getAssetNames().get(i));
        }
    }

//*************************************SAVE GAME********************************************************

    public static boolean gameManagerSaveFilePlayerExists() {
        return Gdx.files.local("player.dat").exists();
    }

    public static boolean gameManagerSaveFileWorldExists() {
        return Gdx.files.local("world.dat").exists();
    }

        /** not in use - old */
    public void setSaveGamePlayerStatsGameOverSpawnLastSavePoint(){
/*
        Vector2 foPlayerPosition = new Vector2();
        String foSavePointWorld ="";
        String foSavePointLevel ="";
        String foSavePointMarker ="";
        float foSaveHighScores = 0;
*/
        if(gameManagerSaveFilePlayerExists()) {
            //System.out.println("GameManagerAssets Class SaveFile Exists!!" );
            //System.out.println("GameManagerAssets Class setSaveGamePlayerStatsWithoutPosition" );
            //readFromSaveGamePlayer();

            // get Last SavePoint and use this on position
            foPlayerPosition.set(saveGamePlayerDataHolderClass.getSavePointPosition());
            foSavePointWorld = saveGamePlayerDataHolderClass.getSavePointWorld();
            foSavePointLevel = saveGamePlayerDataHolderClass.getSavePointLevel();
            foSavePointMarker = saveGamePlayerDataHolderClass.getSavePointMarker();
            foSaveHighScores = saveGamePlayerDataHolderClass.getSaveHighScores();

            //foGreenPowerCrystal = saveGamePlayerDataHolderClass.getPowerCrystalGreen();
            //foBlackPowerCrystal = saveGamePlayerDataHolderClass.getPowerCrystalBlack();
            //foBluePowerCrystal = saveGamePlayerDataHolderClass.getPowerCrystalBlue();
            //foRedPowerCrystal = saveGamePlayerDataHolderClass.getPowerCrystalRed();

            //removeSaveGameFilePlayer();
            saveGamePlayerDataHolderClass.setSavePointPosition(foPlayerPosition);
            saveGamePlayerDataHolderClass.setSavePointLevel(foSavePointLevel);
            saveGamePlayerDataHolderClass.setSavePointMarker(foSavePointMarker);
            saveGamePlayerDataHolderClass.setSavePointWorld(foSavePointWorld);
            saveGamePlayerDataHolderClass.setSaveHighScores(foSaveHighScores);

            //saveGamePlayerDataHolderClass.setSavePointPowerCrystalGreen(foGreenPowerCrystal);
            //saveGamePlayerDataHolderClass.setSavePointPowerCrystalBlack(foBlackPowerCrystal);
            //saveGamePlayerDataHolderClass.setSavePointPowerCrystalBlue(foBluePowerCrystal);
            //saveGamePlayerDataHolderClass.setSavePointPowerCrystalRed(foRedPowerCrystal);

            if(this.bubblePlayer.updatePlayerLifeToHudAndSaveOnExit() != 0) {
                //System.out.println("GameManagerAsset Class setSaveGamePlayerStatsWithoutPosition ");
                //System.out.println(" player life !=0  ");
                saveGamePlayerDataHolderClass.setSavePlayerMainLife(this.bubblePlayer.updatePlayerLifeToHudAndSaveOnExit()); //this.foPlayerMainLife);
            }else{
                saveGamePlayerDataHolderClass.setSavePlayerMainLife(1); //this.foPlayerMainLife);
                //System.out.println("the player died we have set life to **1** ");
            }
            //gameManagerPlayerDataReadWriteSave();
        }

    }


    /** Set's the Save Game Holder Class between worlds and levels
     * - won't commit to save be for we actual hit a save point */
    public void setSaveGamePlayerStatsWithoutPosition(){

        if(gameManagerSaveFilePlayerExists()) {
            //System.out.println("GameManagerAssets Class SaveFile Exists!!" );
            //System.out.println("GameManagerAssets Class setSaveGamePlayerStatsWithoutPosition" );
            //readFromSaveGamePlayer();

            foPlayerPosition.set(saveGamePlayerDataHolderClass.getSavePointPosition());
            foSavePointWorld = saveGamePlayerDataHolderClass.getSavePointWorld();
            foSavePointLevel = saveGamePlayerDataHolderClass.getSavePointLevel();
            foSavePointMarker = saveGamePlayerDataHolderClass.getSavePointMarker();
            foSaveHighScores = saveGamePlayerDataHolderClass.getSaveHighScores();

            foSavePowerCrystalInUse = this.bubblePlayer.getPlayerActvieShootingPower();

            /** Players Power Pool Crystal's */
            foSaveGreenPowerCrystal = this.bubblePlayer.getBallooneBulletGreen();
            foSaveBlackPowerCrystal = this.bubblePlayer.getBallooneBulletBlack();
            foSaveBluePowerCrystal = this.bubblePlayer.getBallooneBulletBlue();
            foSaveRedPowerCrystal = this.bubblePlayer.getBallooneBulletRed();

            //removeSaveGameFilePlayer();
            saveGamePlayerDataHolderClass.setSavePointPosition(foPlayerPosition);
            saveGamePlayerDataHolderClass.setSavePointLevel(foSavePointLevel);
            saveGamePlayerDataHolderClass.setSavePointMarker(foSavePointMarker);
            saveGamePlayerDataHolderClass.setSavePointWorld(foSavePointWorld);
            saveGamePlayerDataHolderClass.setSaveHighScores(foSaveHighScores);

            saveGamePlayerDataHolderClass.setSavePlayerPoserCrystalInUse(foSavePowerCrystalInUse);

            /** Players Power Pool Crystal's */
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalGreen(foSaveGreenPowerCrystal);
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalBlack(foSaveBlackPowerCrystal);
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalBlue(foSaveBluePowerCrystal);
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalRed(foSaveRedPowerCrystal);


            if(this.bubblePlayer.updatePlayerLifeToHudAndSaveOnExit() != 0) {
                //System.out.println("GameManagerAsset Class setSaveGamePlayerStatsWithoutPosition ");
                //System.out.println(" player life !=0  ");
                saveGamePlayerDataHolderClass.setSavePlayerMainLife(this.bubblePlayer.updatePlayerLifeToHudAndSaveOnExit()); //this.foPlayerMainLife);
            }else{
                saveGamePlayerDataHolderClass.setSavePlayerMainLife(1); //this.foPlayerMainLife);
                //System.out.println("the player died we have set life to **1** ");
            }
            //gameManagerPlayerDataReadWriteSave();
        }

    }




    // ToDo : We must look at this again -: We delete the file and write it instead of overWrite it ???
    /** this is active on player hit Save Point */
    public void setDataToSaveGamePlayer(Vector2 position, String mapWorld, String mapLevel, String savepoint){

        if(gameManagerSaveFilePlayerExists()){
            this.foPlayerMainLife = bubblePlayer.getPlayerLife();
            removeSaveGameFilePlayer();
            saveGamePlayerDataHolderClass.setSavePlayerMainLife(this.foPlayerMainLife);
            saveGamePlayerDataHolderClass.setSavePointPosition(position);
            saveGamePlayerDataHolderClass.setSavePointLevel(mapLevel);
            saveGamePlayerDataHolderClass.setSavePointMarker(savepoint);
            saveGamePlayerDataHolderClass.setSavePointWorld(mapWorld);

            saveGamePlayerDataHolderClass.setSavePointPowerCrystalGreen(this.bubblePlayer.getBallooneBulletGreen());
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalBlack(this.bubblePlayer.getBallooneBulletBlack());
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalBlue(this.bubblePlayer.getBallooneBulletBlue());
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalRed(this.bubblePlayer.getBallooneBulletRed());

            saveGamePlayerDataHolderClass.setSavePlayerPoserCrystalInUse(this.bubblePlayer.getPlayerActvieShootingPower());

            gameManagerPlayerDataReadWriteSave();
        }else{
            //System.out.println("GameManagerAssets Class SaveFile Don't Exists!!" );
            saveGamePlayerDataHolderClass.setSavePointWorld(mapWorld);
            saveGamePlayerDataHolderClass.setSavePointLevel(mapLevel);
            saveGamePlayerDataHolderClass.setSavePointPosition(position);
            saveGamePlayerDataHolderClass.setSavePointMarker(savepoint);
            saveGamePlayerDataHolderClass.setSavePlayerMainLife(this.bubblePlayer.updatePlayerLifeToHudAndSaveOnExit());

            saveGamePlayerDataHolderClass.setSavePlayerPoserCrystalInUse(this.bubblePlayer.getPlayerActvieShootingPower());

            /** Not sure ?!! */
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalGreen(this.bubblePlayer.getBallooneBulletGreen());
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalBlack(this.bubblePlayer.getBallooneBulletBlack());
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalBlue(this.bubblePlayer.getBallooneBulletBlue());
            saveGamePlayerDataHolderClass.setSavePointPowerCrystalRed(this.bubblePlayer.getBallooneBulletRed());

            gameManagerPlayerDataReadWriteSave();
        }
    }


    /** PlayScreen  if SaveFileExists player is made */
    public boolean getWorldMapChange(){ return this.worldMapChange; }
    public void setWorldMapChange(boolean value){ this.worldMapChange = value; }

    public boolean getLevelMapChange(){ return this.levelMapChange; }
    public void setLevelMapChange(boolean value){ this.levelMapChange = value; }

    /** Reading saveGamePlayerDataHolderClass | Saving player.dat */
    public static void gameManagerPlayerDataReadWriteSave() {

        if(Gdx.files.local("player.dat").exists()) {

            try{
                saveGamePlayerDataHolderClass = readPlayerSaveGame();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("GameManagerAssets Class :-file Exists, reading to Player holderClass !! ");
        }else{
            try {
                savePlayer(saveGamePlayerDataHolderClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("GameManagerAssets Class :-file dose not Exists, saving player.dat !! ");
        }
    }

    public static void gameManagerWorldDataReadToWorldClassObject() {

        if (Gdx.files.local("world.dat").exists()) {

            try {
                saveGameWorldDataHolderClass = readWorldSaveGame();
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                //System.out.println("ClassNotFoundEx: " + e);
            } catch (IOException e) {
                //e.printStackTrace();
                //System.out.println("IOEx: " + e);
            }
            //System.out.println("GameManagerAssets Class :-file Exists, reading to World holderClass !! ");
        }
    }

    /** delete's world save, read Object SaveGame, save's a new file */
    public static void gameManagerWorldDataWriteToFile(){
        if(Gdx.files.local("world.dat").exists()) {

            Gdx.files.local("world.dat").delete();

            try {
                saveWorld(saveGameWorldDataHolderClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("GameManagerAssets Class :-file dose not Exists, saving to file world.dat !! ");
        }

    }
    /** read World Save file into World Object */
    public static void gameManagerWorldDataReadWriteSave() {

        if(Gdx.files.local("world.dat").exists()) {

            try{
                saveGameWorldDataHolderClass = readWorldSaveGame();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                //System.out.println("ClassNotFoundEx: " + e);
            } catch (IOException e) {
                e.printStackTrace();
                //System.out.println("IOEx: " + e);
            }
            //System.out.println("GameManagerAssets Class :-file Exists, reading to World holderClass !! ");
        }else{
            try {
                saveWorld(saveGameWorldDataHolderClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("GameManagerAssets Class :-file dose not Exists, saving to file world.dat !! ");
        }
    }

    /** delete's player save, read Object SaveGame, save's a new file */
    public static void gameManagerPlayerDataWriteToFile(){
        if(Gdx.files.local("player.dat").exists()) {

            Gdx.files.local("player.dat").delete();

            try {
                savePlayer(saveGamePlayerDataHolderClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("GameManagerAssets Class :-file dose not Exists, saving to file world.dat !! ");
        }

    }



    public void updateAllSavePointTexture(String point) {
        this.updatedSavePointUsed = point;
    }
    public String getUpdateAllSavePointTexture() {
      return   this.updatedSavePointUsed;
    }

    public static void savePlayer(SaveGamePlayerDataHolderClass saveGame) throws IOException {
        //FileHandle fileHandle = Gdx.files.local("player.dat");
        fileHandle = Gdx.files.local("player.dat");

        OutputStream out = null;

        try {
            fileHandle.writeBytes(serialize(saveGame), false);
        }catch(Exception ex){
            System.out.println(ex.toString());
        }finally {
            if(out != null) try{out.close();} catch (Exception ex){}
        }
    }

    public static void saveWorld(SaveGameWorldDataHolderClass saveGame) throws IOException {
        fileHandle = Gdx.files.local("world.dat");

        OutputStream out = null;

        try {
            fileHandle.writeBytes(serialize(saveGame), false);
        }catch(Exception ex){
            System.out.println(ex.toString());
        }finally {
            if(out != null) try{out.close();} catch (Exception ex){}
        }
    }


    public static SaveGamePlayerDataHolderClass readPlayerSaveGame() throws IOException, ClassNotFoundException {
        SaveGamePlayerDataHolderClass saveGamePlayer = null;

        //FileHandle fileHandle = Gdx.files.local("player.dat");
        fileHandle = Gdx.files.local("player.dat");
        saveGamePlayer = (SaveGamePlayerDataHolderClass) deserialize(fileHandle.readBytes());

        return saveGamePlayer;
    }

    public boolean removeSaveGameFilePlayer(){

        Gdx.files.local("player.dat").delete();

        return true;
    }

    public static SaveGameWorldDataHolderClass readWorldSaveGame() throws IOException, ClassNotFoundException {
        SaveGameWorldDataHolderClass saveGameWorld = null;

        fileHandle = Gdx.files.local("world.dat");
        saveGameWorld = (SaveGameWorldDataHolderClass) deserialize(fileHandle.readBytes());

        return saveGameWorld;
    }

    public boolean removeSaveGameFileWorld(){

        Gdx.files.local("world.dat").delete();

        return true;
    }

    public void clearSaveGameObjectWorld(){
        saveGameWorldDataHolderClass = null;
        saveGameWorldDataHolderClass = new SaveGameWorldDataHolderClass();
        saveGameWorldDataHolderClass.init();
    }

    public void clearSaveGameObjectPlayer(){
        saveGamePlayerDataHolderClass = null;
        saveGamePlayerDataHolderClass = new SaveGamePlayerDataHolderClass();
        saveGamePlayerDataHolderClass.init();
    }

    @SuppressWarnings("unused")
    private static byte[] serialize(Object obj) throws IOException {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);

        return o.readObject();
    }


    public SaveGamePlayerDataHolderClass getSaveGamePlayerDataHolderClass(){ return this.saveGamePlayerDataHolderClass; }

    public SaveGameWorldDataHolderClass getSaveGameWorldDataHolderClass(){ return this.saveGameWorldDataHolderClass;}


    public String getSaveGameDataHolderClassSavePoint(){

        return this.saveGamePlayerDataHolderClass.getSavePointMarker();
    }

    public void disposeGameIsRunning() {

        this.gameIsRunning = false;
    }


    public void setMapSpawnStartPosition(Vector2 pos){ playerSpawnStartPosition = pos; }
    public Vector2 getMapSpawnStartPosition() { return this.playerSpawnStartPosition; }
}
