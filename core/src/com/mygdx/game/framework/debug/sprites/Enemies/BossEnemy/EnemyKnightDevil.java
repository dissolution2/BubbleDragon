package com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.managers.GameManagerAITimer;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.powers.BalloneBullet;
import com.mygdx.game.framework.debug.sprites.powers.EnemyKnightRangeAttack;
import com.mygdx.game.framework.debug.sprites.powers.EnemyKnightPowerSword;
import com.mygdx.game.framework.debug.util.GameUtility;

//import com.mygdx.game.framework.ai.steering.SteeringEntityBoss;


public class EnemyKnightDevil extends BossEnemyDef {//SmallEnemyDef {


    public enum EnemyBossState {
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

    public enum EnemyBossActionState {
        NO_ACTION,
        FIGHTING_RANGE_ACTION,
        AI_STEERING_DASH_ACTION,
        AI_STEERING_FOLLOW,
        AI_JUMPING_ACTION,
    }


    private boolean timeTo_DefineEnemyTo_Fight_Close;
    private boolean timeTo_ReDefineEnemy;
    private boolean timeTo_DefineEnemyTo_Fight_Range;

    private boolean timeTo_DefineEnemyTo_Fight_RangeRight;
    private boolean timeTo_DefineEnemyTo_Fight_RangeLeft;

    private boolean timeTo_DefineEnemyTo_Fight_Frenzy;

    private float stateTimer;
    private float gravityTimer;
    private GameManagerAITimer timerAI;
    private GameManagerAITimer rangeAttackAnimationFinished;
    private GameManagerAITimer closeAttackAnimationFinished;
    private GameManagerAITimer runningRightSwitchTimer;
    private float rangeAttackTimer;
    private boolean activeRangeAttack;
    private boolean activeCloseAttack;

    private String dashAssociationNumber;

    private Animation fightAnimationClose, walkAnimation, standingGuardAnimation, getHitAnimation, fightAnimationRange;
    private TextureRegion knightStand, knightFalling;
    private Array<TextureRegion> frames;

    /** Testing Running right/left with atlas right/left*/

    private Array<TextureRegion> framesRunningLeft;
    private Array<TextureRegion> framesRunningRight;

    private Animation fightAnimationClose_Right, walkAnimation_Right, standingGuardAnimation_Right, getHitAnimation_Right, fightAnimationRange_Right;
    private TextureRegion knightStand_Right, knightFalling_Right;

    private Animation fightAnimationClose_Left, walkAnimation_Left, standingGuardAnimation_Left, getHitAnimation_Left, fightAnimationRange_Left;
    private TextureRegion knightStand_Left, knightFalling_Left;

    /** End updateAllSpawnLifeFromEnemy vars */

    private boolean bolCloseAttack;
    private float attackTimer = 0;
    private float testTimer;
    private float hitTimer;
    private boolean fireBoolRangeTrue;

    //fire Balloon updateAllSpawnLifeFromEnemy
    private Array<EnemyKnightRangeAttack> enemyKnightPowerSwordRangeAttacks;
    private Array<BalloneBullet> testBalloneBullet;
    private Array<EnemyKnightPowerSword> enemyKnightPowerSwords;

    private boolean setToDestroy;
    private boolean destroyed;
    private boolean runningRight;
    private boolean enemyIsDead;
    private boolean enemyIsHit;
    private int enemyLife = 20;

    private boolean enemyIsFighting_Close;
    private boolean enemyIsFighting_Range;

    private boolean rangeAttackIsCalledFor;



    private boolean enemyAnimationFightingRangeFinished;
    private boolean boolHaveFiredRange;


    private boolean triggerCloseAttackBol;


    private boolean powerSwordBallContactWithFloor;
    private boolean fireRange;

    private boolean enemySePlayer;
    private float enemySePlayerTimerGoneBy;

    float angle;
    float gravityFallingHard;

    //private SteeringEntityBoss aiEntity;


    private Vector2 collisionFront = new Vector2(), normalFront = new Vector2();
    private float distFront;

    private Vector2 collisionBack = new Vector2(), normalBack = new Vector2();
    private float distBack;

    private Vector2 collisionUp = new Vector2(), normalUp = new Vector2();
    private float distUp;

    private Vector2 collisionDown = new Vector2(), normalDown = new Vector2();
    private float distDown;

    Vector2 enemyLosRayFRONT = new Vector2();
    Vector2 enemyLosRayBACK  = new Vector2();
    Vector2 enemyLosRayUP  = new Vector2();
    Vector2 enemyLosRayDOWN  = new Vector2();


    float rayBeamONEx2 = 0f;
    float rayBeamONEy2 = 0f;

    float rayBeamTWOx2 = 0f;
    float rayBeamTWOy2 = 0f;

    float rayBeamTHREEx2 = 0f;
    float rayBeamTHREEy2 = 0f;

    private boolean losOnPlayerFRONT = false;
    private boolean losOnPlayerBACK = false;
    private boolean losOnPlayerDOWN = false;
    private boolean losOnPlayerUP = false;
    private boolean facingPlayerActive = false;

    float xProductFRONT = 0;
    float yProductFRONT = 0;
    float xProductBodyShiftFRONT = 0;
    float yProductBodyShiftFRONT = 0;

    float xProductBACK = 0;
    float yProductBACK = 0;
    float xProductBodyShiftBACK = 0;
    float yProductBodyShiftBACK = 0;


    float yProductUP = 0;
    float yProductBodyShiftUP = 0;

    float yProductDOWN = 0;
    float yProductBodyShiftDOWN = 0;



    float x2 =0f;
    float y2=0f;

    private int enemyID;

    private EnemyBossState currentEnemyState, prevEnemyState, currentEnemyStateTime, prevEnemyStateTime;
    private EnemyBossActionState currentActionState, prevActionState;

    //TODO not sure about thia !!! ???
    private boolean enemyBossKnight_is_on_Ground;
    private Vector2 enemyMapStartPosition;

    // AI Marker with Contact Listener
    private boolean aiObjectMarker;
    private boolean ai_Move_To_Patroll_Marker_Start = false;
    private boolean ai_Move_To_Patroll_Marker_Left = false;
    private boolean ai_Move_To_Patroll_Marker_Right = false;

    private PolygonShape boxCloseRangeArea, boxEnemyThreathErea;

    private float losTimerLastSeen = 0;

    private boolean jumpIngbool = false;

    private boolean balloonIsShootingFromRight;

    private float fireBallSeedDirection;


    // updateAllSpawnLifeFromEnemy
    Vector2 enemyPositionWalkingLeftRaycasting = new Vector2();
    Vector2 enemyPositionWalkingRightRaycasting = new Vector2();

    private GameManagerAssets gameManagerAssetsInstance;

    public EnemyKnightDevil(PlayScreen screen, float x, float y, int idOfEnemyFromTiledMap, GameManagerAssets instance) {
        super(screen, x, y);

        this.gameManagerAssetsInstance = instance;
        //System.out.println(this.getClass().getSimpleName());

        this.enemyMapStartPosition = new Vector2( x, y);
        this.enemyID = idOfEnemyFromTiledMap;

        frames = new Array<TextureRegion>();
        knightStand = new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365);
        knightFalling = new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365);

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationB"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationC"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationD"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365));

        //Speed on Animation
        fightAnimationClose = new Animation(0.2f, frames); // ToDo : See if we can make a ball and use impulse direction with gravity
        frames.clear();

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationB"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationC"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationD"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365));

        //Speed on Animation
        fightAnimationRange = new Animation(0.2f, frames); // ToDo : See if we can make a ball and use impulse direction with gravity
        frames.clear();

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnightHit.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnight.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/NewKnightHit.atlas").findRegion("knightAnimationA"), 0, 0, 337, 365));

        getHitAnimation = new Animation(0.2f, frames);
        frames.clear();





        //knightStand = new TextureRegion(Utility.getTextureAtlas("spriteAtlas/knight.atlas").findRegion("knightAnimationA"), 196, 167, 176, 133);

        stateTimer = 0; // remove ?
        gravityTimer = 0;
        testTimer = 0; // updateAllSpawnLifeFromEnemy
        hitTimer = 0;
        timerAI = new GameManagerAITimer(); // updateAllSpawnLifeFromEnemy
        rangeAttackAnimationFinished = new GameManagerAITimer(); // Works getFrame animationIsFinishedTimer
        closeAttackAnimationFinished = new GameManagerAITimer(); // not sure we need it / Test!!!
        runningRightSwitchTimer = new GameManagerAITimer(); // we are getting direction animation twitching !!! fix
        activeRangeAttack = false;
        activeCloseAttack = false;

