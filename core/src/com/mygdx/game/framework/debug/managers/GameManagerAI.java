package com.mygdx.game.framework.debug.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.controllers.ControllerJoyStickButtonStyle;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.BossEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyB;
import com.mygdx.game.framework.debug.sprites.Enemies.MovingFallingEnemy.EnemyStalactite;
import com.mygdx.game.framework.debug.sprites.Enemies.MovingFallingEnemy.MovingFallingEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.SmallEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyA;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.EnemyKnightDevil;
import com.mygdx.game.framework.debug.sprites.items.DragonEggGameItem;
import com.mygdx.game.framework.debug.sprites.items.ExtraLifeGameItem;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.B2WorldCreator;
import com.mygdx.game.framework.debug.world.gameAiObjects.GameAIObject;

import java.util.Random;

import com.badlogic.gdx.ai.DefaultTimepiece;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectSwitchHidden;
import com.mygdx.game.framework.debug.world.gameObjects.ItemObjectDef;

public class GameManagerAI {

    private enum EnemyAIBossState {
        SET_PREVIOUS_STATE,
        SET_INACTIVE_STATE,
        FALLING,
        GUARDING,
        FOLLOWING,
        FIGHTING_CLOSE,
        FIGHTING_RANGE,
        JUMPING,
        HIT,
        DEAD,
    }

    private EnemyAIBossState currentEnemyAIState, prevEnemyAIState;
    private GameSteeringStateManagerBoss steeringStateManager;
    Array<SmallEnemyDef> gameEnemyList; // Main EnemyList

    Array<BossEnemyDef> gameEnemyBossList; // Main Boss EnemyList


    Array<EnemyKnightDevil> knightEnemyList = new Array<EnemyKnightDevil>();
    Array<EnemyA> enemyAEnemyList = new Array<EnemyA>();
    //Array<GameAIObject> gameMarkerBaseAIList = new Array<GameAIObject>();
    Array<GameAIObject> gameAIObjects = new Array<GameAIObject>();
    Array<GameAIObject> gameAIBossHiddenMarkers = new Array<GameAIObject>();

    Array<GameSteeringStateManagerBoss> gameSteeringStateManagerBossArray = new Array<GameSteeringStateManagerBoss>();
    Array<GameSteeringStateManagerEnemy> gameSteeringStateManagerEnemyArray = new Array<GameSteeringStateManagerEnemy>();

    Array<BossEnemyDef> enemyObjectsBossList = new Array<BossEnemyDef>();
    Array<SmallEnemyDef> enemyObjectsEnemyA = new Array<SmallEnemyDef>();
    Array<SmallEnemyDef> enemyObjectsEnemyB = new Array<SmallEnemyDef>();


    BubblePlayer gamePlayer;
    NameGame g;
    PlayScreen gameScreen;
    Vector2 collision = new Vector2(), normal = new Vector2();
    float dist;
    float jumpTimerTest;
    float enemyBossCombatTimer;

    //testing AI - timer boss Knight

    DefaultTimepiece defaultTimepiece;

    //DefaultTimepiece timerAI_OuterLoop_JumpSeqtimer;
    //DefaultTimepiece timerAI_InnerLoop_JumpSeqtimer;


    GameManagerAITimer timerAI_OuterLoop_enemyBossJumpFunction;
    GameManagerAITimer timerAI_InnerLoop_enemyBossJumpFunction;


    GameManagerAITimer TimerAI; // in use Main  Ai

    GameManagerAITimer timerAI_OuterLoop_RangeAttack;
    GameManagerAITimer timerAI_InnerLoop_RangeAttack;






    GameManagerAITimer gameRandomSequensTimer;

    GameManagerAITimer TimerAI_KnightBoss;

    GameManagerAITimer TimerAI_RangeAttack;

    GameManagerAITimer TimerAI_CloseAttack;

    GameManagerAITimer TimerAI_Steering_Dash; // inn use
    GameManagerAITimer TimerAI_Steering_Follow;

    GameManagerAITimer TimerAI_Jump;

    private boolean sequenceBoolDone;
    private boolean dashMoveBool;
    private boolean bossJumpBool;

    private boolean bossJumpDoneBool;
    private boolean bossJumpDoneBool2;
    private boolean bossJumpFollowPlayerStopAllActivity;
    private boolean bossJumpSingleRepeatStopAllActivity;

    private boolean steeringFollowRangeAttack;
    private boolean steeringFollowCloseAttack;

    private boolean steeringStateManagerBossBoolean;
    private boolean steeringStateManagerEnemyB_boolean;

    private boolean steeringEnemyB_Start_From_ContactL;
    private boolean steeringEnemyB_Stop_From_ContactL;

    private boolean bossKnightActiveDirection;
    private Array<GameAIObject> dashAImarkerList;
    private Array<GameAIObject> jumpAImarkerList;
    private Array<GameAIObject> travelAImarkerList;

    private Array<ItemObjectDef> gameAI_ItemGameObjects;

    //private Array<GameAIObject> reversEnemyVelocityList;
    private int maxLifeLostOnHitGameManagerAi;
    private String worldBoss;

    //private  B2WorldCreator b2WorldCreator_AI;

    // testing
    Array<ExtraLifeGameItem> extraLifeSpawnfromdead;
    Array<DragonEggGameItem> dragonEggGameItemArray;

    private Array<MovingFallingEnemyDef> gameMovingFallingEnemyList;

    B2WorldCreator b2WorldCreator;

    private GameManagerAssets gameManagerAssetsInstance;
    private World world;
    private ControllerJoyStickButtonStyle playerController;
    private OrthographicCamera gameCammera;
    //private Map tiledMap;
    private MapProperties mapProps;
    private int tiledMap_width;// = 0;
    private int tiledMap_height;// = 0;

    public GameManagerAI(NameGame gameName, BubblePlayer bubble, B2WorldCreator b2WC, String mapW, GameManagerAssets instance, World w, ControllerJoyStickButtonStyle controller, OrthographicCamera cam, Map map) {

        this.gameManagerAssetsInstance = instance;
        this.world = w;
        this.playerController = controller;
        this.gameCammera = cam;
        this.mapProps = map.getProperties();

        this.tiledMap_width = mapProps.get("width", Integer.class);
        this.tiledMap_height = mapProps.get("height", Integer.class);

        //this.b2WorldCreator_AI = b2WorldCreator;
        this.gameEnemyList = b2WC.getEnemies();
        this.gameEnemyBossList = b2WC.getEnemiesBoss();
        splitBossEnemyList(gameEnemyBossList);

        this.gameAIObjects = b2WC.getGameAISteeringObjects();
        this.gameAIBossHiddenMarkers = b2WC.getGameAIBossSpawnObjects();

        this.gameAI_ItemGameObjects = b2WC.getItemGameObjects();

        this.jumpAImarkerList = new Array<GameAIObject>();
        this.dashAImarkerList = new Array<GameAIObject>();
        this.travelAImarkerList = new Array<GameAIObject>();

        extraLifeSpawnfromdead = new Array<ExtraLifeGameItem>();
        dragonEggGameItemArray = new Array<DragonEggGameItem>();

        this.gameMovingFallingEnemyList = new Array<MovingFallingEnemyDef>();
        this.gameMovingFallingEnemyList = b2WC.getEnemyMovingFalling();

        this.b2WorldCreator = b2WC;

        /** Start Var only have to set it all so with inn Init() */



        this.worldBoss = mapW;
        //this.reversEnemyVelocityList = b2WorldCreator.getGameAIEnemyAReversObjects();

        this.gamePlayer = bubble;
        this.g = gameName;

                /** INN USE refactoring */


        /** EnemyKnightDevil's Var's  the more boss's i make, have to make changes or separately Var's*/

                /** inner Var's to make more jump's*/

        bossKnightActiveDirection = false;

        bossJumpDoneBool = false;
        bossJumpDoneBool2 = false;

        /** Stops Jumps Looping Activity - So steering wil work */
        bossJumpFollowPlayerStopAllActivity = false;
        bossJumpSingleRepeatStopAllActivity = false;

        /** After Jump, we set this setRangeAttackFollowingPlayer( getRangeAttackFollowingPlayerBool() )
         *  false be for jump, jump -> sets BossAutoDirection true, that again will sett this to true.
         * */
        steeringFollowRangeAttack = false;
        steeringFollowCloseAttack = false;

        // EnemyB steering start / stop
        this.steeringEnemyB_Start_From_ContactL = false;
        this.steeringEnemyB_Stop_From_ContactL = false;

        this.maxLifeLostOnHitGameManagerAi = 1;

        /** Main AI Timer */
        TimerAI = new GameManagerAITimer();

        // new
        timerAI_OuterLoop_enemyBossJumpFunction = new GameManagerAITimer();
        timerAI_InnerLoop_enemyBossJumpFunction = new GameManagerAITimer();

        // old
        TimerAI_Steering_Dash = new GameManagerAITimer();
        TimerAI_RangeAttack = new GameManagerAITimer();

        timerAI_OuterLoop_RangeAttack = new GameManagerAITimer();
        timerAI_InnerLoop_RangeAttack = new GameManagerAITimer();

        /**  End refactoring  */

        gameRandomSequensTimer = new GameManagerAITimer();

        defaultTimepiece = new DefaultTimepiece();

        //timerAI_OuterLoop_JumpSeqtimer = new DefaultTimepiece();
        //timerAI_InnerLoop_JumpSeqtimer = new DefaultTimepiece();




        //TimerAI_KnightBoss = new GameManagerAITimer();

        TimerAI_CloseAttack = new GameManagerAITimer();
        //TimerAI_Jump = new GameManagerAITimer();

        TimerAI_Steering_Follow = new GameManagerAITimer();

        jumpTimerTest = 0;


        init();
    }



    private void splitEnemyList(Array<SmallEnemyDef> enemyList) {

        //System.out.println("Size of list !! " +  enemyList.size );
        for (SmallEnemyDef e : enemyList ) {

            if(e.getClass().getSimpleName().equals("EnemyA")) {
                //this.enemyAEnemyList.add((EnemyA) e);
                this.enemyObjectsEnemyA.add(e);
                //System.out.println("Added one to the EnemyA list");
            }
            if(e.getClass().getSimpleName().equals("EnemyB")) {
                //this.enemyAEnemyList.add((EnemyA) e);
                this.enemyObjectsEnemyB.add(e);
                //System.out.println("Added one to the EnemyA list");
            }
        }
    }

    /** if we have two boss's on the same lev ? */
    private void splitBossEnemyList(Array<BossEnemyDef> enemyList) {

        //System.out.println("Size of list !! " +  enemyList.size );
        for (BossEnemyDef e : enemyList ) {

            // Boss
            if(e.getClass().getSimpleName().equals("EnemyKnightDevil")) {
                //this.knightEnemyList.add((EnemyKnightDevil) e);
                this.enemyObjectsBossList.add(e);
                //System.out.println("Added one to the knight list");
            }
        }
    }




    private void splitAIObjectsList(Array<GameAIObject> aiObList) {
        for(GameAIObject a : aiObList ){

            // this will only hold - AI_STEERING - Dash is only one for now!!!
            if(a.getClass().getSimpleName().equals("GameAIObject")) {
                //System.out.println("yes: " + a.getMapMarkerType());
                if( a.getMapMarkerType().equals("TRAVEL") ){
                    //this.travelAImarkerList.addAll(a);
                }

                if(a.getMapMarkerType().equals("Dash")){ // DASH
                    this.dashAImarkerList.addAll(a);
                }

                if( a.getMapMarkerType().equals("JUMP")){
                    //this.jumpAImarkerList.addAll(a);
                }
            }
        }
    }

    public void setMaxLifeLostOnHit(int value){this.maxLifeLostOnHitGameManagerAi = value; }
    public int getMaxLifeLostOnHit(){ return this.maxLifeLostOnHitGameManagerAi; }

    /** Later */
    public void setPlayerStatsFromDifficulty(){
    }

    public void init(){
        System.out.println("GameManagerAI init()" );

        splitEnemyList(this.gameEnemyList);
        splitAIObjectsList(this.gameAIObjects);


        System.out.println("GameSteeringStateManagerAi init Test: SmallEnemyDef List Knight Size: " + this.enemyObjectsBossList.size);
        System.out.println("GameSteeringStateManagerAi init Test: SmallEnemyDef List EnemyA Size: " + this.enemyObjectsEnemyA.size);
        System.out.println("GameSteeringStateManagerAi init Test: SmallEnemyDef List EnemyB Size: " + this.enemyObjectsEnemyB.size);
        System.out.println("GameSteeringStateManagerAi init Test: FallingEnemy Stag's List Size: " + this.gameMovingFallingEnemyList.size );
        System.out.println("GameSteeringStateManagerBoss init Test: AIObjects List All Size: " + this.gameAIObjects.size);
        System.out.println("GameSteeringStateManagerBoss init Test: AIObjects List Dash Size: " + this.dashAImarkerList.size);
        System.out.println("GameSteeringStateManagerBoss init Test: AIObjects List Jump Size: " + this.jumpAImarkerList.size);
        System.out.println("GameSteeringStateManagerBoss init Test: AIObjects List Travel Size: " + this.travelAImarkerList.size);
        System.out.println("GameSteeringStateManagerBoss init Test: gameSteeringStateManagerBossArray Size: " + this.gameSteeringStateManagerBossArray.size);
        System.out.println("GameSteeringStateManagerEnemy init Test: gameSteeringStateManagerEnemyArray Size: " + this.gameSteeringStateManagerEnemyArray.size);


        //System.out.println("Boss life is: " + ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyLife());
        System.out.println("SteeringStateManager Boss Boolean is: " + getSteeringStateManagerBossBoolean() );
        System.out.println("SteeringStateManager EnemyB Boolean is: " + getSteeringStateManagerEnemyB_boolean() );


        //System.out.println("Init(): -World Boss is: " + this.worldBoss + " CurrentWorld: "
        // + GameManagerAssets.instance.getCurrentWorld() + " NewCurrentWorld: "
        // + GameManagerAssets.instance.getNewCurrentWorld());

        // change to witch boss we gone face!!! etc, now only knightBoss
        setSteeringStateManagerBossBoolean(true);
        setBossKnightActiveDirectionBoolean(false);

        setSteeringStateManagerEnemyB_boolean(true);

        setDashMoveBool(true);
        setBossJumpBool(true);

        //this.prevEnemyAIState = this.currentEnemyAIState = EnemyAIBossState.SET_INACTIVE_STATE;
        //System.out.println("GameSteeringStateManagerBoss init Test: Marker List Size: " + this.gameMarkerBaseAIList.size);

        // Add it to a SteeringStatManager Class
        // just make one !!!
        //gameSteeringStateManagerBossArray.add(
        //        new GameSteeringStateManagerBoss(this.g, this.gamePlayer, this.enemyObjectsBossList, this.gameAIObjects)); //, this.gameMarkerBaseAIList, true));
//System.out.println("GameSteeringStateManagerBoss init Test: SteeringStateManagerArray Size: " + gameSteeringStateManagerBossArray.size );
    }

