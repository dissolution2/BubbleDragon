package com.mygdx.game.framework.debug.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.SaveGamePlayerDataHolderClass;

import com.mygdx.game.framework.debug.managers.GameManagerAITimer;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyA;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.EnemyKnightDevil;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyB;
import com.mygdx.game.framework.debug.sprites.Enemies.MovingFallingEnemy.EnemyStalactite;
import com.mygdx.game.framework.debug.sprites.Enemies.StationaryEnemies.EnemyGraphicSensor;
import com.mygdx.game.framework.debug.sprites.powers.BalloneBullet;
import com.mygdx.game.framework.debug.sprites.powers.EnemyBullet;
import com.mygdx.game.framework.debug.sprites.powers.EnemyKnightRangeAttack;
import com.mygdx.game.framework.debug.sprites.powers.EnemyKnightPowerSword;
import com.mygdx.game.framework.debug.util.FloatingText;
import com.mygdx.game.framework.debug.util.GameUtility;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


public class BubblePlayer extends Sprite implements Serializable {



    public enum PlayerState {
        FALLING,
        JUMPING,
        IDLE,
        ONBALLONE,
        SHOOTING,
        RUNNING,
        HIT,
        DEAD
    }
    boolean isShooting;
    private String playerActiveShootingPower;

    public PlayerState currentState, previousState, currentPlayerStateTime, prevPlayerStateTime;
    public World world;
    public Body b2body;

    private TextureRegion bubbleStand;
    private TextureRegion bubbleShoot;
    private TextureRegion bubbleDead;

    private Animation bubbleShooting;
    private Animation bubbleRun;
    private Animation bubbleGetHit;
    private Animation bubbleJump;
    private Animation bubbledead;

    private PolygonShape bubbleShieldPolyShape;

    private float stateTimer;
    private float hitTimer;
    private GameManagerAITimer delayDirectionTimerOnHit;
    private boolean runningRight;
    private boolean controllerRunningRight;
    /** new in ContactListener ok */
    private boolean wallJumping; // static don't know why this was here, caused a error!!!
    /** new in ContactListener ok, not in use delete old one first!!!  */
    private boolean onGround;

    private boolean wallJumpingActive = false;

    private boolean playerIsDead;
    private boolean playerIsHit;
    private boolean time_to_reDefine;
    private boolean time_to_define;
    private boolean time_to_defineHit;
    private boolean time_to_define_Shield;
    private boolean isShieldActive;


    /** if we don't sett it here, we die -not sure why, but ned to set it later in creation so keep it!! */
    private static int playerHitMaxPerMainLifeLost = 1;
    private static int mainLifeMax = 4;
    private int playerLife = playerHitMaxPerMainLifeLost;
    private int mainLife; // = 4;

    private boolean enemyFightingHitDirection;

    private boolean timeToMovePlayer;

    private boolean contactWithPortalWorldMapTransfer;
    private boolean contactWithPortalLeveldMapTransfer;
    private boolean contactWithPortalMapTransferBoss = false;

    private boolean portalMove = false;
    private boolean bossMove = false;
    private boolean enemyHitMove = false;

    //Testing var's
    float time_on_balloon = 0f;
    float falling_to_death = 0f;

    float onGround_after_fall_or_Jumping = 0f;
    float newGround_from_Balloon = 0f;

    float deathFall = 0f;

    static boolean player_is_on_Ground = false;

    boolean newGround = false;
    boolean oldGround = false;

    public PlayerState falltestCurrentState, falltestPreviousState;


    // ?? set this to use with BallonBullet
    private PlayScreen screen;

    //fire Balloon updateAllSpawnLifeFromEnemy
    private Array<BalloneBullet> balloneBullet;

    /** Power Crystal */
    private int ballooneBulletGreen;
    private int ballooneBulletBlack;
    private int ballooneBulletBlue;
    private int ballooneBulletRed;

    private boolean knownWeaponBluePowerIsTrue = false;
    private boolean knownWeaponRedPowerIsTrue = false;
    private boolean knownWeaponBlackPowerIsTrue = false;

    private Vector2 spawnPosition;



    boolean setSaveGameBoolean = false;
    private SaveGamePlayerDataHolderClass saveGamePlayerDataHolderClass;
    private boolean mapWorldTransfer;
    private boolean mapLevelTransfer;

    // Main Create
    //public BubblePlayer(World world, PlayScreen playScreen, int hudLife) {
    //    init(world, playScreen, null, hudLife);
    //}

    // Create On player spawn from save || we don't use the spawnPosition -
    // call it directly from GameManagerAssets - use with create new only on spawn!!

    public ParticleEffect dustParticles = new ParticleEffect();

    private FloatingText floatingText;

    private float playerRealTimePosX;
    private float playerRealTimePosY;

    private GameManagerAssets gameManagerAssetsInstance;