        dashAssociationNumber = "null";
        rangeAttackTimer = 0; // updateAllSpawnLifeFromEnemy

        //aiEntity = new SteeringEntityBoss( this.b2body, false, .5f); //B2dSteeringEntity( enemy.b2body, .5f);
        //aiEntity.setMaxLinearSpeed(10); // 10
        //aiEntity.setMaxLinearAcceleration(60); // 100

        setBounds(getX(), getY(),300f / GameUtility.PPM, 300f / GameUtility.PPM);

        setToDestroy = false;
        destroyed = false;
        setIsDead(false);
        angle = 0;
        fireBoolRangeTrue = false; // updateAllSpawnLifeFromEnemy var
        runningRight = false;
        enemyBossKnight_is_on_Ground = false;

        //enemyKnightBladeBashBulletsRight = new Array<EnemyKnightRangeAttack>();
        //enemyKnightBladeBashBulletsLeft = new Array<EnemyKnightRangeAttack>();
        testBalloneBullet = new Array<BalloneBullet>();
        enemyKnightPowerSwords = new Array<EnemyKnightPowerSword>();
        enemyKnightPowerSwordRangeAttacks = new Array<EnemyKnightRangeAttack>();
        fireBallSeedDirection = 0;

        //this.currentEnemyState = EnemyBossState.SET_INACTIVE_STATE;
        //this.prevEnemyState = EnemyBossState.SET_PREVIOUS_STATE;

        // x2 Is the length of the LOS Higher Value = Longer Range!! y2 Is to keep it at eye level straight ahead
        x2 = 0.94f;
        y2 = -0.2f;

//ToDo : had to change this back someWhat as animation is bound by it - check it out more || player have'nt changed
        // this for actual state of the enemy ???!!!
        this.prevEnemyState = this.currentEnemyState = EnemyBossState.SET_INACTIVE_STATE;

        // this for animation time sequence
        this.prevEnemyStateTime = this.currentEnemyStateTime = EnemyBossState.SET_INACTIVE_STATE;

        this.currentActionState = EnemyBossActionState.NO_ACTION;
    }

    public int getEnemyIDCreatingEnemy() {
        return this.enemyID;
    }


    RayCastCallback rayCallback_Front = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            if( fixture.getBody().getType() != BodyType.StaticBody ) {
                distFront = 1*fraction; // Closes collision to target !!
                collisionFront.set( new Vector2(point));
                EnemyKnightDevil.this.normalFront.set(new Vector2(normal)).add(new Vector2(point));
                //System.out.println("rayCallback_Front");
                if( fixture.getFilterData().categoryBits == 4 ) {

                    losOnPlayerFRONT = true;

                }else {
                    losOnPlayerFRONT = false;
                }
            }
            return -1; //-1; // Continue with the rest of the fixtures
        }
    };

    RayCastCallback rayCallback_Back = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            if( fixture.getBody().getType() != BodyType.StaticBody ) {
                distBack = 1*fraction; // Closes collision to target !!
                collisionBack.set( new Vector2(point));
                EnemyKnightDevil.this.normalBack.set(new Vector2(normal)).add(new Vector2(point));

                //System.out.println("rayCallback_Back");
                if( fixture.getFilterData().categoryBits == 4 ) {

                    losOnPlayerBACK = true;

                }else {
                    losOnPlayerBACK = false;
                }
            }
            return -1; //-1; // Continue with the rest of the fixtures
        }
    };


    RayCastCallback rayCallbackDown = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            if( fixture.getBody().getType() != BodyType.StaticBody ) {
                distDown = 1*fraction; // Closes collision to target !!
                collisionDown.set(new Vector2(point));
                EnemyKnightDevil.this.normalDown.set(new Vector2(normal)).add(new Vector2(point));

                if( fixture.getFilterData().categoryBits == 4 ) {

                    losOnPlayerDOWN = true;

                }//else {
                 //   losOnPlayerDOWN = false;
                //}
            }
            return -1; //-1; // Continue with the rest of the fixtures
        }
    };


    RayCastCallback rayCallbackUp = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            if( fixture.getBody().getType() != BodyType.StaticBody ) {
                distUp = 1*fraction; // Closes collision to target !!
                collisionUp.set(new Vector2(point));
                EnemyKnightDevil.this.normalUp.set(new Vector2(normal)).add(new Vector2(point));

                if( fixture.getFilterData().categoryBits == 4 ) {

                    losOnPlayerUP = true;
                    //System.out.println("rayCallbackUp true");

                }//else {
                 //   losOnPlayerUP = false;
                    //System.out.println("rayCallbackUp false");
                //}
            }
            return 1; //-1; // Continue with the rest of the fixtures
        }
    };


    public void setFacingPlayerActive(boolean value){this.facingPlayerActive = value; }
    public boolean getFacingPlayerActive(){ return this.facingPlayerActive; }

    @Override
    public void update(float dt) {
        if(gameManagerAssetsInstance.getGameState() != GameManagerAssets.GameState.GAME_PAUSED ) {
//System.out.println("State of Knight : " + getEnemyState() +  " Pre state : " + getpreEnemyState() );

            if (enemySePlayer) {
                enemySePlayerTimerGoneBy += dt;
            }
            stateTimer += dt;
            gravityTimer += dt;


            if (!runningRight) {
                //enemy.b2body.getPosition().x - 2.85f,enemy.b2body.getPosition().y - 0.35f
                //FRONT
                xProductFRONT =  b2body.getPosition().x - 2.85f;
                yProductFRONT =  b2body.getPosition().y - 0.35f;

                xProductBodyShiftFRONT = -0.25f;
                yProductBodyShiftFRONT = -0.35f;

                //BACK
                xProductBACK = b2body.getPosition().x + 2.85f;
                yProductBACK = b2body.getPosition().y - 0.35f;

                xProductBodyShiftBACK = +0.25f;
                yProductBodyShiftBACK = -0.35f;


                yProductUP = b2body.getPosition().y + 1.5f;
                yProductBodyShiftUP = + 1.5f;

                yProductDOWN = b2body.getPosition().y - 1.5f;
                yProductBodyShiftDOWN = - 1.5f;


            } else {
                //enemy.b2body.getPosition().x + 2.85f,enemy.b2body.getPosition().y - 0.35f
                //FRONT
                xProductFRONT = b2body.getPosition().x + 2.85f;
                yProductFRONT = b2body.getPosition().y - 0.35f;

                xProductBodyShiftFRONT = +0.25f;
                yProductBodyShiftFRONT = -0.35f;

                //BACK
                xProductBACK =  b2body.getPosition().x - 2.85f;
                yProductBACK =  b2body.getPosition().y - 0.35f;

                xProductBodyShiftBACK = -0.25f;
                yProductBodyShiftBACK = -0.35f;

                yProductUP = b2body.getPosition().y + 1.5f;
                yProductBodyShiftUP = + 1.5f;

                yProductDOWN = b2body.getPosition().y - 1.5f;
                yProductBodyShiftDOWN = - 1.5f;
            }

            //enemyLosRayFRONT.set(xProductFRONT, yProductFRONT);
            //world.rayCast(rayCallback_Front,
            //        new Vector2( xProductBodyShiftFRONT + this.b2body.getPosition().x, yProductBodyShiftFRONT + this.b2body.getPosition().y)
            //        , enemyLosRayFRONT);

            //enemyLosRayBACK.set(xProductBACK, yProductBACK);
            //world.rayCast(rayCallback_Back,
            //        new Vector2( xProductBodyShiftBACK + this.b2body.getPosition().x, yProductBodyShiftBACK + this.b2body.getPosition().y)
            //        , enemyLosRayBACK);

            //enemyLosRayDOWN.set(this.b2body.getPosition().x, yProductDOWN );
            //world.rayCast(rayCallbackDown,
            //        new Vector2( this.b2body.getPosition().x, this.b2body.getPosition().y), enemyLosRayDOWN );


            //enemyLosRayUP.set(this.b2body.getPosition().x, yProductUP);
            //world.rayCast(rayCallbackUp,
            //        new Vector2( this.b2body.getPosition().x, this.b2body.getPosition().y), enemyLosRayUP );

/*

            if( !(b2body.getLinearVelocity().y != 0) ) {

                System.out.println("raycast is On");

                if (losOnPlayerFRONT) {
                    losOnPlayerBACK = false;
                    losOnPlayerFRONT = false;
                    //System.out.println("losOnPlayerFRONT true");
                }

                if (losOnPlayerBACK) {
                    losOnPlayerFRONT = false;
                    losOnPlayerBACK = false;
                    //System.out.println("losOnPlayerBACK true");


                    if (getIsRunningRight()) {
                        setIsRunningRight(false);
                    } else {
                        setIsRunningRight(true);
                    }

                }
            }else {
                System.out.println("raycast is Off");
            }
*/

            if (timeTo_DefineEnemyTo_Fight_Close) {
                defineEnemyFightingClose();
            }

            if (timeTo_DefineEnemyTo_Fight_Range) {
                defineEnemyFightingRange();
                //System.out.println("timeTo_DefineEnemyTo_Fight_Range " + timeTo_DefineEnemyTo_Fight_Range);
            }


            if (timeTo_ReDefineEnemy) {
                reDefineEnemy();
            }

            /**  getEnemyKnightOnGround  true on collision with floor */
            if (!getEnemyKnightOnGround() && getEnemyState().equals(EnemyBossState.FALLING)) {

                gravityFallingHard += (gravityTimer + 10f) * 0.02f; //(stateTimer + 10f) * 0.02f;
                b2body.setGravityScale(gravityFallingHard);
//System.out.println("EnemyKnighDevil Class Falling gravityScale: " + this.b2body.getGravityScale());
            } else {
                b2body.setGravityScale(1.0f); //1.0f);
                gravityFallingHard = 0;
//System.out.println("EnemyKnighDevil Class OnTheGround gravityScale: " + this.b2body.getGravityScale() );
            }

            if(getEnemyKnightOnGround()){
               // b2body.setLinearVelocity(velocityMovment);
            }


// we use this !! have to make it work with GameManagerAI ???!!!!
            // set from GameManagerAI | By true or direct !!





            if (enemyLife == 0 || enemyLife < 0) {
                die();
                //System.out.println("SmallEnemyDef should have died!!");
            }

            if (isHit()) {
                hitTimer += dt;
                //System.out.println("EnemyKnight class Update isHit StatTimer: " + stateTimer );
                //b2body.setGravityScale(4.0f);

                if (hitTimer > 0.4f) {
                    //b2body.applyLinearImpulse(new Vector2(2.0f, 1.2f), b2body.getWorldCenter(), true);
                    setIsHit(false); // stop Animation Hit!!!
                    hitTimer = 0;
                    //b2body.setGravityScale(1.0f);
                    //b2body.setLinearVelocity(0f,0f);
                }


            }

            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt)); //stateTimer));

            //enemyKnightPowerSwords - defineEnemyFightingClose
            for (EnemyKnightPowerSword ball : enemyKnightPowerSwords) {
                ball.update(dt);

                if (ball.getHitwithBullet()) {
                    ball.setToDestroy();
                }
                if (ball.isDestroyed()) {
                    enemyKnightPowerSwords.removeValue(ball, true);
                }
            }