    public void setEnemyAIState(EnemyAIBossState state) {
        // So current state and pre state is the same!!
        if(!currentEnemyAIState.equals(state)) {
            this.prevEnemyAIState = currentEnemyAIState;
            this.currentEnemyAIState = state; // new state, but be for we set a new the old must be sett into history
        }
    }

    public EnemyAIBossState getCurrentEnemyAIState(){return this.currentEnemyAIState; }

    /** Boss's */
    private void setSteeringStateManagerBossBoolean(boolean value){ this.steeringStateManagerBossBoolean = value; }
    private boolean getSteeringStateManagerBossBoolean(){ return this.steeringStateManagerBossBoolean; }

    /** EnemyB's */
    private void setSteeringStateManagerEnemyB_boolean(boolean value){ this.steeringStateManagerEnemyB_boolean = value; }
    private boolean getSteeringStateManagerEnemyB_boolean(){ return this.steeringStateManagerEnemyB_boolean; }


    /** set Boss steering to run */
    private void setSteeringStateManagerBoss(boolean run){

        if(run) {
            if (!(gameSteeringStateManagerBossArray.size > 0)) {
                gameSteeringStateManagerBossArray.add(
                        new GameSteeringStateManagerBoss(this.g, this.gamePlayer, this.enemyObjectsBossList, this.gameAIObjects));
            }
        }
    }

    /** set Enemy B steering to run */
    private void setSteeringStateManagerEnemy(boolean run){

        if(run) {
            if (!(gameSteeringStateManagerEnemyArray.size > 0)) {
                gameSteeringStateManagerEnemyArray.add(
                        new GameSteeringStateManagerEnemy(this.g, this.gamePlayer, this.enemyObjectsEnemyB, this.gameAIObjects));

                //System.out.println("GameManagerAi steering enemy B active and sendt ");
            }
        }
    }

    // .compareTo("null")
    private void lookUpSteeringStateManagerHasState(){

        if ( gameSteeringStateManagerBossArray.size > 0) {
            if (gameSteeringStateManagerBossArray.get(0).hasState().equals("null")) {
                //System.out.println("State: (null)" + gameSteeringStateManagerBossArray.get(0).hasState());
            } else {
                System.out.println("State: " + gameSteeringStateManagerBossArray.get(0).hasState());
            }
        }
    }

    private void stopSteeringStateManagerBossKnight(boolean stop){
        if(stop){
            if( !(gameSteeringStateManagerBossArray.size == 0)) {
                this.gameSteeringStateManagerBossArray.get(0).dispose();
                this.gameSteeringStateManagerBossArray.removeIndex(0);
            }
        }
    }

    private void stopSteeringStateManagerEnemy(boolean stop){
        if(stop){
            if( !(gameSteeringStateManagerEnemyArray.size == 0)) {
                this.gameSteeringStateManagerEnemyArray.get(0).dispose();
                this.gameSteeringStateManagerEnemyArray.removeIndex(0);
            }
        }
    }


    /** Set's the boss steering State Follow Player, AI-Marker (speed, range) */
    private void startSteeringStateManagerBossKnight(boolean run, int whatToFollow, int rangeToFollow, int aiBodyToUse){

        if(run){
            if( !(gameSteeringStateManagerBossArray.size == 0) && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() ) {

                if( ((EnemyKnightDevil)this.enemyObjectsBossList.get(0)).isDead() || gamePlayer.isDead()) {

                    this.gameSteeringStateManagerBossArray.get(0).dispose();
                    this.gameSteeringStateManagerBossArray.removeIndex(0);

                }else if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround() && gameSteeringStateManagerBossArray.size > 0 ) {

                switch (whatToFollow){
                    case 0:
                        //System.out.println("Sett's Follow player, with range (PURSUE) LONG, SHORT");
                        switch (rangeToFollow){
                            case 0:
                                /** Speed, Radius, AccelerationMax, bodyToUse ( not in use here ) */
                                gameSteeringStateManagerBossArray.get(0).setState(GameSteeringStateManagerBoss.State.PURSUE, 50, 45, 50, 0); // long range, MaxLinearAcceleration 50 not use
                                //System.out.println("GameManagerAI Class - Running State Pursue long");
                                break;
                            case 1:
                                /** Speed, Radius, AccelerationMax, bodyToUse ( not in use here ) */
                                gameSteeringStateManagerBossArray.get(0).setState(GameSteeringStateManagerBoss.State.PURSUE, 50, 20, 50, 0); // close range
                                //System.out.println("GameManagerAI Class - Running State Pursue short");
                                break;
                        }
                        break;
                    case 1:
                        //System.out.println("Sett's Follow Ai marker");
                        switch (rangeToFollow){
                            case 0:
                                /** Speed, Radius, AccelerationMax, bodyToUse  */
                                gameSteeringStateManagerBossArray.get(0).setState(GameSteeringStateManagerBoss.State.HOMESTATE, 50, 20, 50, aiBodyToUse); // move
                                //System.out.println("GameManagerAI Class - Running State Home short normal speed");
                                break;
                            case 1:
                                /** Speed, Radius, AccelerationMax, bodyToUse  */
                                gameSteeringStateManagerBossArray.get(0).setState(GameSteeringStateManagerBoss.State.HOMESTATE, 50, 15, 200, aiBodyToUse); // dash
                                //System.out.println("GameManagerAI Class - Running State Home short dash speed");
                                break;
                        }
                        break;
                    }

                }
            }
        }
    }
    /** Steering for Small enemies */
    private void startSteeringStateManagerSmallEnemy(boolean run, int whatToFollow, int rangeToFollow, int aiBodyToUse){

        if(run){

            if( !(gameSteeringStateManagerEnemyArray.size == 0) && ((EnemyB) enemyObjectsEnemyB.get(0)).b2body.isActive() ) {

                if( ((EnemyB) enemyObjectsEnemyB.get(0)).isDead() || gamePlayer.isDead()) {

                    this.gameSteeringStateManagerEnemyArray.get(0).dispose();
                    this.gameSteeringStateManagerEnemyArray.removeIndex(0);

                }else{

                    switch (whatToFollow){
                        case 0:
                            //System.out.println("Sett's Follow player, with range (PURSUE) LONG, SHORT");
                            switch (rangeToFollow){
                                case 0:
                                    // /** Speed, Radius, AccelerationMax, bodyToUse ( not in use here )
                                    gameSteeringStateManagerEnemyArray.get(0).setState(GameSteeringStateManagerEnemy.State.PURSUE, 50, 45, 50, 0); // long range, MaxLinearAcceleration 50 not use
                                    //gameSteeringStateManagerEnemyArray.get(0).setState(GameSteeringStateManagerEnemy.State.WANDER, 50, 20, 50, 0); // long range, MaxLinearAcceleration 50 not use
                                   // System.out.println("GameManagerAI Class - Running State Pursue long");
                                    break;
                                case 1:
                                    // Speed, Radius, AccelerationMax, bodyToUse ( not in use here )
                                    //gameSteeringStateManagerEnemyArray.get(0).setState(GameSteeringStateManagerEnemy.State.PURSUE, 50, 20, 50, 0); // close range
                                    //System.out.println("GameManagerAI Class - Running State Pursue short");
                                    break;
                            }
                            break;
                        case 1:
                            //System.out.println("Sett's Follow Ai marker");
                            switch (rangeToFollow){
                                case 0:
                                    // Speed, Radius, AccelerationMax, bodyToUse   //50, 20, 50, aiBodyToUse); // move
                                    //gameSteeringStateManagerEnemyArray.get(0).setState(GameSteeringStateManagerEnemy.State.HOMESTATE, 10, 10, 50, aiBodyToUse); // move
                                    //System.out.println("GameManagerAI Class - Running State Home short normal speed");
                                    break;
                                case 1:
                                    // Speed, Radius, AccelerationMax, bodyToUse
                                    //gameSteeringStateManagerEnemyArray.get(0).setState(GameSteeringStateManagerEnemy.State.WANDER, 50, 15, 200, aiBodyToUse); // dash
                                    //System.out.println("GameManagerAI Class - Running State Home short dash speed");
                                    break;
                            }
                            break;
                    }

                }


            }

        }
    }

    /** SmallEnemyDef Boss Always facing Player -Set When No steering Stat is set.
        Called inn setBossKnightActive
     * */
    private void setBossKnightAlwaysFacingPlayer(boolean value){

        //*******************ALL WAYS FACING THE PLAYER************************

        if( gameSteeringStateManagerBossArray.size == 0 && value ) {
            //System.out.println("GameManagerAI Class - Setting SmallEnemyDef Boss, All ways facing Player Value = true; ");

            if(enemyObjectsBossList.get(0).b2body.isActive() &&
                    gamePlayer.getX() < enemyObjectsBossList.get(0).b2body.getPosition().x &&
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight() ) {

                //if( ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getIsRunningRight() )
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(false);

            }else if(enemyObjectsBossList.get(0).b2body.isActive() && gamePlayer.getX() > enemyObjectsBossList.get(0).b2body.getPosition().x ) {

                //if( !((EnemyKnightDevil)enemyObjectsBossList.get(0)).getIsRunningRight() )
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(true);
            }
        }

    }

    private void setBossKnightActive(float dt, boolean activeFacing) { //, boolean activeFacing){

        for(int i = 0; i < enemyObjectsBossList.size; i++){

            enemyObjectsBossList.get(i).update(dt);

            if( enemyObjectsBossList.get(i).getX() < gamePlayer.getX() + 300 / GameUtility.PPM  &&
                    gameAIBossHiddenMarkers.get(i).getHitBossBoolean()  ){

                ((EnemyKnightDevil) enemyObjectsBossList.get(i)).b2body.setActive(true);
                //setBossKnightAlwaysFacingPlayer(true, i);

               if( !activeFacing ) { //&& ((EnemyKnightDevil)enemyObjectsBossList.get(i)).isHit() || !(((EnemyKnightDevil)enemyObjectsBossList.get(i)).getEnemyKnightOnGround())  ){
                   setBossKnightAlwaysFacingPlayer(false);
                   //System.out.println("GameManagerAI Class - facing Player - false");
               }else {
                   setBossKnightAlwaysFacingPlayer(true);
                   //System.out.println("GameManagerAI Class - facing Player - true");
               }
            }
        }

    }

/*
updateAllSpawnLifeFromEnemy = new EnemyA(
                            ((EnemyA) enemyObjectsEnemyA.get(j)).getEnemyPlayScreen(),
                            ((EnemyA) enemyObjectsEnemyA.get(j)).getX(),
                            ((EnemyA) enemyObjectsEnemyA.get(j)).getY(),
                            ((EnemyA) enemyObjectsEnemyA.get(j)).getEnemyMapObject());
                    //updateAllSpawnLifeFromEnemy = ((EnemyA) enemyObjectsEnemyA.get(j));
 */