    // remove playScreen , PlayScreen playScreen???
    public BubblePlayer(World world, Vector2 spawn, int hudlife, int maxHitperMainLifeLosted, String pwInUse, int pwGreen, int pwBlack, int pwBlue, int pwRed, boolean mapWT, boolean mapLT, GameManagerAssets instance) {
        init(world, spawn, hudlife, maxHitperMainLifeLosted, pwInUse, pwGreen, pwBlack, pwBlue, pwRed, mapWT, mapLT, instance );
    }
//, PlayScreen playScreen
    public void init(World world, Vector2 spawnPos, int hudLife, int maxHitperMainLifeLosted, String pwInUse, int pwGreen, int pwBlack, int pwBlue, int pwRed, boolean mapWT, boolean mapLT, GameManagerAssets instance){



        this.world = world;
        //this.screen = playScreen; // testing OK screen != NULL any more
        this.gameManagerAssetsInstance = instance;
        this.spawnPosition = spawnPos;

        this.mapWorldTransfer = mapWT;
        this.mapLevelTransfer = mapLT;

        // ToDo: have to change this if death and respawn!! && new game's
        this.mainLife = hudLife;
        this.playerHitMaxPerMainLifeLost = maxHitperMainLifeLosted; /** set this from AIManager */

        /** Power Crystal */
        this.ballooneBulletGreen = pwGreen;
        this.ballooneBulletBlack = pwBlack;
        this.ballooneBulletBlue = pwBlue;
        this.ballooneBulletRed = pwRed;

        /** Set's the player power to Default */
        //ToDo:: Set the player power from last saveGame if it exist
        this.playerActiveShootingPower = pwInUse; //"1";



        currentState = PlayerState.IDLE;
        previousState = PlayerState.IDLE;
        contactWithPortalWorldMapTransfer = false;
        contactWithPortalLeveldMapTransfer = false;
        stateTimer = 0;
        hitTimer = 0; // Blinking Animation on Hit by enemy
        delayDirectionTimerOnHit = new GameManagerAITimer();
        runningRight = true;
        enemyFightingHitDirection = false; // Player is for the most part coming towards enemy from left to right, so enemy is face'ing left

        //this will prob. be in a another class called Animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //for (int i = 1; i < 4; i++) {
        //	frames.add(new TextureRegion(screen.getAtlas().findRegion("BubbleA"), i * 64, 0, 64, 64));
        //}

        //Test
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleA"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleB"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleA"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleB"), 0, 0, 64, 64));

         /// don't activate again //frames.add(new TextureRegion((Utility.PLAYER_TEXTUREATLAS).findRegion("BubbleB"), 0, 0, 64, 64));
    /*
            frames.add(new TextureRegion(screen.getAtlasPlayer().findRegion("BubbleA"), 0, 0, 64, 64)); // etc A B A B - must have get'ers in MainGameScreen
            frames.add(new TextureRegion(Utility.getTextureAtlas(Utility.PLAYER_TEXTURE_ATLAS_PATH).findRegion("BubbleB"), 0, 0, 64, 64)); // must be public in Utility
    */
        bubbleRun = new Animation(0.4f, frames);

        //GameUtility.assetManager.load("spriteAtlas/player/newPlayer2.atlas", TextureAtlas.class);
        /*
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer2.atlas").findRegion("run1"), 0, 0, 375, 370));

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer2.atlas").findRegion("run2"), 0, 0, 375, 370));

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer2.atlas").findRegion("run3"), 0, 0, 375, 370));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer2.atlas").findRegion("run4"), 0, 0, 375, 370));

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer2.atlas").findRegion("run5"), 0, 0, 375, 370));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer2.atlas").findRegion("run6"), 0, 0, 375, 370));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer2.atlas").findRegion("run7"), 0, 0, 375, 370));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer2.atlas").findRegion("run8"), 0, 0, 375, 370));


        bubbleRun = new Animation(0.1f, frames);
*/
        frames.clear();

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleA"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerHitAtlas.atlas").findRegion("BubbleA"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleA"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerHitAtlas.atlas").findRegion("BubbleA"), 0, 0, 64, 64));

        bubbleGetHit = new Animation(0.2f, frames);

        frames.clear();

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleD0"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleD1"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleD2"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleD3"), 0, 0, 64, 64));

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleD6"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleD8"), 0, 0, 64, 64));

        bubbledead = new Animation(0.2f, frames);

        frames.clear();
            /*
            for (int i = 4; i < 6; i++)
                frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
            marioJump = new Animation(0.1f, frames);
            frames.clear();
            */

        //get jump animation frames and add them to marioJump Animation
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleD0"), 0, 0, 64, 64));
        bubbleJump = new Animation(0.1f, frames);
        frames.clear();




        bubbleStand = new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleA"), 0, 0, 64, 64);
        //bubbleStand = new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer0.atlas").findRegion("newPlayerAlpha"), 0, 0, 70, 136);
        //bubbleStand = new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/player/newPlayer2.atlas").findRegion("run1"), 0, 0, 260, 333);

        //get Shooting Animation havent got this to work !!

        frames.add(new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleA"), 0, 0, 64, 64));

        bubbleShooting = new Animation(0.2f, frames);
        frames.clear();

        bubbleShoot = new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/player/BubblePlayerAtlas.atlas").findRegion("BubbleA"), 0, 0, 64, 64);

        // Particles
        dustParticles.load(Gdx.files.internal("particles/dust.pfx"), Gdx.files.internal("particles")); //ok


        defineBubble();
        setBounds(0, 0, 32 / GameUtility.PPM, 32 / GameUtility.PPM); // Bubblebubble player Way to small other then bubb.
        //setBounds(0, 0, 64 / GameUtility.PPM, 64 / GameUtility.PPM); // test player
        //setBounds(0, 0, 128 / GameUtility.PPM, 128 / GameUtility.PPM); // test player
        setRegion(bubbleStand);


        balloneBullet = new Array<BalloneBullet>();

        // this for actual state of the enemy ???!!!
        this.previousState = this.currentState = PlayerState.IDLE; // EnemyBossState.SET_INACTIVE_STATE;
        // this for animation time sequence
        this.prevPlayerStateTime = this.currentPlayerStateTime = PlayerState.IDLE; // EnemyBossState.SET_INACTIVE_STATE;
        //setPlayerState(PlayerState.IDLE);



    }




    Array<Fixture> fixList = new Array<Fixture>();
    int player_dust_particle_time = 0;
    public void update(float dt){

        /** check's if player have got wall jumping Power Up etc */
        if(wallJumpingActive) {
            if (!wallJumping ) {
                b2body.setLinearDamping(0f);
                //System.out.println("! No LinearDamping ");
            } else {

                if(!isDead()){
                    b2body.setLinearDamping(10f);
                    //System.out.println("LinearDamping ");
                }else {
                    b2body.setLinearDamping(0f);
                }

            }
        }


        //currentState, previousState, currentPlayerStateTime, prevPlayerStateTime;
        //System.out.println("BubblePlayer Class (Update) C.State: (" + getPlayerState() + ") P.State: (" + getprePlayerState() +")");
        //System.out.println("BubblePlayer Class (Update) Speed X: " + b2body.getLinearVelocity().x + " Y: " + b2body.getLinearVelocity().y );

//System.out.println("BubblePlayer Class: Test OnGround " + getPlayerOnGround());

        if(getPlayerWallJump()){
            //System.out.println("BubblePlayer Class : WallJumping is;  True");
            //defineBubbleWall();
        }
/*
        world.getFixtures(fixList);
        System.out.println( "bubblePlayer Class : fixture size: " + fixList.size );
        for(int i=0; i < fixList.size; i++) {

            System.out.println( "bubblePlayer Class : fixture read ? : " +  fixList.get(i).getUserData() );
        }
*/



        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        playerRealTimePosX = (b2body.getPosition().x - getWidth() / 2);
        playerRealTimePosY = (b2body.getPosition().y - getHeight() / 2);
       /*
        System.out.println( "playerPositionX " + (b2body.getPosition().x - getWidth() / 2) +
                "playerPositionY " + (b2body.getPosition().y - getHeight() / 2));
        */


        setRegion(getFrame(dt));

//ToDo:: Testing effects update, pos and start/end
        dustParticles.update(dt);


        if (b2body.getLinearVelocity().x != 0 && !runningRight) {
            //dustParticles.setPosition(b2body.getPosition().x + 16 / GameUtility.PPM, b2body.getPosition().y);
            //dustParticles.start();
            //dustParticles.allowCompletion();
        }

        if (b2body.getLinearVelocity().x != 0 && runningRight) {
            //dustParticles.setPosition(b2body.getPosition().x - 16 / GameUtility.PPM, b2body.getPosition().y);
            //dustParticles.start();
            //dustParticles.allowCompletion();
        }


        if (b2body.getLinearVelocity().x == 0) {
            //dustParticles.allowCompletion();
        }
        if(!dustParticles.isComplete()){
            dustParticles.allowCompletion();
            //System.out.println("dust Particles is completed");
        }

        if(getPlayerWallJump() && currentState.equals(PlayerState.JUMPING) ){

            dustParticles.setPosition( - 5 / GameUtility.PPM + b2body.getPosition().x, b2body.getPosition().y + 20 / GameUtility.PPM);
            dustParticles.start();
            dustParticles.setDuration(0);
            dustParticles.reset();
        }else {
            dustParticles.allowCompletion();
        }


        if(getPlayerOnGroundNew() && currentState.equals(PlayerState.JUMPING) ){

            dustParticles.setPosition( b2body.getPosition().x , b2body.getPosition().y - 25 / GameUtility.PPM);
            dustParticles.start();
            dustParticles.setDuration(0);
            dustParticles.reset();
        }else {
            dustParticles.allowCompletion();

        }

//ToDo:: End

        if(playerLife == 0 || playerLife < 0) {
            die();
            //System.out.println("BubblePlayer Class playerIsDead: " + playerIsDead);
        }

        if(isHit()) {
            hitTimer += dt;
            delayDirectionTimerOnHit.updateCurrentTime(dt);
            //System.out.println("BubblePlayer Class define Time " + delayDirectionTimerOnHit.getCurrentTime() );

            //time_to_defineHit = true;

            if(hitTimer > 0.7f){ // old 7
                setIsHit(false); // stop Animation Hit!!!
                //b2body.setLinearVelocity(0f,0f);

                if(mainLife != 0){
                   time_to_reDefine = true;
                }
                hitTimer = 0;
            }
        }

        /** if Player get Hit */
        if(time_to_defineHit){
           defineBubbleHit();
        }

        /** After Player get Hit */
        if(time_to_reDefine){
           reDefineBubble();
        }

        /** if Player activate Shield */
        if(time_to_define_Shield){
            defineBubbleShield();
        }

         for(BalloneBullet  ball : balloneBullet) {
            ball.update(dt);

            if(ball.getHitwithBullet()){
                ball.setToDestroy();
            }
            if(ball.isDestroyed())
                balloneBullet.removeValue(ball, true);
        }

    }

    public void removeLifeFallToDeath(){

        if(this.mainLife > 1){
            mainLife--;
        }

    }

    public void setExtraLife(){

        // ToDo : if we need extra life bars, remove hardcoding >= 4 inn ContactListener / change
        //if( !(this.mainLife >= 4)){

            this.mainLife += 1;
        //}


    }

    public void setIsDead(boolean status){
        playerIsDead = status;
    }

    public boolean isDead(){
        return playerIsDead;
    }

    public void setIsHit(boolean status) {
        playerIsHit = status;
    }

    public boolean isHit(){
        return playerIsHit;
    }

    public TextureRegion getFrame(float dt){



        currentPlayerStateTime = currentState = getState();
        //currentState = getState(); //old

        TextureRegion region = null;

        switch(currentState) {
            case DEAD:
                region = (TextureRegion)bubbledead.getKeyFrame(stateTimer);
                break;
            case JUMPING:
                region = (TextureRegion)bubbleJump.getKeyFrame(stateTimer);
                //region = (TextureRegion)bubbleGetHit.getKeyFrame(stateTimer, true); // just testing the animation
                //System.out.println("Jumping!!");
                break;
            case RUNNING:
                region = (TextureRegion)bubbleRun.getKeyFrame(stateTimer, true );
                break;
            case HIT:
                region = (TextureRegion)bubbleGetHit.getKeyFrame(stateTimer, true);
                break;
            case SHOOTING:
                region = (TextureRegion)bubbleShooting.getKeyFrame(stateTimer, true); //bubbleShoot; //something with input controllers check!!
            case FALLING: //will have a animation for fall ?
            case ONBALLONE:
            case IDLE: // We stand and do nothing fiddling with our thumbs /Old Standing
                region = bubbleStand; // change to idle animation :P
                break;
        }
        // fixed on hit from enemy facing left OK
        if(!isHit()) {
            //System.out.println("BubblePlayer Class isHit() region.isFlipX()'ing!! ");
            if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
                region.flip(true, false);
                runningRight = false;
            } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
                region.flip(true, false);
                runningRight = true;
            }

        }