/*
            if(powerSwordBallContactWithFloor){
                //fireRangeAttack();
                powerSwordBallContactWithFloor = false;
                //setRangeAttackActiveBool(false);
            }
*/
            //enemyKnightBladeBashBulletsLeft - defineEnemyFightingRange

            //System.out.println("EnemyKnightDevil class - ListPowerSword Size " + enemyKnightPowerSwordRangeAttacks.size);

            for (int i = 0; i < enemyKnightPowerSwordRangeAttacks.size; i++) {
                ((EnemyKnightRangeAttack) enemyKnightPowerSwordRangeAttacks.get(i)).update(dt);

                if( ((EnemyKnightRangeAttack) enemyKnightPowerSwordRangeAttacks.get(i)).getHitwithBullet() ){

                    ((EnemyKnightRangeAttack) enemyKnightPowerSwordRangeAttacks.get(i)).setToDestroy();
                    //System.out.println("Ball is destroyed!!");
                }

                if( ((EnemyKnightRangeAttack) enemyKnightPowerSwordRangeAttacks.get(i)).isDestroyed()) {
                    enemyKnightPowerSwordRangeAttacks.removeValue( (EnemyKnightRangeAttack) enemyKnightPowerSwordRangeAttacks.get(i), true);
                }

            }

          /*
            for (int i = 0; i < testBalloneBullet.size; i++) {
                ((BalloneBullet) testBalloneBullet.get(i)).update(dt);

                if( ((BalloneBullet) testBalloneBullet.get(i)).getHitwithBullet() ){

                    ((BalloneBullet) testBalloneBullet.get(i)).setToDestroy();
                }

                if( ((BalloneBullet) testBalloneBullet.get(i)).isDestroyed()) {
                    testBalloneBullet.removeValue( (BalloneBullet) testBalloneBullet.get(i), true);
                }

            }
            */
            for(BalloneBullet ball : testBalloneBullet) {
                ball.update(dt);

                if(ball.getHitwithBullet()){
                    ball.setToDestroy();
                }

                if(ball.isDestroyed())
                    testBalloneBullet.removeValue(ball, true);
            }

            /*
            for(EnemyKnightRangeAttack ball : enemyKnightPowerSwordRangeAttacks) {
                ball.update(dt);

                if(ball.getHitwithBullet()){
                    ball.setToDestroy();
                }

                if(ball.isDestroyed())
                    enemyKnightPowerSwordRangeAttacks.removeValue(ball, true);
            }
            */


            //for(FireBall ball : enemyFireBall) {
            //    ball.update(dt);

                //if(ball.getHitwithBullet()){
                //    ball.setToDestroy();
                //}

                //if(ball.isDestroyed())
                //   enemyKnightBladeBashBulletsLeft.removeValue(ball, true);
            //}


        }
    }


    public void setStartTimerRangeAttack(float time){ this.rangeAttackTimer = time; }
    public void setTimerRangeAttackReset(){ this.rangeAttackTimer = 0f; }
    public float getTimeRangeAttack(){ return this.rangeAttackTimer; }




    // not done!!!
    protected void defineEnemyFightingRange() {
        //System.out.println("EnemyKnight Class Define: define fighting is used!!");

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(45 / 2 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_BIT;
        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);// shape); // this !!??

        FixtureDef fix_BodyTorsoMidt = new FixtureDef();
        CircleShape shape_BodyTorsoMidt = new CircleShape();
        shape_BodyTorsoMidt.setRadius(45 / 2 / GameUtility.PPM);
        shape_BodyTorsoMidt.setPosition( new Vector2(-1 / GameUtility.PPM, -15 / GameUtility.PPM));

        fix_BodyTorsoMidt.filter.categoryBits = GameUtility.ENEMY_BIT;
        fix_BodyTorsoMidt.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fix_BodyTorsoMidt.shape = shape_BodyTorsoMidt;
        fix_BodyTorsoMidt.isSensor = true;
        b2body.createFixture(fix_BodyTorsoMidt).setUserData(this);

        //if(runningRight)
            //fireRangeAttack();
        //else
            //fireRangeAttack();


        /** Bottom Sensor for jumpoing on Player */
        FixtureDef fdef_Bottom_Sensor = new FixtureDef();
        CircleShape shapeBottom = new CircleShape();
        shapeBottom.setRadius(30 / 2 / GameUtility.PPM);
        shapeBottom.setPosition(new Vector2(-1 / GameUtility.PPM, -30 / GameUtility.PPM));

        fdef_Bottom_Sensor.filter.categoryBits = GameUtility.ENEMY_BOTTOM_BIT;
        fdef_Bottom_Sensor.filter.maskBits = GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef_Bottom_Sensor.shape = shapeBottom;
        fdef_Bottom_Sensor.isSensor = true;
        b2body.createFixture(fdef_Bottom_Sensor).setUserData(this);

        //----------- Legg left Sensor
        FixtureDef fdefLeggSensor = new FixtureDef();
        CircleShape shapeLeggs = new CircleShape();
        shapeLeggs.setRadius(25 / 2 / GameUtility.PPM);
        shapeLeggs.setPosition(new Vector2(-25 / GameUtility.PPM, -35 / GameUtility.PPM));

        fdefLeggSensor.filter.categoryBits = GameUtility.ENEMY_LEGS_BIT;
        fdefLeggSensor.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdefLeggSensor.shape = shapeLeggs;
        //fdefLeggSensor.isSensor = true;
        b2body.createFixture(fdefLeggSensor).setUserData(this);
        //-----------
        //----------- Legg Right Sensor

        FixtureDef fdef_LeggSensor_Right = new FixtureDef();
        CircleShape shapeRightLeg = new CircleShape();
        shapeRightLeg.setRadius(25 / 2 / GameUtility.PPM);
        shapeRightLeg.setPosition(new Vector2(25 / GameUtility.PPM, -35 / GameUtility.PPM));

        fdef_LeggSensor_Right.filter.categoryBits = GameUtility.ENEMY_BIT;
        fdefLeggSensor.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef_LeggSensor_Right.shape = shapeRightLeg;
        //fdef_LeggSensor_Right.isSensor = true;
        b2body.createFixture(fdef_LeggSensor_Right).setUserData(this);
        //-----------

        // Box for Zone where SmallEnemyDef will notice you !!
        FixtureDef fdefBoxEnemyThreathErea = new FixtureDef();
        //PolygonShape boxEnemyThreathErea = new PolygonShape();
        boxEnemyThreathErea = new PolygonShape();

        fdefBoxEnemyThreathErea.shape = boxEnemyThreathErea;
        boxEnemyThreathErea.setAsBox(b2body.getLocalCenter().x + 280 / GameUtility.PPM, b2body.getLocalCenter().y + 100 / GameUtility.PPM);
        fdefBoxEnemyThreathErea.filter.categoryBits = GameUtility.ENEMY_RANGE_ATTACK_BIT;
        fdefBoxEnemyThreathErea.filter.maskBits = GameUtility.PLAYER_BIT;
        fdefBoxEnemyThreathErea.isSensor = true;
        b2body.createFixture(fdefBoxEnemyThreathErea).setUserData(this);

        // Box for Zone where SmallEnemyDef will notice you !! updateAllSpawnLifeFromEnemy Change to Circle

        FixtureDef fdefBoxAttackCloseRangeArea = new FixtureDef();
        //PolygonShape boxCloseRangeArea = new PolygonShape();
        boxCloseRangeArea = new PolygonShape();
        fdefBoxAttackCloseRangeArea.shape = boxCloseRangeArea;
        //boxCloseRangeArea.setAsBox(b2body.getLocalCenter().x + 80 / GameUtility.PPM, b2body.getLocalCenter().y + 80 / GameUtility.PPM);
        boxCloseRangeArea.setAsBox( b2body.getLocalCenter().x + 80 / GameUtility.PPM, b2body.getLocalCenter().y + 60 / GameUtility.PPM);

        fdefBoxAttackCloseRangeArea.filter.categoryBits = GameUtility.ENEMY_CLOSE_ATTACK_BIT;
        fdefBoxAttackCloseRangeArea.filter.maskBits = GameUtility.PLAYER_BIT;

        //fdefBoxAttackCloseRangeArea.shape = boxCloseRangeArea;
        fdefBoxAttackCloseRangeArea.isSensor = true;
        b2body.createFixture(fdefBoxAttackCloseRangeArea).setUserData(this);


        timeTo_DefineEnemyTo_Fight_Range = false;

    }


    protected void defineEnemyFightingClose() {
        //System.out.println("EnemyKnight Class Define: define fighting Close is used!!");

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(45 / 2 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_BIT;
        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);// shape); // this !!??

        FixtureDef fix_BodyTorsoMidt = new FixtureDef();
        CircleShape shape_BodyTorsoMidt = new CircleShape();
        shape_BodyTorsoMidt.setRadius(45 / 2 / GameUtility.PPM);
        shape_BodyTorsoMidt.setPosition( new Vector2(-1 / GameUtility.PPM, -15 / GameUtility.PPM));

        fix_BodyTorsoMidt.filter.categoryBits = GameUtility.ENEMY_BIT;
        fix_BodyTorsoMidt.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fix_BodyTorsoMidt.shape = shape_BodyTorsoMidt;
        fix_BodyTorsoMidt.isSensor = true;
        b2body.createFixture(fix_BodyTorsoMidt).setUserData(this);

        if(runningRight) {

               powerSwordClose();

        }else{

              powerSwordClose();


        }

        /** Bottom Sensor for jumpoing on Player */
        FixtureDef fdef_Bottom_Sensor = new FixtureDef();
        CircleShape shapeBottom = new CircleShape();
        shapeBottom.setRadius(30 / 2 / GameUtility.PPM);
        shapeBottom.setPosition(new Vector2(-1 / GameUtility.PPM, -30 / GameUtility.PPM));

        fdef_Bottom_Sensor.filter.categoryBits = GameUtility.ENEMY_BOTTOM_BIT;
        fdef_Bottom_Sensor.filter.maskBits = GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef_Bottom_Sensor.shape = shapeBottom;
        fdef_Bottom_Sensor.isSensor = true;
        b2body.createFixture(fdef_Bottom_Sensor).setUserData(this);

        //----------- Legg left Sensor
        FixtureDef fdefLeggSensor = new FixtureDef();
        CircleShape shapeLeggs = new CircleShape();
        shapeLeggs.setRadius(25 / 2 / GameUtility.PPM);
        shapeLeggs.setPosition(new Vector2(-25 / GameUtility.PPM, -35 / GameUtility.PPM));

        fdefLeggSensor.filter.categoryBits = GameUtility.ENEMY_LEGS_BIT;
        fdefLeggSensor.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdefLeggSensor.shape = shapeLeggs;
        //fdefLeggSensor.isSensor = true;
        b2body.createFixture(fdefLeggSensor).setUserData(this);
        //-----------
        //----------- Legg Right Sensor

        FixtureDef fdef_LeggSensor_Right = new FixtureDef();
        CircleShape shapeRightLeg = new CircleShape();
        shapeRightLeg.setRadius(25 / 2 / GameUtility.PPM);
        shapeRightLeg.setPosition(new Vector2(25 / GameUtility.PPM, -35 / GameUtility.PPM));

        fdef_LeggSensor_Right.filter.categoryBits = GameUtility.ENEMY_BIT;
        fdefLeggSensor.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef_LeggSensor_Right.shape = shapeRightLeg;
        //fdef_LeggSensor_Right.isSensor = true;
        b2body.createFixture(fdef_LeggSensor_Right).setUserData(this);
        //-----------

        // Box for Zone where SmallEnemyDef will notice you !!
        FixtureDef fdefBoxEnemyThreathErea = new FixtureDef();
        //PolygonShape boxEnemyThreathErea = new PolygonShape();
        boxEnemyThreathErea = new PolygonShape();

        fdefBoxEnemyThreathErea.shape = boxEnemyThreathErea;
        boxEnemyThreathErea.setAsBox(b2body.getLocalCenter().x + 280 / GameUtility.PPM, b2body.getLocalCenter().y + 100 / GameUtility.PPM);
        fdefBoxEnemyThreathErea.filter.categoryBits = GameUtility.ENEMY_RANGE_ATTACK_BIT;
        fdefBoxEnemyThreathErea.filter.maskBits = GameUtility.PLAYER_BIT;
        fdefBoxEnemyThreathErea.isSensor = true;
        b2body.createFixture(fdefBoxEnemyThreathErea).setUserData(this);

        // Box for Zone where SmallEnemyDef will notice you !! updateAllSpawnLifeFromEnemy Change to Circle

        FixtureDef fdefBoxAttackCloseRangeArea = new FixtureDef();
        //PolygonShape boxCloseRangeArea = new PolygonShape();
        boxCloseRangeArea = new PolygonShape();
        fdefBoxAttackCloseRangeArea.shape = boxCloseRangeArea;
        //boxCloseRangeArea.setAsBox(b2body.getLocalCenter().x + 80 / GameUtility.PPM, b2body.getLocalCenter().y + 80 / GameUtility.PPM);
        boxCloseRangeArea.setAsBox( b2body.getLocalCenter().x + 80 / GameUtility.PPM, b2body.getLocalCenter().y + 60 / GameUtility.PPM);

        fdefBoxAttackCloseRangeArea.filter.categoryBits = GameUtility.ENEMY_CLOSE_ATTACK_BIT;
        fdefBoxAttackCloseRangeArea.filter.maskBits = GameUtility.PLAYER_BIT;

        //fdefBoxAttackCloseRangeArea.shape = boxCloseRangeArea;
        fdefBoxAttackCloseRangeArea.isSensor = true;
        b2body.createFixture(fdefBoxAttackCloseRangeArea).setUserData(this);

        timeTo_DefineEnemyTo_Fight_Close = false;
        //b2body.setGravityScale(5.0f); // think this is good to have !!!

    }

    protected void reDefineEnemy() {
        //System.out.println("EnemyKnight Class reDefine: defining back to  guarding is used!!");

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);


        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(45 / 2 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_BIT;
        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this); // this !!??

        FixtureDef fix_BodyTorsoMidt = new FixtureDef();
        CircleShape shape_BodyTorsoMidt = new CircleShape();
        shape_BodyTorsoMidt.setRadius(45 / 2 / GameUtility.PPM);
        shape_BodyTorsoMidt.setPosition( new Vector2(-1 / GameUtility.PPM, -15 / GameUtility.PPM));

        fix_BodyTorsoMidt.filter.categoryBits = GameUtility.ENEMY_BIT;
        fix_BodyTorsoMidt.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fix_BodyTorsoMidt.shape = shape_BodyTorsoMidt;
        fix_BodyTorsoMidt.isSensor = true;
        b2body.createFixture(fix_BodyTorsoMidt).setUserData(this);

        /** Bottom Sensor for jumpoing on Player */
        FixtureDef fdef_Bottom_Sensor = new FixtureDef();
        CircleShape shapeBottom = new CircleShape();
        shapeBottom.setRadius(30 / 2 / GameUtility.PPM);
        shapeBottom.setPosition(new Vector2(-1 / GameUtility.PPM, -30 / GameUtility.PPM));

        fdef_Bottom_Sensor.filter.categoryBits = GameUtility.ENEMY_BOTTOM_BIT;
        fdef_Bottom_Sensor.filter.maskBits = GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef_Bottom_Sensor.shape = shapeBottom;
        fdef_Bottom_Sensor.isSensor = true;
        b2body.createFixture(fdef_Bottom_Sensor).setUserData(this);

        //----------- Legg left Sensor
        FixtureDef fdefLeggSensor = new FixtureDef();
        CircleShape shapeLeggs = new CircleShape();
        shapeLeggs.setRadius(25 / 2 / GameUtility.PPM);
        shapeLeggs.setPosition(new Vector2(-25 / GameUtility.PPM, -35 / GameUtility.PPM));

        fdefLeggSensor.filter.categoryBits = GameUtility.ENEMY_LEGS_BIT;
        fdefLeggSensor.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdefLeggSensor.shape = shapeLeggs;
        //fdefLeggSensor.isSensor = true;
        b2body.createFixture(fdefLeggSensor).setUserData(this);