//jobber her!!!
/** -failSafe_On_Active- = when player was jumping away from falling stalg and was then to far away from it
    *  the game object was no longer active, but should have been. so made a fail safe trigger!!!
    */
    boolean failSafe_On_Active = false;
    float graphicTimer = 0;
    private void setEnemyMovingFallingActive(float dt) {


        /** Moving Falling Enemy Stalgmites if one comes from othere direction cant have X < X then it must be X > X !!!
         *
         *  We can have Y if we want some to fall when we jump !!!
         *
         */
        for(int i=0; i < gameMovingFallingEnemyList.size; i++ ) {

            gameMovingFallingEnemyList.get(i).update(dt);

            try {
//System.out.println("failSafe is " + failSafe_On_Active);

                float stalgShouldFall = ( gameMovingFallingEnemyList.get(i).getY() - (gamePlayer.getY() + 100 / GameUtility.PPM));
/*
                if(stalgShouldFall > 2.0f){
                    System.out.println("stalgShouldFall > 2 ? " + stalgShouldFall);
                }else{
                    System.out.println("stalgShouldFall < 2 ? " + stalgShouldFall);
                }
*/
                if( gameMovingFallingEnemyList.get(i).getX() < gamePlayer.getX() + 100 / GameUtility.PPM
                        && gameMovingFallingEnemyList.get(i).getX() > gamePlayer.getX() - 100 / GameUtility.PPM
                        && stalgShouldFall < 2f ) {

                    //System.out.println("stalg Y " + gameMovingFallingEnemyList.get(i).getY() );
                    //System.out.println("player Y " + (gamePlayer.getY() + 100 / GameUtility.PPM) );

                    if( !((EnemyStalactite)gameMovingFallingEnemyList.get(i)).getIsDestroyed() ) {
                        ((EnemyStalactite) gameMovingFallingEnemyList.get(i)).b2body.setActive(true);
                        //failSafe_On_Active = true;
                    }
                }


                if(((EnemyStalactite) gameMovingFallingEnemyList.get(i)).isDead()) {


                    ((EnemyStalactite) gameMovingFallingEnemyList.get(i)).setdeath_Gapich_Timer_Active();

                }

                if(((EnemyStalactite) gameMovingFallingEnemyList.get(i)).getIsDestroyed() ){

                    gameMovingFallingEnemyList.removeIndex(i);

                }



            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error on World remove object EnemyStalactite: " + e ); //+ " enemy ID: " + ((EnemyStalactite) gameMovingFallingEnemyList.get(i)).getClass() );
            }

        }
    }

    Array<EnemyA> testArray = new Array<EnemyA>();
    private void setEnemyAActive(float dt){

        for ( int j=0; j < enemyObjectsEnemyA.size; j++ ){
            enemyObjectsEnemyA.get(j).update(dt);
            //((EnemyA) enemyObjectsEnemyA.get(j)).b2body.setActive(true); // remove if try{} is active!!

            try {

                if( enemyObjectsEnemyA.get(j).getX() < gamePlayer.getX() + 400 / GameUtility.PPM  &&
                        enemyObjectsEnemyA.get(j).getY() < gamePlayer.getY() + 205 / GameUtility.PPM ){

                    if( !((EnemyA)enemyObjectsEnemyA.get(j)).getIsDestroyed() ) { // .isDead()

                        ((EnemyA) enemyObjectsEnemyA.get(j)).b2body.setActive(true);

                    }else{
                        //System.out.println("isDestroyed ?? " + ((EnemyA)enemyObjectsEnemyA.get(j)).getIsDestroyed() );

                    }

                    if( ((EnemyA)enemyObjectsEnemyA.get(j)).isDead() ) {


                        if (((EnemyA) enemyObjectsEnemyA.get(j)).getSpawnItemType().equals("EX_LIFE")) {
                                if (((EnemyA) enemyObjectsEnemyA.get(j)).getSpawnItemTypeActive().equals("true")) {

                                    System.out.println("Spawn Life and Amo!!");

                                    if (rnRange(1, 4) == 2) {
                                        extraLifeSpawnfromdead.add(new ExtraLifeGameItem((PlayScreen) g.getScreen(),
                                                ((EnemyA) enemyObjectsEnemyA.get(j)).b2body.getPosition().x,
                                                ((EnemyA) enemyObjectsEnemyA.get(j)).b2body.getPosition().y + 0.4f,
                                                "EX_SPAWN_LIFE",
                                                ((EnemyA) enemyObjectsEnemyA.get(j)).getEnemyID()));
                                    }

                                    /** Amo Range is BLUE, RED, BLACK - THIS enemyA only drops BLUE - */
                                    if (rnRange(1, 2) == 2) {

                                        int spawnNumber = rnRange(1, 5);

                                        for(int s = 0; s < spawnNumber; s++){

                                            dragonEggGameItemArray.add(new DragonEggGameItem((PlayScreen) g.getScreen(),
                                                    ((EnemyA) enemyObjectsEnemyA.get(j)).b2body.getPosition().x +s*0.4f/4,
                                                    ((EnemyA) enemyObjectsEnemyA.get(j)).b2body.getPosition().y + s*0.2f/4,
                                                    "AMO_BLUE",((EnemyA) enemyObjectsEnemyA.get(j)).getEnemyID(), "BLUE"));
                                        }


                                    }
                                    // set's maybe after dead/destroyed
                                    //((EnemyA) enemyObjectsEnemyA.get(j)).b2body.setActive(false);
                                    ((EnemyA) enemyObjectsEnemyA.get(j)).setSpawnItemTypeActiveFalse("false");

                                    // Removes it to fast!!!
                                    //enemyObjectsEnemyA.removeIndex(j);
                                }
                            }
                        // Removes it to fast!!!
                        ((EnemyA) enemyObjectsEnemyA.get(j)).setdeath_Gapich_Timer_Active();


                        //((EnemyA) enemyObjectsEnemyA.get(j)).setToDestroyed();



                        if(((EnemyA) enemyObjectsEnemyA.get(j)).getIsDestroyed() ){
                            //System.out.println("Enemy from GameAI is destroyed!! ");
                            //System.out.println("EnemyA ObjectList Size be for removed Index: " + enemyObjectsEnemyA.size );
                            enemyObjectsEnemyA.removeIndex(j);
                            //System.out.println("EnemyA ObjectList Size after removed Index " + enemyObjectsEnemyA.size );
                        }

                        // make a list of body to be destroyed and then destroy them !!!!
                        //world.destroyBody(((EnemyA) enemyObjectsEnemyA.get(j)).b2body);


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error on rayCasting EnemyA: " + e + " enemy ID: " + ((EnemyA) enemyObjectsEnemyA.get(j)).getEnemyID() );
            }

        }
    }
    private void setEnemyBActive(float dt){

        for ( int j=0; j < enemyObjectsEnemyB.size; j++ ){
            enemyObjectsEnemyB.get(j).update(dt);
            //((EnemyA) enemyObjectsEnemyA.get(j)).b2body.setActive(true); // remove if try{} is active!!

            // should be 400 instead of 50 testing !!!
            try {

                float shouldWeFireOnPlayer = (enemyObjectsEnemyB.get(j).getY() - (gamePlayer.getY() + 100 / GameUtility.PPM ));

                //System.out.println("ShouldWeFireOnPlayer is " + shouldWeFireOnPlayer );

                if( enemyObjectsEnemyB.get(j).getX() < gamePlayer.getX() + 50 / GameUtility.PPM  &&
                        enemyObjectsEnemyB.get(j).getY() < gamePlayer.getY() + 205 / GameUtility.PPM ){

                    if( !((EnemyB)enemyObjectsEnemyB.get(j)).getIsDestroyed() ) { // .isDead()

                        ((EnemyB) enemyObjectsEnemyB.get(j)).b2body.setActive(true);

                    }else{
                        //System.out.println("isDestroyed ?? " + ((EnemyA)enemyObjectsEnemyA.get(j)).getIsDestroyed() );

                    }

                    /** set up shooting down on player */
                    if(shouldWeFireOnPlayer < 2f && !((EnemyB)enemyObjectsEnemyB.get(j)).isDead() ){

                        ((EnemyB)enemyObjectsEnemyB.get(j)).fireEnemyBullet(1);

                    }


                    if( ((EnemyB)enemyObjectsEnemyB.get(j)).isDead() ) {


                        if (((EnemyB) enemyObjectsEnemyB.get(j)).getSpawnItemType().equals("EX_LIFE")) {
                            if (((EnemyB) enemyObjectsEnemyB.get(j)).getSpawnItemTypeActive().equals("true")) {

                                System.out.println("Spawn Life and Amo!!");

                                if (rnRange(1, 4) == 2) {
                                    extraLifeSpawnfromdead.add(new ExtraLifeGameItem((PlayScreen) g.getScreen(),
                                            ((EnemyB) enemyObjectsEnemyB.get(j)).b2body.getPosition().x,
                                            ((EnemyB) enemyObjectsEnemyB.get(j)).b2body.getPosition().y + 0.4f,
                                            "EX_SPAWN_LIFE",
                                            ((EnemyB) enemyObjectsEnemyB.get(j)).getEnemyID()));
                                }

                                /** Amo Range is BLUE, RED, BLACK - THIS enemyA only drops BLUE - */
                                if (rnRange(1, 2) == 2) {

                                    int spawnNumber = rnRange(1, 5);

                                    for(int s = 0; s < spawnNumber; s++){

                                        dragonEggGameItemArray.add(new DragonEggGameItem((PlayScreen) g.getScreen(),
                                                ((EnemyB) enemyObjectsEnemyB.get(j)).b2body.getPosition().x +s*0.4f/4,
                                                ((EnemyB) enemyObjectsEnemyB.get(j)).b2body.getPosition().y + s*0.2f/4,
                                                "AMO_BLUE",((EnemyB) enemyObjectsEnemyB.get(j)).getEnemyID(), "BLUE"));
                                    }


                                }
                                // set's maybe after dead/destroyed
                                //((EnemyA) enemyObjectsEnemyA.get(j)).b2body.setActive(false);
                                ((EnemyB) enemyObjectsEnemyB.get(j)).setSpawnItemTypeActiveFalse("false");

                                // Removes it to fast!!!
                                //enemyObjectsEnemyA.removeIndex(j);
                            }
                        }
                        // Removes it to fast!!!
                        ((EnemyB) enemyObjectsEnemyB.get(j)).setdeath_Gapich_Timer_Active();


                        //((EnemyB) enemyObjectsEnemyB.get(j)).setToDestroyed();



                        if(((EnemyB) enemyObjectsEnemyB.get(j)).getIsDestroyed() ){
                            //System.out.println("Enemy from GameAI is destroyed!! ");
                            //System.out.println("GameManagerAi setEnemyB Active remove index Size be for: " + enemyObjectsEnemyB.size );
                            enemyObjectsEnemyB.removeIndex(j);

                            //if(gameSteeringStateManagerEnemyArray.size == 0 || enemyObjectsEnemyB.size == 0){
                            if(enemyObjectsEnemyB.size == 0){
                                stopSteeringStateManagerEnemy(true);
                                setSteeringStateManagerEnemyB_boolean(false);
                                System.out.println("GameManagerAi setEnemyBActive \nEnemyB is dead no index in SteeringArray stop all EnemyB steering!!!");
                            }
                            //System.out.println("GameManagerAi setEnemyB Active remove index Size after: " + enemyObjectsEnemyB.size );
                        }

                        // make a list of body to be destroyed and then destroy them !!!!
                        //world.destroyBody(((EnemyA) enemyObjectsEnemyA.get(j)).b2body);


                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error on rayCasting EnemyB: " + e + " enemy ID: " + ((EnemyB) enemyObjectsEnemyB.get(j)).getEnemyID() );
            }

        }
    }



    private void aiTestOne(float dt){
        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);

        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
            TimerAI_KnightBoss.updateCurrentTime(dt);
            TimerAI_RangeAttack.updateCurrentTime(dt);
            //System.out.println("current time running - SmallEnemyDef Is alive!! " + timerAI.getCurrentTime() );
        }

        if(((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() &&
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).isHit() ) {


            TimerAI_KnightBoss.resetCurrentTime();
            stopSteeringStateManagerBossKnight(true);
            setSteeringStateManagerBossBoolean(false);

            //((EnemyKnightDevil)enemyObjectsBossList.get(0)).setJumpIngbool( ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getIsRunningRight() );
            //setBossKnightAlwaysFacingPlayer(false,0);


            TimerAI_RangeAttack.resetCurrentTime();
            TimerAI_RangeAttack.updateCurrentTime(dt);
            System.out.println("resetCurrentTime() time is: " + TimerAI_RangeAttack.getCurrentTime() );
            if(TimerAI_RangeAttack.getCurrentTime() > 0f && !(TimerAI_KnightBoss.getCurrentTime() >  0.02f )) {
                //((EnemyKnightDevil) enemyObjectsBossList.get(0)).enemyShortJumpTest(((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight());
            }

            if(((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ){
                setBossKnightAlwaysFacingPlayer(true);
                //System.out.println("facing player true");
            }
        }


        if(TimerAI_KnightBoss.getCurrentTime() > 0.4f && !(TimerAI_KnightBoss.getCurrentTime() >  4.0f ) ) {
            /** true = start |
             *  (0 = follow Player, 1 = follow AI-Marker)
             *
             *  - Use On Player
             *  ( 0 = LongRange, 1 = CloseRange )
             *
             *  -Use On AI-Marker
             *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
             *
             *  - following AIBody number( not in use with following player )
             *  */
            startSteeringStateManagerBossKnight(true, 0, 1, 0);
        }else if( TimerAI_KnightBoss.getCurrentTime() > 4.2f && !(TimerAI_KnightBoss.getCurrentTime() >  4.3f ) ) {
            stopSteeringStateManagerBossKnight(true);
            setSteeringStateManagerBossBoolean(false);
            //System.out.println("GameManagerAI Class - Stop running State && Not set New Value to gameSteeringStateManagerBossArray !!!");
        }

        if(TimerAI_KnightBoss.getCurrentTime() > 8.0f && !(TimerAI_KnightBoss.getCurrentTime() >  25.0f ) ) {
            setSteeringStateManagerBossBoolean(true);
            /** true = start |
             *  (0 = follow Player, 1 = follow AI-Marker)
             *
             *  - Use On Player
             *  ( 0 = LongRange, 1 = CloseRange )
             *
             *  -Use On AI-Marker
             *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
             *
             *  - following AIBody number( not in use with following player )
             *  */
            startSteeringStateManagerBossKnight(true, 0, 0, 0);

            /** RangeAttack Activate */
            if( TimerAI_RangeAttack.getCurrentTime() > 8.2f &&
                    !(TimerAI_RangeAttack.getCurrentTime() > 8.4f) &&
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getRangeAttackActiveBool() ) {
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).rangeAttack(null);
            }
            /** RangeAttack Reset Timer */
            if(TimerAI_RangeAttack.getCurrentTime() > 10f ) {
                TimerAI_RangeAttack.resetCurrentTime();
                TimerAI_RangeAttack.updateCurrentTime(7.9f);
            }
//System.out.println("TimerAI_RangeAttack: " + TimerAI_RangeAttack.getCurrentTime());

        }else if( TimerAI_KnightBoss.getCurrentTime() > 25.2f && !(TimerAI_KnightBoss.getCurrentTime() >  25.3f ) ) {
            stopSteeringStateManagerBossKnight(true);
            setSteeringStateManagerBossBoolean(false);
            //System.out.println("GameManagerAI Class - Stop running State && Not set New Value to gameSteeringStateManagerBossArray !!!");
        }

        if( TimerAI_KnightBoss.getCurrentTime() > 25.4f) {
            TimerAI_KnightBoss.resetCurrentTime();
            System.out.println("TimerAI_KnightBoss: " + TimerAI_KnightBoss.getCurrentTime());
            setSteeringStateManagerBossBoolean(true);
        }
    }

    /**  updateAllSpawnLifeFromEnemy working on */
    private void aiTest_MoveAndJump(float dt){

        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);
        //setBossKnightAlwaysFacingPlayer(true);

        //System.out.println("SteeringArray.Size: " + gameSteeringStateManagerBossArray.size );

        //System.out.println("Boss Time loop: " + TimerAI_KnightBoss.getCurrentTime());
        //System.out.println("jump Time loop: " + TimerAI_Jump.getCurrentTime());

        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() ) { //&& ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
            /** Boss Time */
            TimerAI_KnightBoss.updateCurrentTime(dt);
            /** Jump Time */
            TimerAI_Jump.updateCurrentTime(dt);


            if (TimerAI_KnightBoss.getCurrentTime() > 0.4f && !(TimerAI_KnightBoss.getCurrentTime() > 4.0f)) {
                /** true = start |
                 *  (0 = follow Player, 1 = follow AI-Marker)
                 *
                 *  - Use On Player
                 *  ( 0 = LongRange, 1 = CloseRange )
                 *
                 *  -Use On AI-Marker
                 *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
                 *
                 *  - following AIBody number( not in use with following player )
                 *  */
                startSteeringStateManagerBossKnight(true, 1, 1, 0);
            } else if (TimerAI_KnightBoss.getCurrentTime() > 4.2f && !(TimerAI_KnightBoss.getCurrentTime() > 4.3f)) {
                stopSteeringStateManagerBossKnight(true);
                setSteeringStateManagerBossBoolean(false);
                //System.out.println("steering Array Size should be 0");

                //setBossKnightAlwaysFacingPlayer(false);

                //System.out.println("GameManagerAI Class - Stop running State && Not set New Value to gameSteeringStateManagerBossArray !!!");
            }
            /** BossTimer > 5.0f to 10.0f */
            if (TimerAI_KnightBoss.getCurrentTime() > 5.0f && !(TimerAI_KnightBoss.getCurrentTime() > 10.0f)) {


                /** BossJumpTimer > 5.01f to 5.02f */
                if (TimerAI_Jump.getCurrentTime() > 5.01f &&
                        !(TimerAI_Jump.getCurrentTime() > 5.02f)) {

                    //setBossKnightAlwaysFacingPlayer(true);

                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(false);
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(false);

                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).enemyShortJumpTest( !gamePlayer.getPlayerFacingDirection() );//((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight());


                }


                    /**BossJump Reset Timer */
                if (TimerAI_Jump.getCurrentTime() > 7f) {
                    TimerAI_Jump.resetCurrentTime();
                    TimerAI_Jump.updateCurrentTime(5.0f);
                }
            }

            if( TimerAI_KnightBoss.getCurrentTime() > 10.4f) {
                TimerAI_KnightBoss.resetCurrentTime();
                //System.out.println("Boss Looping -TimerAI_KnightBoss: " + TimerAI_KnightBoss.getCurrentTime());
            }
        }
    }

    private void aiTest_MoveAndJump2(float dt){

        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);
        //setBossKnightAlwaysFacingPlayer(true);

        //System.out.println("SteeringArray.Size: " + gameSteeringStateManagerBossArray.size );

        //System.out.println("Boss Time loop: " + TimerAI_KnightBoss.getCurrentTime());
        //System.out.println("jump Time loop: " + TimerAI_Jump.getCurrentTime());

        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() ) { //&& ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
            /** Boss Time */
            TimerAI_KnightBoss.updateCurrentTime(dt);
            /** Jump Time */
            TimerAI_Jump.updateCurrentTime(dt);


            if (TimerAI_KnightBoss.getCurrentTime() > 0.4f && !(TimerAI_KnightBoss.getCurrentTime() > 4.0f)) {
                /** true = start |
                 *  (0 = follow Player, 1 = follow AI-Marker)
                 *
                 *  - Use On Player
                 *  ( 0 = LongRange, 1 = CloseRange )
                 *
                 *  -Use On AI-Marker
                 *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
                 *
                 *  - following AIBody number( not in use with following player )
                 *  */
                startSteeringStateManagerBossKnight(true, 1, 1, 1);
            } else if (TimerAI_KnightBoss.getCurrentTime() > 4.2f && !(TimerAI_KnightBoss.getCurrentTime() > 4.3f)) {
                stopSteeringStateManagerBossKnight(true);
                setSteeringStateManagerBossBoolean(false);
                //System.out.println("steering Array Size should be 0");

                //setBossKnightAlwaysFacingPlayer(false);

                //System.out.println("GameManagerAI Class - Stop running State && Not set New Value to gameSteeringStateManagerBossArray !!!");
            }

            /** BossTimer > 5.0f to 10.0f */
            if (TimerAI_KnightBoss.getCurrentTime() > 5.0f && !(TimerAI_KnightBoss.getCurrentTime() > 10.0f)) {

                //setBossKnightActiveDirectionBoolean(false);

                /** BossJumpTimer > 5.01f to 5.02f */
                if (TimerAI_Jump.getCurrentTime() > 5.01f &&
                        !(TimerAI_Jump.getCurrentTime() > 5.02f)) {

                    //setBossKnightAlwaysFacingPlayer(true);

                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(false);
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(false);
//!gamePlayer.getPlayerFacingDirection() ); //
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).enemyShortJumpTest( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight());
                }

                //don't work . no jump and stutter
                //((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.setLinearVelocity(0,0);
                //setBossKnightActiveDirectionBoolean(true);



                /**BossJump Reset Timer */
                if (TimerAI_Jump.getCurrentTime() > 7f) {
                    TimerAI_Jump.resetCurrentTime();
                    TimerAI_Jump.updateCurrentTime(5.0f);
                }
            }

            if( TimerAI_KnightBoss.getCurrentTime() > 10.4f) {
                TimerAI_KnightBoss.resetCurrentTime();
                //System.out.println("Boss Looping -TimerAI_KnightBoss: " + TimerAI_KnightBoss.getCurrentTime());
            }
        }
    }

    private void aiTest_NoMoveFightLooping(float dt){

        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);

        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
            TimerAI_KnightBoss.updateCurrentTime(dt);
            TimerAI_RangeAttack.updateCurrentTime(dt);
            //System.out.println("current time running - SmallEnemyDef Is alive!! " + timerAI.getCurrentTime() );
        }
        /** BossTimer > 8.0f to 25.0f */
        if(TimerAI_KnightBoss.getCurrentTime() > 8.0f && !(TimerAI_KnightBoss.getCurrentTime() >  25.0f ) ) {

            /** RangeAttack Activate */
            if (TimerAI_RangeAttack.getCurrentTime() > 8.2f &&
                    !(TimerAI_RangeAttack.getCurrentTime() > 8.4f) &&
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getRangeAttackActiveBool()) {
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).rangeAttack(null);
            }
            /** RangeAttack Reset Timer */
            if (TimerAI_RangeAttack.getCurrentTime() > 10f) {
                TimerAI_RangeAttack.resetCurrentTime();
                TimerAI_RangeAttack.updateCurrentTime(7.9f);
            }
        }

        /** BossTimer > 8.0f to 25.0f */
        if( TimerAI_KnightBoss.getCurrentTime() > 25.4f) {
            TimerAI_KnightBoss.resetCurrentTime();
            System.out.println("Boss Looping -TimerAI_KnightBoss: " + TimerAI_KnightBoss.getCurrentTime());
        }

    }

    private void enemyBossChangeDirection(float dt){

        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
            TimerAI_KnightBoss.updateCurrentTime(dt);


            if (TimerAI_KnightBoss.getCurrentTime() > 2.1f && !(TimerAI_KnightBoss.getCurrentTime() > 2.11f)) {

                //setBossKnightAlwaysFacingPlayer(true);
                System.out.println("direction: " + ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight());
                if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight()) {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(false);
                    System.out.println("running false");
                } else {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(true);
                    System.out.println("running true");
                }

                //TimerAI_KnightBoss.resetCurrentTime();
                //System.out.println("Boss Looping -TimerAI_KnightBoss: " + TimerAI_KnightBoss.getCurrentTime());
            }
        }
    }

    private void enemyTrashTesting(float dt){

        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);

        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive()
                && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {


            TimerAI_KnightBoss.updateCurrentTime(dt);
            TimerAI_RangeAttack.updateCurrentTime(dt);
            //System.out.println("current time running - SmallEnemyDef Is alive!! " + timerAI.getCurrentTime() );
        }

        if(TimerAI_KnightBoss.getCurrentTime() > 8.0f && !(TimerAI_KnightBoss.getCurrentTime() >  25.0f ) ) {

            /** RangeAttack Activate */
            if( TimerAI_RangeAttack.getCurrentTime() > 8.2f &&
                    !(TimerAI_RangeAttack.getCurrentTime() > 8.4f)  ) {

                // running right ->
                if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight() ) {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(false);
                    System.out.println("running true, changing to false!! ");
                }
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).rangeAttack(null);
                System.out.println("Range attack 1");
            }

            /** RangeAttack Activate */
            if( TimerAI_RangeAttack.getCurrentTime() > 10f &&
                    !(TimerAI_RangeAttack.getCurrentTime() > 10.2f)  ) {

                // running <- left
                if( !((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight() ) {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(true);
                    System.out.println("running false, changing to true!! ");
                }
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).rangeAttack(null);
                System.out.println("Range attack 2");
            }



            /** RangeAttack Reset Timer */
            if(TimerAI_RangeAttack.getCurrentTime() > 10f ) {
                //    TimerAI_RangeAttack.resetCurrentTime();
                //    TimerAI_RangeAttack.updateCurrentTime(7.9f);
                //    System.out.println("time reset");
            }
        }

        if( TimerAI_KnightBoss.getCurrentTime() >  25.0f && !(TimerAI_KnightBoss.getCurrentTime() >  25.1f) ) {
            //TimerAI_KnightBoss = null;
            //TimerAI_KnightBoss.stopCurrentTime();
            //TimerAI_KnightBoss.resetCurrentTime();
            sequenceBoolDone = true;
            System.out.println("Boss time > 25f sequenceBoolDone = true");
        }



    }

    private void enemyTrashTesting2(float dt){

        // 8.0 | 10.8 two attacks = 2.8
        if(TimerAI_KnightBoss.getCurrentTime() > 0.1f && !(TimerAI_KnightBoss.getCurrentTime() >  2.5f ) ) {

            /** RangeAttack Activate */
            if( TimerAI_RangeAttack.getCurrentTime() > 0.2f &&
                    !(TimerAI_RangeAttack.getCurrentTime() > 0.21f)  ) { // 8

                // running right ->
                if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight() ) {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(false);
                    System.out.println("running true, changing to false!! ");
                }
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).rangeAttack(null);
                System.out.println("Range attack 1");
            }

            /** RangeAttack Activate */
            if( TimerAI_RangeAttack.getCurrentTime() > 1.91f &&
                    !(TimerAI_RangeAttack.getCurrentTime() > 1.92f)  ) {

                // running <- left
                if( !((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight() ) {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(true);
                    System.out.println("running false, changing to true!! ");
                }
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).rangeAttack(null);
                System.out.println("Range attack 2");
            }

        }

        if( TimerAI_KnightBoss.getCurrentTime() >  2.5f && !(TimerAI_KnightBoss.getCurrentTime() >  2.6f) ) {
            //sequenceBoolDone = true;
            System.out.println("Boss time > 25f sequenceBoolDone");

            TimerAI_KnightBoss.resetCurrentTime();
            TimerAI_RangeAttack.resetCurrentTime();
        }



    }

    private void testRangeAttackRight(float dt){

        if(TimerAI_KnightBoss.getCurrentTime() > 0.1f && !(TimerAI_KnightBoss.getCurrentTime() >  1.5f ) ) {

            /** RangeAttack Activate */
            if (TimerAI_RangeAttack.getCurrentTime() > 0.2f &&
                    !(TimerAI_RangeAttack.getCurrentTime() > 0.22f)) { // 8

                // running right ->
                if ( !((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight()) {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(true);
                    System.out.println("running false, changing to true!! ");
                }
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).rangeAttack(null);
                System.out.println("Range attack 1");
            }
        }

        if( TimerAI_KnightBoss.getCurrentTime() >  1.5f && !(TimerAI_KnightBoss.getCurrentTime() >  1.6f) ) {
            //sequenceBoolDone = true;
            System.out.println("Boss time > 25f sequenceBoolDone");

            TimerAI_KnightBoss.resetCurrentTime();
            TimerAI_RangeAttack.resetCurrentTime();
        }

    }

    private void testRangeAttackLeft(float dt){

        if(TimerAI_KnightBoss.getCurrentTime() > 0.1f && !(TimerAI_KnightBoss.getCurrentTime() >  1.5f ) ) {

            /** RangeAttack Activate */
            if (TimerAI_RangeAttack.getCurrentTime() > 0.2f &&
                    !(TimerAI_RangeAttack.getCurrentTime() > 0.22f)) { // 8

                // running right ->
                if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight()) {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setIsRunningRight(false);
                    System.out.println("running true, changing to false!! ");
                }
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).rangeAttack(null);
                System.out.println("Range attack 1");
            }
        }

        if( TimerAI_KnightBoss.getCurrentTime() >  1.5f && !(TimerAI_KnightBoss.getCurrentTime() >  1.6f) ) {
            //sequenceBoolDone = true;
            System.out.println("Boss time > 25f sequenceBoolDone");

            TimerAI_KnightBoss.resetCurrentTime();
            TimerAI_RangeAttack.resetCurrentTime();
        }

    }

    private void aiDashMoveOneTime(float dt, boolean run){
        if (run) {
            ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(false);
            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(false);
            if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive()) { //&& ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
                /** AI Time Dash */
                TimerAI_Steering_Dash.updateCurrentTime(dt);
                //System.out.println("TimAI_Dash: " + TimerAI_Steering_Dash.getCurrentTime() );

                if (TimerAI_Steering_Dash.getCurrentTime() > 0.4f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.0f)) {
                    /** true = start | Dash - Don't use Follow Player -
                     *  (0 = follow Player, 1 = follow AI-Marker)
                     *
                     *  - Use On - only Player -
                     *  ( 0 = LongRange, 1 = CloseRange )
                     *
                     *  -Use On AI-Marker
                     *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
                     *
                     *  - following AIBody number( not in use with following player )
                     *  0 = Marker far right | 1 = Marker far left
                     *  */

                    // Dash Assosicated number 1 is all ways longest of from boss spawn ???
                    for (int i = 0; i < this.dashAImarkerList.size; i++) {
                        if (dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("1")) {
                            startSteeringStateManagerBossKnight(true, 1, 1, i);
                        }
                    }

                    //startSteeringStateManagerBossKnight(true, 1, 1, 0);
                } else if (TimerAI_Steering_Dash.getCurrentTime() > 4.2f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.3f)) {
                    stopSteeringStateManagerBossKnight(true);
                    setSteeringStateManagerBossBoolean(false);
                }