/*
            if(!enemyFightingHitDirection && !region.isFlipX() && runningRight) {
                region.flip(false, false);
                runningRight = true; //true;
                System.out.println("BubblePlayer Class getFrame isHit direction of player True " + enemyFightingHitDirection + " region " + region.isFlipX() + " runningDir " + runningRight);
            }

            if(!enemyFightingHitDirection && !region.isFlipX() && !runningRight) {
                region.flip(false, false);
                runningRight = false; //true;
                System.out.println("BubblePlayer Class getFrame isHit direction of player False " + enemyFightingHitDirection + " region " + region.isFlipX() + " runningDir " + runningRight);
            }
*/

        stateTimer = currentPlayerStateTime == prevPlayerStateTime ? stateTimer + dt : 0;
        prevPlayerStateTime = currentPlayerStateTime;

        return region;

    }

    //Test with WorldContact Listener
    public static void playerOnGround() {
        player_is_on_Ground = true; // WorldContact begin
    }

    public static void playerLeftGround() {
        player_is_on_Ground = false; // WorldContact End
    }

    // Balloon
    public boolean getPlayerOnGround(){
        return player_is_on_Ground; // Get Value Begin/end ground boolean
    }

    /** Contact listener set's this on pick up -POWER- */
    /** & */
    /** PlayScreen set's this from save file/object  - Checks playerSave Game - */
    public void setPlayerPowerUp(String value){

        switch (value){
            case "WALL JUMPING":
                this.wallJumpingActive = true;
                //addPlayerPowersToSaveGame("wall jumping");
                break;
            case "WEAPON BLUE":
                this.knownWeaponBluePowerIsTrue = true;
        }


    }

    public boolean getPlayerKnownWeaponPowerBlue(){
        return this.knownWeaponBluePowerIsTrue;
    }

    public  void setPlayerWallJumpTrue() { this.wallJumping = true; }
    public  void setPlayerWallJumpFalse() { this.wallJumping = false; }
    public boolean getPlayerWallJump() { return this.wallJumping; }


    public void setPlayerOnGroundTrue(){ this.onGround = true; }
    public void setPlayerOnGroundFalse(){ this.onGround = false; }
    public boolean getPlayerOnGroundNew(){return this.onGround; }

    //updateAllSpawnLifeFromEnemy : have level contact begin / end need more work - but left as is to do enemy 24/10-17 kl 08:53
    public void testFallingToDeath(float dt){

        falltestCurrentState = getState();

        if(falltestCurrentState == PlayerState.FALLING && falltestPreviousState == PlayerState.JUMPING ||
                falltestCurrentState == PlayerState.FALLING && falltestPreviousState == PlayerState.RUNNING){
            falling_to_death = 0;
            falling_to_death = b2body.getPosition().y;
//System.out.println("Flag : Falling/jumping");
        }

        if(getPlayerOnGround()) {
//System.out.println("Player is on Ground");
            onGround_after_fall_or_Jumping = b2body.getPosition().y;
        }else{
//System.out.println("Player left Ground");
        }
        if(falltestCurrentState == PlayerState.JUMPING  && falltestPreviousState == PlayerState.ONBALLONE ){

            newGround_from_Balloon = b2body.getPosition().y;
            time_on_balloon += dt;
            System.out.println("Use : newGround_from_Balloon");

        }


        //calculate
        if( !getPlayerOnGround() &&  newGround_from_Balloon > 0f ) {
            //Take new calculating as the balloon will be the floor

            //deathFall = onGround_after_fall_or_Jumping - newGround_from_Balloon; // Highest value in back !!?
            /* deathFall 0.40808272 onGround_after_fall_or_Jumping  2.584961 newGround_from_Balloon 2.9930437 Value in front */
            deathFall =  newGround_from_Balloon - onGround_after_fall_or_Jumping; // Highest value in front !!?
            System.out.println("deathFall " + deathFall + " onGround_after_fall_or_Jumping  " + onGround_after_fall_or_Jumping + " newGround_from_Balloon " + newGround_from_Balloon);

            // testing to set newGround -> to 0 so we only check it once per cycle ?
            newGround_from_Balloon = 0;

        }else if( getPlayerOnGround() && falling_to_death > 0f ) {

            deathFall = falling_to_death - onGround_after_fall_or_Jumping;
            System.out.println("deathFall " + deathFall + " falling_to_death  " + falling_to_death + " onGround_after_fall_or_Jumping " + onGround_after_fall_or_Jumping);

            //testing to set falling_to -> to  0 so we only check if once per cycle ?
            falling_to_death = 0;
        }


        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = falltestCurrentState == falltestPreviousState ? stateTimer + dt : 0;

        //this worked we got preState and curState
        //System.out.println("preFallState : " + falltestPreviousState + " curFallState : " + falltestCurrentState);

        falltestPreviousState = falltestCurrentState;
    }



    public boolean isPlayerOnABallone(){

        boolean returnValue = false;
        for(BalloneBullet ball : balloneBullet) {
            //return value true if contact between BALLONE_HEAD_BIT | PLAYER_BIT contactListener
            if(ball.getPlayerOnBallon()){
                returnValue = true;
                break;
            }else{
                returnValue = false;
            }
        }
        return  returnValue;
    }

    public PlayerState getState() {

        if(playerIsDead) {
            setPlayerState(PlayerState.DEAD);
            return getPlayerState();
        }else {
            if (playerIsHit) {//playerIsDead) {
                setPlayerState(PlayerState.HIT); //return PlayerState.DEAD;
                return getPlayerState();

            } else {

                if (b2body.getLinearVelocity().x != 0 && !(b2body.getLinearVelocity().y > 0) && !(b2body.getLinearVelocity().y < 0)) {
                    setPlayerState(PlayerState.RUNNING);
                    return getPlayerState();
                } else {

                    if (b2body.getLinearVelocity().y < 0) {
                        setPlayerState(PlayerState.FALLING);
                        return getPlayerState();
                    } else {
                        if (b2body.getLinearVelocity().y > 0) {
                            setPlayerState(PlayerState.JUMPING);
                            return getPlayerState();
                        } else {
                            setPlayerState(PlayerState.IDLE);
                            return getPlayerState();
                        }
                    }
                }
            }
        }
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void jump() {
//System.out.println("Player Class Jump -Power's active: " + this.wallJumpingActive  );
        System.out.println("running Controller right: " + this.controllerRunningRight );


        /** Use with Android Controller */
        if(this.wallJumpingActive) {
            if (this.wallJumping) {

                //b2body.setLinearVelocity(0f, 0f); // forgot this ? testing
                //b2body.setLinearDamping(0f);

                if (controllerRunningRight) { // runningRight ,
                    b2body.setLinearVelocity(0f,0f);
                    b2body.applyLinearImpulse(new Vector2(-2.0f, 5.6f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
                    currentState = PlayerState.JUMPING;
                } else {
                    b2body.setLinearVelocity(0f,0f);
                    b2body.applyLinearImpulse(new Vector2(2.0f, 5.6f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
                    currentState = PlayerState.JUMPING;
                }
            }
        }

        if( currentState != PlayerState.JUMPING  && currentState != PlayerState.FALLING && currentState != PlayerState.HIT && !isDead() ) {

            //b2body.setLinearDamping(0f);
            //b2body.setLinearVelocity(b2body.getLinearVelocity().x, 0f); // forgot this ? testing!!!!
            b2body.setLinearVelocity(0f,0f);
            b2body.applyLinearImpulse(new Vector2(0, 5.6f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
            currentState = PlayerState.JUMPING;
            //System.out.println("BubblePlayer Jumping VelY: " + b2body.getLinearVelocity().y + " PosY: " + b2body.getPosition().y );
        }


    }

    public void jumpDesktop() {
//System.out.println("Player Class Jump -Power's active: " + this.wallJumpingActive  );
        System.out.println("running Controller right: " + this.controllerRunningRight );


        /** Use with Android Controller */
        if(this.wallJumpingActive) {
            if (this.wallJumping) {

                //b2body.setLinearVelocity(0f, 0f); // forgot this ? testing
                //b2body.setLinearDamping(0f);

                if (runningRight) { // runningRight ,controllerRunningRight
                    b2body.setLinearVelocity(0f,0f);
                    b2body.applyLinearImpulse(new Vector2(-2.0f, 5.6f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
                    currentState = PlayerState.JUMPING;
                } else {
                    b2body.setLinearVelocity(0f,0f);
                    b2body.applyLinearImpulse(new Vector2(2.0f, 5.6f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
                    currentState = PlayerState.JUMPING;
                }
            }
        }

        if( currentState != PlayerState.JUMPING  && currentState != PlayerState.FALLING && currentState != PlayerState.HIT && !isDead() ) {

            //b2body.setLinearDamping(0f);
            //b2body.setLinearVelocity(b2body.getLinearVelocity().x, 0f); // forgot this ? testing!!!!
            b2body.setLinearVelocity(0f,0f);
            b2body.applyLinearImpulse(new Vector2(0, 5.6f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
            currentState = PlayerState.JUMPING;
            //System.out.println("BubblePlayer Jumping VelY: " + b2body.getLinearVelocity().y + " PosY: " + b2body.getPosition().y );
        }


    }

    // ToDo : not fhinished with this one
    public void defineBubbleShield(){
        System.out.println("Player Class Define: defineBubbleShield is used!!");

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        if(gameManagerAssetsInstance.gameManagerSaveFilePlayerExists()) {

            //System.out.println("BubblePlayer Class -defineBubble MapTransfer: " + mapWorldTransfer );

            if(mapWorldTransfer) {
                //  System.out.println("BubblePlayer Class - SaveGame Exists  & We have WorldTransfer");
                /*
                System.out.println("BubblePlayer Class - World: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
                        " Level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() +
                        " SavePoint: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointMarker() +
                        " Position: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointPosition() +
                        " PlayerLife: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife());
                */
                bdef.position.set(gameManagerAssetsInstance.getMapSpawnStartPosition().x, gameManagerAssetsInstance.getMapSpawnStartPosition().y + 2.4f );
                mapWorldTransfer = false;
                //System.out.println("BubblePlayer Class - Set SpawnStartPosition!!");

            }else{
                //System.out.println("BubblePlayer Class - SaveGame Exists");
                /*
                System.out.println("BubblePlayer Class - World: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
                        " Level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() +
                        " SavePoint: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointMarker() +
                        " Position: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointPosition() +
                        " PlayerLife: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife() );
                */
                /** her is the position we must use for define move if true!!!  */
                if(time_to_reDefine) { // we use time_to_reDefine all so with shield !!??
                    bdef.position.set(position);
                }else{
                    bdef.position.set(gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointPosition().x,
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointPosition().y + 2.4f);
                }

            }
        }else{
            //System.out.println("BubblePlayer Class - SaveGame Don't Exists");
            /*
            System.out.println("BubblePlayer Class - World: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
                    " Level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() +
                    " SavePoint: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointMarker() +
                    " Position: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointPosition() +
                    " PlayerLife: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife() );
            */
            /**  if No saveFile Or saveFile is deleted and the game is removed from buffer we need this!! */
            bdef.position.set( this.spawnPosition.x, this.spawnPosition.y + 2.4f );  //GameManagerAssets.instance.getMapSpawnStartPosition());
            //System.out.println("BubblePlayer Class - Set SpawnStartPosition!! " + this.spawnPosition);
        }
        bdef.type = BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(22 / 2 / GameUtility.PPM);


        fdef.filter.categoryBits = GameUtility.PLAYER_BIT;

        /** testing !! with walls and ground ok !!! */
        fdef.filter.groupIndex = GameUtility.PLAYER_BIT;

        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_AND_SEALING_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBJECT_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.GAME_ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        FixtureDef fdefBoxShieldArea = new FixtureDef();
        bubbleShieldPolyShape = new PolygonShape();

        fdefBoxShieldArea.shape = bubbleShieldPolyShape;
        bubbleShieldPolyShape.setAsBox( b2body.getLocalCenter().x + 25 / GameUtility.PPM, b2body.getLocalCenter().y + 25 / GameUtility.PPM);

        /**  use same categoryBit - but then have to implement a check if shield is active
         *
         *  and all the bit for enemy will not be active on upper defineBubbleShield()
         *
         * */
        fdefBoxShieldArea.filter.categoryBits = GameUtility.PLAYER_BIT;
        fdefBoxShieldArea.filter.maskBits = GameUtility.ENEMY_BIT |
                GameUtility.ENEMY_BOTTOM_BIT |
                GameUtility.ENEMY_RANGE_ATTACK_BIT |
                GameUtility.ENEMY_CLOSE_ATTACK_BIT |
                GameUtility.ENEMY_LEGS_BIT |
                GameUtility.ENEMY_POWER_BIT;

        fdefBoxShieldArea.isSensor = true;
        b2body.createFixture(fdefBoxShieldArea).setUserData(this);

        time_to_reDefine = false;
        //time_to_define_Shield = false;
    }

    /** Use when player get Hit - Start Hit Timer - */
    public void defineBubbleHit(){
        //System.out.println("Player Class Define: defineHit is used!!");

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyType.DynamicBody;




        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(22 / 2 / GameUtility.PPM);

        fdef.filter.categoryBits = GameUtility.PLAYER_BIT;

        /** testing !! with walls and ground ok !!! */
        fdef.filter.groupIndex = GameUtility.PLAYER_BIT;

        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_AND_SEALING_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBJECT_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.GAME_ITEM_BIT;
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);


        //System.out.println("do Impulse jump!!");

        if(enemyFightingHitDirection) {
            //if(!isDead()) {
            //System.out.println("Player getHit - defineHit()");
            b2body.setLinearVelocity(0f, 0f);
            b2body.applyLinearImpulse(new Vector2(2.0f, 4.2f), b2body.getWorldCenter(), true);
            //}else {
                //b2body.setLinearVelocity(0f, 0f);
            //    b2body.applyLinearImpulse(new Vector2(0f, 4f), b2body.getWorldCenter(), true);
            //}
        }else {
            //if(!isDead()) {
            //System.out.println("Player getHit - defineHit()");
            b2body.setLinearVelocity(0f, 0f);
            b2body.applyLinearImpulse(new Vector2(-2.0f, 4.2f), b2body.getWorldCenter(), true);
            //}else {
                //b2body.setLinearVelocity(0f, 0f);
             //   b2body.applyLinearImpulse(new Vector2(0f, 4f), b2body.getWorldCenter(), true);
            //}
        }

        time_to_defineHit = false;

    }

    /** Use after player gets Hit && Timer of Hit run's out */
    public void reDefineBubble(){
        //System.out.println("Player Class Define: reDefine is used!!");

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        if(gameManagerAssetsInstance.gameManagerSaveFilePlayerExists()) {

            //System.out.println("BubblePlayer Class -defineBubble MapTransfer: " + mapWorldTransfer );

            if(mapWorldTransfer) {
                //  System.out.println("BubblePlayer Class - SaveGame Exists  & We have WorldTransfer");
                /*
                System.out.println("BubblePlayer Class - World: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
                        " Level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() +
                        " SavePoint: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointMarker() +
                        " Position: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointPosition() +
                        " PlayerLife: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife());
                */
                //bdef.position.set(GameManagerAssets.instance.getMapSpawnStartPosition().x, GameManagerAssets.instance.getMapSpawnStartPosition().y + 0.4f );
                //spawnPosition
                bdef.position.set(spawnPosition );
                mapWorldTransfer = false;
                //System.out.println("BubblePlayer Class - Set SpawnStartPosition!!");

            }else{
                //System.out.println("BubblePlayer Class - SaveGame Exists");
                /*
                System.out.println("BubblePlayer Class - World: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
                        " Level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() +
                        " SavePoint: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointMarker() +
                        " Position: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointPosition() +
                        " PlayerLife: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife() );
                */
                /** her is the position we must use for define move if true!!!  */
                if(time_to_reDefine) { // we use time_to_reDefine all so with shield !!??
                    bdef.position.set(position);
                }else{
                    bdef.position.set(gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointPosition().x,
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointPosition().y + 0.4f);
                }

            }
        }else{
            //System.out.println("BubblePlayer Class - SaveGame Don't Exists");
            /*
            System.out.println("BubblePlayer Class - World: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
                    " Level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() +
                    " SavePoint: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointMarker() +
                    " Position: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointPosition() +
                    " PlayerLife: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife() );
            */
            /**  if No saveFile Or saveFile is deleted and the game is removed from buffer we need this!! */
            bdef.position.set( this.spawnPosition.x, this.spawnPosition.y + 0.4f );  //GameManagerAssets.instance.getMapSpawnStartPosition());
            //System.out.println("BubblePlayer Class - Set SpawnStartPosition!! " + this.spawnPosition);
        }
        bdef.type = BodyType.DynamicBody;



        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(22 / 2 / GameUtility.PPM);

        // this makes it so we have to use bit to control how we act!??
        fdef.filter.categoryBits = GameUtility.PLAYER_BIT;

        /** testing !! with walls and ground ok !!! */
        fdef.filter.groupIndex = GameUtility.PLAYER_BIT;

        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_AND_SEALING_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBJECT_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.GAME_ITEM_BIT |
                GameUtility.ENEMY_CLOSE_ATTACK_BIT |
                GameUtility.ENEMY_RANGE_ATTACK_BIT |
                GameUtility.ENEMY_POWER_BIT |
                GameUtility.ENEMY_LEGS_BIT |
                GameUtility.ENEMY_BOTTOM_BIT |
                GameUtility.ENEMY_BIT;

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
        time_to_reDefine = false;
    }

    /** Use when first run Game Sets here the start point spawn !!! */
    public void defineBubble() {
        //System.out.println("Player Class Define: define is used!!");

        BodyDef bdef = new BodyDef();
        if(gameManagerAssetsInstance.gameManagerSaveFilePlayerExists()) {

            //System.out.println("BubblePlayer Class -defineBubble MapTransfer: " + mapWorldTransfer );
            /** playScreen cratePlayer with true */
            if(mapWorldTransfer) {
              //  System.out.println("BubblePlayer Class - SaveGame Exists  & We have WorldTransfer");
                /*
                System.out.println("BubblePlayer Class - World: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
                        " Level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() +
                        " SavePoint: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointMarker() +
                        " Position: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointPosition() +
                        " PlayerLife: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife());
                */
                // this calls it directly should not have to , as we set it in as input
                //bdef.position.set(GameManagerAssets.instance.getMapSpawnStartPosition().x, GameManagerAssets.instance.getMapSpawnStartPosition().y + 1.4f );
                //spawnPosition
                //if(GameManagerAssets.instance.getPortalTravelDirection().equals("LEFT")){
                //    bdef.position.set(spawnPosition);
                //}else {
                    bdef.position.set(spawnPosition);
                //}
                mapWorldTransfer = false;
                //System.out.println("BubblePlayer Class - Set SpawnStartPosition!!");

            }else{
                //System.out.println("BubblePlayer Class - SaveGame Exists");
                /*
                System.out.println("BubblePlayer Class - World: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
                        " Level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() +
                        " SavePoint: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointMarker() +
                        " Position: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointPosition() +
                        " PlayerLife: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife() );
                */
                if(mapLevelTransfer){
                  //  if(GameManagerAssets.instance.getPortalTravelDirection().equals("LEFT")){
                  //      bdef.position.set(spawnPosition );//.add( - 0.3f, 0.4f ));
                        //System.out.println("Player position after travel: " + spawnPosition.x + " y: " + spawnPosition.y );
                  //  }else {
                        bdef.position.set(spawnPosition); // .add( 0.3f, 0.4f ));
                        //System.out.println("Player position after travel: " + spawnPosition.x + " y: " + spawnPosition.y );
                  //  }

                    mapLevelTransfer = false;
                }else {
                    /** her is the position we change in reDefine bubble  */
                    System.out.println("Player use last save point, that is saved!!");
                    bdef.position.set(gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointPosition().x,
                            gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().getSavePointPosition().y + 0.4f);
                }
            }
        }else{
            //System.out.println("BubblePlayer Class - SaveGame Don't Exists");
            /*
            System.out.println("BubblePlayer Class - World: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointWorld() +
                    " Level: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointLevel() +
                    " SavePoint: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointMarker() +
                    " Position: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePointPosition() +
                    " PlayerLife: " + GameManagerAssets.instance.getSaveGamePlayerDataHolderClass().getSavePlayerMainLife() );
            */
            /**  if No saveFile Or saveFile is deleted and the game is removed from buffer we need this!! */
            bdef.position.set( this.spawnPosition.x, this.spawnPosition.y + 0.4f );  //GameManagerAssets.instance.getMapSpawnStartPosition());
            //System.out.println("BubblePlayer Class - Set SpawnStartPosition!! " + this.spawnPosition);
        }
        bdef.type = BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        //shape.setRadius(22 / 2 / GameUtility.PPM);
        shape.setRadius(22 / 2 / GameUtility.PPM);

        // this makes it so we have to use bit to control how we act!??
        fdef.filter.categoryBits = GameUtility.PLAYER_BIT;

        /** testing !! with walls and ground ok !!! */
        fdef.filter.groupIndex = GameUtility.PLAYER_BIT;

        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_AND_SEALING_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBJECT_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.GAME_ITEM_BIT |
                GameUtility.ENEMY_CLOSE_ATTACK_BIT |
                GameUtility.ENEMY_RANGE_ATTACK_BIT; /* |
                GameUtility.ENEMY_POWER_BIT |
                GameUtility.ENEMY_LEGS_BIT |
                GameUtility.ENEMY_BOTTOM_BIT |
                GameUtility.ENEMY_BIT;
*/

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);



/*
		PolygonShape head = new PolygonShape();
        Vector2[] verticehead = new Vector2[4];
        verticehead[0] = new Vector2(-6, 11).scl(1 / Utility.PPM);
        verticehead[1] = new Vector2(6, 11).scl(1 / Utility.PPM);
        verticehead[2] = new Vector2(-3, 3).scl(1 / Utility.PPM);
        verticehead[3] = new Vector2(3, 3).scl(1 / Utility.PPM);
        head.set(verticehead);

        fdef.shape = head;
        fdef.filter.categoryBits = Utility.PLAYER_HEAD_BIT;
        //fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
*/
/*

        EdgeShape Head = new EdgeShape();
        Head.set(new Vector2(-4 / GameUtility.PPM , 12 / GameUtility.PPM), new Vector2( 4 / GameUtility.PPM, 12 / GameUtility.PPM));
        fdef.filter.categoryBits = GameUtility.PLAYER_HEAD_BIT;
        fdef.shape = Head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
*/
/*
		EdgeShape lowerHead = new EdgeShape();
		lowerHead.set(new Vector2(-2 / 14f , 6 / GameUtility.PPM), new Vector2( 2 / 14f, 6 / GameUtility.PPM));
        fdef.filter.categoryBits = GameUtility.PLAYER_HEAD_BIT;
        fdef.shape = lowerHead;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
*/
    }


    public void dash(int powerDash) {
        //if( currentState != PlayerState.JUMPING  && currentState != PlayerState.FALLING && currentState != PlayerState.HIT && !isDead() ) {
        if( currentState != PlayerState.HIT && !isDead() ) {
            if(runningRight) {

                if(!currentState.equals(PlayerState.JUMPING ) ){
                    b2body.setLinearVelocity(0,0);
                    //if(!(b2body.getLinearVelocity().x > 3.5f) && !(b2body.getLinearVelocity().y > 0f)) {
                    b2body.applyLinearImpulse(new Vector2(3.5f, 0f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
                    //}
                }else {
                    b2body.setLinearVelocity(0, 0);
                    //if(!(b2body.getLinearVelocity().x > 3.5f) && !(b2body.getLinearVelocity().y > 2.0f)) {
                    b2body.applyLinearImpulse(new Vector2(3.5f, 2.0f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
                    //}
                }
            }else {
                if(!currentState.equals(PlayerState.JUMPING ) ){
                    b2body.setLinearVelocity(0,0);
                    //if(!(b2body.getLinearVelocity().x > -3.5f) && !(b2body.getLinearVelocity().y > 0f)) {
                    b2body.applyLinearImpulse(new Vector2(-3.5f, 0f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
                    //}
                }else {
                    b2body.setLinearVelocity(0, 0);
                    //if(!(b2body.getLinearVelocity().x > -3.5f) && !(b2body.getLinearVelocity().y > 2.0f)) {
                    b2body.applyLinearImpulse(new Vector2(-3.5f, 2.0f), b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f
                    //}
                }
            }
            //System.out.println("BubblePlayer Jumping VelY: " + b2body.getLinearVelocity().y + " PosY: " + b2body.getPosition().y );
        }
    }

    public void fire(int power){
        //isShooting = true;

        switch (power) {
            case 1:
                balloneBullet.add(new BalloneBullet(world, b2body.getPosition().x, b2body.getPosition().y, 1, runningRight ? true : false));//screen, b2body.getPosition().x, b2body.getPosition().y, 1, runningRight ? true : false));
                //System.out.println("player is shooting: " + isShooting);

                //isShooting = false;
                break;
            case 2:
                if(this.ballooneBulletBlue != 0) {
                  balloneBullet.add(new BalloneBullet(world, b2body.getPosition().x, b2body.getPosition().y, 2, runningRight ? true : false));
                  this.ballooneBulletBlue--;
                  //isShooting = false;
                }
                break;
        }
        //isShooting = false;
    }

    public void setPlayerActiveShootingPower(String value){

        playerActiveShootingPower = value;

    }

    public String getPlayerActvieShootingPower(){
        return playerActiveShootingPower;
    }

    public void draw(Batch batch){
        super.draw(batch);

        dustParticles.draw(batch);

        for(BalloneBullet ball : balloneBullet) {
         if(!ball.isDestroyed()) {
                ball.draw(batch);
            }
        }
    }

    //public void setNewPositionOnPlayer(Portal data){}



    public boolean changeWorldMap() { return this.contactWithPortalWorldMapTransfer; }

    public boolean changeLevelMap(){
        return this.contactWithPortalLeveldMapTransfer;
    }

    public boolean changeMapWithBoss() {
        return this.contactWithPortalMapTransferBoss;
    }


    public void onPortalTravelHit(String travelFrom, String travelWorld, String travelLevel){

        if(travelFrom.equals("LEVEL")){
            contactWithPortalLeveldMapTransfer = true;
            //DESTINATION / travelTo = portal id
            //System.out.println("Portal travel to same Level portal nr: " + travelTo);

        }else if(travelFrom.equals("WORLD")){
            contactWithPortalWorldMapTransfer = true;
            //DESTINATION / travelTo = world map
            //System.out.println("Portal travel to World nr: " + travelTo);
        }
    }

    public boolean getSaveGameBoolean(){
        return this.setSaveGameBoolean;
    }

    public void setSetSaveGameBooleanFalse(){
        this.setSaveGameBoolean = false;
    }

    /** Might use userData later ? */
    public void contactWithEnemyClose(EnemyGraphicSensor userData) {

        HitbyEnemy(!runningRight);
    }

    public void contactWithEnemyClose(EnemyStalactite userData) {
        HitbyEnemy(!runningRight);
    }


    public void contactWithEnemyBBullet(EnemyBullet userData){
        HitbyEnemy( true); //userData.getHitwithBullet() );
    }

    public void contactWithEnemyClose(EnemyA userData) {

        HitbyEnemy(userData.getIsRunningRight());
    }

    public void contactWithEnemyClose(EnemyB userData) {

        HitbyEnemy(userData.getIsRunningRight());
    }

    // hit touch on body
    public void contactWithEnemyClose(EnemyKnightDevil userData) {
        HitbyEnemy(userData.getIsRunningRight());
    }

    // hit with power's
    public void contactWithEnemyClose(EnemyKnightPowerSword userData) {
        HitbyEnemy(userData.getFireDirection());
    }

    public void contactWithEnemyRange(EnemyKnightRangeAttack userDataBullet) {


        if(userDataBullet.getDirectionFired()) {
            //if(!isDead() || !(b2body.getLinearVelocity().x > 1.5f) || !(b2body.getLinearVelocity().y > 3.5f))
            b2body.setLinearVelocity(0f, 0f );
            b2body.applyLinearImpulse(new Vector2(1.0f, 3.5f), b2body.getWorldCenter(), true);
            //System.out.println("BubblePlayer Class HitByEnemy: SmallEnemyDef Facing runningRight");
        }else{
            //if(!isDead()|| !(b2body.getLinearVelocity().x > -1.5f) || !(b2body.getLinearVelocity().y > 3.5f))
            b2body.setLinearVelocity(0f, 0f );
            b2body.applyLinearImpulse(new Vector2(-1.0f, 3.5f), b2body.getWorldCenter(), true);
            //System.out.println("BubblePlayer Class HitByEnemy: SmallEnemyDef Facing runningLeft");
        }
        //setPlayerState(PlayerState.HIT);

        if(!isHit()) {
            setIsHit(true);
//System.out.println("Contact with SmallEnemyDef!! Remember to playerLifte-- is quoted out");
            playerLife--;
        }

        }


    public void HitbyEnemy(boolean enemyRunningDirection){



        // player Current Position ,
        // change animation on Player to blinkAnimation,
        // move the player by pulse 0.2f jump back
        // remove a life from the life bar Hud
        // if last life, invoke die(); - respawn at last save Point!! if player press playButton
        // Direction on the impulse facing !!

        //Vector2 playerPosition = new Vector2(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        enemyFightingHitDirection = enemyRunningDirection;

        if(enemyRunningDirection) {

            //System.out.println("BubblePlayer Class HitByEnemy Main lif: " + mainLife);

            if(mainLife != 1) {
                time_to_defineHit = true;
                /** we use redefineHit() and set impulse there!!!  */
                //System.out.println("time to define hit true");
            }else {
                /** we don't use redefineHit() Vector + (when we only have one life left and lose it) */
                //System.out.println("Player getHit - Outside of defineHit() One life to spend so death it is!!");
                b2body.setLinearDamping(0f);
                b2body.setLinearVelocity(0f, 0f);
                b2body.applyLinearImpulse(new Vector2(2.0f, 1.2f), b2body.getWorldCenter(), true);
                //die();
            }
             //System.out.println("BubblePlayer Class HitByEnemy: SmallEnemyDef Facing runningRight: forceX: " +
            //        b2body.getLinearVelocity().x + " forceY: " + b2body.getLinearVelocity().y);
        }else{

            //System.out.println("BubblePlayer Class HitByEnemy Else Main lif: " + mainLife);

            if(mainLife != 1) {
                time_to_defineHit = true;
                /** we use redefineHit() and set impulse there!!!  */
                //System.out.println("time to define hit true");
            }else {
                /** we don't use redefineHit() when we only have one life left and lose it */
                //System.out.println("Player getHit - Outside of defineHit() Vector - (when we only have one life left and lose it)");
                b2body.setLinearDamping(0f);
                b2body.setLinearVelocity(0f, 0f);
                b2body.applyLinearImpulse(new Vector2(-2.0f, 1.2f), b2body.getWorldCenter(), true);
                //die();
            }
            //System.out.println("BubblePlayer Class HitByEnemy: SmallEnemyDef Facing runningLeft: forceX: " +
            //        b2body.getLinearVelocity().x + " forceY: " + b2body.getLinearVelocity().y);
        }
        //setPlayerState(PlayerState.HIT);

        if(!isHit()) {
            setIsHit(true);
//System.out.println("Contact with SmallEnemyDef!! Remember to playerLifte-- is quoted out");
            playerLife--;

        }

    }

    public void updatePlayerLifeFromSaveOnExit(int l){ this.mainLife = l; }

    public int updatePlayerLifeToHudAndSaveOnExit(){ return mainLife; }

    public void die() {
        if (!isDead()) {
            //System.out.println("playerLife !isDead: " + playerLife);
            if (mainLife == 0 || mainLife < 0 ) {

                setIsDead(true);

                Filter filter = new Filter();
                filter.maskBits = GameUtility.NOTHING_BIT;

                for (Fixture fixture : b2body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
                b2body.setLinearDamping(0f);
                b2body.setLinearVelocity(0f, 0f );
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
//System.out.println("Player is dead!!");

            }else{
                // reset player life
                playerLife = playerHitMaxPerMainLifeLost;
                mainLife--;
                //System.out.println("Main life force is: " + mainLife);
                //System.out.println("playerLife after lost a mainLife: " + playerLife);
                if(mainLife == 0){
                    setIsDead(true);

                    Filter filter = new Filter();
                    filter.maskBits = GameUtility.NOTHING_BIT;

                    for (Fixture fixture : b2body.getFixtureList()) {
                        fixture.setFilterData(filter);
                    }
                    b2body.setLinearDamping(0f);
                    b2body.setLinearVelocity(0f, 0f );
                    b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
                    //System.out.println("Player is dead!!");
                }
            }
        }

    }

    public int getPlayerLife(){return this.mainLife; }


    public Boolean getPlayerFacingDirection(){ return this.runningRight; }
    public void setPlayerFacingDirection(boolean value){this.runningRight = value;}

    /** Controller Joystick */
    public void setPlayerControllDirectionRunningRightTrue(){ this.controllerRunningRight = true; }
    public void setPlayerControllDirectionRunningRightFalse(){ this.controllerRunningRight = false; }

    // for testing SteeringEntityBoss
    public Body getPlayerAsTargetToFollow() {

        return this.b2body;
    }

    //PlayerState - currentState, previousState
    public void setPlayerState(PlayerState state) {

        if(!currentState.equals(state)) {

            this.previousState = currentState;
            this.currentState = state; // new state, but be for we set a new the old must be sett into history
        }
        //this.previousState = currentState;
        //this.currentState = state;
    }
    /** set diffeculti on how many hit player take be for lose main life */
    public void setPlayerHitMaxPerMainLifeLost(int value){
        this.playerHitMaxPerMainLifeLost = value;
    }

    public PlayerState getPlayerState() {

        return this.currentState;
    }
    public PlayerState getprePlayerState() {

        return this.previousState;
    }

    public Vector2 getPlayerPosition(){
        return this.b2body.getPosition();
    }

    public BubblePlayer getPlayerSaveGame(){

        //BubblePlayer saveObject = new BubblePlayer()

        return this.getClass().cast(BubblePlayer.class);
    }

    public float getRealtimePlayerPosX(){
        return   playerRealTimePosX;
    }

    public float getRealTimePlayerPosY(){
        return playerRealTimePosY;
    }

    public int getBallooneBulletGreen(){ return this.ballooneBulletGreen; }
    public void addBallooneBulletGreen(){ ballooneBulletGreen++; }

    public int getBallooneBulletBlack(){ return this.ballooneBulletBlack; }
    public void addBallooneBulletBlack(){ ballooneBulletBlack++; }

    public int getBallooneBulletBlue(){ return this.ballooneBulletBlue; }
    public void addBallooneBulletBlue(){ ballooneBulletBlue++; }

    public int getBallooneBulletRed(){ return this.ballooneBulletRed; }
    public void addBallooneBulletRed(){ ballooneBulletRed++; }

    public boolean getPlayerIsShooting(){
        return isShooting;
    }

    public void setPlayerIsShooting(boolean value){
        this.isShooting = value;
    }


}