//-----------
//----------- Legg Right Sensor
        FixtureDef fdef_LeggSensor_Right = new FixtureDef();
        CircleShape shapeRightLeg = new CircleShape();
        shapeRightLeg.setRadius(25 / 2 / GameUtility.PPM);
        shapeRightLeg.setPosition(new Vector2(25 / GameUtility.PPM, -35 / GameUtility.PPM));

        fdef_LeggSensor_Right.filter.categoryBits = GameUtility.ENEMY_BIT;
        fdefLeggSensor.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef_LeggSensor_Right.shape = shapeRightLeg;
        //fdef_LeggSensor_Right.isSensor = true;
        b2body.createFixture(fdef_LeggSensor_Right).setUserData(this);

//-----------

        // Box for Zone where SmallEnemyDef will notice you !!
        FixtureDef fdefBoxEnemyThreathErea = new FixtureDef();
        //PolygonShape boxEnemyThreathErea = new PolygonShape();
        boxEnemyThreathErea = new PolygonShape();

        fdefBoxEnemyThreathErea.shape = boxEnemyThreathErea;
        boxEnemyThreathErea.setAsBox(b2body.getLocalCenter().x + 280 / GameUtility.PPM, b2body.getLocalCenter().y + 100 / GameUtility.PPM);
        fdefBoxEnemyThreathErea.filter.categoryBits = GameUtility.ENEMY_RANGE_ATTACK_BIT;
        fdefBoxEnemyThreathErea.filter.maskBits = GameUtility.PLAYER_BIT;
        fdefBoxEnemyThreathErea.isSensor = true;
        b2body.createFixture(fdefBoxEnemyThreathErea).setUserData(this);

        // Box for Zone where SmallEnemyDef will notice you !! updateAllSpawnLifeFromEnemy Change to Circle

        FixtureDef fdefBoxAttackCloseRangeArea = new FixtureDef();
        //PolygonShape boxCloseRangeArea = new PolygonShape();
        boxCloseRangeArea = new PolygonShape();
        fdefBoxAttackCloseRangeArea.shape = boxCloseRangeArea;
        //boxCloseRangeArea.setAsBox(b2body.getLocalCenter().x + 80 / GameUtility.PPM, b2body.getLocalCenter().y + 80 / GameUtility.PPM);
        boxCloseRangeArea.setAsBox( b2body.getLocalCenter().x + 80 / GameUtility.PPM, b2body.getLocalCenter().y + 60 / GameUtility.PPM);

        fdefBoxAttackCloseRangeArea.filter.categoryBits = GameUtility.ENEMY_CLOSE_ATTACK_BIT;
        fdefBoxAttackCloseRangeArea.filter.maskBits = GameUtility.PLAYER_BIT;

        //fdefBoxAttackCloseRangeArea.shape = boxCloseRangeArea;
        fdefBoxAttackCloseRangeArea.isSensor = true;
        b2body.createFixture(fdefBoxAttackCloseRangeArea).setUserData(this);

        timeTo_ReDefineEnemy = false;
    }

    @Override
    protected void defineEnemyBoss() {
        //System.out.println("EnemyKnight Class Define: defineEnemy is used!!");

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() , getY() );
        bdef.type = BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(45 / 2 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_BIT;

        /** testing !!!! with walls & ground !!!! */
        fdef.filter.groupIndex = GameUtility.ENEMY_BIT;


        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this); // this !!??

        FixtureDef fix_BodyTorsoMidt = new FixtureDef();
        CircleShape shape_BodyTorsoMidt = new CircleShape();
        shape_BodyTorsoMidt.setRadius(45 / 2 / GameUtility.PPM);
        shape_BodyTorsoMidt.setPosition( new Vector2(-1 / GameUtility.PPM, -15 / GameUtility.PPM));

        fix_BodyTorsoMidt.filter.categoryBits = GameUtility.ENEMY_BIT;

        /** testing !!!! with walls & ground !!!! */
        fix_BodyTorsoMidt.filter.groupIndex = GameUtility.ENEMY_BIT;

        fix_BodyTorsoMidt.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fix_BodyTorsoMidt.shape = shape_BodyTorsoMidt;
        fix_BodyTorsoMidt.isSensor = true;
        b2body.createFixture(fix_BodyTorsoMidt).setUserData(this);



        FixtureDef fdef_Bottom_Sensor = new FixtureDef();
        CircleShape shapeBottom = new CircleShape();
        shapeBottom.setRadius(30 / 2 / GameUtility.PPM);
        shapeBottom.setPosition(new Vector2(-1 / GameUtility.PPM, -30 / GameUtility.PPM));

        fdef_Bottom_Sensor.filter.categoryBits = GameUtility.ENEMY_BOTTOM_BIT;

        /** testing !!!! with walls & ground !!!! */
        fdef_Bottom_Sensor.filter.groupIndex = GameUtility.ENEMY_BIT;

        fdef_Bottom_Sensor.filter.maskBits = GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef_Bottom_Sensor.shape = shapeBottom;
        fdef_Bottom_Sensor.isSensor = true;
        b2body.createFixture(fdef_Bottom_Sensor).setUserData(this);


        //----------- Legg Right Sensor
        FixtureDef fdef_LeggSensor_Right = new FixtureDef();
        CircleShape shapeRightLeg = new CircleShape();
        shapeRightLeg.setRadius(25 / 2 / GameUtility.PPM);
        shapeRightLeg.setPosition(new Vector2(25 / GameUtility.PPM, -35 / GameUtility.PPM));

        fdef_LeggSensor_Right.filter.categoryBits = GameUtility.ENEMY_BIT;

        /** testing !!!! with walls & ground !!!! */
        fdef_LeggSensor_Right.filter.groupIndex = GameUtility.ENEMY_BIT;

        fdef_LeggSensor_Right.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef_LeggSensor_Right.shape = shapeRightLeg;
        b2body.createFixture(fdef_LeggSensor_Right).setUserData(this);
        //-----------


        /** Legg left Sensor for reg SmallEnemyDef Contact Floor (Jumping) */
        //----------- Legg left Sensor
        FixtureDef fdefLeggSensor = new FixtureDef();
        CircleShape shapeLeggs = new CircleShape();
        shapeLeggs.setRadius(25 / 2 / GameUtility.PPM);
        shapeLeggs.setPosition(new Vector2(-25 / GameUtility.PPM, -35 / GameUtility.PPM));