/*
                    if (TimerAI_Steering_Dash.getCurrentTime() > 5.0f && !(TimerAI_Steering_Dash.getCurrentTime() > 8.6f)) {
                        setSteeringStateManagerBossBoolean(true);
                        for (int i = 0; i < this.dashAImarkerList.size; i++) {
                            if (dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("2")) {
                                startSteeringStateManagerBossKnight(true, 1, 1, i);
                            }
                        }
                        //startSteeringStateManagerBossKnight(true, 1, 1, 1);
                    } else if (TimerAI_Steering_Dash.getCurrentTime() > 8.8f && !(TimerAI_Steering_Dash.getCurrentTime() > 8.9f)) {
                        stopSteeringStateManagerBossKnight(true);
                        setSteeringStateManagerBossBoolean(false);
                    }
*/
            }
        }
    }

    // did not work!!
    private void aiDashMoveTestAir(float dt, boolean run){

        if(run) {
            ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(false);
            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(false);
            if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive()) { //&& ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
                /** AI Time Dash */
                TimerAI_Steering_Dash.updateCurrentTime(dt);
                //System.out.println("TimAI_Dash: " + TimerAI_Steering_Dash.getCurrentTime() );

                if (TimerAI_Steering_Dash.getCurrentTime() > 0.4f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.0f)) {
                    /** true = start | Dash - Don't use Follow Player -
                     *  (0 = follow Player, 1 = follow AI-Marker)
                     *
                     *  - Use On - only Player -
                     *  ( 0 = LongRange, 1 = CloseRange )
                     *
                     *  -Use On AI-Marker
                     *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
                     *
                     *  - following AIBody number( not in use with following player )
                     *  0 = Marker far right | 1 = Marker far left
                     *  */

                    // Dash Assosicated number 1 is all ways longest of from boss spawn ???
                    for(int i=0; i < this.dashAImarkerList.size; i++){
                        if(dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("1")){
                            startSteeringStateManagerBossKnight(true, 1, 1, i );
                        }
                    }

                    //startSteeringStateManagerBossKnight(true, 1, 1, 0);
                } else if (TimerAI_Steering_Dash.getCurrentTime() > 4.2f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.3f)) {
                    stopSteeringStateManagerBossKnight(true);
                    setSteeringStateManagerBossBoolean(false);
                }

                if (TimerAI_Steering_Dash.getCurrentTime() > 5.0f && !(TimerAI_Steering_Dash.getCurrentTime() > 8.6f)) {
                    setSteeringStateManagerBossBoolean(true);
                    for(int i=0; i < this.dashAImarkerList.size; i++){
                        if(dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("2")){
                            startSteeringStateManagerBossKnight(true, 1, 1, i );
                        }
                    }
                    //startSteeringStateManagerBossKnight(true, 1, 1, 1);
                } else if (TimerAI_Steering_Dash.getCurrentTime() > 8.8f && !(TimerAI_Steering_Dash.getCurrentTime() > 8.9f)) {
                    stopSteeringStateManagerBossKnight(true);
                    setSteeringStateManagerBossBoolean(false);
                }

                if (TimerAI_Steering_Dash.getCurrentTime() > 10.0f && !(TimerAI_Steering_Dash.getCurrentTime() > 13.4f)) {
                    setSteeringStateManagerBossBoolean(true);
                    for(int i=0; i < this.dashAImarkerList.size; i++){
                        if(dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("3")){
                            startSteeringStateManagerBossKnight(true, 1, 1, i );
                        }
                    }
                    //startSteeringStateManagerBossKnight(true, 1, 1, 0);
                }

                if (TimerAI_Steering_Dash.getCurrentTime() > 14.4f) {
                    setSteeringStateManagerBossBoolean(false);
                    TimerAI_Steering_Dash.resetCurrentTime();
                }
            }
        }
    }

    private void aiDashMoveOneTimeAir(float dt, boolean run){
        if (run) {
            ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(false);
            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(false);
            if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive()) { //&& ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
                /** AI Time Dash */
                TimerAI_Steering_Dash.updateCurrentTime(dt);
                //System.out.println("TimAI_Dash: " + TimerAI_Steering_Dash.getCurrentTime() );

                if (TimerAI_Steering_Dash.getCurrentTime() > 0.4f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.0f)) {
                    /** true = start | Dash - Don't use Follow Player -
                     *  (0 = follow Player, 1 = follow AI-Marker)
                     *
                     *  - Use On - only Player -
                     *  ( 0 = LongRange, 1 = CloseRange )
                     *
                     *  -Use On AI-Marker
                     *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
                     *
                     *  - following AIBody number( not in use with following player )
                     *  0 = Marker far right | 1 = Marker far left
                     *  */

                    //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveGravity(-6.5f);
                    // Dash Assosicated number 1 is all ways longest of from boss spawn ???
                    for (int i = 0; i < this.dashAImarkerList.size; i++) {
                        if (dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("1")) {
                            startSteeringStateManagerBossKnight(true, 1, 1, i);
                        }
                    }

                    //startSteeringStateManagerBossKnight(true, 1, 1, 0);
                } else if (TimerAI_Steering_Dash.getCurrentTime() > 4.2f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.3f)) {

                    stopSteeringStateManagerBossKnight(true);
                    setSteeringStateManagerBossBoolean(false);
                    //((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.setGravityScale(-6.5f); //1.0f);
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveGravity(-6.5f);

                }
                if (TimerAI_Steering_Dash.getCurrentTime() > 5.0f && !(TimerAI_Steering_Dash.getCurrentTime() > 8.6f)) {
                    setSteeringStateManagerBossBoolean(true);
                    for (int i = 0; i < this.dashAImarkerList.size; i++) {
                        if (dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("2")) {
                            startSteeringStateManagerBossKnight(true, 1, 1, i);
                            //enemyBossJumpFunction(dt, true, 1);
                        }
                    }
                    //startSteeringStateManagerBossKnight(true, 1, 1, 1);
                } else if (TimerAI_Steering_Dash.getCurrentTime() > 8.8f && !(TimerAI_Steering_Dash.getCurrentTime() > 8.9f)) {
                    stopSteeringStateManagerBossKnight(true);
                    setSteeringStateManagerBossBoolean(false);
                }
            }
        }
    }

    private void levelTwoSequence(float dt){

        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() && !((EnemyKnightDevil) enemyObjectsBossList.get(0)).isDead() ) {

            //setSteeringStateManagerBoss( getSteeringStateManagerBossBoolean() );

            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
            //enemyBossJump(dt, getBossJumpBool(), 1); // 0 = Short Jump, 1 = Long Jump
            //enemyBossJumpFunction(dt, getBossJumpBool(), 1);
        }

        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).isDead() ) {
            //System.out.println("dead!!");
            //setSteeringStateManagerBossBoolean(false);
            //stopSteeringStateManagerBossKnight(true);
            setBossKnightActiveDirectionBoolean(false);
        }
    }

    private void levelOneBossSequence(float dt){

        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() && !((EnemyKnightDevil) enemyObjectsBossList.get(0)).isDead() ) {

            setSteeringStateManagerBoss( getSteeringStateManagerBossBoolean() );

            //aiDashMoveTest(dt, getDashMoveBool() );
            //aiDashMoveOneTime(dt, getDashMoveBool() );
            //aiDashMoveTestAir(dt, getDashMoveBool() );
            //aiDashMoveOneTimeAir(dt, getDashMoveBool() );

            /** check dash Marker etc*/
            enemyBossDashToMarker(dt, getDashMoveBool(), "1");


            if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getDashAssociationNumber().equals("1")){
                System.out.println("levelOneBossSequence hit dash 1");
                setDashMoveBool(false); // we can now set dash to 2 etc...
                setSteeringStateManagerBossBoolean(false);
                stopSteeringStateManagerBossKnight(true);
            }
        }
        if( ((EnemyKnightDevil) enemyObjectsBossList.get(0)).isDead() ) {
            setSteeringStateManagerBossBoolean(false);
            stopSteeringStateManagerBossKnight(true);
            setBossKnightActiveDirectionBoolean(false);
        }
    }

    private void worldBossLevelKnightDevilStage01(float dt, String w) {


        /** Boss Knight Devil is active and running  */
        if (w.equals("2")) {
            if (gameAIBossHiddenMarkers.get(0).getHitBossBoolean() &&
                    !((EnemyKnightDevil) enemyObjectsBossList.get(0)).isDead()) {

                TimerAI.updateCurrentTime(dt);
                //System.out.println("AI Timer: " + TimerAI.getCurrentTime() );
            }


            if (TimerAI.getCurrentTime() > 1.4f && !(TimerAI.getCurrentTime() > 1.6f)) {
                setBossKnightActiveDirectionBoolean(false);
            }

            /** Single Action Jump */ // increase by 1.2 follow further down
            if (TimerAI.getCurrentTime() > 1.4f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                //enemyBossJumpFunction(dt, getBossJumpBool(), 1);
            }


            if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {
                if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyState().equals(EnemyKnightDevil.EnemyBossState.GUARDING)) {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.setLinearVelocity(0f, 0f);
                    setBossKnightActiveDirectionBoolean(true);
                    //System.out.println("onGround , Vel(0f,0f): " + ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.getLinearVelocity());
                }
            }

            /** Be for we do Dash */
            if (TimerAI.getCurrentTime() > 1.7f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {
                //System.out.println("facingPlayerActive Time > 1.7f: " + ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getFacingPlayerActive() );
                //System.out.println("Time > 1.7f, Dash at 1.9" );
                // setBossKnightActiveDirectionBoolean(true);
            }

            if (TimerAI.getCurrentTime() > 1.9f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {
                /** Sequence Action ( Dash & Jump */
                //levelOneBossSequence(dt);

                //setSteeringStateManagerBoss( getSteeringStateManagerBossBoolean() );
                /** check dash Marker etc*/
                //System.out.println("getDashMoveBool: " + getDashMoveBool());
                enemyBossDashToMarker(dt, getDashMoveBool(), "1");

                if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getDashAssociationNumber().equals("1")) {
                    //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setFacingPlayerActive(true);
                    //System.out.println("levelOneBossSequence hit dash 1");
                    //((EnemyKnightDevil)enemyObjectsBossList.get(0)).setDashAssociationNumber("null");

                    /** clear all steering, so new steering can be sett!! */
                    setDashMoveBool(false); // we can now set dash to 2 etc...
                    setSteeringStateManagerBossBoolean(false);
                    stopSteeringStateManagerBossKnight(true);

                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
                    //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setFacingPlayerActive(true);
                }


                if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getDashAssociationNumber().equals("1")) {

                    //stopSteeringStateManagerBossKnight(false);
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);
                    setSteeringStateManagerBossBoolean(true);
                    setRangeAttackFollowingPlayer(dt, true);
                }

                if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).isDead()) {
                    setSteeringStateManagerBossBoolean(false);
                    stopSteeringStateManagerBossKnight(true);
                    //setBossKnightActiveDirectionBoolean(false);
                    //  ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setFacingPlayerActive(false);
                }

            }

            /** Reset TimerAI - so Everything can be repeated */
            if (TimerAI.getCurrentTime() > 20.8f) {

                //System.out.println("***Reset Action on Boss***: " );

                //TimerAI.resetCurrentTime();
                //TimerAI_KnightBoss.resetCurrentTime();
                //TimerAI_Jump.resetCurrentTime();

                //setSteeringStateManagerBossBoolean(true);
                //TimerAI_Steering_Dash.resetCurrentTime();
                //setDashMoveBool(true);

            }
        }
    }

    private void worldBossLevelKnightDevil(float dt, String w) {

        if(w.equals("2")){
            /** Boss Knight Devil is active and running  */

            // To crazy
            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);


            if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive() &&
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround() &&
                    !((EnemyKnightDevil) enemyObjectsBossList.get(0)).isDead() ) {//!(TimerAI.getCurrentTime() > 30.9f)) {

                TimerAI.updateCurrentTime(dt);
                //System.out.println("AI Timer: " + TimerAI.getCurrentTime() );
            }

            /** Dash and Jump */
            //levelOneBossSequence(dt);

            if (TimerAI.getCurrentTime() > 10.5f && !(TimerAI.getCurrentTime() > 10.7f)) {

                /** Stop Dash */
                setDashMoveBool(false);
            }

            if (TimerAI.getCurrentTime() > 12.1f && !(TimerAI.getCurrentTime() > 12.2f)) {

                /** Stop Jump */
                setBossJumpBool(false);

                /** AI Time Boss - So we can use Jump another time */
                TimerAI_KnightBoss.resetCurrentTime();

                /** AI Time Jump - So we can use Jump another time*/
                TimerAI_Jump.resetCurrentTime();
            }
            if (!getDashMoveBool() && !getBossJumpBool()) {
                stopSteeringStateManagerBossKnight(true);
                setSteeringStateManagerBossBoolean(false);
                setBossKnightActiveDirectionBoolean(true);
            }



            if (TimerAI.getCurrentTime() > 17.1f && !(TimerAI.getCurrentTime() > 20.2f)) {
                /** Reset Jump To - true - So it can be used again*/
                //setBossJumpBool(true);
                /** only jump */
                //levelTwoSequence(dt);
            }

            /** Reset TimerAI - so Everything can be repeated */
            if (TimerAI.getCurrentTime() > 20.8f ) { // && !(TimerAI.getCurrentTime() > 30.9f)) {

                // this is the place where we call - everything to repeat so long as the enemy is not dead!!!!
                //setBossKnightActiveDirectionBoolean(true); if no repeat us this!!!

                setBossKnightActiveDirectionBoolean(false);
                setSteeringStateManagerBossBoolean(true);
                setDashMoveBool(true);
                TimerAI_Steering_Dash.resetCurrentTime();
                TimerAI.resetCurrentTime();
            }


        }

    }
    /**  updateAllSpawnLifeFromEnemy working on */


    private void setAttackFollowingPlayer(float dt, boolean run){


        if(run) {
            //if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive()) {

            //setSteeringStateManagerBossBoolean(true);
            setSteeringStateManagerBoss(getSteeringStateManagerBossBoolean());
            // this works when when setRangeAttackF... etc. is a stand alone Sequence // but not in worldBoss...etc
            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);

            TimerAI_KnightBoss.updateCurrentTime(dt);
            System.out.println("TimerAI_KnightBoss: " + TimerAI_KnightBoss.getCurrentTime() );
            TimerAI_RangeAttack.updateCurrentTime(dt);
            System.out.println("TimerAI_RangeAttack: " + TimerAI_RangeAttack.getCurrentTime() );
            //}
            // > 8.0f && >  25.0f
            if (TimerAI_KnightBoss.getCurrentTime() > 0.1f) { //&& !(TimerAI_KnightBoss.getCurrentTime() > 25.0f)) {

                /** true = start |
                 *  (0 = follow Player, 1 = follow AI-Marker)
                 *
                 *  - Use On Player
                 *  ( 0 = LongRange, 1 = CloseRange )
                 *
                 *  -Use On AI-Marker
                 *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
                 *
                 *  - following AIBody number( not in use with following player )
                 *  */
                startSteeringStateManagerBossKnight(true, 0, 0, 0);


                //System.out.println("TimerAI_RangeAttack: " + TimerAI_RangeAttack.getCurrentTime());

            }
        }
    }

    /** Inn Use */
    private boolean getDashMoveBool(){return dashMoveBool;}
    private void setDashMoveBool(boolean value){ this.dashMoveBool = value; }
    private void aiDashMoveTest(float dt, boolean run){

        if(run) {
            ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(false);
            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(false);
            if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive()) { //&& ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
                /** AI Time Dash */
                TimerAI_Steering_Dash.updateCurrentTime(dt);
                //System.out.println("TimAI_Dash: " + TimerAI_Steering_Dash.getCurrentTime() );

                if (TimerAI_Steering_Dash.getCurrentTime() > 0.4f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.0f)) {
                    /** true = start | Dash - Don't use Follow Player -
                     *  (0 = follow Player, 1 = follow AI-Marker)
                     *
                     *  - Use On - only Player -
                     *  ( 0 = LongRange, 1 = CloseRange )
                     *
                     *  -Use On AI-Marker
                     *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
                     *
                     *  - following AIBody number( not in use with following player )
                     *  0 = Marker far right | 1 = Marker far left
                     *  */

                    // Dash Assosicated number 1 is all ways longest of from boss spawn ???
                    for(int i=0; i < this.dashAImarkerList.size; i++){
                        if(dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("1")){
                            startSteeringStateManagerBossKnight(true, 1, 1, i );
                        }
                    }

                    //startSteeringStateManagerBossKnight(true, 1, 1, 0);
                } else if (TimerAI_Steering_Dash.getCurrentTime() > 4.2f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.3f)) {
                    stopSteeringStateManagerBossKnight(true);
                    setSteeringStateManagerBossBoolean(false);
                }

                if (TimerAI_Steering_Dash.getCurrentTime() > 5.0f && !(TimerAI_Steering_Dash.getCurrentTime() > 8.6f)) {
                    setSteeringStateManagerBossBoolean(true);
                    for(int i=0; i < this.dashAImarkerList.size; i++){
                        if(dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("2")){
                            startSteeringStateManagerBossKnight(true, 1, 1, i );
                        }
                    }
                    //startSteeringStateManagerBossKnight(true, 1, 1, 1);
                } else if (TimerAI_Steering_Dash.getCurrentTime() > 8.8f && !(TimerAI_Steering_Dash.getCurrentTime() > 8.9f)) {
                    stopSteeringStateManagerBossKnight(true);
                    setSteeringStateManagerBossBoolean(false);
                }

                if (TimerAI_Steering_Dash.getCurrentTime() > 10.0f && !(TimerAI_Steering_Dash.getCurrentTime() > 13.4f)) {
                    setSteeringStateManagerBossBoolean(true);
                    for(int i=0; i < this.dashAImarkerList.size; i++){
                        if(dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals("1")){
                            startSteeringStateManagerBossKnight(true, 1, 1, i );
                        }
                    }
                    //startSteeringStateManagerBossKnight(true, 1, 1, 0);
                }

                if (TimerAI_Steering_Dash.getCurrentTime() > 14.4f) {
                    setSteeringStateManagerBossBoolean(false);
                    TimerAI_Steering_Dash.resetCurrentTime();
                }
            }
        }
    }
    private void enemyBossDashToMarker(float dt, boolean run, String dashTo){
        if (run) {

            //setSteeringStateManagerBoss( getSteeringStateManagerBossBoolean() );
            setSteeringStateManagerBossBoolean(true);

            ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(false);
            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(false);
            if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive()) { //&& ((EnemyKnightDevil)enemyObjectsBossList.get(0)).getEnemyKnightOnGround() ) {
                /** AI Time Dash */
                TimerAI_Steering_Dash.updateCurrentTime(dt);
                //System.out.println("TimAI_Dash: " + TimerAI_Steering_Dash.getCurrentTime() );

                if (TimerAI_Steering_Dash.getCurrentTime() > 0.4f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.0f)) {

                    /** Dash Assosicated number 1 is all ways longest of from boss spawn */
                    for (int i = 0; i < this.dashAImarkerList.size; i++) {
                        if (dashAImarkerList.get(i).getMapMarkerAssociationNumber().equals(dashTo)) {
                            startSteeringStateManagerBossKnight(true, 1, 1, i);
                        }
                    }
                } else if (TimerAI_Steering_Dash.getCurrentTime() > 4.2f && !(TimerAI_Steering_Dash.getCurrentTime() > 4.3f)) {
                    stopSteeringStateManagerBossKnight(true);
                    setSteeringStateManagerBossBoolean(false);
                }
            }
        }
    }

    private void setRangeAttackFollowingPlayer(float dt, boolean run){


        if(run) {
            if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive()) {

                setSteeringStateManagerBossBoolean(true);
                setSteeringStateManagerBoss(getSteeringStateManagerBossBoolean());
                ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);

                //TimerAI_KnightBoss.updateCurrentTime(dt);
                //defaultTimepiece.update(dt);
                timerAI_OuterLoop_RangeAttack.updateCurrentTime(dt);

                //System.out.println("TimerAI_KnightBoss: " + TimerAI_KnightBoss.getCurrentTime() );
                //TimerAI_RangeAttack.updateCurrentTime(dt);
                timerAI_InnerLoop_RangeAttack.updateCurrentTime(dt);
                //System.out.println("TimerAI_RangeAttack: " + TimerAI_RangeAttack.getCurrentTime() );
            }
            // > 8.0f && >  25.0f
            //if (TimerAI_KnightBoss.getCurrentTime() > 0.1f) { //&& !(TimerAI_KnightBoss.getCurrentTime() > 25.0f)) {
            //if(defaultTimepiece.getTime() > 0.1f) {
            if(timerAI_OuterLoop_RangeAttack.getCurrentTime() > 0.1f) {

                /** true = start |
                 *  (0 = follow Player, 1 = follow AI-Marker)
                 *
                 *  - Use On Player
                 *  ( 0 = LongRange, 1 = CloseRange )
                 *
                 *  -Use On AI-Marker
                 *  ( 0 = close range(normal speed), 1 = Close Range(Super dash speed)
                 *
                 *  - following AIBody number( not in use with following player )
                 *  */
                startSteeringStateManagerBossKnight(true, 0, 0, 0);

                // 8.2f && 8.4f
                /** RangeAttack Activate */
                if (timerAI_InnerLoop_RangeAttack.getCurrentTime() > 0.4f &&
                        !(timerAI_InnerLoop_RangeAttack.getCurrentTime() > 0.6f) &&
                        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getRangeAttackActiveBool()) {
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).rangeAttack(null);
                }

                // 10f / updateCurrentTime(7.9f);
                /** RangeAttack Reset Timer */
                if (timerAI_InnerLoop_RangeAttack.getCurrentTime() > 2f) {
                    timerAI_InnerLoop_RangeAttack.resetCurrentTime();
                    //TimerAI_RangeAttack.updateCurrentTime(0.1f);
                }
                //System.out.println("TimerAI_RangeAttack: " + TimerAI_RangeAttack.getCurrentTime());

            }// else if (TimerAI_KnightBoss.getCurrentTime() > 25.2f && !(TimerAI_KnightBoss.getCurrentTime() > 25.3f)) {
             //   stopSteeringStateManagerBossKnight(true);
             //   setSteeringStateManagerBossBoolean(false);
                //System.out.println("GameManagerAI Class - Stop running State && Not set New Value to gameSteeringStateManagerBossArray !!!");
            //}
        }
    }

    //ToDo: Fix up time every one have it's one timer...
    private boolean getBossJumpBool() {return bossJumpBool; }
    private void setBossJumpBool(boolean value){this.bossJumpBool = value; }
    /** Used in - enemyBossJumpFollowPlayer() & enemyBossJumpSingleRepeat() */
    private void enemyBossJumpFunction(float dt, boolean run, int jumpType) {
        if (run) {
            //System.out.println("enemyBossJumpFunction -running!! ");
            if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.isActive()  ) {

                /** AI Time OuterLoop */
                //TimerAI_KnightBoss.updateCurrentTime(dt);
                timerAI_OuterLoop_enemyBossJumpFunction.updateCurrentTime(dt);

                /** AI Time Jump InnerLoop */
                //TimerAI_Jump.updateCurrentTime(dt);
                timerAI_InnerLoop_enemyBossJumpFunction.updateCurrentTime(dt);


                if (timerAI_OuterLoop_enemyBossJumpFunction.getCurrentTime() > 0f && !(timerAI_OuterLoop_enemyBossJumpFunction.getCurrentTime() > 0.1f)) {
                    if (timerAI_InnerLoop_enemyBossJumpFunction.getCurrentTime() > 0.01f &&
                            !(timerAI_InnerLoop_enemyBossJumpFunction.getCurrentTime() > 0.02f)) {
                        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
                        if (jumpType == 0) {
                            //System.out.println("jump short");
                            ((EnemyKnightDevil) enemyObjectsBossList.get(0)).enemyShortJumpTest(((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight());
                        }
                        if (jumpType == 1) {
                            //System.out.println("jump long");
                            ((EnemyKnightDevil) enemyObjectsBossList.get(0)).enemyLongJumpTest(((EnemyKnightDevil) enemyObjectsBossList.get(0)).getIsRunningRight());
                        }
                    }
                }
            }
        }
    }

    /** can only be used if -enemyBossJumpSingleRepeat()- is not used   */
    /** or */
    /** can be used in Conjunction with enemyBossJumpSingleRepeat()
     *
     * 1. enemyBossJumpSingleRepeat(true...)
     * 2. enemyBossJumpFollowPlayer(false...)
     *
     * */
    /** Jump 1 time Normal 2 jump, we will follow the player with jumps after 1.8f */
    private void enemyBossJumpFollowPlayer(boolean atLevelStartUse, int jumpType, float dt){

        //System.out.println("bossJumpFollowPlayerStopAllActivity: " + bossJumpFollowPlayerStopAllActivity);
        //System.out.println("boss OnGround: " + ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround() );

        if(!bossJumpFollowPlayerStopAllActivity) {
            if (atLevelStartUse) {

                /** ned to calculate start from sky drop */
                if (gameAIBossHiddenMarkers.get(0).getHitBossBoolean() &&
                        !((EnemyKnightDevil) enemyObjectsBossList.get(0)).isDead()) {

                    TimerAI.updateCurrentTime(dt);
                    //System.out.println("AI Timer: " + TimerAI.getCurrentTime() );
                }


                //if (TimerAI.getCurrentTime() > 0.6f && !(TimerAI.getCurrentTime() > 0.8f)) {
                    //setBossKnightActiveDirectionBoolean(false);
                //}

                /** on Ground check don't work !!?? no error's STOP DO NOTHING - after taken away !!! */
                /** Single Action Jump */ // increase by 1.2 follow further down
                if (TimerAI.getCurrentTime() > 0.6f ) { //&& ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                    enemyBossJumpFunction(dt, getBossJumpBool(), jumpType);
                    bossJumpDoneBool = true;
                }


                if (TimerAI.getCurrentTime() > 1.7f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                    //System.out.println("SmallEnemyDef have jumped!!");
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.setLinearVelocity(0f, 0f);

                    //setBossKnightActiveDirectionBoolean(true);
                    //System.out.println("onGround , Vel(0f,0f): " + ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.getLinearVelocity());

                }

                if (TimerAI.getCurrentTime() > 1.8f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                    /** turn off enemyBossJumpFunction and Stops -stopSteeringStateManagerBossKnight(true)- repeating
                     * So we can sett steering behavior -NB Remember to set getBossJumpBool() to true to use jump again!!!
                     * */

                    /** !bossJumpFollowPlayerStopAllActivity  - Turn On, after BossDirection is true - */

                    /** if SteeringArray is > 0 we remove it(index) */
                    stopSteeringStateManagerBossKnight(true); // in case SteeringArray > 0

                    /** this wont be active if SteeringArray > 0 */
                    /** ---NB---*/
                    /** this here, activate - bossJumpFollowPlayerStopAllActivity Set to -true- */
                    setBossKnightActiveDirectionBoolean(true);


                }
            } else {
                /** ned to start the jump right away */
                //System.out.println("boosJumDoneBoo is: " + bossJumpDoneBool);

                if (bossJumpDoneBool && TimerAI.getCurrentTime() > 1.8f) {
                    bossJumpDoneBool = false;

                    TimerAI = null;
                    TimerAI = new GameManagerAITimer();
                    //TimerAI_KnightBoss.resetCurrentTime();
                    timerAI_OuterLoop_enemyBossJumpFunction.resetCurrentTime();
                    //TimerAI_Jump.resetCurrentTime();
                    timerAI_InnerLoop_enemyBossJumpFunction.resetCurrentTime();
                    //System.out.println("this should only once!!");
                }

                TimerAI.updateCurrentTime(dt);
                //System.out.println("TimerAI.currentTime: " + TimerAI.getCurrentTime() );

                /** Single Action Jump */
                if (TimerAI.getCurrentTime() > 0.1f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                    //System.out.println("We are starting jump again, TimerAI > 0.1f && EnemyOnGround!! "); // OK

                    // need to reset this Timers Should be one time so with inn bossJumDoneBool
                    enemyBossJumpFunction(dt, getBossJumpBool(), jumpType);
                    bossJumpDoneBool = true;
                    //System.out.println("TimeAI_knightBoss " + TimerAI_KnightBoss.getCurrentTime());
                    //System.out.println("TimerAI_Jump.getCurrentTime(): " + TimerAI_Jump.getCurrentTime() ) ;
                }

                if (TimerAI.getCurrentTime() > 1.7f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                    //System.out.println("SmallEnemyDef have jumped!!");
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.setLinearVelocity(0f, 0f);
                }

                if (TimerAI.getCurrentTime() > 1.8f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {
                    /** if SteeringArray is > 0 we remove it(index) */
                    stopSteeringStateManagerBossKnight(true); // in case SteeringArray > 0
                    /** this wont be active if SteeringArray > 0 */
                    setBossKnightActiveDirectionBoolean(true);
                    //System.out.println("DirectionBoolean: " + getBossKnightActiveDirectionBoolean() );
                }
            }
        }

    }

    /** can only be used if -enemyBossJumpFollowPlayer()- is not used */
    /** Jump single - Can be reused  (true 1.jump) (false 2.jump) (true 3.jump) etc */
    private void enemyBossJumpSingleRepeat(boolean atLevelStartUse, int jumpType, float dt){

        if(!bossJumpSingleRepeatStopAllActivity) {
            if (atLevelStartUse) {

                if (bossJumpDoneBool2 && TimerAI.getCurrentTime() > 1.8f) {
                    bossJumpDoneBool2 = false;

                    TimerAI = null;
                    TimerAI = new GameManagerAITimer();
                    //TimerAI_KnightBoss.resetCurrentTime();
                    timerAI_OuterLoop_enemyBossJumpFunction.resetCurrentTime();
                    //TimerAI_Jump.resetCurrentTime();
                    timerAI_InnerLoop_enemyBossJumpFunction.resetCurrentTime();
                    System.out.println("this should only once!!");
                }

                /** ned to calculate start from sky drop */
                if (gameAIBossHiddenMarkers.get(0).getHitBossBoolean() &&
                        !((EnemyKnightDevil) enemyObjectsBossList.get(0)).isDead()) {

                    TimerAI.updateCurrentTime(dt);
                    //System.out.println("AI Timer: " + TimerAI.getCurrentTime() );
                }


                if (TimerAI.getCurrentTime() > 0.6f && !(TimerAI.getCurrentTime() > 0.8f)) {
                    //setBossKnightActiveDirectionBoolean(false);
                }

                /** Single Action Jump */ // increase by 1.2 follow further down
                if (TimerAI.getCurrentTime() > 0.6f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                    enemyBossJumpFunction(dt, getBossJumpBool(), jumpType);
                    bossJumpDoneBool = true;
                }


                if (TimerAI.getCurrentTime() > 1.7f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                    System.out.println("SmallEnemyDef have jumped!!");
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.setLinearVelocity(0f, 0f);

                    //setBossKnightActiveDirectionBoolean(true);
                    //System.out.println("onGround , Vel(0f,0f): " + ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.getLinearVelocity());

                }

                if (TimerAI.getCurrentTime() > 1.8f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {
                    /** if SteeringArray is > 0 we remove it(index) */
                    stopSteeringStateManagerBossKnight(true); // in case SteeringArray > 0
                    /** this wont be active if SteeringArray > 0 */
                    setBossKnightActiveDirectionBoolean(true);
                    //System.out.println("DirectionBoolean: " + getBossKnightActiveDirectionBoolean() );
                }


                if (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyState().equals(EnemyKnightDevil.EnemyBossState.GUARDING)) {

                    //System.out.println("SmallEnemyDef have jumped!!");

                    //((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.setLinearVelocity(0f, 0f);
                    //setBossKnightActiveDirectionBoolean(true);
                    //System.out.println("onGround , Vel(0f,0f): " + ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.getLinearVelocity());

                    //TimerAI = null;
                    //TimerAI = new GameManagerAITimer();

                    //timerAI_OuterLoop_JumpSeqtimer = null; //.update(0f); // in a way reset them ?
                    //timerAI_OuterLoop_JumpSeqtimer = new DefaultTimepiece();
                    //timerAI_InnerLoop_JumpSeqtimer.update(0f);
                    /** Have to have a way to se if jump is done!!! call next action */
                    //bossJumpDoneBool = true;
                    //setRangeAttackFollowingPlayer(dt, true); // works alone
                }


            } else {
                /** ned to start the jump right away */
                //System.out.println("boosJumDoneBoo is: " + bossJumpDoneBool);

                if (bossJumpDoneBool && TimerAI.getCurrentTime() > 1.8f) {
                    bossJumpDoneBool = false;

                    TimerAI = null;
                    TimerAI = new GameManagerAITimer();
                    //TimerAI_KnightBoss.resetCurrentTime();
                    timerAI_OuterLoop_enemyBossJumpFunction.resetCurrentTime();
                    //TimerAI_Jump.resetCurrentTime();
                    timerAI_InnerLoop_enemyBossJumpFunction.resetCurrentTime();
                    //System.out.println("this should only once!!");
                }

                TimerAI.updateCurrentTime(dt);
                //System.out.println("TimerAI.currentTime: " + TimerAI.getCurrentTime());

                /** Single Action Jump */
                if (TimerAI.getCurrentTime() > 0.1f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                    //System.out.println("We are starting jump again, TimerAI > 0.1f && EnemyOnGround!! "); // OK

                    // need to reset this Timers Should be one time so with inn bossJumDoneBool
                    enemyBossJumpFunction(dt, getBossJumpBool(), jumpType);
                    bossJumpDoneBool2 = true;
                    //System.out.println("TimeAI_knightBoss " + TimerAI_KnightBoss.getCurrentTime());
                    //System.out.println("TimerAI_Jump.getCurrentTime(): " + TimerAI_Jump.getCurrentTime() ) ;
                }

                if (TimerAI.getCurrentTime() > 1.7f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {

                    //System.out.println("SmallEnemyDef have jumped!!");
                    ((EnemyKnightDevil) enemyObjectsBossList.get(0)).b2body.setLinearVelocity(0f, 0f);
                }

                if (TimerAI.getCurrentTime() > 1.8f && ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyKnightOnGround()) {
                    /** if SteeringArray is > 0 we remove it(index) */
                    stopSteeringStateManagerBossKnight(true); // in case SteeringArray > 0
                    /** this wont be active if SteeringArray > 0 */
                    setBossKnightActiveDirectionBoolean(true);
                    //System.out.println("DirectionBoolean: " + getBossKnightActiveDirectionBoolean() );
                }
            }
        }

    }

    private void setRangeAttackFollowingPlayerBool(boolean value){ steeringFollowRangeAttack = value; }
    private boolean getRangeAttackFollowingPlayerBool(){return this.steeringFollowRangeAttack; }

    private void setCloseAttackFollowingPlayerBool(boolean value){ steeringFollowCloseAttack = value; }
    private boolean getCloseeAttackFollowingPlayerBool(){return this.steeringFollowCloseAttack; }

    private boolean getBossKnightActiveDirectionBoolean(){return bossKnightActiveDirection; }
    private void setBossKnightActiveDirectionBoolean(boolean value){ bossKnightActiveDirection = value; }


    public void updateAllSpawnLifeFromEnemy(float dt){

        if(extraLifeSpawnfromdead.size > 0) {
            for (ItemObjectDef items : extraLifeSpawnfromdead) {
                items.update(dt);
            }
        }

        if(dragonEggGameItemArray.size > 0) {
            for (ItemObjectDef items : dragonEggGameItemArray) {
                items.update(dt);
            }
        }

    }




    public Array<ExtraLifeGameItem> getExtraLifeSpawnFromDeadToDraw(){
        return this.extraLifeSpawnfromdead;
    }

    public Array<DragonEggGameItem> getDragonEggToDraw(){
        return this.dragonEggGameItemArray;
    }




    public void runWorld_1(float dt){

        if(this.enemyObjectsBossList.size > 0){
            //System.out.println("");

            if(this.worldBoss.equals("1")) {

                setSteeringStateManagerBoss( getSteeringStateManagerBossBoolean() );

                // if TimeAI - if Difficulty is (Easy,Normal,Hard)
                // AI Action on life
                switch (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyLife()) {
                    case 20:

                        enemyBossJumpFollowPlayer(true, 0, dt);
                        break;
                    case 19:
                    case 18:
                    case 17:
                    case 16:
                    case 15:
                    case 14:
                    case 13:
                    case 12:
                    case 11:
                    case 10:
                    case 9:
                    case 8:
                    case 7:
                    case 6:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:

                        //enemyBossJumpFollowPlayer(false, 0, dt);
                        break;
                    case 0:
                        //System.out.println("Life 0 - set this since we use the same boss level 1 and 2 just in case!!!");
                        setBossKnightActiveDirectionBoolean(false);
                        bossJumpFollowPlayerStopAllActivity = false;
                        setRangeAttackFollowingPlayerBool(false);

                        ((GameObjectSwitchHidden) b2WorldCreator.getGameObjectSwitchesBossDead().get(0))
                                .setActivateSwitch("true");
                        break;
                }


                // Spawn Action on life
                switch (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyLife()) {
                    case 20:
                        break;
                    case 0:
                        if( happenOnTimeSpawnDeadBoss == 0 ){
                            happenOnTimeSpawnDeadBoss = 1;
                            System.out.println("Boss is dead, should spawn a prize & further the game along!!");
                            switch (rnRange(1,4) ){
                                case 1:
                                    System.out.println("rnRange 1,20 = 1");
                                    break;
                                case 2:
                                    System.out.println("rnRange 1,20 = 2");
                                    break;
                                case 3:
                                    System.out.println("rnRange 1,20 = 3");
                                    break;
                                case 4:
                                    System.out.println("rnRange 1,20 = 4");
                                    break;
                            }
                        }
                        break;
                }


            }
        }

    }

    public void runWorld_2(float dt){

        //System.out.println("GameManagerAI Running World 2");

        if(this.enemyObjectsBossList.size > 0) {
            //if (GameManagerAssets.instance.getCurrentLevel() == "1" ) { // World Boss Level 2

                //System.out.println("Boss life is: " + ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyLife());
                setSteeringStateManagerBoss(getSteeringStateManagerBossBoolean());

                //setBossKnightActiveDirectionBoolean(false); // check of to make steering after jump but need it for jump ?

                switch (((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyLife()) {
                    case 20:
                        //System.out.println("20:19");

                        enemyBossJumpFollowPlayer(true, 0, dt);

                        /** this is set after jump and timer is > 1.8f */
                        if (getBossKnightActiveDirectionBoolean()) { // bossKnightActiveDirection

                            /** Stops -enemyBossJumpFollowPlayer() */
                            bossJumpFollowPlayerStopAllActivity = true;
                            // this gives -setRangeAttackFollowingPlayer(true) So it can start
                            setRangeAttackFollowingPlayerBool(true);

                        }
                        //setSteeringStateManagerBossBoolean(true);
                        //setSteeringStateManagerBoss(getSteeringStateManagerBossBoolean());
                        //startSteeringStateManagerBossKnight(true, 0, 1, 0);
                        setRangeAttackFollowingPlayer(dt, getRangeAttackFollowingPlayerBool());
                        break;
                    case 19:
                        //System.out.println("19:15");
                        stopSteeringStateManagerBossKnight(true);
                        setSteeringStateManagerBossBoolean(false);
                        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);

                        setSteeringStateManagerBossBoolean(true);
                        setSteeringStateManagerBoss(getSteeringStateManagerBossBoolean());
                        startSteeringStateManagerBossKnight(true, 0, 1, 0);
                        break;
                    case 18:
                    case 17:
                    case 16:
                    case 15:
                        //System.out.println("18:15");

                        //**  *NB*  -if jump two after Steering have been active this must be set- *NB*
                        bossJumpFollowPlayerStopAllActivity = false;
                        stopSteeringStateManagerBossKnight(true);
                        setSteeringStateManagerBossBoolean(false);
                        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);


                        enemyBossJumpFollowPlayer(false, 1, dt);


                        //enemyBossJumpFollowPlayer(true, 1, dt); // 0 or 1
                        //worldBossLevelKnightDevilStage01(dt, worldBoss); // last 10 sec , ca 7 attacks Range

                        //enemyBossJumpSingleRepeat(true, 0, dt);

                        //System.out.println("Direction: On/Off: " + getBossKnightActiveDirectionBoolean());

                        //  Will be true in jump done and timer > 1.8f && EnemyOnGround
                        if (bossKnightActiveDirection) { //


                            //  Set false to stop -Steering -STOP- to be repeating
                            //setBossJumpBool(false);
                            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
                            //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);

                            //setSteeringStateManagerBossBoolean(true);
                            //setSteeringStateManagerBoss(getSteeringStateManagerBossBoolean());
                            //startSteeringStateManagerBossKnight(true, 0, 1, 0);


                            //setSteeringStateManagerBossBoolean(true);
                            //setSteeringStateManagerBoss(getSteeringStateManagerBossBoolean());
                            //startSteeringStateManagerBossKnight(true, 0, 1, 0);
                            //setRangeAttackFollowingPlayer(dt, true); // works alone
                        }

                        //setRangeAttackFollowingPlayer(dt, true); // works alone
                        break;
                    case 14:
                    case 13:
                    case 12:
                        //System.out.println("14:12");
                        break;
                    case 11:
                    case 10:
                    case 9:
                    case 8:
                    case 7:
                        //System.out.println("11:7");

                        // More aggressive line of attacks
                        //stopSteeringStateManagerBossKnight(true);
                        //setSteeringStateManagerBossBoolean(false);
                        //setBossKnightActiveDirectionBoolean(true);
                        //((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);

                        //setSteeringStateManagerBossBoolean(true);
                        //setSteeringStateManagerBoss(getSteeringStateManagerBossBoolean());
                        //startSteeringStateManagerBossKnight(true, 0, 1, 0);
                        break;
                    case 6:
                        //System.out.println("6:6");
                        //stopSteeringStateManagerBossKnight(true);
                        //setSteeringStateManagerBossBoolean(false);
                        //setBossKnightActiveDirectionBoolean(true);
                        break;
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:
                    case 0:
                        ((GameObjectSwitchHidden) b2WorldCreator.getGameObjectSwitchesBossDead().get(0))
                                .setActivateSwitch("true");
                        break;
                }

                if (gamePlayer.isDead()) {
                    if (gameSteeringStateManagerBossArray.size > 0) {
                        stopSteeringStateManagerBossKnight(true);
                        setSteeringStateManagerBossBoolean(false);
                        //System.out.println("Player died - call off EnemySteering!!! ");

                        // don't know - updateAllSpawnLifeFromEnemy !!!
                        //TimerAI_KnightBoss.stopCurrentTime();
                        timerAI_OuterLoop_enemyBossJumpFunction.stopCurrentTime();
                        //TimerAI_RangeAttack.stopCurrentTime();
                        timerAI_OuterLoop_RangeAttack.stopCurrentTime();

                    }
                }


                if(((GameObjectSwitchHidden) b2WorldCreator.getGameObjectSwitchesBossDead().get(0)).getIsBossDeadOpenDoorSwitch().equals("true") &&
                        ((GameObjectSwitchHidden) b2WorldCreator.getGameObjectSwitchesBossDead().get(0)).getRunDoors()){

                    //if(happendOneTimeTest == 0){
                    //happendOneTimeTest =1;
                    //System.out.println("Happen's one time!!! ");

                    //for( int i=0; i > b2WorldCreator.activableObstacles.size;i++) {
                    //  ((ObstacleDoor) b2WorldCreator.activableObstacles.get(0)).setIsBooActivDoorOpenIsFinished(false);
                    //}

                    for( GameObjectSwitchHidden gameObjectSwitch : b2WorldCreator.getGameObjectSwitchesBossDead()){
                        if (gameObjectSwitch.getRunDoors()) {
                            //System.out.println("Running door Boss dead w2");
                            gameObjectSwitch.activeAfterBossDeath(b2WorldCreator.activableObstacles);
                        }
                    }
                    //}

                }




            //}


            for(GameObjectSwitchHidden gameObjectSwitch : b2WorldCreator.getGameObjectSwitchesBossDead()) {

                gameObjectSwitch.update(dt);
            }


        }


    }
    public void runWorld_3(){}
    public void runWorld_4(){}
    public void runWorld_5(){}



public void testFlyby(){

//System.out.println("enemyB list is " + enemyObjectsEnemyB.size );
        for(int fly =0; fly < enemyObjectsEnemyB.size; fly++){

        ((EnemyB)enemyObjectsEnemyB.get(fly)).setSwoopingActionFlyByTargetH(gamePlayer.b2body.getPosition().y);
        System.out.println("ManagerAI flyby value from player " + gamePlayer.b2body.getPosition().y);

        }
}


    /** us in runWorld_1 testing spawn one time random !! */
    int happenOnTimeSpawnDeadBoss = 0;

    public void setPlayerPowerUpTextToScreen(String msg, Vector2 pos, float deltaMoveX, float deltaMoveY){

        Vector3 positionVec3 = new Vector3(pos.x,pos.y, 0);
        gameCammera.project(positionVec3);
        playerController.initCreateFloatingText(msg, positionVec3.x, positionVec3.y, deltaMoveX, deltaMoveY );


    }


    public void update(float dt) {


        // ToDo: have to check , know boss knight don't give error if not present
        // ToDo: check - setEnemyAActive(dt);

        /** All SmallEnemyDef's Update && Active running && set Active Facing -if Boolean is true-  */
        setBossKnightActive(dt, getBossKnightActiveDirectionBoolean()); //true);
        setEnemyAActive(dt);
        setEnemyBActive(dt);

        /** this update of extra_life spawn must be after all potential SmallEnemyDef witch spawn's from */
        updateAllSpawnLifeFromEnemy(dt);

        /** updates and sets active moving falling enemy's like stalgmite*/
        setEnemyMovingFallingActive(dt);




/** test on steering EnemyB to player !!!      OK!!  */
        setStopAllSteeringActivityUpdate(dt);
        setStartAllSteeringActivityUpdate();
/** End test!! */


        /** Not sure if we want this... text every time player shoots green for now!!! */
        if(gamePlayer.getPlayerIsShooting()){

            Vector3 proSition = new Vector3(gamePlayer.b2body.getPosition().x, gamePlayer.b2body.getPosition().y, 0);
            /** project  - Active */
            gameCammera.project(proSition);
            playerController.initCreateFloatingText("die.. Monster die!!", proSition.x, proSition.y, 0,200 );
            gamePlayer.setPlayerIsShooting(false);
        }

        switch (gameManagerAssetsInstance.getCurrentWorld()){

            case "1":
                //runWorld_1(dt);
                //break;
            case "2":
                switch(gameManagerAssetsInstance.getCurrentLevel()){

                    case "2":
                      runWorld_2(dt);
                        //testFlyby();
                      //break;
                }
               // break;
        }


        //Debug
        //lookUpSteeringStateManagerHasState(); // System out Se if we have a State!!
        //System.out.println("witch world Boss are we inn!!!  " + this.worldBoss );
        //System.out.println("EnemyLife: " + ((EnemyKnightDevil) enemyObjectsBossList.get(0)).getEnemyLife());


        /*
        setSteeringStateManagerBossBoolean(true);
        setSteeringStateManagerBoss( getSteeringStateManagerBossBoolean() );
        stopSteeringStateManagerBossKnight(true);
        setSteeringStateManagerBossBoolean(false);
        setBossKnightActiveDirectionBoolean(true);
        enemyBossDashToMarker(dt, getDashMoveBool(), "1");
        enemyBossJumpFunction(dt, getBossJumpBool(), 1);
        setDashMoveBool(true);
        setBossJumpBool(true);
        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveCloseAttack(true);
        ((EnemyKnightDevil) enemyObjectsBossList.get(0)).setActiveRangeAttack(true);
        */


        /**
         * This must be in the right order old individual testing !!!
         */
        //setSteeringStateManagerBoss( getSteeringStateManagerBossBoolean() ); // bool get if we should set new to steeringState Array
        //setBossKnightActive(dt, getBossKnightActiveDirectionBoolean() ); //true);
        //aiTestOne(dt);
        //aiTest_MoveAndJump2(dt);
        //aiDashMoveTest(dt);
        //aiTest_NoMoveFightLooping(dt);
        //enemyBossJump(dt); // linearVel = 0.0 then direction Auto works!!
        //enemyTrashTesting(dt);

        /** update StateSteering entity's for Boss */
        for(int i = 0; i < this.gameSteeringStateManagerBossArray.size; i++) {
            //System.out.println("GameManagerAI Class Update render steeringState!!");
            this.gameSteeringStateManagerBossArray.get(i).render();
        }

        /** update StateSteering entity's for Enemy B */
        for(int i = 0; i < this.gameSteeringStateManagerEnemyArray.size; i++) {
            //System.out.println("GameManagerAI Class Update render steeringState!!");
            this.gameSteeringStateManagerEnemyArray.get(i).render();
            //System.out.println("GameManagerAi update enemyB has state " + this.gameSteeringStateManagerEnemyArray.get(i).hasState() );

        }
    }

    public void setStopAllSteeringActivityUpdate(float dt){



        if(getStartSteeringActivityFromContactWithAI() && getStopSteeringActivityFromContactWithAI()) {


                if (getSteeringStateManagerEnemyB_boolean()) {

                    if (gameSteeringStateManagerEnemyArray.size > 0) {

                        stopSteeringStateManagerEnemy(true);
                        setSteeringStateManagerEnemyB_boolean(false);
                        //System.out.println("Stop SteeringActivity is activated");


    /*
                        for(int steeringPos = 0; steeringPos < gameSteeringStateManagerEnemyArray.size; steeringPos++){

                            if( !gameSteeringStateManagerEnemyArray.get(steeringPos).hasState().equals("PursueStateEnemy") ) {

                                ((EnemyB) enemyObjectsEnemyB.get(steeringPos)).updateEnemyFlying(dt);

    System.out.println("enemy stop steering, update it...");

                            }
                        }
    */

                        if (enemyObjectsEnemyB.size > 0) {
                            ((EnemyB) enemyObjectsEnemyB.get(0)).setEnemyHasSteeringStateBool(false);
                            ((EnemyB) enemyObjectsEnemyB.get(0)).update(dt);
                            ((EnemyB) enemyObjectsEnemyB.get(0)).setIsRunningRight(false);
                            //System.out.println("Stop SteeringActivity - Update index 0 to fly...");
                        }


                    }
                }
        }
        /**if player die - make enemy do regular flying wile we die... looks better */
        if(gameSteeringStateManagerEnemyArray.size > 0 && gamePlayer.isDead()){
            if (enemyObjectsEnemyB.size > 0) {
                ((EnemyB) enemyObjectsEnemyB.get(0)).setEnemyHasSteeringStateBool(false);
                ((EnemyB) enemyObjectsEnemyB.get(0)).update(dt);
                ((EnemyB) enemyObjectsEnemyB.get(0)).setIsRunningRight(false);
                //System.out.println("Player is dead - Stop SteeringActivity - Update index 0 to fly...");
            }
        }

    }
    public void setStartSteeringActivityFromContactWithAI(boolean value){
        this.steeringEnemyB_Start_From_ContactL = value;

        /** if steering have started and then stopped we need to reset this to false*/
        if(getStopSteeringActivityFromContactWithAI()){
            setStopSteeringActivityFromContactWithAI(false);
        }

    }

    //Todo: need better look into a stop with start function like start
    //jobb her
    public void setStopSteeringActivityFromContactWithAI(boolean value){

        if(getStartSteeringActivityFromContactWithAI()) {
            this.steeringEnemyB_Stop_From_ContactL = value;
            //System.out.println("steeringEnemyB_Stop_From_ContactL " + steeringEnemyB_Stop_From_ContactL );
        }
    }


    public boolean getStopSteeringActivityFromContactWithAI(){

        return this.steeringEnemyB_Stop_From_ContactL;
    }

    public boolean getStartSteeringActivityFromContactWithAI(){
        return this.steeringEnemyB_Start_From_ContactL;
    }



    public void setStartAllSteeringActivityUpdate(){

        if(this.steeringEnemyB_Start_From_ContactL && !this.steeringEnemyB_Stop_From_ContactL ) {


            if (this.enemyObjectsEnemyB.size > 0) {


                if (gamePlayer.isDead() ) {
                    if (gameSteeringStateManagerEnemyArray.size > 0) {

                        stopSteeringStateManagerEnemy(true);
                        setSteeringStateManagerEnemyB_boolean(false);
                        //System.out.println("Player dead Stop SteeringActivity...");
                    }
                }else {

                    //if player move to far from enemy that is perusuing shit happends
                    //
                    //System.out.println("GameManagerAi update enemyB X " +  ((EnemyB)enemyObjectsEnemyB.get(0)).getX() );
                    //System.out.println("GameManagerAi update player X " + gamePlayer.getX() );

                    if (!getSteeringStateManagerEnemyB_boolean()) {
                        setSteeringStateManagerEnemyB_boolean(true);
                    }
                    /** this set SteeringStatManagerArray add from enemyObjectsEnemyB */
                    setSteeringStateManagerEnemy(getSteeringStateManagerEnemyB_boolean());
                    startSteeringStateManagerSmallEnemy(true, 0, 0, 0);

                    for(int steeringPos = 0; steeringPos < gameSteeringStateManagerEnemyArray.size; steeringPos++){

                        if( gameSteeringStateManagerEnemyArray.get(steeringPos).hasState().equals("PursueStateEnemy") ) {
                            ((EnemyB) enemyObjectsEnemyB.get(steeringPos)).setEnemyHasSteeringStateBool(true);
                        }
                    }

                    //for(int enemyPos = 0; enemyPos < enemyObjectsEnemyB.size; enemyPos++){
                        //((EnemyB)enemyObjectsEnemyB.get(enemyPos)).setEnemyHasSteeringStateBool(true);
                    //}

                    //System.out.println("check if got State " + gameSteeringStateManagerEnemyArray.get(0).hasState() );

                    //System.out.println("Start SteeringActivity is activated");

                }
            }
        }

        /*
        else{

            if (this.enemyObjectsEnemyB.size > 0) {
                setSteeringStateManagerEnemy(getSteeringStateManagerEnemyB_boolean());
                startSteeringStateManagerSmallEnemy(true, 0, 0, 0);
                System.out.println("Else Start SteeringActivity is activated");
            }
        }

         */
    }

    public static int rnRange(int start, int finished) {
        return new Random().nextInt(finished + 1 - start) + start ;
    }

    public static int rnRange(int finished) {
        return rnRange(1, finished);
    }

    // ToDo : not sure , nullPoint Ex when player die!! change Screen !!! Game Over
    public  void dispose(){
         // dispose after player dead !! don't work here!!!
        //this.steeringStateManager.dispose(); // this dose not work
        //this.gameSteeringStateManagerBossArray.get(0).dispose(); // this dose not work
    }

}