//ToDo :: remove this we will use groupIndex
        fdefLeggSensor.filter.categoryBits = GameUtility.ENEMY_LEGS_BIT;

        /** testing !!!! with walls & ground !!!! */
        fdefLeggSensor.filter.groupIndex = GameUtility.ENEMY_BIT;


        fdefLeggSensor.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.GAME_OBSTACLE_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdefLeggSensor.shape = shapeLeggs;
        b2body.createFixture(fdefLeggSensor).setUserData(this);
        //-----------





        // Box for Zone where SmallEnemyDef will notice you !!
        FixtureDef fdefBoxEnemyThreathErea = new FixtureDef();
        boxEnemyThreathErea = new PolygonShape();

        fdefBoxEnemyThreathErea.shape = boxEnemyThreathErea;
        boxEnemyThreathErea.setAsBox(b2body.getLocalCenter().x + 280 / GameUtility.PPM, b2body.getLocalCenter().y + 100 / GameUtility.PPM);

        fdefBoxEnemyThreathErea.filter.categoryBits = GameUtility.ENEMY_RANGE_ATTACK_BIT;
        fdefBoxEnemyThreathErea.filter.maskBits = GameUtility.PLAYER_BIT;

        fdefBoxEnemyThreathErea.isSensor = true;
        b2body.createFixture(fdefBoxEnemyThreathErea).setUserData(this);

        // Box for Zone where SmallEnemyDef will notice you !! updateAllSpawnLifeFromEnemy Change to Circle
        FixtureDef fdefBoxAttackCloseRangeArea = new FixtureDef();
        boxCloseRangeArea = new PolygonShape();

        fdefBoxAttackCloseRangeArea.shape = boxCloseRangeArea;
        boxCloseRangeArea.setAsBox( b2body.getLocalCenter().x + 80 / GameUtility.PPM, b2body.getLocalCenter().y + 60 / GameUtility.PPM);

        fdefBoxAttackCloseRangeArea.filter.categoryBits = GameUtility.ENEMY_CLOSE_ATTACK_BIT;
        fdefBoxAttackCloseRangeArea.filter.maskBits = GameUtility.PLAYER_BIT;

        fdefBoxAttackCloseRangeArea.isSensor = true;
        b2body.createFixture(fdefBoxAttackCloseRangeArea).setUserData(this);


/*
        old data to hold on to

        EdgeShape lowerHead = new EdgeShape();
        lowerHead.set(new Vector2(-2 / 8f , -42 / GameUtility.PPM), new Vector2( 2 / 8f, -42 / GameUtility.PPM));
        fdef.filter.categoryBits = GameUtility.ENEMY_BOTTOM_BIT;
        fdef.shape = lowerHead;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);



        //FixtureDef fdefBoxAttackCloseRangeArea = new FixtureDef();
        CircleShape shapeCloseRangeArea = new CircleShape();
        shapeCloseRangeArea.setRadius(150 / 2 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_CLOSE_RANGE_ATTACK_BIT;
        fdef.filter.maskBits = GameUtility.PLAYER_BIT;

        fdef.shape = shapeCloseRangeArea;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
*/
// two ways to do it... don't know which one is best!!!
/*
        // Box for Zone where SmallEnemyDef will notice you !!
        FixtureDef fdefBoxAttackCloseRangeArea = new FixtureDef();
        fdefBoxAttackCloseRangeArea.shape = new PolygonShape();
        ((PolygonShape)fdefBoxAttackCloseRangeArea.shape).setAsBox(b2body.getLocalCenter().x + 80 / Utility.PPM, b2body.getLocalCenter().y + 80 / Utility.PPM);
        fdefBoxAttackCloseRangeArea.isSensor = true;
        b2body.createFixture(fdefBoxAttackCloseRangeArea).setUserData(this);

*/




    }

    public void fireRangeAttack(){

        //System.out.println("Fire!! enemyKnightPowerSwordRangeAttacks Size " +  enemyKnightPowerSwordRangeAttacks.size );



        if(!(enemyKnightPowerSwordRangeAttacks.size > 0) ){ //&& enemyAnimationFightingRangeFinished && !boolHaveFiredRange) {

            //setRangeAttackActiveBool(false);
            enemyKnightPowerSwordRangeAttacks.add(new EnemyKnightRangeAttack(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false, this.b2body.getPosition().x));
            //boolHaveFiredRange = true;
            //System.out.println("Fire!!");
            //enemyAnimationFightingRangeFinished = false;
            }

            //enemyKnightPowerSwordRangeAttacks.add(new EnemyKnightRangeAttack(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false, this.b2body.getPosition().x));
            //enemyKnightPowerSwordRangeAttacks.get(0).isDestroyed()
        //}
    }

    public void powerSwordClose(){
        //ToDo : NB NB
        // this works on destroying body!! not the other one!!!
        if(!(enemyKnightPowerSwords.size > 0)) {
            enemyKnightPowerSwords.add(new EnemyKnightPowerSword(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false, this.b2body.getPosition().y));
        }
        //if(enemyKnightPowerSwords.size > 0){
        //    enemyKnightPowerSwords.setSize(0);
        //}
        //enemyKnightPowerSwords.insert(0, new EnemyKnightPowerSword(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false, this.b2body.getPosition().y));
        //System.out.println("enemyKnightPowerSwords.size: " + enemyKnightPowerSwords.size);
    }

    public void enemyFallsOrJumpsOnPlayer(){
       System.out.println("enemyFallsOrJumpsOnPlayer");
       setJumpIngbool();
       enemyShortJumpTest(runningRight);
    }

    public void setJumpIngbool() { this.jumpIngbool = true; }

    public void setJumpLongBool() { System.out.println("EnemyKnight Long Jump");}
    public void setJumpShortBool() {System.out.println("EnemyKnight Short Jump");}

    public void enemyLongJumpTest(boolean directionTojump){

        if(directionTojump){
            if(enemyLife != 0) {
                //System.out.println("Jumping Gravity is then: " + b2body.getGravityScale());
                setEnemyState(EnemyBossState.JUMPING);
                enemyBossKnight_is_on_Ground = false;
                this.b2body.applyLinearImpulse(new Vector2(2.5f, 5.0f), this.b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f);
                //enemyLeftGround();
                jumpIngbool = false;
            }
        }else {
            if(enemyLife != 0) {
                //System.out.println("Jumping Gravity is then: " + b2body.getGravityScale());
                setEnemyState(EnemyBossState.JUMPING);
                enemyBossKnight_is_on_Ground = false;
                this.b2body.applyLinearImpulse(new Vector2(-2.5f, 5.0f), this.b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f);
                //enemyLeftGround();
                jumpIngbool = false;

            }
        }
    }

    public void enemyShortJumpTest(boolean directionTojump){

        if(directionTojump){
            if(enemyLife != 0) {
                //System.out.println("Jumping Gravity is then: " + b2body.getGravityScale());
                setEnemyState(EnemyBossState.JUMPING);
                enemyBossKnight_is_on_Ground = false;
                this.b2body.applyLinearImpulse(new Vector2(1.1f, 6.0f), this.b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f);
                //enemyLeftGround();
                jumpIngbool = false;
            }
        }else {
            if(enemyLife != 0) {
                //System.out.println("Jumping Gravity is then: " + b2body.getGravityScale());
                setEnemyState(EnemyBossState.JUMPING);
                enemyBossKnight_is_on_Ground = false;
                this.b2body.applyLinearImpulse(new Vector2(-1.1f, 6.0f), this.b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f);
                //enemyLeftGround();
                jumpIngbool = false;

            }
        }


    }



    public void enemyShortJumpTest(){
        //System.out.println("Jumping Gravity is then: " + b2body.getGravityScale());
        setEnemyState(EnemyBossState.JUMPING);
        //enemyBossKnight_is_on_Ground = false;
        this.b2body.applyLinearImpulse(new Vector2(-0.1f, 1.0f), this.b2body.getWorldCenter(), true); //Vector2(0, 4f) 5.6f);
        jumpIngbool = false;

    }

    /*  ToDo: Check all enemy and player on the same thing!!!
    Must have to State's one for actual frame time state where preFrameStateTime and currentFrameStateTime will sometime be the same!!
    but currentEnemyState and preEnemyState will be always be different so we can check the old and new state of the enemy!!
     */



    public TextureRegion getFrame(float dt) {

        //System.out.println("this enemy : " + this.enemyID + " state: " + getEnemyState() + " | preState: " + getpreEnemyState());

        /**
         * currentEnemyStateTime & prevEnemyStateTime= frameTime change
         *
         * stateTimer = currentEnemyStateTime == prevEnemyStateTime ? stateTimer + dt : 0;
         *         prevEnemyStateTime = currentEnemyStateTime;
         *
         *   But :  currentEnemyState & prevEnemyState is :
         *
         *   current SmallEnemyDef State and Previous Last State to be used with checks on SmallEnemyDef !!??
         */
        currentEnemyStateTime = currentEnemyState = getState();

        TextureRegion region = null;

        switch(currentEnemyStateTime) {//currentEnemyState) {
            case SET_PREVIOUS_STATE:
                region = knightStand; // no animation
                break;
            case SET_INACTIVE_STATE:
                region = knightStand; // no animation
                //region = fightAnimationClose.getKeyFrame(stateTime, true);
                break;
            case FALLING:
                region = knightStand; // no animation
                //region = fightAnimationClose.getKeyFrame(stateTime, true); // Animation
                break;
            case GUARDING:
                region = knightStand; // no animation
                //region = fightAnimationClose.getKeyFrame(stateTime, true);
                break;
            case FIGHTING_CLOSE:
                region = (TextureRegion) fightAnimationClose.getKeyFrame(stateTimer); //, false);

                closeAttackAnimationFinished.updateCurrentTime(dt);
                if(closeAttackAnimationFinished.getCurrentTime() > 0.4f){
                    enemyIsFighting_Close = false;
                    timeTo_ReDefineEnemy = true;
                    closeAttackAnimationFinished.resetCurrentTime();
                }
                /* old
                if (fightAnimationClose.isAnimationFinished(stateTimer)) {
                    //System.out.println("Fighting Close Animation is finished!!");
                    enemyIsFighting_Close = false;
                    timeTo_ReDefineEnemy = true;
                 }
                 */
                break;
            case FIGHTING_RANGE:
                region = (TextureRegion) fightAnimationRange.getKeyFrame(stateTimer); //, false);
                //System.out.println("EnemyKnightClass getFrame stateTimer: " + stateTimer );
                rangeAttackAnimationFinished.updateCurrentTime(dt);
                //System.out.println("EnemyKnightClass getFrame rangeAttackAnimationFinished: " + rangeAttackAnimationFinished.getCurrentTime() );
                if(rangeAttackAnimationFinished.getCurrentTime() > 0.4f){
                    fireRangeAttack();
                    rangeAttackAnimationFinished.resetCurrentTime();
                    enemyIsFighting_Range = false;
                    timeTo_ReDefineEnemy = true;
                }
                //if (fightAnimationRange.isAnimationFinished( (rangeAttackAnimationFinished.getCurrentTime() - 0.1f)) ) {
                    //System.out.println("Fighting Range Animation is finished!!");
                    //enemyAnimationFightingRangeFinished = true;
                    //fireRangeAttack();
                    //rangeAttackAnimationFinished.resetCurrentTime();
                    //enemyIsFighting_Range = false;
                    //timeTo_ReDefineEnemy = true;
                //}

                break;
            case FOLLOWING:
                region = knightStand; // no animation
                break;
            case JUMPING:
                region = knightStand; // no animation
                break;
            case HIT:
                //System.out.println("EnemyKnight Class getFrame: Hit ");
                region = (TextureRegion)getHitAnimation.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = knightStand; // no animation
                break;
        }


        //runningRightSwitchTimer.updateCurrentTime(dt);
        /*
        if( b2body.isActive() && currentEnemyState == EnemyBossState.JUMPING || currentEnemyState == EnemyBossState.FALLING){
            runningRightSwitchTimer.resetCurrentTime();
            System.out.println("timer is Reset!!");
        }
        */

        if( !(currentEnemyState == EnemyBossState.JUMPING) || !(currentEnemyState == EnemyBossState.FALLING)) {

            if ((b2body.getLinearVelocity().x < 0 || !runningRight) && region.isFlipX()) { // && enemyIsHit
                region.flip(true, false);
                runningRight = false;
            } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && !region.isFlipX()) { // && !enemyIsHit
                region.flip(true, false);
                runningRight = true;
            }
        }

        stateTimer = currentEnemyStateTime == prevEnemyStateTime ? stateTimer + dt : 0;
        prevEnemyStateTime = currentEnemyStateTime;
//System.out.println("prevStat: " + prevEnemyStateTime + " curStat: " + currentEnemyStateTime);
        return region;
/*
        if ( currentActionState == EnemyBossActionState.NO_ACTION ){



        }

        if ( currentActionState == EnemyBossActionState.NO_ACTION ){

            int action = 0;

            switch (action) {

                case 1:
                    action = 0; // No action, action finished - go out of switch!!
                    break;
                case 2:
                    action = 0; // No action, action finished - go out of switch!!
                    break;
            }
        }
*/
        // //currentEnemyState != EnemyBossState.JUMPING || currentEnemyState != EnemyBossState.FALLING ) {
        //if( currentEnemyState != EnemyBossState.JUMPING || currentEnemyState != EnemyBossState.FALLING) {

/*
            if( currentActionState != EnemyBossActionState.AI_JUMPING_ACTION ) {//b2body.getLinearVelocity().y == 0 && currentActionState != EnemyBossActionState.AI_JUMPING_ACTION ){


                System.out.println("isFlipX() Vel.x: " + b2body.getLinearVelocity().x + " Vel.y: " + b2body.getLinearVelocity().y);
                if ((b2body.getLinearVelocity().x < 0 || !runningRight) && region.isFlipX()) { // && enemyIsHit
                    region.flip(true, false);
                    runningRight = false;
                } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && !region.isFlipX()) { // && !enemyIsHit
                    region.flip(true, false);
                    runningRight = true;
                }

            }
*/
        //}

        /*
        if(b2body.isActive() && runningRightSwitchTimer.getCurrentTime() > 0.4 ){
            System.out.println("CurrentTime > 0.4");
            runningRightSwitchTimer.resetCurrentTime();
        }
        */


    }



    //working on !! ToDo : FIGTIHG STATE
    public EnemyBossState getState() {

        if (enemyIsDead) {
            //setEnemyState(EnemyBossState.DEAD);
            return EnemyBossState.DEAD;//getEnemyState();
        }else if(enemyIsFighting_Close) {
            //setEnemyState(EnemyBossState.FIGHTING_CLOSE);
            //System.out.println("EnemyKnight Class - enemyIsFighting_Close");
            return EnemyBossState.FIGHTING_CLOSE;//getEnemyState();
        }else if(enemyIsFighting_Range) {
            //System.out.println("getState: FIGHTING_RANGE!!");
                //setEnemyState(EnemyBossState.FIGHTING_RANGE);
            //System.out.println("EnemyKnight Class - enemyIsFighting_Range");
            return EnemyBossState.FIGHTING_RANGE;//getEnemyState();
        }else if(enemyIsHit){
            //setEnemyState(EnemyBossState.HIT);
            return EnemyBossState.HIT;//getEnemyState();
        }else if(b2body.getLinearVelocity().y < 0){
            //setEnemyState(EnemyBossState.FALLING);
            return EnemyBossState.FALLING;//getEnemyState();
        }else if(b2body.getLinearVelocity().y > 0){
            //setEnemyState(EnemyBossState.FOLLOWING);
            return EnemyBossState.JUMPING;//getEnemyState();
        }else if(b2body.getLinearVelocity().x != 0){
            //setEnemyState(EnemyBossState.FOLLOWING);
            return EnemyBossState.FOLLOWING;//getEnemyState();
        }else{
            //setEnemyState(EnemyBossState.GUARDING);
            return EnemyBossState.GUARDING; //getEnemyState();
        }
    }


    //TODO not sure about thia !!! ???
    public void setEnemyOnGround(boolean value){ enemyBossKnight_is_on_Ground = value; }
    //public void enemyLeftGround() { enemyBossKnight_is_on_Ground = false; }

    public boolean getEnemyKnightOnGround(){ return enemyBossKnight_is_on_Ground; }

    public void setIsDead(boolean status) { enemyIsDead = status;}

    public boolean isDead() { return enemyIsDead; }

    public int getEnemyLife(){ return this.enemyLife; }

    public void setIsHit(boolean status) { enemyIsHit = status; }

    public boolean isHit(){ return enemyIsHit; }

    public void setActiveGravity(float value){
        b2body.setGravityScale(value);
    }

    public void setActiveCloseAttack(boolean value){this.activeCloseAttack = value; }
    public void setActiveRangeAttack(boolean value){this.activeRangeAttack = value; }

    public boolean getActiveCloseAttackBoolean(){ return this.activeCloseAttack; }
    public boolean getActiveRangeAttackBoolean(){ return this.activeRangeAttack; }

    //public void setTriggerCloseAttackBol(boolean value) { triggerCloseAttackBol = value; }
    //public boolean getTriggerCloseAttackBol(){ return this.triggerCloseAttackBol; }

    public void draw(Batch batch){
        if(!destroyed || stateTimer < 4) { //1
            super.draw(batch);

            for (EnemyKnightRangeAttack ball : enemyKnightPowerSwordRangeAttacks)
                ball.draw(batch);

            // don't want texture on this one!!!
            //for(EnemyKnightPowerSword ball : enemyKnightPowerSwords)
            //    ball.draw(batch);


            for (BalloneBullet ball : testBalloneBullet)
                ball.draw(batch);

            //for(FireBall ball : enemyFireBallFrenzy)
            //    ball.draw(batch);


        }
    }

    @Override
    public void hitByEnemy(BossEnemyDef smallEnemyDef) {}

    @Override
    public void hitWithBullet(BalloneBullet bullet) { HitbyEnemy(bullet); }

    public void HitbyEnemy(BalloneBullet balloneBullet){
        if(!isHit()) { // if isHit = false, set true
            setIsHit(true);

            if( balloneBullet.getBalloneBulletDamageColor().equals("Green") ) {

                System.out.println("Boss hit Green player Power damage 1");
                enemyLife--;
                System.out.println("EnemyKnight Class - enemyIsHitByPlayerPower(Player) Life taken - have left! " + enemyLife);
            }

            if( balloneBullet.getBalloneBulletDamageColor().equals("Blue") ) {

                System.out.println("Boss hit Blue player Power  damage 2");
                enemyLife = enemyLife - 2;
                System.out.println("EnemyKnight Class - enemyIsHitByPlayerPower(Player) Life taken - have left! " + enemyLife);
            }

            /*
            if(balloneBullet.equals("Green")){
                this.enemyLife--;
                //System.out.println("Enemy: " + this.enemyID + " life: " + this.enemyLife);
            }
            if(balloneBullet.equals("Blue")){
                this.enemyLife = this.enemyLife - 2;
                //System.out.println("Enemy: " + this.enemyID + " life: " + this.enemyLife);
            }
            */
            //enemyLife--;
            //System.out.println("EnemyKnight Class - enemyIsHitByPlayerPower(Player) Life taken - have left! " + enemyLife);
        }
    }

    public void die() {
        //System.out.println("dead!!");
        if (!isDead()) {

            setIsDead(true);

            Filter filter = new Filter();
            filter.maskBits = GameUtility.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            b2body.setLinearVelocity(0f,0f);
            //b2body.setGravityScale(1.0f);
            b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);


        }
    }

    public boolean getEnemyBossIsDead(){ return this.enemyIsDead; }

    public boolean getIsDestroyed(){ return this.setToDestroy; }


    @Override
    public void closeAttack(BossEnemyDef bossEnemyDef) {

        if(activeCloseAttack) {
            //if(currentEnemyState != EnemyBossState.JUMPING || currentEnemyState != EnemyBossState.FALLING ) {
            //if(!jumpIngbool) {
            //b2body.setGravityScale(10.0f); // think this is good to have !!!
            //}else{
            //    b2body.setGravityScale(1.0f); // think this is good to have !!!
            //}
            timeTo_DefineEnemyTo_Fight_Close = true;
            enemyIsFighting_Close = true;
            //}
        }
    }

    @Override
    public void rangeAttack(BossEnemyDef bossEnemyDef){

        if(activeRangeAttack) {
            //System.out.println("rangeAttack is called!!!");
            //if(currentEnemyState != EnemyBossState.JUMPING || currentEnemyState != EnemyBossState.FALLING) {
            timeTo_DefineEnemyTo_Fight_Range = true;
            // set's getFrame - that set's the state.fightingRange
            // When false = animation is Finished.
            enemyIsFighting_Range = true;
            //}
        }
    }


    public void setDashAssociationNumber(String value){this.dashAssociationNumber = value; }
    public String getDashAssociationNumber(){ return this.dashAssociationNumber; }

    public void setRangeBallContactWithFloor() { powerSwordBallContactWithFloor = true;}

    public void setRangeAttackActiveBool(boolean value){ rangeAttackIsCalledFor = value;  }

    public boolean getRangeAttackActiveBool(){ return rangeAttackIsCalledFor;}

    // set from End Contact updateAllSpawnLifeFromEnemy
    public void setboolHaveFiredRange(){ boolHaveFiredRange = false;}

    @Override
    public void seekerAttack(BossEnemyDef smallEnemyDef){}

    @Override
    public void rangeAttackFrenzy(BossEnemyDef smallEnemyDef, boolean right) { }

    @Override
    public void frenzyAttack(BossEnemyDef smallEnemyDef, float seed){
        //setFireBallSeed(seed);
        //timeTo_DefineEnemyTo_Fight_Frenzy = true;
        //setEnemyState(EnemyBossState.FIGHTING);
    }

    @Override
    public void closeAttackEnd(BossEnemyDef smallEnemyDef) {
        //bolCloseAttack = false;
        //timeToRedefineGuarding = true;
        //timeToDefineFigthing = false;
        //setEnemyState(EnemyBossState.GUARDING);
    }

    public void setEnemyState(EnemyBossState state) {
        // So current state and pre state is the same!!
        if(!currentEnemyState.equals(state)) {
            this.prevEnemyState = currentEnemyState;
            this.currentEnemyState = state; // new state, but be for we set a new the old must be sett into history
        }
    }

    public void setEnemyActionState(String actionState){

        if(actionState.equals( EnemyBossActionState.NO_ACTION.toString() )){
            System.out.println("NO_ACTION TRUE");
            setEnemyActionState(EnemyBossActionState.NO_ACTION);
        }else if(actionState.equals( EnemyBossActionState.AI_JUMPING_ACTION.toString() )) {
            System.out.println("AI_JUMPING_ACTION TRUE");
            setEnemyActionState(EnemyBossActionState.AI_JUMPING_ACTION);
        }



        /*
        if( !currentActionState.equals(actionState)) {
            this.prevActionState = currentActionState;
            this.currentActionState = actionState; // new state, but be for we set a new the old must be sett into history
        }
        */
    }

    public void setEnemyActionState(EnemyBossActionState actionState){

        if(! currentActionState.equals(actionState)) {
            this.prevActionState = currentActionState;
            this.currentActionState = actionState; // new state, but be for we set a new the old must be sett into history
        }
    }

    public EnemyBossActionState getEnemyActionState(){return this.currentActionState; }


    public void setPreEnemyState(EnemyBossState state) { this.prevEnemyState = state; }
    public EnemyBossState getEnemyState(){ return this.currentEnemyState; }

    public String getEnemyStateToString(){ return this.currentEnemyState.toString(); }

    public EnemyBossState getpreEnemyState(){ return this.prevEnemyState;}

    public Vector2 getEnemyMapStartPosition() { return this.enemyMapStartPosition; }

    public Vector2 getEnemyWorldCenter(){ return this.b2body.getWorldCenter(); }

    //public float getEnemyPositonWhenfireRangeAttack(){ return this.b2body.getPosition().x; }

    @Override
    public int getEnemyID(){ return this.enemyID; }

    @Override
    public boolean getEnemyFaceDirection(){ return this.runningRight; }

    public void setIsRunningRight(Boolean r){ this.runningRight = r; }

    public boolean getIsRunningRight(){return this.runningRight; }

    public boolean getEnemySePlayer(){return enemySePlayer;}

    public Vector2 getEnemyLosRayFRONT(){return this.enemyLosRayFRONT;}

    public Vector2 getEnemyLosRayBACK(){return this.enemyLosRayBACK;}

    public Vector2 getEnemyLosRayUP(){return this.enemyLosRayUP;}

    public Vector2 getEnemyLosRayDOWN(){return this.enemyLosRayDOWN;}

    public Vector2 getEnemyCollisionFRONT(){return this.collisionFront;}

    public Vector2 getEnemyNormalFRONT(){return this.normalFront;}

    public Vector2 getEnemyCollisionBACK(){return this.collisionBack;}

    public Vector2 getEnemyNormalBACK(){return this.normalBack;}

    public Vector2 getEnemyCollisionUP(){return this.collisionUp;}

    public Vector2 getEnemyNormalUP(){return this.normalUp;}

    public Vector2 getEnemyCollisionDOWN(){return this.collisionDown;}

    public Vector2 getEnemyNormalDOWN(){return this.normalDown;}




    public PolygonShape getEnemyThreathErea() {return this.boxEnemyThreathErea;}

    public PolygonShape getEnemyCloseRangeAttackErea(){return this.boxCloseRangeArea;}
}